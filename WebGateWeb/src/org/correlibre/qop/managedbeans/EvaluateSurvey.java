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
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import org.correlibre.qop.domain.Survey;
import org.correlibre.qop.domain.SurveyStructure;
import org.correlibre.qop.services.QopException;
import org.correlibre.qop.services.SrvSurveysEngineLocal;
import org.correlibre.qop.util.BeanUtil;
import org.correlibre.qop.util.SelectItemStringLabelComparator;

@ManagedBean(name="evaluateSurvey")
@ViewScoped
public class EvaluateSurvey {
	
	@EJB
	private SrvSurveysEngineLocal srvSurveysEngine;	
	//private List<Survey> surveys;
	
	//private Survey parentSurvey;
	
	private Survey currentSurvey;
	
	
	private String selectedSurveyStructureId;
	
	private List<SurveyStructure> listDeployedSurveyStructures;
	
	@PostConstruct
	public void init(){
		
		try {
			System.out.println("##################  [EvaluateSurvey] init");
			
			Flash f = FacesContext.getCurrentInstance().getExternalContext().getFlash();
			
			Survey tmpSurvey = (Survey) f.get("survey");
			System.out.println("##################   [EvaluateSurvey] tmpSurvey for currentSurvey: "+tmpSurvey );
			
			if(tmpSurvey != null){
			
				tmpSurvey = srvSurveysEngine.getSurvey(tmpSurvey.getId());
				setCurrentSurvey(tmpSurvey);
				
				listDeployedSurveyStructures = tmpSurvey.getSurveyStructure().getChildrenSurveyStructures();
				
				if(listDeployedSurveyStructures.size() > 0){
					selectedSurveyStructureId = listDeployedSurveyStructures.get(0).getId().toString();
				}
			
			}
			
			System.out.println("##################   [EvaluateSurvey] selectedSurveyStructureId: "+selectedSurveyStructureId );
			
			System.out.println("##################  [EvaluateSurvey] init OK");
			
		} catch (Exception e) {
			BeanUtil.getInstance().addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			e.printStackTrace();
		}
		
	}	
	
	
	public List<SelectItem> getDeployedSurveyStructuresItems(){
		
		//TODO Usar el metodo en BeanUtil 
		
		List<SelectItem> listItems = new ArrayList<SelectItem>();
		
		for (SurveyStructure ss : listDeployedSurveyStructures) {
			listItems.add(new SelectItem(ss.getId(), ss.getName()));
		}
		
		Collections.sort(listItems, new SelectItemStringLabelComparator());
		
		return listItems;
		
	}
	
	public String initChildSurveyAgain(){
		
		try{
			System.out.println("########### [EvaluateSurvey] initChildSurveyAgain");
			
			System.out.println("########### [EvaluateSurvey] selectedSurveyStructureId: "+selectedSurveyStructureId);
			System.out.println("########### [EvaluateSurvey] currentSurvey : "+currentSurvey);
			
			FacesContext.getCurrentInstance().getExternalContext().getFlash().put("surveyStructureId", ""+currentSurvey.getSurveyStructure().getId());
			FacesContext.getCurrentInstance().getExternalContext().getFlash().put("parentSurveyId", ""+ currentSurvey.getParentSurvey().getId());
			
			return "fill_survey";
		
		} catch (Exception ex) {
			BeanUtil.getInstance().addMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage());
			ex.printStackTrace();
			return null;
		}
		
	}
	
	//revisAR SI ES NECESARI0
	public void initChildSurveyAgain2(ActionEvent e){
		
		try{
		
			System.out.println("########### [EvaluateSurvey] e.getSource():" + e.getSource());
			
			System.out.println("########### [EvaluateSurvey] selectedSurveyStructureId:" + selectedSurveyStructureId);
			System.out.println("########### [EvaluateSurvey] currentSurvey.getId():" + currentSurvey.getId());
		
		} catch (Exception ex) {
			BeanUtil.getInstance().addMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage());
			ex.printStackTrace();
		}
		
		return;
	}
	
	public String initSelectedChildSurvey(){
		
		try{
			System.out.println("########### [EvaluateSurvey] initSelectedChildSurvey");
			System.out.println("########### [EvaluateSurvey] currentSurvey: "+currentSurvey);
			System.out.println("########### [EvaluateSurvey] currentSurvey.getSurveyStructure(): "+currentSurvey.getSurveyStructure());
			
			FacesContext.getCurrentInstance().getExternalContext().getFlash().put("surveyStructureId", ""+selectedSurveyStructureId);
			FacesContext.getCurrentInstance().getExternalContext().getFlash().put("parentSurveyId", ""+ currentSurvey.getId());
			
			return "fill_survey";
		
		} catch (Exception e) {
			BeanUtil.getInstance().addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			e.printStackTrace();
			return null;
		}
		
	}
	
	public Survey getCompleteParentSurvey(){
		
		Survey parentSurvey = null;
		
		try {
			
			parentSurvey = this.srvSurveysEngine.getSurvey(currentSurvey.getParentSurvey().getId());
			System.out.println("########################## [EvaluateSurvey] parentSurvey.getSubSurveys(): "+parentSurvey.getSubSurveys());
			List<Survey> subS = parentSurvey.getSubSurveys();
			
			parentSurvey.setAnswers(this.srvSurveysEngine.getAllSurveyAnswers(parentSurvey));
			
			for (Survey survey : subS) {
				survey.setAnswers(this.srvSurveysEngine.getAllSurveyAnswers(survey));
			}
			
			System.out.println("########################## [EvaluateSurvey] parentSurvey: "+parentSurvey);
			
			return parentSurvey;
			
		} catch (Exception e) {
			BeanUtil.getInstance().addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			e.printStackTrace();
			return null;
		}
		
		
	}
	
	public boolean hasParent(Survey s){
		
		if(s == null)
			return false;
		else{
			return (s.getSurveyStructure().getParentSurveyStructure() != null);
		}
		
	}


	public void setCurrentSurvey(Survey currentSurvey) {
		this.currentSurvey = currentSurvey;
	}


	public Survey getCurrentSurvey() {
		return currentSurvey;
	}


	public List<SurveyStructure> getListDeployedSurveyStructures() {
		return listDeployedSurveyStructures;
	}


	public void setListDeployedSurveyStructures(
			List<SurveyStructure> listDeployedSurveyStructures) {
		this.listDeployedSurveyStructures = listDeployedSurveyStructures;
	}


	public void setSelectedSurveyStructureId(String selectedSurveyStructureId) {
		this.selectedSurveyStructureId = selectedSurveyStructureId;
	}


	public String getSelectedSurveyStructureId() {
		return selectedSurveyStructureId;
	}
	
}
