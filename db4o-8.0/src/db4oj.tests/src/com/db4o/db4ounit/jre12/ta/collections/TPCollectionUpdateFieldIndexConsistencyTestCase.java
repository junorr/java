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
package com.db4o.db4ounit.jre12.ta.collections;

import java.util.*;

import com.db4o.db4ounit.common.ta.*;

public class TPCollectionUpdateFieldIndexConsistencyTestCase extends TPFieldIndexConsistencyTestCaseBase {

	/**
	 * @sharpen.ignore
	 */
	@decaf.Ignore(decaf.Platform.JDK11)
	public static class Holder {
		public List<Item> _items = new com.db4o.collections.ActivatableArrayList<Item>();
		
		public void add(Item item) {
			_items.add(item);
		}
	}
	
	/**
	 * @sharpen.ignore
	 */
	@decaf.Ignore(decaf.Platform.JDK11)
	public void testImplicitStoreThroughCollection() {
		int id = 42;
		Item item = new Item(id);
		Holder holder = new Holder();
		store(item);
		store(holder);
		holder.add(item);
		assertItemQuery(id);
		commit();
		assertFieldIndex(id);
	}
}
