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
package org.correlibre.qop.parsing;

import java.util.HashMap;

import org.correlibre.qop.services.QopException;

public class Parser {
	private static Parser parser = null;
		
        
    private Parser() throws QopException {
    
    }
    
    private static void createInstance() throws QopException {
        if (parser == null) { 
            parser = new Parser( );
        }
    }
 
    public static Parser getInstance( String expr, String classExpr, HashMap<String,String> values) throws QopException {
        if (parser == null) createInstance();
        return parser;
    }
}
