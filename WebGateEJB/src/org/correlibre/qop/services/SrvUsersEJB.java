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
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.correlibre.qop.domain.Role;
import org.correlibre.qop.domain.SurveyState;
import org.correlibre.qop.domain.User;

import sun.misc.BASE64Encoder;

import com.sun.mail.util.BASE64EncoderStream;

//TODO:  Colocar roles en metodos

/**
 * Session Bean implementation class SrvUsersEJB
 */
@Stateless
public class SrvUsersEJB implements SrvUsersLocal {
	
	@PersistenceContext
	private EntityManager em;
	
	
    /**
     * Default constructor. 
     */
    public SrvUsersEJB() {
        // TODO Auto-generated constructor stub
    }

	@Override
	public String getUser(String userLogin){
		User u = em.find(User.class, userLogin);
		if(u == null)
			return null;
		
		return u.getUserName();
	}	
	
	@Override
	public User getUserObj(String userLogin){
		return em.find(User.class, userLogin);		
	}
	
	@Override
	public List<String> getUsers() {
		List<User> users =  em.createNamedQuery("User.findAll").getResultList();
		List<String> userNames = new ArrayList<String>();
		for(User u: users){
			userNames.add(u.getUserName());
		}
		return userNames;
	}

	@Override
	public List<String> getUsers(Role role) {
		Query q = em.createNamedQuery("User.findByRole");
		q.setParameter("role", role);
		List<User> users =  q.getResultList(); 		
		List<String> userNames = new ArrayList<String>();
		for(User u: users){
			userNames.add(u.getUserName());
		}
		return userNames;
	}

	@Override
	public void setUsers(User user) throws QopException{
		user.setActivo(1);
		em.persist(user);
	}

	@Override
	public String encrypt(String s) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		MessageDigest md = MessageDigest.getInstance("SHA-256");			
		md.update(s.getBytes("UTF-8"));
		byte[] digest = md.digest();
		String enc = new BASE64Encoder().encode(digest);
		return enc;
		
	}

	@Override
	public List<String> getUsers(String userName, Role role) {
		Query q = em.createNamedQuery("User.findByUserAndRole");
		q.setParameter("userName", userName);
		q.setParameter("role", role);
		List<User> users =  q.getResultList(); 		
		List<String> userNames = new ArrayList<String>();
		for(User u: users){
			userNames.add(u.getUserName());
		}
		return userNames;
	}

	@Override
	public void delete(String userName) throws QopException {
		Query q = em.createNamedQuery("Survey.NumberOfSurveysByState");
		q.setParameter("userLogin", userName);
		q.setParameter("surveyState", SurveyState.FILLING); 
		Long activas = (Long) q.getSingleResult();
		
		q = em.createNamedQuery("Survey.NumberOfSurveysByState");
		q.setParameter("userLogin", userName);
		q.setParameter("surveyState", SurveyState.NEW);
		activas += (Long)q.getSingleResult();
		
		if (activas > 0){
			throw new QopException("No es posible eliminar un usuario con " + 
					"encuestas activas"); 
		}
		else{
			User u = em.find(User.class, userName);
			u.setActivo(0);
			u.setRoles(null);
			em.persist(u);
		}
	}
	
	@Override
	public void modifyUser(User user){
		em.merge(user);
	}
	
	public void changePassword(String userName, String actualPassword, String newPassword, String confirmPassword)throws QopException{
		
		User actualUser;
		
		try {
			if(newPassword.compareTo(confirmPassword) == 0){
			
				actualUser = this.getUserObj(userName);
				
				System.out.println("########### [SrvUsersEJB] actualUser: "+actualUser);
				
				System.out.println("########### [SrvUsersEJB] actualPassword: "+actualPassword);
				System.out.println("########### [SrvUsersEJB] encrypt(actualPassword): "+encrypt(actualPassword));
				System.out.println("########### [SrvUsersEJB] actualUser.getUserPassword(): "+actualUser.getUserPassword());
				System.out.println("########### [SrvUsersEJB] actualUser.getUserPassword().compareTo(encrypt(actualPassword)): "+actualUser.getUserPassword().compareTo(encrypt(actualPassword)));
				
				if(actualUser.getUserPassword().compareTo(encrypt(actualPassword)) == 0){
					actualUser.setUserPassword(encrypt(newPassword));
					em.merge(actualUser);
				}else{
					throw new QopException("La contraseña actual no es correcta");
				}
			
			}else{
				
				throw new QopException("La nueva contraseña y la confirmación no conciden");
				
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw new QopException(e.getMessage());
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new QopException(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			throw new QopException(e.getMessage());
		}
		
		
		
	}
}
