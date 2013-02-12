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
import java.lang.Integer;
import java.lang.String;
import java.util.List;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: CategoryDomain
 * 
 */
@Entity
@Table(name = "category_domains", schema = "surveys")
@NamedQueries({ 
	@NamedQuery(name = "CategoryDomain.findAll", query = "Select cd from CategoryDomain cd")
	})
public class CategoryDomain implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String name;
	
	@OneToMany(mappedBy = "categoryDomain")
	private List<CategoryOption> categoryOptions;
	
	@OneToMany(mappedBy = "categoryDomain")
	private List<Question> questions;
	
	private static final long serialVersionUID = 1L;

	public CategoryDomain() {
		super();
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}



	public List<CategoryOption> getCategoryOptions() {
		return this.categoryOptions;
	}

	public void setCategoryOptions(List<CategoryOption> categoryOptions) {
		this.categoryOptions = categoryOptions;
	}

	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}

	public List<Question> getQuestions() {
		return questions;
	}

}
