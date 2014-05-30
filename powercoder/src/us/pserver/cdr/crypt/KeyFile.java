/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.cdr.crypt;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import us.pserver.cdr.hex.HexCoder;



/**
 *
 * @author juno
 */
public class KeyFile {
  
  private CryptKey key;
  
  private String file;
  
  
  public KeyFile(String file) {
    if(file == null || file.isEmpty()
        || !Files.exists(Paths.get(file)))
      throw new IllegalArgumentException("Invalid file: "+ file);
    this.file = file;
    key = null;
  }
  
  
  public KeyFile(CryptKey k, String file) {
    if(file == null || file.isEmpty())
      throw new IllegalArgumentException("Invalid file: "+ file);
    this.file = file;
    key = k;
  }
  
  
  public boolean save() {
    if(file == null || key == null)
      return false;
    
    try {
      BufferedWriter bw = Files.newBufferedWriter(
          Paths.get(file), Charset.forName("UTF-8"), 
          StandardOpenOption.WRITE);
      bw.write(key.getAlgorithm().toString());
      bw.newLine();
      bw.write(HexCoder.toHexString(key.getHash()));
      bw.newLine();
      bw.flush();
      bw.close();
      return true;
    } catch(IOException e) {
      return false;
    }
  }
  
  
  public KeyFile load() {
    if(file == null || !Files.exists(Paths.get(file)))
      return this;
    
    try {
      BufferedReader br = Files.newBufferedReader(
          Paths.get(file), Charset.forName("UTF-8"));
      String algo = br.readLine();
      String hex = br.readLine();
      CryptAlgorithm ca = this.fromString(algo);
      byte[] hash = HexCoder.fromHexString(hex);
      if(ca != null && hash != null) {
        key = new CryptKey();
        key.setKey(hash, ca);
      }
      return this;
    } catch(IOException ex) {
      throw new RuntimeException(ex);
    }
  }
  
  
  public CryptKey getKey() {
    return key;
  }
  
  
  public String getFile() {
    return file;
  }
  
  
  private CryptAlgorithm fromString(String str) {
    if(str == null) return null;
    if(str.equals(CryptAlgorithm.AES_CBC.getAlgorithm()))
      return CryptAlgorithm.AES_CBC;
    else if(str.equals(CryptAlgorithm.AES_CBC_PKCS5.getAlgorithm()))
      return CryptAlgorithm.AES_CBC_PKCS5;
    else if(str.equals(CryptAlgorithm.AES_ECB.getAlgorithm()))
      return CryptAlgorithm.AES_ECB;
    else if(str.equals(CryptAlgorithm.AES_ECB_PKCS5.getAlgorithm()))
      return CryptAlgorithm.AES_ECB_PKCS5;
    else if(str.equals(CryptAlgorithm.DES_CBC.getAlgorithm()))
      return CryptAlgorithm.DES_CBC;
    else if(str.equals(CryptAlgorithm.DES_CBC_PKCS5.getAlgorithm()))
      return CryptAlgorithm.DES_CBC_PKCS5;
    else if(str.equals(CryptAlgorithm.DES_ECB.getAlgorithm()))
      return CryptAlgorithm.DES_ECB;
    else if(str.equals(CryptAlgorithm.DES_ECB_PKCS5.getAlgorithm()))
      return CryptAlgorithm.DES_ECB_PKCS5;
    else if(str.equals(CryptAlgorithm.DESede_CBC.getAlgorithm()))
      return CryptAlgorithm.DESede_CBC;
    else if(str.equals(CryptAlgorithm.DESede_CBC_PKCS5.getAlgorithm()))
      return CryptAlgorithm.DESede_CBC_PKCS5;
    else if(str.equals(CryptAlgorithm.DESede_ECB.getAlgorithm()))
      return CryptAlgorithm.DESede_ECB;
    else if(str.equals(CryptAlgorithm.DESede_ECB_PKCS5.getAlgorithm()))
      return CryptAlgorithm.DESede_ECB_PKCS5;
    else 
      return null;
  }
  
}
