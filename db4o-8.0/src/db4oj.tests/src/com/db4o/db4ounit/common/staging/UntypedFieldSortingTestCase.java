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
package com.db4o.db4ounit.common.staging;

import com.db4o.*;
import com.db4o.query.*;

import db4ounit.*;
import db4ounit.extensions.*;

/**
 * #COR-1790
 */
public class UntypedFieldSortingTestCase extends AbstractDb4oTestCase {
	
	public static class Item {
		
		public Item(Object id) {
			_id = id;
		}

		public Object _id;
		
	}
	
	@Override
	protected void store() throws Exception {
		store(new Item(2));
		store(new Item(3));
		store(new Item(1));
	}
	
	public void test(){
		Query query = db().query(); 
		query.constrain(Item.class); 
		query.descend("_id").orderAscending(); 
		ObjectSet<Item> objectSet = query.execute();
		int lastId = 0;
		while(objectSet.hasNext()){
			Item item = objectSet.next();
			int currentId = ((Integer)item._id).intValue();
			Assert.isGreater(lastId, currentId);
			currentId = lastId;
		}
	}

}
