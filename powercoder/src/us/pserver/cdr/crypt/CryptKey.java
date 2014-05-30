/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.cdr.crypt;

import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;



/**
 *
 * @author juno
 */
public class CryptKey {
  
  private SecretKeySpec spec;
  
  private byte[] hash;
  
  private CryptAlgorithm algorithm;
  
  
  public CryptKey() {
    hash = null;
    spec = null;
    algorithm = null;
  }
  
  
  public CryptKey(String key, CryptAlgorithm algo) {
    if(key == null || key.isEmpty())
      throw new IllegalArgumentException("Invalid key: "+ key);
    if(algo == null)
      throw new IllegalArgumentException("Invalid Algorithm: "+ algo);
    
    algorithm = algo;
    hash = Digester.toSHA256(key);
    spec = KeySpecFactory.createKey(truncate(hash, algo.getBytesSize()), algo);
    if(hash == null || key.isEmpty())
      throw new IllegalArgumentException("Invalid key: "+ key);
    if(spec == null)
      throw new IllegalArgumentException("Invalid Algorithm: "+ algo);
  }


  public byte[] truncate(byte[] bs, int length) {
    if(bs == null || bs.length == 0 || length <= 0)
      return bs;
    
    byte[] nb = new byte[length];
    int idx = 0;
    int ibs = 0;
    while(idx < length) {
      if(ibs >= bs.length)
        ibs = 0;
      nb[idx++] = bs[ibs++];
    }
    return nb;
  }
  
  
  public SecretKeySpec getSpec() {
    return spec;
  }


  public void setSpec(SecretKeySpec spec) {
    this.spec = spec;
  }


  public byte[] getHash() {
    return hash;
  }


  public void setHash(byte[] hash) {
    this.hash = hash;
  }


  public CryptAlgorithm getAlgorithm() {
    return algorithm;
  }


  public void setAlgorithm(CryptAlgorithm algorithm) {
    this.algorithm = algorithm;
  }
  
  
  public void setKey(byte[] bs, CryptAlgorithm algo) {
    hash = bs;
    algorithm = algo;
    spec = KeySpecFactory.createKey(truncate(hash, algo.getBytesSize()), algo);
  }
  
}
