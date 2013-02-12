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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.correlibre.qop.domain.Question;

public class CheckButtonQuestion extends ChoiceQuestion {
	
	private List<String> selectedItems;
	
	public List<String> getSelectedItems() {
		return selectedItems;
	}

	public void setSelectedItems(List<String> selectedItems) {
		this.selectedItems = selectedItems;
	}


	
	@Override
	public Map<Question, String> getAnswer() {		
		Map<Question, String> answers = new HashMap<Question, String> ();
		for (Question q: subQuestions){
			if (selectedItems.contains(q.getId().toString())){
				answers.put(q, "true");
			}else{
				answers.put(q, "false");
			}
		}
		return answers;
	}

	@Override
	public void setAnswer(Map<Question, String> answers) {
		selectedItems= new ArrayList<String>();
		for (Question q: subQuestions){
			String answer = answers.get(q);
			if (answer != null){
				if("true".equalsIgnoreCase(answer))
					selectedItems.add(q.getId().toString());
			}
		}
		
	}
}
