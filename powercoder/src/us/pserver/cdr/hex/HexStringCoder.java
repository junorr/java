/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.cdr.hex;

import us.pserver.cdr.Coder;
import us.pserver.cdr.StringByteConverter;
import us.pserver.cdr.crypt.DigestAlgorithm;
import us.pserver.cdr.crypt.Digester;



/**
 *
 * @author juno
 */
public class HexStringCoder implements Coder<String> {

  private StringByteConverter scv;
  
  
  public HexStringCoder() {
    scv = new StringByteConverter();
  }
  
  
  @Override
  public String apply(String t, boolean encode) {
    if(t == null || t.isEmpty())
      return t;
    return (encode ? encode(t): decode(t));
  }
  
  
  @Override
  public String encode(String t) {
    if(t == null || t.isEmpty())
      return t;
    return HexCoder.toHexString(scv.convert(t));
  }


  @Override
  public String decode(String t) {
    if(t == null || t.isEmpty())
      return t;
    return scv.reverse(HexCoder.fromHexString(t));
  }
  
  
  public static void main(String[] args) {
    String s = "z3VQXufZgpbg$=UWc7d$wUX#%QnTFoDF";
    StringByteConverter conv = new StringByteConverter();
    byte[] bs = conv.convert(s);
    bs = Digester.digest(s, DigestAlgorithm.SHA_256);
    System.out.println(HexCoder.encode(bs));
  }
  
}
