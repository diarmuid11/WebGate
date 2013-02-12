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

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import org.correlibre.qop.domain.Answer;
import org.correlibre.qop.domain.ConfigProperty;
import org.correlibre.qop.domain.Question;
import org.correlibre.qop.domain.Survey;
import org.correlibre.qop.domain.SurveyState;
import org.correlibre.qop.domain.SurveyStructure;
import org.correlibre.qop.services.QopException;
import org.correlibre.qop.services.SrvSurveysEngineLocal;
import org.correlibre.qop.util.BeanUtil;
import org.correlibre.qop.util.SortableSurveyList;
import org.icefaces.bean.ViewRetained;

@ManagedBean(name="showChildrenSurveys")
@ViewScoped
public class ShowChildrenSurveys extends SortableSurveyList{
	
	
	@EJB
	private SrvSurveysEngineLocal srvSurveysEngine;	
	//private List<Survey> surveys;
	private Survey parentSurvey;

	private String surveyState;
	
	private String surveyStructureName;
	
	private SurveyStructure childSurveyStructure;
	
	private boolean hasIdentifyQuestion;
	
	private String identityQuestionText;
	
	long cuentaGlobal = 0;
	
	protected ShowChildrenSurveys(String defaultSortColumn) {
		super(defaultSortColumn);
	}
	
	public ShowChildrenSurveys() {
		super(SortableSurveyList.RESPUESTA_COLUMN_NAME);
	}
	
