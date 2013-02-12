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
package org.correlibre.qop.creationviewadapters;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.correlibre.qop.domain.CategoryDomain;
import org.correlibre.qop.domain.Question;
import org.correlibre.qop.domain.QuestionType;
import org.correlibre.qop.services.QopException;
import org.correlibre.qop.util.BeanUtil;

public class ChoiceQuestionWithPredefinedCategoryDomain extends
		BaseCreationViewAdapter {
	
	private List<CategoryDomain> categoryDomains;
	
	private int selectedCategoryDomain;
	
	@Override
	public void init() {
		System.out.println("#################### [ChoiceQuestionWithPredefinedCategoryDomain] init");
		
		categoryDomains = engine.getAllCategoryDomains();
		
		if (creating){
			question = new Question();
			
			selectedCategoryDomain = categoryDomains.get(0).getId();
			System.out.println("#################### [ChoiceQuestionWithPredefinedCategoryDomain] creating: "+creating);
			try {
				
				question.setCategoryDomain(this.engine.getCategoryDomain(selectedCategoryDomain));
				
			} catch (QopException e) {
				
				e.printStackTrace();
				BeanUtil.getInstance().addMessage(FacesMessage.SEVERITY_ERROR, "MMMM :"+e.getMessage());
			}
			
			QuestionType qt=new QuestionType();
			qt.setId(10);
			question.setQuestionType(qt);
						
		}else{
			
			selectedCategoryDomain = question.getCategoryDomain().getId();
			System.out.println("#################### [ChoiceQuestionWithPredefinedCategoryDomain] selectedCategoryDomain id: "+selectedCategoryDomain);
			System.out.println("#################### [ChoiceQuestionWithPredefinedCategoryDomain] question.getCategoryDomain(): "+question.getCategoryDomain().getName());
			//options = engine.getSubQuestions(question, 7);
		}
		
		
		System.out.println("#################### [ChoiceQuestionWithPredefinedCategoryDomain] categoryDomains: "+categoryDomains);
	}

	@Override
	public void adaptEditedSubQuestions() {
		//childQuestion.setCategoryDomain(categoryDomain)
		editedSubQuestions.clear();

	}
	
	public ChoiceQuestionWithPredefinedCategoryDomain() throws QopException{
		super();
		
	}
	
	public void changeCategoryDomain(ValueChangeEvent e){
		
		int categoryDomainId = (Integer) e.getNewValue();
		
		try {
			CategoryDomain cd = engine.getCategoryDomain(categoryDomainId);
			
			question.setCategoryDomain(cd) ;
			
			selectedCategoryDomain = categoryDomainId;
		} catch (QopException e1) {
			
			e1.printStackTrace();
			BeanUtil.getInstance().addMessage(e1.getMessage());
		}
		
		System.out.println("#################### [ChoiceQuestionWithPredefinedCategoryDomain] categoryDomainId: "+categoryDomainId);
	}
	
	public List<SelectItem> getCategoryDomainItems(){
		
		return BeanUtil.getInstance().getCategoryDomainsAsSelectItems(getCategoryDomains());
	}

	public void setCategoryDomains(List<CategoryDomain> categoryDomains) {
		this.categoryDomains = categoryDomains;
	}

	public List<CategoryDomain> getCategoryDomains() {
		return categoryDomains;
	}

	public int getSelectedCategoryDomain() {
		return selectedCategoryDomain;
	}

	public void setSelectedCategoryDomain(int selectedCategoryDomain) {
		this.selectedCategoryDomain = selectedCategoryDomain;
	}

}
