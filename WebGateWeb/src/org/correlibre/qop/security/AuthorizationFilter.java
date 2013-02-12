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
package org.correlibre.qop.security;

import java.io.IOException;
import java.security.Principal;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.correlibre.qop.managedbeans.SessionBean;


public class AuthorizationFilter implements Filter {	
		
	private String sessionPage;
	
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {		
		HttpServletRequest httpRequest = (HttpServletRequest)request;
		HttpSession session = httpRequest.getSession(false);
		SessionBean sessionBean = (SessionBean)session.getAttribute("sessionBean");		
		Principal principal = httpRequest.getUserPrincipal();
		 
		if(principal == null){
			session.invalidate(); 
			System.out.println("Se murio la sesion");	
			//RequestDispatcher rd = request.getRequestDispatcher(sessionPage);
			//rd.forward(request,response);			
		}
		else if (sessionBean == null){			
			//RequestDispatcher rd = request.getRequestDispatcher("/faces/main.xhtml");
			//rd.forward(request,response);			
		}
		 chain.doFilter(request,response);	   
	}
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
        this.sessionPage = filterConfig.getInitParameter("session_page");

	}	
}