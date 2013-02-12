package org.correlibre.qop.managedbeans;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.servlet.http.HttpServletRequest;

import org.correlibre.qop.domain.ConfigProperty;
import org.correlibre.qop.domain.Survey;
import org.correlibre.qop.domain.SurveyState;
import org.correlibre.qop.services.QopException;
import org.correlibre.qop.services.SrvSurveysEngineLocal;
import org.correlibre.qop.services.SrvUsers;
import org.correlibre.qop.services.SrvUsersLocal;
import org.correlibre.qop.util.BeanUtil;

@ManagedBean
@ViewScoped
public class ShowGoodstandingSurvey {

	private Survey survey;
	
	@EJB
	private SrvSurveysEngineLocal srvSurveysEngine;
	
	@EJB
	private SrvUsersLocal srvUsers;
	
	private String verificationCode;
	
	private String endedTime;
	
	@PostConstruct
	public void init(){
		
		Flash f = FacesContext.getCurrentInstance().getExternalContext().getFlash();
		
		survey = (Survey) f.get("survey");
		System.out
				.println("##################   [ShowGoodstandingSurvey] survey: "
						+ survey);

		try {
			if (survey != null) {

				survey = srvSurveysEngine.getSurvey(survey.getId());

			} else {

				HttpServletRequest request = (HttpServletRequest) FacesContext
						.getCurrentInstance().getExternalContext().getRequest();

				String surveyId = request.getParameter("surveyId");

				System.out.println(surveyId);

				if (surveyId != null && surveyId.length() != 0) {

					survey = srvSurveysEngine.getSurvey(new Integer(surveyId));

				}

			}
			
			if (survey != null) {

				setVerificationCode(srvUsers.encrypt("" + survey.getId()));
				endedTime = formatTime(survey.getEndingTime());

			}
			
		} catch (NumberFormatException e) {

			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {

			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {

			e.printStackTrace();
		} catch (QopException e) {

			e.printStackTrace();
		}
		
	}
	
	public String getReferenceTime(){
		
		String referenceTime = "";
		
		try {
			ConfigProperty showReferenceTimeProperty = srvSurveysEngine.getConfigProperty("showReferenceTime", survey.getSurveyStructure(), null);
			
			SimpleDateFormat sdf = null; 
			
			if(showReferenceTimeProperty != null && Boolean.parseBoolean(showReferenceTimeProperty.getValue())){
					
					ConfigProperty dateFormatProperty = srvSurveysEngine.getConfigProperty("dateFormat", survey.getSurveyStructure(), null);
					
					sdf = new SimpleDateFormat(dateFormatProperty.getValue());
				
			}else{
				
				sdf = new SimpleDateFormat("dd/MM/yyyy");
				
			}
			
			referenceTime = BeanUtil.convertCamelCase(sdf.format(survey.getReferenceTime()));
		} catch (Exception e) {
			BeanUtil.getInstance().addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			e.printStackTrace();
		}
		
		return referenceTime;
		
	}
	
	public String formatTime(Date time){
		
		
		String timeStr =  null;
		SimpleDateFormat sdf = null; 
		
		//ConfigProperty dateFormatProperty = srvSurveysEngine.getConfigProperty("dateFormat", null);
		
		//sdf = new SimpleDateFormat(dateFormatProperty.getValue());
		
		sdf = new SimpleDateFormat("dd/MM/yyyy");
		
		if(time != null)
			timeStr = BeanUtil.convertCamelCase(sdf.format(time));
		
		return timeStr;
		
	}
	
	public String showAnswer(String key){
		
		String answerText = null;
		
		answerText = srvSurveysEngine.getSingleAnswerValue(survey, key);
		
		return answerText;
		
	}
	
	public boolean isEndedState(SurveyState s){
		return s.equals(SurveyState.ENDED);
	}
	
	public boolean isFillingState(SurveyState s){
		return s.equals(SurveyState.FILLING);
	}
	
	public boolean isNewState(SurveyState s){
		return s.equals(SurveyState.NEW);
	}

	public void setSurvey(Survey survey) {
		this.survey = survey;
	}

	public Survey getSurvey() {
		return survey;
	}

	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}

	public String getVerificationCode() {
		return verificationCode;
	}

	public void setEndedTime(String endedTime) {
		this.endedTime = endedTime;
	}

	public String getEndedTime() {
		return endedTime;
	}
	
}
