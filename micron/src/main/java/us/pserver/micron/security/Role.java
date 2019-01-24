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
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import us.pserver.tools.Match;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 22/01/2019
 */
public class Role {
  
  private final String name;
  
  private final boolean allowed;
  
  private final Set<String> groups;
  
  private final Instant created;
  
  
  public Role(String name, boolean allowed, Set<String> groups, Instant created) {
    this.name = Match.notEmpty(name).getOrFail("Bad null/empty name");
    this.allowed = allowed;
    this.groups = Collections.unmodifiableSet(
        Match.notNull(groups).getOrFail("Bad null/empty groups")
    );
    this.created = Match.notNull(created).getOrFail("Bad null created");
  }
  
  public Role(String name, boolean allowed, Set<String> groups) {
    this(name, allowed, groups, Instant.now());
  }
  
  
  public String getName() {
    return name;
  }
  
  public boolean isAllowed() {
    return allowed;
  }
  
  public Instant getCreated() {
    return created;
  }
  
  public Set<String> getGroups() {
    return groups;
  }
  
  public boolean contains(Group group) {
    return groups.contains(group.getName());
  }
  
  public RoleBuilder edit() {
    return builder()
        .setName(name)
        .setCreated(created)
        .setGroups(groups);
  }


  @Override
  public int hashCode() {
    int hash = 3;
    hash = 71 * hash + Objects.hashCode(this.name);
    hash = 71 * hash + (this.allowed ? 1 : 0);
    hash = 71 * hash + Objects.hashCode(this.groups);
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
    final Role other = (Role) obj;
    if (this.allowed != other.allowed) {
      return false;
    }
    if (!Objects.equals(this.name, other.name)) {
      return false;
    }
    if (!Objects.equals(this.groups, other.groups)) {
      return false;
    }
    return true;
  }


  @Override
  public String toString() {
    return "Role{" + "name=" + name + ", allowed=" + allowed + ", groups=" + groups + ", created=" + created + '}';
  }
  
  
  
  public static RoleBuilder builder() {
    return new RoleBuilder();
  }
  
  
  
  
  
  public static class RoleBuilder {

    private String name;
    
    private boolean allowed;
    
    private Instant created = Instant.now();
    
    private Set<String> groups = new HashSet<>();
    
    
    public String getName() {
      return name;
    }
    
    public RoleBuilder setName(String name) {
      this.name = name;
      return this;
    }
    
    
    public boolean isAllowed() {
      return allowed;
    }
    
    public RoleBuilder setAllowed(boolean allowed) {
      this.allowed = allowed;
      return this;
    }
    
    
    public Instant getCreated() {
      return created;
    }
    
    public RoleBuilder setCreated(Instant created) {
      this.created = created;
      return this;
    }
    
    
    public Set<String> getGroups() {
      return groups;
    }
    
    public RoleBuilder setGroups(Set<String> usernames) {
      this.groups = new HashSet(usernames);
      return this;
    }
    
    public RoleBuilder clearGroups() {
      this.groups.clear();
      return this;
    }
    
    public RoleBuilder addGroupName(String groupName) {
      groups.add(Match.notEmpty(groupName).getOrFail("Bad null/empty group name"));
      return this;
    }
    
    public RoleBuilder addGroup(Group group) {
      groups.add(Match.notNull(group).getOrFail("Bad null user").getName());
      return this;
    }
    
    
    public Role build() {
      return new Role(name, allowed, groups, created);
    }
    
  }

}
