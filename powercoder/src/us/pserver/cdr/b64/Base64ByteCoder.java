/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.cdr.b64;

import org.apache.commons.codec.binary.Base64;
import us.pserver.cdr.Coder;



/**
 *
 * @author juno
 */
public class Base64ByteCoder implements Coder<byte[]> {

  
  @Override
  public byte[] apply(byte[] t, boolean encode) {
    if(t == null || t.length == 0)
      return t;
    
    if(encode)
      return Base64.encodeBase64(t);
    else
      return Base64.decodeBase64(t);
  }


  @Override
  public byte[] encode(byte[] t) {
    return apply(t, true);
  }


  @Override
  public byte[] decode(byte[] t) {
    return apply(t, false);
  }
  
}
