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

package us.pserver.micron.config.impl;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import us.pserver.micron.config.MicronConfig;
import us.pserver.micron.config.SecurityConfig;
import us.pserver.micron.security.Group;
import us.pserver.micron.security.Resource;
import us.pserver.micron.security.Role;
import us.pserver.micron.security.User;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 02/02/2019
 */
public class SecurityConfigBuilderImpl implements SecurityConfig.SecurityConfigBuilder {
  
  private MicronConfig.MicronConfigBuilder micron;
  
  private final Set<User> users;
  
  private final Set<Group> groups;
  
  private final Set<Role> roles;
  
  private final Set<Resource> resources;
  
  
  public SecurityConfigBuilderImpl(MicronConfig.MicronConfigBuilder micron) {
    this.micron = micron;
    this.users = new HashSet();
    this.groups = new HashSet();
    this.roles = new HashSet();
    this.resources = new HashSet();
  }
  
  
  public SecurityConfigBuilderImpl() {
    this(null);
  }
  
  
  @Override
  public Set<User> getUsers() {
    return users;
  }


  @Override
  public Set<Group> getGroups() {
    return groups;
  }


  @Override
  public Set<Role> getRoles() {
    return roles;
  }


  @Override
  public Set<Resource> getResources() {
    return resources;
  }


  @Override
  public SecurityConfig.SecurityConfigBuilder addUser(User... user) {
    users.addAll(Arrays.asList(user));
    return this;
  }


  @Override
  public SecurityConfig.SecurityConfigBuilder addGroup(Group... group) {
    groups.addAll(Arrays.asList(group));
    return this;
  }


  @Override
  public SecurityConfig.SecurityConfigBuilder addRole(Role... role) {
    roles.addAll(Arrays.asList(role));
    return this;
  }


  @Override
  public SecurityConfig.SecurityConfigBuilder addResource(Resource... resource) {
    resources.addAll(Arrays.asList(resource));
    return this;
  }


  @Override
  public SecurityConfig.SecurityConfigBuilder addUsers(Collection<User> users) {
    users.addAll(users);
    return this;
  }


  @Override
  public SecurityConfig.SecurityConfigBuilder addGroups(Collection<Group> groups) {
    groups.addAll(groups);
    return this;
  }


  @Override
  public SecurityConfig.SecurityConfigBuilder addRoles(Collection<Role> roles) {
    roles.addAll(roles);
    return this;
  }


  @Override
  public SecurityConfig.SecurityConfigBuilder addResources(Collection<Resource> resources) {
    resources.addAll(resources);
    return this;
  }


  @Override
  public SecurityConfig.SecurityConfigBuilder clearUsers() {
    users.clear();
    return this;
  }


  @Override
  public SecurityConfig.SecurityConfigBuilder clearGroups() {
    groups.clear();
    return this;
  }


  @Override
  public SecurityConfig.SecurityConfigBuilder clearRoles() {
    roles.clear();
    return this;
  }


  @Override
  public SecurityConfig.SecurityConfigBuilder clearResources() {
    resources.clear();
    return this;
  }


  @Override
  public SecurityConfig build() {
    return new SecurityConfigImpl(users, groups, roles, resources);
  }


  @Override
  public int hashCode() {
    int hash = 5;
    hash = 11 * hash + Objects.hashCode(this.users);
    hash = 11 * hash + Objects.hashCode(this.groups);
    hash = 11 * hash + Objects.hashCode(this.roles);
    hash = 11 * hash + Objects.hashCode(this.resources);
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
    if(getClass() != obj.getClass()) {
      return false;
    }
    final SecurityConfigBuilderImpl other = (SecurityConfigBuilderImpl) obj;
    if(!Objects.equals(this.users, other.users)) {
      return false;
    }
    if(!Objects.equals(this.groups, other.groups)) {
      return false;
    }
    if(!Objects.equals(this.roles, other.roles)) {
      return false;
    }
    if(!Objects.equals(this.resources, other.resources)) {
      return false;
    }
    return true;
  }


  @Override
  public String toString() {
    return "SecurityConfigBuilder{" + "users=" + users + ", groups=" + groups + ", roles=" + roles + ", resources=" + resources + '}';
  }


  @Override
  public MicronConfig.MicronConfigBuilder setBuild() {
    if(micron == null) {
      throw new IllegalStateException("SecurityConfigBuilder is not in MicronConfigBuilder context");
    }
    return micron.setSecurityConfig(build());
  }

}
