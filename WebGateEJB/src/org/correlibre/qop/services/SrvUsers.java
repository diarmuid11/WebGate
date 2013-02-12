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
package org.correlibre.qop.services;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.correlibre.qop.domain.Role;
import org.correlibre.qop.domain.User;

public interface SrvUsers {
	
	public abstract String getUser(String login); //TODO Unificar
	public abstract User getUserObj(String userLogin); //TODO: unificar
	public abstract List<String>getUsers();
	public abstract List<String>getUsers(String userName, Role role);
	public abstract List<String> getUsers(Role role);
	public abstract void setUsers(User user)throws QopException;
	public abstract String encrypt(String s) throws NoSuchAlgorithmException, UnsupportedEncodingException;
	public abstract void delete(String userName)throws QopException ;
	public abstract void modifyUser(User user);
	public abstract void changePassword(String userName, String actualPassword, String newPassword, String confirmPassword)throws QopException;
	
}
