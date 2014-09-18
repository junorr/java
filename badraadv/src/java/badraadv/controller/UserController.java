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
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;


/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 28/05/2012
 */
@RequestScoped
@ManagedBean
public class UserController implements Serializable {
  
  public static final String DEFAULT_SAVE_OUTCOME = "reg_success.xhtml";
  
  @ManagedProperty(value="#{sessionControl}")
  private SessionControl sessionControl;
  
  @ManagedProperty(value="#{user}")
  private User user;
  
  @ManagedProperty(value="#{dao}")
  private DAOProvider dao;
  
  private String saveOutcome;
  
  
  public UserController() {
    saveOutcome = DEFAULT_SAVE_OUTCOME;
  }

  
  public String save() {
    if(dao == null || dao.get() == null
        || user == null
        || !user.validate())
      return null;
    
    if(sessionControl != null
        && sessionControl.isLogged()
        && sessionControl.getUserID() != 0) {
      dao.get().update(sessionControl.getUserID(), user);
    }
    else if(!dao.get().save(user)) 
      return null;
    
    user = dao.get().findOne(user);
    
    sessionControl.setUser(user);
    sessionControl.setLogged(true);
    return saveOutcome;
  }
  
  
  public User getUser() {
    return user;
  }
  
  
  public void setUser(User u) {
    user = u;
  }
  
  
  public SessionControl getSessionControl() {
    return sessionControl;
  }
  
  
  public void setSessionControl(SessionControl s) {
    sessionControl = s;
  }
  
  
  public void setDao(DAOProvider d) {
    this.dao = d;
  }
  
  
  public DAOProvider getDao() {
    return dao;
  }
  
  
  public String getSaveOutcome() {
    return saveOutcome;
  }
  
  
  public void setSaveOutcome(String outcome) {
    saveOutcome = outcome;
  }
  
}
