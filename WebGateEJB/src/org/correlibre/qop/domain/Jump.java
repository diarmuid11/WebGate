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
package org.correlibre.qop.domain;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the jumps database table.
 * 
 */
@Entity
@Table(name="jumps",schema="surveys")
public class Jump implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	private Integer ordinal;

	private String rule;

	//bi-directional many-to-one association to Question
    @ManyToOne
    @JoinColumn(name="question_id")
	private Question question;

	//uni-directional many-to-one association to Question
    @ManyToOne
	@JoinColumn(name="next")
	private Question nextQuestion;

    public Jump() {
    }

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getOrdinal() {
		return this.ordinal;
	}

	public void setOrdinal(Integer ordinal) {
		this.ordinal = ordinal;
	}

	public String getRule() {
		return this.rule;
	}

	public void setRule(String rule) {
		this.rule = rule;
	}

	public Question getQuestion() {
		return this.question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}
	
	public Question getNextQuestion() {
		return this.nextQuestion;
	}

	public void setNextQuestion(Question nextQuestion) {
		this.nextQuestion = nextQuestion;
	}
	
}