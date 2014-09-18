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
package com.db4o.db4ounit.common.ta.nonta;

import com.db4o.db4ounit.common.ta.*;

import db4ounit.*;

/**
 * @exclude
 */
public class NonTANArrayTestCase extends NonTAItemTestCaseBase {
	
	private static final int[][] INTS1 = new int[][]{ {1,2,3}, {4,5,6}};
	
	private static final int[][] INTS2 = new int[][] { {4,5,6}, {7,8,9}};
	
	private static final LinkedList[][] LIST1 = new LinkedList[][] {{ LinkedList.newList(5)}, {LinkedList.newList(5)}};
	
	private static final LinkedList[][] LIST2 = new LinkedList[][] {{ LinkedList.newList(5)}, {LinkedList.newList(5)}};

	public static void main(String[] args) {
		new NonTANArrayTestCase().runAll();
	}

    protected void assertItemValue(Object obj) {
        NArrayItem item = (NArrayItem) obj;
        JaggedArrayAssert.areEqual(INTS1, item.value());
        JaggedArrayAssert.areEqual(INTS2, (int[][])item.object());
        JaggedArrayAssert.areEqual(LIST1, item.lists());
        JaggedArrayAssert.areEqual(LIST2, (LinkedList[][]) item.listsObject());
    }

    protected Object createItem() {
        NArrayItem item = new NArrayItem();
        item.value = INTS1;
        item.obj = INTS2;
        
        item.lists = LIST1;
        item.listsObject = LIST2;
        return item;
    }

}
