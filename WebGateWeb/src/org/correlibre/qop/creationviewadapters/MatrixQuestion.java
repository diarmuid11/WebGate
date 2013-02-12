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
import javax.faces.event.ValueChangeEvent;

import org.correlibre.qop.domain.Question;
import org.correlibre.qop.domain.QuestionType;
import org.correlibre.qop.services.QopException;

public class MatrixQuestion extends BaseCreationViewAdapter {

	protected Question rowListQuestion1;
	protected Question rowListQuestion2;
	protected Question rowListQuestion3;
	protected String rowText1;
	protected String rowText2;
	protected String rowText3;
	
	//protected Question cellQuestion1;
	//protected Question cellQuestion2;
	//protected Question cellQuestion3;
	
	protected boolean activeRowListQuestion1;
	protected boolean activeRowListQuestion2;
	protected boolean activeRowListQuestion3;
	
	protected List<Question> rowListOptions;
	
	protected String newRowOption;
	
	protected String questionType1;
	protected String questionType2;
	protected String questionType3;
	
	
	public MatrixQuestion() throws QopException {
		super();
		
	}

	public void init() {
		
		System.out.println("############### [MatrixQuestion] creating: "+creating);
		if (creating){
			
			questionType1 = "8";
			
			activeRowListQuestion1 = true;
			activeRowListQuestion2 = false;
			activeRowListQuestion3 = false;
			
			rowListOptions = new ArrayList<Question>();
			rowListQuestion1 = new Question();
			QuestionType qt=new QuestionType();
			qt.setId(17);
			rowListQuestion1.setText(rowText1);
			rowListQuestion1.setQuestionType(qt);
			rowListQuestion1.setQuestions(rowListOptions);
			rowListQuestion1.setQuestion(question);
			
			/*
			cellQuestion1 = new Question();
			QuestionType qt2 = new QuestionType();
			qt2.setId(8);
			cellQuestion1.setText("Celda1");
			cellQuestion1.setQuestionType(qt2);
			cellQuestion1.setQuestion(rowListQuestion1);
			rowListQuestion1.getQuestions().add(cellQuestion1);
			*/
		}else{
			
			List<Question> rowListQuestions = engine.getSubQuestions(question, 17);
			
			rowListOptions = new ArrayList<Question>();
			
			if(rowListQuestions.size() == 1){
				rowListQuestion1 = rowListQuestions.get(0);
				
				rowListOptions = rowListQuestion1.getQuestions(); 
				
				questionType1 = ""+rowListOptions.get(0).getQuestionType().getId(); 
				
				/*
				for (Question rQ : rowListQuestion1.getQuestions()) {
					if(rQ.getQuestionType().getId().intValue() != 7){
						questionType1 = ""+rQ.getQuestionType().getId();
						cellQuestion1 = rQ;
						break;
					}else{
						rowListOptions.add(rQ);
					}
				}
				*/
				//questionType1 = ""+rowListQuestion1.getQuestions().get(0).getQuestionType().getId();
				
				activeRowListQuestion1 = true;
				
				rowText1 = rowListQuestion1.getText();
				
				
			}else if(rowListQuestions.size() == 2){
				
				rowListQuestion1 = rowListQuestions.get(0);
				rowListQuestion2 = rowListQuestions.get(1);
				
				rowListOptions = rowListQuestion1.getQuestions(); 
				
				questionType1 = ""+rowListOptions.get(0).getQuestionType().getId();
				questionType2 = ""+rowListQuestion2.getQuestions().get(0).getQuestionType().getId();
				
				/*
				for (Question rQ : rowListQuestion1.getQuestions()) {
					if(rQ.getQuestionType().getId().intValue() != 7){
						questionType1 = ""+rQ.getQuestionType().getId();
						cellQuestion1 = rQ;
						break;
					}
				}
				*/
				//questionType1 = ""+rowListQuestion1.getQuestions().get(0).getQuestionType().getId();
				/*
				for (Question rQ : rowListQuestion2.getQuestions()) {
					if(rQ.getQuestionType().getId().intValue() != 7){
						questionType2 = ""+rQ.getQuestionType().getId();
						cellQuestion2 = rQ;
						break;
					}
				}
				*/
				//questionType2 = ""+rowListQuestion2.getQuestions().get(0).getQuestionType().getId();
				
				activeRowListQuestion1 = true;
				activeRowListQuestion2 = true;
				
				rowText1 = rowListQuestion1.getText();
				rowText2 = rowListQuestion2.getText();
				
			}else if(rowListQuestions.size() == 3){
				rowListQuestion1 = rowListQuestions.get(0);
				rowListQuestion2 = rowListQuestions.get(1);
				rowListQuestion3 = rowListQuestions.get(2);
				
				rowListOptions = rowListQuestion1.getQuestions(); 
				
				questionType1 = ""+rowListQuestion1.getQuestions().get(0).getQuestionType().getId();
				questionType2 = ""+rowListQuestion2.getQuestions().get(0).getQuestionType().getId();
				questionType3 = ""+rowListQuestion3.getQuestions().get(0).getQuestionType().getId();
				
				/*
				for (Question rQ : rowListQuestion1.getQuestions()) {
					if(rQ.getQuestionType().getId().intValue() != 7){
						questionType1 = ""+rQ.getQuestionType().getId();
						cellQuestion1 = rQ;
						break;
					}
				}
				*/
				//questionType1 = ""+rowListQuestion1.getQuestions().get(0).getQuestionType().getId();
				/*
				for (Question rQ : rowListQuestion2.getQuestions()) {
					if(rQ.getQuestionType().getId().intValue() != 7){
						questionType2 = ""+rQ.getQuestionType().getId();
						cellQuestion2 = rQ;
						break;
					}
				}
				*/
				//questionType2 = ""+rowListQuestion2.getQuestions().get(0).getQuestionType().getId();
				/*
				for (Question rQ : rowListQuestion3.getQuestions()) {
					if(rQ.getQuestionType().getId().intValue() != 7){
						questionType3 = ""+rQ.getQuestionType().getId();
						cellQuestion3 = rQ;
						break;
					}
				}
				*/
				//questionType3 = ""+rowListQuestion3.getQuestions().get(0).getQuestionType().getId();
				
				activeRowListQuestion1 = true;
				activeRowListQuestion2 = true;
				activeRowListQuestion3 = true;
				
				rowText1 = rowListQuestion1.getText();
				rowText2 = rowListQuestion2.getText();
				rowText3 = rowListQuestion3.getText();
				
			}
			
			
						
		}
	}
	
