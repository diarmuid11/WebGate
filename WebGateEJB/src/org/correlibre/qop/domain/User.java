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
package org.correlibre.qop.domain;

import java.io.Serializable;
import javax.persistence.*;

import java.util.List;


/**
 * The persistent class for the principals database table.
 * 
 */
@Entity
@Table(name="principals", schema="surveys")
@NamedQueries({
	@NamedQuery(name="User.findAll",query="Select u from User u where u.activo = 1"),
	@NamedQuery(name="User.findByRole",query="select u from User u where u.activo = 1 and :role member of u.roles"),
	@NamedQuery(name="User.findByUserAndRole", query="select u from User u where u.activo = 1 and u.userName =:userName and :role member of u.roles")
})
public class User implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="principal_name")
	private String userName;

	@Column(name="principal_password")
	private String userPassword;
	
	@Column(name="activo")
	private Integer activo;
	
	@Column(name="complete_name")
	private String completeName;
	
	@ElementCollection
	@CollectionTable(name="principals_roles",schema="surveys",
	       joinColumns=@JoinColumn(name="principal_name")
	)	
	@Enumerated(EnumType.STRING)
	@Column(name="role_name")
	private List<Role> roles;		
	


	public User() {
    
    }

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String principalName) {
		this.userName = principalName;
	}

	public String getUserPassword() {
		return this.userPassword;
	}

	public void setUserPassword(String principalPassword) {
		this.userPassword = principalPassword;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}
	public Integer getActivo() {
		return activo;
	}

	public void setActivo(Integer activo) {
		this.activo = activo;
	}

	public void setCompleteName(String completeName) {
		this.completeName = completeName;
	}

	public String getCompleteName() {
		return completeName;
	}

	public String toString(){
		return "userName: "+ userName + ", completeName: "+ completeName;
	}
		
}