/* This file is part of the db4o object database http://www.db4o.com

Copyright (C) 2004 - 2011  Versant Corporation http://www.versant.com

db4o is free software; you can redistribute it and/or modify it under
the terms of version 3 of the GNU General Public License as published
by the Free Software Foundation.

db4o is distributed in the hope that it will be useful, but WITHOUT ANY
WARRANTY; without even the implied warranty of MERCHANTABILITY or
FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
for more details.

You should have received a copy of the GNU General Public License along
with this program.  If not, see http://www.gnu.org/licenses/. */
package db4ounit;

public class JaggedArrayAssert {

	public static void areEqual(Object[][] expected, Object[][] actual) {
		if (expected == actual) return;
		if (expected == null || actual == null) Assert.areSame(expected, actual);
		Assert.areEqual(expected.length, actual.length);
		Assert.areSame(expected.getClass(), actual.getClass());
	    for (int i = 0; i < expected.length; i++) {
	        ArrayAssert.areEqual(expected[i], actual[i]);
	    }
	}

	public static void areEqual(int[][] expected, int[][] actual) {
		if (expected == actual) return;
		if (expected == null || actual == null) Assert.areSame(expected, actual);
		Assert.areEqual(expected.length, actual.length);
		Assert.areSame(expected.getClass(), actual.getClass());
	    for (int i = 0; i < expected.length; i++) {
	        ArrayAssert.areEqual(expected[i], actual[i]);
	    }
	}

}
