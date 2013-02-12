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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.correlibre.qop.domain.Answer;
import org.correlibre.qop.domain.CategoryDomain;
import org.correlibre.qop.domain.CategoryOption;
import org.correlibre.qop.domain.ConfigProperty;
import org.correlibre.qop.domain.Question;
import org.correlibre.qop.domain.Survey;
import org.correlibre.qop.domain.SurveyState;
import org.correlibre.qop.domain.SurveyStructure;
import org.correlibre.qop.domain.SurveyStructureState;
import org.correlibre.qop.domain.Validation;


public interface SrvSurveysEngine {

	public abstract Question getQuestion(SurveyStructure surveyStructure,String key);
	
	public abstract Question getQuestion(Integer id);

	public abstract Question getNextQuestion(Survey survey,
			Question currentQuestion,Map<Question,String> answers) throws QopException;

	public abstract Question getPreviousQuestion(Survey survey,
			Question currentQuestion, Map<Question,String> answers) throws QopException;

	public abstract Survey initSurvey(Integer id) throws QopException;

	public abstract Survey initSurvey(Integer id, String user,boolean admin) throws QopException;

	public abstract List<SurveyStructure> getSurveyStructures();
	
	public abstract List<SurveyStructure> getSurveyStructures(String user, boolean admin);

	public abstract SurveyStructure getSurveyStructure(Integer id)throws QopException;
	
	public abstract SurveyStructure getSurveyStructure(Integer id, String user,boolean admin)throws QopException;
	
	public abstract List<Survey> getSurveys(SurveyStructure surveyStructure);
	
	public abstract List<Survey> getSurveys(SurveyStructure surveyStructure, String userLogin, boolean admin);	

	public abstract Survey getSurvey(Integer id)throws QopException;	

	public List<Question> getSurveyStructureQuestions(SurveyStructure surveyStructure,
			String userLogin, boolean admin) throws QopException;

	public List<Question> getSurveyStructureQuestions(SurveyStructure surveyStructure) throws QopException;

	public abstract Map<Validation, String> setAnswers(Survey s,Question question, Map<Question, String> answers,
			String user) throws QopException;
	
	public abstract Map<Question, String> getAnswers(Survey s, Question question, String user);

	public abstract List<Question> getSubQuestions(Question currentQuestion,
			Integer questionTypeId);

	public abstract void changeSurveysOwner(SurveyStructure surveyStructure,
			SurveyState surveyState, String userLogin, String newOwner);
	
	public abstract List<Object> getSurveysCount();

	public abstract List<Survey> getAllUserSurveys();
	public abstract List<Survey> getAllParentUserSurveys();
	
	public abstract List<Survey> getAllUserSurveys(SurveyState state);
	public abstract List<Survey> getAllParentUserSurveys(SurveyState state);
	
	public abstract List<Survey> getSurveys(SurveyStructure surveyStructure, SurveyState state,String userLogin,boolean admin);
	public abstract List<Survey> getSurveys(Integer surveyStructureId, String stateName);
	List<SurveyStructure> getSurveyStructuresByState(SurveyStructureState state);
	List<SurveyStructure> getSurveyStructuresByState(SurveyStructureState state,String user, boolean admin);
	public abstract List<Question> getAllSurveyStructureQuestions(SurveyStructure surveyStructure) throws QopException;
	public abstract List<Question> getAllSurveyStructureQuestions(SurveyStructure surveyStructure,String userLogin,boolean admin) throws QopException;
	public abstract List<Answer> getAllSurveyAnswers(Survey s);
	
	List<SurveyStructure> getSurveyStructuresBy2State(SurveyStructureState state1, SurveyStructureState state2);
	List<SurveyStructure> getSurveyStructuresBy2State(SurveyStructureState state1, SurveyStructureState state2,String user, boolean admin);
	
	public abstract List<CategoryDomain> getAllCategoryDomains();
	
	public abstract List<CategoryOption> getCategoryOptions(CategoryOption co);
	
	public abstract Survey updateSurvey(Survey s) throws QopException;
	
	public abstract void refreshSurvey(Survey s) throws QopException;
	
	public abstract CategoryDomain getCategoryDomain(Integer id) throws QopException;
	
	public abstract CategoryOption getCategoryOption(Integer id) throws QopException;

	public abstract List<SurveyStructure> getParentSurveyStructures();

	public abstract List<SurveyStructure> getParentSurveyStructures(String user, boolean admin);
	
	public abstract List<Survey> getAllParentSurveys(SurveyState ss, SurveyStructure surveyStructure);
	
	public abstract List<Survey> getAllParentSurveys(SurveyState ss, SurveyStructure surveyStructure, Question question, String value);
	
	public abstract List<Survey> getAllParentUserSurveys(List<SurveyState> ssList);
	
	public abstract boolean isUserAdmin();
	
	public abstract boolean isUserSurveyed();
	
	public abstract Answer getSingleAnswer(Survey s, Question question);
	
	public ConfigProperty getConfigProperty(String key, SurveyStructure surveyStructure, Question question) throws QopException;
	
	public abstract String getSingleAnswerValue(Survey s, String questionKey);
	
	public abstract List<Survey> getAllUserSurveys(List<SurveyState> ssList);
	
	public abstract Survey createSurvey(Integer id, String userLogin, Survey parent) throws QopException;
	
	public abstract List<Survey> getSubSurveys(SurveyStructure surveyStructure, ArrayList<SurveyState> ssList, Survey parentSurvey);
	
	public abstract List<Survey> getAllParentSurveys(List<SurveyState> ss, SurveyStructure surveyStructure, Question question, String value);
	
}