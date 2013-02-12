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
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import org.correlibre.qop.domain.SurveyStructure;
import org.correlibre.qop.services.QopException;
import org.correlibre.qop.services.SrvSurveysAdminLocal;
import org.correlibre.qop.services.SrvSurveysEngineLocal;
import org.correlibre.qop.services.SrvUsersLocal;
import org.correlibre.qop.util.BeanUtil;


@ManagedBean(name="assignSurveyInstances")
@ViewScoped
public class AssignSurveyInstances {
	
	private List<SelectItem> instanceUserItems;
	private List<String> usersSurveyInstances;
	private Integer instances;
	private SurveyStructure surveyStructure; 

	@EJB
	private SrvUsersLocal srvUsers;	
	@EJB
	private SrvSurveysAdminLocal srvSurveysAdmin;
	@EJB
	private SrvSurveysEngineLocal srvSurveysEngine;
	
	
	@PostConstruct
	public void init() throws QopException{
		HttpServletRequest request=(HttpServletRequest)FacesContext
		.getCurrentInstance().getExternalContext().getRequest();
		String id = request.getParameter("surveyStructureId");
		surveyStructure = srvSurveysEngine.getSurveyStructure(new Integer(id));
		List<String> instanceUsers = srvUsers.getUsers();
		instanceUserItems = new ArrayList<SelectItem>();
		for(String u: instanceUsers){
			instanceUserItems.add(new SelectItem(u));
		}
	}
	
	
	public void assignInstances(ActionEvent e) throws QopException{	
		if(instances == null || instances <=0 ){
			BeanUtil.getInstance().addMessage(FacesMessage.SEVERITY_ERROR,"El número de instancias a asignar no es válido");
		}
		else if(usersSurveyInstances.size() == 0 ){
			BeanUtil.getInstance().addMessage(FacesMessage.SEVERITY_ERROR,"Seleccione los usuarios para asignar las encuentas");
		}
		else{
			for(int i= 0; i< instances; i++){
				for(String userLogin: usersSurveyInstances){
					srvSurveysAdmin.assignSurvey(surveyStructure, userLogin);
				}
			}	
		}
	}
	
	public SurveyStructure getSurveyStructure() {
		return surveyStructure;
	}


	public void setSurveyStructure(SurveyStructure surveyStructure) {
		this.surveyStructure = surveyStructure;
	}


	public Integer getInstances() {
		return instances;
	}

	public void setInstances(Integer instances) {
		this.instances = instances;
	}
	
	public List<SelectItem> getInstanceUserItems() {
		return instanceUserItems;
	}
	
	public List<String> getUsersSurveyInstances() {
		return usersSurveyInstances;
	}

	public void setUsersSurveyInstances(List<String> usersSurveyInstances) {
		this.usersSurveyInstances = usersSurveyInstances;
	}
}