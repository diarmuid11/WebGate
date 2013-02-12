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

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@MappedSuperclass
public class TimeAwareEntity implements Timeable {
	@Column(name="creation_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationTime;

	@Column(name="modification_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date modificationTime;
	
	@Column(name="ending_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date endingTime;
	
	@Transient
	private boolean ending;

	/* (non-Javadoc)
	 * @see org.correlibre.orfeo.workflow.domain.Timeable#getCreationTime()
	 */
	public Date getCreationTime() {
		return creationTime;
	}

	/* (non-Javadoc)
	 * @see org.correlibre.orfeo.workflow.domain.Timeable#setCreationTime(java.util.Date)
	 */
	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	/* (non-Javadoc)
	 * @see org.correlibre.orfeo.workflow.domain.Timeable#getModificationTime()
	 */
	public Date getModificationTime() {
		return modificationTime;
	}

	/* (non-Javadoc)
	 * @see org.correlibre.orfeo.workflow.domain.Timeable#setModificationTime(java.util.Date)
	 */
	public void setModificationTime(Date modificationTime) {
		this.modificationTime = modificationTime;
	}

	/* (non-Javadoc)
	 * @see org.correlibre.orfeo.workflow.domain.Timeable#getEndingTime()
	 */
	public Date getEndingTime() {
		return endingTime;
	}

	/* (non-Javadoc)
	 * @see org.correlibre.orfeo.workflow.domain.Timeable#setEndingTime(java.util.Date)
	 */
	public void setEndingTime(Date endingTime) {
		this.endingTime = endingTime;
	}
	
	/* (non-Javadoc)
	 * @see org.correlibre.orfeo.workflow.domain.Timeable#adjustCreationTime()
	 */
	@PrePersist
	public void adjustCreationTime(){
		this.creationTime=new Date();
	}

	/* (non-Javadoc)
	 * @see org.correlibre.orfeo.workflow.domain.Timeable#adjustModificationAndEndingTime()
	 */
	@PreUpdate
	public void adjustModificationAndEndingTime(){
		Date d=new Date();
		this.modificationTime=d;
		if (ending){
			this.endingTime=d;	
		}
	}

	/* (non-Javadoc)
	 * @see org.correlibre.orfeo.workflow.domain.Timeable#signalEnding()
	 */
	public void signalEnding(){
		ending=true;
	}
	
	
}
