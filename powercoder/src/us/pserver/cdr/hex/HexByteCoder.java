/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.cdr.hex;

import us.pserver.cdr.Coder;
import us.pserver.cdr.StringByteConverter;



/**
 *
 * @author juno
 */
public class HexByteCoder implements Coder<byte[]> {
  
  private HexStringCoder hsc;
  
  private StringByteConverter scv;
  
  
  public HexByteCoder() {
    hsc = new HexStringCoder();
    scv = new StringByteConverter();
  }


  @Override
  public byte[] apply(byte[] t, boolean encode) {
    return (encode ? encode(t) : decode(t));
  }


  @Override
  public byte[] encode(byte[] t) {
    if(t == null || t.length == 0)
      return t;
    return scv.convert(HexCoder.toHexString(t));
  }


  @Override
  public byte[] decode(byte[] t) {
    if(t == null || t.length == 0)
      return t;
    return HexCoder.fromHexString(scv.reverse(t));
  }
  
}
