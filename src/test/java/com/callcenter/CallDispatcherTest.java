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

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.Before;
import org.junit.Test;

public class CallDispatcherTest {

	CallDispatcher instance = null;

	private int NUM_CALLERS = 30;
	private int NUM_THREADS = 6;
	private Random rnd = new Random();

	private class Caller implements Callable<Boolean> {
		Call call;

		public Caller(Call _call) {
			this.call = _call;
		}

		@Override
		public Boolean call() throws Exception {
			instance.dispatchCall(call);
			/*
			 * NB: a Caller thread may finish when its call has not been served yet, since
			 * it may have been enqueued due to lack of available Employees.
			 */
			return !call.isActive;
		}

		public boolean hasBeenServedCorrectly() {
			return !call.isActive && call.solvedBy.getValue() >= call.priority;
		}
	}

	@Before
	public void init() {
		instance = new CallDispatcher(3, 2, 1);
	}

	@Test
	public void testBasicCall() {
		Call basicCall = new Call();
		assertTrue(basicCall.isActive);
		instance.dispatchCall(basicCall);
		assertFalse(basicCall.isActive);
		testNoQueuedCalls();
	}
	
	@Test
	public void testInvalidCalls() {
		instance.dispatchCall(null);
		instance.dispatchCall(new Call(2342));
		testNoQueuedCalls();
	}

	@Test
	public void testPrioritizedCalls() {
		Call priritizedCall1 = new Call(1);
		assertTrue(priritizedCall1.isActive);
		instance.dispatchCall(priritizedCall1);
		assertFalse(priritizedCall1.isActive);
		assertTrue(priritizedCall1.solvedBy.getValue() >= priritizedCall1.priority);

		Call priritizedCall2 = new Call(2);
		assertTrue(priritizedCall2.isActive);
		instance.dispatchCall(priritizedCall2);
		assertFalse(priritizedCall2.isActive);
		assertTrue(priritizedCall2.solvedBy.getValue() >= priritizedCall2.priority);
		testNoQueuedCalls();
	}

	@Test
	public void testConcurrentCalls() {
		ArrayList<Caller> callers = new ArrayList<Caller>();
		for (int i = 0; i < NUM_CALLERS; i++)
			callers.add(new Caller(new Call(rnd.nextInt(CallDispatcher.RANKS))));

		ExecutorService ex = Executors.newFixedThreadPool(NUM_THREADS);
		ArrayList<Future<Boolean>> futures = new ArrayList<Future<Boolean>>();
		try {
			for (int i = 0; i < NUM_CALLERS; i++)
				futures.add(ex.submit(callers.get(i)));

			// Wait for all Caller threads to terminate
			for (Future<Boolean> future : futures)
				future.get();

			for (Caller caller : callers)
				assertTrue(caller.hasBeenServedCorrectly());
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		testNoQueuedCalls();
	}
	
	private void testNoQueuedCalls() {
		int[] expQueuesSize = new int[CallDispatcher.RANKS];
		Arrays.fill(expQueuesSize, 0);
		assertArrayEquals(expQueuesSize, instance.getQueuesSize());
	}
}
