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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import org.correlibre.qop.domain.Answer;
import org.correlibre.qop.domain.Question;
import org.correlibre.qop.domain.Survey;
import org.correlibre.qop.domain.SurveyState;
import org.correlibre.qop.domain.SurveyStructure;
import org.correlibre.qop.services.QopException;
import org.correlibre.qop.services.SrvSurveysEngineLocal;
import org.correlibre.qop.util.BeanUtil;
import org.correlibre.qop.util.SelectItemStringLabelComparator;
import org.correlibre.qop.util.SortableSurveyList;
import org.icefaces.bean.ViewRetained;

@ManagedBean
@ViewScoped
@ViewRetained
public class ShowSurveys extends SortableSurveyList{
	
	
	@EJB
	private SrvSurveysEngineLocal srvSurveysEngine;	
	
	private String surveyState;
	
	private String searchSurveyStructureId;
	
	List<SelectItem> surveyStateItems;
	
	List<SelectItem> surveyStructureItems;
	
	private boolean hasIdentifyQuestion;
	
	private String identityQuestionText;
	
	private String identityQuestionSearchText;
	
	private boolean isParentSurveyStructure;
	
	private String surveyStructureName;
	
	private List<SurveyStructure> childrenSurveyStructures;
	
	
	protected ShowSurveys(String defaultSortColumn) {
		super(defaultSortColumn);
	}
	
	public ShowSurveys() {
		super(SortableSurveyList.RESPUESTA_COLUMN_NAME);
	}
	
	@PostConstruct
	public void init(){		
		
		try {
			List<SurveyState> surveyStates = new ArrayList<SurveyState>();
			
			for (SurveyState surveyState : SurveyState.values()) {
				surveyStates.add(surveyState);
			}
			
			surveyStateItems = BeanUtil.getInstance().getSurveyStatesAsSelectItems(surveyStates, false);
			
			List<SurveyStructure> surveyStructures = srvSurveysEngine.getParentSurveyStructures();
			
			surveyStructureItems = BeanUtil.getInstance().getSurveyStructuresAsSelectItems(surveyStructures);
			
			//Collections.sort(surveyStateItems, new SelectItemStringLabelComparator());
			
			
			if(surveyStructureItems.size() > 0){
				Collections.sort(surveyStructureItems, new SelectItemStringLabelComparator());
				searchSurveyStructureId = (String) surveyStructureItems.get(0).getValue();
			}
			
			
			HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
			String surveyStructureId = request.getParameter("surveyStructureId");	
			String surveyId = request.getParameter("surveyId");
			
			System.out.println("#################### [ShowSurveys] surveyStructureId: "+surveyStructureId);
			System.out.println("#################### [ShowSurveys] surveyState: "+surveyState);
			
			
			if(surveyStructureId != null && surveyStructureId.length() > 0){
				
				searchSurveyStructureId = surveyStructureId;
				
				//this.searchSurveys();
				
			}
			
			if(surveyId != null && surveyId.length() > 0){
				
				Survey survey = srvSurveysEngine.getSurvey(new Integer(surveyId));
				
				this.surveys = new ArrayList<Survey>();
				
				this.surveys.add(survey);
				
				if(childrenSurveyStructures == null || childrenSurveyStructures.isEmpty())
					childrenSurveyStructures =  survey.getSurveyStructure().getChildrenSurveyStructures();
				
			}
			
			
			
			verifyIdetifyQuestion(searchSurveyStructureId);
			
			/*
			if(!srvSurveysEngine.isUserAdmin()){
				this.searchSurveys();
			}
			*/
		} catch (Exception e) {
			e.printStackTrace();
			BeanUtil.getInstance().addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		}
		
	}
	
