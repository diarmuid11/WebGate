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

import static javax.persistence.FetchType.LAZY;

import java.io.Serializable;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;


/**
 * The persistent class for the survey_structures database table.
 * 
 */
@Entity
@Table(name="survey_structures",schema="surveys")
@NamedQueries({
	@NamedQuery(name="SurveyStructure.findAll",query="Select ss from SurveyStructure ss order by ss.name ASC"),
	@NamedQuery(name="SurveyStructure.findAllParents",query="Select ss from SurveyStructure ss where ss.parentSurveyStructure is null order by ss.name"),
	@NamedQuery(name="SurveyStructure.findByCreatorUser",query="Select ss from SurveyStructure ss where :userLogin member of ss.creatorUsers"),
	@NamedQuery(name="SurveyStructure.findByState",query="Select ss from SurveyStructure ss where ss.state=:state and ss.parentSurveyStructure is null"),
	@NamedQuery(name="SurveyStructure.findAllByState",query="Select ss from SurveyStructure ss where ss.state=:state"),
	@NamedQuery(name="SurveyStructure.findBy2State",query="Select ss from SurveyStructure ss where (ss.state=:state1 OR ss.state=:state2)"),
	@NamedQuery(name="SurveyStructure.findByCreatorUserAndState",query="Select ss from SurveyStructure ss where :userLogin member of ss.creatorUsers and ss.state=:state and ss.parentSurveyStructure is null")
})
public class SurveyStructure extends TimeAwareEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	private String name;
	
	
	@Enumerated(EnumType.ORDINAL)
	@Column(name="survey_structure_state_id")
	private SurveyStructureState state;

	//bi-directional many-to-one association to Question
	@OneToMany(mappedBy="surveyStructure",cascade=CascadeType.ALL)
	private List<Question> questions;
	
	@ElementCollection
	@CollectionTable(name="principals_survey_structures",schema="surveys",
	       joinColumns=@JoinColumn(name="survey_structure_id")
	)
	@Column(name="principal_name")
	private List<String> creatorUsers;
	
	@ManyToOne(fetch = LAZY)
	@JoinColumn(name="parent_structuresurvey_id")
	private SurveyStructure parentSurveyStructure;
    
    @OneToMany(mappedBy="parentSurveyStructure", cascade = CascadeType.ALL)
	private List<SurveyStructure> childrenSurveyStructures;
    

    public SurveyStructure() {
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
	
	public SurveyStructureState getState() {
		return state;
	}

	public void setState(SurveyStructureState state) {
		this.state = state;
	}

	public List<String> getCreatorUsers() {
		return creatorUsers;
	}

	public void setCreatorUsers(List<String> creatorUsers) {
		this.creatorUsers = creatorUsers;
	}

	public boolean equals(final Object o) {
        if (o instanceof SurveyStructure) {
            SurveyStructure ss = (SurveyStructure) o;
            return ss.getId().intValue() == this.id.intValue();
        }
        return false;
    }
	
	public Question getIdentifyQuestion(){
		
		for (Question q : questions) {
			
			if(q.getQuestionType().getId().intValue() == 14 || q.getQuestionType().getId().intValue() == 15)
				return q;
			
		}
		
		return null;
	}
	
	public List<Question> getMainQuestions(){
		
		List<Question> mainQuestionList = new ArrayList<Question>();
		
		for (Question question : this.questions) {
			if(question.getQuestion() == null)
				mainQuestionList.add(question);
		}
		
		return mainQuestionList;
		
	}
 
    public int hashCode() {
        return this.getId().intValue();
    }

	public void setParentSurveyStructure(SurveyStructure parentSurveyStructure) {
		this.parentSurveyStructure = parentSurveyStructure;
	}

	public SurveyStructure getParentSurveyStructure() {
		return parentSurveyStructure;
	}

	public void setChildrenSurveyStructures(
			List<SurveyStructure> childrenSurveyStructures) {
		this.childrenSurveyStructures = childrenSurveyStructures;
	}

	public List<SurveyStructure> getChildrenSurveyStructures() {
		return childrenSurveyStructures;
	}

	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}

	public List<Question> getQuestions() {
		return questions;
	}
	
	public String toString(){
		return name;		
	}
}