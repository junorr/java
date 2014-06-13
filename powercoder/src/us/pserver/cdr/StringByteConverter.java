/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.cdr;

import java.io.UnsupportedEncodingException;



/**
 *
 * @author juno
 */
public class StringByteConverter implements Converter<String, byte[]> {


  @Override
  public byte[] convert(String a) {
    if(a == null || a.isEmpty())
      return null;
    try {
      return a.getBytes("UTF-8");
    } catch(UnsupportedEncodingException e) {
      return null;
    }
  }


  @Override
  public String reverse(byte[] b) {
    if(b == null || b.length == 0)
      return null;
    try {
      return new String(b, "UTF-8");
    } catch(UnsupportedEncodingException e) {
      return null;
    }
  }
  
  
  public String reverse(byte[] b, int offset, int length) {
    if(b == null || b.length == 0)
      return null;
    try {
      return new String(b, offset, length, "UTF-8");
    } catch(UnsupportedEncodingException e) {
      return null;
    }
  }
  
}
