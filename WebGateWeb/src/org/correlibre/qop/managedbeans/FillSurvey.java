/*****************************************************************************
Copyright (C) 2012  
diarmuid julian.rolon@gmail.com
Gloria Patricia Meneses gpmeneses@gmail.com
OScar Puentes oskarj84@gmail.com

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *************************************************************************/
package org.correlibre.qop.managedbeans;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.correlibre.qop.domain.Answer;
import org.correlibre.qop.domain.ConfigProperty;
import org.correlibre.qop.domain.Question;
import org.correlibre.qop.domain.Survey;
import org.correlibre.qop.domain.SurveyState;
import org.correlibre.qop.domain.Validation;
import org.correlibre.qop.fillviewadapters.FillViewAdapter;
import org.correlibre.qop.services.QopException;
import org.correlibre.qop.services.SrvSurveysEngineLocal;
import org.correlibre.qop.util.BeanUtil;
import org.correlibre.qop.util.WebDynamicClassLoader;

@ManagedBean(name = "fillSurvey")
@ViewScoped
public class FillSurvey {

	@EJB
	private SrvSurveysEngineLocal srvSurveysEngine;

	private Survey parentSurvey;

	private Survey survey;

	private Question question;

	private FillViewAdapter fillViewAdapter;

	private Map<Question, FillViewAdapter> fillViewAdapters;

	private boolean nextEnabled;

	private boolean previousEnabled;

	private boolean referenceTimeEnabled;

	private String referenceTime;

	private boolean hasIdentifyQuestion;

	private String identityQuestionText;

	private String answerIdentityQuestionText;

	private boolean hasParentIdentifyQuestion;

	private String parentIdentityQuestionText;

	private String parentAnswerIdentityQuestionText;

	private boolean admin;

