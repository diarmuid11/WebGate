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
package org.correlibre.qop.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.correlibre.qop.domain.Survey;

public class SortableSurveyList extends SortableList {
	
	public static final String RESPUESTA_COLUMN_NAME = "Respuesta";
	
	protected List<Survey> surveys = new ArrayList<Survey>();
	

	@Override
	protected void sort() {
	    @SuppressWarnings("rawtypes")
		Comparator comparator = new Comparator() {
	        public int compare(Object o1, Object o2) {
	            Survey c1 = (Survey) o1;
	            Survey c2 = (Survey) o2;
	            if (sortColumnName == null) {
	                return 0;
	            }
	            if (sortColumnName.equals(RESPUESTA_COLUMN_NAME)) {
	            	
	            	
	            	
	            	if(c1.getAnswers().size() > 0 && c2.getAnswers().size() > 0){
	            		try{
	            		return ascending ?
	                        new Long(c1.getAnswers().get(0).getValue()).compareTo(new Long(c2.getAnswers().get(0).getValue())) :
	                        new Long(c2.getAnswers().get(0).getValue()).compareTo(new Long(c1.getAnswers().get(0).getValue()));
	            		}catch(NumberFormatException e){
	            			return ascending ?
	                                c1.getAnswers().get(0).getValue().compareTo(c2.getAnswers().get(0).getValue()) :
	                                c2.getAnswers().get(0).getValue().compareTo(c1.getAnswers().get(0).getValue());
	            		}
	            	}else if(c1.getAnswers().size() > 0){
	            		return 1;
	            	}else if(c2.getAnswers().size() > 0){
	            		return -1;
	            	}else
	            		return 0;
	                      
	            }/*
	            else if (sortColumnName.equals(modelColumnName)) {
	                return ascending ? c1.getModel().compareTo(c2.getModel()) :
	                        c2.getModel().compareTo(c1.getModel());
	            } else if (sortColumnName.equals(descriptionColumnName)) {
	                return ascending ? c1.getDescription().compareTo(c2.getDescription()) :
	                        c2.getDescription().compareTo(c1.getDescription());
	            } else if (sortColumnName.equals(odometerColumnName)) {
	                return ascending ?
	                        new Integer(c1.getOdometer()).compareTo(new Integer(c2.getOdometer())) :
	                        new Integer(c2.getOdometer()).compareTo(new Integer(c1.getOdometer()));
	            } else if (sortColumnName.equals(priceColumnName)) {
	                return ascending ?
	                        new Integer(c1.getPrice()).compareTo(new Integer(c2.getPrice())) :
	                        new Integer(c2.getPrice()).compareTo(new Integer(c1.getPrice()));
	            }*/
	            else return 0;
	        }
	    };
	    Collections.sort(surveys, comparator);
	}

	@Override
	protected boolean isDefaultAscending(String sortColumn) {
		return false;
	}

	public List<Survey> getSurveys() {
		
		if (!oldSort.equals(sortColumnName) ||
                oldAscending != ascending){
             sort();
             oldSort = sortColumnName;
             oldAscending = ascending;
        }
		return surveys;
	}

	protected SortableSurveyList(String defaultSortColumn) {
		super(defaultSortColumn);
	}

}
