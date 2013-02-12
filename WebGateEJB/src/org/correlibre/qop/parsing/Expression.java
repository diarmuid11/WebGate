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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import org.correlibre.qop.datatypes.BooleanExpression;
import org.correlibre.qop.datatypes.DateExpression;
import org.correlibre.qop.datatypes.NumberExpression;
import org.correlibre.qop.datatypes.StringExpression;
import org.correlibre.qop.datatypes.TypeExpression;
import org.correlibre.qop.domain.Question;
import org.correlibre.qop.services.QopException;
import org.correlibre.qop.services.ServiceAwareClass;


public class Expression extends ServiceAwareClass
{
	private String expressionStr;
	private int position;
	private HashMap<String,String> values;
	private Stack <Operator> operators;
	private Stack <Operand> operands;	
	
	public Expression(String expressionStr, Map<Question, String> vars) throws QopException
	{
		this.expressionStr = expressionStr;
		this.position = 0;
		//TODO: No deberia ser de instancia
		this.operators = new Stack<Operator>();
		this.operands = new Stack<Operand>();	
		this.values = getValues(vars);
	}	
		
	public Boolean evalExpression() throws QopException
	{
		this.parsing();				
		if(this.operators.isEmpty() && this.operands.size() == 1){
			return (Boolean) this.operands.pop().getValue();
		}
		throw new QopException("expresion mal formada.");
	}

	/**
	 * @throws QopException
	 */
	private void parsing() throws QopException
	{			
		int size;
		char ch = 0, next = 0;
		Operator currentOperator = null;
		//System.out.println("PARSEANDO:"+this.expressionStr);
		
		parseVal();		
		replaceVars();
		this.position = 0;
		size = this.expressionStr.length();
		System.out.println("PARSEANDO DESPUES DE REEMPLAZAR:"+this.expressionStr);
		
		while(this.position < size){			
			ch = this.expressionStr.charAt(this.position);
			if(this.position + 1 < size){
				next = this.expressionStr.charAt(this.position + 1);
			}
			else{
				next = ' ';
			}
			switch (ch){
			case ' ': 
				this.position ++;
				break;
			case '(': 			
				currentOperator = new Operator(0,OperatorsValue.OPEN,null);
				this.operators.push(currentOperator);
				this.position ++;
				break;
			case ')':
				currentOperator = new Operator(0,OperatorsValue.CLOSE,null);
				this.operators.push(currentOperator);
				compute();		
				this.position ++;
				break;
			case '=':			
				currentOperator = new Operator(2, OperatorsValue.EQ,Boolean.class);
				this.operators.push(currentOperator);
				this.position ++;
				break;
			case '>':	
				if (this.position + 1 < size && next == '='){
					this.position ++;
					currentOperator = new Operator(2, OperatorsValue.GE,
							Boolean.class);
					this.operators.push(currentOperator);
				}
				else{
					currentOperator = new Operator(2, OperatorsValue.GT,
							Boolean.class);
					this.operators.push(currentOperator);
				}
				this.position ++;
				break;			
			case '<':
				if (this.position + 1 < size && next == '='){
					this.position ++;
					currentOperator = new Operator(2,OperatorsValue.LE,
							Boolean.class);
					this.operators.push(currentOperator);
				}
				else{
					currentOperator = new Operator(2,OperatorsValue.LT,
							Boolean.class);
					this.operators.push(currentOperator);
				}	
				this.position ++;
				break;
			case '!':
				if (this.position + 1 < size && next == '='){
					currentOperator = new Operator(2,OperatorsValue.DIF,
							Boolean.class);
					this.operators.push(currentOperator);
				}
				else{
					throw new QopException("use el operador !=  para la operacion difernte ");
				}
				this.position ++;
				break;
			case '&':			
				if (this.position + 1 < size && next == '&'){
					this.position ++;
					currentOperator = new Operator(2,OperatorsValue.AND,Boolean.class);
					this.operators.push(currentOperator);
				}
				else{
					throw new QopException("use el operador && para la operacion AND ");
				}
				this.position ++;
				break;
			case '|':			
				if (this.position + 1 < size && next == '|'){
					this.position ++;
					currentOperator = new Operator(2,OperatorsValue.OR, Boolean.class);
					this.operators.push(currentOperator);
				}
				else{
					throw new QopException("use el operador || para la operacion OR ");
				}
				this.position ++;
				break;			
			default:
				Operand op = parseOperand();
				this.operands.push(op);
			}			
		}
		compute();
	}
	
	
	private HashMap<String, String> getValues(Map<Question, String> vars){
		HashMap<String, String> values = new HashMap<String, String>();
		Set<Question> questions = (Set<Question>)vars.keySet();
		for(Question q: questions){
			values.put(q.getKey(), vars.get(q));
		}
		return values;
	}
	
