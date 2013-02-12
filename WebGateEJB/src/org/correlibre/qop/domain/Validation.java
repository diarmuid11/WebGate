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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the validations database table.
 * 
 */
@Entity
@Table(name="validations",schema="surveys")
@NamedQueries(
	@NamedQuery(name="Validation.findByQuestionKeyAndValidationKey",
		query="Select v from Validation v where v.question.key=:questionKey and v.key=:validationKey")
	)
public class Validation extends TimeAwareEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	private String message;

	private String rule;
	
	private String key;

	//bi-directional many-to-one association to Question
    @ManyToOne
	private Question question;

	//uni-directional many-to-one association to ValidationType
    @ManyToOne
	@JoinColumn(name="validation_type_id")
	private ValidationType validationType;

    public Validation() {
    }

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
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
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	
	public ValidationType getValidationType() {
		return this.validationType;
	}

	public void setValidationType(ValidationType validationType) {
		this.validationType = validationType;
	}

	@Override
	public String toString() {
		return "Validation [id=" + id + ", message=" + message + ", rule="
				+ rule + ", key=" + key + ", validationType=" + validationType.getName()
				+ "]";
	}
	
	
	
}