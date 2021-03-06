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
package com.callcenter.employees;

import java.util.Random;

import com.callcenter.Call;
import com.callcenter.CallDispatcher;

/**
 * An employee of the call center.
 * 
 * @author pviotti
 */
public class Employee {

    CallDispatcher callDispatcher;

    // Employee's details: name, age, address, etc...
    // private String name = ...

    private final Rank rank;
    private boolean isFree;

    private static final String MSG_START = "Hi! I'm a %s. How can I help you?";
    private static final String MSG_ESCALATE = "This looks like a challenging issue! I'm going to call my boss.";
    private static final String MSG_END = "Issue solved! Thank you for calling, have a nice day!";

    private static final int MAX_CALL_DURATION = 100;
    private static final Random rnd = new Random();

    public Employee(Rank _rank, CallDispatcher _dispatcher) {
        this.rank = _rank;
        this.callDispatcher = _dispatcher;
        this.isFree = true;
    }

    /**
     * Starts handling a call. (Synchronized as a single employee cannot handle more
     * than one call at a time).
     * 
     * @param call
     *            the call to be handled.
     */
    public synchronized void handleCall(Call call) {
        this.isFree = false;
        call.setStartTime();
        call.say(String.format(MSG_START, rank.toString().toLowerCase()));

        // Emulate conversation time
        try {
            Thread.sleep(rnd.nextInt(MAX_CALL_DURATION));
        } catch (InterruptedException e) {
        }

        // Randomly escalate the call to a higher level (if it's not a director)
        if (rank != Rank.DIRECTOR && rnd.nextBoolean())
            escalateCall(call);
        else
            endCall(call);
    }

    /**
     * Escalates the call to a higher level employee.
     * 
     * @param call
     */
    private void escalateCall(Call call) {
        call.say(MSG_ESCALATE);
        call.setPriority(this.rank.getValue() + 1);
        callDispatcher.dispatchCall(call);
        this.isFree = true;
        callDispatcher.getNextCall(this);
    }

    /**
     * Ends a call.
     * 
     * @param call
     */
    private void endCall(Call call) {
        call.disconnect(this.rank);
        call.say(MSG_END + "[" + call.getDuration() + "ms]");
        this.isFree = true;
        callDispatcher.getNextCall(this);
    }

    /**
     * Returns whether the employee is free at the moment.
     * 
     * @return boolean on whether the employee is free.
     */
    public boolean isFree() {
        return this.isFree;
    }

    /**
     * Returns the rank of the employee.
     * 
     * @return Rank object of the employee.
     */
    public Rank getRank() {
        return this.rank;
    }
}