	private TypeExpression inferType(String literal) throws QopException{
		BooleanExpression be=new BooleanExpression();
		try{
			be.buildType(literal);
			return be;
		}catch (QopException e){
			NumberExpression ne=new NumberExpression();
			try {
				ne.buildType(literal);
				return ne;
			} catch (QopException e1) {
				DateExpression de=new DateExpression();
				try {
					de.buildType(literal);
					return de;
				} catch (QopException e2) {
					try{
						StringExpression se=new StringExpression();
						se.buildType(literal);
						return se;
					}catch (QopException e3) {
						return null;
					}
				}
			}
		}
	}
	
	private void compute() throws QopException
	{			
		Boolean bool, close = false;
		Operator opr = null;		
		while(!this.operators.empty() &&
				(opr= this.operators.pop()).getValue()!= OperatorsValue.OPEN){	
			if(opr.getValue() == OperatorsValue.CLOSE){
				close = true;
			}
			else{
				Operand op2 = this.operands.pop();
				Operand op1 = this.operands.pop();	
				TypeExpression typeExpression = op1.getType();
				if (typeExpression == null){
					typeExpression = op2.getType();
				}				
				bool= typeExpression.eval(opr, op1, op2);
				Operand op3 = new Operand(bool, new BooleanExpression());			
				this.operands.push(op3);	
			}
		}
		if(opr != null)
			if(close && opr.getValue() != OperatorsValue.OPEN){
				throw new QopException("Error en el orden y/o número de parentesis " + 
						"Falta un parentisis de apertura " + this.position );
			}
			else if(!close && opr.getValue() == OperatorsValue.OPEN){
				throw new QopException("Error en el orden y/o número de parentesis " + 
						"Falta un parentisis de cierre " + this.position );
			}
	}
	
	private void parseVal() throws QopException{
		int valIndex = this.expressionStr.indexOf("VAL");
		int pos;
		int size = this.expressionStr.length();
		while(valIndex >= 0 && valIndex < size){
			pos = valIndex;
			while( pos < size && this.expressionStr.charAt(pos)!= ')'){
				pos++;
			}
			
			String parmStr = this.expressionStr.substring(valIndex, pos + 1);
			String[] params = parmStr.split(",");
			
			if(params.length != 2){
				throw new QopException("se esperaban 2 parametros para el VAL");
			}
			else{
				//TODO Hacer el llamado al metodo validata que retorna un Boolean
				Boolean b = new Boolean(true); //validata(params[0],params[1]);
				String expression = this.expressionStr.substring(0,valIndex) + 
					b.toString();
				if(pos + 1 < size){
					expression += this.expressionStr.substring(pos + 1);
				}
				this.expressionStr = expression; 
			}
			size = this.expressionStr.length();
			valIndex = this.expressionStr.indexOf("VAL");		
		}
	}
	
	private void replaceVars() throws QopException{
		Set <String> keys = (Set<String>) this.values.keySet();
		for(String key: keys){
			String v=this.values.get(key);
			
			if (v!=null && !v.equals("")){
				TypeExpression t = inferType(v);
				if (t==null){ //es una cadena: MACHETAZO!
					v="\'"+v+"\'";
				}
				
				//System.out.println("############### [Expression] expressionStr: "+expressionStr);
				
				//String keyOnExpression = this.expressionStr.substring(0, key.length());
				
				this.expressionStr = this.expressionStr.replace(key, v);
				//System.out.println("############### [Expression] expressionStr: "+expressionStr);
				
			}
		}
	}
	
	
	private Operand parseOperand() throws QopException{
		int begin = this.position;
		//TODO: que pasa si el operando es una cadena de texto con espacios?
		int size = this.expressionStr.length();
		while(this.position < size && (!isOperationToken(this.expressionStr.charAt(this.position)))){
			this.position ++;			
		}
		
		String operandStr = this.expressionStr.substring(begin, this.position);
		operandStr = operandStr.trim();		
	
		TypeExpression typeExpression= inferType(operandStr);
		if (typeExpression!=null){
			//System.out.println("PARSEANDO OPERADOR:"+operandStr+" TYPEEXPR:"+typeExpression.getClass().getName());
			return new Operand(typeExpression.buildType(operandStr), typeExpression);
		} 
		else {// es una variable no reemplazada
			//System.out.println("PARSEANDO OPERADOR:"+operandStr+" TYPEEXPR:NULL");
			return new Operand(null,null);
		}
	}
			
	private boolean isOperationToken(char ch){
		switch(ch)
		{
		case '>':			
		case '<':
		case '=':			
		case '!':
		case '&':			
		case '|':
		case ')':
		case '(':
			return true;
		default:
			return false;
		}		
	}	
}
