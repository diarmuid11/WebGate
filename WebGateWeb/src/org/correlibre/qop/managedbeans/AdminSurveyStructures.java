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

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.correlibre.qop.domain.SurveyState;
import org.correlibre.qop.domain.SurveyStructure;
import org.correlibre.qop.domain.SurveyStructureState;
import org.correlibre.qop.services.QopException;
import org.correlibre.qop.services.SrvSurveysAdminLocal;
import org.correlibre.qop.services.SrvSurveysEngineLocal;

@ManagedBean(name="adminSurveyStructures")
@ViewScoped
public class AdminSurveyStructures {
	@EJB
	private SrvSurveysEngineLocal srvSurveysEngine;			

	@EJB
	private SrvSurveysAdminLocal srvSurveysAdmin;			

	private String surveyStructureName;		
	
	private List<SurveyStructure> surveyStructures;	

	
	@PostConstruct
	public void init(){
		surveyStructures = srvSurveysEngine.getSurveyStructures();
	}
	
	public String getSurveyStructureName() {
		return surveyStructureName;
	}

	public void setSurveyStructureName(String surveyStructureName) {
		this.surveyStructureName = surveyStructureName;
	}

	public List<SurveyStructure> getSurveyStructures() {
		return surveyStructures;
	}

		
	public String createSurveyStructure(){
		srvSurveysAdmin.createSurveyStructure(surveyStructureName);
		surveyStructures = srvSurveysEngine.getSurveyStructures();
		return null;
	}
	
	public String createSurveyStructureAndQuestions(){
		System.out.println("########### [AdminSurveyStructures] surveyStructureName: "+surveyStructureName);
		SurveyStructure ss = srvSurveysAdmin.createNewSurveyStructure(surveyStructureName);
		System.out.println("########### [AdminSurveyStructures] ss.getId(): "+ (ss != null ? ss.getId() : "null" ) );
		FacesContext.getCurrentInstance().getExternalContext().getFlash().put("surveyStructure", ss);
		return "edit_question";
	}
	
	
	public void editSurveyStructure(ActionEvent e) throws QopException{
		SurveyStructure ss = (SurveyStructure) e.getComponent().getAttributes().get("surveyStructure");
		srvSurveysAdmin.editSurveyStructure(ss);
		return;
	}
	
	public boolean isForEditing(SurveyStructure ss){
		return ss.getState()!=SurveyStructureState.ENDED &&
			ss.getState()!=SurveyStructureState.DELETED;
	}

	public void deploySurveyStructure(ActionEvent e) throws QopException{
		SurveyStructure ss= (SurveyStructure)e.getComponent().getAttributes().get("surveyStructure");
		srvSurveysAdmin.deploySurveyStructure(ss);
		return;
	}
	
	public boolean isForDeploying(SurveyStructure ss){
		return ss.getState()==SurveyStructureState.CREATING ||
			ss.getState()==SurveyStructureState.IN_EDITION;
	}

	public void deleteSurveyStructure(SurveyStructure ss) throws QopException{	
		System.out.println("########### [AdminSurveyStructures] ss: "+ss);
		srvSurveysAdmin.deleteSurveyStructure(ss);
	}

	public boolean isForDeleting(SurveyStructure ss){
		return ss.getState()==SurveyStructureState.CREATING;
	}
	
	public boolean isInEdition(SurveyStructure ss){
		return ss.getState() == SurveyStructureState.IN_EDITION;
	}
	
	public boolean isCreating(SurveyStructure ss){
		return ss.getState() == SurveyStructureState.CREATING;
	}

	public void endSurveyStructure(SurveyStructure ss) throws QopException{		
		srvSurveysAdmin.endSurveyStructure(ss);
	}

	public boolean isForEnding(SurveyStructure ss){
		return ss.getState()==SurveyStructureState.DEPLOYED;
	}

	public boolean isEndedState(SurveyState s){
		return s.equals(SurveyState.ENDED);
	}

}