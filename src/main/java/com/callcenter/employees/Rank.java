package com.callcenter.employees;

/**
 * Rank of employees.
 * 
 * @author pviotti
 */
public enum Rank {
	RESPONDENT(0), MANAGER(1), DIRECTOR(2);

	private final int rank;

	Rank(int _rank) {
		rank = _rank;
	}

	public int getValue() {
		return rank;
	}
}
