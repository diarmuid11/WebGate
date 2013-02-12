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
package org.correlibre.qop.services;

import java.util.List;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.correlibre.qop.domain.Answer;
import org.correlibre.qop.domain.Question;
import org.correlibre.qop.domain.QuestionType;
import org.correlibre.qop.domain.Survey;
import org.correlibre.qop.domain.SurveyState;
import org.correlibre.qop.domain.SurveyStructure;
import org.correlibre.qop.domain.SurveyStructureState;
import org.correlibre.qop.domain.Validation;
import org.correlibre.qop.domain.ValidationType;

/**
 * Session Bean implementation class SrvSurveysAdminEJB
 */
@Stateless
@DeclareRoles("ADMINISTRADOR")
public class SrvSurveysAdminEJB implements SrvSurveysAdminLocal {

	@PersistenceContext
	private EntityManager em;
	
	@EJB
    private SrvSurveysEngineLocal srvSurveysEngine;

	
    /**
     * Default constructor. 
     */
    public SrvSurveysAdminEJB() {
        // TODO Auto-generated constructor stub
    	
    }
    
	@Override
	@RolesAllowed("ADMINISTRADOR")
	public void createSurveyStructure(String surveyStructureName) {
		SurveyStructure ss = new SurveyStructure();
		ss.setName(surveyStructureName);
		ss.setState(SurveyStructureState.CREATING);
		em.persist(ss);	
	}
	
	@Override
	@RolesAllowed("ADMINISTRADOR")
	public SurveyStructure createNewSurveyStructure(String surveyStructureName) {
		SurveyStructure ss = new SurveyStructure();
		ss.setName(surveyStructureName);
		ss.setState(SurveyStructureState.CREATING);
		em.persist(ss);	
		return ss;
	}

	@Override
	@RolesAllowed("ADMINISTRADOR")
	public void editSurveyStructure(SurveyStructure ss) throws QopException {
		switch (ss.getState()) {
			case CREATING:
			case IN_EDITION:
				return;
			case DEPLOYED:
				ss.setState(SurveyStructureState.IN_EDITION);
				em.merge(ss);
				return;
			default:
				throw new QopException("Imposible Editar SurveyStructure, estado invalido: "+ss.getState());
		}
	}

	@Override
	@RolesAllowed("ADMINISTRADOR")
	public void deploySurveyStructure(SurveyStructure ss) throws QopException {
		switch (ss.getState()) {
			case CREATING:
			case IN_EDITION:
				ss.setState(SurveyStructureState.DEPLOYED);
				em.merge(ss);	
				//TODO: subir version, ver q se hace con la copia
				break;
			default:
				throw new QopException("Imposible Editar SurveyStructure, estado invalido: "+ss.getState());
			
		}
	}

	@Override
	@RolesAllowed("ADMINISTRADOR")
	public void endSurveyStructure(SurveyStructure ss) throws QopException {
		switch (ss.getState()) {
			case DEPLOYED:
				ss.setState(SurveyStructureState.ENDED);
				em.merge(ss);	
				//TODO: subir version, ver q se hace con la copia
				break;
			default:
				throw new QopException("Imposible Editar SurveyStructure, estado invalido: "+ss.getState());
			
		}
	}

	
	@Override
	@RolesAllowed("ADMINISTRADOR")
	public void deleteSurveyStructure(SurveyStructure surveyStructure) throws QopException {
		if (surveyStructure.getState() != SurveyStructureState.CREATING){
			throw new QopException("Imposible Eliminar SurveyStructure en estado "+surveyStructure.getState());
		}
		SurveyStructure ss = srvSurveysEngine.getSurveyStructure(surveyStructure.getId());
		List<Survey> surveys = srvSurveysEngine.getSurveys(ss);
		for(Survey s: surveys){
			List<Answer> answers = srvSurveysEngine.getAllSurveyAnswers(s);
			for(Answer a: answers){
				em.remove(a);
			}
			em.remove(s);
		}	
		
		List <Question> questions = srvSurveysEngine.getSurveyStructureQuestions(ss);
		for(Question q: questions){
			 removeQuestion(q);
		}		
		em.remove(ss);		
	}
	
	
	@Override
	@RolesAllowed("ADMINISTRADOR")
	public void modifySurveyStructure(SurveyStructure ss){		
		em.merge(ss);
	}	
	
