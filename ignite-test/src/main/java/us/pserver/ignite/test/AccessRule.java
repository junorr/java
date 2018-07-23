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

package us.pserver.ignite.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 22/07/2018
 */
public class AccessRule {

  private Group group;
  
  private int perms;
  
  
  public AccessRule(Group group, int perms) {
    this.group = group;
    this.perms = perms;
  }
  
  public AccessRule(Group group, String posixPerms) {
    this(group, Permission.toPosixBin(Permission.fromPosixString(posixPerms)));
  }
  
  public AccessRule() {
  }
  
  public Group getGroup() {
    return group;
  }
  
  public void setGroup(Group group) {
    this.group = group;
  }
  
  public int getPerms() {
    return perms;
  }
  
  public boolean hasPermission(User usr, Permission prm) {
    return hasPermission(usr.getName(), prm);
  } 
  
  public boolean hasPermission(String username, Permission prm) {
    return Permission.NOPERM == prm
        || (group.conteinsUser(username) 
        && getEnumPerms().contains(prm));
  } 
  
  public Set<Permission> getEnumPerms() {
    return Permission.fromPosixBin(perms);
  }
  
  public Permission[] getEnumPermsArray() {
    List<Permission> ls = new ArrayList<>(getEnumPerms());
    return ls.toArray(new Permission[ls.size()]);
  }
  
  public String getPosixStringPerms() {
    return Permission.toPosixString(getEnumPerms());
  }
  
  public String getPosixCodeStringPerms() {
    Permission[] ps = getEnumPermsArray();
    return String.format("%d%d%d", ps[0], ps[1], ps[2]);
  }
  
  public AccessRule setPerms(int perms) {
    this.perms = perms;
    return this;
  }
  
  @Override
  public int hashCode() {
    int hash = 7;
    hash = 29 * hash + Objects.hashCode(this.group);
    return hash;
  }
  
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final AccessRule other = (AccessRule) obj;
    if (!Objects.equals(this.group, other.group)) {
      return false;
    }
    return true;
  }
  
  @Override
  public String toString() {
    return "AccessRule{" + "group=" + group + ", perms=" + getPosixStringPerms() + '}';
  }
  
}
