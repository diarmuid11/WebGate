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

import java.util.List;
import java.util.Map;

import javax.faces.model.SelectItem;

import org.correlibre.qop.domain.Question;

//TODO: Utilizar Decorator de Otro cual
public class CheckButtonWithOthersQuestion extends CheckButtonQuestion {
	
	private Question othersQuestion;
	
	private Question whichOthersQuestion;
	
	private SelectItem othersOption;
	
	private String textAnswer;
	
	public void init(){
		super.init();
		othersQuestion = srvSurveysEngine.getSubQuestions(question, 6).get(0);
		othersOption = new SelectItem(othersQuestion.getId(),othersQuestion.getText());
		whichOthersQuestion = srvSurveysEngine.getSubQuestions(question, 1).get(0);
	}
	
	public String getTextAnswer() {
		return textAnswer;
	}

	public void setTextAnswer(String textAnswer) {
		this.textAnswer = textAnswer;
	}

	public SelectItem getOthersOption() {
		return othersOption;
	}

	public Question getWhichOthersQuestion() {
		return whichOthersQuestion;
	}

	public boolean isOthers(){
		System.out.println("getSelectedItems(): "+getSelectedItems());
		if (getSelectedItems()==null){
			return false;
		}
		List<String> answers = getSelectedItems();
		
		System.out.println("othersQuestion.getId(): "+othersQuestion.getId());
		
		if(answers.contains(othersQuestion.getId().toString())){
			
			//String ans = super.getAnswer().get(othersQuestion);
			
			//for (String idAns : answers) {
				//Map<Question, String> values = super.getAnswer();
				
				//System.out.println("ans: "+ans);
				//if("true".equalsIgnoreCase(ans)){
					return true;
				//}
			//}
		}else
			return false;
		
		//return false;
	}
	
	@Override
	public Map<Question, String> getAnswer() {
		Map<Question, String> answers = super.getAnswer();
		if (isOthers()){
			answers.put(othersQuestion, "true");
			answers.put(whichOthersQuestion, textAnswer);
		}else{
			answers.put(othersQuestion, "false");
		}
		return answers;
	}

	@Override
	public void setAnswer(Map<Question, String> answers) {
		super.setAnswer(answers);
		String ans = answers.get(othersQuestion);
		if (ans != null){
			if("true".equalsIgnoreCase(ans))
				getSelectedItems().add(othersQuestion.getId().toString());
		}
		if (isOthers()){
			textAnswer=answers.get(whichOthersQuestion);
		}
	}


}
