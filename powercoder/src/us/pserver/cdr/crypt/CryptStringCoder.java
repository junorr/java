/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.cdr.crypt;

import static us.pserver.cdr.Checker.nullstr;
import us.pserver.cdr.StringByteConverter;
import us.pserver.cdr.b64.Base64ByteCoder;



/**
 *
 * @author juno
 */
public class CryptStringCoder implements CryptCoder<String> {
  
  private CryptByteCoder coder;
  
  private Base64ByteCoder bbc;
  
  private StringByteConverter scv;
  
  
  public CryptStringCoder(CryptKey key) {
    if(key == null)
      throw new IllegalArgumentException(
          "Illegal CryptKey: "+ key);
    coder = new CryptByteCoder(key);
    bbc = new Base64ByteCoder();
    scv = new StringByteConverter();
  }
  
  
  @Override
  public CryptKey getKey() {
    return coder.getKey();
  }
  
  
  public CryptByteCoder getCoder() {
    return coder;
  }
  
  
  @Override
  public String apply(String  str, boolean encode) {
    nullstr(str);
    byte[] bs = scv.convert(str);
    if(encode) {
      bs = coder.encode(bs);
      bs = bbc.encode(bs);
    }
    else {
      bs = bbc.decode(bs);
      bs = coder.decode(bs);
    }
    return scv.reverse(bs);
  }
  
  
  @Override
  public String encode(String str) {
    return apply(str, true);
  }
  
  
  @Override
  public String decode(String str) {
    return apply(str, false);
  }
  
}
