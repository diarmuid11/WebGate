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
package org.correlibre.qop.datatypes;

import org.correlibre.qop.parsing.Operand;
import org.correlibre.qop.parsing.Operator;
import org.correlibre.qop.services.QopException;

public interface TypeExpression {
	public Boolean eval(Operator op, Operand op1, Operand op2)throws QopException;
	
	public boolean validate(Operator op, Operand op1, Operand op2)throws QopException;
	
	public Object buildType(String str) throws QopException;
	
	public Object getType();
}
