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
import com.jpower.mongo4j.util.Utils;
import java.io.Serializable;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;


/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 13/06/2012
 */
@SessionScoped
@ManagedBean
public class EditUsers implements Serializable {
  
  @ManagedProperty(value="#{dao}")
  private DAOProvider dao;
  
  private User editing;
  
  private long editingID;
  
  private List<User> users;
  
  
  public EditUsers() {
    editing = null;
    editingID = 0;
  }
  
  
  public List<User> getUsers() {
    if(dao == null || dao.get() == null)
      return null;
    
    QueryBuilder q = new QueryBuilder()
        .byClass(User.class);
    users = dao.get().find(q);
    return users;
  }


  public DAOProvider getDao() {
    return dao;
  }


  public void setDao(DAOProvider dao) {
    this.dao = dao;
  }


  public User getEditing() {
    return editing;
  }


  public void setEditing(User ed) {
    this.editing = ed;
    if(editing != null)
      editingID = Utils.generateID(editing);
  }
  
  
  public String saveEditing() {
    if(editing == null)
      return null;
    
    if(editingID == 0) {
      dao.get().save(editing);
    } else {
      dao.get().update(editingID, editing);
    }
    
    return "adm-users.xhtml";
  }
  
  
  public String edit() {
    if(editing == null || editingID == 0)
      return null;
    return "edit-user.xhtml";
  }
  
  
  public String add() {
    editing = new User();
    editingID = 0;
    return "edit-user.xhtml";
  }
  
  
  public void delete() {
    if(editingID == 0)
      return;
    dao.get().delete(editingID, false);
    DynamicNavigation.go("adm-users.xhtml");
  }
  
}
