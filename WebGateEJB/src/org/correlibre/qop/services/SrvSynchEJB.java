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

import java.io.Writer;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.correlibre.qop.domain.Jump;
import org.correlibre.qop.domain.Question;
import org.correlibre.qop.domain.SurveyStructure;
import org.correlibre.qop.domain.Validation;

/**
 * Session Bean implementation class SrvSynchEJB
 */
@Stateless
@LocalBean
public class SrvSynchEJB implements SrvSynchLocal {
	
	@EJB
    private SrvSurveysEngineLocal srvSurveysEngine;


    /**
     * Default constructor. 
     */
    public SrvSynchEJB() {
    	
        // TODO Auto-generated constructor stub
    }
    
    public void exportSurveyStructure(Integer surveyStructureId,Writer w) throws QopException{
    	SurveyStructure ss=srvSurveysEngine.getSurveyStructure(surveyStructureId);
    	XMLOutputFactory f=XMLOutputFactory.newInstance();
    	try {
			XMLStreamWriter sw=f.createXMLStreamWriter(w);
			sw.writeStartDocument();
			//TODO: Manejar propiedades
			sw.setPrefix("qop","http://www.correlibre.org/qop");
			sw.setDefaultNamespace("http://www.correlibre.org/qop");
			sw.writeStartElement("surveyStructure");
			sw.writeAttribute("id", ss.getId().toString());
			sw.writeAttribute("name", ss.getName());
			List<Question> ql=srvSurveysEngine.getSurveyStructureQuestions(ss);
			for (Question q:ql){
				writeQuestion(q, sw);
			}
			sw.writeEndElement();
		} catch (XMLStreamException e) {
			throw new QopException("Error exportando SS",e);
		}
    }
    
    
    private void writeQuestion(Question q,XMLStreamWriter sw) throws XMLStreamException{
		sw.writeStartElement("question");
		sw.writeAttribute("id", q.getId().toString());
		//sw.writeAttribute("name", q.getName());
		sw.writeAttribute("key", q.getKey());
		sw.writeAttribute("ordinal", q.getOrdinal().toString());
		sw.writeAttribute("text",q.getText());
		for (Jump j:q.getJumps()){
			sw.writeStartElement("jump");
			sw.writeAttribute("id", j.getId().toString());
			sw.writeAttribute("nextId", j.getNextQuestion().getId().toString());
			sw.writeAttribute("ordinal", j.getOrdinal().toString());
			sw.writeAttribute("rule",j.getRule());
			sw.writeEndElement();
		}
		for (Validation v:q.getValidations()){
			sw.writeStartElement("validation");
			sw.writeAttribute("id", v.getId().toString());
			sw.writeAttribute("key", v.getKey());
			sw.writeAttribute("type", v.getValidationType().getId().toString());
			sw.writeAttribute("rule",v.getRule());
			sw.writeAttribute("message", v.getMessage());
			sw.writeEndElement();
		}
		for (Question subQ:srvSurveysEngine.getSubQuestions(q,null)){
			writeQuestion(subQ, sw);
		}
		sw.writeEndElement();
    }
}
