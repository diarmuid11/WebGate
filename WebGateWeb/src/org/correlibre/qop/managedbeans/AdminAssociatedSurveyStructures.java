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
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.correlibre.qop.domain.SurveyStructure;
import org.correlibre.qop.services.SrvSurveysEngineLocal;

import com.icesoft.faces.component.ext.RowSelectorEvent;

@ManagedBean(name="adminAssociatedSurveyStructures")
@ViewScoped
public class AdminAssociatedSurveyStructures {
	@EJB
	private SrvSurveysEngineLocal srvSurveysEngine;			

	//@EJB
	//private SrvSurveysAdminLocal srvSurveysAdmin;					
	
	//private List<SurveyStructure> surveyStructures;
	
	private List<SurveyStructure> associatedSurveyStructures;
	
	//private List<SurveyStructure> subSurveyStructures;
	
	private SurveyStructure parentSurveyStructure;
	
	private SurveyStructure childSurveyStructure;
	
	private SurveyStructure selectedSurveyStructure;
	
	private boolean activeSurveyStructure;

	
	@PostConstruct
	public void init(){
		System.out.println("################# [AdminAssociatedSurveyStructures] init");
		//surveyStructures = srvSurveysEngine.getSurveyStructures();
		//surveyStructures = srvSurveysEngine.getSurveyStructuresBy2State(SurveyStructureState.CREATING, SurveyStructureState.IN_EDITION);
		loadAssociatedSurveyStructures();
	}
	
	public void loadAssociatedSurveyStructures(){
		
		List<SurveyStructure> tmpListSS = srvSurveysEngine.getSurveyStructures();
		
		associatedSurveyStructures = new ArrayList<SurveyStructure>();
		
		Iterator<SurveyStructure> it = tmpListSS.iterator();
		
		while(it.hasNext()){
			SurveyStructure tmpSS = it.next();
			if(tmpSS.getChildrenSurveyStructures().size()>0){
				associatedSurveyStructures.add(tmpSS);
			}
		}
	}
	
	/*
	public List<SurveyStructure> getSurveyStructures() {
		return surveyStructures;
	}
	
	public List<SelectItem> getSurveyStructuresItems(){
		List<SelectItem> listSs = new ArrayList<SelectItem>();
		
		for (SurveyStructure ss : getSurveyStructures()) {
			listSs.add(new SelectItem(ss, ss.getName()));
			
		}
		
		Collections.sort(listSs, new SelectItemStringLabelComparator());
		
		return listSs;
	}
	*/
	
	public int sizeChildSurveyStructures(SurveyStructure surveyStructure){
		
		//System.out.println("******************* surveyStructure.getName(): "+ (surveyStructure != null ? surveyStructure.getName() : null));
		
		if(surveyStructure != null){
			
			return surveyStructure.getChildrenSurveyStructures().size();
		}
		return 0;
	}
	/*
	public String associateSurveyStructures(){
		
		System.out.println("************* Asociando...");
		try {
			System.out.println("************* Asociando...");
			srvSurveysAdmin.associateSurveyStructure(getParentSurveyStructure(), getChildSurveyStructure());
			
		} catch (QopException e) {

			e.printStackTrace();
		}
		return null;
		
	}
	*/
	public void selectionSurveyStructureListener(RowSelectorEvent event) {

		selectedSurveyStructure = null;
		activeSurveyStructure = false;
		
		SurveyStructure tmpSurveyS = associatedSurveyStructures.get(event.getRow());
		
		System.out.println("############### [AdminAssociatedSurveyStructures] tmpSurveyS.getName(): "+tmpSurveyS.getName());
		
		selectedSurveyStructure = tmpSurveyS;
		
		FacesContext.getCurrentInstance().renderResponse();

    }

	public SurveyStructure getChildSurveyStructure() {
		return childSurveyStructure;
	}

	public void setChildSurveyStructure(SurveyStructure childSurveyStructure) {
		this.childSurveyStructure = childSurveyStructure;
	}

	public void setParentSurveyStructure(SurveyStructure parentSurveyStructure) {
		this.parentSurveyStructure = parentSurveyStructure;
	}

	public SurveyStructure getParentSurveyStructure() {
		return parentSurveyStructure;
	}

	public void setSelectedSurveyStructure(SurveyStructure selectedSurveyStructure) {
		this.selectedSurveyStructure = selectedSurveyStructure;
	}

	public SurveyStructure getSelectedSurveyStructure() {
		return selectedSurveyStructure;
	}

	public void setActiveSurveyStructure(boolean activeSurveyStructure) {
		this.activeSurveyStructure = activeSurveyStructure;
	}

	public boolean isActiveSurveyStructure() {
		return activeSurveyStructure;
	}

	public List<SurveyStructure> getAssociatedSurveyStructures() {
		return associatedSurveyStructures;
	}

	public void setAssociatedSurveyStructures(
			List<SurveyStructure> associatedSurveyStructures) {
		this.associatedSurveyStructures = associatedSurveyStructures;
	}

}