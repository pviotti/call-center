/**
 * call-center - ${project.description}
 * Copyright © 2018 P Viotti (notexistent@email.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.callcenter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.callcenter.employees.Rank;

/**
 * A single call to the call centre.
 * 
 * @author pviotti
 */
public class Call {

	private static final Logger log = LogManager.getLogger();

	public int priority;
	public boolean isActive;
	public Rank solvedBy;

	public Call() {
		this(0);
	}

	public Call(int _priority) {
		this.priority = _priority;
		this.isActive = true;
	}

	/**
	 * Say something to the customer.
	 * 
	 * @param message
	 *            the message to be communicated to the customer.
	 */
	public void say(String message) {
		log.info(message);
	}

	/**
	 * Mark this call as solved by a certain employee of rank _rank and hang up (set
	 * isActive to false).
	 * 
	 * @param _rank
	 *            the rank of the Employee who closed the call.
	 */
	public void disconnect(Rank _rank) {
		this.solvedBy = _rank;
		this.isActive = false;
	}
}
