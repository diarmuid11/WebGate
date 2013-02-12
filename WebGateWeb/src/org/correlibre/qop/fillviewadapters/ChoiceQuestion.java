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

import java.util.List;

import javax.faces.model.SelectItem;

import org.correlibre.qop.domain.ConfigProperty;
import org.correlibre.qop.domain.Question;
import org.correlibre.qop.services.QopException;
import org.correlibre.qop.util.BeanUtil;

public abstract class ChoiceQuestion extends BaseFillViewAdapter {

	protected List<SelectItem> options;
	protected List<Question> subQuestions;

	public ChoiceQuestion() {
		super();
	}

	public void init() {
		
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
		
		subQuestions = srvSurveysEngine.getSubQuestions(question, 7);
		options = BeanUtil.getInstance().getQuestionsAsSelectItems(subQuestions, showValueOnLabel);
	}

	public List<SelectItem> getOptions() {
		return options;
	}

}