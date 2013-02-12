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
import java.util.List;

import javax.persistence.*;


/**
 * The persistent class for the validation_types database table.
 * 
 */
@Entity
@Table(name="validation_types",schema="surveys")
public class ValidationType implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	private String message;

	private String name;

	private String validator;
	
	@Column(name="creation_view_component")
	private String creationViewComponent;
	
	@Column(name="creation_view_adapter")
	private String creationViewAdapter;
	
	@Column(name="key_prefix")
	private String keyPrefix;
	
	@ManyToMany
	@JoinTable(name="question_types_validation_types", schema="surveys",
			joinColumns=@JoinColumn(name="validation_type_id"),
			inverseJoinColumns=@JoinColumn(name="question_type_id"))
	private List<QuestionType> questionTypes;

    public ValidationType() {
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

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValidator() {
		return this.validator;
	}

	public void setValidator(String validator) {
		this.validator = validator;
	}

	public String getCreationViewComponent() {
		return creationViewComponent;
	}

	public void setCreationViewComponent(String creationViewComponent) {
		this.creationViewComponent = creationViewComponent;
	}

	public String getCreationViewAdapter() {
		return creationViewAdapter;
	}

	public void setCreationViewAdapter(String creationViewAdapter) {
		this.creationViewAdapter = creationViewAdapter;
	}
	
	

	public String getKeyPrefix() {
		return keyPrefix;
	}

	public void setKeyPrefix(String keyPrefix) {
		this.keyPrefix = keyPrefix;
	}

	@Override
	public int hashCode() {
		if (id!=null){
			return id.intValue();
		}else{
			return 0;
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ValidationType other = (ValidationType) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public void setQuestionTypes(List<QuestionType> questionTypes) {
		this.questionTypes = questionTypes;
	}

	public List<QuestionType> getQuestionTypes() {
		return questionTypes;
	}
	
	
	
	
	
	
}