	@PostConstruct
	public void init() {
		// TODO: Pasar objetos no ids
		try {

			HttpServletRequest request = (HttpServletRequest) FacesContext
					.getCurrentInstance().getExternalContext().getRequest();

			admin = request.isUserInRole("ADMINISTRADOR");

			String surveyStructureId = request
					.getParameter("surveyStructureId");
			String surveyId = request.getParameter("surveyId");
			String parentSurveyId = request.getParameter("parentSurveyId");

			System.out.println("####### [FillSurvey] entrySet(): "
					+ FacesContext.getCurrentInstance().getExternalContext()
							.getFlash().entrySet());

			if (surveyStructureId == null || surveyStructureId.length() == 0) {
				surveyStructureId = (String) FacesContext.getCurrentInstance()
						.getExternalContext().getFlash()
						.get("surveyStructureId");
			}

			if (parentSurveyId == null || parentSurveyId.length() == 0) {
				parentSurveyId = (String) FacesContext.getCurrentInstance()
						.getExternalContext().getFlash().get("parentSurveyId");
			}

			if (surveyId == null || surveyId.length() == 0) {
				surveyId = (String) FacesContext.getCurrentInstance()
						.getExternalContext().getFlash().get("surveyId");
			}

			System.out.println("############# [FillSurvey] surveyId: "
					+ surveyId);
			System.out.println("############# [FillSurvey] parentSurveyId: "
					+ parentSurveyId);
			System.out.println("############# [FillSurvey] surveyStructureId: "
					+ surveyStructureId);

			if (parentSurveyId != null && parentSurveyId.length() > 0) {
				parentSurvey = srvSurveysEngine.getSurvey(new Integer(
						parentSurveyId));
			}

			Question previousQuestion = null;
			Question nextQuestion = null;

			if (surveyStructureId != null) {

				if (surveyStructureId.length() > 0
						&& !"null".equalsIgnoreCase(surveyStructureId)) {
					survey = srvSurveysEngine.initSurvey(new Integer(
							surveyStructureId));
					question = survey.getCurrentQuestion();

					System.out
							.println("################# [FillSurvey] survey: "
									+ survey);
					System.out
							.println("################# [FillSurvey] question: "
									+ question);

					if (parentSurvey != null) {
						survey.setParentSurvey(parentSurvey);
						parentSurvey.getSubSurveys().add(survey);
						srvSurveysEngine.updateSurvey(parentSurvey);
					}

					if (!admin) {
						survey.setCreationTime(new Date());
						survey.setSurveyState(SurveyState.FILLING);
						srvSurveysEngine.updateSurvey(survey);
					}
				} else {
					throw new QopException(
							"El codigo del formato no llego correctamente");
				}
			} else {

				if(surveyId==null || "null".equalsIgnoreCase(surveyId) || "".equalsIgnoreCase(surveyId)){
					BeanUtil.getInstance().addMessage(FacesMessage.SEVERITY_ERROR, "No hay identificador de encuesta para continuar");
					return;
				}
				
				survey = srvSurveysEngine.getSurvey(new Integer(surveyId));
				parentSurvey = survey.getParentSurvey();
				question = survey.getCurrentQuestion();

				System.out.println("############# [FillSurvey] question: "
						+ question);
				System.out.println("############# [FillSurvey] parentSurvey: "
						+ parentSurvey);

				if (question == null) {
					List<Question> questions = srvSurveysEngine
							.getSurveyStructureQuestions(survey
									.getSurveyStructure());
					question = questions.get(0);
				}

				// boolean admin =
				// sessionContext.isCallerInRole("ADMINISTRADOR");

				if (!admin) {
					if (survey.getSurveyState().equals(SurveyState.NEW)) {
						survey.setSurveyState(SurveyState.FILLING);
						survey.setModificationTime(new Date());
						srvSurveysEngine.updateSurvey(survey);
					}
				}

			}

			System.out.println("############# [FillSurvey] question: "
					+ question);

			initFillViewAdapters();

			System.out
					.println("################# [FillSurvey] fillViewAdapter: "
							+ fillViewAdapter);

			Map<Question, String> answers = fillViewAdapter.getAnswer();

			System.out.println("################# [FillSurvey] answers: "
					+ answers);

			previousQuestion = srvSurveysEngine.getPreviousQuestion(survey,
					question, answers);
			nextQuestion = srvSurveysEngine.getNextQuestion(survey, question,
					answers);

			System.out
					.println("################# [FillSurvey] previousQuestion: "
							+ previousQuestion);
			System.out.println("################# [FillSurvey] nextQuestion: "
					+ nextQuestion);

			previousEnabled = checkPreviousEnabled(previousQuestion);
			nextEnabled = checkNextEnabled(nextQuestion);

			referenceTimeEnabled = false;

			ConfigProperty showReferenceTimeProperty = srvSurveysEngine
					.getConfigProperty("showReferenceTime",
							survey.getSurveyStructure(), null);

			if (showReferenceTimeProperty != null
					&& Boolean.parseBoolean(showReferenceTimeProperty
							.getValue())) {

				SimpleDateFormat sdf = null;

				setReferenceTimeEnabled(true);

				ConfigProperty dateFormatProperty = srvSurveysEngine
						.getConfigProperty("dateFormat",
								survey.getSurveyStructure(), null);

				sdf = new SimpleDateFormat(dateFormatProperty.getValue());

				setReferenceTime(BeanUtil.convertCamelCase(sdf.format(survey
						.getReferenceTime())));

			}

			identifyIdetityQuestion();

		} catch (NumberFormatException e) {
			BeanUtil.getInstance().addMessage(FacesMessage.SEVERITY_ERROR, 
					"El id no corresponde a un numero");
			e.printStackTrace();

		} catch (QopException e) {
			BeanUtil.getInstance().addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			e.printStackTrace();
			System.out.println("############ [FillSurvey] e.getMessage(): "
					+ e.getMessage());

		} catch (Exception e) {
			BeanUtil.getInstance().addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			e.printStackTrace();
			System.out.println("############ [FillSurvey] e.getMessage(): "
					+ e.getMessage());

		}
	}

