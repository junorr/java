/*
 * Direitos Autorais Reservados (c) 2011 Juno Roesler
 * Contato: juno.rr@gmail.com
 * 
 * Esta biblioteca é software livre; você pode redistribuí-la e/ou modificá-la sob os
 * termos da Licença Pública Geral Menor do GNU conforme publicada pela Free
 * Software Foundation; tanto a versão 2.1 da Licença, ou qualquer
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
package badraadv.controller;

import badraadv.bean.User;
import com.jpower.mongo4j.util.Utils;
import java.io.Serializable;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;


/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 28/05/2012
 */
@ManagedBean
@SessionScoped
public class SessionControl implements Serializable {
  
  public static final long DEFAULT_LOGOUT_TIME = 8 * 1000 * 60;
  
  private boolean logged;
  
  private User user;
  
  private Timer logout;
  
  private long logoutTime;
  
  private long userID;
  
  
  private class LogoutTask extends TimerTask {
    @Override
    public void run() {
      setLogged(false);
      System.out.println("## SessionControl: LogoutTask: LogoutTime expired!");
      DynamicNavigation.go("index.xhtml");
    }
  }
  
  
  public SessionControl() {
    logged = false;
    user = new User();
    userID = 0;
    logoutTime = DEFAULT_LOGOUT_TIME;
  }
  
  
  public void setUser(User u) {
    user = u;
    Map<String, Object> session = 
        this.getSession();
    if(session == null) return;
    session.remove("user");
    session.put("user", user);
    this.genUserID();
  }
  
  
  public User getUser() {
    return user;
  }
  
  
  public void startLogout() {
    if(logout != null) {
      logout.cancel();
      //System.out.println("## SessionControl: logout.cancel()!");
    }
    logout = new Timer();
    logout.schedule(new LogoutTask(), logoutTime);
  }
  
  
  public void setLogoutTime(long time) {
    if(time < 0) return;
    logoutTime = time;
  }
  
  
  public long getLogoutTime() {
    return logoutTime;
  }


  public boolean isLogged() {
    if(logged)
      this.startLogout();
    
    return logged;
  }


  public long getUserID() {
    return userID;
  }


  public void setUserID(long userID) {
    this.userID = userID;
  }
  
  
  public void setLogged(boolean value) {
    logged = value;
    if(logged) {
      this.startLogout();
      this.genUserID();
    } 
    else {
      this.setUser(new User());
      userID = 0;
      logout.cancel();
    }
  }
  
  
  private void genUserID() {
    if(user != null && user.validate() && logged)
      userID = Utils.generateID(user);
  }
  
  
  public Map<String, Object> getSession() {
    FacesContext cont = FacesContext.getCurrentInstance();
    if(cont != null)
      return cont.getExternalContext().getSessionMap();
    else
      return null;
  }
  
}
