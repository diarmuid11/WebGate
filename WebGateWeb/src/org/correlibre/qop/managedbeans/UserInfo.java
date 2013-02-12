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

import org.correlibre.qop.domain.SurveyState;
import org.correlibre.qop.domain.SurveyStructure;
import org.correlibre.qop.services.QopException;
import org.correlibre.qop.services.SrvSurveysAdminLocal;
import org.correlibre.qop.services.SrvSurveysEngineLocal;
import org.correlibre.qop.services.SrvUsersLocal;
import org.correlibre.qop.util.BeanUtil;

@ManagedBean(name="userInfo")
@ViewScoped
public class UserInfo {
	public List<Object> surveys;	
	private Long fillingState;
	private Long newState;
	private Long endedState;
	private String userName;	
	private String newOwner;
	private List<SelectItem> userItems;
	
	@EJB
	private SrvSurveysAdminLocal srvSurveysAdmin;	
	@EJB
	private SrvSurveysEngineLocal srvSurveys;
	@EJB
	private SrvUsersLocal srvUser;
	
	@PostConstruct
	public void init() {
		HttpServletRequest request = (HttpServletRequest)FacesContext
		.getCurrentInstance().getExternalContext().getRequest();
		userName = request.getParameter("userName");	
		surveys = srvSurveysAdmin.getUserSurveysDetail(userName);
		fillingState = srvSurveysAdmin.getSurveys(userName, SurveyState.FILLING);
		newState = srvSurveysAdmin.getSurveys(userName, SurveyState.NEW);
		endedState = srvSurveysAdmin.getSurveys(userName, SurveyState.ENDED);		
	}
	
	
	public List<SelectItem> getUserItems(){
		List<String> users = srvUser.getUsers();
		users.remove(userName);
		userItems = new ArrayList<SelectItem>();
		for(String s: users){
			userItems.add(new SelectItem(s));
		}		
		return userItems;
	}  
	
	public void changeOwner(ActionEvent event) throws QopException{
		SurveyStructure ss= (SurveyStructure)event.getComponent().getAttributes()
				.get("surveyStructure");		
		SurveyState st= (SurveyState)event.getComponent().getAttributes()
		.get("surveyState");
		if(!"".trim().equalsIgnoreCase(newOwner) && ss != null && st != null){
			srvSurveys.changeSurveysOwner(ss, st, userName, newOwner);
		}else{
			BeanUtil.getInstance().addMessage(FacesMessage.SEVERITY_ERROR, "Debe seleccionar un usuario, y un registro de la tabla");
		}
	}	
	
	public boolean isSurveyEnded(SurveyState surveyState){
		return surveyState.equals(SurveyState.ENDED);
	}		
	
	public List<Object> getSurveys() {
		return surveys;
	}

	public void setSurveys(List<Object> surveys) {
		this.surveys = surveys;
	}
	
	public Long getFillingState() {
		return fillingState;
	}
	public void setFillingState(Long fillingState) {
		this.fillingState = fillingState;
	}
	public Long getNewState() {
		return newState;
	}
	public void setNewState(Long newState) {
		this.newState = newState;
	}
	public Long getEndedState() {
		return endedState;
	}
	public void setEndedState(Long endedState) {
		this.endedState = endedState;
	}
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getNewOwner() {
		return newOwner;
	}

	public void setNewOwner(String newOwner) {
		this.newOwner = newOwner;
	}

}
