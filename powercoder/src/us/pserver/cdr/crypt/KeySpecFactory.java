/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.cdr.crypt;

import javax.crypto.spec.SecretKeySpec;
import static us.pserver.cdr.Checker.nullarg;
import static us.pserver.cdr.Checker.nullarray;
import static us.pserver.cdr.Checker.range;



/**
 *
 * @author juno
 */
public class KeySpecFactory {
  
  
  public static SecretKeySpec createKey(byte[] bs, CryptAlgorithm algo) {
    nullarray(bs);
    nullarg(CryptAlgorithm.class, algo);
    return new SecretKeySpec(bs, getKeyAlgorithm(algo));
  }
  
  
  public SecretKeySpec genetareKey(CryptAlgorithm algo) {
    nullarg(CryptAlgorithm.class, algo);
    return new SecretKeySpec(randomBytes(
        algo.getBytesSize()), getKeyAlgorithm(algo));
  }
  
  
  public static String getKeyAlgorithm(CryptAlgorithm algo) {
    nullarg(CryptAlgorithm.class, algo);
    int idx = algo.toString().indexOf("/");
    if(idx < 0) return null;
    return algo.toString().substring(0, idx);
  }
  
  
  private static byte[] randomBytes(int size) {
    range(size, 1, Integer.MAX_VALUE);
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
