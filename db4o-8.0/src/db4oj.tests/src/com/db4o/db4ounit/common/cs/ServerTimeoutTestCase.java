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
package com.db4o.db4ounit.common.cs;

import com.db4o.config.*;
import com.db4o.cs.internal.*;
import com.db4o.foundation.*;

import db4ounit.*;

/**
 * @exclude
 */
public class ServerTimeoutTestCase extends ClientServerTestCaseBase {

	public static void main(String[] arguments) {
		new ServerTimeoutTestCase().runNetworking();
	}

	protected void configure(Configuration config) {
		config.clientServer().timeoutClientSocket(1);
		config.clientServer().timeoutServerSocket(1);
	}

	public void _test() throws Exception {
		ObjectServerImpl serverImpl = (ObjectServerImpl) clientServerFixture().server();
		Iterator4 iter = serverImpl.iterateDispatchers();
		iter.moveNext();
		ServerMessageDispatcher serverDispatcher = (ServerMessageDispatcher) iter.current();
		ClientMessageDispatcher clientDispatcher = ((ClientObjectContainer) db())
			.messageDispatcher();
		clientDispatcher.close();
		Runtime4.sleep(1000);
		Assert.isFalse(serverDispatcher.isMessageDispatcherAlive());
	}

}
