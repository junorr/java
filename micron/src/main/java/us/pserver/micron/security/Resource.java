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
public class Resource {
  
  private final String name;
  
  private final Set<String> roles;
  
  private final Instant created;
  
  
  public Resource(String name, Set<String> roles, Instant created) {
    this.name = Match.notEmpty(name).getOrFail("Bad null/empty name");
    this.roles = Collections.unmodifiableSet(
        Match.notNull(roles).getOrFail("Bad null/empty roles")
    );
    this.created = Match.notNull(created).getOrFail("Bad null created");
  }
  
  public Resource(String name, Set<String> groups) {
    this(name, groups, Instant.now());
  }
  
  
  public String getName() {
    return name;
  }
  
  public Instant getCreated() {
    return created;
  }
  
  public Set<String> getRoles() {
    return roles;
  }
  
  public boolean contains(Role role) {
    return roles.contains(role.getName());
  }
  
  public ResourceBuilder edit() {
    return builder()
        .setName(name)
        .setCreated(created)
        .setRoles(roles);
  }


  @Override
  public int hashCode() {
    int hash = 3;
    hash = 71 * hash + Objects.hashCode(this.name);
    hash = 71 * hash + Objects.hashCode(this.roles);
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
    final Resource other = (Resource) obj;
    if (!Objects.equals(this.name, other.name)) {
      return false;
    }
    if (!Objects.equals(this.roles, other.roles)) {
      return false;
    }
    return true;
  }


  @Override
  public String toString() {
    return "Resource{" + "name=" + name + ", roles=" + roles + ", created=" + created + '}';
  }
  
  
  
  public static ResourceBuilder builder() {
    return new ResourceBuilder();
  }
  
  
  
  
  
  public static class ResourceBuilder {

    private String name;
    
    private Instant created;
    
    private Set<String> roles;
    
    
    public ResourceBuilder() {
      this.name = null;
      this.created = Instant.now();
      this.roles = new HashSet<>();
    }
    
    
    public String getName() {
      return name;
    }
    
    public ResourceBuilder setName(String name) {
      this.name = name;
      return this;
    }
    
    
    public Instant getCreated() {
      return created;
    }
    
    public ResourceBuilder setCreated(Instant created) {
      this.created = created;
      return this;
    }
    
    
    public Set<String> getRoles() {
      return roles;
    }
    
    public ResourceBuilder setRoles(Set<String> usernames) {
      this.roles = new HashSet(usernames);
      return this;
    }
    
    public ResourceBuilder clearRoles() {
      this.roles.clear();
      return this;
    }
    
    public ResourceBuilder addRole(String roleName) {
      roles.add(Match.notEmpty(roleName).getOrFail("Bad null/empty role name"));
      return this;
    }
    
    public ResourceBuilder addRole(Role role) {
      roles.add(Match.notNull(role).getOrFail("Bad null role").getName());
      return this;
    }
    
    
    public Resource build() {
      return new Resource(name, roles, created);
    }
    
  }

}