	public void verifyIdetifyQuestion(String searchSurveyStructureId){
		
		try {
			if(searchSurveyStructureId != null && searchSurveyStructureId.length() > 0){
				SurveyStructure searchSurveyStructure = srvSurveysEngine.getSurveyStructure(new Integer(searchSurveyStructureId));
				
				hasIdentifyQuestion = searchSurveyStructure.getIdentifyQuestion() != null;
				
				if(hasIdentifyQuestion)
					identityQuestionText = searchSurveyStructure.getIdentifyQuestion().getText();
				
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (QopException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public List<SelectItem> getSurveyStateItems(){
		
		return surveyStateItems; 
		
	}
	
	public void searchSurveys(ActionEvent e){
		
		searchSurveys();
		
	}
	
	public void searchSurveys(){
		
		System.out.println("########### [ShowSurveys] surveyState: "+surveyState);
		System.out.println("########### [ShowSurveys] searchSurveyStructureId: "+searchSurveyStructureId);
		
		try {
			SurveyState ss = SurveyState.fromString(surveyState);
				
			if(searchSurveyStructureId != null){
				
				SurveyStructure surveyStructure = srvSurveysEngine.getSurveyStructure(new Integer(searchSurveyStructureId));
				
				if(hasIdentifyQuestion){
					if(identityQuestionSearchText != null && identityQuestionSearchText.length() > 0){
						surveys = srvSurveysEngine.getAllParentSurveys(ss, surveyStructure, surveyStructure.getIdentifyQuestion(), identityQuestionSearchText+"%");
					}else{
						surveys = srvSurveysEngine.getAllParentSurveys(ss, surveyStructure);
					}
				}else{
					surveys = srvSurveysEngine.getAllParentSurveys(ss, surveyStructure);
				}
				
				this.sort();	
				
				/*
				for (Survey s : surveys) {
					s.setAnswers(srvSurveysEngine.getAllSurveyAnswers(s));
				}
				*/
				
				isParentSurveyStructure = (surveyStructure.getChildrenSurveyStructures() != null && surveyStructure.getChildrenSurveyStructures().size() > 0);
				
				surveyStructureName = surveyStructure.getName();
				
				childrenSurveyStructures = surveyStructure.getChildrenSurveyStructures();
				
				System.out.println("###### childrenSurveyStructures: "+childrenSurveyStructures);
				
			}else{
				
				BeanUtil.getInstance().addMessage("Debe seleccionar un formato de encuesta");
				
			}
		} catch (NumberFormatException e1) {
			//TODO: Cambiar mensaje
			
			BeanUtil.getInstance().addMessage(FacesMessage.SEVERITY_ERROR,e1.getMessage());
			e1.printStackTrace();
		} catch (QopException e1) {
			//TODO: Cambiar mensaje
			
			BeanUtil.getInstance().addMessage(FacesMessage.SEVERITY_ERROR,e1.getMessage());
			e1.printStackTrace();
		} catch (Exception e1) {
			
			BeanUtil.getInstance().addMessage(FacesMessage.SEVERITY_ERROR,e1.getMessage());
			e1.printStackTrace();
		}
		
		if(surveys.size() > 0)
			BeanUtil.getInstance().addMessage(FacesMessage.SEVERITY_INFO,"Se han encontrado "+surveys.size()+" resultados.");
		else
			BeanUtil.getInstance().addMessage(FacesMessage.SEVERITY_INFO,"No se obtuvo ningun resultado con los parametros de búsqueda.");
		
	}
	
	public String initSelectedChildSurvey(Survey parentSurvey, SurveyStructure childSurveyStructure){
		
		System.out.println("########### [ShowSurveys] initSelectedChildSurvey");
		System.out.println("########### [ShowSurveys] surveyStructureId: "+childSurveyStructure.getId());
		System.out.println("########### [ShowSurveys] parentSurvey.getId(): "+parentSurvey.getId());
		
		
		FacesContext.getCurrentInstance().getExternalContext().getFlash().put("surveyStructureId", ""+childSurveyStructure.getId());
		FacesContext.getCurrentInstance().getExternalContext().getFlash().put("parentSurveyId", ""+ parentSurvey.getId());
		
		return "fill_survey";
		
	}
	
	public String createAndEditSelectedChildSurvey(Survey parentSurvey, SurveyStructure childSurveyStructure){
		
		try{
			
			System.out.println("########### [ShowSurveys] createAndEditSelectedChildSurvey");
			System.out.println("########### [ShowSurveys] surveyStructureId: "+childSurveyStructure.getId());
			System.out.println("########### [ShowSurveys] parentSurvey.getId(): "+parentSurvey != null ? parentSurvey.getId() : null);
			
			Survey newSurvey = srvSurveysEngine.createSurvey(childSurveyStructure.getId(), parentSurvey.getUserLogin(), parentSurvey);
			
			parentSurvey.getSubSurveys().add(newSurvey);
			srvSurveysEngine.updateSurvey(parentSurvey);
			
			System.out.println("########### [ShowSurveys] createAndEditSelectedChildSurvey");
			
			System.out.println("########### [ShowSurveys] newSurvey.getId(): "+newSurvey.getId());
			
			FacesContext.getCurrentInstance().getExternalContext().getFlash().put("surveyId", ""+newSurvey.getId());
			//FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("surveyId", ""+newSurvey.getId());
			
			//System.out.println("########### [ShowSurveys] preNav edit_survey");
			
			return "edit_survey";
			
		}catch(Exception e){
			
			BeanUtil.getInstance().addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			
			return null;
			
		}
		
	}
	
	public String createAndEditSurvey(String surveyStructureId, String userLogin){
		
		try{
			
			System.out.println("########### [ShowSurveys] createAndEditSurvey");
			System.out.println("########### [ShowSurveys] surveyStructureId: "+surveyStructureId);
			
			Survey newSurvey = srvSurveysEngine.createSurvey(new Integer(surveyStructureId), userLogin, null);
			
			
			System.out.println("########### [ShowSurveys] newSurvey.getId(): "+newSurvey.getId());
			
			FacesContext.getCurrentInstance().getExternalContext().getFlash().put("surveyId", ""+newSurvey.getId());
			//FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("surveyId", ""+newSurvey.getId());
			
			//System.out.println("########### [ShowSurveys] preNav edit_survey");
			
			return "edit_survey";
			
		}catch(Exception e){
			
			BeanUtil.getInstance().addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			
			return null;
			
		}
		
	}
	
	public String editSurvey(Survey survey){
		
		try{
			
			System.out.println("########### [ShowSurveys] editSurvey");
			System.out.println("########### [ShowSurveys] survey.getId(): "+survey.getId());
			
			//Survey newSurvey = srvSurveysEngine.createSurvey(new Integer(surveyStructureId), userLogin, null);
			
			
			//System.out.println("########### [ShowChildrenSurveys] newSurvey.getId(): "+newSurvey.getId());
			
			FacesContext.getCurrentInstance().getExternalContext().getFlash().put("surveyId", ""+survey.getId());
			//FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("surveyId", ""+newSurvey.getId());
			
			//System.out.println("########### [ShowSurveys] preNav edit_survey");
			
			return "edit_survey";
			
		}catch(Exception e){
			
			BeanUtil.getInstance().addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			
			return null;
			
		}
		
	}
	
	public String createSelectedChildSurvey(Survey parentSurvey, SurveyStructure childSurveyStructure){
		
		try{
			
			System.out.println("########### [ShowSurveys] createSelectedChildSurvey");
			System.out.println("########### [ShowSurveys] surveyStructureId: "+childSurveyStructure.getId());
			System.out.println("########### [ShowSurveys] parentSurvey.getId(): "+parentSurvey.getId());
			
			Survey newSurvey = srvSurveysEngine.createSurvey(childSurveyStructure.getId(), parentSurvey.getUserLogin(), parentSurvey);
			
			parentSurvey.getSubSurveys().add(newSurvey);
			srvSurveysEngine.updateSurvey(parentSurvey);
			
			System.out.println("########### [ShowSurveys] createAndEditSelectedChildSurvey");
			
			System.out.println("########### [ShowSurveys] newSurvey.getId(): "+newSurvey.getId());
			
			//FacesContext.getCurrentInstance().getExternalContext().getFlash().put("surveyId", ""+newSurvey.getId());
			//FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("surveyId", ""+newSurvey.getId());
			
			//System.out.println("########### [ShowSurveys] preNav edit_survey");
			
			//return "edit_survey";
			
		}catch(Exception e){
			
			BeanUtil.getInstance().addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			
			
			
		}
		return null;
		
	}
	
	public void open(Survey survey){
		try {
			survey.setSurveyState(SurveyState.FILLING);
			
			List<Question> questions = srvSurveysEngine.getSurveyStructureQuestions(survey.getSurveyStructure());
			
			survey.setCurrentQuestion(questions.get(0));
			
			
			this.srvSurveysEngine.updateSurvey(survey);
		} catch (QopException e) {
			e.printStackTrace();
			BeanUtil.getInstance().addMessage(e.getMessage());
		}
	}
	
	public SurveyStructure getChildSurveyStructure(Survey s){
		return s.getSurveyStructure().getChildrenSurveyStructures().get(0);
		
	}
	
	public int countChildSurvey(Survey s, SurveyStructure childSurveyStructure){
		
		int c = 0;
		
		try{
			s = srvSurveysEngine.getSurvey(s.getId());
			
			for (Survey surv : s.getSubSurveys()) {
				
					if(!surv.getSurveyState().equals(SurveyState.DELETED) && surv.getSurveyStructure().getId().intValue() == childSurveyStructure.getId().intValue()){
						c++;
					}
				
			}
		}catch(Exception e){
			e.printStackTrace();
			//BeanUtil.getInstance().addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		}
		
		return c;
	}
	
	public boolean isEmptyCountChildren(Survey s, SurveyStructure childSurveyStructure){
		
		return (countChildSurvey(s, childSurveyStructure) <= 0);
		
	}
	
	public Object getAnswerIdentifyQuestion(Survey survey){
		
		Answer ans = srvSurveysEngine.getSingleAnswer(survey, survey.getSurveyStructure().getIdentifyQuestion());
		
		if(ans != null){
			
			if(survey.getSurveyStructure().getIdentifyQuestion().getQuestionType().getId() == 14)
				return new Long(ans.getValue().trim());
			else
				return ans.getValue();
		}else
			return "";
	}
	
	public void changeSearchSurveyStructure(ValueChangeEvent v){
		
		String newSearchSurveyStrutureId = (String) v.getNewValue();
		
		verifyIdetifyQuestion(newSearchSurveyStrutureId);
		
		this.surveys = new ArrayList<Survey>();
		
	}
	
	public String delete(Survey survey){
		
		
		if(survey != null){
			
			try {
				survey.setSurveyState(SurveyState.DELETED);
				this.srvSurveysEngine.updateSurvey(survey);
				BeanUtil.getInstance().addMessage(FacesMessage.SEVERITY_INFO, "Encuesta eliminada");
			} catch (QopException ex) {
				ex.printStackTrace();
				BeanUtil.getInstance().addMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage());
			}
		}else{
			BeanUtil.getInstance().addMessage(FacesMessage.SEVERITY_ERROR, "No se encontró ninguna encuesta");
		}
		
		return null;
	}
	
	public boolean isUserAdmin(){
		return srvSurveysEngine.isUserAdmin();
	}
	
	public boolean isClosedState(SurveyState s){
		return s.equals(SurveyState.CLOSED);
	}
	
	public boolean isDeletedState(SurveyState s){
		return s.equals(SurveyState.DELETED);
	}
	
	public boolean isEndedState(SurveyState s){
		return s.equals(SurveyState.ENDED);
	}
	
	public boolean isFillingState(SurveyState s){
		return s.equals(SurveyState.FILLING);
	}
	
	public boolean isNewState(SurveyState s){
		return s.equals(SurveyState.NEW);
	}

	public String getSurveyState() {
		return surveyState;
	}

	public void setSurveyState(String surveyState) {
		this.surveyState = surveyState;
	}

	public String getSearchSurveyStructureId() {
		return searchSurveyStructureId;
	}

	public void setSearchSurveyStructureId(String searchSurveyStructureId) {
		this.searchSurveyStructureId = searchSurveyStructureId;
	}

	public List<SelectItem> getSurveyStructureItems() {
		return surveyStructureItems;
	}

	public boolean isHasIdentifyQuestion() {
		return hasIdentifyQuestion;
	}

	public void setHasIdentifyQuestion(boolean hasIdentifyQuestion) {
		this.hasIdentifyQuestion = hasIdentifyQuestion;
	}

	public String getIdentityQuestionText() {
		return identityQuestionText;
	}

	public void setIdentityQuestionText(String identityQuestionText) {
		this.identityQuestionText = identityQuestionText;
	}

	public void setParentSurveyStructure(boolean isParentSurveyStructure) {
		this.isParentSurveyStructure = isParentSurveyStructure;
	}

	public boolean isParentSurveyStructure() {
		return isParentSurveyStructure;
	}


	public void setSurveyStructureName(String surveyStructureName) {
		this.surveyStructureName = surveyStructureName;
	}

	public String getSurveyStructureName() {
		return surveyStructureName;
	}

	public void setChildrenSurveyStructures(List<SurveyStructure> childrenSurveyStructures) {
		this.childrenSurveyStructures = childrenSurveyStructures;
	}

	public List<SurveyStructure> getChildrenSurveyStructures() {
		return childrenSurveyStructures;
	}

	public void setIdentityQuestionSearchText(String identityQuestionSearchText) {
		this.identityQuestionSearchText = identityQuestionSearchText;
	}

	public String getIdentityQuestionSearchText() {
		return identityQuestionSearchText;
	}


}
