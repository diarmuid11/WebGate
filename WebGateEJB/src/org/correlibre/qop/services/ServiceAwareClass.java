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

import javax.annotation.ManagedBean;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

@ManagedBean
public abstract class ServiceAwareClass implements ServiceAware {
	
	protected transient SrvSurveysEngine engine;
	
	protected transient SrvSurveysValidation validation;
	
	//@EJB(name="java:module/SrvLocatorEJB")
	protected transient SrvLocatorLocal sl;

	public ServiceAwareClass() throws QopException {
		super();
		init();
	}
	
	//@PostConstruct
	private void init() throws QopException{
		Context c=null;
		try {
			c=new InitialContext();
			SrvLocator sl=(SrvLocator)c.lookup("java:app/WebGateEJB/SrvLocatorEJB");
			sl.injectServices(this);
		} catch (NamingException e) {
			throw new QopException("Imposible obtener initial Context",e);
		}
	}

	@Override
	public void setSurveysEngine(SrvSurveysEngine engine) {
		this.engine=engine;
	}

	@Override
	public void setSurveysValidation(SrvSurveysValidation validation) {
		this.validation=validation;
	}
	
	
}