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

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import org.correlibre.qop.domain.Role;
import org.correlibre.qop.services.QopException;
import org.correlibre.qop.services.SrvUsersLocal;
import org.correlibre.qop.util.BeanUtil;

@ManagedBean(name="adminUsers")
@ViewScoped
public class AdminUsers {

	private List<String> users;
	private String selectedRole;
	private String userName;
	@EJB 
	private SrvUsersLocal srvUsers;
	
	
	public String find(){
		users = new ArrayList<String>();
		if(selectedRole == null && userName.trim() == ""){
			users = srvUsers.getUsers();
		}else if(selectedRole != null && userName.trim() == ""){
			users = srvUsers.getUsers(Role.valueOf(selectedRole));
		}else if(selectedRole == null && userName.trim() != ""){
			String user = srvUsers.getUser(userName.trim());
			if(user != null){
				users.add(user);
			}
		}else{
			users = srvUsers.getUsers(userName.trim(), Role.valueOf(selectedRole));				
		}
		
		if(users.size() == 0){
			BeanUtil.getInstance().addMessage(FacesMessage.SEVERITY_ERROR, "No se encontraron usuarios");
		}		
		return null;
	}
	
	
	public String delete(ActionEvent event) throws QopException{
		String user = (String)event.getComponent().getAttributes()
		.get("userName");	
		try{
			srvUsers.delete(user);
			find();
			BeanUtil.getInstance().addMessage(FacesMessage.SEVERITY_INFO,"Se ha eliminado el usuario satisfactoriamente");
		} catch (QopException e) {
			BeanUtil.getInstance().addMessage(FacesMessage.SEVERITY_ERROR,"No es posible eliminar un usuario con encuestas activas");
			e.printStackTrace();
		}
		return null;
	}
	
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public List<String> getUsers() {
		return users;
	}
	public void setUsers(List<String> users) {
		this.users = users;
	}
	public String getSelectedRole() {
		return selectedRole;
	}
	public void setSelectedRole(String selectedRole) {
		this.selectedRole = selectedRole;
	}
	
}
