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
package org.correlibre.qop.parsing;


public class Operator{	
	private int nroOperands;
	private int value;
	private Object resultType;
	private Object operandType;
	
	public Operator(int numberOfOperands,
			int value, Object resultType)
	{
		this.nroOperands = numberOfOperands;
		this.value = value;
		this.resultType = resultType;
		
	}


	public int getNroOperands() {
		return nroOperands;
	}

	public int getValue() {
		return value;
	}

	public Object getResultType() {
		return resultType;
	}

	public Object getOperandType() {
		return operandType;
	}
	
	public String toString(){
		return ""+value;
	}
}	