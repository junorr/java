/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.cdr.crypt;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;



/**
 *
 * @author juno
 */
public class Digester {
  
  
  public static byte[] digest(byte[] bs, DigestAlgorithm algo) {
    try {
      return MessageDigest.getInstance(algo.getAlgorithm()).digest(bs);
    } catch(NoSuchAlgorithmException e) {
      return null;
    }
  }
  
  
  public static byte[] digest(String str, DigestAlgorithm algo) {
    return digest(toBytes(str), algo);
  }
  
  
  public static byte[] toSHA1(byte[] bs) {
    return digest(bs, DigestAlgorithm.SHA_1);
  }
  
  
  public static byte[] toSHA1(String str) {
    return digest(str, DigestAlgorithm.SHA_1);
  }
  
  
  public static byte[] toSHA256(byte[] bs) {
    return digest(bs, DigestAlgorithm.SHA_256);
  }
  
  
  public static byte[] toSHA256(String str) {
    return digest(str, DigestAlgorithm.SHA_256);
  }
  
  
  public static byte[] toMD5(byte[] bs) {
    return digest(bs, DigestAlgorithm.MD5);
  }
  
  
  public static byte[] toMD5(String str) {
    return digest(str, DigestAlgorithm.MD5);
  }
  
  
  public static byte[] toBytes(String str) {
    if(str == null) return null;
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
