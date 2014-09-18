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
import com.jpower.mongo4j.QueryBuilder;
import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;


/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 30/05/2012
 */
@ManagedBean
@RequestScoped
public class Login implements Serializable {
  
  public static final String SUCCESS_LOGIN_OUTCOME = "user.xhtml";
  
  public static final String LOGOUT_OUTCOME = "index.xhtml";
  
  public static final String INVALID_LOGIN = "E-mail/Senha inválidos";

  
  @ManagedProperty(value="#{sessionControl}")
  private SessionControl sessionControl;
  
  @ManagedProperty(value="#{user}")
  private User user;
  
  @ManagedProperty(value="#{dao}")
  private DAOProvider dao;
  
  public String error;

  
  public Login() {}
  
  
  public String doLogin() {
    if(user == null || dao == null)
      return null;
    QueryBuilder q = new QueryBuilder()
        .byClass(User.class)
        .equal("email", user.getEmail());
    System.out.println("# Login.doLogin(): email = "+ user.getEmail());
    User found = dao.get().findOne(q);
    if(found == null) {
      System.out.println("# Invalid User!");
      this.invalidLogin();
      return null;
    }
    if(found.authenticate(user)) {
      sessionControl.setUser(found);
      sessionControl.setLogged(true);
      return SUCCESS_LOGIN_OUTCOME;
    } else {
      System.out.println("# Invalid Password");
      this.invalidLogin();
      return null;
    }
  }
  
  
  private void invalidLogin() {
    FacesContext.getCurrentInstance()
        .addMessage(null, 
        new FacesMessage(FacesMessage.SEVERITY_WARN, INVALID_LOGIN, null));
  }
  
  
  public String doLogout() {
    if(user == null) return LOGOUT_OUTCOME;
    
    sessionControl.setLogged(false);
    return LOGOUT_OUTCOME;
  }
  
  
  public DAOProvider getDao() {
    return dao;
  }


  public void setDao(DAOProvider dao) {
    this.dao = dao;
  }


  public SessionControl getSessionControl() {
    return sessionControl;
  }


  public void setSessionControl(SessionControl sessionControl) {
    this.sessionControl = sessionControl;
  }


  public User getUser() {
    return user;
  }


  public void setUser(User user) {
    this.user = user;
  }
  
}
