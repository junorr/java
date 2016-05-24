/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.zeromap.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import us.pserver.zeromap.Node;



/**
 *
 * @author juno
 */
public class ONode implements Node {
	
	private final String value;
	
	private final Node parent;
	
	private final ArrayList<Node> childs;
	
	
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
		this.childs = new ArrayList<>();
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
	public List<Node> childs() {
		return childs;
	}


	@Override
	public Optional<Node> findAny(String value) {
		Optional<Node> child = Optional.empty();
		if(!childs.isEmpty() && value != null) {
      child = childs.stream().filter(
          c->c.value().equals(value)
      ).findFirst();
			if(!child.isPresent()) {
        child = childs.stream().map(c->c.findAny(value))
            .filter(o->o.isPresent()).findFirst()
            .orElse(Optional.empty());
			}//if
		}//if
		return child;
	}
	
	
	@Override
	public Optional<Node> findChild(String value) {
		return childs.stream().filter(
          c->c.value().equals(value)
      ).findFirst();
	}
	
	
	@Override
	public Optional<Node> firstChild() {
		return childs.stream().findFirst();
	}


	@Override
	public boolean contains(String value) {
		return findChild(value) != null;
	}


	@Override
	public boolean contains(Node child) {
		return findChild(child.value()) != null;
	}


	@Override
	public Node add(Node child) {
		if(child != null) {
      Node ch = new ONode(child.value(), this);
      ch.childs().addAll(child.childs());
			childs.add(ch);
		}
		return this;
	}


	@Override
	public Node add(String childValue) {
		if(childValue != null && !childValue.isEmpty()) {
			childs.add(new ONode(childValue, this));
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
	public Optional<Node> parent() {
		return Optional.ofNullable(parent);
	}
  
  
  @Override
  public boolean hasParent() {
    return parent != null;
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
