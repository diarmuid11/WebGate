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

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServletRequest;

import org.correlibre.qop.domain.Question;
import org.correlibre.qop.domain.SurveyStructure;
import org.correlibre.qop.domain.SurveyStructureState;
import org.correlibre.qop.services.QopException;
import org.correlibre.qop.services.SrvSurveysAdminLocal;
import org.correlibre.qop.services.SrvSurveysEngineLocal;
import org.correlibre.qop.util.BeanUtil;

@ManagedBean(name="editSurveyStructure")
@ViewScoped
public class EditSurveyStructure {
	
	@EJB
	private SrvSurveysEngineLocal srvSurveysEngine;	
	
	@EJB
	private SrvSurveysAdminLocal srvSurveysAdmin;	

	private SurveyStructure surveyStructure; 
	
	private List<Question> questions;

	@PostConstruct
	public void init() throws QopException{
		//TODO: Pasar objetos no ids
		
		System.out.println("############### [EditSurveyStructure] init");
		
		String id=null;
		try {
			HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
			id = request.getParameter("surveyStructureId");
			
			System.out.println("############### [EditSurveyStructure] surveyStructureId: "+id);
			
			if (id != null && id.length() > 0){
				surveyStructure  = srvSurveysEngine.getSurveyStructure(Integer.parseInt(id)); 
				questions = srvSurveysEngine.getSurveyStructureQuestions(surveyStructure); 
			}else{
				
				Flash f = FacesContext.getCurrentInstance().getExternalContext().getFlash();
				surveyStructure = (SurveyStructure) f.get("surveyStructure");
				
				if(surveyStructure == null){
					BeanUtil.getInstance().addMessage(FacesMessage.SEVERITY_ERROR,"Parametro de Modelo de Encuesta Invalido: "+id);
				}else{
					questions = srvSurveysEngine.getSurveyStructureQuestions(surveyStructure); 
				}
			}
			
			System.out.println("############### [EditSurveyStructure] surveyStructure: "+surveyStructure);
			
		} catch (NumberFormatException e) {
			e.printStackTrace();
			BeanUtil.getInstance().addMessage(FacesMessage.SEVERITY_ERROR,"Parametro de Modelo de Encuesta Inválido: "+id);
		} catch (QopException e) {
			e.printStackTrace();
			BeanUtil.getInstance().addMessage(FacesMessage.SEVERITY_ERROR,"Imposible inicializar Bean EditSurveyStructure: "+e.getMessage());
		}
		
	}

	public SurveyStructure getSurveyStructure() {
		return surveyStructure;
	}

	public List<Question> getQuestions() {
		return questions;
	}
	
	public boolean isInEdition(){
		return surveyStructure.getState() == SurveyStructureState.IN_EDITION;
	}
	
	public boolean isCreating(){
		return surveyStructure.getState() == SurveyStructureState.CREATING;
	}
	
	public void goUp(ActionEvent e) throws QopException{
		Question q= (Question)e.getComponent().getAttributes().get("question");
		srvSurveysAdmin.reorderQuestion(surveyStructure, q, true, 1);		
		questions = srvSurveysEngine.getSurveyStructureQuestions(surveyStructure); 
	}
	
	public void goDown(ActionEvent e) throws QopException{
		Question q= (Question)e.getComponent().getAttributes().get("question");
		srvSurveysAdmin.reorderQuestion(surveyStructure, q, false, 1);	
		System.out.println("########### [EditSurveyStructure] q.getQuestionType().getValidationTypes(): "+q.getQuestionType().getValidationTypes());
		questions = srvSurveysEngine.getSurveyStructureQuestions(surveyStructure); 
	}
	
	public void editQuestion(Question q){
		FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("question", q);
		HttpServletRequest req=(HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
	}
	
	public int getSizeQuestions(){
		
		if(questions != null)
			return questions.size();
		
		return 0;
	}
	
}