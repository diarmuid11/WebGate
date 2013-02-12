package org.correlibre.qop.managedbeans;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import org.correlibre.qop.services.QopException;
import org.correlibre.qop.services.SrvUsersLocal;
import org.correlibre.qop.util.BeanUtil;

@ManagedBean
@ViewScoped
public class ModifyPassword {

	@EJB 
	private SrvUsersLocal srvUser;
	
	private String userName;
	
	private String actualPassword;
	private String newPassword;
	private String confirmPassword;
	
	public void updatePassword(ActionEvent event){
		
		userName = (String)event.getComponent().getAttributes().get("userName");
		
		System.out.println("############### [ModifyPassword] userName: "+userName);
		
		try {
			
			if(actualPassword != null && actualPassword.length() == 0){
				BeanUtil.getInstance().addMessage("La contrase�a actual no puede ser vac�a");
				return;
			}
			
			if(newPassword != null && newPassword.length() == 0){
				BeanUtil.getInstance().addMessage("La nueva contrase�a no puede ser vac�a");
				return;
			}
				
			if(confirmPassword != null && confirmPassword.length() == 0){
				BeanUtil.getInstance().addMessage("La confirmaci�n de la nueva contrase�a no puede ser vac�a");
				return;
			}
			
			srvUser.changePassword(userName, actualPassword, newPassword, confirmPassword);
			
			BeanUtil.getInstance().addMessage("La contrase�a se ha actualizado correctamente");
			
		} catch (QopException e) {
			BeanUtil.getInstance().addMessage(e.getMessage());
		}
		
	}

	public String getActualPassword() {
		return actualPassword;
	}

	public void setActualPassword(String actualPassword) {
		this.actualPassword = actualPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
	
}
