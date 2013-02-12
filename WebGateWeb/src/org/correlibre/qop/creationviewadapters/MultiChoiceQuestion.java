package org.correlibre.qop.creationviewadapters;

import java.util.ArrayList;
import java.util.List;

import javax.faces.event.ActionEvent;

import org.correlibre.qop.domain.Question;
import org.correlibre.qop.domain.QuestionType;
import org.correlibre.qop.services.QopException;

public class MultiChoiceQuestion extends BaseCreationViewAdapter {

	protected List<Question> categoryOptions;
	protected List<Question> ratingOptions;
	
	private String newCategoryOption;
	private String newRatingOption;
	
	protected Question categoryQuestion;
	protected Question ratingQuestion;

	public MultiChoiceQuestion() throws QopException {
		super();
	}

	public void init() {
		
		System.out.println("############### [MultiChoiceQuestion] creating: "+creating);
		if (creating){
			
			categoryOptions =  new ArrayList<Question>();
			categoryQuestion = new Question();
			QuestionType qt=new QuestionType();
			qt.setId(2);
			categoryQuestion.setText("Categorias");
			categoryQuestion.setQuestionType(qt);
			categoryQuestion.setQuestions(categoryOptions);
			categoryQuestion.setQuestion(question);
			
			ratingOptions =  new ArrayList<Question>();
			ratingQuestion = new Question();
			QuestionType qt2=new QuestionType();
			qt2.setId(2);
			ratingQuestion.setText("Calificaciones");
			ratingQuestion.setQuestionType(qt2);
			ratingQuestion.setQuestions(ratingOptions);
			ratingQuestion.setQuestion(question);
		}else{
			
			List<Question> subQuestions = engine.getSubQuestions(question, 2);
			
			categoryQuestion = subQuestions.get(0);
			categoryOptions = categoryQuestion.getQuestions();
			
			ratingQuestion = subQuestions.get(1);
			ratingOptions = ratingQuestion.getQuestions();
			
		}
	}


	public void addRatingOption(ActionEvent e){
		Question q=new Question();
		q.setText(newRatingOption);
		q.setQuestion(ratingQuestion);
		QuestionType qt=new QuestionType();
		qt.setId(7);
		q.setQuestionType(qt);
		q.setSurveyStructure(question.getSurveyStructure());
		ratingOptions.add(q);
	}
	
	public void removeRatingOption(ActionEvent e){
		Question q= (Question)e.getComponent().getAttributes().get("ratingOption");
		q.setForRemoval(true);
	}
	
	public void undoRemoveRatingOption(ActionEvent e){
		Question q= (Question)e.getComponent().getAttributes().get("ratingOption");
		q.setForRemoval(false);
	}
	
	public void addCategoryOption(ActionEvent e){
		Question q=new Question();
		q.setText(newCategoryOption);
		q.setQuestion(categoryQuestion);
		QuestionType qt=new QuestionType();
		qt.setId(7);
		q.setQuestionType(qt);
		q.setSurveyStructure(question.getSurveyStructure());
		categoryOptions.add(q);
	}
	
	public void removeCategoryOption(ActionEvent e){
		Question q= (Question)e.getComponent().getAttributes().get("categoryOption");
		q.setForRemoval(true);
	}
	
	public void undoRemoveCategoryOption(ActionEvent e){
		Question q= (Question)e.getComponent().getAttributes().get("categoryOption");
		q.setForRemoval(false);
	}

	public void adaptEditedSubQuestions(){
		editedSubQuestions.clear();
		editedSubQuestions.add(categoryQuestion);
		editedSubQuestions.add(ratingQuestion);
		
	}

	public void setNewCategoryOption(String newCategoryOption) {
		this.newCategoryOption = newCategoryOption;
	}

	public String getNewCategoryOption() {
		return newCategoryOption;
	}
	
	public List<Question> getRatingOptions() {
		return ratingOptions;
	}

	public String getNewRatingOption() {
		return newRatingOption;
	}

	public void setNewRatingOption(String newRatingOption) {
		this.newRatingOption = newRatingOption;
	}

	public List<Question> getCategoryOptions() {
		return categoryOptions;
	}

	public void setCategoryOptions(List<Question> categoryOptions) {
		this.categoryOptions = categoryOptions;
	}

	
}