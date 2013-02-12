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

import org.correlibre.qop.domain.Question;
import org.correlibre.qop.services.QopException;
import org.correlibre.qop.services.ServiceAwareClass;

public abstract class BaseCreationViewAdapter extends ServiceAwareClass implements CreationViewAdapter{

	protected Question question;
	protected boolean creating;
	protected List<Question> editedSubQuestions;

	public BaseCreationViewAdapter() throws QopException {
		super();
		editedSubQuestions=new ArrayList<Question>();
	}

	@Override
	public void init() {
	}
	
	@Override
	public void setQuestionToEdit(Question q) {
		question = q;		
	}

	@Override
	public Question getQuestion() {
		return question;
	}

	@Override
	public boolean isCreating() {
		return creating;
	}

	@Override
	public void setCreating(boolean creating) {
		this.creating = creating;
	}

	@Override
	public Question getEditedQuestion() {
		return question;
	}

	public abstract void adaptEditedSubQuestions();
	
	@Override
	public List<Question> getEditedSubQuestions() {
		adaptEditedSubQuestions();
		return editedSubQuestions;
	}

	
	
	
	
	
	
	
}