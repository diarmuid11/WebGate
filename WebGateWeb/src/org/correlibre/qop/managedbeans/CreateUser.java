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

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.correlibre.qop.domain.Role;
import org.correlibre.qop.domain.User;
import org.correlibre.qop.services.SrvUsersLocal;
import org.correlibre.qop.util.BeanUtil;

@ManagedBean(name = "createUser")
@ViewScoped
public class CreateUser {
	private String userName;
	private String completeName;
	private String userPassword;
	private String userPasswordConfirm;
	private List<String> selectedRoles;

	@EJB
	private SrvUsersLocal srvUser;

	public String createUser() {
		if (selectedRoles.size() == 0) {
			BeanUtil.getInstance().addMessage(
					"Seleccione los roles del nuevo usuario");
		} else {
			
			if(userPassword.length() == 0 || userPassword.length() == 0){
				BeanUtil.getInstance()
				.addMessage(FacesMessage.SEVERITY_ERROR,"La contraseña no puede ser vacía");
				return null;
			}
			
			if (checkPassword()) {
				User user = new User();
				String enc;
				try {
					enc = srvUser.encrypt(userPassword);
					user.setUserPassword(enc);
					user.setUserName(userName);
					user.setCompleteName(completeName);
				} catch (NoSuchAlgorithmException e) {
					BeanUtil.getInstance()
							.addMessage(FacesMessage.SEVERITY_ERROR,"Algoritmo no encontrado");
					e.printStackTrace();
				} catch (UnsupportedEncodingException e) {
					BeanUtil.getInstance().addMessage(FacesMessage.SEVERITY_ERROR,
							"No fue posible encriptar la contrasena");
					e.printStackTrace();
				} catch (Exception e) {
					BeanUtil.getInstance().addMessage(FacesMessage.SEVERITY_ERROR,
					"No fue posible crear el usuario: ");
					e.printStackTrace();
				}

				List<Role> roles = new ArrayList<Role>();
				for (String s : selectedRoles) {
					roles.add(Role.valueOf(s));
				}
				user.setRoles(roles);
				try {
					srvUser.setUsers(user);
					BeanUtil.getInstance().addMessage(FacesMessage.SEVERITY_INFO,
					"El usuario ha sido guardado satisfactoriamente");
				} catch (Exception e) {
					BeanUtil.getInstance()
							.addMessage(FacesMessage.SEVERITY_ERROR,
									"El nombre de usuario "
											+ user
											+ " existe actualmente en la base de datos");
					e.printStackTrace();
				}
			} else {
				BeanUtil.getInstance().addMessage(FacesMessage.SEVERITY_ERROR,
						"La contrasena y la confirmacion no coinciden");
			}
		}
		return "main";
	}

	private boolean checkPassword() {
		return userPassword.compareTo((userPasswordConfirm)) == 0;
	}

	public List<String> getSelectedRoles() {
		return selectedRoles;
	}

	public void setSelectedRoles(List<String> selectedRoles) {
		this.selectedRoles = selectedRoles;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
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

	public void setCompleteName(String completeName) {
		this.completeName = completeName;
	}

	public String getCompleteName() {
		return completeName;
	}

}
