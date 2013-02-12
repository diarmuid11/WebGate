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
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.correlibre.qop.domain.Role;
import org.correlibre.qop.domain.SurveyStructure;
import org.correlibre.qop.domain.SurveyStructureState;
import org.correlibre.qop.services.QopException;
import org.correlibre.qop.services.SrvSurveysAdminLocal;
import org.correlibre.qop.services.SrvSurveysEngineLocal;
import org.correlibre.qop.services.SrvUsersLocal;

@ManagedBean(name="assignSurveys")
@ViewScoped
public class AssignSurveys {
	
	private List<SelectItem> creatorUserItems;
	private List<SurveyStructure> surveyStructures;			

	@EJB
	private SrvUsersLocal srvUsers;	
	@EJB
	private SrvSurveysAdminLocal srvSurveysAdmin;
	@EJB
	private SrvSurveysEngineLocal srvSurveysEngine;
	
	
	@PostConstruct
	public void init(){		
		surveyStructures =  srvSurveysEngine.getSurveyStructuresByState(SurveyStructureState.DEPLOYED);
		if(surveyStructures.size() > 0){
			List<String> creatorUsers = srvUsers.getUsers(Role.valueOf("ENCUESTADOR"));
			List<String> creatorSurveyedUsers = srvUsers.getUsers(Role.valueOf("ENCUESTADO"));
			
			creatorUserItems = new ArrayList<SelectItem>();
			for(String cu: creatorUsers){
				creatorUserItems.add(new SelectItem(cu));
			}
			
			for(String cu: creatorSurveyedUsers){
				creatorUserItems.add(new SelectItem(cu));
			}
		}
	}
		
	// TODO: Optimizar!!! Buscar la forma de que la propiedad Value del combo no haga set en el 
	// objeto sino en otra lista y que a su vez queden seleccionados los objetos de la lista de 
	// usuarios de la SS.
	public void assignCreators(ActionEvent event) throws QopException{
		SurveyStructure ss= (SurveyStructure)event.getComponent().getAttributes()
				.get("surveyStructure");
		List<String> creatorUsers = ss.getCreatorUsers();
		srvSurveysAdmin.modifySurveyStructureCreators(ss, creatorUsers);		
	}	
	
	public List<SelectItem> getCreatorUserItems() {
		return creatorUserItems;
	}
	
	public List<SurveyStructure> getSurveyStructures() {
		return surveyStructures;
	}

	public void setSurveyStructures(List<SurveyStructure> surveyStructures) {
		this.surveyStructures = surveyStructures;
	}	
}
