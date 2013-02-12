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

import java.util.Date;
import java.util.List;
import static javax.persistence.FetchType.EAGER;


/**
 * The persistent class for the surveys database table.
 * 
 */
@Entity
@Table(name="surveys",schema="surveys")
@NamedQueries({
	@NamedQuery(name="Survey.findBySurveyStructure",query="Select s from Survey s where s.surveyStructure=:surveyStructure"),
	@NamedQuery(name="Survey.findBySurveyStructureAndUser",query="Select s from Survey s where s.surveyStructure=:surveyStructure and s.userLogin=:userLogin"),
	@NamedQuery(name="Survey.findByUserAndState",query="Select s from Survey s where s.userLogin=:userLogin and s.surveyState IN :surveyState"),
	@NamedQuery(name="Survey.NumberOfSurveysByState",query="select COUNT(s) from Survey s where s.surveyState =:surveyState and s.userLogin=:userLogin"),
	@NamedQuery(name="Survey.groupSurvey", query="Select s.surveyStructure, s.surveyState,count(s) from Survey s where s.userLogin=:userLogin group by s.surveyState, s.surveyStructure"),
	@NamedQuery(name="Survey.findBySurveyStructureStateAndUser", query="Select s from Survey s where s.surveyStructure=:surveyStructure and s.surveyState IN :surveyState and s.userLogin=:userLogin"),
	@NamedQuery(name="Survey.findChildrenBySurveyStructureAndState", query="Select s from Survey s where s.surveyStructure=:surveyStructure and s.surveyState IN :surveyState and s.parentSurvey = :parentSurvey"),
	@NamedQuery(name="Survey.findInstancesCountByStructure", query="Select count(s.surveyState), s.surveyStructure, s.surveyState from Survey s where s.userLogin =:userLogin Group By s.surveyStructure, s.surveyState"),
	@NamedQuery(name="Survey.findByUser", query="Select s from Survey s where s.userLogin=:userLogin"),
	@NamedQuery(name="Survey.findBySurveyStructureAndState", query="Select s from Survey s where s.surveyStructure=:surveyStructure and s.surveyState IN :surveyState"),
	@NamedQuery(name="Survey.findParentBySurveyStructureAndState",query="Select s from Survey s where s.surveyStructure=:surveyStructure and s.parentSurvey is null and s.surveyState IN :surveyState  and s.surveyState != org.correlibre.qop.domain.SurveyState.DELETED"),
	@NamedQuery(name="Survey.findParentBySurveyStructureAndState-idQuestion",query="Select s from Survey s, Answer a where s.surveyStructure=:surveyStructure and s.parentSurvey is null and s.surveyState IN :surveyState and a.survey = s and a.question = :question and a.value like :searchText  and s.surveyState != org.correlibre.qop.domain.SurveyState.DELETED"),
	@NamedQuery(name="Survey.findParentByUser", query="Select s from Survey s where s.userLogin=:userLogin and s.parentSurvey is null"),
	@NamedQuery(name="Survey.findParentByUserAndState",query="Select s from Survey s where s.userLogin=:userLogin and s.surveyState IN :surveyState and s.parentSurvey is null"),
	@NamedQuery(name="Survey.findParentBySurveyStructureUserAndState",query="Select s from Survey s where s.surveyStructure=:surveyStructure and s.userLogin=:userLogin and s.surveyState IN :surveyState and s.parentSurvey is null  and s.surveyState != org.correlibre.qop.domain.SurveyState.DELETED"),
	@NamedQuery(name="Survey.findParentByState", query="Select s from Survey s where s.parentSurvey is null and s.surveyState IN :surveyState"),
	@NamedQuery(name="Survey.findParentBySurveyStructureUserAndState-idQuestion",query="Select s from Survey s, Answer a where s.surveyStructure=:surveyStructure and s.userLogin=:userLogin and s.surveyState IN :surveyState and s.parentSurvey is null and a.survey = s and a.question = :question and a.value like :searchText and s.surveyState != org.correlibre.qop.domain.SurveyState.DELETED"),
})
public class Survey extends TimeAwareEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	@Column(name="user_login")
	private String userLogin;
	
	@Column(name="creation_user")
	private String creationUser;	

	@Column(name="reference_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date referenceTime;	
	
	//bi-directional many-to-one association to Answer
	@OneToMany(mappedBy="survey")
	@OrderBy("creationTime ASC")
	private List<Answer> answers;

	//uni-directional many-to-one association to SurveyStructure
    @ManyToOne
	@JoinColumn(name="survey_structure_id")
	private SurveyStructure surveyStructure;

	//uni-directional many-to-one association to SurveyStructure
    //@ManyToOne
	//@JoinColumn(name="survey_state_id")
	//private SurveyState surveyState;
    @Enumerated(EnumType.ORDINAL)
	@Column(name="survey_state_id")
	private SurveyState surveyState;

	//uni-directional many-to-one association to SurveyStructure
    @ManyToOne
	@JoinColumn(name="current_question_id")
	private Question currentQuestion;
    
    @ManyToOne(fetch = EAGER)
	@JoinColumn(name="parentsurvey_id")
	private Survey parentSurvey;
    
    @OneToMany(mappedBy="parentSurvey")
	private List<Survey> subSurveys;
    
    
    
    public Survey() {
    	referenceTime = new Date();
    }

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUserLogin() {
		return this.userLogin;
	}

	public void setUserLogin(String userLogin) {
		this.userLogin = userLogin;
	}

	public List<Answer> getAnswers() {
		return this.answers;
	}

	public void setAnswers(List<Answer> answers) {
		this.answers = answers;
	}
	
	public SurveyStructure getSurveyStructure() {
		return this.surveyStructure;
	}

	public void setSurveyStructure(SurveyStructure surveyStructure) {
		this.surveyStructure = surveyStructure;
	}

	public SurveyState getSurveyState() {
		return surveyState;
	}

	public void setSurveyState(SurveyState surveyState) {
		this.surveyState = surveyState;
	}

	public Question getCurrentQuestion() {
		return currentQuestion;
	}

	public void setCurrentQuestion(Question currentQuestion) {
		this.currentQuestion = currentQuestion;
	}
	
	public String getCreationUser() {
		return creationUser;
	}

	public void setCreationUser(String creationUser) {
		this.creationUser = creationUser;
	}

	public void setParentSurvey(Survey parentSurvey) {
		this.parentSurvey = parentSurvey;
	}

	public Survey getParentSurvey() {
		return parentSurvey;
	}

	public void setSubSurveys(List<Survey> subSurveys) {
		this.subSurveys = subSurveys;
	}

	public List<Survey> getSubSurveys() {
		return subSurveys;
	}
	
	public void setReferenceTime(Date referenceTime) {
		this.referenceTime = referenceTime;
	}

	public Date getReferenceTime() {
		return referenceTime;
	}

	public String toString(){
		
		return ""+this.getId();
		
		
	}
	
	
}