package org.correlibre.qop.managedbeans;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import org.correlibre.qop.domain.Question;
import org.correlibre.qop.domain.Survey;
import org.correlibre.qop.domain.SurveyState;
import org.correlibre.qop.domain.SurveyStructure;
import org.correlibre.qop.domain.User;
import org.correlibre.qop.services.SrvSurveysAdmin;
import org.correlibre.qop.services.SrvSurveysEngineLocal;
import org.correlibre.qop.services.SrvUsersLocal;
import org.correlibre.qop.util.BeanUtil;

@ManagedBean
@ViewScoped
public class EditSurvey {
	
	@EJB
	private SrvSurveysEngineLocal srvSurveyEngine;
	
	@EJB
	private SrvUsersLocal srvUser;
	
	private Survey survey;
	
	private List<String> userList;
	private List<SurveyState> surveyStateList;
	private List<Question> surveyQuestionList;
	
	private String userLogin;
	private String surveyStateValue;
	private String questionId;
	
	private Date referenceTime;
	
	
	@PostConstruct
	public void init(){
		
		String surveyId = null;
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		
		userList = new ArrayList<String>();
		surveyQuestionList = new ArrayList<Question>();
		surveyStateList = new ArrayList<SurveyState>();
		
		try{
			
			System.out.println("########### [EditSurvey] init");
			
			surveyId = request.getParameter("surveyId");
			
			System.out.println("########### [EditSurvey] surveyId: "+surveyId);
			
			if (surveyId == null || surveyId.length() <= 0){
				
				surveyId = (String) FacesContext.getCurrentInstance()
				.getExternalContext().getFlash()
				.get("surveyId");
				
			}
			
			System.out.println("########### [EditSurvey] surveyId2: "+surveyId);
			
			if (surveyId != null && surveyId.length() > 0){
				
				userList = srvUser.getUsers();
				
				System.out.println("[EditSurvey] userList: "+userList);
				
				surveyStateList = Arrays.asList(SurveyState.values());
				
				survey = srvSurveyEngine.getSurvey(new Integer(surveyId));
				
				surveyQuestionList = survey.getSurveyStructure().getMainQuestions();
				
				userLogin = survey.getUserLogin();
				
				surveyStateValue = survey.getSurveyState().getName();
				
				System.out.println("########### [EditSurvey] surveyStateValue: "+surveyStateValue);
				
				questionId = survey.getCurrentQuestion().getId().toString();
				
				referenceTime = survey.getReferenceTime();
				
			}else{
				
				throw new Exception("No se ha recibido correctamente el identificador de la encuesta");
				
			}
		
		}catch(Exception e){
			BeanUtil.getInstance().addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		}
		
	}
	
	public List<SelectItem> getUserListItems(){
		
		return BeanUtil.getInstance().getUserAsSelectItems(userList);
		
	}
	
	public List<SelectItem> getSurveyStateListItems(){
		
		return BeanUtil.getInstance().getSurveyStatesAsSelectItems(surveyStateList);
		
	}
	
	public List<SelectItem> getQuestionListItems(){
		
		return BeanUtil.getInstance().getQuestionsOrderedAsSelectItems(surveyQuestionList);
		
	}
	
	public void update(ActionEvent ev){
		
		try{
			
			Question q = null;
			
			if(questionId != null && questionId.length() > 0){
				q = srvSurveyEngine.getQuestion(new Integer(questionId));
			}
			
			survey.setUserLogin(userLogin);
			survey.setSurveyState(SurveyState.fromString(surveyStateValue));
			survey.setCurrentQuestion(q);
			survey.setReferenceTime(referenceTime);
			
			survey.setModificationTime(new Date());
			
			srvSurveyEngine.updateSurvey(survey);
			
			BeanUtil.getInstance().addMessage(FacesMessage.SEVERITY_INFO, "La información de la encuesta ha sido actualizada");
			
		}catch(Exception e){
			BeanUtil.getInstance().addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		}
		
	}

	public String getUserLogin() {
		return userLogin;
	}

	public void setUserLogin(String userLogin) {
		this.userLogin = userLogin;
	}

	public String getSurveyStateValue() {
		return surveyStateValue;
	}

	public void setSurveyStateValue(String surveyStateValue) {
		this.surveyStateValue = surveyStateValue;
	}

	public String getQuestionId() {
		return questionId;
	}

	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}

	public Date getReferenceTime() {
		return referenceTime;
	}

	public void setReferenceTime(Date referenceTime) {
		this.referenceTime = referenceTime;
	}

	public Survey getSurvey() {
		return survey;
	}

	public void setSurvey(Survey survey) {
		this.survey = survey;
	}

}
