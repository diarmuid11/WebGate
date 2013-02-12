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

import java.math.BigDecimal;

import org.correlibre.qop.parsing.Operand;
import org.correlibre.qop.parsing.Operator;
import org.correlibre.qop.parsing.OperatorsValue;
import org.correlibre.qop.services.QopException;

public class NumberExpression implements TypeExpression{

	public NumberExpression(){		
	}
	
	@Override
	public Boolean eval(Operator op, Operand op1, Operand op2) throws QopException {
		if(!validate(op, op1, op2)){
			throw new QopException("Los operandos deben ser de tipo númerico");
		}
		BigDecimal d1 = (BigDecimal) op1.getValue();
		BigDecimal d2 = (BigDecimal) op2.getValue();
		
		
		
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
			if (d1==null || d2==null)
				return false;
			if(d1.compareTo(d2) < 0)
				return true;
			return false;
		case OperatorsValue.LE:
			if (d1==null || d2==null)
				return false;
			if(d1.compareTo(d2) <= 0)
				return true;
			return false;		
		case OperatorsValue.GT:
			if (d1==null || d2==null)
				return false;
			if(d1.compareTo(d2) > 0)
				return true;
			return false;
		case OperatorsValue.GE:
			if (d1==null || d2==null)
				return false;
			if(d1.compareTo(d2) >= 0)
				return true;
			return false;
		default:
			throw new QopException("La operacion " + op.getValue() +
				" no esta soportada para tipos numericos");
		}
	}

	@Override
	public boolean validate(Operator op, Operand op1, Operand op2) throws QopException {		
		if (op1.getValue() != null){
			if(op2.getValue() != null){
				return((op1.getValue() instanceof BigDecimal) && (op2.getValue() instanceof BigDecimal));				
			}else{
				return op1.getValue() instanceof BigDecimal;
			}
		}			
		return op2.getValue() instanceof BigDecimal;
	}
	
	@Override
	public Object getType() {
		return BigDecimal.class;
	}

	@Override
	public Object buildType(String str) throws QopException {
		int pos = 0;
		int point = 0;
		while(str.charAt(pos) == '.'){
			point ++;
			System.out.println(str.charAt(pos));
		}
		if(point > 1){
			throw new QopException("se espera un valor numerico en lugar de " +
					str);
		}
		try{
			return new BigDecimal(str.trim());
		}
		catch(Exception e){
			throw new QopException("se espera un valor numerico en lugar de " +
					str);		
		}
	}



}
