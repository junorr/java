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
package com.db4o.test.test2;

import java.util.*;

import com.db4o.*;
import com.db4o.test.*;
import com.db4o.test.types.*;

/**
 */
@decaf.Ignore(decaf.Platform.JDK11)
public class RTreeMapComparator extends RMap{

	TEntry entry(){
		return new IntEntry();
	}

	public Object newInstance(){
		return new TreeMap(new CustomComparator());
	}

	public void specific(ObjectContainer con, int step){
		if(step > 0){
			int foundComparators = 0;
			ObjectSet set = con.queryByExample(new TreeMap());
			while(set.hasNext()){
				TreeMap tm = (TreeMap)set.next();
				Comparator cmp = tm.comparator();
				if(cmp != null){
					if(cmp instanceof CustomComparator){
						foundComparators ++;
						if(foundComparators >= step){
							return;
						}
					}
				}
			}
			Regression.addError("RTreeMapComparator comparator lost on the way");
		}
	}
}
