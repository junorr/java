/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.cdr.crypt;

import us.pserver.cdr.Coder;



/**
 *
 * @author juno
 */
public interface CryptCoder<T> extends Coder<T> {
  
  public CryptKey getKey();
  
}
