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

package us.pserver.micro.security.impl;

import java.time.Instant;
import java.util.Set;
import us.pserver.micro.security.Role;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 22/01/2019
 */
public class RoleImpl extends AbstractNamedSet implements Role {
  
  private final boolean allowed;
  
  
  public RoleImpl(String name, boolean allowed, Set<String> groups, Instant created) {
    super(name, groups, created);
    this.allowed = allowed;
  }
  
  public RoleImpl(String name, boolean allowed, Set<String> groups) {
    this(name, allowed, groups, Instant.now());
  }
  
  
  @Override
  public boolean isAllowed() {
    return allowed;
  }
  
  @Override
  public RoleBuilder edit() {
    return new RoleBuilderImpl()
        .setName(name)
        .setCreated(created)
        .addItems(items);
  }
  
  @Override
  public int hashCode() {
    int hash = super.hashCode();
    hash = 71 * hash + (this.allowed ? 1 : 0);
    return hash;
  }
  
  @Override
  public boolean equals(Object obj) {
    if(this == obj) {
      return true;
    }
    if(obj == null) {
      return false;
    }
    if(!super.equals(obj)) {
      return false;
    }
    if(!Role.class.isAssignableFrom(obj.getClass())) {
      return false;
    }
    final Role other = (Role) obj;
    return this.isAllowed() == other.isAllowed();
  }
  
  @Override
  public String toString() {
    return name + "{ items=" + items + ", allowed=" + allowed + ", created=" + created + " }";
  }
  
}
