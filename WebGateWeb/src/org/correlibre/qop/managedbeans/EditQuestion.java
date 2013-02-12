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
package org.correlibre.qop.managedbeans;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import org.correlibre.qop.creationviewadapters.CreationViewAdapter;
import org.correlibre.qop.domain.Question;
import org.correlibre.qop.domain.QuestionType;
import org.correlibre.qop.domain.SurveyStructure;
import org.correlibre.qop.domain.Validation;
import org.correlibre.qop.domain.ValidationType;
import org.correlibre.qop.services.QopException;
import org.correlibre.qop.services.SrvSurveysAdminLocal;
import org.correlibre.qop.util.BeanUtil;
import org.correlibre.qop.util.SelectItemStringLabelComparator;
import org.correlibre.qop.util.WebDynamicClassLoader;

@ManagedBean(name="editQuestion")
@ViewScoped
public class EditQuestion {
	
	@EJB
	private SrvSurveysAdminLocal srvSurveysAdmin;	

	private Question question;
	
	private boolean creating;
	
	private SurveyStructure surveyStructure;
	
	private String questionTypeId;
	
	private List<SelectItem> questionTypeItems;
	
	private List<QuestionType> questionTypes;
	
	public List<Validation> newValidations;
	
	public List<Validation> validations;

	private CreationViewAdapter creationViewAdapter;
	
	private Validation editedValidation;
	
	private List<ValidationType> validationTypes;
	
	private List<SelectItem> validationTypeItems;
	
	private String validationTypeId;
	
	private boolean newValidation;
	
