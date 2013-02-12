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
/**
 * 
 */
package org.correlibre.qop.datatypes; 

import org.correlibre.qop.parsing.Operand;
import org.correlibre.qop.parsing.Operator;
import org.correlibre.qop.parsing.OperatorsValue;
import org.correlibre.qop.services.QopException;


public class StringExpression implements TypeExpression{

		
	public StringExpression(){
		super();
	}

	@Override
	public Boolean eval(Operator op, Operand op1, Operand op2) throws QopException {
		if(!validate(op, op1, op2)){
			throw new QopException("Los operandos deben ser cadenas de caracteres");
		}
		String s1 = op1.getValue()==null?"":(String)op1.getValue();
		String s2 = op2.getValue()==null?"":(String)op2.getValue();
		
		switch(op.getValue()){
		case OperatorsValue.DIF:			
			if(s1.compareTo(s2)!= 0)
				return true;
			return false;
		case OperatorsValue.EQ:
			if(s1.compareTo(s2)== 0)
				return true;
			return false;
		case OperatorsValue.LT:
			if(s1.compareTo(s2) < 0)
				return true;
			return false;
		case OperatorsValue.LE:			
			if(s1.compareTo(s2) <= 0)
				return true;
			return false;		
		case OperatorsValue.GT:
			if(s1.compareTo(s2) > 0)
				return true;
			return false;
		case OperatorsValue.GE:
			if(s1.compareTo(s2) >= 0)
				return true;
			return false;
		default:
			throw new QopException("El tipo de dato del operador " + op.getValue() +
				" es diferente al tipo de dato de la operación");
		}
	}

	@Override
	public boolean validate(Operator op, Operand op1, Operand op2) throws QopException {				
		if (op1.getValue() != null){
			if(op2.getValue() != null){
				return((op1.getValue() instanceof String) && (op2.getValue() 
						instanceof String));				
			}
			else{
				return op1.getValue() instanceof String;
			}
		}			
		return op2.getValue() instanceof String;
	}

	@Override
	public Object getType() {
		return String.class;
	}
	
	
	@Override
	public Object buildType(String str) throws QopException {
		if (str.charAt(0)!='\'' || str.charAt(str.length()-1)!='\''){
			throw new QopException("no fue posible construir valor tipo cadena");
		}
		return str.substring(1,str.length()-1);
	}
}
