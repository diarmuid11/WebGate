package org.correlibre.qop.fillviewadapters;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.correlibre.qop.domain.Question;

public class MatrixQuestionInstance extends BaseFillViewAdapter {
	
	protected List<Question> rowOptions1;
	protected List<Question> rowOptions2;
	protected List<Question> rowOptions3;
	protected List<Question> rowOptions4;
	
	protected Question paragraphQuestion;
	
	protected Question rowListQuestion1;
	protected Question rowListQuestion2;
	protected Question rowListQuestion3;
	protected Question rowListQuestion4;
	
	private Map<Question,String> selectedItems1;
	private Map<Question,String> selectedItems2;
	private Map<Question,String> selectedItems3;
	private Map<Question,String> selectedItems4;
	
	private HashMap<Question,HashMap<Question,String>> selectedItems;
	
	private String paragraphAnswer;
	
	protected boolean activeRowListQuestion1;
	protected boolean activeRowListQuestion2;
	protected boolean activeRowListQuestion3;
	protected boolean activeRowListQuestion4;
	
	protected String questionType1;
	protected String questionType2;
	protected String questionType3;
	protected String questionType4;
	
	public void init(){
		
		List<Question> subQuestions = srvSurveysEngine.getSubQuestions(question, 17);
		
		List<Question> subParagraphQuestions = srvSurveysEngine.getSubQuestions(question, 16);
		
		if(subParagraphQuestions != null && subParagraphQuestions.size() > 0)
			paragraphQuestion = subParagraphQuestions.get(0);
		
		if(subQuestions.size() == 1){
			rowListQuestion1 = subQuestions.get(0);
			
			activeRowListQuestion1 = true;
			activeRowListQuestion2 = false;
			activeRowListQuestion3 = false;
			activeRowListQuestion4 = false;
			
		}else if(subQuestions.size() == 2){
			rowListQuestion1 = subQuestions.get(0);
			rowListQuestion2 = subQuestions.get(1);
			
			activeRowListQuestion1 = true;
			activeRowListQuestion2 = true;
			activeRowListQuestion3 = false;
			activeRowListQuestion4 = false;
			
		}else if(subQuestions.size() == 3){
			rowListQuestion1 = subQuestions.get(0);
			rowListQuestion2 = subQuestions.get(1);
			rowListQuestion3 = subQuestions.get(2);
			
			activeRowListQuestion1 = true;
			activeRowListQuestion2 = true;
			activeRowListQuestion3 = true;
			activeRowListQuestion4 = false;
			
		}else if(subQuestions.size() == 4){
			rowListQuestion1 = subQuestions.get(0);
			rowListQuestion2 = subQuestions.get(1);
			rowListQuestion3 = subQuestions.get(2);
			rowListQuestion4 = subQuestions.get(3);
			
			activeRowListQuestion1 = true;
			activeRowListQuestion2 = true;
			activeRowListQuestion3 = true;
			activeRowListQuestion4 = true;
		}
		
		
		rowOptions1 = rowListQuestion1.getQuestions();
		questionType1 = ""+rowOptions1.get(0).getQuestionType().getId(); 
		
		if(activeRowListQuestion2){
			rowOptions2 = rowListQuestion2.getQuestions();
			questionType2 = ""+rowOptions2.get(0).getQuestionType().getId();
		}
		
		
		if(activeRowListQuestion3){
			rowOptions3 = rowListQuestion3.getQuestions();
			questionType3 = ""+rowOptions3.get(0).getQuestionType().getId();

		}
		
		if(activeRowListQuestion4){
			rowOptions4 = rowListQuestion4.getQuestions();
			questionType4 = ""+rowOptions4.get(0).getQuestionType().getId();

		}

	}

	
	@Override
	public Map<Question, String> getAnswer() {
		
		Map<Question, String> answers = new HashMap<Question, String> ();
		
		System.out.println("###### [MatrixQuestionInstance2] selectedItems: "+selectedItems);
		
		for (Question q: rowOptions1){
			
			HashMap<Question, String> f = null;
			f = selectedItems.get(q);
			
			String v1 = null;
			String v2 = null; 
			String v3 = null;
			String v4 = null;
			
			v1 = f.get(rowListQuestion1);
			
			v1 = formatValue(q, v1);
			
			answers.put(q, v1);
			
			if(activeRowListQuestion2){
				v2 = f.get(rowListQuestion2);
				
				for (Question q2: rowOptions2){
					
					if(q2.getText().equalsIgnoreCase(q.getText())){
						v2 = formatValue(q2, v2);
						answers.put(q2, v2);
						break;
					}
				}
			}
			
			if(activeRowListQuestion3){
				v3 = f.get(rowListQuestion3);
				
				for (Question q3: rowOptions3){
					
					if(q3.getText().equalsIgnoreCase(q.getText())){
						v3 = formatValue(q3, v3);
						answers.put(q3, v3);
						break;
					}
				}
			}
			
			if(activeRowListQuestion4){
				v4 = f.get(rowListQuestion4);
				
				for (Question q4: rowOptions4){
					
					if(q4.getText().equalsIgnoreCase(q.getText())){
						v4 = formatValue(q4, v4); 
						answers.put(q4, v4);
						break;
					}
				}
			}
			
		}
		
		if(paragraphQuestion != null)
			answers.put(paragraphQuestion, paragraphAnswer);
		
		return answers;
	}
	