	public void addRowListOption(ActionEvent e){
		
		System.out.println("########## e.getComponent(): "+e.getComponent());
		
		Question q = new Question();
		q.setText(newRowOption);
		q.setQuestion(rowListQuestion1);
		QuestionType qt = new QuestionType();
		qt.setId(new Integer(questionType1));
		q.setQuestionType(qt);
		q.setSurveyStructure(question.getSurveyStructure());
		rowListOptions.add(q);
		
		if(activeRowListQuestion2)
			addOption(newRowOption, rowListQuestion2, questionType2);
		
		if(activeRowListQuestion3)
			addOption(newRowOption, rowListQuestion3, questionType3);
	}
	
	public void removeRowOption(ActionEvent e){
		Question q = (Question) e.getComponent().getAttributes().get("rowOption");
		q.setForRemoval(true);
		
		if(activeRowListQuestion2)
			removeOption(q, rowListQuestion2);
		
		if(activeRowListQuestion3)
			removeOption(q, rowListQuestion3);
	}
	
	public void undoRemoveRowOption(ActionEvent e){
		Question q = (Question)e.getComponent().getAttributes().get("rowOption");
		q.setForRemoval(false);
		
		if(activeRowListQuestion2)
			undoRemoveOption(q, rowListQuestion2);
		
		if(activeRowListQuestion3)
			undoRemoveOption(q, rowListQuestion3);
	}
	
	public void addOption(String newRowOption, Question rowListQuestion, String questionType){
		
		List<Question> newRowListOptions = rowListQuestion.getQuestions();
		
		Question q = new Question();
		q.setText(newRowOption);
		q.setQuestion(rowListQuestion);
		QuestionType qt = new QuestionType();
		qt.setId(new Integer(questionType));
		q.setQuestionType(qt);
		q.setSurveyStructure(question.getSurveyStructure());
		newRowListOptions.add(q);
		
		//rowListQuestion.setQuestions(newRowListOptions);
	}
	
	public void removeOption(Question q, Question rowListQuestion){
		
		List<Question> rowListOptions = rowListQuestion.getQuestions();
		
		for (Question rowOption : rowListOptions) {
			
			if(rowOption.getText().equalsIgnoreCase(q.getText())){
				
				rowOption.setForRemoval(true);
				break;
				
			}
			
		}
		
	}
	
	public void undoRemoveOption(Question q, Question rowListQuestion){
		
		List<Question> rowListOptions = rowListQuestion.getQuestions();
		
		for (Question rowOption : rowListOptions) {
			
			if(rowOption.getText().equalsIgnoreCase(q.getText())){
				
				rowOption.setForRemoval(false);
				break;
			}
			
		}
		
	}
	
