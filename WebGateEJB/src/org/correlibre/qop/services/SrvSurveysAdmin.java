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

import java.util.List;

import org.correlibre.qop.domain.Question;
import org.correlibre.qop.domain.QuestionType;
import org.correlibre.qop.domain.SurveyState;
import org.correlibre.qop.domain.SurveyStructure;
import org.correlibre.qop.domain.Validation;

public interface SrvSurveysAdmin {

	public abstract void createSurveyStructure(String surveyStructureName);

	public abstract void saveQuestion(Question question);

	public abstract List<QuestionType> getQuestionTypes();

	public abstract void reorderQuestion(SurveyStructure surveyStructure,
			Question question, boolean up, int steps) throws QopException;

	public abstract Question createQuestion(SurveyStructure surveyStructure,
			Question question, List<Question> editedSubQuestions)
			throws QopException;

	public abstract Question modifyQuestion(Question question,
			List<Question> editedSubQuestions, List<Validation> newValidations) throws QopException;

	public abstract Question getNextQuestion(Question question)
			throws QopException;

	public abstract Question getPreviousQuestion(Question question)
			throws QopException;

	public abstract void modifySurveyStructure(SurveyStructure ss);
	public abstract void modifySurveyStructureCreators(SurveyStructure ss, List<String> creatorUsers);	
	public abstract void deleteSurveyStructure(SurveyStructure surveyStructure)throws QopException;
	public abstract void editSurveyStructure(SurveyStructure ss) throws QopException;
	public abstract void deploySurveyStructure(SurveyStructure ss) throws QopException;
	public abstract void endSurveyStructure(SurveyStructure ss) throws QopException;
	public abstract void assignSurvey(SurveyStructure ss, String userLogin) throws QopException;
	public abstract List<Object> getUserSurveysDetail(String userName);
	public abstract Long getSurveys(String userName, SurveyState state);

	public abstract void addValidation(Question question, String validationTypeId);
	
	public abstract void associateSurveyStructure(SurveyStructure parent, SurveyStructure child)throws QopException;
	
	public abstract SurveyStructure createNewSurveyStructure(String surveyStructureName);


}