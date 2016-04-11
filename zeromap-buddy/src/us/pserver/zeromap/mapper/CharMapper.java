/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.zeromap.mapper;

import us.pserver.zeromap.Mapper;
import us.pserver.zeromap.Node;
import us.pserver.zeromap.impl.ONode;



/**
 *
 * @author juno
 */
public class CharMapper implements Mapper<Character> {

	@Override
	public Node map(Character t) {
		Node n = null;
		if(t != null) {
			n = new ONode(t.toString());
		}
		return n;
	}


	@Override
	public Character unmap(Node n, Class<? extends Character> cls) {
		Character c = null;
		if(n != null) {
			c = n.value().charAt(0);
		}
		return c;
	}
	
	
	@Override
	public boolean canHandle(Class cls) {
		return cls != null 
				&& (Character.class.isAssignableFrom(cls) 
				|| char.class == cls); 
	}

}
