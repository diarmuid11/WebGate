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
package org.correlibre.qop.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.correlibre.qop.domain.Question;
import org.correlibre.qop.domain.Validation;
import org.correlibre.qop.parsing.Expression;
import org.correlibre.qop.parsing2.ParseException;
import org.correlibre.qop.parsing2.QOPGrammar;
import org.correlibre.qop.util.DynamicClassLoader;
import org.correlibre.qop.validations.Validator;


/**
 * Session Bean implementation class SrvSurveysValidationEJB
 */
@Stateless
public class SrvSurveysValidationEJB implements SrvSurveysValidationLocal {

	@PersistenceContext
	private EntityManager em;
	
	@EJB
    private SrvSurveysEngineLocal srvSurveysEngine;

	
    /**
     * Default constructor. 
     */
    public SrvSurveysValidationEJB() {
        // TODO Auto-generated constructor stub
    }

    @Override
	public boolean evalExpression(String expressionStr,Map<Question, String> answers) throws QopException{
    	/*
    	Expression e = new Expression(expressionStr,answers);
    	return e.evalExpression();
    	*/
    	Boolean validationValue = null;
    	Map<String, String> values = this.getValues(answers);
    	
    	System.out.println("expressionStr: "+expressionStr);
    	
    	QOPGrammar parser = new QOPGrammar(expressionStr, values);
    	
    	try {
			parser.parse();
			
			boolean tmp = parser.getResult();
	    	
			System.out.println("result: "+tmp);
			
	    	validationValue = new Boolean(tmp);
			
		} catch (ParseException e) {
			e.printStackTrace();
			throw new QopException(e.getMessage());
		} catch (Exception e){
			e.printStackTrace();
			throw new QopException(e.getMessage());
		}
    	
    	return validationValue.booleanValue();
    	
    }
    
    private HashMap<String, String> getValues(Map<Question, String> vars){
		HashMap<String, String> values = new HashMap<String, String>();
		Set<Question> questions = (Set<Question>)vars.keySet();
		for(Question q: questions){
			String tmp = vars.get(q);
			if(tmp != null && tmp.trim().length()==0)
				tmp = "null";
			else if(q.getQuestionType().getId() == 1 || q.getQuestionType().getId() == 16 || q.getQuestionType().getId() == 15)
				tmp = "\'"+tmp+"\'";
			
			values.put(q.getKey(), tmp);
		}
		return values;
	}
    
    @Override
	public boolean validate(String questionKey,String validationKey,Map<Question, String> answers) throws QopException{
    	Query q=em.createNamedQuery("Validation.findByQuestionKeyAndValidationKey");
    	q.setParameter("questionKey", questionKey);
    	q.setParameter("validationKey", validationKey);
    	Validation v=null;
    	try {
			v = (Validation) q.getSingleResult();
		} catch (NonUniqueResultException e) {
			throw new QopException("Se encontró más de una validación con la key: "+validationKey+" para la pregunta: "+questionKey);
		} catch (NoResultException e) {
			throw new QopException("No se encontraron validaciones con la key: "+validationKey+" para la pregunta: "+questionKey);
		}
		return validate(v.getQuestion() , answers, v);
    }
    
    private String generateMessage(Validation v,Question question,String answer){
    	String message=v.getMessage();
    	if (message==null){
    		message=v.getValidationType().getMessage();
    	}
    	if (message==null){
    		message="Error en validación: "+v.getKey();
    		return message;
    	}
    	message=message.replaceAll("\\$question", question.getKey());
    	message=message.replaceAll("\\$validation", v.getRule());
    	message=message.replaceAll("\\$answer", answer);
    	return message;
    }
    
    @Override
	public void validateQuestion(Question question, Map<Question, String> answers, Map<Validation,String> result) throws QopException{
    	List<Validation> lstVal = question.getValidations();
    	
    	for (Validation v:lstVal){
    		if (!validate(question,answers,v)){
    			result.put(v, generateMessage(v, question, answers.get(question)));
    		}
    	}
		for(Question subQ:srvSurveysEngine.getSubQuestions(question, null)){
			validateQuestion(subQ,answers,result);
		}
    }
    
    
    @Override
	public boolean validate(Question question,Map<Question, String> answers,Validation validation) throws QopException{
    	String validator = validation.getValidationType().getValidator();
    	Validator v = DynamicClassLoader.loadClass(validator);
    	return v.validate(question, answers, validation);
    }
    
    
    

}