	//TODO: Optimizar!
	@Override
	@RolesAllowed("ADMINISTRADOR")
	public void modifySurveyStructureCreators(SurveyStructure ss, List<String> creatorUsers){
		ss.setCreatorUsers(null);
		em.merge(ss);
		ss.setCreatorUsers(creatorUsers);
		em.merge(ss);
	}
	
	@Override
	@RolesAllowed("ADMINISTRADOR")
	public void saveQuestion(Question question) {
		em.persist(question);
	}


	@Override
	@RolesAllowed("ADMINISTRADOR")
	public List<QuestionType> getQuestionTypes() {
		Query q = em.createNamedQuery("questionType.findAll");		
		return q.getResultList();
	}

	@Override
	@RolesAllowed("ADMINISTRADOR")
	public void reorderQuestion(SurveyStructure surveyStructure,Question question, boolean up, int steps) throws QopException {
		List<Question> questions=srvSurveysEngine.getSurveyStructureQuestions(surveyStructure);
		int idx=questions.indexOf(question);
		if (idx==-1){
			throw new QopException("Error reordenando preguntas: La pregunta no existe");
		}
		if (up && idx==0){
			return;
		}
		if (!up && idx==questions.size()-1){
			return;
		}
		Question q1=questions.get(idx);
		for (int i=1;i<=steps;i++){
			Question q2;
			if (up){
				if (idx-i<0) break;
				q2=questions.get(idx-i);
			}else{
				if ((idx+i)==questions.size()) break;
				q2=questions.get(idx+i);
			}
			int tempOrd=q2.getOrdinal();
			q2.setOrdinal(q1.getOrdinal());
			q1.setOrdinal(tempOrd);
			em.merge(q2);
		}
		em.merge(q1);
	}
	
	@Override
	@RolesAllowed("ADMINISTRADOR")
	public Question getNextQuestion(Question question) throws QopException{
		SurveyStructure ss= question.getSurveyStructure();
		List<Question> lq=srvSurveysEngine.getSurveyStructureQuestions(ss);
		int idx=lq.indexOf(question);
		if (idx==(lq.size()-1)){
			return null;
		}else{
			return lq.get(idx+1);
		}
	}

	@Override
	@RolesAllowed("ADMINISTRADOR")
	public Question getPreviousQuestion(Question question) throws QopException{
		SurveyStructure ss= question.getSurveyStructure();
		List<Question> lq=srvSurveysEngine.getSurveyStructureQuestions(ss);
		int idx=lq.indexOf(question);
		if (idx==0){
			return question;
		}else{
			return lq.get(idx-1);
		}
	}

	@Override
	@RolesAllowed("ADMINISTRADOR")
	public Question createQuestion(SurveyStructure surveyStructure,Question question, List<Question> editedSubQuestions) throws QopException {
		//TODO: Revisar si se necesita esta linea
		SurveyStructure ss=srvSurveysEngine.getSurveyStructure(surveyStructure.getId());
		persistQuestion(question, ss);
		
		if (editedSubQuestions != null){
			for (Question q : editedSubQuestions){
				persistQuestion(q, ss);
				
				if(q.getQuestions() != null){
					for (Question q2 : q.getQuestions()){
						persistQuestion(q2, ss);
					}
				}
			}
			
		}
		
		setKeysAndOrdinals(question,ss);
		System.out.println("############ [SrvSurveysAdminEJB] SALIENDO DE CREATE");
		return question;
	}
	
	@Override
	@RolesAllowed("ADMINISTRADOR")
	public Question modifyQuestion(Question question, List<Question> editedSubQuestions, List<Validation> newValidations) throws QopException {
		SurveyStructure ss = question.getSurveyStructure();
		persistNewValidations(question,newValidations);
	
		persistQuestion(question, ss);
		if (editedSubQuestions!=null){
			for (Question q:editedSubQuestions){
				persistQuestion(q, ss);
			}
		}
		setKeysAndOrdinals(question,ss);
		System.out.println("######### [SrvSurveysAdminEJB] SALIENDO DE MODIFY");
		return question;
	}
	
	//TODO: verificar si question es nueva (id es null)
	private void persistNewValidations(Question q,List<Validation> newVs){
		for (Validation v : newVs){
			
			if(v.getId() == null){
				em.persist(v);
				q.getValidations().add(v);
				
			}else
				em.merge(v);
			
			v.setQuestion(q);
			
			System.out.println(v);
		}
	}
	