	@PostConstruct
	public void init(){
		
		long inicio = System.currentTimeMillis();
		long antesOrden = 0;
		long fin = 0;
		try {
			System.out.println("#################### [ShowChildrenSurveys] parentSurvey: "+parentSurvey);
			
			HttpServletRequest request= (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
			
			String surveyStructureId = null;

			if(parentSurvey == null){
				
				surveyState = request.getParameter("surveyState");
				String parentSurveyId = request.getParameter("parentSurveyId");
				
				System.out.println("#################### [ShowChildrenSurveys] parentSurveyId: "+parentSurveyId);
				if(parentSurveyId != null && parentSurveyId.length() > 0){
					parentSurvey = srvSurveysEngine.getSurvey(new Integer(parentSurveyId));
					
					System.out.println("#################### [ShowChildrenSurveys] parentSurvey: "+parentSurvey);
					parentSurvey.setAnswers(srvSurveysEngine.getAllSurveyAnswers(parentSurvey));
					
					System.out.println("#################### [ShowChildrenSurveys] surveyState: "+surveyState);
				}else{
					
					Flash f = FacesContext.getCurrentInstance().getExternalContext().getFlash();
					
					Survey tmpSurvey = (Survey) f.get("parentSurvey");
					System.out.println("##################   [ShowChildrenSurveys] tmpSurvey: "+tmpSurvey );
					
					if(tmpSurvey != null){
						parentSurvey = tmpSurvey;
					}
					
				}
			
			}
			
			if(childSurveyStructure == null){
				
				SurveyStructure surveyStructure = null;
				
				surveyStructureId = request.getParameter("surveyStructureId");
				
				if(surveyStructureId != null && !"".equalsIgnoreCase(surveyStructureId)){
					surveyStructure = srvSurveysEngine.getSurveyStructure(new Integer(surveyStructureId));
					childSurveyStructure = surveyStructure;
				}
				
			}
			
			
			System.out.println("[ShowChildrenSurveys] childSurveyStructure: "+childSurveyStructure);
			
			if(parentSurvey != null){
				List<Survey> tmpSurveys = parentSurvey.getSubSurveys(); 
				
				System.out.println("[ShowChildrenSurveys] parentSurvey.getSubSurveys(): "+parentSurvey.getSubSurveys());
				
				surveys = new ArrayList<Survey>();
				
				if(childSurveyStructure == null){
					childSurveyStructure = parentSurvey.getSurveyStructure().getChildrenSurveyStructures().get(0);
				}
				
				for (Survey survey : tmpSurveys) {
					if(!survey.getSurveyState().equals(SurveyState.DELETED) && survey.getSurveyStructure().getId().intValue() ==  childSurveyStructure.getId().intValue()){
						surveys.add(survey);
					}
				}
				
				
				
				if(childSurveyStructure != null){
				
					surveyStructureName = childSurveyStructure.getName();
					
					hasIdentifyQuestion = childSurveyStructure.getIdentifyQuestion() != null;
					
					if(hasIdentifyQuestion)
						identityQuestionText = childSurveyStructure.getIdentifyQuestion().getText();
				
				}
				antesOrden = System.currentTimeMillis();
				this.sort();
			}
			
			System.out.println("#################### [ShowChildrenSurveys] surveys: "+surveys);
		} catch (NumberFormatException e) {
			BeanUtil.getInstance().addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			e.printStackTrace();
		} catch (QopException e) {
			BeanUtil.getInstance().addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			BeanUtil.getInstance().addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			e.printStackTrace();
		}
		
		fin = System.currentTimeMillis();
		
		System.out.println("Fin - inicio: " +(fin - inicio));
		System.out.println("Fin - antesOrden: " + (antesOrden != 0 ? (fin - antesOrden) : "NA" ));
		
		cuentaGlobal = 0;
		
	}
	
	public void reInit(Survey parentSurvey){
		System.out.println("############### [ShowChildrenSurveys] reInit");
		System.out.println("############### [ShowChildrenSurveys] parentSurvey: "+parentSurvey);
		System.out.println("############### [ShowChildrenSurveys] this.parentSurvey: "+this.parentSurvey);
		
		if(this.parentSurvey == null){
			this.parentSurvey = parentSurvey;
			init();
			//return true;
		}
		//return false;
	}
	
	
	public List<SelectItem> getSurveyStates(){
		
		List<SurveyState> surveyStates = new ArrayList<SurveyState>();
		
		for (SurveyState surveyState : SurveyState.values()) {
			surveyStates.add(surveyState);
		}
		
		return BeanUtil.getInstance().getSurveyStatesAsSelectItems(surveyStates);
		
	}
	
	public void updateSurveys(ActionEvent e){
		
		System.out.println("########### [ShowChildrenSurveys] surveyState: "+surveyState);
		
		if(surveyState == null || surveyState.length() <= 0){
			surveys = srvSurveysEngine.getAllParentUserSurveys();
		}else{
			
			SurveyState ss = SurveyState.fromString(surveyState); 
			
			surveys = srvSurveysEngine.getAllParentUserSurveys(ss);
		}
			
				
		for (Survey s : surveys) {
			s.setAnswers(srvSurveysEngine.getAllSurveyAnswers(s));
		}
		
	}
	
	public String initSelectedChildSurvey(Survey parentSurvey){
		
		System.out.println("########### [ShowSurveys] initSelectedChildSurvey");
		System.out.println("########### [ShowSurveys] parentSurvey.getSurveyStructure().getChildrenSurveyStructures().get(0): "+parentSurvey.getSurveyStructure().getChildrenSurveyStructures().get(0));
		System.out.println("########### [ShowSurveys] parentSurvey.getId(): "+parentSurvey.getId());
		
		String surveyStructureId = parentSurvey.getSurveyStructure().getChildrenSurveyStructures().get(0).getId().toString();
		
		System.out.println("########### [ShowSurveys] surveyStructureId: "+surveyStructureId);
		
		FacesContext.getCurrentInstance().getExternalContext().getFlash().put("parentSurveyId", ""+ parentSurvey.getId());
		FacesContext.getCurrentInstance().getExternalContext().getFlash().put("surveyStructureId", surveyStructureId);
		
		return "fill_survey";
		
	}
	
	public void open(Survey survey){
		try {
			survey.setSurveyState(SurveyState.FILLING);
			this.srvSurveysEngine.updateSurvey(survey);
		} catch (QopException e) {
			e.printStackTrace();
			BeanUtil.getInstance().addMessage(e.getMessage());
		}
	}
	
	public void delete(ActionEvent e){
		
		Survey survey = (Survey) e.getComponent().getAttributes().get("survey");
		
		if(survey != null){
			
			try {
				survey.setSurveyState(SurveyState.DELETED);
				this.srvSurveysEngine.updateSurvey(survey);
				//this.srvSurveysEngine.refreshSurvey(survey.getParentSurvey());
				BeanUtil.getInstance().addMessage("Encuesta eliminada");
			} catch (QopException ex) {
				ex.printStackTrace();
				BeanUtil.getInstance().addMessage(FacesMessage.SEVERITY_ERROR,ex.getMessage());
			}
		}else{
			BeanUtil.getInstance().addMessage(FacesMessage.SEVERITY_ERROR,"No se encontró ninguna encuesta");
		}
	}
	
		public String getReferenceTime(Survey survey){
		
		String referenceTime = "";
		
		try {
			ConfigProperty showReferenceTimeProperty = srvSurveysEngine.getConfigProperty("showReferenceTime", survey.getSurveyStructure(), null);
			
			if(showReferenceTimeProperty != null && Boolean.parseBoolean(showReferenceTimeProperty.getValue())){
				
					SimpleDateFormat sdf = null; 
					
					
					ConfigProperty dateFormatProperty = srvSurveysEngine.getConfigProperty("dateFormat", survey.getSurveyStructure(), null);
					
					sdf = new SimpleDateFormat(dateFormatProperty.getValue());
					
					if(survey.getReferenceTime() != null)
						referenceTime = BeanUtil.convertCamelCase(sdf.format(survey.getReferenceTime()));
				
			}
		} catch (QopException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return referenceTime;
		
	}
	
	public Object getAnswerIdentifyQuestion(Survey survey){
		
		
		long inicioLocal = System.currentTimeMillis();
		long finLocal = 0;
		
		Answer ans = srvSurveysEngine.getSingleAnswer(survey, survey.getSurveyStructure().getIdentifyQuestion());
		
		if(ans != null){
			if(survey.getSurveyStructure().getIdentifyQuestion().getQuestionType().getId() == 14){
				finLocal = System.currentTimeMillis();
				cuentaGlobal += (finLocal - inicioLocal);
				System.out.println("cuentaGlobal: "+cuentaGlobal);
				return new Long(ans.getValue());
			}else{
				finLocal = System.currentTimeMillis();
				cuentaGlobal += (finLocal - inicioLocal);
				System.out.println("cuentaGlobal: "+cuentaGlobal);
				return ans.getValue();
			}
		}else
			return "";
	}
	
	public String editSurvey(Survey survey){
		
		try{
			
			System.out.println("########### [ShowChildrenSurveys] editSurvey");
			System.out.println("########### [ShowChildrenSurveys] survey.getId(): "+survey.getId());
			
			//Survey newSurvey = srvSurveysEngine.createSurvey(new Integer(surveyStructureId), userLogin, null);
			
			
			//System.out.println("########### [ShowChildrenSurveys] newSurvey.getId(): "+newSurvey.getId());
			
			FacesContext.getCurrentInstance().getExternalContext().getFlash().put("surveyId", ""+survey.getId());
			//FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("surveyId", ""+newSurvey.getId());
			
			//System.out.println("########### [ShowSurveys] preNav edit_survey");
			
			return "edit_survey";
			
		}catch(Exception e){
			
			BeanUtil.getInstance().addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			
			return null;
			
		}
		
	}

	
	public int countChildSurvey(Survey s){
		return s.getSubSurveys().size();
	}
	
	public boolean isEndedState(SurveyState s){
		return s.equals(SurveyState.ENDED);
	}
	
	public boolean isClosedState(SurveyState s){
		return s.equals(SurveyState.CLOSED);
	}
	
	public boolean isFillingState(SurveyState s){
		return s.equals(SurveyState.FILLING);
	}
	
	public boolean isDeletedState(SurveyState s){
		return s.equals(SurveyState.DELETED);
	}
	
	public boolean isNewState(SurveyState s){
		return s.equals(SurveyState.NEW);
	}

	public String getSurveyState() {
		return surveyState;
	}

	public void setSurveyState(String surveyState) {
		this.surveyState = surveyState;
	}

	public void setParentSurvey(Survey parentSurvey) {
		this.parentSurvey = parentSurvey;
	}

	public Survey getParentSurvey() {
		return parentSurvey;
	}

	public void setSurveyStructureName(String surveyStructureName) {
		this.surveyStructureName = surveyStructureName;
	}

	public String getSurveyStructureName() {
		return surveyStructureName;
	}

	public void setHasIdentifyQuestion(boolean hasIdentifyQuestion) {
		this.hasIdentifyQuestion = hasIdentifyQuestion;
	}

	public boolean isHasIdentifyQuestion() {
		return hasIdentifyQuestion;
	}

	public void setIdentityQuestionText(String identityQuestionText) {
		this.identityQuestionText = identityQuestionText;
	}

	public String getIdentityQuestionText() {
		return identityQuestionText;
	}

	public void setChildSurveyStructure(SurveyStructure childSurveyStructure) {
		this.childSurveyStructure = childSurveyStructure;
	}

	public SurveyStructure getChildSurveyStructure() {
		return childSurveyStructure;
	}
}
