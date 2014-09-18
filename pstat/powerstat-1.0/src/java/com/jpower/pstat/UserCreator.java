/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jpower.pstat;

import com.db4o.ObjectContainer;
import com.jpower.sys.DB;
import com.jpower.sys.security.Password;
import com.jpower.sys.security.User;
import com.jpower.sys.security.UserDAO;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;



/**
 *
 * @author juno
 */
@ManagedBean
@SessionScoped
public class UserCreator {
  
  @ManagedProperty(value="#{userController}")
  private UserController userCtrl;
  
  @ManagedProperty(value="#{security}")
  private SecurityController security;
  
  @ManagedProperty(value="#{sessionController}")
  private SessionController session;
  
  private ObjectContainer db;
  
  private User user;
  
  private UserDAO dao;
  
  
  public UserCreator() {
    db = null;
    dao = null;
    user = new User();
  }


  public SessionController getSession() {
    return session;
  }


  public void setSession(SessionController session) {
    this.session = session;
    if(session != null) {
      db = session.getDBConnection();
      if(db != null)
        dao = new UserDAO(db);
    }
  }


  public User getUser() {
    return user;
  }


  public void setUser(User user) {
    this.user = user;
  }
  
  
  public void newUser() {
    user = new User();
  }


  public UserController getUserCtrl() {
    return userCtrl;
  }


  public void setUserCtrl(UserController userCtrl) {
    this.userCtrl = userCtrl;
  }


  public SecurityController getSecurity() {
    return security;
  }


  public void setSecurity(SecurityController security) {
    this.security = security;
  }
  
  
  public String getPassword() {
    return " ";
  }
  
  
  public void setPassword(String password) {
    if(password == null || password.trim().isEmpty())
      return;
    password = security.decrypt(password);
    if(password == null || password.trim().isEmpty())
      return;
    user.setPassword(new Password(password));
  }
  
  
  public String saveUser() {
    if(user == null || user.getEmail() == null 
        || db == null) return null;
    
    User u = dao.queryUserByEmail(user.getEmail());
    if(u != null) return null;
    db.store(user);
    db.commit();
    userCtrl.setSelected(user);
    return "user-edit.xhtml?faces-redirect=true";
  }
  
}
