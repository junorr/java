/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.cdr.hex;


/**
 *
 * @author juno
 */
public class HexCoder {
  
  public static String toHexString(byte[] array) {
    if(array == null || array.length == 0)
      return null;
    StringBuilder sb = new StringBuilder();
    for(int i = 0; i < array.length; i++) {
      int high = ((array[i] >> 4) & 0xf) << 4;
      int low = array[i] & 0xf;
      if(high == 0) sb.append("0");
      sb.append(Integer.toHexString(high | low));
    }
    return sb.toString();
  }
  
  
  public static byte[] fromHexString(String hex) {
    if(hex == null || hex.isEmpty()) return null;
    int len = hex.length();
    byte[] bytes = new byte[len / 2];
    for(int i = 0; i < len; i += 2) {
      if(i == len-1) break;
      else
        bytes[i / 2] = (byte) (
            (Character.digit(hex.charAt(i), 16) << 4)
            + Character.digit(hex.charAt(i + 1), 16));
    }
    return bytes;
  }
  
  
  public static byte[] decode(String str) {
    return fromHexString(str);
  }
  
  
  public static String encode(byte[] bs) {
    return toHexString(bs);
  }
  
}
