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

import javax.faces.model.SelectItem;

import org.correlibre.qop.domain.ConfigProperty;
import org.correlibre.qop.domain.Question;
import org.correlibre.qop.services.QopException;
import org.correlibre.qop.util.BeanUtil;
import org.correlibre.qop.util.SelectItemStringLabelComparator;

import com.icesoft.faces.component.selectinputtext.SelectInputText;
import com.icesoft.faces.component.selectinputtext.TextChangeEvent;

public class SelectOneMenuWithPredefinedDomainQuestion extends BaseFillViewAdapter {

	private String selectedItem;
	
	private List<SelectItem> allOptions;
	
	private List<SelectItem> options;
	
	public void init(){
		
		ConfigProperty showValueOnLabelConfig = null;
		
		try {
			showValueOnLabelConfig = srvSurveysEngine
			.getConfigProperty("showValueAndLabel", this.question.getSurveyStructure(),
					this.question);
		} catch (QopException e) {
			e.printStackTrace();
		}
		
		boolean showValueOnLabel = false;

		System.out.println("[SelectOne] showValueOnLabelConfig: "+showValueOnLabelConfig);
		
		if (showValueOnLabelConfig != null
				&& Boolean.parseBoolean(showValueOnLabelConfig.getValue())) {
			
			showValueOnLabel = true;
		}
		
		options = BeanUtil.getInstance().getCategoryOptionsAsSelectItems(question.getCategoryDomain().getCategoryOptions(), showValueOnLabel);
		allOptions = BeanUtil.getInstance().getCategoryOptionsAsSelectItems(question.getCategoryDomain().getCategoryOptions(), showValueOnLabel);
	}
	
	public void changeSelection(TextChangeEvent e){
		
		SelectInputText autoComplete = (SelectInputText) e.getComponent();
		String newWord = (String) e.getNewValue();
		
		options = generateOptionMatches(newWord, allOptions);
		
		if (autoComplete.getSelectedItem() != null) {
			selectedItem = (String) autoComplete.getSelectedItem().getValue();
        }else{
            String tmp = getFindOptionMatch(newWord, options);
            if (tmp != null){
                selectedItem = tmp;
            }
        }
	}
	
	protected String getFindOptionMatch(String word, List<SelectItem> options) {
        if (options != null) {
            SelectItem s;
            for(int i = 0, max = options.size(); i < max; i++){
                s = (SelectItem)options.get(i);
                if (s.getLabel().compareToIgnoreCase(word) == 0) {
                    return (String) s.getValue();
                }
            }
        }
        return null;
    }
	
	protected List<SelectItem> generateOptionMatches(String searchWord, List<SelectItem> allOptions){
		
		List<SelectItem> matchList = new ArrayList<SelectItem>();
		
		SelectItem searchItem = new SelectItem("", searchWord);
		 
        int insert = Collections.binarySearch(allOptions, searchItem, new SelectItemStringLabelComparator());
		
        if (insert < 0) {
            insert = Math.abs(insert) - 1;
        }
        else {
            
            if(insert != allOptions.size() && new SelectItemStringLabelComparator().compare(searchItem, allOptions.get(insert)) == 0) {
                while(insert > 0 && new SelectItemStringLabelComparator().compare(searchItem, allOptions.get(insert-1)) == 0) {
                    insert = insert - 1;
                }
            }
        }
        
        for (int i = 0; i < 10; i++) {
            
            if ((insert + i) >= allOptions.size() ||
                    i >= 10) {
                break;
            }
            matchList.add(allOptions.get(insert + i));
        }
        
        return matchList;
	}
	
	@Override
	public Map<Question, String> getAnswer() {
		Map<Question, String> answers = new HashMap<Question, String> ();
		answers.put(question, selectedItem);
		return answers;
	}

	@Override
	public void setAnswer(Map<Question, String> answers) {
		this.selectedItem=answers.get(question);

	}

	public String getSelectedItem() {
		return selectedItem;
	}

	public void setSelectedItem(String selectedItem) {
		this.selectedItem = selectedItem;
	}

	public List<SelectItem> getOptions() {
		return options;
	}

	public void setOptions(List<SelectItem> options) {
		this.options = options;
	}

}
