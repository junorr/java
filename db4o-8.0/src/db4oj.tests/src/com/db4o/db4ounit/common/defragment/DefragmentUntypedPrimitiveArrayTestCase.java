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
package com.db4o.db4ounit.common.defragment;

import db4ounit.Assert;
import db4ounit.extensions.AbstractDb4oTestCase;

public class DefragmentUntypedPrimitiveArrayTestCase extends AbstractDb4oTestCase {

	private static final int ITEM_SIZE = 42;

	public static class Item {
		public int _id;
		public Object _intData;
		public Object _byteData;
		public String _name;
		
		public Item(int size) {
			_id = size;
			_intData = new int[size];
			_byteData = new byte[size];
			for(int idx = 0; idx < size; idx++) {
				((int[])_intData)[idx] = idx;
				((byte[])_byteData)[idx] = (byte)idx;
			}
			_name = String.valueOf(size);
		}
	}
	
	protected void store() throws Exception {
		store(new Item(ITEM_SIZE));
	}
	
	public void testDefragment() throws Exception {
		assertItemSizes();
		defragment();
		assertItemSizes();
	}

	private void assertItemSizes() {
		Item item = (Item) retrieveOnlyInstance(Item.class);
		Assert.areEqual(ITEM_SIZE, item._id);
		Assert.areEqual(ITEM_SIZE, ((int[])item._intData).length);
		Assert.areEqual(ITEM_SIZE - 1, ((int[])item._intData)[ITEM_SIZE - 1]);
		Assert.areEqual(ITEM_SIZE, ((byte[])item._byteData).length);
		Assert.areEqual(ITEM_SIZE - 1, ((byte[])item._byteData)[ITEM_SIZE - 1]);
		Assert.areEqual(String.valueOf(ITEM_SIZE), item._name);
	}
}
