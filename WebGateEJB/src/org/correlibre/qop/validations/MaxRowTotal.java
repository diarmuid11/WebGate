package org.correlibre.qop.validations;

import java.util.List;
import java.util.Map;

import org.correlibre.qop.domain.Question;
import org.correlibre.qop.domain.Validation;
import org.correlibre.qop.services.QopException;

public class MaxRowTotal extends Expression {
	
	public MaxRowTotal() throws QopException { 
		super();
	}

	@Override
	public String conformExpression(Question question,
			Map<Question, String> answers, Validation validation) {

		List<Question> colQuestions = question.getQuestions();  
		
		double sum = 0;
		
		for (int i = 0; i < colQuestions.size(); i++) {
			
			Question q = colQuestions.get(i);
			//System.out.println("################################################");
			//System.out.println("###### [MaxRowTotal] q: "+q);
			String valueStr = answers.get(q);
			//System.out.println("###### [MaxRowTotal] valueStr: "+(valueStr != null && valueStr.length() > 0 ? valueStr : "0.0"));
			//System.out.println("###############################################");
			
			sum += Double.parseDouble(valueStr != null && valueStr.length() > 0 ? valueStr : "0.0");
			
		}
		
		//validation.getRule()
		/*
		if(sum == Double.parseDouble(totalValueStr != null && totalValueStr.length() > 0 ? totalValueStr : "0.0"))
			return true;
		else
			return false;
			*/
		return sum+"="+validation.getRule();
		
	}

}