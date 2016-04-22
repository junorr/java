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
	
	public Node map(T t) throws ObjectMappingException;
	
	public default Node map(T t, Node parent) throws ObjectMappingException {
		Node n = map(t);
		if(parent != null) {
			n = parent.add(n);
		}
		return n;
	}
	
	public T unmap(Node n, Class<? extends T> cls) throws ObjectMappingException;
	
	public boolean canHandle(Class cls);
	
}
