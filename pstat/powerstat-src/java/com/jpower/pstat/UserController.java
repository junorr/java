/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jpower.pstat;

import com.db4o.ObjectContainer;
import com.db4o.query.Query;
import com.jpower.sys.DB;
import com.jpower.sys.security.Access;
import com.jpower.sys.security.AccessType;
import com.jpower.sys.security.Password;
import com.jpower.sys.security.User;
import com.jpower.sys.security.UserDAO;
import com.jpower.sys.security.UserXmlIO;
import java.util.LinkedList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;



/**
 *
 * @author juno
 */
@ManagedBean
@SessionScoped
public class UserController {
  
  @ManagedProperty(value="#{security}")
  private SecurityController security;
  
  @ManagedProperty(value="#{sessionController}")
  private SessionController session;
  
  private List<User> users;
  
  private ObjectContainer db;
  
  private User selected;
  
  private Access access;
  
  private List<String> accessTypes;
  
  private UserDAO dao;
  
  private String selectedType;
  
  private String newPassword;
  
  
  public UserController() {
    users = null;
    selected = new User();
    access = null;
    accessTypes = null;
    selectedType = null;
    newPassword = null;
    db = null;
    dao = null;
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
  
  
  public void updateList() {
    if(db == null) return;
    
    Query q = db.query();
    q.constrain(User.class);
    List<User> us = DB.query(q, db);
    users = new LinkedList<>();
    users.addAll(us);
  }
  
  
  public List<User> getUsers() {
    if(users == null)
      this.updateList();
    return users;
  }
  
  
  public void setSelected(User u) {
    if(u != null) {
      selected = u;
    }
  }


  public SecurityController getSecurity() {
    return security;
  }


  public void setSecurity(SecurityController security) {
    this.security = security;
  }
  
  
  public void setAccess(Access ac) {
    access = ac;
  }
  
  
  public Access getAccess() {
    return access;
  }
  
  
  public void removeAccess() {
    if(access != null && selected != null)
      selected.remove(access.getType());
  }
  
  
  public List<String> getAccessTypes() {
    if(accessTypes == null) {
      accessTypes = new LinkedList<>();
      accessTypes.add("ALL");
      accessTypes.add("CPU");
      accessTypes.add("MEMORY");
      accessTypes.add("DISK");
      accessTypes.add("NETWORK");
      accessTypes.add("CHART_NETWORK");
      accessTypes.add("CHART_CPU");
      accessTypes.add("CHART_MEMORY");
      accessTypes.add("CHART_DISK");
      accessTypes.add("NOTIFICATION");
      accessTypes.add("PROCESSES");
      accessTypes.add("TRAFFIC");
      accessTypes.add("USER");
      accessTypes.add("MEMORY_CLEAR");
      accessTypes.add("PROCESS_KILL");
      accessTypes.add("TERMINAL");
    }
    return accessTypes;
  }
  
  
  public String getSelectedType() {
    return selectedType;
  }
  
  
  public void setSelectedType(String type) {
    selectedType = type;
  }
  
  
  public void addAccess() {
    if(selected == null || selectedType == null)
      return;
    AccessType type = this.getTypeByName(selectedType);
    if(type != null && !selected.contains(type))
      selected.getAccess().add(new Access(type));
  }
  
  
  public AccessType getTypeByName(String name) {
    if(name == null) return null;
    this.getAccessTypes();
    
    if(name.equals(AccessType.ALL.name()))
      return AccessType.ALL;
    else if(name.equals(AccessType.CHART_CPU.name()))
      return AccessType.CHART_CPU;
    else if(name.equals(AccessType.CHART_DISK.name()))
      return AccessType.CHART_DISK;
    else if(name.equals(AccessType.CHART_MEMORY.name()))
      return AccessType.CHART_MEMORY;
    else if(name.equals(AccessType.CHART_NETWORK.name()))
      return AccessType.CHART_NETWORK;
    else if(name.equals(AccessType.CPU.name()))
      return AccessType.CPU;
    else if(name.equals(AccessType.DISK.name()))
      return AccessType.DISK;
    else if(name.equals(AccessType.MEMORY.name()))
      return AccessType.MEMORY;
    else if(name.equals(AccessType.MEMORY_CLEAR.name()))
      return AccessType.MEMORY_CLEAR;
    else if(name.equals(AccessType.NETWORK.name()))
      return AccessType.NETWORK;
    else if(name.equals(AccessType.NOTIFICATION.name()))
      return AccessType.NOTIFICATION;
    else if(name.equals(AccessType.PROCESSES.name()))
      return AccessType.PROCESSES;
    else if(name.equals(AccessType.PROCESS_KILL.name()))
      return AccessType.PROCESS_KILL;
    else if(name.equals(AccessType.TERMINAL.name()))
      return AccessType.TERMINAL;
    else if(name.equals(AccessType.TRAFFIC.name()))
      return AccessType.TRAFFIC;
    else if(name.equals(AccessType.USER.name()))
      return AccessType.USER;
    
    return null;
  }
  
  
  public User getSelected() {
    if(selected == null)
      selected = new User();
    return selected;
  }
  
  
  public void deleteUser() {
    if(selected == null || db == null)
      return;
    
    selected = dao.queryUserByEmail(selected.getEmail());
    if(selected != null) {
      db.delete(selected);
      db.commit();
      System.out.println("* User deleted: "+ selected);
      selected = null;
    }
  }
  
  
  public void saveUser() {
    if(selected == null || selected.getEmail() == null)
      return;
    
    if(db == null || dao == null) {
      this.saveDefault();
      return;
    }
    
    User bkp = selected.clone();
    User u = dao.queryUserByEmail(selected.getEmail());
    if(u != null) {
      u.copy(bkp);
      db.store(u);
    }
    else {
      db.store(bkp);
    }
    selected = bkp;
    db.commit();
  }
  
  
  public void saveDefault() {
    if(selected == null) return;
    UserXmlIO uio = new UserXmlIO();
    uio.save(selected, LoginController.DEFAULT_USER_FILE);
  }
  
  
  public void setNewPassword(String pass) {
    if(pass != null && selected != null
        && !pass.trim().isEmpty()) {
      String s = security.decrypt(pass);
      if(s != null && !s.trim().isEmpty()
          && !s.trim().equals("password")) {
        selected.setPassword(new Password(s));
      }
    }
  }
  
  
  public String getNewPassword() {
    return " ";
  }

}
