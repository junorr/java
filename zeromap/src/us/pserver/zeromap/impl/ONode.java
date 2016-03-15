/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.zeromap.impl;

import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import us.pserver.zeromap.Node;



/**
 *
 * @author juno
 */
public class ONode implements Node {
	
	private final String value;
	
	private final Node parent;
	
	private final TreeSet<Node> childs;
	
	
	public ONode(String value) {
		this(value, null);
	}
	
	
	public ONode(String value, Node parent) {
		if(value == null) {
			throw new IllegalArgumentException(
					"Value must be not null"
			);
		}
		this.value = value;
		this.parent = parent;
		this.childs = new TreeSet<>();
	}
	
	
	@Override
	public String value() {
		return value;
	}


	@Override
	public boolean hasChilds() {
		return !childs.isEmpty();
	}


	@Override
	public Set<Node> childs() {
		return childs;
	}


	@Override
	public Node find(String value) {
		Node child = null;
		if(!childs.isEmpty() && value != null) {
			child = childs.floor(new ONode(value));
			if(child == null || !value.equals(child.value())) {
				for(Node n : childs) {
					if((child = n.find(value)) != null) {
						break;
					}
				}
			}
		}
		return child;
	}
	
	
	@Override
	public Node firstChild() {
		Node first = null;
		if(hasChilds()) {
			first = childs.first();
		}
		return first;
	}


	@Override
	public boolean contains(String value) {
		return childs.contains(new ONode(value));
	}


	@Override
	public boolean contains(Node child) {
		return childs.contains(child);
	}


	@Override
	public Node add(Node child) {
		if(child != null) {
			childs.add(child);
		}
		return this;
	}


	@Override
	public Node addChild(String value) {
		if(value != null && !value.isEmpty()) {
			childs.add(new ONode(value, this));
		}
		return this;
	}


	@Override
	public Node newChild(String value) {
		Node child = null;
		if(value != null && !value.isEmpty()) {
			child = new ONode(value, this);
			childs.add(child);
		}
		return child;
	}


	@Override
	public Node parent() {
		return parent;
	}


	@Override
	public int compareTo(Node o) {
		int cmp = 1;
		if(o != null) {
			cmp = value.compareTo(o.value());
		}
		return cmp;
	}


	@Override
	public int hashCode() {
		int hash = 3;
		hash = 79 * hash + Objects.hashCode(this.value);
		return hash;
	}


	@Override
	public boolean equals(Object obj) {
		if(this == obj) {
			return true;
		}
		if(obj == null) {
			return false;
		}
		if(getClass() != obj.getClass()) {
			return false;
		}
		final ONode other = (ONode) obj;
		if(!Objects.equals(this.value, other.value)) {
			return false;
		}
		return true;
	}


	@Override
	public String toString() {
		return new StringNode(this).toString("");
	}
	
}
