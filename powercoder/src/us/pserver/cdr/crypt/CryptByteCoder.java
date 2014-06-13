/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.cdr.crypt;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;



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
    initCoders();
  }
  
  
  private void initCoders() {
    try {
      int size = 8;
      if(key.getAlgorithm() == CryptAlgorithm.AES_CBC
          || key.getAlgorithm() == CryptAlgorithm.AES_CBC_PKCS5)
        size = 16;
      
      IvParameterSpec iv = new IvParameterSpec(key.truncate(key.getHash(), size));
      encoder = Cipher.getInstance(key.getAlgorithm().toString());
      decoder = Cipher.getInstance(key.getAlgorithm().toString());
      
      if(key.getAlgorithm() == CryptAlgorithm.AES_ECB
          || key.getAlgorithm() == CryptAlgorithm.AES_ECB_PKCS5
          || key.getAlgorithm() == CryptAlgorithm.DES_ECB
          || key.getAlgorithm() == CryptAlgorithm.DES_ECB_PKCS5
          || key.getAlgorithm() == CryptAlgorithm.DESede_ECB
          || key.getAlgorithm() == CryptAlgorithm.DESede_ECB_PKCS5) {
        encoder.init(Cipher.ENCRYPT_MODE, key.getSpec());
        decoder.init(Cipher.DECRYPT_MODE, key.getSpec());
      }
      else {
        encoder.init(Cipher.ENCRYPT_MODE, key.getSpec(), iv);
        decoder.init(Cipher.DECRYPT_MODE, key.getSpec(), iv);
      }
    } catch(InvalidKeyException | NoSuchAlgorithmException
        | NoSuchPaddingException | InvalidAlgorithmParameterException  ex) {
      throw new IllegalStateException("Error creating Cipher: "+ ex, ex);
    }
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
    if(bs == null || bs.length == 0)
      return bs;
    try {
      return encoder.doFinal(bs);
    } catch(BadPaddingException | IllegalBlockSizeException ex) {
      throw new RuntimeException("Error encrypting: "+ ex, ex);
    }
  }
  
  
  @Override
  public byte[] decode(byte[] bs) {
    if(bs == null || bs.length == 0)
      return bs;
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
    if(bs == null || bs.length == 0)
      return bs;
    try {
      return encoder.doFinal(bs, offset, length);
    } catch(BadPaddingException | IllegalBlockSizeException ex) {
      throw new RuntimeException("Error encrypting: "+ ex, ex);
    }
  }
  
  
  public byte[] decode(byte[] bs, int offset, int length) {
    if(bs == null || bs.length == 0)
      return bs;
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
