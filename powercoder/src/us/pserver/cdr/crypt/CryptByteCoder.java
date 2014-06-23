/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.cdr.crypt;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import static us.pserver.cdr.Checker.nullarg;
import static us.pserver.cdr.Checker.nullarray;
import static us.pserver.cdr.Checker.range;



/**
 *
 * @author juno
 */
public class CryptByteCoder implements CryptCoder<byte[]> {
  
  private Cipher encoder;
  
  private Cipher decoder;
  
  private CryptKey key;
  
  
  public CryptByteCoder(CryptKey key) {
    if(key == null || key.getAlgorithm() == null
        || key.getHash() == null || key.getSpec() == null)
      throw new IllegalArgumentException("Invalid key: "+ key);
    
    this.key = key;
    Cipher[] cps = CryptUtils.createEncryptDecryptCiphers(key);
    encoder = cps[0];
    decoder = cps[1];
  }
  
  
  @Override
  public CryptKey getKey() {
    return key;
  }


  public Cipher getEncoder() {
    return encoder;
  }


  public Cipher getDecoder() {
    return decoder;
  }
  
  
  @Override
  public byte[] encode(byte[] bs) {
    nullarray(bs);
    try {
      return encoder.doFinal(bs);
    } catch(BadPaddingException | IllegalBlockSizeException ex) {
      throw new RuntimeException("Error encrypting: "+ ex, ex);
    }
  }
  
  
  @Override
  public byte[] decode(byte[] bs) {
    nullarray(bs);
    try {
      return decoder.doFinal(bs);
    } catch(BadPaddingException | IllegalBlockSizeException ex) {
      throw new RuntimeException("Error decrypting: "+ ex, ex);
    }
  }
  
  
  @Override
  public byte[] apply(byte[] bs, boolean encode) {
    return (encode ? encode(bs) : decode(bs));
  }
  
  
  public byte[] encode(byte[] bs, int offset, int length) {
    nullarray(bs);
    range(offset, 0, bs.length -1);
    range(length, 1, bs.length);
    try {
      return encoder.doFinal(bs, offset, length);
    } catch(BadPaddingException | IllegalBlockSizeException ex) {
      throw new RuntimeException("Error encrypting: "+ ex, ex);
    }
  }
  
  
  public byte[] decode(byte[] bs, int offset, int length) {
    nullarray(bs);
    range(offset, 0, bs.length -1);
    range(length, 1, bs.length);
    try {
      return decoder.doFinal(bs, offset, length);
    } catch(BadPaddingException | IllegalBlockSizeException ex) {
      throw new RuntimeException("Error decrypting: "+ ex, ex);
    }
  }
  
  
  public byte[] apply(byte[] bs, int offset, int length, boolean encode) {
    return (encode ? encode(bs, offset, length) 
        : decode(bs, offset, length));
  }
  
  
  public static void main(String[] args) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
    CryptKey key = new CryptKey("mySecretPassword", 
        CryptAlgorithm.DES_CBC_PKCS5);
    CryptByteCoder ccd = new CryptByteCoder(key);
    
    byte[] bs = Files.readAllBytes(Paths.get("sample.log"));
    System.out.println("* bytes readed: "+ bs.length);
    bs = ccd.encode(bs);
    Files.write(Paths.get("sample2.log.bce"), bs, StandardOpenOption.WRITE);
    System.out.println("* bytes writed: "+ bs.length);
    
    bs = Files.readAllBytes(Paths.get("sample2.log.bce"));
    System.out.println("* bytes readed: "+ bs.length);
    bs = ccd.decode(bs);
    Files.write(Paths.get("sample2.log"), bs, StandardOpenOption.WRITE);
    System.out.println("* bytes writed: "+ bs.length);
  }
  
}
