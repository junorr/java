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
package adv.roesler.talitah.controller;

import adv.roesler.talitah.NavSession;
import adv.roesler.talitah.Navigation;
import badraadv.User;
import com.jpower.mongo4j.GenericDAO;
import com.jpower.mongo4j.QueryExample;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 16/12/2011
 */
@ManagedBean
@RequestScoped
public class LoginController implements Serializable {
  
  public static final String INVALID_LOGIN = "Usuário/Senha inválidos!";
  
  private String page;
  
  private String loginError;
  
  private GenericDAO dao;
  
  @ManagedProperty(value="#{navSession}")
  private NavSession session;
  
  
  public LoginController() {
    dao = new GenericDAO("adv");
    page = null;
    loginError = null;
  }
  
  
  public String getLoginError() {
    return loginError;
  }
  
  
  public String getNextNav() {
    return page;
  }
  
  
  public NavSession getSession() {
    return session;
  }
  
  
  public void setSession(NavSession s) {
    session = s;
  }
  
  
  public String login(User u) {
    System.out.println("LOGIN");
    System.out.println(session);
    System.out.println(u);
    if(session == null || u == null) return null;
    
    loginError = null;
    
    QueryExample qe = new QueryExample(u);
    User found = dao.findOne(qe);
    
    if(found == null) {
      page = null;
      loginError = INVALID_LOGIN;
    } else {
      session.setAuthenticated(found.authenticate(u));
      
      if(session.isAuthenticated()) {
        session.setCurrentUser(found);
        page = Navigation.USER;
      }
    }
    
    System.out.println("Logged In: "+found);
    System.out.println(found.getBirthday());
    return page;
  }
  
  
  public String logout() {
    if(session == null) return null;
    
    session.setAuthenticated(false);
    session.setCurrentUser(new User());
    page = Navigation.HOME;
    return page;
  }
  
}
