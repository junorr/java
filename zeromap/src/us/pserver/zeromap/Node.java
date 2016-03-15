/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.zeromap;

import java.util.Set;



/**
 *
 * @author juno
 */
public interface Node extends Comparable<Node> {
	
	public String value();
	
	public boolean hasChilds();
	
	public Set<Node> childs();
	
	public Node add(Node child);
	
	public Node addChild(String value);
	
	public Node newChild(String value);
	
	public Node parent();
	
	public Node find(String value);
	
	public Node firstChild();
	
	public boolean contains(String value);
	
	public boolean contains(Node child);
	
}
