/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.zeromap.mapper;

import us.pserver.zeromap.Mapper;
import us.pserver.zeromap.Node;
import us.pserver.zeromap.impl.ClassFactory;
import us.pserver.zeromap.impl.ONode;



/**
 *
 * @author juno
 */
public class ClassMapper implements Mapper<Class> {
	
	@Override
	public Node map(Class t) {
		Node n = null;
		if(t != null) {
			n = new ONode(t.getName());
		}
		return n;
	}


	@Override
	public Class unmap(Node n, Class<? extends Class> cls) {
		Class c = null;
		if(n != null) {
			c = ClassFactory.create(n.value());
		}
		return c;
	}
	
	
	@Override
	public boolean canHandle(Class cls) {
		return cls != null 
				&& Class.class.isAssignableFrom(cls); 
	}

}
