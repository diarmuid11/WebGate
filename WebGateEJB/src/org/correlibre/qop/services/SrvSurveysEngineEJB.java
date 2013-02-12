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
package org.correlibre.qop.services;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import javax.annotation.Resource;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.correlibre.qop.domain.Answer;
import org.correlibre.qop.domain.CategoryDomain;
import org.correlibre.qop.domain.CategoryOption;
import org.correlibre.qop.domain.ConfigProperty;
import org.correlibre.qop.domain.Jump;
import org.correlibre.qop.domain.Question;
import org.correlibre.qop.domain.Survey;
import org.correlibre.qop.domain.SurveyState;
import org.correlibre.qop.domain.SurveyStructure;
import org.correlibre.qop.domain.SurveyStructureState;
import org.correlibre.qop.domain.Validation;

/**
 * Session Bean implementation class SrvSurveysEngineEJB
 */

@Stateless
@DeclareRoles({ "ENCUESTADOR", "ADMINISTRADOR", "ENCUESTADO" })
public class SrvSurveysEngineEJB implements SrvSurveysEngineLocal {

	@PersistenceContext
	private EntityManager em;

	@EJB
	private SrvSurveysValidationLocal srvSurveysValidation;

	@Resource
	private SessionContext sessionContext;

	/**
	 * Default constructor.
	 */

	public SrvSurveysEngineEJB() {
	}

	@Override
	public List<Question> getSurveyStructureQuestions(SurveyStructure surveyStructure)
			throws QopException {
		String userLogin = sessionContext.getCallerPrincipal().getName();
		boolean admin = sessionContext.isCallerInRole("ADMINISTRADOR");
		return getSurveyStructureQuestions(surveyStructure, userLogin, admin);
	}

	@Override
	public List<Question> getSurveyStructureQuestions(SurveyStructure surveyStructure,
			String userLogin, boolean admin) throws QopException {
		Query q = em.createNamedQuery("Question.findMainQuestionsBySurveyStructure");
		q.setParameter("surveyStructure", surveyStructure);
		return (List<Question>) q.getResultList();
	}

	@Override
	public List<Question> getAllSurveyStructureQuestions(SurveyStructure surveyStructure)
			throws QopException {
		String userLogin = sessionContext.getCallerPrincipal().getName();
		boolean admin = sessionContext.isCallerInRole("ADMINISTRADOR");
		return getAllSurveyStructureQuestions(surveyStructure, userLogin, admin);
	}

	@Override
	public List<Question> getAllSurveyStructureQuestions(SurveyStructure surveyStructure,
			String userLogin, boolean admin) throws QopException {
		Query q = em.createNamedQuery("Question.findQuestionsBySurveyStructure");
		q.setParameter("surveyStructure", surveyStructure);
		return q.getResultList();
	}

	@Override
	public Survey initSurvey(Integer id) throws QopException {
		String userLogin = sessionContext.getCallerPrincipal().getName();
		boolean admin = sessionContext.isCallerInRole("ADMINISTRADOR");
		return initSurvey(id, userLogin, admin);
	}

	@Override
	public Survey updateSurvey(Survey s) throws QopException {
		return em.merge(s);
	}
	
	@Override
	public void refreshSurvey(Survey s) throws QopException {
		em.refresh(s);
	}

	@Override
	public Survey initSurvey(Integer id, String userLogin, boolean admin) throws QopException {
		SurveyStructure ss = getSurveyStructure(id, userLogin, admin);
		List<Question> questions = getSurveyStructureQuestions(ss);
		Survey s = new Survey();
		s.setSurveyStructure(ss);
		s.setUserLogin(userLogin);
		s.setSurveyState(SurveyState.FILLING);
		// TODO: Manejar caso en q la encuesta no empieza en la primera pregunta
		// y validar que la encuesta tenga por lo menos una pregunta
		s.setCurrentQuestion(questions.get(0));
		em.persist(s);
		return s;
	}
	
