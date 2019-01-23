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
import us.pserver.micron.security.api.IGroup;
import us.pserver.micron.security.api.IRole;
import us.pserver.tools.Match;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 22/01/2019
 */
public class Role implements IRole {
  
  private final String name;
  
  private final Set<String> groups;
  
  private final Instant created;
  
  
  public Role(String name, Set<String> groups, Instant created) {
    this.name = Match.notEmpty(name).getOrFail("Bad null/empty name");
    this.groups = Collections.unmodifiableSet(
        Match.notNull(groups).getOrFail("Bad null/empty groups")
    );
    this.created = Match.notNull(created).getOrFail("Bad null created");
  }
  
  public Role(String name, Set<String> groups) {
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
  public Set<String> getGroups() {
    return groups;
  }
  
  @Override
  public boolean contains(IGroup group) {
    return groups.contains(group.getName());
  }
  
  @Override
  public Builder edit() {
    return builder()
        .setName(name)
        .setCreated(created)
        .setGroups(groups);
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
    public Set<String> getGroups() {
      return groups;
    }
    
    @Override
    public Builder setGroups(Set<String> usernames) {
      this.groups = new HashSet(usernames);
      return this;
    }
    
    @Override
    public Builder clearGroups() {
      this.groups.clear();
      return this;
    }
    
    @Override
    public Builder addGroupName(String groupName) {
      groups.add(Match.notEmpty(groupName).getOrFail("Bad null/empty group name"));
      return this;
    }
    
    @Override
    public Builder addGroup(IGroup group) {
      groups.add(Match.notNull(group).getOrFail("Bad null user").getName());
      return this;
    }
    
    
    @Override
    public Role build() {
      return new Role(name, groups, created);
    }
    
  }

}