	private void identifyIdetityQuestion() {

		if (answerIdentityQuestionText == null
				|| (answerIdentityQuestionText != null && answerIdentityQuestionText
						.length() == 0)) {

			Question idQuestion = survey.getSurveyStructure()
					.getIdentifyQuestion();

			hasIdentifyQuestion = idQuestion != null;

			if (hasIdentifyQuestion) {
				identityQuestionText = idQuestion.getText();
				answerIdentityQuestionText = BeanUtil
						.convertCamelCase(getAnswerIdentifyQuestion(survey));
				System.out
						.println("################ [FillSurvey] answerIdentityQuestionText: "
								+ answerIdentityQuestionText);
			}

			hasParentIdentifyQuestion = false;

			if (parentSurvey != null) {

				if (parentAnswerIdentityQuestionText == null
						|| (parentAnswerIdentityQuestionText != null && parentAnswerIdentityQuestionText
								.length() == 0)) {

					Question idParentQuestion = parentSurvey
							.getSurveyStructure().getIdentifyQuestion();

					System.out
							.println("################ [FillSurvey]  parentSurvey.getSurveyStructure().getIdentifyQuestion(): "
									+ parentSurvey.getSurveyStructure()
											.getIdentifyQuestion());

					hasParentIdentifyQuestion = idParentQuestion != null;

					System.out
							.println("################ [FillSurvey]  hasParentIdentifyQuestion: "
									+ hasParentIdentifyQuestion);

					if (hasParentIdentifyQuestion) {
						parentIdentityQuestionText = idParentQuestion.getText();
						parentAnswerIdentityQuestionText = BeanUtil
								.convertCamelCase(getAnswerIdentifyQuestion(parentSurvey));
						System.out
								.println("################ [FillSurvey] parentAnswerIdentityQuestionText: "
										+ parentAnswerIdentityQuestionText);
					}
				}

			}
		}

	}

	public Survey getCompleteParentSurvey() {

		Survey parentSurvey = null;

		try {

			parentSurvey = this.srvSurveysEngine.getSurvey(survey
					.getParentSurvey().getId());
			System.out
					.println("########################## [EvaluateSurvey] parentSurvey.getSubSurveys(): "
							+ parentSurvey.getSubSurveys());
			List<Survey> subS = parentSurvey.getSubSurveys();

			parentSurvey.setAnswers(this.srvSurveysEngine
					.getAllSurveyAnswers(parentSurvey));

			for (Survey surveys : subS) {
				surveys.setAnswers(this.srvSurveysEngine
						.getAllSurveyAnswers(surveys));
			}

			return parentSurvey;

		} catch (Exception e) {
			
			
			BeanUtil.getInstance().addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			e.printStackTrace();
			return null;
		}

	}

	private void addMessages(Map<Validation, String> valsResult) {
		BeanUtil bu = BeanUtil.getInstance();
		for (Entry<Validation, String> val : valsResult.entrySet()) {
			// TODO: Volver recurso y usar StringBuffer
			// String message = "Error: ";
			String message = "";
			// message += val.getKey().getValidationType().getName() + " :";
			message += val.getValue();
			bu.addMessage(FacesMessage.SEVERITY_ERROR, message);

		}
	}

	private boolean saveAndValidate() throws Exception {

		System.out.println("######## [FillSurvey] fillViewAdapter: "
				+ fillViewAdapter);

		Map<Question, String> answers = fillViewAdapter.getAnswer();

		if (validateUniqueAnswers(answers)) {

			List<Answer> surveyAnswers = srvSurveysEngine
					.getAllSurveyAnswers(survey);

			for (Answer answer : surveyAnswers) {

				if (!answers.containsKey(answer.getQuestion())) {
					answers.put(answer.getQuestion(), answer.getValue());
				}
			}

			System.out.println("######### [FillSurvey] answers: " + answers);

			Map<Validation, String> valsResult = srvSurveysEngine.setAnswers(
					survey, question, answers, null);

			System.out.println("######### [FillSurvey] valsResult: "
					+ valsResult);

			if (valsResult != null) {
				addMessages(valsResult);
				return false;
			} else {
				// BeanUtil.getInstance().addMessage("La respuesta se ha guardado correctamente");
				return true;
			}
		} else {
			return false;
		}
	}