	private void persistQuestion(Question q,SurveyStructure ss){
		
		System.out.println("##### [SrvSurveyAdminEJB] q: "+q);
		
		if (q.getSurveyStructure()==null){
			q.setSurveyStructure(ss);
		}
		
		if (q.getId()==null){ //for insert
			if (!q.isForRemoval()){
				System.out.println("ADICIONANDO:"+q.getKey());
				em.persist(q);
			}else{
				System.out.println("IGNORANDO:"+q.getKey());
				return;
			}
		}else{ //for update or remove
			if (!q.isForRemoval()){
				System.out.println("ACTUALIZANDO:"+q.getKey());
				q=em.merge(q);
			}else{
				removeQuestion(q);
			}
		}
		/*
		for (Question subQ:srvSurveysEngine.getSubQuestions(q,null)){
			associateSurveyStructure(ss,subQ);
		}
		return q;
		*/
	}
	
	private void setKeysAndOrdinals(Question question, SurveyStructure ss) throws QopException{
		List<Question> mainqs = srvSurveysEngine.getSurveyStructureQuestions(ss);
		generateKey(question,"preg_"+ss.getId()+"_", mainqs);
		generateOrdinal(question, mainqs);
	}
	
	//genera la key de la pregunta base
	private void generateKey(Question question, String baseKey, List<Question> questions){
		if (question.getKey()==null){
			if (questions.size()==1){
				question.setKey(baseKey+'A');
			}else{
				//System.out.println("questions.size()-2: "+(questions.size()-2));
				//System.out.println("questions.get(questions.size()-2): "+questions.get(questions.size()-2));
				
				int i = 0;
				String lastKey = "";
				char currentKeyIndex = 0;
				for (Question q : questions) {
					
					System.out.println("q: "+q);
					System.out.println("question: "+question);
					
					if(q.getKey() != null){
						i = q.getKey().lastIndexOf("_")+1;
						
						lastKey = q.getKey().substring(i, q.getKey().length());
						
						System.out.println("currentKey: "+lastKey);
						
						if(lastKey.charAt(0) > currentKeyIndex){
							currentKeyIndex = lastKey.charAt(0);
						}
					}
						
				}
				
				System.out.println("lastKey: "+lastKey);
				
				//lastKey = questions.get(questions.size()-2).getKey(); //OJO
				
				//if(lastKey == null)
					//lastKey = questions.get(questions.size()-3).getKey(); //OJO
				
				//char lastKeyIndex = lastKey.substring(lastKey.lastIndexOf('_')+1).charAt(0);
				char lastKeyIndex;
				lastKeyIndex = currentKeyIndex;
				char newLastKeyIndex = (char)(((int) lastKeyIndex)+1);
				if((int) newLastKeyIndex == 90)
					newLastKeyIndex = 'a';
				
				question.setKey(baseKey+newLastKeyIndex);
			}
		}
		generateKey(question.getKey()+"_",srvSurveysEngine.getSubQuestions(question, null));
	}

	//genera recursivamente keys de subquestions
	private void generateKey(String baseKey,List<Question> questions){
		if (questions==null){
			return;
		}
		for (int i=0;i<questions.size();i++){
			Question q=questions.get(i);
			if (q.getKey()==null){
				if (i==0){
					q.setKey(baseKey+'A');
				}else{
					/*
					String lastKey=questions.get(i-1).getKey();
					
					if(lastKey == null)
						lastKey = questions.get(i-2).getKey(); //OJO
					
					
					char lastKeyIndex = lastKey.substring(lastKey.lastIndexOf('_')+1).charAt(0);
					char newLastKeyIndex = (char)(((int) lastKeyIndex)+1);
					if((int) newLastKeyIndex == 90)
						newLastKeyIndex = 'a';
					
					q.setKey(baseKey+newLastKeyIndex);
					*/
					int j = 0;
					String lastKey = "";
					char currentKeyIndex = 0;
					for (Question q2 : questions) {
						
						System.out.println("q: "+q2);
						
						
						if(q2.getKey() != null){
							i = q2.getKey().lastIndexOf("_")+1;
							
							lastKey = q2.getKey().substring(i, q2.getKey().length());
							
							System.out.println("currentKey: "+lastKey);
							
							if(lastKey.charAt(0) > currentKeyIndex){
								currentKeyIndex = lastKey.charAt(0);
							}
						}
							
					}
					
					System.out.println("lastKey: "+lastKey);
					
					//lastKey = questions.get(questions.size()-2).getKey(); //OJO
					
					//if(lastKey == null)
						//lastKey = questions.get(questions.size()-3).getKey(); //OJO
					
					//char lastKeyIndex = lastKey.substring(lastKey.lastIndexOf('_')+1).charAt(0);
					char lastKeyIndex;
					lastKeyIndex = currentKeyIndex;
					char newLastKeyIndex = (char)(((int) lastKeyIndex)+1);
					if((int) newLastKeyIndex == 90)
						newLastKeyIndex = 'a';
					
					q.setKey(baseKey+newLastKeyIndex);
				}
			}
			generateKey(q.getKey()+"_",srvSurveysEngine.getSubQuestions(q,null));
		}
	}

