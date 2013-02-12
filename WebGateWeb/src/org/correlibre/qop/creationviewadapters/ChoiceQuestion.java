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

public class ChoiceQuestion extends BaseCreationViewAdapter {

	protected List<Question> options;
	
	private String newOption;

	public ChoiceQuestion() throws QopException {
		super();
	}

	public void init() {
		System.out.println("############### [ChoiceQuestion] creating: "+creating);
		if (creating){
			options = new ArrayList<Question>();
		}else{
			options = engine.getSubQuestions(question, 7);
		}
	}

	public List<Question> getOptions() {
		return options;
	}

	public String getNewOption() {
		return newOption;
	}

	public void setNewOption(String newOption) {
		this.newOption = newOption;
	}

	public void addOption(ActionEvent e){
		Question q=new Question();
		q.setText(newOption);
		q.setQuestion(question);
		QuestionType qt=new QuestionType();
		qt.setId(7);
		q.setQuestionType(qt);
		options.add(q);
	}
	
	public void removeOption(ActionEvent e){
		Question q= (Question)e.getComponent().getAttributes().get("option");
		q.setForRemoval(true);
	}
	
	public void undoRemoveOption(ActionEvent e){
		Question q= (Question)e.getComponent().getAttributes().get("option");
		q.setForRemoval(false);
	}

	public void adaptEditedSubQuestions(){
		editedSubQuestions.addAll(options);
	}
	
}