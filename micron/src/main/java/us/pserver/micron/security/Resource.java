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
import java.util.Set;
import us.pserver.micron.security.api.IResource;
import us.pserver.micron.security.api.IRole;
import us.pserver.tools.Match;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 22/01/2019
 */
public class Resource implements IResource {
  
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
  
  
  @Override
  public String getName() {
    return name;
  }
  
  @Override
  public Instant getCreated() {
    return created;
  }
  
  @Override
  public Set<String> getRoles() {
    return roles;
  }
  
  @Override
  public boolean contains(IRole role) {
    return roles.contains(role.getName());
  }
  
  @Override
  public Builder edit() {
    return builder()
        .setName(name)
        .setCreated(created)
        .setRoles(roles);
  }
  
  
  
  public static Builder builder() {
    return new Builder();
  }
  
  
  
  
  
  public static class Builder implements IBuilder {

    private String name;
    
    private Instant created = Instant.now();
    
    private Set<String> groups = new HashSet<>();
    
    
    @Override
    public String getName() {
      return name;
    }
    
    @Override
    public Builder setName(String name) {
      this.name = name;
      return this;
    }
    
    
    @Override
    public Instant getCreated() {
      return created;
    }
    
    @Override
    public Builder setCreated(Instant created) {
      this.created = created;
      return this;
    }
    
    
    @Override
    public Set<String> getRoles() {
      return groups;
    }
    
    @Override
    public Builder setRoles(Set<String> usernames) {
      this.groups = new HashSet(usernames);
      return this;
    }
    
    @Override
    public Builder clearRoles() {
      this.groups.clear();
      return this;
    }
    
    @Override
    public Builder addRole(String roleName) {
      groups.add(Match.notEmpty(roleName).getOrFail("Bad null/empty role name"));
      return this;
    }
    
    @Override
    public Builder addRole(IRole role) {
      groups.add(Match.notNull(role).getOrFail("Bad null role").getName());
      return this;
    }
    
    
    @Override
    public Resource build() {
      return new Resource(name, groups, created);
    }
    
  }

}
