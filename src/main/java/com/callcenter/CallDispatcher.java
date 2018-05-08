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

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.callcenter.employees.Director;
import com.callcenter.employees.Employee;
import com.callcenter.employees.Manager;
import com.callcenter.employees.Rank;
import com.callcenter.employees.Respondent;

/**
 * The main class modeling the call center.
 * 
 * @author pviotti
 */
public class CallDispatcher {

    static final int RANKS = 3;

    private final int numRespondents;
    private final int numManagers;
    private final int numDirectors;

    private final ArrayList<Employee>[] employeeLevels = new ArrayList[RANKS];
    private final ConcurrentLinkedQueue<Call>[] callQueues = new ConcurrentLinkedQueue[RANKS];

    static final String MSG_WAIT = "All employees are busy. Please hang on: you'll be served as soon as possible.";

    /**
     * Creates a call dispatched with the given numbers of respondents, managers and
     * directors.
     * 
     * @param _numRespondents
     *            number of respondents
     * @param _numManagers
     *            number of managers
     * @param _numDirectors
     *            number of directors
     */
    public CallDispatcher(int _numRespondents, int _numManagers, int _numDirectors) {
        this.numRespondents = _numRespondents;
        this.numManagers = _numManagers;
        this.numDirectors = _numDirectors;

        // Create respondents
        ArrayList<Employee> respondents = new ArrayList<Employee>(numRespondents);
        for (int i = 0; i < numRespondents; i++)
            respondents.add(new Respondent(this));
        employeeLevels[Rank.RESPONDENT.getValue()] = respondents;

        // Create managers
        ArrayList<Employee> managers = new ArrayList<Employee>(numManagers);
        for (int i = 0; i < numManagers; i++)
            managers.add(new Manager(this));
        employeeLevels[Rank.MANAGER.getValue()] = managers;

        // Create directors
        ArrayList<Employee> directors = new ArrayList<Employee>(numDirectors);
        for (int i = 0; i < numDirectors; i++)
            directors.add(new Director(this));
        employeeLevels[Rank.DIRECTOR.getValue()] = directors;

        // Initialise call queues
        for (int i = 0; i < RANKS; i++)
            callQueues[i] = new ConcurrentLinkedQueue<Call>();
    }

    /**
     * Routes the call to the first available employee having the minimal rank
     * corresponding to the call's rank, or saves it in a queue if no employee
     * available.
     * 
     * @param call
     *            the call being dispatched
     */
    public void dispatchCall(Call call) {
        if (call == null || call.priority >= RANKS || call.priority < 0)
            return;
        
        Employee emp = getHandler(call);
        if (emp != null) {
            emp.handleCall(call);
        } else {
            // No employee is available: place the call into queue according to its priority
            call.say(MSG_WAIT);
            callQueues[call.priority].add(call);
        }
    }

    /**
     * Get the first available employee having the minimal rank corresponding to the
     * call's rank.
     * 
     * @param call
     * @return an available employee compatible with the call's rank or null if no
     *         employee is available
     */
    private Employee getHandler(Call call) {
        for (int level = call.priority; level < RANKS; level++) {
            // starts checking for free employees at the rank level of the call
            ArrayList<Employee> employeeLevel = employeeLevels[level];
            for (Employee emp : employeeLevel)
                if (emp.isFree())
                    return emp;
        }
        return null;
    }

    /**
     * Called by an employee when he/she gets free to handle queued calls.
     * 
     * @param emp
     *            the employee that wants to handle a new call
     */
    public void getNextCall(Employee emp) {
        for (int rank = emp.getRank().getValue(); rank >= 0; rank--) {
            ConcurrentLinkedQueue<Call> queque = callQueues[rank];
            Call call = queque.poll();
            if (call != null) {
                emp.handleCall(call);
                return;
            }
        }
    }

    /**
     * Get the length of the call queues.
     * 
     * @return an array with the length of the queues for each rank.
     */
    public int[] getQueuesSize() {
        int[] res = new int[RANKS];
        for (int i = 0; i < RANKS; i++)
            res[i] = callQueues[i].size();
        return res;
    }
}
