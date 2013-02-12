package org.correlibre.qop.fillviewadapters;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.model.SelectItem;

import org.correlibre.qop.domain.Question;
import org.correlibre.qop.services.QopException;
import org.correlibre.qop.util.BeanUtil;

public class MultiOptionSelectOneMenuQuestion extends BaseFillViewAdapter {

	protected List<SelectItem> ratingOptions;
	protected Question categoryQuestion;
	protected Question ratingQuestion;
	
	protected List<Question> categoryOptions;
	
	private Map<Question,String> selectedItems;
	
	public void init() throws QopException{
		
		List<Question> subQuestions = srvSurveysEngine.getSubQuestions(question, 2);
		
		try {
			categoryQuestion = subQuestions.get(0);
			ratingQuestion = subQuestions.get(1);
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new QopException("Las subpreguntas no están bien configuradas", e);
		}
		
		ratingOptions = BeanUtil.getInstance().getQuestionsAsSelectItems(ratingQuestion.getQuestions());
		categoryOptions = categoryQuestion.getQuestions();
		
	}
	
	@Override
	public Map<Question, String> getAnswer() {
		Map<Question, String> answers = new HashMap<Question, String> ();
		for (Question q: categoryQuestion.getQuestions()){
			if (selectedItems.get(q) != null){
				answers.put(q, selectedItems.get(q));
			}
		}
		return answers;
	}

	@Override
	public void setAnswer(Map<Question, String> answers) {
		selectedItems= new HashMap<Question, String>();
		for (Question q: categoryQuestion.getQuestions()){
			if (answers.get(q) != null){
				selectedItems.put(q, answers.get(q));
			}
		}
	}

	public List<Question> getCategoryOptions() {
		return categoryOptions;
	}

	public void setCategoryOptions(List<Question> categoryOptions) {
		this.categoryOptions = categoryOptions;
	}

	public Question getRatingQuestion() {
		return ratingQuestion;
	}

	public void setRatingQuestion(Question ratingQuestion) {
		this.ratingQuestion = ratingQuestion;
	}

	public List<SelectItem> getRatingOptions() {
		return ratingOptions;
	}

	public void setRatingOptions(List<SelectItem> ratingOptions) {
		this.ratingOptions = ratingOptions;
	}

	public Map<Question, String> getSelectedItems() {
		return selectedItems;
	}

	public void setSelectedItems(Map<Question, String> selectedItems) {
		this.selectedItems = selectedItems;
	}

}
