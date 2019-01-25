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

package us.pserver.micron.security;

import java.time.Instant;
import java.util.Collection;
import java.util.Set;
import us.pserver.tools.Match;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 22/01/2019
 */
public class Role extends AbstractGroup {
  
  private final boolean allowed;
  
  
  public Role(String name, boolean allowed, Set<String> groups, Instant created) {
    super(name, groups, created);
    this.allowed = allowed;
  }
  
  public Role(String name, boolean allowed, Set<String> groups) {
    this(name, allowed, groups, Instant.now());
  }
  
  
  public boolean isAllowed() {
    return allowed;
  }
  
  public Set<String> getGroups() {
    return items;
  }
  
  public boolean containsGroup(Group group) {
    return items.contains(group.getName());
  }
  
  @Override
  public RoleBuilder edit() {
    return (RoleBuilder) builder()
        .setName(name)
        .setCreated(created)
        .setItems(items);
  }

  
  
  public static RoleBuilder builder() {
    return new RoleBuilder();
  }
  
  
  
  
  
  public static class RoleBuilder extends AbstractGroupBuilder<Role> {

    private boolean allowed;
    
    
    public RoleBuilder() {
      super();
    }
    
    
    public boolean isAllowed() {
      return allowed;
    }
    
    public RoleBuilder setAllowed(boolean allowed) {
      this.allowed = allowed;
      return this;
    }
    
    public RoleBuilder addGroups(Collection<Group> groups) {
      Match.notNull(groups).getOrFail("Bad null users Collection").forEach(this::addGroup);
      return this;
    }
    
    public RoleBuilder addGroup(Group group) {
      this.items.add(Match.notNull(group).getOrFail("Bad null User").getName());
      return this;
    }
    
    public RoleBuilder clearGroups() {
      this.items.clear();
      return this;
    }
    
    @Override
    public Role build() {
      return new Role(name, allowed, items, created);
    }
    
  }

}