	private boolean validateUniqueAnswers(Map<Question, String> answers)
			throws Exception {

		if (survey.getSurveyStructure().getIdentifyQuestion() != null
				&& survey.getSurveyStructure().getIdentifyQuestion() == question) {

			String actualAnswer = answers.get(question);

			List<Survey> surveys = null;
			boolean foundAnswer = false;

			Answer actualSavedAnswer = srvSurveysEngine.getSingleAnswer(survey,
					question);

			System.out
					.println("######### [FillSurvey] actualSavedAnswer.getValue(): "
							+ (actualSavedAnswer == null ? null
									: actualSavedAnswer.getValue()));
			System.out.println("######### [FillSurvey] actualAnswer: "
					+ actualAnswer);

			if (actualAnswer.equalsIgnoreCase((actualSavedAnswer == null ? null
					: actualSavedAnswer.getValue())))
				return true;

			if (survey.getParentSurvey() != null) {

				List<Survey> tmpSurveys = survey.getParentSurvey()
						.getSubSurveys();

				surveys = new ArrayList<Survey>();

				for (Survey sur : tmpSurveys) {
					if (sur.getSurveyState() != SurveyState.DELETED)
						surveys.add(sur);
				}

				for (Survey sur : surveys) {
					Question q = sur.getSurveyStructure().getIdentifyQuestion();
					Answer ans = srvSurveysEngine.getSingleAnswer(sur, q);

					if (ans != null
							&& ans.getValue().equalsIgnoreCase(actualAnswer)) {
						foundAnswer = true;
						break;
					}

				}

			} else {

				System.out.println("ISPARENT ");

				long inicio = new Date().getTime();

				List<SurveyState> ssList = new ArrayList<SurveyState>();

				ssList.add(SurveyState.NEW);
				ssList.add(SurveyState.CLOSED);
				ssList.add(SurveyState.ENDED);
				ssList.add(SurveyState.FILLING);

				// ssList = Arrays.asList(SurveyState.values());
				// ssList = Arrays.asList(SurveyState.values());

				// ssList.remove(SurveyState.DELETED);

				System.out.println("ssList: " + ssList);
				System.out.println("survey.getSurveyStructure(): "
						+ survey.getSurveyStructure());
				System.out.println("question: " + question);
				System.out.println("actualAnswer: " + actualAnswer);

				surveys = srvSurveysEngine.getAllParentSurveys(ssList,
						survey.getSurveyStructure(), question, actualAnswer);

				/*
				 * List<Survey> tmpSurveys =
				 * srvSurveysEngine.getSurveys(survey.getSurveyStructure(),
				 * null, true);
				 * 
				 * surveys = new ArrayList<Survey>();
				 * 
				 * for (Survey sur : tmpSurveys) { if(sur.getSurveyState() !=
				 * SurveyState.DELETED) surveys.add(sur); }
				 */

				for (Survey sur : surveys) {
					Question q = sur.getSurveyStructure().getIdentifyQuestion();
					Answer ans = srvSurveysEngine.getSingleAnswer(sur, q);

					if (ans != null
							&& ans.getValue().equalsIgnoreCase(actualAnswer)) {
						foundAnswer = true;
						break;
					}

				}

				long fin = new Date().getTime();

				System.out.println("Tiempo utilizado: " + (fin - inicio));

			}

			if (foundAnswer) {
				BeanUtil.getInstance().addMessage(
						FacesMessage.SEVERITY_ERROR,
						"Ya existe una encuesta con el mismo "
								+ question.getText() + " " + actualAnswer);
				return false;
			}

		}

		return true;

	}

	public String save() {
		try {
			System.out.println("############# [FillSurvey] save");
			if (saveAndValidate())
				BeanUtil.getInstance().addMessage(
						"La respuesta se ha guardado correctamente");

		} catch (Exception e) {
			BeanUtil.getInstance().addMessage(FacesMessage.SEVERITY_ERROR,
					"Error: " + e.getMessage());
			e.printStackTrace();

		}
		return null;
	}

