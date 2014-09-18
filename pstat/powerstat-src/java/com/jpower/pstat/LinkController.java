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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jpower.pstat;

import com.jpower.sys.security.AccessType;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 10/02/2013
 */
@ManagedBean
@SessionScoped
public class LinkController implements Serializable {

  public static final String
      
      LINK = "link-",
      
      APPS = "apps",
      
      LINK_INDEX = "index",
      
      LINK_DOWNLOAD = "download",
      
      LINK_ABOUT = "about",
      
      LINK_INSTALL_GUIDE = "install-guide",
      
      LINK_CONFIG = "config",
      
      LINK_CPU = "cpu",
      
      LINK_CPU_CHART = "custom-cpu-chart",
      
      LINK_DISK_CHART = "custom-disk-chart",
      
      LINK_MEM_CHART = "custom-mem-chart",
      
      LINK_NET_CHART = "custom-net-chart",
      
      LINK_DISK = "disk",
      
      LINK_LOGIN = "login",
      
      LINK_MEMORY = "memory",
      
      LINK_NETWORK = "network",
      
      LINK_NOTIFICATION = "notification",
      
      LINK_PROCESSES = "processes",
      
      LINK_TRAFFIC = "traffic",
      
      LINK_TERM = "term",
      
      LINK_USER = "user-edit",
      
      PROCESS_KILL = "process-kill",
      
      CLEAR_MEM_CACHE = "clear-mem-cache",
      
      ADMIN = "administrator",
      
      XHTML = ".xhtml";
      
      
  private String selectedLink;
  
  private boolean canShowCurrentPage;
  
  
  @ManagedProperty(value="#{loginController}")
  private LoginController login;
  
  
  public LinkController() {
    selectedLink = LINK + LINK_INDEX;
    canShowCurrentPage = true;
  }
  
  
  public String getSelectedLink() {
    String curr = this.getCurrentPage();
    if(curr != null && !selectedLink.equals(curr)) {
      switch(curr) {
        case LINK_LOGIN:
          selectedLink = LINK + LINK_LOGIN;
          break;
        case LINK_INDEX:
          selectedLink = LINK + LINK_INDEX;
          break;
        case LINK_DOWNLOAD:
          selectedLink = LINK + LINK_ABOUT;
          break;
        case LINK_INSTALL_GUIDE:
          selectedLink = LINK + LINK_ABOUT;
          break;
        default:
          selectedLink = LINK + APPS;
          break;
      }
    }
    return selectedLink;
  }
  
  
  public void setSelectedLink(String s) {
    selectedLink = s;
  }


  public LoginController getLogin() {
    return login;
  }


  public void setLogin(LoginController login) {
    this.login = login;
  }
  
  
  public String getCurrentPage() {
    HttpServletRequest req = (HttpServletRequest) FacesContext
        .getCurrentInstance().getExternalContext().getRequest();
    String uri = req.getRequestURI();
    int bar = uri.lastIndexOf("/");
    int ie = uri.indexOf(".xhtml");
    try {
      return uri.substring(bar + 1, ie);
    } catch(Exception e) {
      return null;
    }
  }
  
  
  public boolean getCanShowCurrentPage() {
    String page = this.getCurrentPage();
    canShowCurrentPage = this.canPerformAction(page);
    return canShowCurrentPage;
  }
  
  
  public boolean canPerformAction(String action) {
    if(action == null || action.isEmpty()
        || login == null)
      return false;
    
    if(!login.isLogged() || login.getUser() == null) 
      return false;
    
    if(login.getUser().contains(AccessType.ALL))
      return true;
    
    AccessType type = null;
    boolean allow = false;
    switch(action) {
      case LINK_CONFIG:
        allow = login.getUser().isAdmin();
        break;
      case LINK_CPU:
        type = AccessType.CPU;
        break;
      case LINK_CPU_CHART:
        type = AccessType.CHART_CPU;
        break;
      case LINK_DISK:
        type = AccessType.DISK;
        break;
      case LINK_DISK_CHART:
        type = AccessType.CHART_DISK;
        break;
      case LINK_INDEX:
      case LINK_DOWNLOAD:
      case LINK_LOGIN:
        allow = true;
        break;
      case LINK_MEMORY:
        type = AccessType.MEMORY;
        break;
      case LINK_MEM_CHART:
        type = AccessType.CHART_MEMORY;
        break;
      case LINK_NETWORK:
        type = AccessType.NETWORK;
        break;
      case LINK_NET_CHART:
        type = AccessType.CHART_NETWORK;
        break;
      case LINK_NOTIFICATION:
        type = AccessType.NOTIFICATION;
        break;
      case LINK_PROCESSES:
        type = AccessType.PROCESSES;
        break;
      case LINK_TERM:
        allow = login.getUser().isAdmin();
        break;
      case LINK_TRAFFIC:
        type = AccessType.TRAFFIC;
        break;
      case LINK_USER:
        type = AccessType.USER;
        break;
      case PROCESS_KILL:
        type = AccessType.PROCESS_KILL;
        break;
      case CLEAR_MEM_CACHE:
        type = AccessType.MEMORY_CLEAR;
        break;
      case ADMIN:
        allow = login.getUser().isAdmin();
        break;
    }
    
    if(allow) return allow;
    else if(type != null) {
      return login.getUser().contains(type);
    }
    else return false;
  }
  
}
