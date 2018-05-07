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
package com.callcenter.employees;

import com.callcenter.CallDispatcher;

/**
 * Director employee.
 * 
 * @author pviotti
 */
public class Director extends Employee {
	public Director(CallDispatcher _dispatcher) {
		super(Rank.DIRECTOR, _dispatcher);
	}
}
