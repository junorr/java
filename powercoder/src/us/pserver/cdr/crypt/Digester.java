/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.cdr.crypt;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import static us.pserver.cdr.Checker.nullarg;
import static us.pserver.cdr.Checker.nullarray;
import static us.pserver.cdr.Checker.nullstr;



/**
 *
 * @author juno
 */
public class Digester {
  
  
  public static byte[] digest(byte[] bs, DigestAlgorithm algo) {
    nullarray(bs);
    nullarg(DigestAlgorithm.class, algo);
    try {
      return MessageDigest.getInstance(algo.getAlgorithm()).digest(bs);
    } catch(NoSuchAlgorithmException e) {
      return null;
    }
  }
  
  
  public static byte[] digest(String str, DigestAlgorithm algo) {
    nullstr(str);
    nullarg(DigestAlgorithm.class, algo);
    return digest(toBytes(str), algo);
  }
  
  
  public static byte[] toSHA1(byte[] bs) {
    nullarray(bs);
    return digest(bs, DigestAlgorithm.SHA_1);
  }
  
  
  public static byte[] toSHA1(String str) {
    nullstr(str);
    return digest(str, DigestAlgorithm.SHA_1);
  }
  
  
  public static byte[] toSHA256(byte[] bs) {
    nullarray(bs);
    return digest(bs, DigestAlgorithm.SHA_256);
  }
  
  
  public static byte[] toSHA256(String str) {
    nullstr(str);
    return digest(str, DigestAlgorithm.SHA_256);
  }
  
  
  public static byte[] toMD5(byte[] bs) {
    nullarray(bs);
    return digest(bs, DigestAlgorithm.MD5);
  }
  
  
  public static byte[] toMD5(String str) {
    nullstr(str);
    return digest(str, DigestAlgorithm.MD5);
  }
  
  
  public static byte[] toBytes(String str) {
    nullstr(str);
    if(str.isEmpty()) return new byte[0];
    try {
      return str.getBytes("UTF-8");
    } catch(UnsupportedEncodingException e) {
      return new byte[0];
    }
  }
  
  
  public static void main(String[] args) {
    System.out.println("* codeSHA1: "+ toSHA1("s3cReT"));
  }
  
}
