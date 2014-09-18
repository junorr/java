/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jpower.pstat;

import com.db4o.ObjectContainer;
import com.jpower.sys.DB;
import com.jpower.sys.security.User;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;



/**
 *
 * @author juno
 */
@ManagedBean
@SessionScoped
public class SessionController {
  
  public static final String 
      
      KEY_CONNECTIONS = "DB4O_OBJECTCONTAINER_LIST",
      
      KEY_USER = User.class.getCanonicalName();
      
  
  private HttpSession session;
  
  private User user;
  
  private List<ObjectContainer> connections;
  
  
  public SessionController() {
    session = this.getSession();
    user = null;
    connections = Collections.synchronizedList(
        new LinkedList<ObjectContainer>());
  }
  
  
  public synchronized HttpSession getSession() {
    if(session == null)
      session = (HttpSession) FacesContext.getCurrentInstance()
          .getExternalContext().getSession(true);
    return session;
  }
  
  
  public synchronized User getUser() {
    return user;
  }
  
  
  public synchronized void setUser(User u) {
    user = u;
    if(user != null)
      session.setAttribute(KEY_USER, user);
  }
  
  
  public synchronized void destroySession() {
    user = null;
    destroy(session);
    connections.clear();
    session.invalidate();
    session = null;
  }
  
  
  public static void destroy(HttpSession session) {
    session.removeAttribute(KEY_USER);
    List<ObjectContainer> conns = (List<ObjectContainer>) 
        session.getAttribute(KEY_CONNECTIONS);
    if(conns != null && !conns.isEmpty())
      for(ObjectContainer db : conns) {
        db.close();
      }
    session.removeAttribute(KEY_CONNECTIONS);
  }
  
  
  public synchronized ObjectContainer getDBConnection() {
    ObjectContainer db = null;
    try {
      db = DB.openSocketClient();
    } catch(Exception e) {}
    if(db != null) {
      connections.add(db);
      session.setAttribute(KEY_CONNECTIONS, connections);
    }
    return db;
  }
  
}
