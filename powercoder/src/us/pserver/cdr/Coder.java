/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.cdr;


/**
 *
 * @author juno
 */
public interface Coder<T> {
  
  public T apply(T t, boolean encode);
  
  public T encode(T t);
  
  public T decode(T t);
  
}
