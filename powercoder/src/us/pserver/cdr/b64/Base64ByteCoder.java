/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.cdr.b64;

import org.apache.commons.codec.binary.Base64;
import static us.pserver.cdr.ArrayUtil.copyOf;
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
  

  public byte[] apply(byte[] t, int offset, int length, boolean encode) {
    if(t == null || t.length == 0)
      return t;
    
    byte[] bs = copyOf(t, offset, length);
    if(encode)
      return Base64.encodeBase64(bs);
    else
      return Base64.decodeBase64(bs);
  }


  public byte[] encode(byte[] t, int offset, int length) {
    return apply(t, offset, length, true);
  }


  public byte[] decode(byte[] t, int offset, int length) {
    return apply(t, offset, length, false);
  }
  
}