	public String formatValue(Question q, String value){
		
		int questionType = q.getQuestionType().getId();
		
		switch (questionType) {
		case 8://numerica
			if(value == null || value.length()==0)
				return "0";
			else
				return value;
			
		default:
			return value;
			
		}
		
		
	}

	@Override
	public void setAnswer(Map<Question, String> answers) {
		
		HashMap<Question, String> si1 = null;
		HashMap<Question, String> si2 = null;
		HashMap<Question, String> si3 = null;
		HashMap<Question, String> si4 = null;
		
		selectedItems = new HashMap<Question, HashMap<Question, String>>();
		
		si1 = new HashMap<Question, String>();
		int i = 0;
		
		for (Question q: rowOptions1){
			i = 1;
			if (answers.get(q) != null){
				si1.put(q, answers.get(q));
				i++;
			}
		}
		
		if(activeRowListQuestion2){
			si2 = new HashMap<Question, String>();
			
			for (Question q: rowOptions2){
				i = 1;
				if (answers.get(q) != null){
					si2.put(q, answers.get(q));
					i++;
				}
			}
		}
		
		if(activeRowListQuestion3){
			si3 = new HashMap<Question, String>();
			for (Question q: rowOptions3){
				i = 1;
				if (answers.get(q) != null){
					si3.put(q, answers.get(q));
					i++;
				}
			}
		}
		
		if(activeRowListQuestion4){
			si4 = new HashMap<Question, String>();
			for (Question q: rowOptions4){
				i = 1;
				if (answers.get(q) != null){
					si4.put(q, answers.get(q));
					i++;
				}
			}
		}
		
		for (int j = 0; j < rowOptions1.size(); j++){
			
			Question rowQ = rowOptions1.get(j);
			
			HashMap<Question, String> f = new HashMap<Question, String>();
			
			f.put(rowListQuestion1, si1.get(rowQ));
			
			if(activeRowListQuestion2){
				
				for (Question rowQ2 : rowOptions2) {
					if(rowQ2.getText().equalsIgnoreCase(rowQ.getText())){
						f.put(rowListQuestion2, si2.get(rowQ2));
						break;
					}
				}
				
			}
			
			if(activeRowListQuestion3){
				for (Question rowQ3 : rowOptions3) {
					if(rowQ3.getText().equalsIgnoreCase(rowQ.getText())){
						f.put(rowListQuestion3, si3.get(rowQ3));
						break;
					}
				}
				
			}
			
			if(activeRowListQuestion4){
				for (Question rowQ4 : rowOptions4) {
					if(rowQ4.getText().equalsIgnoreCase(rowQ.getText())){
						f.put(rowListQuestion4, si4.get(rowQ4));
						break;
					}
				}
				
			}
			
			selectedItems.put(rowQ, f);
			
		}
		
		if(paragraphQuestion != null)
			paragraphAnswer = answers.get(paragraphQuestion);
	}
	
	public List<Question> getRowOptions1() {
		return rowOptions1;
	}

	public void setRowOptions1(List<Question> rowOptions1) {
		this.rowOptions1 = rowOptions1;
	}

	public List<Question> getRowOptions2() {
		return rowOptions2;
	}

	public void setRowOptions2(List<Question> rowOptions2) {
		this.rowOptions2 = rowOptions2;
	}

	public List<Question> getRowOptions3() {
		return rowOptions3;
	}

