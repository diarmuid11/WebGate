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
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.correlibre.qop.domain.SurveyStructure;
import org.correlibre.qop.domain.SurveyStructureState;
import org.correlibre.qop.services.SrvSurveysEngineLocal;

@ManagedBean(name="showSurveyStructures")
@ViewScoped
public class ShowSurveyStructures {
	
	@EJB
	private SrvSurveysEngineLocal srvSurveysEngine;

	private List<SurveyStructure> surveyStructures;	
	private List<Object> surveyInstances;
	
	
	@PostConstruct
	public void init(){
		surveyStructures =  srvSurveysEngine.getParentSurveyStructures();
		surveyInstances = srvSurveysEngine.getSurveysCount();		
	}

	public List<SurveyStructure> getSurveyStructures() {
		return surveyStructures;
	}
		
	public List<Object> getSurveyInstances() {
		return surveyInstances;
	}
	
	public boolean isDeployed(SurveyStructure ss){
		return ss.getState()==SurveyStructureState.DEPLOYED;
	}
}