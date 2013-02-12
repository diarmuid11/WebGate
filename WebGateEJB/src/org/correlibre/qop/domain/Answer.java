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

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the answers database table.
 * 
 */
@Entity
@Table(name = "answers", schema = "surveys")
@NamedQueries({
		@NamedQuery(name = "Answer.findBySurveyAndQuestion", query = "Select a from Answer a where a.survey=:survey and a.question=:question"),
		@NamedQuery(name = "Answer.findBySurvey", query = "Select a from Answer a where a.survey=:survey order by a.creationTime ASC"),
		@NamedQuery(name = "Answer.findBySurveyAndQuestionKey", query = "Select a from Answer a where a.survey=:survey and a.question.key=:key"), })
public class Answer extends TimeAwareEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String value;

	// uni-directional many-to-one association to Question
	@ManyToOne(fetch = FetchType.EAGER)
	private Question question;

	// bi-directional many-to-one association to Survey
	@ManyToOne
	private Survey survey;

	public Answer() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Question getQuestion() {
		return this.question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}

	public Survey getSurvey() {
		return this.survey;
	}

	public void setSurvey(Survey survey) {
		this.survey = survey;
	}

	public String toString() {
		return "id: " + this.id + ", value: " + this.value;
	}

}