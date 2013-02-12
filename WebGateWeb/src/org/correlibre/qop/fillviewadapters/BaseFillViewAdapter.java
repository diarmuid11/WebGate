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

import java.util.Map;

import org.correlibre.qop.domain.Question;
import org.correlibre.qop.services.QopException;
import org.correlibre.qop.services.SrvSurveysEngine;

public abstract class BaseFillViewAdapter implements FillViewAdapter{

	protected Question question;
	protected SrvSurveysEngine srvSurveysEngine;
	private Map<Question, FillViewAdapter> subFillViewAdapters;

	public Map<Question, FillViewAdapter> getSubFillViewAdapters() {
		return subFillViewAdapters;
	}

	public BaseFillViewAdapter() {
		super();
	}

	@Override
	public void init() throws QopException {
	}
	
	@Override
	public void setQuestion(Question q) {
		question = q;		
	}

	@Override
	public void setSrvSurveysEngine(SrvSurveysEngine srvSurveysEngine) {
		this.srvSurveysEngine=srvSurveysEngine;
	}

	@Override
	public void setSubFillViewAdapters(
			Map<Question, FillViewAdapter> fillViewAdapters) {
		this.subFillViewAdapters=fillViewAdapters;
	}
	
	


}