	public void adaptEditedSubQuestions(){
		editedSubQuestions.clear();
		editedSubQuestions.add(rowListQuestion1);
		
		if(activeRowListQuestion2)
			editedSubQuestions.add(rowListQuestion2);
		
		if(activeRowListQuestion3)
			editedSubQuestions.add(rowListQuestion3);
	}
	
	public void duplicateOptions(List<Question> rowListOptions, Question rowListQuestion, String questionType){
		
		List<Question> newRowListOptions = new ArrayList<Question>();
		
		for (Question rowOpt : rowListOptions) {
			if(rowOpt.getQuestionType().getId() == 7){
				Question q = new Question();
				q.setText(rowOpt.getText());
				q.setQuestion(rowListQuestion);
				QuestionType qt = new QuestionType();
				qt.setId(new Integer(questionType));
				q.setQuestionType(qt);
				q.setSurveyStructure(question.getSurveyStructure());
				newRowListOptions.add(q);
			}
		}
		rowListQuestion.setQuestions(newRowListOptions);
	}
	
	public List<Question> getRowOptionsForTable(){
		
		return rowListOptions;
		
		/*
		List<Question> tmpRowList = new ArrayList<Question>();
		
		for (Question rOpt : rowListQuestion1.getQuestions()) {
			
			if(rOpt.getQuestionType().getId() == 7)
				tmpRowList.add(rOpt);
			
		}
		
		return tmpRowList;
		*/
		
	}
	
	
	public void activateRowListQuestion2(ValueChangeEvent e){
		
		if(this.activeRowListQuestion2 == false){
			
			rowListQuestion2 = new Question();
			QuestionType qt=new QuestionType();
			qt.setId(17);
			rowListQuestion2.setText(rowText2);
			rowListQuestion2.setQuestionType(qt);
			
			duplicateOptions(rowListOptions, rowListQuestion2, questionType2);
			
			rowListQuestion2.setQuestion(question);
			questionType2 = "8";
			
			/*
			cellQuestion2 = new Question();
			QuestionType qt2 = new QuestionType();
			qt2.setId(new Integer(questionType2));
			cellQuestion2.setText("Celda2");
			cellQuestion2.setQuestionType(qt2);
			cellQuestion2.setQuestion(rowListQuestion2);
			rowListQuestion2.getQuestions().add(cellQuestion2);
			*/
			
			rowListQuestion2.setForRemoval(false);
			//cellQuestion2.setForRemoval(false);
			
		}else{
			
			rowText2 = "";
			if(rowListQuestion2 != null)
				rowListQuestion2.setForRemoval(true);
			
			/*
			if(cellQuestion2 != null)
				cellQuestion2.setForRemoval(true);
			*/
			
		}
		System.out.println("###### [MatrixQuestion2] activate this.activeRowListQuestion2: "+this.activeRowListQuestion2);
		
		System.out.println("###### [MatrixQuestion2] activate questionType2: "+questionType2);
		
	}
	
	public void activateRowListQuestion3(ValueChangeEvent e){
		
		System.out.println("####### [MatrixQuestion2] 3 e.getComponent().getId(): "+e.getComponent().getId());
		
		if(this.activeRowListQuestion3 == false){
			
			rowListQuestion3 = new Question();
			QuestionType qt = new QuestionType();
			qt.setId(17);
			rowListQuestion3.setText(rowText3);
			rowListQuestion3.setQuestionType(qt);
			//rowListQuestion3.setQuestions(rowListOptions);
			
			duplicateOptions(rowListOptions, rowListQuestion3, questionType3);
			
			rowListQuestion3.setQuestion(question);
			/*
			questionType3 = "8";
			cellQuestion3 = new Question();
			QuestionType qt3 = new QuestionType();
			qt3.setId(new Integer(questionType3));
			cellQuestion3.setText("Celda3");
			cellQuestion3.setQuestionType(qt3);
			cellQuestion3.setQuestion(rowListQuestion3);
			rowListQuestion3.getQuestions().add(cellQuestion3);
			*/
			rowListQuestion3.setForRemoval(false);
			//cellQuestion3.setForRemoval(false);
			
		}else{
			
			rowText3 = "";
			
			if(rowListQuestion3 != null)
				rowListQuestion3.setForRemoval(true);
			
			/*
			if(cellQuestion3 != null)
				cellQuestion3.setForRemoval(true);
			*/
			
		}
		
	}
	