	@PostConstruct
	public void init() throws QopException{
		
		System.out.println("############### [EditQuestion] init");
		
		Flash f=FacesContext.getCurrentInstance().getExternalContext().getFlash();
		surveyStructure = (SurveyStructure) f.get("surveyStructure");
		//viene de edit_survey_structures o admin_survey_structures
		Question toEdit=(Question)f.get("questionToEdit");
		//viene de edit_validations
		Question editing=(Question)f.get("questionEditing");
		
		System.out.println("############### [EditQuestion] surveyStructure: "+surveyStructure);
		
		if(surveyStructure == null){
			HttpServletRequest request=(HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
			surveyStructure = (SurveyStructure) request.getAttribute("surveyStructure");
		}
		
		System.out.println("############### [EditQuestion] surveyStructure (2): "+surveyStructure);
		
		if (toEdit==null && editing == null){
			initCreationMode();
		}else{
			if (toEdit!=null){
				question = toEdit;
			}else{
				question=editing;
			}
			initCreationViewAdapters();
			creating = false;
		}
		initValidationTypes();
		validations=new ArrayList<Validation>(question.getValidations());
		newValidations=new ArrayList<Validation>();
	}		
	
	private void initCreationMode(){
		
		if (questionTypes==null){
			initQuestionTypes();
		}
		
		question = new Question();
		question.setQuestionType(questionTypes.get(0));
		question.setValidations(new ArrayList<Validation>());
		questionTypeId=questionTypeItems.get(0).getValue().toString();
		question.setSurveyStructure(surveyStructure);
		creating = true;
	}

	private void initQuestionTypes() {
		questionTypes = srvSurveysAdmin.getQuestionTypes();
		questionTypeItems=new ArrayList<SelectItem>();
		for(QuestionType qt: questionTypes){
			questionTypeItems.add(new SelectItem(qt.getId(), qt.getName()));
		}
		Collections.sort(questionTypeItems, new SelectItemStringLabelComparator());
	}
	
	private void initValidationTypes() {
		validationTypes=question.getQuestionType().getValidationTypes();

		System.out.println("################## [EditQuestion] question.getQuestionType().getName(): "+question.getQuestionType().getName());
		System.out.println("################## [EditQuestion] validationTypes.size(): "+validationTypes.size());
		validationTypeItems=new ArrayList<SelectItem>();
		for(ValidationType vt: validationTypes){
			System.out.println("################## [EditQuestion] vt.getId(): "+vt.getId());
			validationTypeItems.add(new SelectItem(vt.getId(), vt.getName()));
		}
	}
	

	public void chooseQuestionType(ValueChangeEvent e) throws QopException{
		String qtId=(String)e.getNewValue();
		QuestionType qtAux=new QuestionType();
		qtAux.setId(new Integer(qtId));
		int idx=questionTypes.indexOf(qtAux);
		question.setQuestionType(questionTypes.get(idx));
		
		System.out.println("################# [EditQuestion] question.getQuestionType().getName(): " + question.getQuestionType().getName());
		validationTypes = question.getQuestionType().getValidationTypes();
		System.out.println("################# [EditQuestion] validationTypes: " + validationTypes);
		
		initCreationViewAdapters();
	}

	private void initCreationViewAdapters() throws QopException{
		String cvac = question.getQuestionType().getCreationViewAdapter();
		Timestamp inicio = new Timestamp((new Date()).getTime());
		if (cvac!=null){
			
			creationViewAdapter = WebDynamicClassLoader.loadClass(cvac);
			creationViewAdapter.setQuestionToEdit(question);
			creationViewAdapter.setCreating(creating);
			creationViewAdapter.init();
		}else{
			creationViewAdapter=null;
		}
		Timestamp fin = new Timestamp((new Date()).getTime());
		
		System.out.println("##### [EditQuestion] initCreationViewAdapters ("+cvac+") time: "+(fin.getTime()-inicio.getTime()));
	}


	private Question saveQuestion() throws QopException{
		editedValidation=null;
		Question q;
		List<Question> editedSubQuestions=null;
		
		if (creationViewAdapter != null){
			q=creationViewAdapter.getEditedQuestion();
			q.setText(question.getText());
			q.setQuestionType(question.getQuestionType());
			q.setValidations(question.getValidations());
			editedSubQuestions=creationViewAdapter.getEditedSubQuestions();
		}else{
			q=question;
		}
		System.out.println("############# [EditQuestion] q: "+q);
		
		if (creating){
			return srvSurveysAdmin.createQuestion(surveyStructure, q, editedSubQuestions);
		}else{
			return srvSurveysAdmin.modifyQuestion(q,editedSubQuestions,newValidations);
		}
		
		
		
	}

	public String justSaveQuestion(){
		try {
			question = saveQuestion();
			validations = question.getValidations();
			newValidations = new ArrayList<Validation>();
			creating = false;
			initCreationViewAdapters();
			initValidationTypes();
			
			BeanUtil.getInstance().addMessage(FacesMessage.SEVERITY_INFO,"La pregunta se ha guardado satisfactoriamente");
		} catch (QopException e) {
			
			BeanUtil.getInstance().addMessage(FacesMessage.SEVERITY_ERROR,"Ha ocurrido un error guardando la pregunta: " + e.getMessage());
			e.printStackTrace();
		} catch (NullPointerException e) {
			
			BeanUtil.getInstance().addMessage(FacesMessage.SEVERITY_ERROR,"Ha ocurrido un error guardando la pregunta: " + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	//solo cuando creating es false
	public String saveAndGoToNextQuestion() throws QopException{
		Question q=saveQuestion();
		Question nextQ=srvSurveysAdmin.getNextQuestion(q);
		System.out.println("PREGUNTA ACTUAL:"+q.getKey()+":"+q.getId());

		
		if (nextQ==null){ //crear
			System.out.println("PREGUNTA NUEVA");
			initCreationMode();
		}else{
			System.out.println("PREGUNTA SIGUIENTE:"+nextQ.getKey()+":"+nextQ.getId());
			question=nextQ;
			validations = question.getValidations();
			newValidations=new ArrayList<Validation>();
			initCreationViewAdapters();
			creating = false;
		}
		initValidationTypes();
		
		return null;
	}
	
	//solo cuando creating es false
	public String saveAndGoToPreviousQuestion() {
		try {
			Question q=saveQuestion();
			Question previousQ=srvSurveysAdmin.getPreviousQuestion(q);
			question=previousQ;
			validations = question.getValidations();
			newValidations=new ArrayList<Validation>();
			initCreationViewAdapters();
			creating = false;
			initValidationTypes();
		} catch (QopException e) {
			e.printStackTrace();
			BeanUtil.getInstance().addMessage(e.getMessage());
		}
		return null;
	}
	
	public boolean isNextEnabled(){
		
		if(creating==false){
			
			try {
				Question nextQ=srvSurveysAdmin.getNextQuestion(question);
				
				//System.out.println("################ [EditQuestion] nextQ.getText(): "+nextQ.getText());
				
				if(nextQ != null)
					return true;
				else
					return false;
				
			} catch (QopException e) {
				e.printStackTrace();
				BeanUtil.getInstance().addMessage(e.getMessage());
			}
			
		}
		return false;
		
	}
	
	public boolean isNewEnabled(){
		
		if(creating==false){
			
			try {
				
				if(isNextEnabled())
					return false;
				
				Question nextQ=srvSurveysAdmin.getNextQuestion(question);
				
				if(nextQ == null)
					return true;
				else
					return false;
				
			} catch (QopException e) {
				e.printStackTrace();
				BeanUtil.getInstance().addMessage(e.getMessage());
			}
			
		}
		return false;
		
	}
	
	//solo cuando creating es false
	public String goToNextQuestion(){
		
		if(creating==false){
			
			try {
				Question nextQ=srvSurveysAdmin.getNextQuestion(question);

				if (nextQ!=null){
					System.out.println("PREGUNTA SIGUIENTE:"+nextQ.getKey()+":"+nextQ.getId());
					question=nextQ;
					validations = question.getValidations();
					newValidations=new ArrayList<Validation>();
					initCreationViewAdapters();
					creating = false;
				}
				initValidationTypes();
			} catch (QopException e) {
				e.printStackTrace();
				BeanUtil.getInstance().addMessage(e.getMessage());
			}
			
		}
		
		return null;
	}
	
	public boolean isPreviousEnabled(){
		
		if(creating==false){
			
			try {
				Question previousQ=srvSurveysAdmin.getPreviousQuestion(question);
				
				if(previousQ != question)
					return true;
				else
					return false;
				
			} catch (QopException e) {
				e.printStackTrace();
				BeanUtil.getInstance().addMessage(e.getMessage());
			}
			
		}else{
			return true;
		}
		
		return false;
		
	}
	
	//solo cuando creating es false
	public String goToPreviousQuestion(){
		
		if(creating==false){
			
			try {
				Question previousQ=srvSurveysAdmin.getPreviousQuestion(question);
				
				if(previousQ != null){
					question=previousQ;
					validations = question.getValidations();
					newValidations=new ArrayList<Validation>();
					initCreationViewAdapters();
					creating = false;
					initValidationTypes();
				}
				
			} catch (QopException e) {
				e.printStackTrace();
				BeanUtil.getInstance().addMessage(e.getMessage());
			}
			
		}
		
		return null;
	}
	
	public void newValidation(){
		
		editedValidation=new Validation();
		ValidationType vtAux=new ValidationType();
		vtAux.setId(Integer.valueOf(validationTypeId));
		int idx=validationTypes.indexOf(vtAux);
		editedValidation.setValidationType(validationTypes.get(idx));
		calculateValidationKey(editedValidation);
		//this.question.getValidations().add(e)
		newValidation=true;
		return;
	}
	
	//TODO:  Logica de negocio, cambiar a EJB
	private void calculateValidationKey(Validation newV){
		String prefix=question.getKey()+"_"+newV.getValidationType().getKeyPrefix()+"_";
		int i=1;
		outer:do{
			String key=prefix+i;
			for (Validation v:question.getValidations()){
				if (v.getKey().equals(key)){
					i++;
					continue outer;
				}
			}
			newV.setKey(key);
			return;
		}while (true);
	}

	public void acceptValidationChanges(){
		if (newValidation){
			newValidations.add(editedValidation);
			validations.add(editedValidation);
			newValidation=false;
		}
		editedValidation=null;
	}
	
	public void editValidation(Validation v){
		editedValidation=v;
	}
	
	public void removeValidation(){
		question.getValidations().remove(editedValidation);
	}

	public Validation getEditedValidation() {
		return editedValidation;
	}

	
	public List<SelectItem> getQuestionTypeItems() {
		return questionTypeItems;
	}
	
	public List<SelectItem> getValidationTypeItems() {
		return validationTypeItems;
	}

	public boolean isCreating() {
		return creating;
	}

	public String getQuestionTypeId() {
		return questionTypeId;
	}

	public void setQuestionTypeId(String questionTypeId) {
		this.questionTypeId = questionTypeId;
	}

	public String getValidationTypeId() {
		return validationTypeId;
	}

	public void setValidationTypeId(String validationTypeId) {
		this.validationTypeId = validationTypeId;
	}

	public Question getQuestion() {
		return question;
	}	
	
	public String getCreationViewComponent(){
		return question.getQuestionType().getCreationViewComponent();  
	}
	
	public CreationViewAdapter getCreationViewAdapter() {
		return creationViewAdapter;
	}

	public List<Validation> getValidations() {
		return validations;
	}

	public SurveyStructure getSurveyStructure() {
		return surveyStructure;
	}

	public void setSurveyStructure(SurveyStructure surveyStructure) {
		this.surveyStructure = surveyStructure;
	}
	
}