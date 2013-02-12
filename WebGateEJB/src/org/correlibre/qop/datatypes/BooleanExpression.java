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
import org.correlibre.qop.parsing.OperatorsValue;
import org.correlibre.qop.services.QopException;

public class BooleanExpression implements TypeExpression{

	public BooleanExpression(){		
	}
	
	@Override
	public Boolean eval(Operator op, Operand op1, Operand op2) throws QopException {
		if(!validate(op, op1, op2)){
			throw new QopException("los operadores deben ser de tipo booleano");
		}
		Boolean b1 = op1.getValue()==null?new Boolean(false):(Boolean)op1.getValue();
		Boolean b2 = op2.getValue()==null?new Boolean(false):(Boolean)op2.getValue();
		
		switch(op.getValue()){
		case OperatorsValue.AND:			
			return b1.booleanValue() && b2.booleanValue();
		case OperatorsValue.OR:
			return b1.booleanValue() || b2.booleanValue();
		case OperatorsValue.EQ:
			return b1.equals(b2);
		default:
			throw new QopException("La operacion " + op.getValue() +
				" no esta soportada para tipos booleanos");
		}
	}

	@Override
	public boolean validate(Operator op, Operand op1, Operand op2) {
		if (op1.getValue() != null){
			if(op2.getValue() != null){
				return((op1.getValue() instanceof Boolean) && (op2.getValue() 
						instanceof Boolean));				
			}
			else{
				return op1.getValue() instanceof Boolean;
			}
		}			
		return op2.getValue() instanceof Boolean;
	}
	
	@Override
	public Object getType() {
		return Boolean.class;
	}

	@Override
	public Object buildType(String str) throws QopException {
		if (str!=null && str.equalsIgnoreCase("true")){
			return new Boolean(true);
		}else if (str!=null && str.equalsIgnoreCase("false")){
			return new Boolean(false);
		} else {
			throw new QopException("La expresión no es booleana");
		}
	}



}
