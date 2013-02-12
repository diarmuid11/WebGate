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

import java.util.Map;

import javax.faces.model.SelectItem;

import org.correlibre.qop.domain.ConfigProperty;
import org.correlibre.qop.domain.Question;
import org.correlibre.qop.services.QopException;

//TODO: Utilizar Decorator de Otro cual
public class RadioButtonWithOthersQuestion extends RadioButtonQuestion {
	
	private Question othersQuestion;
	
	private Question whichOthersQuestion;
	
	private SelectItem othersOption;
	
	private String textAnswer;
	
	public void init(){ 
		super.init();
		othersQuestion = srvSurveysEngine.getSubQuestions(question, 6).get(0);
		
		ConfigProperty showValueOnLabelConfig = null;
		
		try {
			showValueOnLabelConfig = srvSurveysEngine
			.getConfigProperty("showValueAndLabel", this.question.getSurveyStructure(),
					this.question);
		} catch (QopException e) {
			e.printStackTrace();
		}
		
		boolean showValueOnLabel = false;

		System.out.println("[SelectOne] showValueOnLabelConfig: "+showValueOnLabelConfig);
		
		if (showValueOnLabelConfig != null
				&& Boolean.parseBoolean(showValueOnLabelConfig.getValue())) {
			
			showValueOnLabel = true;
		}
		
		othersOption = null;
		if(showValueOnLabel)
			othersOption = new SelectItem(othersQuestion.getId(),othersQuestion.getName()+" : "+othersQuestion.getText());
		else
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
		if (getSelectedItem()==null){
			return false;
		}
		return getSelectedItem().equals(othersQuestion.getId().toString());
	}
	
	@Override
	public Map<Question, String> getAnswer() {
		Map<Question, String> answers = super.getAnswer();
		if (isOthers()){
			answers.put(whichOthersQuestion, textAnswer);
		}
		return answers;
	}

	@Override
	public void setAnswer(Map<Question, String> answers) {
		super.setAnswer(answers);
		if (isOthers()){
			textAnswer=answers.get(whichOthersQuestion);
		}
	}

}
