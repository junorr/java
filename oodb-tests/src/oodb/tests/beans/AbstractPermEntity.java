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

package oodb.tests.beans;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 12/12/2016
 */
public abstract class AbstractPermEntity implements PermEntity {

  protected Set<Permission> perms;
  
  
  protected AbstractPermEntity() {
    perms = new HashSet<>();
  }
  
  
  protected AbstractPermEntity(Set<Permission> prs) {
    Objects.requireNonNull(prs, "Bad Null Permission Set");
    this.perms = prs;
  }


  @Override
  public Set<Permission> getPermissions() {
    return perms;
  }
  
  
  public AbstractPermEntity add(Permission p) {
    if(p != null) perms.add(p);
    return this;
  }
  
  
  public AbstractPermEntity add(Set<Permission> prs) {
    if(prs != null && !prs.isEmpty()) {
      perms.addAll(prs);
    }
    return this;
  }
  
  
  @Override
  public boolean equals(Object other) {
    if(other instanceof PermEntity) {
      PermEntity e = (PermEntity) other;
      return Objects.equals(this.getType(), e.getType());
    }
    return false;
  }
  
  
  @Override
  public int hashCode() {
    return Objects.hash(this.getType());
  }
  
  
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("PermissionEntity{ ")
        .append(this.getType())
        .append(", [");
    Iterator<Permission> it = perms.iterator();
    while(it.hasNext()) {
      sb.append(it.next()).append(",");
    }
    return sb.deleteCharAt(sb.length() -1)
        .append("] }").toString();
  }
  
}
