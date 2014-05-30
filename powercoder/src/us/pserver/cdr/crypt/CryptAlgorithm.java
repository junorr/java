/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.cdr.crypt;


/**
 *
 * @author juno
 */
public enum CryptAlgorithm {
  
  AES_CBC("AES/CBC/NoPadding", 128),
  
  AES_CBC_PKCS5("AES/CBC/PKCS5Padding", 128),
  
  DES_CBC("DES/CBC/NoPadding", 64),
  
  DES_CBC_PKCS5("DES/CBC/PKCS5Padding", 64),
  
  DESede_CBC("DESede/CBC/NoPadding", 192),
  
  DESede_CBC_PKCS5("DESede/CBC/PKCS5Padding", 192),
  
  AES_ECB("AES/ECB/NoPadding", 128),
  
  AES_ECB_PKCS5("AES/ECB/PKCS5Padding", 128),
  
  DES_ECB("DES/ECB/NoPadding", 64),
  
  DES_ECB_PKCS5("DES/ECB/PKCS5Padding", 64),
  
  DESede_ECB("DESede/ECB/NoPadding", 192),
  
  DESede_ECB_PKCS5("DESede/ECB/PKCS5Padding", 192);
  
  
  private CryptAlgorithm(String transform, int size) {
    this.transform = transform;
    keySize = size;
  }
  
  
  public String getAlgorithm() {
    return transform;
  }
  
  
  public int getBitsSize() {
    return keySize;
  }
  
  
  public int getBytesSize() {
    return keySize / 8;
  }
  
  
  @Override
  public String toString() {
    return transform;
  }
  
  private String transform;
  
  private int keySize;
  
}
