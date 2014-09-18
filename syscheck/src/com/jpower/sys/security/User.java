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


package com.jpower.sys.security;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @version 0.0 - 05/02/2013
 * @author Juno Roesler - juno.rr@gmail.com
 */
public class User implements Serializable {
  
  private String name;
  
  private String email;
  
  private boolean admin;
  
  private Password pass;
  
  private ArrayList<Access> access;
  
  
  public User() {
    name = null;
    email = null;
    admin = false;
    pass = null;
    access = new ArrayList<>();
    access.add(new Access(AccessType.USER));
  }
  
  
  public List<Access> getAccess() {
    return access;
  }
  
  
  public boolean contains(AccessType type) {
    if(access == null || access.isEmpty()
        || type == null)
      return false;
    for(Access a : access) {
      if(a.equals(type))
        return true;
    }
    return false;
  }
  
  
  public boolean remove(AccessType type) {
    if(access == null || access.isEmpty()
        || type == null)
      return false;
    Access ac = this.getAccess(type);
    return access.remove(ac);
  }
  
  
  public Access getAccess(AccessType type) {
    if(access == null || access.isEmpty()
        || type == null)
      return null;
    for(Access a : access) {
      if(a.getType() == type)
        return a;
    }
    return null;
  }
  
  
  public void clearAccess() {
    Access user = this.getAccess(AccessType.USER);
    access.clear();
    if(user != null) access.add(user);
  }
  
  
  @Override
  public User clone() {
    User u = new User();
    u.copy(this);
    return u;
  }
  
  
  public void copy(User another) {
    if(another == null) return;
    this.setAdmin(another.isAdmin());
    this.setEmail(another.getEmail());
    this.setName(another.getName());
    this.setPassword(another.getPassword());
    this.getAccess().clear();
    this.getAccess().addAll(another.getAccess());
  }


  public String getName() {
    return name;
  }


  public void setName(String name) {
    this.name = name;
  }


  public String getEmail() {
    return email;
  }


  public void setEmail(String email) {
    this.email = email;
  }


  public boolean isAdmin() {
    return admin;
  }


  public void setAdmin(boolean admin) {
    this.admin = admin;
  }


  public String getPass() {
    if(pass != null)
      return pass.getHash();
    return null;
  }
  
  
  public void setPass(String pass) {
    this.pass = new Password(pass);
  }


  public void setPassword(Password pass) {
    this.pass = pass;
  }
  
  
  public Password getPassword() {
    return pass;
  }


  @Override
  public int hashCode() {
    int hash = 5;
    hash = 29 * hash + Objects.hashCode(this.name);
    hash = 29 * hash + Objects.hashCode(this.email);
    hash = 29 * hash + (this.admin ? 1 : 0);
    return hash;
  }


  @Override
  public boolean equals(Object obj) {
    if(obj == null) {
      return false;
    }
    if(getClass() != obj.getClass()) {
      return false;
    }
    final User other = (User) obj;
    if(!Objects.equals(this.name, other.name)) {
      return false;
    }
    if(!Objects.equals(this.email, other.email)) {
      return false;
    }
    if(this.admin != other.admin) {
      return false;
    }
    return true;
  }


  @Override
  public String toString() {
    return "User{" + "name=" + name + ", email=" + email + ", admin=" + admin + '}';
  }
  
}
