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

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.correlibre.qop.domain.Question;

public class CurrencyQuestion extends BaseFillViewAdapter {

	private String textAnswer;
	
	private NumberFormat currencyFormatter;
	private NumberFormat simpleFormatter;
	
	public static void main(String args []){
		new CurrencyQuestion();
	}
	
	public CurrencyQuestion() {
		super();
		
		DecimalFormatSymbols unusualSymbols = new DecimalFormatSymbols();
		unusualSymbols.setDecimalSeparator('.');
		unusualSymbols.setGroupingSeparator(',');
		
		currencyFormatter = new DecimalFormat("###,###,###", unusualSymbols);
		simpleFormatter = new DecimalFormat("###.#", unusualSymbols);
		
		System.out.println(""+currencyFormatter.format(new Double("40000000.0")));
		
	}
	
	@Override
	public void setAnswer(Map<Question, String> answers) {
		String strAnswer = answers.get(question);
		if(strAnswer != null && strAnswer.length() > 0)
			textAnswer = currencyFormatter.format(new Double(strAnswer)) ;
		else
			textAnswer = "";
	}

	@Override
	public Map<Question, String> getAnswer() {
		Map<Question, String> answers = new HashMap<Question, String> ();
		double num = 0;
		try {
			if(textAnswer != null && textAnswer.length() > 0)
				num = currencyFormatter.parse(textAnswer).doubleValue();
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		answers.put(question, simpleFormatter.format(num));
		return answers;
	}

	public String getTextAnswer() {
		return textAnswer;
	}

	public void setTextAnswer(String textAnswer) {
		this.textAnswer = textAnswer;
	}


}