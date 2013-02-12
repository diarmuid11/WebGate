package org.correlibre.qop.fillviewadapters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.correlibre.qop.domain.Question;
import org.correlibre.qop.services.QopException;
import org.correlibre.qop.util.BeanUtil;

public class TupleQuestion extends BaseFillViewAdapter {

	
	List<Question> elements=new ArrayList<Question>();
			
	
	public void init() throws QopException{
		elements = srvSurveysEngine.getSubQuestions(question, null);
		if (elements.size()==0){
			throw new QopException("La tupla no tiene subpreguntas");
		}
	}
	
	@Override
	public Map<Question, String> getAnswer() {
		Map<Question,String> answers=new HashMap<Question, String>();
		for (Question elem:elements){
			FillViewAdapter fva=this.getSubFillViewAdapters().get(elem);
			answers.putAll(fva.getAnswer());
		}
		return answers;
	}

	@Override
	public void setAnswer(Map<Question, String> answers) {
		/*
		for (Question elem:elements){
			FillViewAdapter fva=this.getSubFillViewAdapters().get(elem);
		}
		*/
	}

	public List<Question> getElements() {
		return elements;
	}
	
	

}
