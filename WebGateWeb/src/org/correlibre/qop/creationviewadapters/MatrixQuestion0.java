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
package org.correlibre.qop.creationviewadapters;

import java.util.ArrayList;
import java.util.List;

import javax.faces.event.ActionEvent;

import org.correlibre.qop.domain.Question;
import org.correlibre.qop.domain.QuestionType;
import org.correlibre.qop.services.QopException;

public class MatrixQuestion0 extends BaseCreationViewAdapter {

	protected Question rowListQuestion;
	protected Question colListQuestion;
	
	protected List<Question> rowListOptions;
	protected List<Question> colListOptions;
	
	protected String newRowOption;
	protected String newColOption;
	
	protected String questionType;
	
	protected String rowListTitle;
	protected String colListTitle;

	public MatrixQuestion0() throws QopException {
		super();
		
	}

	public void init() {
		
		System.out.println("############### [MatrixQuestion] creating: "+creating);
		if (creating){
			questionType = "1";
			rowListTitle = "";
			colListTitle = "";
			
			rowListOptions =  new ArrayList<Question>();
			rowListQuestion = new Question();
			QuestionType qt=new QuestionType();
			qt.setId(17);
			rowListQuestion.setText(rowListTitle);
			rowListQuestion.setQuestionType(qt);
			rowListQuestion.setQuestions(rowListOptions);
			rowListQuestion.setQuestion(question);
			
			colListOptions =  new ArrayList<Question>();
			colListQuestion = new Question();
			QuestionType qt2=new QuestionType();
			qt2.setId(18);
			colListQuestion.setText(colListTitle);
			colListQuestion.setQuestionType(qt2);
			colListQuestion.setQuestions(colListOptions);
			colListQuestion.setQuestion(question);
			
			
//			
//			
//			
//			Question answerQuestion = new Question();
//			//q.setText(newColOption);
//			answerQuestion.setQuestion(question);
//			QuestionType qt4 = new QuestionType();
//			qt4.setId(new Integer(questionType));
//			answerQuestion.setQuestionType(qt4);
//			answerQuestion.setSurveyStructure(question.getSurveyStructure());
//			
//			cellQuestion.getQuestions().add(answerQuestion);
//			
//			question.getQuestions().add(cellQuestion);
			
			
		}else{
			
			rowListQuestion = engine.getSubQuestions(question, 17).get(0);
			
			colListQuestion = engine.getSubQuestions(question, 18).get(0);
			
			rowListTitle = rowListQuestion.getText();
			colListTitle = colListQuestion.getText();
			
			rowListOptions = rowListQuestion.getQuestions();
			colListOptions = colListQuestion.getQuestions();
			
			//Question cellQuestion = engine.getSubQuestions(question, 7).get(0);
			
			//questionType = cellQuestion.getQuestions().get(0).getQuestionType().toString();
			
			questionType = colListOptions.get(0).getQuestions().get(0).getQuestionType().toString();
			
			/*
			List<Question> subQuestions = engine.getSubQuestions(question, null);
			
			categoryQuestion = subQuestions.get(0);
			categoryOptions = categoryQuestion.getQuestions();
			
			ratingQuestion = subQuestions.get(1);
			
			if(ratingQuestion.getQuestionType().getId() == 2 || ratingQuestion.getQuestionType().getId() == 3){
				ratingOptions = ratingQuestion.getQuestions();
			}
			
			questionType = ratingQuestion.getQuestionType().getId().toString();
			
			questionTitle = categoryQuestion.getText();
			answerTitle = ratingQuestion.getText();
			*/
		}
	}
	
	public void addRowListOption(ActionEvent e){
		Question q = new Question();
		q.setText(newRowOption);
		q.setQuestion(rowListQuestion);
		QuestionType qt = new QuestionType();
		qt.setId(7);
		q.setQuestionType(qt);
		q.setSurveyStructure(question.getSurveyStructure());
		rowListOptions.add(q);
	}


	public void addColListOption(ActionEvent e){
		Question q = new Question();
		q.setText(newColOption);
		q.setQuestion(colListQuestion);
		QuestionType qt=new QuestionType();
		qt.setId(7);
		q.setQuestionType(qt);
		q.setSurveyStructure(question.getSurveyStructure());
		colListOptions.add(q);
		
		List<Question> tmpQ = new ArrayList<Question>();
		for(int i = 0 ; i <rowListOptions.size() ; i++){
			
			Question rowForColumnQuestion = new Question();
			rowForColumnQuestion.setQuestion(q);
			QuestionType qt2 = new QuestionType();
			qt2.setId(17);
			rowForColumnQuestion.setQuestionType(qt2);
			rowForColumnQuestion.setSurveyStructure(question.getSurveyStructure());
			
			Question cellQuestion = new Question();
			cellQuestion.setQuestion(rowForColumnQuestion);
			QuestionType qt3 = new QuestionType();
			qt3.setId(new Integer(questionType));
			cellQuestion.setQuestionType(qt3);
			cellQuestion.setSurveyStructure(question.getSurveyStructure());
			
			rowForColumnQuestion.getQuestions().add(cellQuestion);
			
			tmpQ.add(rowForColumnQuestion);
			
		}
		
		q.setQuestions(tmpQ);
		
	}
	
	public void removeRowOption(ActionEvent e){
		Question q = (Question) e.getComponent().getAttributes().get("rowOption");
		q.setForRemoval(true);
	}
	
	public void undoRemoveRowOption(ActionEvent e){
		Question q = (Question)e.getComponent().getAttributes().get("rowOption");
		q.setForRemoval(false);
	}
	
	
	
	public void removeColumnOption(ActionEvent e){
		Question q = (Question) e.getComponent().getAttributes().get("colOption");
		q.setForRemoval(true);
	}
	
	public void undoRemoveCategoryOption(ActionEvent e){
		Question q = (Question)e.getComponent().getAttributes().get("colOption");
		q.setForRemoval(false);
	}

	public void adaptEditedSubQuestions(){
		editedSubQuestions.clear();
		editedSubQuestions.add(rowListQuestion);
		editedSubQuestions.add(colListQuestion);
		
	}

	public void setNewRowOption(String newRowOption) {
		this.newRowOption = newRowOption;
	}

	public String getNewRowOption() {
		return newRowOption;
	}
	
	public List<Question> getColListOptions() {
		return colListOptions;
	}
	
	public void setColListOptions(List<Question> colListOptions) {
		this.colListOptions = colListOptions;
	}

	public String getNewColOption() {
		return newColOption;
	}

	public void setNewColOption(String newColOption) {
		this.newColOption = newColOption;
	}

	public List<Question> getRowListOptions() {
		return rowListOptions;
	}

	public void setRowListOptions(List<Question> rowListOptions) {
		this.rowListOptions = rowListOptions;
	}

	public String getQuestionType() {
		return questionType;
	}

	public void setQuestionType(String questionType) {
		this.questionType = questionType;
	}
	
}
