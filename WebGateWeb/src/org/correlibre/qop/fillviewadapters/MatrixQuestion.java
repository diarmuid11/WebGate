package org.correlibre.qop.fillviewadapters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.transaction.Transaction;

import org.correlibre.qop.domain.Question;
import org.correlibre.qop.services.QopException;

public class MatrixQuestion extends BaseFillViewAdapter {

	
	private DataModel<Question> rowDataModel=null;
	private DataModel<Question> columnDataModel=null;		
	private List<List<String>> traverse=null;
	
	private List<List<Question>> matrix=new ArrayList<List<Question>>();
	
	
	public void init() throws QopException{
		List<Question> tuples = srvSurveysEngine.getSubQuestions(question, 21);
		if (tuples.size()==0){
			throw new QopException("La matriz no tiene subtuplas");
		}
		
		for (Question tuple:tuples){
			List<Question> caux=srvSurveysEngine.getSubQuestions(tuple, null);
			matrix.add(caux);
		}
		columnDataModel = new ListDataModel<Question>(tuples);  //preguntas tupla
		traverse=new ArrayList<List<String>>();
		for (int i=0;i<matrix.get(0).size();i++){
			List<String> row=new ArrayList<String>();
			for (List<Question> tuple:matrix){
				row.add(new String());
			}
			traverse.add(row);
		}
		rowDataModel=new ListDataModel<Question>(matrix.get(0));  //preguntas base
	}
	
	@Override
	public Map<Question, String> getAnswer() {
		Map<Question,String> answers=new HashMap<Question, String>();
		//traverse again
		System.out.println("wtf3:"+traverse);
		for (int i=0;i<matrix.size();i++){
			List<Question> tuple=matrix.get(i);
			for (int j=0;j<tuple.size();j++){
				String answer=traverse.get(j).get(i);
				System.out.println("wtf4: answer:"+answer +"j"+j+"i"+i);
				answers.put(tuple.get(j), answer);
			}
		}
		return answers;
    }

	@Override
	public void setAnswer(Map<Question, String> answers) {
		//traverse again
		for (int i=0;i<matrix.size();i++){
			List<Question> tuple=matrix.get(i);
			for (int j=0;j<tuple.size();j++){
				String answer=answers.get(tuple.get(j));
				traverse.get(j).set(i, answer);
				System.out.println("wtf2:"+traverse);
			}
		}
	}

	public DataModel<Question> getRowDataModel() {
		return rowDataModel;
	}

	public DataModel<Question> getColumnDataModel() {
		return columnDataModel;
	}
	
	public String getCell() {
		DataModel<Question> rowDMAux=getRowDataModel();
		if (rowDMAux.isRowAvailable()){
			int indexR=rowDMAux.getRowIndex();
			DataModel<Question> columnsDMAux=getColumnDataModel();
			if (columnsDMAux.isRowAvailable()){
				int indexC=columnsDMAux.getRowIndex();
				System.out.println("datatable index R:"+indexR +"indexC"+indexC);
				return traverse.get(indexR).get(indexC);
			}
		}
		return null;
	}

	public void setCell(String valor) {
		DataModel<Question> rowDMAux=getRowDataModel();
		if (rowDMAux.isRowAvailable()){
			int indexR=rowDMAux.getRowIndex();
			DataModel<Question> columnsDMAux=getColumnDataModel();
			if (columnsDMAux.isRowAvailable()){
				int indexC=columnsDMAux.getRowIndex();
				System.out.println("datatablew index R:"+indexR +"indexC"+indexC);
				traverse.get(indexR).set(indexC,valor);
			}
		}
	}


}
