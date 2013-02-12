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
package org.correlibre.qop.fillviewadapters;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.correlibre.qop.domain.Question;

public class MatrixInstanceQuestion0 extends BaseFillViewAdapter {

	//protected List<SelectItem> ratingOptions;
	//protected List<Question> categoryOptions;
	
	protected Question rowListQuestion;
	protected Question colListQuestion;
	
	protected HashMap<Question, HashMap<Question, String>> colItems;
	
	protected String questionType;
	
	public void init(){
		
		rowListQuestion = srvSurveysEngine.getSubQuestions(question, 17).get(0);
		
		colListQuestion = srvSurveysEngine.getSubQuestions(question, 18).get(0);
		
		//rowListOptions = rowListQuestion.getQuestions();
		//colListOptions = colListQuestion.getQuestions();
		
		//Question cellQuestion = engine.getSubQuestions(question, 7).get(0);
		
		//questionType = cellQuestion.getQuestions().get(0).getQuestionType().toString();
		
		questionType = colListQuestion.getQuestions().get(0).getQuestions().get(0).getQuestionType().toString();
		
//		List<Question> subQuestions = srvSurveysEngine.getSubQuestions(question, null);
//		
//		categoryQuestion = subQuestions.get(0);
//		ratingQuestion = subQuestions.get(1);
//		
//		if(ratingQuestion.getQuestionType().getId() == 2 || ratingQuestion.getQuestionType().getId() == 3){
//			ratingOptions = BeanUtil.getInstance().getQuestionsAsSelectItems(ratingQuestion.getQuestions());
//		}
//		categoryOptions = categoryQuestion.getQuestions();
//		
//		questionType = ratingQuestion.getQuestionType().getId().toString() ;
//		
//		System.out.println("################# [MultiOptionSelectOneMenuQuestion] questionType: "+questionType);
//		System.out.println("################# [MultiOptionSelectOneMenuQuestion] categoryOptions: "+categoryOptions.size());
		
	}
	
	@Override
	public Map<Question, String> getAnswer() {
		Map<Question, String> answers = new HashMap<Question, String> ();
		
		Set<Question> colItem = colItems.keySet();
		
		for (Question colOption : colItem) {
			
			Question rowQuestion = colOption.getQuestions().get(0);
			HashMap<Question, String> rowItems = (HashMap<Question, String>) colItems.get(colOption);
			
			Question cellQuestion = rowQuestion.getQuestions().get(0);
			String val = rowItems.get(rowQuestion);
			
			answers.put(cellQuestion, val);			
			
		}
	
		return answers;
	}

	@Override
	public void setAnswer(Map<Question, String> answers) {
		
		colItems = new HashMap<Question, HashMap<Question, String>>();
		
		for (Question columnOption: colListQuestion.getQuestions()){
			
			HashMap<Question, String> rowItems = new HashMap<Question, String>();
			
			for (Question anotherRowListQuestion: columnOption.getQuestions()){
				
				Question cellQuestion = anotherRowListQuestion.getQuestions().get(0);
				
				if (answers.get(cellQuestion) != null){
				
					rowItems.put(cellQuestion, answers.get(cellQuestion));
					
				}
				
			}
			colItems.put(columnOption, rowItems);
			
		}
	}
	
	public String getCellOption(Question row, Question col){
		
		HashMap<Question, String> valueSet = colItems.get(row);
        return valueSet.get(col);
		
	}

	public Question getRowListQuestion() {
		return rowListQuestion;
	}

	public void setRowListQuestion(Question rowListQuestion) {
		this.rowListQuestion = rowListQuestion;
	}

	public Question getColListQuestion() {
		return colListQuestion;
	}

	public void setColListQuestion(Question colListQuestion) {
		this.colListQuestion = colListQuestion;
	}

	public HashMap<Question, HashMap<Question, String>> getColItems() {
		return colItems;
	}

	public void setColItems(HashMap<Question, HashMap<Question, String>> colItems) {
		this.colItems = colItems;
	}

	public String getQuestionType() {
		return questionType;
	}

	public void setQuestionType(String questionType) {
		this.questionType = questionType;
	}

	
}
