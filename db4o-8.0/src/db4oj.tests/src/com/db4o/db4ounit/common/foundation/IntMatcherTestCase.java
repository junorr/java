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
package com.db4o.db4ounit.common.foundation;

import com.db4o.internal.*;

import db4ounit.*;

/**
 * @exclude
 */
public class IntMatcherTestCase implements TestCase {
	
	public void test(){
		Assert.isTrue(IntMatcher.ZERO.match(0));
		Assert.isFalse(IntMatcher.ZERO.match(-1));
		Assert.isFalse(IntMatcher.ZERO.match(1));
		Assert.isFalse(IntMatcher.ZERO.match(Integer.MIN_VALUE));
		Assert.isFalse(IntMatcher.ZERO.match(Integer.MAX_VALUE));
		
		Assert.isFalse(IntMatcher.POSITIVE.match(0));
		Assert.isFalse(IntMatcher.POSITIVE.match(-1));
		Assert.isTrue(IntMatcher.POSITIVE.match(1));
		Assert.isFalse(IntMatcher.POSITIVE.match(Integer.MIN_VALUE));
		Assert.isTrue(IntMatcher.POSITIVE.match(Integer.MAX_VALUE));
		
		Assert.isFalse(IntMatcher.NEGATIVE.match(0));
		Assert.isTrue(IntMatcher.NEGATIVE.match(-1));
		Assert.isFalse(IntMatcher.NEGATIVE.match(1));
		Assert.isTrue(IntMatcher.NEGATIVE.match(Integer.MIN_VALUE));
		Assert.isFalse(IntMatcher.NEGATIVE.match(Integer.MAX_VALUE));
	}

}
