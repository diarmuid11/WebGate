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

import java.security.Principal;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.correlibre.qop.services.SrvUsersLocal;


@ManagedBean(name="sessionBean")
@SessionScoped
public class SessionBean {
	
	private String currentUser;
	private String completeNameUser;
	private boolean admin;	
	private boolean surveyed;
	private boolean surveyor;
	
	@EJB 
	private SrvUsersLocal srvUsers;
		
	@PostConstruct
	public void init(){
		HttpServletRequest httpRequest = (HttpServletRequest)FacesContext
		.getCurrentInstance().getExternalContext().getRequest();
		Principal principal = httpRequest.getUserPrincipal();
		if(principal != null){
			currentUser = principal.getName();						
			admin = httpRequest.isUserInRole("ADMINISTRADOR");
			setSurveyed(httpRequest.isUserInRole("ENCUESTADO"));
			setSurveyor(httpRequest.isUserInRole("ENCUESTADOR"));
			setCompleteNameUser(srvUsers.getUserObj(currentUser).getCompleteName());
		}
		
	}
	
	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	public boolean isAdmin() {
		return admin;
	}
	
	public String closeSession(){
		HttpSession s = (HttpSession)FacesContext.getCurrentInstance().getExternalContext()
			.getSession(false);
		System.out.println("s: "+s);
		
		if(s != null)
			s.invalidate();
		
		return "main?faces-redirect=true";
	}
	
	public String getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(String currentUser) {
		this.currentUser = currentUser;
	}

	public void setCompleteNameUser(String completeNameUser) {
		this.completeNameUser = completeNameUser;
	}

	public String getCompleteNameUser() {
		return completeNameUser;
	}

	public void setSurveyed(boolean surveyed) {
		this.surveyed = surveyed;
	}

	public boolean isSurveyed() {
		return surveyed;
	}

	public void setSurveyor(boolean surveyor) {
		this.surveyor = surveyor;
	}

	public boolean isSurveyor() {
		return surveyor;
	}	
}