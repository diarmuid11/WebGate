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

import java.util.List;


/**
 * The persistent class for the question_types database table.
 * 
 */
@Entity
@Table(name="question_types",schema="surveys")
@NamedQueries(
	@NamedQuery(name="questionType.findAll", query="Select qt from QuestionType qt")
)

public class QuestionType implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	@Column(name="creation_view_component")
	private String creationViewComponent;

	@Column(name="display_view_component")
	private String displayViewComponent;

	@Column(name="fill_view_adapter")
	private String fillViewAdapter;
	
	@Column(name="creation_view_adapter")
	private String creationViewAdapter;

	@Column(name="expression_class")
	private String expressionClass;

	private String name;

	@Column(name="validations_view_component")
	private String validationsViewComponent;

	//bi-directional many-to-one association to QuestionType
    @ManyToOne
	@JoinColumn(name="question_type_id")
	private QuestionType questionType;

	//bi-directional many-to-one association to QuestionType
	@OneToMany(mappedBy="questionType")
	private List<QuestionType> questionTypes;

	//bi-directional many-to-one association to ValidationType
	@ManyToMany (fetch = FetchType.EAGER)
	@JoinTable(name="question_types_validation_types", schema="surveys",
			joinColumns=@JoinColumn(name="question_type_id"),
			inverseJoinColumns=@JoinColumn(name="validation_type_id"))
	private List<ValidationType> validationTypes;

    public QuestionType() {
    }

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCreationViewComponent() {
		return this.creationViewComponent;
	}

	public void setCreationViewComponent(String creationViewComponent) {
		this.creationViewComponent = creationViewComponent;
	}

	public String getDisplayViewComponent() {
		return this.displayViewComponent;
	}

	public void setDisplayViewComponent(String displayViewComponent) {
		this.displayViewComponent = displayViewComponent;
	}

	public String getFillViewAdapter() {
		return fillViewAdapter;
	}

	public void setFillViewAdapter(String fillViewAdapter) {
		this.fillViewAdapter = fillViewAdapter;
	}

	public String getCreationViewAdapter() {
		return creationViewAdapter;
	}

	public void setCreationViewAdapter(String creationViewAdapter) {
		this.creationViewAdapter = creationViewAdapter;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValidationsViewComponent() {
		return this.validationsViewComponent;
	}

	public void setValidationsViewComponent(String validationsViewComponent) {
		this.validationsViewComponent = validationsViewComponent;
	}

	public String getExpressionClass() {
		return expressionClass;
	}

	public void setExpressionClass(String expressionClass) {
		this.expressionClass = expressionClass;
	}

	public QuestionType getQuestionType() {
		return this.questionType;
	}

	public void setQuestionType(QuestionType questionType) {
		this.questionType = questionType;
	}
	
	public List<QuestionType> getQuestionTypes() {
		return this.questionTypes;
	}

	public void setQuestionTypes(List<QuestionType> questionTypes) {
		this.questionTypes = questionTypes;
	}
	
	public List<ValidationType> getValidationTypes() {
		return this.validationTypes;
	}

	public void setValidationTypes(List<ValidationType> validationTypes) {
		this.validationTypes = validationTypes;
	}

	@Override
	public int hashCode() {
		if (id==null){
			return 0;
		}
		return id.intValue();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		QuestionType other = (QuestionType) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
	
}