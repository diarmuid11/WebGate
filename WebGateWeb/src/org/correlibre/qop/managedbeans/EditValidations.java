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
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import org.correlibre.qop.domain.Question;
import org.correlibre.qop.domain.Validation;
import org.correlibre.qop.domain.ValidationType;
import org.correlibre.qop.services.QopException;
import org.correlibre.qop.services.SrvSurveysAdminLocal;
import org.correlibre.qop.services.SrvSurveysEngineLocal;

@ManagedBean(name="editValidations")
@ViewScoped
public class EditValidations {
	
	@EJB
	private SrvSurveysAdminLocal srvSurveysAdmin;	

	@EJB
	private SrvSurveysEngineLocal srvSurveysEngine;	

	private Question question;
	
	private List<Validation> validations;
	
	private List<SelectItem> validationTypeItems;
	
	private String validationTypeId;
	
	
		
	@PostConstruct
	public void init() throws QopException{
		//TODO: Pasar objetos no ids
		HttpServletRequest request=(HttpServletRequest)FacesContext
			.getCurrentInstance().getExternalContext().getRequest();
		String questionId=request.getParameter("questionId");
		question= srvSurveysEngine.getQuestion(new Integer(questionId));
		validations=question.getValidations();
		initValidationTypes();
	}		

	private void initValidationTypes() {
		validationTypeItems=new ArrayList<SelectItem>();
		for(ValidationType vt: question.getQuestionType().getValidationTypes()){
			validationTypeItems.add(new SelectItem(vt.getId(), vt.getName()));
		}
	}
	
	public List<SelectItem> getValidationTypeItems() {
		return validationTypeItems;
	}

	public Question getQuestion() {
		return question;
	}

	public List<Validation> getValidations() {
		return validations;
	}

	public String getValidationTypeId() {
		return validationTypeId;
	}

	public void setValidationTypeId(String validationTypeId) {
		this.validationTypeId = validationTypeId;
	}
	
	public void newValidation(ActionEvent e){
		srvSurveysAdmin.addValidation(question,validationTypeId);
		validations=question.getValidations();
	}
	
	
}