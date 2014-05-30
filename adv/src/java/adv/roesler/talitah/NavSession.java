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
package adv.roesler.talitah;

import badraadv.User;
import java.io.Serializable;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;


/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 21/12/2011
 */
@ManagedBean
@SessionScoped
public class NavSession implements Serializable {
  
  //10 minutos
  public static int LOGOUT_TIME = 10 * 60 * 1000;
  
  
  @ManagedProperty(value="#{user}")
  private User currentUser;
  
  private boolean authenticated;
  
  private long ID;
  
  private Timer logout;
  
  
  private class LogoutTask extends TimerTask {
    @Override
    public void run() {
      authenticated = false;
      currentUser = new User();
    }
  }
  
  
  public NavSession() {
    currentUser = null;
    authenticated = false;
    logout = new Timer();
    this.createID();
  }
  
  
  public Map<String, Object> getSession() {
    return FacesContext.getCurrentInstance()
        .getExternalContext().getSessionMap();
  }


  public boolean isAuthenticated() {
    this.initLogoutTimer();
    return authenticated;
  }
  
  
  public void initLogoutTimer() {
    logout.cancel();
    if(!authenticated) return;
    logout = new Timer();
    logout.schedule(new LogoutTask(), LOGOUT_TIME);
  }


  public void setAuthenticated(boolean authenticated) {
    this.authenticated = authenticated;
    if(!authenticated) {
      this.setCurrentUser(new User());
    }
    this.initLogoutTimer();
  }


  public User getCurrentUser() {
    return currentUser;
  }


  public void setCurrentUser(User currentUser) {
    this.currentUser = currentUser;
    Map<String, Object> session = 
        this.getSession();
    session.remove("user");
    session.put("user", this.currentUser);
  }
  
  
  public long getID() {
    return ID;
  }


  public void setID(long id) {
    this.ID = id;
  }
  
  
  public void createID() {
    ID = new Random().nextLong() * LOGOUT_TIME;
  }
  
  
  @PreDestroy
  public final void stop() {
    logout.cancel();
  }
  
}
