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
public class NumberMapper implements Mapper<Number> {

	@Override
	public Node map(Number t) {
		Node n = null;
		if(t != null) {
			n = new ONode(t.toString());
		}
		return n;
	}


	@Override
	public Number unmap(Node node) {
		Number n = null;
		if(node != null) {
			try {
				if(node.value().contains(".")) {
					n = Double.parseDouble(node.value());
				} else {
					n = Long.parseLong(node.value());
				}
			} catch(NumberFormatException e) {}
		}
		return n;
	}
	
}
