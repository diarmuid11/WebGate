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
package org.correlibre.qop.fillviewadapters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.correlibre.qop.domain.CategoryOption;
import org.correlibre.qop.domain.ConfigProperty;
import org.correlibre.qop.domain.Question;
import org.correlibre.qop.services.QopException;
import org.correlibre.qop.util.BeanUtil;
import org.correlibre.qop.util.SelectItemStringLabelComparator;

import com.icesoft.faces.component.selectinputtext.SelectInputText;
import com.icesoft.faces.component.selectinputtext.TextChangeEvent;

public class RelatedSelectOneMenuQuestion extends BaseFillViewAdapter {

	private Question parentQuestion;

	private Question childQuestion;
	
	private Question otherQuestion;

	private String selectedParentItem;

	private String selectedChildItem;

	private List<SelectItem> parentOptions;
	private List<SelectItem> childOptions;
	private List<SelectItem> allChildOptions;
	
	private String otherText;

	public void init() {

		try {
			parentQuestion = question.getQuestions().get(0);
			childQuestion = question.getQuestions().get(1);
			otherQuestion = srvSurveysEngine.getSubQuestions(question, 6).get(0);

			ConfigProperty showValueOnLabelConfig = srvSurveysEngine
					.getConfigProperty("showValueAndLabel", null, this.question);

			boolean showValueOnLabel = false;

			if (showValueOnLabelConfig != null
					&& Boolean.parseBoolean(showValueOnLabelConfig.getValue())) {
				showValueOnLabel = true;
			}

			System.out
					.println("[RelatedSelectOneMenuQuestion] selectedParentItem: "
							+ selectedParentItem);
			System.out
					.println("[RelatedSelectOneMenuQuestion] selectedChildItem: "
							+ selectedChildItem);

			parentOptions = BeanUtil.getInstance()
					.getCategoryOptionsAsSelectItems(
							parentQuestion.getCategoryDomain()
									.getCategoryOptions(), showValueOnLabel);

			Integer idCo = null;
			for (SelectItem opt : parentOptions) {
				if (opt.getLabel().equalsIgnoreCase(selectedParentItem)) {
					idCo = (Integer) opt.getValue();
				}
			}
			CategoryOption co;
			if (idCo == null)
				co = srvSurveysEngine
						.getCategoryOption((Integer) (parentOptions.get(0)
								.getValue()));
			else
				co = srvSurveysEngine.getCategoryOption(idCo);

			allChildOptions = BeanUtil.getInstance()
					.getCategoryOptionsAsSelectItemsString(
							srvSurveysEngine.getCategoryOptions(co),
							showValueOnLabel);
		} catch (QopException e) {
			e.printStackTrace();
			BeanUtil.getInstance().addMessage(e.getMessage());
		}
	}

	/*
	public void parentValueChanged(TextChangeEvent event) {

		CategoryOption co = null;

		int coId = new Integer((String) event.getNewValue());

		System.out
				.println("####################### [RelatedSelectOneMenuQuestion] coId: "
						+ coId);

		try {
			co = srvSurveysEngine.getCategoryOption(coId);

			ConfigProperty showValueOnLabelConfig = srvSurveysEngine
					.getConfigProperty("showValueAndLabel",
							this.question.getSurveyStructure(), this.question);

			boolean showValueOnLabel = false;

			if (showValueOnLabelConfig != null
					&& Boolean.parseBoolean(showValueOnLabelConfig.getValue())) {
				showValueOnLabel = true;
			}

			allChildOptions = BeanUtil.getInstance()
					.getCategoryOptionsAsSelectItemsString(
							srvSurveysEngine.getCategoryOptions(co),
							showValueOnLabel);
			
			childOptions = allChildOptions;
			
			selectedChildItem = "";

		} catch (QopException e) {
			e.printStackTrace();
			BeanUtil.getInstance().addMessage(e.getMessage());
		}

	}
	*/

	public void childValueChanged(ValueChangeEvent event) {
		
		System.out
		.println("####################### [RelatedSelectOneMenuQuestion] Child event.getNewValue(): "+ event.getNewValue());
		
		
		if(event.getNewValue() != null){
			selectedChildItem = (String) event.getNewValue();
		}else{
			return;
		}
		
	}
	
	public void parentValueChanged(ValueChangeEvent event) {

		CategoryOption co = null;
		
		System.out
		.println("####################### [RelatedSelectOneMenuQuestion] event.getNewValue(): "+ event.getNewValue());

		int coId;
		
		if(event.getNewValue() != null){
			coId = new Integer((String) event.getNewValue());
			selectedParentItem = (String) event.getNewValue();
		}else{
			allChildOptions = new ArrayList<SelectItem>();
			childOptions = allChildOptions;
			return;
		}

		System.out
				.println("####################### [RelatedSelectOneMenuQuestion] coId: "
						+ coId);

		try {
			co = srvSurveysEngine.getCategoryOption(coId);

			ConfigProperty showValueOnLabelConfig = srvSurveysEngine
					.getConfigProperty("showValueAndLabel", null, this.question);

			boolean showValueOnLabel = false;

			if (showValueOnLabelConfig != null
					&& Boolean.parseBoolean(showValueOnLabelConfig.getValue())) {
				showValueOnLabel = true;
			}

			allChildOptions = BeanUtil.getInstance()
					.getCategoryOptionsAsSelectItemsString(
							srvSurveysEngine.getCategoryOptions(co),
							showValueOnLabel);
			
			childOptions = allChildOptions;
			
			selectedChildItem = "";
			
			System.out.println("ChildOptions OK");

		} catch (QopException e) {
			e.printStackTrace();
			BeanUtil.getInstance().addMessage(e.getMessage());
		}

	}

