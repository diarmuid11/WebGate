package org.correlibre.qop.managedbeans;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.correlibre.qop.domain.Answer;
import org.correlibre.qop.domain.ConfigProperty;
import org.correlibre.qop.domain.Question;
import org.correlibre.qop.domain.Survey;
import org.correlibre.qop.domain.SurveyState;
import org.correlibre.qop.domain.Validation;
import org.correlibre.qop.services.QopException;
import org.correlibre.qop.services.SrvSurveysEngineLocal;
import org.correlibre.qop.util.BeanUtil;

@ManagedBean
@ViewScoped
public class ShowBasicSurveys {

	private List<Survey> surveys;
	
	@EJB
	private SrvSurveysEngineLocal srvSurveysEngine;	
	
	@PostConstruct
	public void init(){
		
		List<SurveyState> ss = new ArrayList<SurveyState>();
		
		ss.add(SurveyState.FILLING);
		ss.add(SurveyState.NEW);
		
		if(srvSurveysEngine.isUserSurveyed())
			ss.add(SurveyState.ENDED);
			
		setSurveys(srvSurveysEngine.getAllUserSurveys(ss));
		
		System.out.println("[ShowBasicSurveys] surveys: "+surveys);
		
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
	
	public String getFeedRate(Survey survey){
		
		double feedRate = 0;
		double answersQuantity = 0;
		double questionsQuantity = 0;
		List<Question> questions = null;
		//List<Question> questions = survey.getSurveyStructure().getQuestions();
		
		NumberFormat formatter;
		formatter = new DecimalFormat("#.##");
		try {
			questions = srvSurveysEngine.getSurveyStructureQuestions(survey.getSurveyStructure());
			
			for (Question q : questions) {
				if(!q.equals(survey.getCurrentQuestion())){
					answersQuantity++;
				}else{
					break;
				}
					
			}
			
			questionsQuantity = questions.size();
			
			/*
			questionsQuantity = countQuestions(questions);
			
			List<Answer> answers = srvSurveysEngine.getAllSurveyAnswers(survey);
			
			if(answers != null && answers.size()>0)
				answersQuantity = answers.size();
			*/
			
			System.out.println("answersQuantity: "+answersQuantity);
			System.out.println("questionsQuantity: "+questionsQuantity);
			
			feedRate = (answersQuantity/questionsQuantity)*100;
			
			
		} catch (QopException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ""+formatter.format(feedRate)+"%";
		 
	}
	
	public double countQuestions(List<Question> questions){
		
		double c = 0;
		
		for (Question q : questions) {
			if(q.getQuestions() != null || q.getQuestions().size() == 0){
				
				for (Validation v : q.getValidations()) {
					if(v.getValidationType().getId().intValue() == 3){
						c++;
						break;
					}
				}
				
			}else{
				c += countQuestions(q.getQuestions());
			}
		}
		return c;
		
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
