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

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * The persistent class for the questions database table.
 * 
 */
@Entity
@Table(name="questions",schema="surveys")
@NamedQueries({
	@NamedQuery(name="Question.findMainQuestionsBySurveyStructure",query="Select q from Question q where q.surveyStructure=:surveyStructure and q.question is null order by q.ordinal"),
	@NamedQuery(name="Question.findQuestionBySurveyStructureAndKey",query="Select q from Question q where q.surveyStructure=:surveyStructure and q.key=:key"),
	@NamedQuery(name="Question.findSubQuestions",query="Select q from Question q where q.question=:question order by q.ordinal"),
	@NamedQuery(name="Question.findSubQuestionsByQuestionTypeId",query="Select q from Question q where q.question=:question and q.questionType.id=:questionTypeId order by q.ordinal"),	
	@NamedQuery(name="Question.findQuestionsBySurveyStructure",query="Select q from Question q where q.surveyStructure=:surveyStructure")
})
public class Question extends TimeAwareEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	private String key;

	private String name;

	private Integer ordinal;

	private String text;
	
	@Column(name="help_text")
	private String helpText;

	//bi-directional many-to-one association to Option
	@OneToMany(mappedBy="question")
	private List<Option> options;

	//uni-directional many-to-one association to QuestionType
    @ManyToOne
	@JoinColumn(name="question_type_id")
	private QuestionType questionType;

	//bi-directional many-to-one association to Question
    @ManyToOne
    private Question question;

	//bi-directional many-to-one association to Question
	@OneToMany(mappedBy="question", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@OrderBy("ordinal")
	private List<Question> questions;

	//bi-directional many-to-one association to SurveyStructure
    @ManyToOne
	@JoinColumn(name="survey_structure_id")
	private SurveyStructure surveyStructure;

	//bi-directional many-to-one association to Validation
	@OneToMany(mappedBy="question",cascade=CascadeType.ALL)
	private List<Validation> validations;
	
	//bi-directional many-to-one association to Jump
	@OneToMany(mappedBy="question",cascade=CascadeType.REMOVE)
	@OrderBy("ordinal")
	private List<Jump> jumps;
	
	@OneToMany(mappedBy="nextQuestion",cascade=CascadeType.REMOVE)
	@OrderBy("ordinal")
	private List<Jump> backJumps;
	
	//bi-directional many-to-one association to Option
	@ManyToOne
	@JoinColumn(name="category_domain_id")
	private CategoryDomain categoryDomain;
	
	@Transient
	private boolean forRemoval;

    public Question() {
    }

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getKey() {
		return this.key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getOrdinal() {
		return this.ordinal;
	}

	public void setOrdinal(Integer ordinal) {
		this.ordinal = ordinal;
	}

	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public List<Option> getOptions() {
		return this.options;
	}

	public void setOptions(List<Option> options) {
		this.options = options;
	}
	
	public QuestionType getQuestionType() {
		return this.questionType;
	}

	public void setQuestionType(QuestionType questionType) {
		this.questionType = questionType;
	}
	
	public Question getQuestion() {
		return this.question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}
	
	public SurveyStructure getSurveyStructure() {
		return this.surveyStructure;
	}

	public void setSurveyStructure(SurveyStructure surveyStructure) {
		this.surveyStructure = surveyStructure;
	}
	
	public List<Validation> getValidations() {
		return this.validations;
	}

	public void setValidations(List<Validation> validations) {
		this.validations = validations;
	}
	
	public List<Jump> getJumps() {
		return this.jumps;
	}

	public void setJumps(List<Jump> jumps) {
		this.jumps = jumps;
	}

	public boolean isForRemoval() {
		return forRemoval;
	}

	public void setForRemoval(boolean forRemoval) {
		this.forRemoval = forRemoval;
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
		Question other = (Question) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	public String toString(){
		return "id: "+ id + ", key: "+key+ ", text: "+text;
		/*
		StringBuffer sb=new StringBuffer("INFORMACION DE PREGUNTA\n");
		sb.append("id: "+this.id+"\n");
		sb.append("key: "+this.key+"\n");
		sb.append("name: "+this.name+"\n");
		
		sb.append("Type: "+(questionType==null?"null":questionType.getName())+"\n");
		sb.append("TypeId: "+(questionType==null?"null":questionType.getId())+"\n");
		sb.append("SS: "+(surveyStructure==null?"null":surveyStructure.getName())+"\n");
		sb.append("ordinal: "+ordinal+"\n");
		sb.append("text: "+text+"\n");
		sb.append("forRemoval: "+forRemoval +"\n");
		
		if (questions!=null && questions.size()>0){
			sb.append("SUBQUESTIONS\n\n");
			for (Question q:questions){
				sb.append(q.toString());
			}
		}
		*/
		//return sb.toString();
	}

	public void setCategoryDomain(CategoryDomain categoryDomain) {
		this.categoryDomain = categoryDomain;
	}

	public CategoryDomain getCategoryDomain() {
		return categoryDomain;
	}

	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}

	public List<Question> getQuestions() {
		return questions;
	}

	public void setBackJumps(List<Jump> backJumps) {
		this.backJumps = backJumps;
	}

	public List<Jump> getBackJumps() {
		return backJumps;
	}

	public void setHelpText(String helpText) {
		this.helpText = helpText;
	}

	public String getHelpText() {
		return helpText;
	}
	
	
}