	public String saveAndGoToNextQuestion() {
		long inicio = System.currentTimeMillis();
		System.out.println("inicio: " + inicio);
		long despuesSaveAndValidate = 0;
		long despuesHabilitarBotones = 0;
		long antesEvaluate1 = 0;
		long antesEvaluate2 = 0;
		try {
			Map<Question, String> answers = fillViewAdapter.getAnswer();
			if (!saveAndValidate()) {
				return null;
			}

			despuesSaveAndValidate = System.currentTimeMillis();
			System.out.println("despuesSaveAndValidate: "
					+ despuesSaveAndValidate);

			question = srvSurveysEngine.getNextQuestion(survey, question,
					answers);
			Question previousQuestion = srvSurveysEngine.getPreviousQuestion(
					survey, question, answers);
			Question nextQuestion = srvSurveysEngine.getNextQuestion(survey,
					question, answers);

			despuesHabilitarBotones = System.currentTimeMillis();
			System.out.println("despuesHabilitarBotones: "
					+ despuesHabilitarBotones);

			if (question == null) {
				survey.setSurveyState(SurveyState.ENDED);
				survey.setEndingTime(new Date());
				survey.setCurrentQuestion(null);
				srvSurveysEngine.updateSurvey(survey);

				System.out
						.println("[FillSurvey] srvSurveysEngine.isUserSurveyed(): "
								+ srvSurveysEngine.isUserSurveyed());

				if (srvSurveysEngine.isUserSurveyed()) {

					ConfigProperty closeSurveyOnEnd = srvSurveysEngine
							.getConfigProperty("closeSurveyOnEnd",
									survey.getSurveyStructure(), null);

					System.out.println("[FillSurvey] closeSurveyOnEnd: "
							+ closeSurveyOnEnd);

					if (closeSurveyOnEnd != null
							&& Boolean
									.parseBoolean(closeSurveyOnEnd.getValue()) == false) {

						survey.setSurveyState(SurveyState.ENDED);

					} else {

						survey.setSurveyState(SurveyState.CLOSED);

					}

					srvSurveysEngine.updateSurvey(survey);
					FacesContext.getCurrentInstance().getExternalContext()
							.getFlash().put("survey", survey);

					return "show_goodstanding_survey.xhtml";

				} else {

					if (survey.getSurveyStructure()
							.getChildrenSurveyStructures().size() > 0) {
						// survey.setAnswers(this.srvSurveysEngine.getAllSurveyAnswers(survey));
						FacesContext.getCurrentInstance().getExternalContext()
								.getFlash().put("survey", survey);
						antesEvaluate1 = System.currentTimeMillis();
						System.out.println("antesEvaluate1: " + antesEvaluate1);
						return "evaluate_survey.xhtml";

					} else if (survey.getParentSurvey() != null) {
						// survey.setAnswers(this.srvSurveysEngine.getAllSurveyAnswers(survey));
						FacesContext.getCurrentInstance().getExternalContext()
								.getFlash().put("survey", survey);

						FacesContext.getCurrentInstance().getExternalContext()
								.getFlash()
								.put("parentSurvey", survey.getParentSurvey());

						antesEvaluate2 = System.currentTimeMillis();
						System.out.println("antesEvaluate2: " + antesEvaluate2);

						return "evaluate_survey.xhtml";
					} else
						return "show_surveys.xhtml";
				}

			} else {
				survey.setCurrentQuestion(question);
				srvSurveysEngine.updateSurvey(survey);
			}
			initFillViewAdapters();

			previousEnabled = checkPreviousEnabled(previousQuestion);
			nextEnabled = checkNextEnabled(nextQuestion);

		} catch (QopException e) {
			BeanUtil.getInstance().addMessage(FacesMessage.SEVERITY_ERROR,
					e.getMessage());
			e.printStackTrace();

		} catch (Exception e) {
			BeanUtil.getInstance().addMessage(FacesMessage.SEVERITY_ERROR,
					e.getMessage());
			e.printStackTrace();

		}

		return null;
	}

	public boolean checkNextEnabled(Question nextQ) {

		if (nextQ != null)
			return true;
		else
			return false;

	}

	public boolean isPreviousEnabled() {

		return previousEnabled;

	}

	public boolean checkPreviousEnabled(Question previousQ) {

		if (previousQ != null)
			return true;
		else
			return false;

	}

