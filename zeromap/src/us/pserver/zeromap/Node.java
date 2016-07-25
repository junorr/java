/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.zeromap;

import java.util.List;
import java.util.Optional;



/**
 *
 * @author juno
 */
public interface Node extends Comparable<Node> {
	
	public String value();
	
	public boolean hasChilds();
	
	public List<Node> childs();
	
	public Node add(Node child);
	
	public Node add(String value);
	
	public Node newChild(String value);
  
  public boolean hasParent();
	
	public Optional<Node> parent();
	
	public Optional<Node> findChild(String value);
	
	public Optional<Node> findAny(String value);
	
	public Optional<Node> firstChild();
	
	public boolean contains(String value);
	
	public boolean contains(Node child);
	
}
