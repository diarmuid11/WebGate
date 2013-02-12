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
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.correlibre.qop.domain.CategoryDomain;
import org.correlibre.qop.domain.CategoryOption;
import org.correlibre.qop.domain.Question;
import org.correlibre.qop.domain.SurveyState;
import org.correlibre.qop.domain.SurveyStructure;
import org.jsoup.Jsoup;

public class BeanUtil {
	private static BeanUtil instance = new BeanUtil();

	public static BeanUtil getInstance() {
		return instance;
	}

	private BeanUtil() {

	}

	public void addMessage(String message) {
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, message, ""));
	}

	public void addMessage(Severity severity, String message) {
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(severity, message, ""));
	}

	public void refreshPage() {
		FacesContext context = FacesContext.getCurrentInstance();
		String currentView = context.getViewRoot().getViewId();
		ViewHandler vh = context.getApplication().getViewHandler();
		UIViewRoot x = vh.createView(context, currentView);
		context.setViewRoot(x);
	}

	public List<SelectItem> getQuestionsOrderedAsSelectItems(List<Question> questions) {
		
		List<SelectItem> items = new ArrayList<SelectItem>();
		for (Question q : questions) {
			
			String text = q.getText();
			
			if(text.indexOf("<br/>") > 0)
				text = text.substring(0, text.indexOf("<br/>"));
			
			String plainText = Jsoup.parse(text).text();
			
			System.out.println("[BeanUtil] plainText: "+plainText);
			
			SelectItem item = new SelectItem(q.getId(), q.getOrdinal() +" : "+ plainText);
			items.add(item);
		}
		
		Collections.sort(items, new SelectItemStringLabelComparator());
		
		return items;
	}
	
	public List<SelectItem> getQuestionsAsSelectItems(List<Question> questions) {
		
		List<SelectItem> items = new ArrayList<SelectItem>();
		for (Question q : questions) {
			
			String text = q.getText();
			
			if(text.indexOf("<br/>") > 0)
				text = text.substring(0, text.indexOf("<br/>"));
			
			String plainText = Jsoup.parse(text).text();
			
			System.out.println("[BeanUtil] plainText: "+plainText);
			
			SelectItem item = new SelectItem(q.getId(), plainText);
			items.add(item);
		}
		return items;
	}
	
	public List<SelectItem> getQuestionsAsSelectItems(List<Question> questions, boolean showValueAndLabel) {
		List<SelectItem> items = new ArrayList<SelectItem>();
		for (Question q : questions) {
			
			SelectItem item = null;
			
			if(showValueAndLabel)
				item = new SelectItem(q.getId(), q.getName() + " : " +q.getText());
			else
				item = new SelectItem(q.getId(), q.getText());
			
			items.add(item);
		}
		return items;
	}
	
	public List<SelectItem> getUserAsSelectItems(
			List<String> userList) {
		
		System.out.println("[BeanUtil] userList: "+userList);
		
		List<SelectItem> items = new ArrayList<SelectItem>();
		
		for (String str : userList) {
			SelectItem item = new SelectItem(str, str);
			items.add(item);
		}
		
		Collections.sort(items, new SelectItemStringLabelComparator());
		
		return items;
	}

	public List<SelectItem> getSurveyStatesAsSelectItems(
			List<SurveyState> surveyStates) {
		List<SelectItem> items = new ArrayList<SelectItem>();
		
		//items.add(new SelectItem("", "Todos"));
		
		for (SurveyState s : surveyStates) {
			SelectItem item = new SelectItem(s.getName(), s.getName());
			items.add(item);
		}
		return items;
	}
	
	public List<SelectItem> getSurveyStatesAsSelectItems(
			List<SurveyState> surveyStates, boolean firstEmpty) {
		List<SelectItem> items = new ArrayList<SelectItem>();
		
		if(firstEmpty)
			items.add(new SelectItem("", "Seleccione una opción"));
		
		for (SurveyState s : surveyStates) {
			SelectItem item = new SelectItem(s.getName(), s.getName());
			items.add(item);
		}
		return items;
	}

	public List<SelectItem> getCategoryOptionsAsSelectItems(List<CategoryOption> options, boolean showValueOnLabel) {
		List<SelectItem> items = new ArrayList<SelectItem>();
		for (CategoryOption op : options) {
			SelectItem item = null;
			if(showValueOnLabel)
				item = new SelectItem(op.getId(), op.getValue()+ " : " + op.getLabel());
			else
				item = new SelectItem(op.getId(), op.getLabel());
			
			items.add(item);
		}
		Collections.sort(items, new SelectItemStringLabelComparator());
		return items;
	}
	
	public List<SelectItem> getCategoryOptionsAsSelectItemsString(List<CategoryOption> options, boolean showValueOnLabel) {
		List<SelectItem> items = new ArrayList<SelectItem>();
		for (CategoryOption op : options) {
			SelectItem item = null;
			if(showValueOnLabel)
				item = new SelectItem(op.getLabel(), op.getValue()+ " : " + op.getLabel());
			else
				item = new SelectItem(op.getLabel(), op.getLabel());
			
			items.add(item);
		}
		Collections.sort(items, new SelectItemStringLabelComparator());
		return items;
	}

	public List<SelectItem> getCategoryDomainsAsSelectItems(
			List<CategoryDomain> cds) {

		List<SelectItem> listCategoryDomainItems = new ArrayList<SelectItem>();

		for (CategoryDomain cd : cds) {
			listCategoryDomainItems
					.add(new SelectItem(cd.getId(), cd.getName()));
		}

		Collections.sort(listCategoryDomainItems,
				new SelectItemStringLabelComparator());

		return listCategoryDomainItems;
	}

	public List<SelectItem> getSurveyStructuresAsSelectItems(
			List<SurveyStructure> surveyStructures) {
		List<SelectItem> items = new ArrayList<SelectItem>();
		
		for (SurveyStructure ss : surveyStructures) {
			SelectItem item = new SelectItem(ss.getId().toString(),ss.getName());
			items.add(item);
		}
		return items;
	}

	public static String convertCamelCase(String string) {
		String result = "";
		for (int i = 0; i < string.length(); i++) {
			String next = string.substring(i, i + 1);
			if (i == 0) {
				result += next.toUpperCase();
			} else {
				result += next.toLowerCase();
			}
		}
		return result;
	}

}