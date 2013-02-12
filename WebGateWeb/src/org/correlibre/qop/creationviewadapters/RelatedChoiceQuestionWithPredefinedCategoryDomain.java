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

import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.correlibre.qop.domain.CategoryDomain;
import org.correlibre.qop.domain.Question;
import org.correlibre.qop.domain.QuestionType;
import org.correlibre.qop.services.QopException;
import org.correlibre.qop.util.BeanUtil;

public class RelatedChoiceQuestionWithPredefinedCategoryDomain extends
		BaseCreationViewAdapter {

	private Question parentQuestion;

	private Question childQuestion;

	private List<CategoryDomain> categoryDomains;

	private int parentCategoryDomainId;

	private int childCategoryDomainId;

	@Override
	public void init() {
		System.out
				.println("#################### [RelatedChoiceQuestionWithPredefinedCategoryDomain] init");

		categoryDomains = engine.getAllCategoryDomains();

		if (creating) {
			question = new Question();

			QuestionType qt = new QuestionType();
			qt.setId(12);
			question.setQuestionType(qt);

			// Pregunta padre
			parentQuestion = new Question();
			parentQuestion.setQuestion(question);
			parentCategoryDomainId = categoryDomains.get(0).getId();

			parentQuestion.setCategoryDomain(categoryDomains.get(0));

			QuestionType qt2 = new QuestionType();
			qt2.setId(10);
			parentQuestion.setQuestionType(qt2);

			// Pregunta hija
			childQuestion = new Question();
			childQuestion.setQuestion(question);
			childQuestion.setCategoryDomain(categoryDomains.get(0));

			childCategoryDomainId = categoryDomains.get(0).getId();

			QuestionType qt3 = new QuestionType();
			qt3.setId(10);
			childQuestion.setQuestionType(qt3);

		} else {

			parentQuestion = engine.getSubQuestions(question, 10).get(0);
			childQuestion = engine.getSubQuestions(question, 10).get(1);

			parentCategoryDomainId = parentQuestion.getCategoryDomain().getId();
			childCategoryDomainId = childQuestion.getCategoryDomain().getId();

			// options = engine.getSubQuestions(question, 7);
		}

		System.out
				.println("#################### [RelatedChoiceQuestionWithPredefinedCategoryDomain] childQuestion: "
						+ childQuestion);
	}

	public void changeParentCategoryDomain(ValueChangeEvent e) {

		int categoryDomainId = (Integer) e.getNewValue();

		try {
			CategoryDomain cd = engine.getCategoryDomain(categoryDomainId);

			parentQuestion.setCategoryDomain(cd);

			parentCategoryDomainId = categoryDomainId;
		} catch (QopException e1) {

			e1.printStackTrace();
			BeanUtil.getInstance().addMessage(e1.getMessage());
		}

		System.out
				.println("#################### [RelatedChoiceQuestionWithPredefinedCategoryDomain] parentCategoryDomainId: "
						+ parentCategoryDomainId);
	}

	public void changeChildCategoryDomain(ValueChangeEvent e) {

		int categoryDomainId = (Integer) e.getNewValue();

		try {
			CategoryDomain cd = engine.getCategoryDomain(categoryDomainId);

			childQuestion.setCategoryDomain(cd);

			childCategoryDomainId = categoryDomainId;
		} catch (QopException e1) {

			e1.printStackTrace();
			BeanUtil.getInstance().addMessage(e1.getMessage());
		}

		System.out
				.println("#################### [RelatedChoiceQuestionWithPredefinedCategoryDomain] ChildCategoryDomainId: "
						+ childCategoryDomainId);
	}

	@Override
	public void adaptEditedSubQuestions() {
		// childQuestion.setCategoryDomain(categoryDomain)
		editedSubQuestions.clear();
		editedSubQuestions.add(parentQuestion);
		editedSubQuestions.add(childQuestion);

	}

	public RelatedChoiceQuestionWithPredefinedCategoryDomain()
			throws QopException {
		super();

	}

	public List<SelectItem> getCategoryDomainItems() {

		return BeanUtil.getInstance().getCategoryDomainsAsSelectItems(
				getCategoryDomains());
	}

	public void setChildQuestion(Question childQuestion) {
		this.childQuestion = childQuestion;
	}

	public Question getChildQuestion() {
		return childQuestion;
	}

	public void setCategoryDomains(List<CategoryDomain> categoryDomains) {
		this.categoryDomains = categoryDomains;
	}

	public List<CategoryDomain> getCategoryDomains() {
		return categoryDomains;
	}

	public int getParentCategoryDomainId() {
		return parentCategoryDomainId;
	}

	public void setParentCategoryDomainId(int parentCategoryDomainId) {
		this.parentCategoryDomainId = parentCategoryDomainId;
	}

	public int getChildCategoryDomainId() {
		return childCategoryDomainId;
	}

	public void setChildCategoryDomainId(int childCategoryDomainId) {
		this.childCategoryDomainId = childCategoryDomainId;
	}

}