	public String saveAndGoToPreviousQuestion() {

		try {
			Map<Question, String> answers = fillViewAdapter.getAnswer();
			if (!saveAndValidate()) {
				return null;
			}

			question = srvSurveysEngine.getPreviousQuestion(survey, question,
					answers);
			Question previousQuestion = srvSurveysEngine.getQuestion(question
					.getId());
			Question nextQuestion = srvSurveysEngine.getNextQuestion(survey,
					question, answers);

			survey.setCurrentQuestion(question);
			srvSurveysEngine.updateSurvey(survey);
			if (question == null) {
				return "show_surveys";
			}

			initFillViewAdapters();

			previousEnabled = checkPreviousEnabled(previousQuestion);
			nextEnabled = checkNextEnabled(nextQuestion);

		} catch (QopException e) {
			e.printStackTrace();
			BeanUtil.getInstance().addMessage(e.getMessage());
		} catch (Exception e) {
			BeanUtil.getInstance().addMessage(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	public String goToPreviousQuestion() {
		try {
			Map<Question, String> answers = fillViewAdapter.getAnswer();

			question = srvSurveysEngine.getPreviousQuestion(survey, question,
					answers);
			Question previousQuestion = srvSurveysEngine.getPreviousQuestion(
					survey, question, answers);
			Question nextQuestion = srvSurveysEngine.getNextQuestion(survey,
					question, answers);

			previousEnabled = checkPreviousEnabled(previousQuestion);
			nextEnabled = checkNextEnabled(nextQuestion);

			if (question == null)
				return "show_surveys.xhtml";
			initFillViewAdapters();
		} catch (QopException e) {
			BeanUtil.getInstance().addMessage(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	public String goToNextQuestion() {
		Map<Question, String> answers = fillViewAdapter.getAnswer();

		try {

			System.out.println("########## [FillSurvey] answers: " + answers);
			System.out.println("########## [FillSurvey] survey: " + survey);
			System.out.println("########## [FillSurvey] question: " + question);

			question = srvSurveysEngine.getNextQuestion(survey, question,
					answers);
			Question previousQuestion = srvSurveysEngine.getPreviousQuestion(
					survey, question, answers);
			Question nextQuestion = srvSurveysEngine.getNextQuestion(survey,
					question, answers);

			previousEnabled = checkPreviousEnabled(previousQuestion);
			nextEnabled = checkNextEnabled(nextQuestion);

			if (question == null)
				return "show_surveys";
			else {
				if (survey.getSurveyState() != SurveyState.ENDED)
					survey.setCurrentQuestion(question);
			}
			initFillViewAdapters();
		} catch (QopException e) {
			BeanUtil.getInstance().addMessage(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	private void initFillViewAdapters() throws QopException {
		fillViewAdapters = new HashMap<Question, FillViewAdapter>();
		Map<Question, String> currentAnswers = srvSurveysEngine.getAnswers(
				survey, question, null);
		initFillViewAdapters(question, currentAnswers, true);

		identifyIdetityQuestion();
	}

	private FillViewAdapter initFillViewAdapters(Question q,
			Map<Question, String> currentAnswers, boolean rootQuestion)
			throws QopException {

		FillViewAdapter fillViewAdapter = null;
		if (q.getQuestionType().getFillViewAdapter() != null) {
			Timestamp inicio = new Timestamp((new Date()).getTime());
			fillViewAdapter = WebDynamicClassLoader.loadClass(q
					.getQuestionType().getFillViewAdapter());
			if (rootQuestion) {
				this.fillViewAdapter = fillViewAdapter;
			}

			fillViewAdapter.setQuestion(q);
			fillViewAdapter.setSrvSurveysEngine(srvSurveysEngine);
			fillViewAdapter.init();
			fillViewAdapter.setAnswer(currentAnswers);
			fillViewAdapters.put(q, fillViewAdapter);
			Timestamp fin = new Timestamp((new Date()).getTime());
			
			// System.out.println("############# [FillSurvey] this.fillViewAdapter:"+this.fillViewAdapter);

			System.out.println("##### [FillSurvey] initCreationViewAdapters ("
					+ q.getQuestionType().getFillViewAdapter() + ") time: "
					+ (fin.getTime() - inicio.getTime()));
		}

		List<Question> subQuestions = srvSurveysEngine.getSubQuestions(q, null);
		Map<Question,FillViewAdapter> subAdapters= new HashMap<Question, FillViewAdapter>();
		for (Question qst : subQuestions) {
			FillViewAdapter aux=initFillViewAdapters(qst, currentAnswers, false);
			if (aux!=null){
				subAdapters.put(qst, aux);
			}
		}
		if (fillViewAdapter!=null){
			fillViewAdapter.setSubFillViewAdapters(subAdapters);
		}
		return fillViewAdapter;

	}

	public void open() {
		try {
			survey.setSurveyState(SurveyState.FILLING);

			// List<Question> questions =
			// srvSurveysEngine.getSurveyStructureQuestions(survey.getSurveyStructure());

			survey.setCurrentQuestion(question);

			this.srvSurveysEngine.updateSurvey(survey);
		} catch (QopException e) {
			e.printStackTrace();
			BeanUtil.getInstance().addMessage(e.getMessage());
		}
	}

	public boolean isEndedState() {
		if (survey != null)
			return survey.getSurveyState().equals(SurveyState.ENDED);
		else
			return false;
	}

	public String getAnswerIdentifyQuestion(Survey survey) {

		Answer ans = srvSurveysEngine.getSingleAnswer(survey, survey
				.getSurveyStructure().getIdentifyQuestion());

		if (ans != null)
			return ans.getValue();
		else
			return "";
	}

	public Survey getSurvey() {
		return survey;
	}

	public Question getQuestion() {
		return question;
	}

	public String getDisplayViewComponent() {
		// System.out.println("question.getQuestionType().getDisplayViewComponent(): "+question.getQuestionType().getDisplayViewComponent());
		return question.getQuestionType().getDisplayViewComponent();
	}

	public FillViewAdapter getFillViewAdapter() {
		return fillViewAdapter;
	}

	public FillViewAdapter locateFillViewAdapter(Question question) {
		System.out.println("INFO: [FillSurvey] locating fillAdapter for: "+question.getText()+fillViewAdapters);
		System.out.println(fillViewAdapters.get(question));
		return fillViewAdapters.get(question);
	}

	public void setParentSurvey(Survey parentSurvey) {
		this.parentSurvey = parentSurvey;
	}

	public Survey getParentSurvey() {
		return parentSurvey;
	}

	public boolean isNextEnabled() {
		return nextEnabled;

	}

	public void setReferenceTimeEnabled(boolean referenceTimeEnabled) {
		this.referenceTimeEnabled = referenceTimeEnabled;
	}

	public boolean isReferenceTimeEnabled() {
		return referenceTimeEnabled;
	}

	public void setReferenceTime(String referenceTime) {
		this.referenceTime = referenceTime;
	}

	public String getReferenceTime() {
		return referenceTime;
	}

	public boolean isHasIdentifyQuestion() {
		return hasIdentifyQuestion;
	}

	public void setHasIdentifyQuestion(boolean hasIdentifyQuestion) {
		this.hasIdentifyQuestion = hasIdentifyQuestion;
	}

	public String getIdentityQuestionText() {
		return identityQuestionText;
	}

	public void setIdentityQuestionText(String identityQuestionText) {
		this.identityQuestionText = identityQuestionText;
	}

	public void setAnswerIdentityQuestionText(String answerIdentityQuestionText) {
		this.answerIdentityQuestionText = answerIdentityQuestionText;
	}

	public String getAnswerIdentityQuestionText() {
		return answerIdentityQuestionText;
	}

	public boolean isHasParentIdentifyQuestion() {
		return hasParentIdentifyQuestion;
	}

	public void setHasParentIdentifyQuestion(boolean hasParentIdentifyQuestion) {
		this.hasParentIdentifyQuestion = hasParentIdentifyQuestion;
	}

	public String getParentIdentityQuestionText() {
		return parentIdentityQuestionText;
	}

	public void setParentIdentityQuestionText(String parentIdentityQuestionText) {
		this.parentIdentityQuestionText = parentIdentityQuestionText;
	}

	public String getParentAnswerIdentityQuestionText() {
		return parentAnswerIdentityQuestionText;
	}

	public void setParentAnswerIdentityQuestionText(
			String parentAnswerIdentityQuestionText) {
		this.parentAnswerIdentityQuestionText = parentAnswerIdentityQuestionText;
	}

	public boolean isRefreshable() {

		return (this.question.getJumps() != null && this.question.getJumps()
				.size() == 0);

	}
}