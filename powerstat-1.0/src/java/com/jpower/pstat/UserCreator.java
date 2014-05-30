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
  
  private ObjectContainer db;
  
  private User user;
  
  private UserDAO dao;
  
  
  public UserCreator() {
    db = DB.openSocketClient();
    dao = new UserDAO(db);
    user = new User();
  }


  public User getUser() {
    return user;
  }


  public void setUser(User user) {
    this.user = user;
  }
  
  
  public void newUser() {
    user = new User();
    System.out.println("* new user created!");
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
    System.out.println("* setting password ["+ password+ "]");
    user.setPassword(new Password(password));
  }
  
  
  public String saveUser() {
    if(user == null || user.getEmail() == null)
      return null;
    User u = dao.queryUserByEmail(user.getEmail());
    if(u != null) return null;
    db.store(user);
    db.commit();
    userCtrl.setSelected(user);
    return "user-edit.xhtml?faces-redirect=true";
  }
  
}
