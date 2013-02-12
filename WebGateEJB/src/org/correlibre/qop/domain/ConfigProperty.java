package org.correlibre.qop.domain;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the config_properties database table.
 * 
 */
@Entity
@Table(schema="surveys", name="config_properties")
@NamedQueries({
	@NamedQuery(name="ConfigProperty.findByKey",query="Select c from ConfigProperty c where c.key=:key"),
	@NamedQuery(name="ConfigProperty.findByKey-SurveyStructure",query="Select c from ConfigProperty c where c.surveyStructure=:surveyStructure and c.key=:key"),
	@NamedQuery(name="ConfigProperty.findByKey-Question",query="Select c from ConfigProperty c where c.key=:key and c.question = :question")
})
public class ConfigProperty implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	private String key;

	private String value;
	
	@ManyToOne
	@JoinColumn(name="survey_structure_id")
	private SurveyStructure surveyStructure;
	
	@ManyToOne
	@JoinColumn(name="question_id")
	private Question question;

    public ConfigProperty() {
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

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void setSurveyStructure(SurveyStructure surveyStructure) {
		this.surveyStructure = surveyStructure;
	}

	public SurveyStructure getSurveyStructure() {
		return surveyStructure;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}

	public Question getQuestion() {
		return question;
	}
	
	public String toString(){
		return this.key+ ":"+ this.value;
	}

}