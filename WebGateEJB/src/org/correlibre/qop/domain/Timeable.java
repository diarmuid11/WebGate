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

import java.util.Date;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

public interface Timeable {

	public abstract Date getCreationTime();

	public abstract void setCreationTime(Date creationTime);

	public abstract Date getModificationTime();

	public abstract void setModificationTime(Date modificationTime);

	public abstract Date getEndingTime();

	public abstract void setEndingTime(Date endingTime);

	@PrePersist
	public abstract void adjustCreationTime();

	@PreUpdate
	public abstract void adjustModificationAndEndingTime();

	public abstract void signalEnding();

}