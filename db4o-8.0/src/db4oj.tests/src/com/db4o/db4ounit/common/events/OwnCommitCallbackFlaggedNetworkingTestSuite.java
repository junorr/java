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
package com.db4o.db4ounit.common.events;

import db4ounit.fixtures.*;

public class OwnCommitCallbackFlaggedNetworkingTestSuite extends FixtureBasedTestSuite {

	public static class Item {
		public int _id;
		
		public Item(int id) {
			_id = id;
		}
	}

	@Override
	public FixtureProvider[] fixtureProviders() {
		return new FixtureProvider[] {
			new SimpleFixtureProvider<OwnCommittedCallbacksFixture.ContainerFactory>(OwnCommittedCallbacksFixture.FACTORY, 
					new OwnCommittedCallbacksFixture.NetworkingCSContainerFactory()
			),
			new SimpleFixtureProvider<OwnCommittedCallbacksFixture.CommitAction>(OwnCommittedCallbacksFixture.ACTION,
					new OwnCommittedCallbacksFixture.ClientACommitAction(),
					new OwnCommittedCallbacksFixture.ClientBCommitAction()
			),
		};
	}

	@Override
	public Class[] testUnits() {
		return new Class[] {
				OwnCommittedCallbacksFixture.OwnCommitCallbackFlaggedTestUnit.class,
		};
	}

}
