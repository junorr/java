/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.cdr;


/**
 *
 * @author juno
 */
public interface Converter<A,B> {
  
  public B convert(A a);
  
  public A reverse(B b);
  
}