	public void changeQuestionType1(ValueChangeEvent e){
		
		String newQuestionType1 = (String) e.getNewValue();
		questionType1 = newQuestionType1;
		
		QuestionType qt1 = new QuestionType();
		qt1.setId(new Integer(questionType1));
		
		for (Question row : rowListQuestion1.getQuestions()) {
			row.setQuestionType(qt1);
		}
		
		//QuestionType qt1 = new QuestionType();
		//qt1.setId(new Integer(questionType1));
		//cellQuestion1.setQuestionType(qt1);
		
	}
	
	public void changeQuestionType2(ValueChangeEvent e){
		
		System.out.println("####### [MatrixQuestion2] 2 e.getComponent().getId(): "+e.getComponent().getId());
		System.out.println("######## [MatrixQuestion2] questionType2: "+questionType2);
		
		String newQuestionType2 = (String) e.getNewValue();
		questionType2 = newQuestionType2;
		
		if(activeRowListQuestion2){
			
			QuestionType qt2 = new QuestionType();
			qt2.setId(new Integer(questionType2));
			
			for (Question row : rowListQuestion2.getQuestions()) {
				row.setQuestionType(qt2);
			}
		/*
			QuestionType qt2 = new QuestionType();
			qt2.setId(new Integer(questionType2));
			cellQuestion2.setQuestionType(qt2);
		*/
		}
		
	}

	public void changeQuestionType3(ValueChangeEvent e){
		
		System.out.println("######## [MatrixQuestion2] 2 e.getComponent().getId(): "+e.getComponent().getId());
		System.out.println("######## [MatrixQuestion2] questionType3: "+questionType3);
	
		String newQuestionType3 = (String) e.getNewValue();
		questionType3 = newQuestionType3;
		
		if(activeRowListQuestion3){
			
			QuestionType qt3 = new QuestionType();
			qt3.setId(new Integer(questionType3));
			
			for (Question row : rowListQuestion3.getQuestions()) {
				row.setQuestionType(qt3);
			}
			/*
			QuestionType qt3 = new QuestionType();
			qt3.setId(new Integer(questionType3));
			cellQuestion3.setQuestionType(qt3);
			*/
		}
	}

	public String getRowText1() {
		return rowText1;
	}

	public void setRowText1(String rowText1) {
		this.rowText1 = rowText1;
		rowListQuestion1.setText(rowText1);
	}

	public String getRowText2() {
		return rowText2;
	}

	public void setRowText2(String rowText2) {
		this.rowText2 = rowText2;
		rowListQuestion2.setText(rowText2);
	}

	public String getRowText3() {
		return rowText3;
	}

	public void setRowText3(String rowText3) {
		this.rowText3 = rowText3;
		rowListQuestion3.setText(rowText3);
	}

	public boolean isActiveRowListQuestion1() {
		return activeRowListQuestion1;
	}

	public String getNewRowOption() {
		return newRowOption;
	}

	public void setNewRowOption(String newRowOption) {
		this.newRowOption = newRowOption;
	}

	public void setActiveRowListQuestion1(boolean activeRowListQuestion1) {
		
		this.activeRowListQuestion1 = activeRowListQuestion1;
	}

	public boolean isActiveRowListQuestion2() {
		return activeRowListQuestion2;
	}

	public void setActiveRowListQuestion2(boolean activeRowListQuestion2) {
		
		System.out.println("this.activeRowListQuestion2: "+ this.activeRowListQuestion2);
		System.out.println("activeRowListQuestion2: "+ activeRowListQuestion2);
		
		this.activeRowListQuestion2 = activeRowListQuestion2;
	}
	
	public boolean isActiveRowListQuestion3() {
		return activeRowListQuestion3;
	}

	public void setActiveRowListQuestion3(boolean activeRowListQuestion3) {
		this.activeRowListQuestion3 = activeRowListQuestion3;
	}

	public String getQuestionType1() {
		return questionType1;
	}

	public void setQuestionType1(String questionType1) {
		this.questionType1 = questionType1;
		
	}
	
	public String getQuestionType2() {
		return questionType2;
	}

	public void setQuestionType2(String questionType2) {
		this.questionType2 = questionType2;
	
	}

	public String getQuestionType3() {
		return questionType3;
	}

	public void setQuestionType3(String questionType3) {
		this.questionType3 = questionType3;
		
	}

	public List<Question> getRowListOptions() {
		return rowListOptions;
	}

	public void setRowListOptions(List<Question> rowListOptions) {
		this.rowListOptions = rowListOptions;
	}
	
}
