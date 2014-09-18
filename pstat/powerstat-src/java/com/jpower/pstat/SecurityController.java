/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jpower.pstat;

import com.jpower.sys.security.AES256Cipher;
import com.jpower.sys.security.Password;
import com.jpower.sys.security.SHA256;
import java.util.Random;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;



/**
 *
 * @author juno
 */
@ManagedBean(name="security")
@SessionScoped
public class SecurityController {
  
  private String uid;
  
  protected Password puid;
  
  private AES256Cipher aes;
  
  
  public SecurityController() {
    this.updateUID();
  }
  
  
  public void updateUID() {
    uid = String.valueOf(new Random().nextInt());
    puid = new Password(uid);
    uid = puid.getHash();
  }
  
  
  public String getUid() {
    this.updateUID();
    return uid;
  }
  
  
  public String decrypt(String enc) {
    aes = new AES256Cipher(puid);
    byte[] bp = SHA256.fromHexString(enc);
    return new String(aes.decrypt(bp));
  }
  
}
