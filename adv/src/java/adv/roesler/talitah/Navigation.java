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

import java.io.Serializable;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;


/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 21/12/2011
 */
@ManagedBean
@ApplicationScoped
public class Navigation implements Serializable {
  
  public static final String LINK_PREFIX = "faces/";
  
  public static final String 
      
      HOME = "index.xhtml",
  
      USER = "user.xhtml",
  
      LOGIN = "login.xhtml",
      
      REGISTRATION = "registration.xhtml",
      
      FORGET_PASS = "resetPass.xhtml",
      
      ADMIN = "admin.xhtml",
      
      EDIT_PROCESS = "process-edit.xhtml";

  
  private String linkHome, home;
  
  private String linkUser, user;
  
  private String linkLogin, login;
  
  private String linkRegistration, registration;
  
  private String linkForgetPass, forgetPass;
  
  private String linkAdmin, admin;
  
  private String linkEditProcess, editProcess;
  
  
  public Navigation() {
    home = HOME;
    user = USER;
    login = LOGIN;
    registration = REGISTRATION;
    forgetPass = FORGET_PASS;
    admin = ADMIN;
    editProcess = EDIT_PROCESS;
    
    linkAdmin = LINK_PREFIX + ADMIN;
    linkHome = LINK_PREFIX + HOME;
    linkUser = LINK_PREFIX + USER;
    linkLogin = LINK_PREFIX + LOGIN;
    linkRegistration = LINK_PREFIX + REGISTRATION;
    linkForgetPass = LINK_PREFIX + FORGET_PASS;
    linkEditProcess = LINK_PREFIX + EDIT_PROCESS;
  }


  public String getLinkHome() {
    return linkHome;
  }


  public String getLinkLogin() {
    return linkLogin;
  }


  public String getLinkUser() {
    return linkUser;
  }


  public String getLinkRegistration() {
    return linkRegistration;
  }


  public String getHome() {
    return home;
  }


  public String getLogin() {
    return login;
  }


  public String getRegistration() {
    return registration;
  }


  public String getUser() {
    return user;
  }


  public String getForgetPass() {
    return forgetPass;
  }


  public String getLinkForgetPass() {
    return linkForgetPass;
  }


  public String getAdmin() {
    return admin;
  }


  public String getLinkAdmin() {
    return linkAdmin;
  }


  public String getLinkEditProcess() {
    return linkEditProcess;
  }


  public String getEditProcess() {
    return editProcess;
  }

}
