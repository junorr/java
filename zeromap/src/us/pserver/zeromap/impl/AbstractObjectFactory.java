/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.zeromap.impl;

import us.pserver.zeromap.Node;
import us.pserver.zeromap.ObjectFactory;



/**
 *
 * @author juno
 */
public abstract class AbstractObjectFactory implements ObjectFactory {

	@Override
	public Object create(Class cls, Node nod) throws Exception {
		if(cls == null) {
			throw new IllegalArgumentException(
					"Class must be not null"
			);
		}
		if(nod == null) {
			throw new IllegalArgumentException(
					"Node must be not null"
			);
		}
		return null;
	}
	
}
