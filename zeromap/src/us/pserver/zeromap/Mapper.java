/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.zeromap;


/**
 *
 * @author juno
 */
public interface Mapper<T> {
	
	public Node map(T t);
	
	public T unmap(Node n);
	
}
