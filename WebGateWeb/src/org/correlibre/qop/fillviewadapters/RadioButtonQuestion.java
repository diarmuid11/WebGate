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

import java.util.HashMap;
import java.util.Map;


import org.correlibre.qop.domain.Question;


public class RadioButtonQuestion extends ChoiceQuestion {
	
	protected String selectedItem;
	
	public String getSelectedItem() {
		return selectedItem;
	}

	public void setSelectedItem(String selectedItem) {
		this.selectedItem = selectedItem;
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
}
