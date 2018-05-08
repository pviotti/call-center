/**
 * call-center - ${project.description}
 * Copyright © 2018 pviotti (notexistent@email.com)
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

import java.util.concurrent.atomic.AtomicInteger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.callcenter.employees.Rank;

/**
 * A single call to the call center.
 * 
 * @author pviotti
 */
public class Call {

    private static final Logger log = LogManager.getLogger();

    /* Corresponds to the minimal Rank of an employee that can handle this call. */
    private AtomicInteger priority;

    /* Whether this call has been serviced. */
    private boolean isActive;

    /* The rank of the employee that serviced this call. */
    private Rank handlerRank;

    private long startTime = 0;
    private long endTime = 0;

    public Call() {
        this(0);
    }

    public Call(int _priority) {
        this.priority = new AtomicInteger(_priority);
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
        this.handlerRank = _rank;
        this.isActive = false;
        this.endTime = System.currentTimeMillis();
    }

    /**
     * Set the call start time.
     */
    public void setStartTime() {
        if (this.startTime == 0)
            this.startTime = System.currentTimeMillis();
    }

    /**
     * Get the call duration.
     * 
     * @return the call duration in milliseconds.
     */
    public long getDuration() {
        return this.endTime - this.startTime;
    }

    /**
     * Get the call priority.
     * 
     * @return the call priority as int.
     */
    public int getPriority() {
        return this.priority.get();
    }

    /**
     * Set the call priority.
     * 
     * @param newValue
     *            the new priority for the call.
     */
    public void setPriority(int newValue) {
        this.priority.set(newValue);
    }

    /**
     * Returns whether the call hasn't been serviced yet.
     * 
     * @return boolean on whether the call hasn't ended.
     */
    public boolean isActive() {
        return this.isActive;
    }

    /**
     * Get the rank of the employee who ended the call.
     * 
     * @return Rank object of the employee who ended the call.
     */
    public Rank getHandlerRank() {
        return this.handlerRank;
    }
}
