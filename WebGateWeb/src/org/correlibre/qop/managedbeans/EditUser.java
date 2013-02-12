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

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import org.correlibre.qop.domain.Role;
import org.correlibre.qop.domain.User;
import org.correlibre.qop.services.SrvUsersLocal;
import org.correlibre.qop.util.BeanUtil;

@ManagedBean(name="editUser")
@ViewScoped
public class EditUser {
	private User user;		
	private String userPassword;
	private String userPasswordConfirm;
	private List<SelectItem> roleItems;
	private List<String> selectedRoles;	
	@EJB 
	private SrvUsersLocal srvUser;
	
	@PostConstruct
	public void init(){
		try{
			HttpServletRequest request = (HttpServletRequest)FacesContext
			.getCurrentInstance().getExternalContext().getRequest();
			String userName = request.getParameter("userName");
			user = srvUser.getUserObj(userName);
			roleItems = new ArrayList<SelectItem>();
			roleItems.add(new SelectItem(Role.ADMINISTRADOR, Role.ADMINISTRADOR.getRole()));
			roleItems.add(new SelectItem(Role.ENCUESTADOR, Role.ENCUESTADOR.getRole()));
			roleItems.add(new SelectItem(Role.ENCUESTADO, Role.ENCUESTADO.getRole()));
			
			selectedRoles = new ArrayList<String>();
			
			for (Role role : user.getRoles()) {
				selectedRoles.add(role.getRole());
			}
		}catch(Exception e){
			BeanUtil.getInstance().addMessage(FacesMessage.SEVERITY_INFO, e.getMessage());
		}
		
	}
	
	public String  editUser() {
		if(user.getRoles().size() == 0){
			BeanUtil.getInstance().addMessage(FacesMessage.SEVERITY_ERROR,"Seleccione los roles del usuario");
		}
		else{ 
			boolean emptyPassword = false;
			if(userPassword.length() == 0 && userPassword.length() == 0){
				//BeanUtil.getInstance().addMessage(FacesMessage.SEVERITY_ERROR,"La contraseña no puede ser vacía");
				//return null;
				emptyPassword = true;
			}
			
			if(checkPassword() || emptyPassword){			
				String enc;
				if(!emptyPassword){
					try {
						enc = srvUser.encrypt(userPassword);					
						user.setUserPassword(enc);
					} catch (NoSuchAlgorithmException e) {
						BeanUtil.getInstance().addMessage(FacesMessage.SEVERITY_ERROR,"Algoritmo no encontrado");
						e.printStackTrace();
					} catch (UnsupportedEncodingException e) {
						BeanUtil.getInstance().addMessage(FacesMessage.SEVERITY_ERROR,"No fue posible encriptar la contraseña");
						e.printStackTrace();
					}	
				}
				//TODO: ver por que no obtiene objetos de los check
				List<Role> roles = new ArrayList<Role>();
				for(String s: selectedRoles){
					roles.add(Role.valueOf(s));
				}
				if(roles.size() == 0){
					BeanUtil.getInstance().addMessage(FacesMessage.SEVERITY_ERROR,"Ingrese los roles de usuario");
				}
				else{
					user.setRoles(roles);
				}
				srvUser.modifyUser(user);		
				BeanUtil.getInstance().addMessage(FacesMessage.SEVERITY_INFO,"Se ha modificado correctamente el usuario");
			}else{
				BeanUtil.getInstance().addMessage(FacesMessage.SEVERITY_ERROR,"La contraseña y la confirmación no coinciden");
			}
		}		
		return null;
	}
	
	private boolean checkPassword(){
		return userPassword.equals(userPasswordConfirm);	
	}
	
	
	public String getUserPassword() {
		return userPassword;
	}
	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}
	public String getUserPasswordConfirm() {
		return userPasswordConfirm;
	}
	public void setUserPasswordConfirm(String userPasswordConfirm) {
		this.userPasswordConfirm = userPasswordConfirm;
	}
	public List<SelectItem> getRoleItems() {
		return roleItems;
	}
	public void setRoleItems(List<SelectItem> roleItems) {
		this.roleItems = roleItems;
	}
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	public List<String> getSelectedRoles() {
		return selectedRoles;
	}

	public void setSelectedRoles(List<String> selectedRoles) {
		this.selectedRoles = selectedRoles;
	}
}