	@Override
	public Survey createSurvey(Integer id, String userLogin, Survey parent) throws QopException {
		SurveyStructure ss = getSurveyStructure(id);
		List<Question> questions = getSurveyStructureQuestions(ss);
		Survey s = new Survey();
		s.setSurveyStructure(ss);
		s.setUserLogin(userLogin);
		s.setSurveyState(SurveyState.NEW);
		s.setParentSurvey(parent);
		s.setCurrentQuestion(questions.get(0));
		
		em.persist(s);
		
		return s;
	}

	public Question calculateJump(Question q, Map<Question, String> answers) throws QopException {
		q = em.find(Question.class, q.getId()); // Para evitar LazyException
		for (Jump j : q.getJumps()) {
			if (srvSurveysValidation.evalExpression(j.getRule(), answers)) {
				return j.getNextQuestion();
			}
		}
		return null;
	}

	public Question calculateBackJump(Question q, Map<Question, String> answers)
			throws QopException {
		q = em.find(Question.class, q.getId()); // Para evitar LazyException
		for (Jump j : q.getBackJumps()) {
			if (srvSurveysValidation.evalExpression(j.getRule(), answers)) {
				return j.getQuestion();
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.correlibre.qop.services.SrvSurveysEngineLocal#getNextQuestion(org
	 * .correlibre.qop.domain.Question)
	 */
	public Question getNextQuestion(Survey survey, Question currentQuestion,
			Map<Question, String> answers) throws QopException {
		Question result = null;

		if (currentQuestion == null)
			return null;

		if (currentQuestion.getJumps().size() > 0) {
			result = calculateJump(currentQuestion, answers);

			if (result == null) {
				List<Answer> answersTmpList = getAllSurveyAnswers(survey);
				Map<Question, String> answersTmp = new HashMap<Question, String>();

				for (Answer answer : answersTmpList) {
					answersTmp.put(answer.getQuestion(), answer.getValue());
				}
				result = calculateJump(currentQuestion, answersTmp);

				if (result == null && survey.getParentSurvey() != null) {
					answersTmpList = getAllSurveyAnswers(survey.getParentSurvey());
					answersTmp = new HashMap<Question, String>();
					for (Answer answer : answersTmpList) {
						answersTmp.put(answer.getQuestion(), answer.getValue());
					}
					result = calculateJump(currentQuestion, answersTmp);
				}
			}

			if (result == null) {
				List<Question> questions = getSurveyStructureQuestions(survey.getSurveyStructure());
				int idx = questions.indexOf(currentQuestion);
				if (idx + 1 < questions.size()) {
					result = questions.get(idx + 1);
				}
			}

		} else {
			List<Question> questions = getSurveyStructureQuestions(survey.getSurveyStructure());
			int idx = questions.indexOf(currentQuestion);
			if (idx + 1 < questions.size()) {
				result = questions.get(idx + 1);
			}
		}
		/*
		 * if(survey.getSurveyState() != SurveyState.ENDED)
		 * survey.setCurrentQuestion(result);
		 */
		// em.merge(survey);

		return result;
	}

	/*
	 * public Question getPreviousQuestion(Survey survey, Question
	 * currentQuestion) throws QopException { Question result = currentQuestion;
	 * //Survey s=em.find(Survey.class, survey.getId()); List<Question>
	 * questions=getSurveyStructureQuestions(survey.getSurveyStructure()); int
	 * idx = questions.indexOf(currentQuestion); if (idx -1 >= 0){ result =
	 * questions.get(idx-1); survey.setCurrentQuestion(result);
	 * //em.merge(survey); } return result; }
	 */

	public Question getPreviousQuestion(Survey survey, Question currentQuestion,
			Map<Question, String> answers) throws QopException {
		Question result = null;

		if (currentQuestion == null)
			return null;

		if (currentQuestion.getBackJumps().size() > 0) {
			result = calculateBackJump(currentQuestion, answers);

			if (result == null) {
				List<Answer> answersTmpList = getAllSurveyAnswers(survey);
				Map<Question, String> answersTmp = new HashMap<Question, String>();

				for (Answer answer : answersTmpList) {
					answersTmp.put(answer.getQuestion(), answer.getValue());
				}
				result = calculateBackJump(currentQuestion, answersTmp);

				if (result == null && survey.getParentSurvey() != null) {
					answersTmpList = getAllSurveyAnswers(survey.getParentSurvey());
					answersTmp = new HashMap<Question, String>();
					for (Answer answer : answersTmpList) {
						answersTmp.put(answer.getQuestion(), answer.getValue());
					}
					result = calculateBackJump(currentQuestion, answersTmp);
				}
			}

			if (result == null) {
				List<Question> questions = getSurveyStructureQuestions(survey.getSurveyStructure());
				int idx = questions.indexOf(currentQuestion);
				if (idx - 1 >= 0) {
					result = questions.get(idx - 1);
					// survey.setCurrentQuestion(result);
					// em.merge(survey);
				}
			}

		} else {
			List<Question> questions = getSurveyStructureQuestions(survey.getSurveyStructure());
			int idx = questions.indexOf(currentQuestion);
			if (idx - 1 >= 0) {
				result = questions.get(idx - 1);
				// survey.setCurrentQuestion(result);
				// em.merge(survey);
			}
		}
		/*
		 * if(survey.getSurveyState() != SurveyState.ENDED)
		 * survey.setCurrentQuestion(result);
		 */
		// em.merge(survey);

		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.correlibre.qop.services.SrvSurveysEngineLocal#getComplementaryQuestions
	 * (org.correlibre.qop.domain.Question)
	 */
	// TODO: Manejar con key para hacer independiente del id consecutivo
	public List<Question> getSubQuestions(Question question, Integer questionTypeId) {
		if (questionTypeId == null) {
			Query q = em.createNamedQuery("Question.findSubQuestions");
			q.setParameter("question", question);
			return q.getResultList();
		} else {
			Query q = em.createNamedQuery("Question.findSubQuestionsByQuestionTypeId");
			q.setParameter("question", question);
			q.setParameter("questionTypeId", questionTypeId);
			return q.getResultList();
		}
	}

	@Override
	public List<SurveyStructure> getParentSurveyStructures() {
		String userLogin = sessionContext.getCallerPrincipal().getName();
		boolean admin = sessionContext.isCallerInRole("ADMINISTRADOR");
		return getParentSurveyStructures(userLogin, admin);
	}

	@Override
	public List<SurveyStructure> getSurveyStructures() {
		String userLogin = sessionContext.getCallerPrincipal().getName();
		boolean admin = sessionContext.isCallerInRole("ADMINISTRADOR");
		return getSurveyStructures(userLogin, admin);
	}

	@Override
	public List<SurveyStructure> getParentSurveyStructures(String user, boolean admin) {
		if (admin) {
			Query q = em.createNamedQuery("SurveyStructure.findAllParents");
			return q.getResultList();
		} else {
			return getSurveyStructuresByState(SurveyStructureState.DEPLOYED, user, admin);
		}
	}

	@Override
	public List<SurveyStructure> getSurveyStructures(String user, boolean admin) {
		if (admin) {
			Query q = em.createNamedQuery("SurveyStructure.findAll");
			return q.getResultList();
		} else {
			return getSurveyStructuresByState(SurveyStructureState.DEPLOYED, user, admin);
		}
	}

	@Override
	public List<CategoryDomain> getAllCategoryDomains() {

		Query q = em.createNamedQuery("CategoryDomain.findAll");
		return q.getResultList();

	}

	@Override
	public List<SurveyStructure> getSurveyStructuresByState(SurveyStructureState sss) {
		String userLogin = sessionContext.getCallerPrincipal().getName();
		boolean admin = sessionContext.isCallerInRole("ADMINISTRADOR");
		return getSurveyStructuresByState(sss, userLogin, admin);
	}

	@Override
	public List<SurveyStructure> getSurveyStructuresByState(SurveyStructureState sss, String user,
			boolean admin) {
		Query q;
		if (admin) {
			q = em.createNamedQuery("SurveyStructure.findAllByState");
		} else {
			q = em.createNamedQuery("SurveyStructure.findByCreatorUserAndState");
			q.setParameter("userLogin", user);
		}
		q.setParameter("state", sss);
		return q.getResultList();
	}

	@Override
	public List<SurveyStructure> getSurveyStructuresBy2State(SurveyStructureState sss1,
			SurveyStructureState sss2) {
		String userLogin = sessionContext.getCallerPrincipal().getName();
		boolean admin = sessionContext.isCallerInRole("ADMINISTRADOR");
		return getSurveyStructuresBy2State(sss1, sss2, userLogin, admin);
	}

	@Override
	public List<SurveyStructure> getSurveyStructuresBy2State(SurveyStructureState sss1,
			SurveyStructureState sss2, String user, boolean admin) {
		Query q;
		if (admin) {
			q = em.createNamedQuery("SurveyStructure.findBy2State");
			q.setParameter("state1", sss1);
			q.setParameter("state2", sss2);
		} else {
			q = em.createNamedQuery("SurveyStructure.findByCreatorUserAndState");
			q.setParameter("userLogin", user);
			q.setParameter("state", sss1);
		}

		return q.getResultList();
	}

	@Override
	// TODO: Metodo redundante, ver getSurveys()
	public List<Object> getSurveysCount() {
		String userLogin = sessionContext.getCallerPrincipal().getName();
		Query q = em.createNamedQuery("Survey.findInstancesCountByStructure");
		q.setParameter("userLogin", userLogin);
		return q.getResultList();
	}

	@Override
	// TODO: Metodo redundante, ver getSurveys()
	public List<Survey> getAllUserSurveys() {
		String userLogin = sessionContext.getCallerPrincipal().getName();
		Query q = em.createNamedQuery("Survey.findByUser");
		q.setParameter("userLogin", userLogin);
		List<Survey> surveys = q.getResultList();

		return surveys;
	}

	public List<Survey> getAllParentUserSurveys() {
		String userLogin = sessionContext.getCallerPrincipal().getName();
		Query q;
		boolean admin = sessionContext.isCallerInRole("ADMINISTRADOR");

		List<SurveyState> ssList = new ArrayList<SurveyState>();
		ssList = Arrays.asList(SurveyState.values());

		/*
		 * if(state == null) ssList = Arrays.asList(SurveyState.values()); else
		 * ssList.add(state);
		 */

		if (admin) {
			q = em.createNamedQuery("Survey.findParentByState");

		} else {
			q = em.createNamedQuery("Survey.findParentByUserAndState");
			q.setParameter("userLogin", userLogin);
		}
		q.setParameter("surveyState", ssList);

		List<Survey> surveys = q.getResultList();

		return surveys;
	}

	public List<Survey> getAllParentSurveys(SurveyState ss, SurveyStructure surveyStructure) {
		String userLogin = sessionContext.getCallerPrincipal().getName();
		Query q;
		boolean admin = sessionContext.isCallerInRole("ADMINISTRADOR");

		List<SurveyState> ssList = new ArrayList<SurveyState>();
		// ssList = Arrays.asList(SurveyState.values());

		if (ss == null)
			ssList = Arrays.asList(SurveyState.values());
		else
			ssList.add(ss);

		if (admin) {
			q = em.createNamedQuery("Survey.findParentBySurveyStructureAndState");

		} else {
			q = em.createNamedQuery("Survey.findParentBySurveyStructureUserAndState");
			q.setParameter("userLogin", userLogin);
		}
		q.setParameter("surveyState", ssList);
		q.setParameter("surveyStructure", surveyStructure);

		List<Survey> surveys = q.getResultList();

		return surveys;
	}
	
	public List<Survey> getAllParentSurveys(SurveyState ss, SurveyStructure surveyStructure, Question question, String value) {
		String userLogin = sessionContext.getCallerPrincipal().getName();
		Query q;
		boolean admin = sessionContext.isCallerInRole("ADMINISTRADOR");

		List<SurveyState> ssList = new ArrayList<SurveyState>();
		// ssList = Arrays.asList(SurveyState.values());

		if (ss == null)
			ssList = Arrays.asList(SurveyState.values());
		else
			ssList.add(ss);

		if (admin) {
			q = em.createNamedQuery("Survey.findParentBySurveyStructureAndState-idQuestion");
			q.setParameter("question", question);
			q.setParameter("searchText", value+"%");

		} else {
			q = em.createNamedQuery("Survey.findParentBySurveyStructureUserAndState-idQuestion");
			q.setParameter("userLogin", userLogin);
			q.setParameter("question", question);
			q.setParameter("searchText", value);
		}
		q.setParameter("surveyState", ssList);
		q.setParameter("surveyStructure", surveyStructure);

		List<Survey> surveys = q.getResultList();

		return surveys;
	}
	
	
	public List<Survey> getAllParentSurveys(List<SurveyState> ss, SurveyStructure surveyStructure, Question question, String value) {
		String userLogin = sessionContext.getCallerPrincipal().getName();
		Query q;

		List<SurveyState> ssList = null;
		
		if (ss == null)
			ssList = Arrays.asList(SurveyState.values());
		else
			ssList = ss;

		q = em.createNamedQuery("Survey.findParentBySurveyStructureAndState-idQuestion");
		q.setParameter("question", question);
		q.setParameter("searchText", value);		
		q.setParameter("surveyState", ssList);
		q.setParameter("surveyStructure", surveyStructure);

		List<Survey> surveys = q.getResultList();

		return surveys;
	}
	

	public List<Survey> getAllUserSurveys(SurveyState state) {
		String userLogin = sessionContext.getCallerPrincipal().getName();
		Query q = em.createNamedQuery("Survey.findByUserAndState");

		List<SurveyState> ssList = new ArrayList<SurveyState>();

		ssList.add(state);

		q.setParameter("userLogin", userLogin);
		q.setParameter("surveyState", ssList);
		List<Survey> surveys = q.getResultList();

		return surveys;
	}

	public List<Survey> getAllUserSurveys(List<SurveyState> ssList) {
		String userLogin = sessionContext.getCallerPrincipal().getName();
		Query q = em.createNamedQuery("Survey.findByUserAndState");

		q.setParameter("userLogin", userLogin);
		q.setParameter("surveyState", ssList);
		List<Survey> surveys = q.getResultList();

		return surveys;
	}

	public List<Survey> getAllParentUserSurveys(SurveyState state) {
		String userLogin = sessionContext.getCallerPrincipal().getName();
		Query q = em.createNamedQuery("Survey.findParentByUserAndState");

		List<SurveyState> ssList = new ArrayList<SurveyState>();

		if (state == null)
			ssList = Arrays.asList(SurveyState.values());
		else
			ssList.add(state);

		q.setParameter("userLogin", userLogin);
		q.setParameter("surveyState", state);
		List<Survey> surveys = q.getResultList();

		return surveys;
	}

	public List<Survey> getAllParentUserSurveys(List<SurveyState> ssList) {
		String userLogin = sessionContext.getCallerPrincipal().getName();
		Query q = em.createNamedQuery("Survey.findParentByUserAndState");

		q.setParameter("userLogin", userLogin);
		q.setParameter("surveyState", ssList);
		List<Survey> surveys = q.getResultList();

		return surveys;
	}

	public SurveyStructure getSurveyStructure(Integer id) throws QopException {
		String userLogin = sessionContext.getCallerPrincipal().getName();
		boolean admin = sessionContext.isCallerInRole("ADMINISTRADOR");
		return getSurveyStructure(id, userLogin, admin);
	}

	public SurveyStructure getSurveyStructure(Integer id, String userLogin, boolean admin)
			throws QopException {
		SurveyStructure ss = em.find(SurveyStructure.class, id);
		if (ss == null) {
			throw new QopException("No se encontró Modelo de Encuesta con Id: " + id);
		}
		
		if (admin || ss.getCreatorUsers().contains(userLogin)) {
			return ss;
		}
		
		throw new QopException("Usuario: " + userLogin
				+ " no tiene privilegios para acceder a Modelo de Encuesta con Id: " + id);
	}

	private void persistAnswer(Survey s, Question question, Map<Question, String> answers,
			boolean delete) {
		Query q = em.createNamedQuery("Answer.findBySurveyAndQuestion");
		q.setParameter("survey", s);
		q.setParameter("question", question);
		String answer = answers.get(question);
		if (answer != null) {
			try {
				Answer a = (Answer) q.getSingleResult();
				a.setValue(answer);
				em.merge(a);
			} catch (NoResultException e) {
				Answer a = new Answer();
				a.setSurvey(s);
				a.setQuestion(question);
				a.setValue(answer.trim());
				em.persist(a);
				if (s.getAnswers() == null)
					s.setAnswers(new ArrayList<Answer>());
				s.getAnswers().add(a);
			}
		} else if (delete) {
			try {
				Answer a = (Answer) q.getSingleResult();
				em.remove(a);
			} catch (NoResultException e) {
			}
		}
		for (Question subQ : getSubQuestions(question, null)) {
			persistAnswer(s, subQ, answers, true);
		}

	}

	public Map<Validation, String> setAnswers(Survey s, Question question,
			Map<Question, String> answers, String user) throws QopException {
		Map<Validation, String> valsResults = new HashMap<Validation, String>();

		srvSurveysValidation.validateQuestion(question, answers, valsResults);
		System.out.println("######### [SrvSurveyEngineEJB] valsResults: " + valsResults);
		if (valsResults.size() == 0) {
			// Modifica o guarda respuesta pregunta principal
			// TODO: optimize
			persistAnswer(s, question, answers, false);
			return null;
		} else {
			return valsResults;
		}
	}

	@Override
	public List<Survey> getSurveys(SurveyStructure surveyStructure, String userLogin, boolean admin) {
		Query q;

		if (admin) {
			q = em.createNamedQuery("Survey.findBySurveyStructure"); 
			q.setParameter("surveyStructure", surveyStructure);
		} else {
			q = em.createNamedQuery("Survey.findBySurveyStructureAndUser");
			q.setParameter("surveyStructure", surveyStructure);
			q.setParameter("userLogin", userLogin);
		}
		return q.getResultList();
	}
	
	@Override
	public List<Survey> getSubSurveys(SurveyStructure surveyStructure, ArrayList<SurveyState> ssList, Survey parentSurvey) {
		Query q;

		q = em.createNamedQuery("Survey.findChildrenBySurveyStructureAndState");
		q.setParameter("surveyStructure", surveyStructure);
		q.setParameter("surveyState", ssList);
		q.setParameter("parentSurvey", parentSurvey);
		
		return q.getResultList();
	}

	@Override
	public List<Survey> getSurveys(SurveyStructure surveyStructure, SurveyState state,
			String userLogin, boolean admin) {
		Query q;
 
		List<SurveyState> ssList = new ArrayList<SurveyState>();

		if (state == null)
			ssList = Arrays.asList(SurveyState.values());
		else
			ssList.add(state);

		if (admin) {
			q = em.createNamedQuery("Survey.findBySurveyStructureAndState");
			q.setParameter("surveyStructure", surveyStructure);
			q.setParameter("surveyState", ssList);
		} else {
			q = em.createNamedQuery("Survey.findBySurveyStructureStateAndUser");
			q.setParameter("surveyStructure", surveyStructure);
			q.setParameter("surveyState", ssList);
			q.setParameter("userLogin", userLogin);
		}
		return q.getResultList();
	}

	@Override
	public List<Survey> getSurveys(SurveyStructure surveyStructure) {
		String userLogin = sessionContext.getCallerPrincipal().getName();
		boolean admin = sessionContext.isCallerInRole("ADMINISTRADOR");
		return getSurveys(surveyStructure, userLogin, admin);
	}

	@Override
	public List<Survey> getSurveys(Integer surveyStructureId, String stateName) {

		SurveyStructure ss = null;

		if (surveyStructureId != null)
			ss = em.find(SurveyStructure.class, surveyStructureId);

		SurveyState state = SurveyState.fromString(stateName);

		String userLogin = sessionContext.getCallerPrincipal().getName();
		boolean admin = sessionContext.isCallerInRole("ADMINISTRADOR");
		return getSurveys(ss, state, userLogin, admin);
	}

	@Override
	public void changeSurveysOwner(SurveyStructure surveyStructure, SurveyState surveyState,
			String userLogin, String newOwner) {
		Query q;
		q = em.createNamedQuery("Survey.findBySurveyStructureStateAndUser");
		q.setParameter("surveyStructure", surveyStructure);
		q.setParameter("surveyState", surveyState);
		q.setParameter("userLogin", userLogin);

		List<Survey> surveys = q.getResultList();
		for (Survey s : surveys) {
			s.setUserLogin(newOwner);
			em.merge(s);
		}
	}

	@Override
	public Survey getSurvey(Integer id) throws QopException {
		String userLogin = sessionContext.getCallerPrincipal().getName();
		boolean admin = sessionContext.isCallerInRole("ADMINISTRADOR");
		Survey s = em.find(Survey.class, id);

		// TODO: Es el dueno o el currentUser?
		if (admin || s.getUserLogin().equals(userLogin)) {
			return s;
		}
		throw new QopException("Usuario no tiene privilegios para acceder a " + "esta encuesta");
	}

	@Override
	public Map<Question, String> getAnswers(Survey s, Question question, String user) {
		Map<Question, String> answers = new HashMap<Question, String>();
		fillAnswerMap(answers, s, question, user);
		return answers;
	}

	@Override
	public Answer getSingleAnswer(Survey s, Question question) {

		try {
			Query q = em.createNamedQuery("Answer.findBySurveyAndQuestion");
			q.setParameter("survey", s);
			q.setParameter("question", question);
			return (Answer) q.getSingleResult();

		} catch (NoResultException e) {
			return null;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	@Override
	public String getSingleAnswerValue(Survey s, String questionKey) {

		Answer answer = null;
		String value = null;

		System.out.println("s: " + s);
		System.out.println("questionKey: " + questionKey);

		try {
			Query q = em.createNamedQuery("Answer.findBySurveyAndQuestionKey");
			q.setParameter("survey", s);
			q.setParameter("key", questionKey);
			answer = (Answer) q.getSingleResult();

			System.out.println("answer: " + answer);

			value = fillAnswerValue(answer);

		} catch (NoResultException e) {
			if (s.getParentSurvey() != null) {
				try {
					Query q = em.createNamedQuery("Answer.findBySurveyAndQuestionKey");
					q.setParameter("survey", s.getParentSurvey());
					q.setParameter("key", questionKey);
					answer = (Answer) q.getSingleResult();

					System.out.println("parent answer: " + answer);

					value = fillAnswerValue(answer);

				} catch (NoResultException e3) {
					return null;

				} catch (Exception e2) {
					e.printStackTrace();
					return null;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return value;

	}

	private String fillAnswerValue(Answer a) {

		String value = null;

		System.out.println("a.getId(): " + a.getId());

		switch (a.getQuestion().getQuestionType().getId()) {
		case 1:
		case 8:
		case 9:
		case 14:
		case 15:
		case 16:
		case 20:
			value = a.getValue();
			break;
		case 2:
		case 11:
			Question q = this.getQuestion(new Integer(a.getValue()));
			value = q.getText();

			break;

		default:
			value = null;
			break;
		}

		return value;

	}

	private void fillAnswerMap(Map<Question, String> map, Survey s, Question question, String user) {
		Query q = em.createNamedQuery("Answer.findBySurveyAndQuestion");
		q.setParameter("survey", s);
		q.setParameter("question", question);
		List<Answer> la = q.getResultList();
		for (Answer a : la) {
			map.put(a.getQuestion(), a.getValue());
		}
		for (Question subq : getSubQuestions(question, null)) {
			fillAnswerMap(map, s, subq, user);
		}
	}

	public List<Answer> getAllSurveyAnswers(Survey s) {
		Query q = em.createNamedQuery("Answer.findBySurvey");
		q.setParameter("survey", s);
		return q.getResultList();
	}

	@Override
	public Question getQuestion(Integer id) {
		return em.find(Question.class, id);
	}

	@Override
	@RolesAllowed("ADMINISTRADOR")
	public Question getQuestion(SurveyStructure surveyStructure, String key) {
		Query q = em.createNamedQuery("Question.findQuestionBySurveyStructureAndKey");
		q.setParameter("surveyStructure", surveyStructure);
		q.setParameter("key", key);
		return (Question) q.getSingleResult();
	}

	@Override
	public List<CategoryOption> getCategoryOptions(CategoryOption co) {

		Query q = em.createNamedQuery("CategoryOption.findChildOptions");
		q.setParameter("parentCategoryOption", co);

		return (List<CategoryOption>) q.getResultList();

	}

	public CategoryDomain getCategoryDomain(Integer id) throws QopException {

		return em.find(CategoryDomain.class, id);

	}

	public CategoryOption getCategoryOption(Integer id) throws QopException {

		return em.find(CategoryOption.class, id);

	}

	public boolean isUserAdmin() {
		return sessionContext.isCallerInRole("ADMINISTRADOR");
	}

	public boolean isUserSurveyed() {
		return sessionContext.isCallerInRole("ENCUESTADO");
	}

	public ConfigProperty getConfigProperty(String key, SurveyStructure surveyStructure, Question question) throws QopException {

		Query q = null;
		ConfigProperty cp = null;
		
		if(key == null)
			throw new QopException("La clave no puede ser vacía");
		else{
			
			if(question != null){
				
				q = em.createNamedQuery("ConfigProperty.findByKey-Question");
				q.setParameter("key", key);
				q.setParameter("question", question);
				
				try {
					List<ConfigProperty> listConfigProperty = q.getResultList();
					
					if(listConfigProperty != null && listConfigProperty.size() > 0){
						cp = (ConfigProperty) listConfigProperty.get(0);
						return cp;
					}
				} catch (NoResultException e) {
					System.out.println("Sin resultado por Question");
				}
				
			}
			
			if(surveyStructure != null){
				
				q = em.createNamedQuery("ConfigProperty.findByKey-SurveyStructure");
				q.setParameter("key", key);
				q.setParameter("surveyStructure", surveyStructure);
				
				try {
					
					List<ConfigProperty> listConfigProperty = q.getResultList();
					
					if(listConfigProperty != null && listConfigProperty.size() > 0){
						cp = (ConfigProperty) listConfigProperty.get(0);
						
						if(cp.getQuestion() != null)
							return null;
						
						return cp;
					}
					
				} catch (NoResultException e) {
					System.out.println("Sin resultado por SurveyStructure");
				}
				
			}
				
			q = em.createNamedQuery("ConfigProperty.findByKey");
			q.setParameter("key", key);
			
			try {
				List<ConfigProperty> listConfigProperty = q.getResultList();
				
				if(listConfigProperty != null && listConfigProperty.size() > 0){
					cp = (ConfigProperty) listConfigProperty.get(0);
					
					if(cp.getSurveyStructure() != null || cp.getQuestion() != null)
						return null;
					
					return cp;
				}
				return cp;
			} catch (NoResultException e) {
				System.out.println("Sin resultado definitivo");
			}
			
		}
		

		/*
		try {
			cp = (ConfigProperty) q.getSingleResult();
		} catch (NoResultException e) {
			if (surveyStructure != null) {
				q.setParameter("surveyStructure", null);
				try {
					cp = (ConfigProperty) q.getSingleResult();
				} catch (NoResultException e2) {
					cp = null;
				}
			}
			
		}*/
		
		return cp;

	}

}