	//genera ordinal de pregunta base
	private void generateOrdinal(Question question,List<Question> questions){
		if (question.getOrdinal()==null){
			if (questions.size()==1){
				question.setOrdinal(1);
			}else{
				Integer ord = questions.get(questions.size()-2).getOrdinal();
				
				if(ord == null)
					ord = questions.get(questions.size()-3).getOrdinal();
				
				question.setOrdinal(ord+1);
			}
		}
		generateOrdinal(srvSurveysEngine.getSubQuestions(question, null));
	}

	//genera recursivamente ordinals de subquestions
	private void generateOrdinal(List<Question> questions){
		if (questions==null){
			return;
		}
		for (int i=0;i<questions.size();i++){
			Question q=questions.get(i);
			if (q.getOrdinal()==null){
				if (i==0){	//pregunta 1
					q.setOrdinal(1);
				}else{
					
					Integer ord = questions.get(i-1).getOrdinal();
					
					if(ord == null)
						ord = questions.get(i-2).getOrdinal();
					
					q.setOrdinal(ord+1);
				}
			}
			generateOrdinal(srvSurveysEngine.getSubQuestions(q,null));
		}
	}

	// TODO: Cambiar nombre a removeSubQuestions, pues Solo elimina las Subquestions del a question, la 
	// question la deja tal cual.
	private void removeQuestion(Question q){
		for (Question subQ:srvSurveysEngine.getSubQuestions(q,null)){
			removeQuestion(subQ);
		}
		System.out.println("REMOVIENDO:"+q.getKey());
		Question q2=em.merge(q);
		em.remove(q2);
	}
	
	
	@Override
	@RolesAllowed("ADMINISTRADOR")
	public void assignSurvey(SurveyStructure ss, String userLogin) throws QopException{
		if (ss.getState()!=SurveyStructureState.DEPLOYED){
			throw new QopException("Solo es posible asignar encuestas desplegadas");
		}
		List<Question> questions = srvSurveysEngine.getSurveyStructureQuestions(ss);
		
    	Survey s = new Survey();  
		s.setSurveyStructure(ss);
		s.setUserLogin(userLogin); 
		s.setSurveyState(SurveyState.NEW);
		s.setCurrentQuestion(questions.get(0));
		em.persist(s);		
	}
			
	@Override
	public List<Object> getUserSurveysDetail(String userName){
		Query q = em.createNamedQuery("Survey.groupSurvey");
		q.setParameter("userLogin", userName);		
		return q.getResultList();
	}

	@Override
	public Long getSurveys(String userName, SurveyState state){
		Query q = em.createNamedQuery("Survey.NumberOfSurveysByState");
		q.setParameter("userLogin", userName); 
		q.setParameter("surveyState", state); 
		return (Long) q.getSingleResult();
	}

	@Override
	public void addValidation(Question question, String validationTypeId) {
		Validation v= new Validation();
		v.setQuestion(question);
		ValidationType vt=em.find(ValidationType.class, Integer.valueOf(validationTypeId));
		v.setValidationType(vt);
		em.persist(v);
	}
	
	@Override
	@RolesAllowed("ADMINISTRADOR")
	public void associateSurveyStructure(SurveyStructure parent, SurveyStructure child) throws QopException {
		if ((parent.getState()==SurveyStructureState.IN_EDITION || parent.getState()==SurveyStructureState.CREATING )
				&& (child.getState()==SurveyStructureState.IN_EDITION || child.getState()!=SurveyStructureState.CREATING)){
			child.setParentSurveyStructure(parent);
			em.merge(child);	
		}else{
			throw new QopException("Solo es posible asociar formatos en estado de creación o edición");
		}
		
	}
	
}
