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
import badraadv.User;
import com.jpower.mongo4j.GenericDAO;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;


/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 26/12/2011
 */
@ManagedBean
@ViewScoped
public class RegisterController implements Serializable {
  
  @ManagedProperty(value="#{navSession}")
  private NavSession session;
  
  private GenericDAO dao;
  
  private boolean registerOK;
  
  
  public RegisterController() {
    dao = new GenericDAO("adv");
    registerOK = false;
  }


  public boolean isRegisterOK() {
    if(registerOK) {
      registerOK = false;
      return !registerOK;
    }
    return registerOK;
  }
  
  
  public NavSession getSession() {
    return session;
  }
  
  
  public void setSession(NavSession s) {
    session = s;
  }
  
  
  public String save(User user) {
    if(user == null || session == null || !user.validate()) {
      registerOK = false;
      return null;
    }
    
    dao.save(user);
    session.setAuthenticated(true);
    session.setCurrentUser(user);
    registerOK = true;
    return null;
  }
  
}
