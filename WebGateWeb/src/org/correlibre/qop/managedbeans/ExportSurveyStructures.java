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

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletResponse;

import org.correlibre.qop.domain.SurveyStructure;
import org.correlibre.qop.services.QopException;
import org.correlibre.qop.services.SrvSurveysEngineLocal;
import org.correlibre.qop.services.SrvSynchLocal;

@ManagedBean(name="exportSurveyStructures")
@ViewScoped
public class ExportSurveyStructures {
	
	@EJB
	private SrvSynchLocal srvSynch;
	
	@EJB
	private SrvSurveysEngineLocal srvSurveysEngine;

	private List<SelectItem> surveyStructureItems;
	
	private Integer surveyStructureId;
	
	@PostConstruct
	private void init(){
		surveyStructureItems=new ArrayList<SelectItem>();
		for (SurveyStructure ss:srvSurveysEngine.getSurveyStructures()){
			surveyStructureItems.add(new SelectItem(ss.getId(),ss.getName()));
		}
	}
	
	public Integer getSurveyStructureId() {
		return surveyStructureId;
	}

	public void setSurveyStructureId(Integer surveyStructureId) {
		this.surveyStructureId = surveyStructureId;
	}

	public List<SelectItem> getSurveyStructureItems() {
		return surveyStructureItems;
	}

	public void exportSurveyStructure(ActionEvent e) throws QopException, IOException{
		HttpServletResponse r=(HttpServletResponse)FacesContext.getCurrentInstance().getExternalContext().getResponse();
		r.setContentType("application/xml");
		//Writer w=FacesContext.getCurrentInstance().getExternalContext().getResponseOutputWriter();
		Writer w=r.getWriter();
		srvSynch.exportSurveyStructure(surveyStructureId, w);
		w.flush();
		FacesContext.getCurrentInstance().responseComplete();
	}
}
