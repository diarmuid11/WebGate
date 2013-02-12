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

import org.correlibre.qop.domain.Question;
import org.correlibre.qop.domain.QuestionType;
import org.correlibre.qop.services.QopException;

public class ChoiceWithOtherQuestion extends ChoiceQuestion {

	//private List<SelectItem> whichQuestionTypesItems;
	
	private Question otherQuestion;

	private Question whichQuestion;

	public ChoiceWithOtherQuestion() throws QopException {
		super();
	}

	public void init() {
		super.init();
		if (creating){
			otherQuestion = new Question();
			otherQuestion.setQuestion(question);
			QuestionType qt=new QuestionType();
			qt.setId(6);
			otherQuestion.setQuestionType(qt);
			otherQuestion.setText("Otro");
			
			whichQuestion=new Question();
			whichQuestion.setQuestion(question);
			qt=new QuestionType();
			qt.setId(1);
			whichQuestion.setQuestionType(qt);
			whichQuestion.setText("Cuál?");
		}else{
			otherQuestion = engine.getSubQuestions(question, 6).get(0);
			whichQuestion = engine.getSubQuestions(question, 1).get(0);
		}
	}

	public Question getOtherQuestion() {
		return otherQuestion;
	}

	public Question getWhichQuestion() {
		return whichQuestion;
	}

	public void adaptEditedSubQuestions(){
		super.adaptEditedSubQuestions();
		editedSubQuestions.add(otherQuestion);
		editedSubQuestions.add(whichQuestion);
	}
}