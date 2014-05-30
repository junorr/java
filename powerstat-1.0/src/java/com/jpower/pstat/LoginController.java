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
import com.jpower.sys.security.Access;
import com.jpower.sys.security.AccessType;
import com.jpower.sys.security.Password;
import com.jpower.sys.security.User;
import com.jpower.sys.security.UserXmlIO;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @version 0.0 - 05/02/2013
 * @author Juno Roesler - juno.rr@gmail.com
 */
@ManagedBean
@SessionScoped
public class LoginController implements Serializable {
  
  public static final String 
      
      SESSION_KEY_USER = User.class.getCanonicalName(),
      
      INDEX = "index.xhtml",
      
      DEFAULT_USER_FILE =  LoginController.class
          .getResource("conf/rsc").toString()
          .replace("rsc", "user.xml").replace("file:", "");
  
  
  public static final User DEFAULT_USER = createDefault();
  
  
  @ManagedProperty(value="#{security}")
  private SecurityController security;
  
  private String email;
  
  private Password pass;
  
  private User user;
  
  private ObjectContainer db;
  
  private boolean logged;
  
  private UserXmlIO uio;
  
  
  public LoginController() {
    email = null;
    user = null;
    logged = false;
    pass = null;
    uio = new UserXmlIO();
    tryConnectDB();
  }
  
  
  private void tryConnectDB() {
    System.out.println("* tryConnectDB...");
    System.out.println("--------------------------");
    try {
      db = DB.openSocketClient();
    } catch(Exception e) {
      System.out.print("** "+ e);
      db = null;
      Path p;
      if(DEFAULT_USER_FILE.startsWith("/") &&
          DEFAULT_USER_FILE.contains(":"))
        p = Paths.get(DEFAULT_USER_FILE.substring(1));
      else
        p = Paths.get(DEFAULT_USER_FILE);
      if(!Files.exists(p))
        uio.save(DEFAULT_USER, DEFAULT_USER_FILE);
    }
  }
  
  
  public boolean isDBAvaliable() {
    return db != null;
  }


  public SecurityController getSecurity() {
    return security;
  }


  public void setSecurity(SecurityController security) {
    this.security = security;
  }
  
  
  public HttpSession getSession(boolean create) {
    return (HttpSession) FacesContext.getCurrentInstance()
        .getExternalContext().getSession(create);
  }
  
  
  public String getPasswd() {
    return null;
  }
  
  
  public void setPasswd(String p) {
    if(p == null || p.isEmpty())
      return;
    String passwd = security.decrypt(p);
    if(passwd != null && !passwd.isEmpty())
      pass = new Password(passwd);
  }
  
  
  public String getEmail() {
    return email;
  }
  
  
  public void setEmail(String e) {
    if(e == null || e.isEmpty())
      return;
    email = security.decrypt(e);
  }
  
  
  public User getUser() {
    user = (User) this.getSession(false)
        .getAttribute(SESSION_KEY_USER);
    return user;
  }
  
  
  public boolean isLogged() {
    return logged;
  }
  
  
  public void login() {
    if(email == null || pass == null)
      return;
    
    if(db == null) {
      try {
        db = DB.openSocketClient();
      } catch(Exception e) {
        System.out.print("** "+ e);
        this.defaultLogin();
        return;
      }
    }
    
    user = this.search();
    this.auth();
  }
  
  
  private void auth() {
    if(user == null) {
      this.showFatalMessage("Invalid E-mail");
      return;
    }
    if(pass == null) {
      this.showFatalMessage("Invalid Password");
      return;
    }
    
    logged = email.equals(user.getEmail())
        && user.getPassword().validate(pass);
    
    if(logged) {
      this.getSession(true)
          .setAttribute(SESSION_KEY_USER, user);
    }
    else {
      showFatalMessage("Invalid Password!");
    }
  }
  
  
  public void removeDefaultUser() {
    try {
      Files.delete(Paths.get(DEFAULT_USER_FILE));
    } catch(IOException e) {}
  }
  
  
  public void defaultLogin() {
    if(email == null || pass == null)
      return;
    user = uio.load(DEFAULT_USER_FILE);
    this.auth();
  }
  
  
  public String logout() {
    logged = false;
    user = null;
    this.getSession(false).removeAttribute(SESSION_KEY_USER);
    this.getSession(false).invalidate();
    try {
      ExternalContext req = FacesContext.getCurrentInstance().getExternalContext();
      req.invalidateSession();
      req.redirect(INDEX);
    } catch(IOException ex) {}
    return INDEX;
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
  
  
  public static User createDefault() {
    User u = new User();
    u.setAdmin(true);
    u.setEmail("admin@localhost");
    u.setName("Administrator");
    u.setPass("admin");
    u.getAccess().add(new Access(AccessType.ALL));
    return u;
  }
  
  
  
  
  public static void main(String[] args) {
    System.out.println(LoginController.DEFAULT_USER_FILE);
  }
  
  
}