	public void setRowOptions3(List<Question> rowOptions3) {
		this.rowOptions3 = rowOptions3;
	}

	public Question getRowListQuestion1() {
		return rowListQuestion1;
	}

	public void setRowListQuestion1(Question rowListQuestion1) {
		this.rowListQuestion1 = rowListQuestion1;
	}

	public Question getRowListQuestion2() {
		return rowListQuestion2;
	}

	public void setRowListQuestion2(Question rowListQuestion2) {
		this.rowListQuestion2 = rowListQuestion2;
	}

	public Question getRowListQuestion3() {
		return rowListQuestion3;
	}

	public void setRowListQuestion3(Question rowListQuestion3) {
		this.rowListQuestion3 = rowListQuestion3;
	}

	public Map<Question, String> getSelectedItems1() {
		return selectedItems1;
	}

	public void setSelectedItems1(Map<Question, String> selectedItems1) {
		this.selectedItems1 = selectedItems1;
	}

	public Map<Question, String> getSelectedItems2() {
		return selectedItems2;
	}

	public void setSelectedItems2(Map<Question, String> selectedItems2) {
		this.selectedItems2 = selectedItems2;
	}

	public Map<Question, String> getSelectedItems3() {
		return selectedItems3;
	}

	public void setSelectedItems3(Map<Question, String> selectedItems3) {
		this.selectedItems3 = selectedItems3;
	}

	public boolean isActiveRowListQuestion1() {
		return activeRowListQuestion1;
	}

	public void setActiveRowListQuestion1(boolean activeRowListQuestion1) {
		this.activeRowListQuestion1 = activeRowListQuestion1;
	}

	public boolean isActiveRowListQuestion2() {
		return activeRowListQuestion2;
	}

	public void setActiveRowListQuestion2(boolean activeRowListQuestion2) {
		this.activeRowListQuestion2 = activeRowListQuestion2;
	}

	public boolean isActiveRowListQuestion3() {
		return activeRowListQuestion3;
	}

	public void setActiveRowListQuestion3(boolean activeRowListQuestion3) {
		this.activeRowListQuestion3 = activeRowListQuestion3;
	}

	public String getQuestionType1() {
		return questionType1;
	}

	public void setQuestionType1(String questionType1) {
		this.questionType1 = questionType1;
	}

	public String getQuestionType2() {
		return questionType2;
	}

	public void setQuestionType2(String questionType2) {
		this.questionType2 = questionType2;
	}

	public String getQuestionType3() {
		return questionType3;
	}

	public void setQuestionType3(String questionType3) {
		this.questionType3 = questionType3;
	}

	public HashMap<Question, HashMap<Question, String>> getSelectedItems() {
		return selectedItems;
	}

	public void setSelectedItems(
			HashMap<Question, HashMap<Question, String>> selectedItems) {
		this.selectedItems = selectedItems;
	}

	public Question getParagraphQuestion() {
		return paragraphQuestion;
	}

	public void setParagraphQuestion(Question paragraphQuestion) {
		this.paragraphQuestion = paragraphQuestion;
	}

	public String getParagraphAnswer() {
		return paragraphAnswer;
	}

	public void setParagraphAnswer(String paragraphAnswer) {
		this.paragraphAnswer = paragraphAnswer;
	}
	
	public String getQuestionName(){
		return question.getName();
	}


	public List<Question> getRowOptions4() {
		return rowOptions4;
	}


	public void setRowOptions4(List<Question> rowOptions4) {
		this.rowOptions4 = rowOptions4;
	}


	public Question getRowListQuestion4() {
		return rowListQuestion4;
	}


	public void setRowListQuestion4(Question rowListQuestion4) {
		this.rowListQuestion4 = rowListQuestion4;
	}


	public Map<Question, String> getSelectedItems4() {
		return selectedItems4;
	}


	public void setSelectedItems4(Map<Question, String> selectedItems4) {
		this.selectedItems4 = selectedItems4;
	}


	public boolean isActiveRowListQuestion4() {
		return activeRowListQuestion4;
	}


	public void setActiveRowListQuestion4(boolean activeRowListQuestion4) {
		this.activeRowListQuestion4 = activeRowListQuestion4;
	}


	public String getQuestionType4() {
		return questionType4;
	}


	public void setQuestionType4(String questionType4) {
		this.questionType4 = questionType4;
	}

}
