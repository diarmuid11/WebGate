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
package org.correlibre.qop.validations;

import java.util.Map;

import org.correlibre.qop.domain.Question;
import org.correlibre.qop.domain.Validation;
import org.correlibre.qop.services.QopException;
import org.correlibre.qop.services.ServiceAwareClass;

public class Expression extends ServiceAwareClass implements Validator {

	public Expression() throws QopException {
		super();
	}

	@Override
	public boolean validate(Question question, Map<Question, String> answers,
			Validation validation) throws QopException {
		String expr=conformExpression(question, answers, validation);
		return invokeExpression(expr, answers);
	}
	
	protected String conformExpression(Question question, Map<Question, String> answers,Validation validation){
		return validation.getRule();
	}
	
	private boolean invokeExpression(String expression,Map<Question, String> answers) throws QopException{
		return validation.evalExpression(expression, answers);
	}

}
