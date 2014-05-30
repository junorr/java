/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.cdr.crypt;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;



/**
 *
 * @author juno
 */
public class KeySpecFactory {
  
  
  public static SecretKeySpec createKey(byte[] bs, CryptAlgorithm algo) {
    return new SecretKeySpec(bs, getKeyAlgorithm(algo));
  }
  
  
  public SecretKeySpec genetareKey(CryptAlgorithm algo) {
    return new SecretKeySpec(randomBytes(
        algo.getBytesSize()), getKeyAlgorithm(algo));
  }
  
  
  public static String getKeyAlgorithm(CryptAlgorithm algo) {
    if(algo == null) return null;
    int idx = algo.toString().indexOf("/");
    if(idx < 0) return null;
    return algo.toString().substring(0, idx);
  }
  
  
  private static byte[] randomBytes(int size) {
    if(size <= 0) return null;
    byte[] bs = new byte[size];
    for(int i = 0; i < size; i++) {
      bs[i] = (byte) Math.random();
    }
    return bs;
  }
  
  
  public static void main(String[] args) {
    byte[] secret = "s3cReT-001".getBytes();
    SecretKeySpec key = createKey(secret, CryptAlgorithm.DESede_ECB_PKCS5);
    System.out.println("* key = "+ key);
  }
  
}
