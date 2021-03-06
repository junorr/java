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
package com.db4o.test.nativequery;

import com.db4o.*;
import com.db4o.query.*;

import db4ounit.*;
import db4ounit.extensions.*;

public class NQPredicateFalseTestCase extends AbstractInMemoryDb4oTestCase {

	public static class Item {
	}
	
	@Override
	protected void store() throws Exception {
		store(new Item());
	}
	
	public void testPredicateFalse() {
		ObjectSet<Item> result = db().query(new Predicate<Item>() {
			@Override
			public boolean match(Item candidate) {
				return false;
			}
		});
		Assert.isTrue(result.isEmpty());
	}
	
}
