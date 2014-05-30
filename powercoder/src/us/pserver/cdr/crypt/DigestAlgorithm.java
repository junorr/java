/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.cdr.crypt;


/**
 *
 * @author juno
 */
public enum DigestAlgorithm {
  
  SHA_1("SHA-1"),
  
  SHA_256("SHA-256"),
  
  MD5;
  
  
  private String algo;
  
  
  private DigestAlgorithm() {
    algo = this.name();
  }
  
  
  private DigestAlgorithm(String algo) {
    this.algo = algo;
  }
  
  
  public String getAlgorithm() {
    return algo;
  }
  
  
  @Override
  public String toString() {
    return algo;
  }
  
}
