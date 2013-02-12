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
 * Entity implementation class for Entity: CategoryOption
 *
 */
@Entity
@Table(name="category_options",schema="surveys")
@NamedQueries({ 
	@NamedQuery(name = "CategoryOption.findAll", query = "Select co from CategoryOption co"),
	@NamedQuery(name = "CategoryOption.findChildOptions", query = "Select co from CategoryOption co where co.parentCategoryOption = :parentCategoryOption")
	})
public class CategoryOption implements Serializable {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	private String value;
	private String label;
	private boolean active;
	
	@ManyToOne
	@JoinColumn(name="category_domain_id")
	private CategoryDomain categoryDomain;
	
	@ManyToOne
	@JoinColumn(name="category_option_id")
	private CategoryOption parentCategoryOption;
	
	@OneToMany(mappedBy = "parentCategoryOption")
	private List<CategoryOption> categoryOptions;

	private static final long serialVersionUID = 1L;

	public CategoryOption() {
		super();
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
	public String getLabel() {
		return this.label;
	}

	public void setLabel(String label) {
		this.label = label;
	}   
	public boolean getActive() {
		return this.active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
	
    public CategoryDomain getCategoryDomain() {
		return this.categoryDomain;
	}

	public void setCategoryDomain(CategoryDomain categoryDomain) {
		this.categoryDomain = categoryDomain;
	}   
	
	public List<CategoryOption> getCategoryOptions() {
		return this.categoryOptions;
	}

	public void setCategoryOptions(List<CategoryOption> categoryOptions) {
		this.categoryOptions = categoryOptions;
	}
	
	 public CategoryOption getParentCategoryOption() {
			return this.parentCategoryOption;
		}

		public void setParentCategoryOption(CategoryOption parentCategoryOption) {
			this.parentCategoryOption = parentCategoryOption;
		} 
   
}
