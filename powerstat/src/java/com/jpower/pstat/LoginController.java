/*
 * Direitos Autorais Reservados (c) 2011 Juno Roesler
 * Contato: juno.rr@gmail.com
 * 
 * Esta biblioteca é software livre; você pode redistribuí-la e/ou modificá-la sob os
 * termos da Licença Pública Geral Menor do GNU conforme publicada pela Free
 * Software Foundation; tanto a versão 2.1 da Licença, ou (a seu critério) qualquer
 * versão posterior.
 * 
 * Esta biblioteca é distribuída na expectativa de que seja útil, porém, SEM
 * NENHUMA GARANTIA; nem mesmo a garantia implícita de COMERCIABILIDADE
 * OU ADEQUAÇÃO A UMA FINALIDADE ESPECÍFICA. Consulte a Licença Pública
 * Geral Menor do GNU para mais detalhes.
 * 
 * Você deve ter recebido uma cópia da Licença Pública Geral Menor do GNU junto
 * com esta biblioteca; se não, acesse 
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html, 
 * ou escreva para a Free Software Foundation, Inc., no
 * endereço 59 Temple Street, Suite 330, Boston, MA 02111-1307 USA.
 */


package com.jpower.pstat;

import com.db4o.ObjectContainer;
import com.db4o.query.Query;
import com.jpower.sys.DB;
import com.jpower.sys.security.AES256Cipher;
import com.jpower.sys.security.Password;
import com.jpower.sys.security.SHA256;
import com.jpower.sys.security.User;
import java.util.List;
import java.util.Random;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @version 0.0 - 05/02/2013
 * @author Juno Roesler - juno.rr@gmail.com
 */
@ManagedBean
@SessionScoped
public class LoginController {
  
  private String uid;
  
  protected Password puid;
  
  private String email;
  
  private Password pass;
  
  private User user;
  
  private ObjectContainer db;
  
  private boolean logged;
  
  private AES256Cipher aes;
  
  
  public LoginController() {
    this.updateUID();
    email = null;
    user = null;
    logged = false;
    pass = null;
    db = DB.openSocketClient();
  }
  
  
  public void updateUID() {
    uid = String.valueOf(new Random().nextInt());
    puid = new Password(uid);
    uid = puid.getHash();
  }
  
  
  public String getPasswd() {
    return null;
  }
  
  
  public void setPasswd(String p) {
    if(p == null || p.isEmpty())
      return;
    
    aes = new AES256Cipher(puid);
    byte[] bp = SHA256.fromHexString(p);
    String passwd = new String(aes.decrypt(bp));
    if(passwd != null && !passwd.isEmpty())
      pass = new Password(passwd);
  }
  
  
  public String getEmail() {
    return email;
  }
  
  
  public void setEmail(String e) {
    if(e == null || e.isEmpty())
      return;
    
    aes = new AES256Cipher(puid);
    byte[] be = SHA256.fromHexString(e);
    email = new String(aes.decrypt(be));
  }
  
  
  public String getUid() {
    this.updateUID();
    return uid;
  }
  
  
  public User getUser() {
    return user;
  }
  
  
  public boolean isLogged() {
    return logged;
  }
  
  
  public void login() {
    if(email == null || pass == null)
      return;
    
    user = this.search();
    System.out.println("* Login.user: "+ user);
    if(user == null) {
      this.showFatalMessage("Invalid E-mail");
      return;
    }
    logged = user.getPass().validate(pass);
    
    if(!logged) {
      showFatalMessage("Invalid Password!");
    }
  }
  
  
  public String logout() {
    logged = false;
    user = null;
    return "new-index.xhtml";
  }
  
  
  public void showFatalMessage(String msg) {
    FacesContext cont = FacesContext.getCurrentInstance();
    cont.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, msg, null));
  }
  
  
  public User search() {
    if(email == null)
      return null;
    
    Query q = db.query();
    q.constrain(User.class);
    q.descend("email").constrain(email);
    
    List<User> us = DB.query(q, db);
    if(us == null || us.isEmpty())
      return null;
    
    return us.get(0);
  }
  
}