	protected String getFindOptionMatch(String word, List<SelectItem> options) {
		if (options != null) {
			SelectItem s;
			for (int i = 0, max = options.size(); i < max; i++) {
				s = (SelectItem) options.get(i);
				if (s.getLabel().compareToIgnoreCase(word) == 0) {
					return (String) s.getValue();
				}
			}
		}
		return null;
	}

	protected List<SelectItem> generateOptionMatches(String searchWord,
			List<SelectItem> allOptions) {

		List<SelectItem> matchList = new ArrayList<SelectItem>();

		SelectItem searchItem = new SelectItem("", searchWord);

		int insert = Collections.binarySearch(allOptions, searchItem,
				new SelectItemStringLabelComparator());

		if (insert < 0) {
			insert = Math.abs(insert) - 1;
		} else {

			if (insert != allOptions.size()
					&& new SelectItemStringLabelComparator().compare(
							searchItem, allOptions.get(insert)) == 0) {
				while (insert > 0
						&& new SelectItemStringLabelComparator().compare(
								searchItem, allOptions.get(insert - 1)) == 0) {
					insert = insert - 1;
				}
			}
		}

		for (int i = 0; i < 10; i++) {

			if ((insert + i) >= allOptions.size() || i >= 10) {
				break;
			}
			matchList.add(allOptions.get(insert + i));
		}

		return matchList;
	}

	@Override
	public Map<Question, String> getAnswer() {
		Map<Question, String> answers = new HashMap<Question, String>();
		answers.put(parentQuestion, selectedParentItem);
		
		if(selectedChildItem != null && selectedChildItem.length() >0)
			otherText = "";
		
		answers.put(otherQuestion, otherText);
		
		if(otherText != null && otherText.length() > 0)
			selectedChildItem = "";
		
		answers.put(childQuestion, selectedChildItem);
		return answers;
	}

	@Override
	public void setAnswer(Map<Question, String> answers) {
		this.selectedParentItem = answers.get(parentQuestion);
		this.selectedChildItem = answers.get(childQuestion);
		this.otherText = answers.get(otherQuestion);

		ConfigProperty showValueOnLabelConfig;
		try {
			showValueOnLabelConfig = srvSurveysEngine.getConfigProperty(
					"showValueAndLabel", null, this.question);

			boolean showValueOnLabel = false;

			if (showValueOnLabelConfig != null
					&& Boolean.parseBoolean(showValueOnLabelConfig.getValue())) {
				showValueOnLabel = true;
			}

			System.out
					.println("[RelatedSelectOneMenuQuestion] selectedParentItem: "
							+ selectedParentItem);
			System.out
					.println("[RelatedSelectOneMenuQuestion] selectedChildItem: "
							+ selectedChildItem);

			parentOptions = BeanUtil.getInstance()
					.getCategoryOptionsAsSelectItems(
							parentQuestion.getCategoryDomain()
									.getCategoryOptions(), showValueOnLabel);

			Integer idCo = null;
			for (SelectItem opt : parentOptions) {
				if (opt.getValue().toString().equalsIgnoreCase(selectedParentItem)) {
					idCo = (Integer) opt.getValue();
					break;
				}
			}
			
			CategoryOption co;
			
			System.out.println("idCo: "+idCo);
			
			if (idCo == null)
				co = srvSurveysEngine
						.getCategoryOption((Integer) (parentOptions.get(0)
								.getValue()));
			else
				co = srvSurveysEngine.getCategoryOption(idCo);
			
			System.out.println("co: "+co);

			allChildOptions = BeanUtil.getInstance()
					.getCategoryOptionsAsSelectItemsString(
							srvSurveysEngine.getCategoryOptions(co),
							showValueOnLabel);
			
			childOptions = allChildOptions;

		} catch (QopException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public Question getChildQuestion() {
		return childQuestion;
	}

	public void setChildQuestion(Question childQuestion) {
		this.childQuestion = childQuestion;
	}

	public String getSelectedParentItem() {
		return selectedParentItem;
	}

	public void setSelectedParentItem(String selectedParentItem) {
		this.selectedParentItem = selectedParentItem;
	}

	public String getSelectedChildItem() {
		return selectedChildItem;
	}

	public void setSelectedChildItem(String selectedChildItem) {
		this.selectedChildItem = selectedChildItem;
	}

	public List<SelectItem> getParentOptions() {
		return parentOptions;
	}

	public void setParentOptions(List<SelectItem> parentOptions) {
		this.parentOptions = parentOptions;
	}

	public List<SelectItem> getChildOptions() {
		return childOptions;
	}

	public void setChildOptions(List<SelectItem> childOptions) {
		this.childOptions = childOptions;
	}

	public void setParentQuestion(Question parentQuestion) {
		this.parentQuestion = parentQuestion;
	}

	public Question getParentQuestion() {
		return parentQuestion;
	}

	public void setOtherQuestion(Question otherQuestion) {
		this.otherQuestion = otherQuestion;
	}

	public Question getOtherQuestion() {
		return otherQuestion;
	}

	public String getOtherText() {
		return otherText;
	}

	public void setOtherText(String otherText) {
		this.otherText = otherText;
	}

}
