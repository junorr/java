/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.zeromap.impl;

import us.pserver.zeromap.Node;



/**
 *
 * @author juno
 */
public class StringNode {
	
	private final Node node;
	
	
	public StringNode(Node n) {
		if(n == null) {
			throw new IllegalArgumentException(
					"Node must be not null"
			);
		}
		this.node = n;
	}
	
	
	public Node getNode() {
		return node;
	}
	
	
	public String toString(String ident) {
		StringBuilder sb = new StringBuilder()
				.append(ident)
				.append("Node(")
				.append(node.value())
				.append("){");
		if(node.hasChilds()) {
			node.childs().stream().forEach((n) -> {
				sb.append("\n").append(new StringNode(n).toString(ident+"  "));
			});
			sb.append("\n").append(ident);
		}
		return sb.append("}").toString();
	}
	
}
