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
package com.db4o.db4ounit.common.concurrency;

import com.db4o.db4ounit.common.persistent.*;
import com.db4o.ext.*;

import db4ounit.*;
import db4ounit.extensions.*;

public class SetRollbackTestCase extends Db4oClientServerTestCase {

	public static void main(String[] args) {
		new SetRollbackTestCase().runConcurrency();
	}
	
	public void concSetRollback(ExtObjectContainer oc, int seq) {
		if (seq % 2 == 0) { // if the thread sequence is even, store something
			for (int i = 0; i < 1000; i++) {
				SimpleObject c = new SimpleObject("oc " + i, i);
				oc.store(c);
			}
		} else { // if the thread sequence is odd, rollback
			for (int i = 0; i < 1000; i++) {
				SimpleObject c = new SimpleObject("oc " + i, i);
				oc.store(c);
				oc.rollback();
				c = new SimpleObject("oc2.2 " + i, i);
				oc.store(c);
			}
			oc.rollback();
		}
	}

	public void checkSetRollback(ExtObjectContainer oc) {
		Assert.areEqual(threadCount() / 2 * 1000, oc.query(SimpleObject.class)
				.size());
	}
}
