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

import adv.roesler.talitah.Navigation;
import adv.roesler.talitah.bean.Processo;
import badraadv.User;
import com.jpower.mongo4j.GenericDAO;
import com.jpower.mongo4j.QueryBuilder;
import java.util.LinkedList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;


/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 26/01/2012
 */
@ManagedBean
@SessionScoped
public class AdminController {

  @ManagedProperty(value = "#{user}")
  private User user;
  
  private User adv;
  
  private List<User> allAdvs;
  
  private int advhash;
  
  private int prchash;
  
  private Processo editPrc;
  
  private List<Processo> prcs;
  
  private GenericDAO dao;
  
  
  public AdminController() {
    dao = new GenericDAO("adv");
    editPrc = null;
    prcs = new LinkedList<Processo>();
    allAdvs = new LinkedList<User>();
    prchash = 0;
    if(user != null && user.isAdv()) {
      adv = user;
    }
  }


  public User getAdv() {
    return adv;
  }
  
  
  public void setAdv(User a) {
    adv = a;
    System.out.println("AdminController.setAdv[ "+ adv+ " ]");
  }


  public List<User> getAllAdvs() {
    if(allAdvs == null || allAdvs.isEmpty()) {
      QueryBuilder q = new QueryBuilder();
      q.byClass(User.class).field("adv").equal(true);
      allAdvs = dao.find(q);
    }
    return allAdvs;
  }


  public Processo getEditPrc() {
    return editPrc;
  }
  
  
  private void updatePrcs(User a) {
    if(a == null || !a.isAdv()) return;
    QueryBuilder q = new QueryBuilder();
    q.byClass(Processo.class)
        .equal("advogado", a);
    prcs = dao.find(q);
  }


  public String setEditPrc(Processo editPrc) {
    if(editPrc != null)
      this.editPrc = editPrc;
    return Navigation.EDIT_PROCESS;
  }


  public List<Processo> getPrcs() {
    return prcs;
  }


  public void setPrcs(List<Processo> prcs) {
    if(prcs == null) prcs = new LinkedList<Processo>();
    this.prcs = prcs;
  }


  public User getUser() {
    return user;
  }


  public void setUser(User user) {
    this.user = user;
    if(user != null && user.isAdv()) {
      this.adv = user;
    }
  }
  
  
  public String salvar() {
    System.out.println(Navigation.ADMIN);
    return Navigation.ADMIN;
  }
  
}
