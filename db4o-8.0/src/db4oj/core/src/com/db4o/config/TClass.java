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
package com.db4o.config;

import com.db4o.*;
import com.db4o.reflect.jdk.*;

/**
 * @exclude
 * 
 * @sharpen.ignore
 */
public class TClass implements ObjectConstructor
{
	public Object onStore(ObjectContainer oc, Object object){
		return ((Class)object).getName();
	}
	
	public void onActivate(ObjectContainer oc, Object object, Object members){
		// do nothing
	}

	public Object onInstantiate(ObjectContainer oc, Object storedObject){
		return JdkReflector.toNative(oc.ext().reflector().forName((String)storedObject));
	}

	public Class storedClass(){
		return String.class;
	}
}
