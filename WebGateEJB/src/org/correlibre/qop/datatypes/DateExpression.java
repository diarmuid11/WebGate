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

import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;

import org.correlibre.qop.parsing.Operand;
import org.correlibre.qop.parsing.Operator;
import org.correlibre.qop.parsing.OperatorsValue;
import org.correlibre.qop.services.QopException;


public class DateExpression implements TypeExpression{

		
	public DateExpression(){
		super();
	}

	@Override
	public Boolean eval(Operator op, Operand op1, Operand op2) throws QopException {
		if(!validate(op, op1, op2)){
			throw new QopException("Los operandos deben ser de tipo fecha");
		}
		
		Date d1 = (Date) op1.getValue();
		Date d2 = (Date) op2.getValue();
		
		switch(op.getValue()){
		case OperatorsValue.DIF:
			if (d1==null && d2==null)
				return false;
			if (d1==null || d2==null)
				return true;
			if(d1.compareTo(d2)!= 0)
				return true;
			return false;
		case OperatorsValue.EQ:
			if (d1==null && d2==null)
				return true;
			if (d1==null || d2==null)
				return false;
			if(d1.compareTo(d2)== 0)
				return true;
			return false;
		case OperatorsValue.LT:
			if (d1==null)
				return true;
			if(d1.compareTo(d2) < 0)
				return true;
			return false;
		case OperatorsValue.LE:			
			if (d1==null)
				return true;
			if(d1.compareTo(d2) <= 0)
				return true;
			return false;		
		case OperatorsValue.GT:
			if (d1==null)
				return false;
			if(d1.compareTo(d2) > 0)
				return true;
			return false;
		case OperatorsValue.GE:
			if (d1==null)
				return false;
			if(d1.compareTo(d2) >= 0)
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
				return((op1.getValue() instanceof Date) && (op2.getValue() 
						instanceof Date));				
			}
			else{
				return op1.getValue() instanceof Date;
			}
		}			
		return op2.getValue() instanceof Date;
	}

	@Override
	public Object getType() {
		return Date.class;
	}
	
	
	@Override
	public Object buildType(String str) throws QopException {
		
		try {
			return DateFormat.getInstance().parse(str);
		} catch (ParseException e) {
			throw new QopException("no fue posible construir valor tipo fecha");
		}
		
	}
}
