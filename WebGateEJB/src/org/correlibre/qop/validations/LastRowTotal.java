package org.correlibre.qop.validations;

import java.util.List;
import java.util.Map;

import org.correlibre.qop.domain.Question;
import org.correlibre.qop.domain.Validation;
import org.correlibre.qop.services.QopException;

public class LastRowTotal implements Validator {

	@Override
	public boolean validate(Question question, Map<Question, String> answers,
			Validation validation) throws QopException {

		List<Question> colQuestions = question.getQuestions();
		
		int lastIndexQuestion = colQuestions.size() - 1;  
		
		double sum = 0;
		
		for (int i = 0; i < colQuestions.size()-1; i++) {
			
			Question q = colQuestions.get(i);
			System.out.println("################################################");
			System.out.println("###### [LastRowTotal] q: "+q);
			String valueStr = answers.get(q);
			System.out.println("###### [LastRowTotal] valueStr: "+(valueStr != null && valueStr.length() > 0 ? valueStr : "0.0"));
			System.out.println("################################################");
			
			sum += Double.parseDouble(valueStr != null && valueStr.length() > 0 ? valueStr : "0.0");
			
		}
		
		Question lastQuestion = colQuestions.get(lastIndexQuestion);
		
		System.out.println("######## [LastRowTotal] lastQuestion: "+lastQuestion);
		
		String totalValueStr = answers.get(lastQuestion);
		
		System.out.println("######## [LastRowTotal] totalValueStr: "+totalValueStr != null && totalValueStr.length() > 0 ? totalValueStr : "0.0");
		System.out.println("######## [LastRowTotal] sum: "+sum);
		
		if(sum == Double.parseDouble(totalValueStr != null && totalValueStr.length() > 0 ? totalValueStr : "0.0"))
			return true;
		else
			return false;
	}

}
