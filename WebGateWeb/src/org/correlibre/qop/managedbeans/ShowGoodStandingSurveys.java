package org.correlibre.qop.managedbeans;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

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
public class ShowGoodStandingSurveys {

	private List<Survey> surveys;
	
	@EJB
	private SrvSurveysEngineLocal srvSurveysEngine;
	
	@EJB
	private SrvUsersLocal srvUsers;
	
	@PostConstruct
	public void init(){
		
		List<SurveyState> ss = new ArrayList<SurveyState>();
		
		ss.add(SurveyState.CLOSED);
		ss.add(SurveyState.ENDED);
		
		setSurveys(srvSurveysEngine.getAllUserSurveys(ss));
		
		System.out.println("[ShowGoodStandingSurveys] surveys: "+surveys);
		
	}
	
	public String getValidationKey(Survey survey){
		
		String key = null;
		
		try {
			key = srvUsers.encrypt(""+survey.getId());
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return key;
		
	}
	
	public String getReferenceTime(Survey survey){
		
		String referenceTime = "";
		
		try {
			ConfigProperty showReferenceTimeProperty = srvSurveysEngine.getConfigProperty("showReferenceTime", survey.getSurveyStructure(), null);
			
			if(showReferenceTimeProperty != null && Boolean.parseBoolean(showReferenceTimeProperty.getValue())){
				
					SimpleDateFormat sdf = null; 
					
					
					ConfigProperty dateFormatProperty = srvSurveysEngine.getConfigProperty("dateFormat", survey.getSurveyStructure(), null);
					
					sdf = new SimpleDateFormat(dateFormatProperty.getValue());
					
					if(survey.getReferenceTime() != null)
						referenceTime = BeanUtil.convertCamelCase(sdf.format(survey.getReferenceTime()));
				
			}
		} catch (QopException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return referenceTime;
		
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

	public void setSurveys(List<Survey> surveys) {
		this.surveys = surveys;
	}

	public List<Survey> getSurveys() {
		return surveys;
	}
	
}
