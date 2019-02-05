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

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.ignite.lang.IgniteBiPredicate;
import us.pserver.micron.config.SecurityConfig;
import us.pserver.micron.security.Group;
import us.pserver.micron.security.Resource;
import us.pserver.micron.security.Role;
import us.pserver.micron.security.User;
import us.pserver.tools.Match;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 02/02/2019
 */
public class SecurityConfigImpl implements SecurityConfig {
  
  private final Set<User> users;
  
  private final Set<Group> groups;
  
  private final Set<Role> roles;
  
  private final Set<Resource> resources;
  
  public SecurityConfigImpl(Set<User> users, Set<Group> groups, Set<Role> roles, Set<Resource> resources) {
    this.users = Collections.unmodifiableSet(Match.notEmpty(users).getOrFail("Bad null/empty Set<User> users"));
    this.groups = Collections.unmodifiableSet(Match.notEmpty(groups).getOrFail("Bad null/empty Set<Group> groups"));
    this.roles = Collections.unmodifiableSet(Match.notEmpty(roles).getOrFail("Bad null/empty Set<Role> roles"));
    this.resources = Collections.unmodifiableSet(Match.notEmpty(resources).getOrFail("Bad null/empty Set<Resource> resources"));
  }
  
  @Override
  public Set<User> getUsers() {
    return users;
  }


  @Override
  public List<User> findUser(IgniteBiPredicate<String,User> prd) {
    return users.stream().filter(u -> prd.apply(u.getName(), u)).collect(Collectors.toList());
  }


  @Override
  public Optional<User> getUser(String name) {
    return users.stream().filter(u -> u.getName().equals(name)).findAny();
  }


  @Override
  public Set<Group> getGroups() {
    return groups;
  }


  @Override
  public List<Group> findGroup(IgniteBiPredicate<String,Group> prd) {
    return groups.stream().filter(g -> prd.apply(g.getName(), g)).collect(Collectors.toList());
  }


  @Override
  public Optional<Group> getGroup(String name) {
    return groups.stream().filter(u -> u.getName().equals(name)).findAny();
  }


  @Override
  public Set<Role> getRoles() {
    return roles;
  }


  @Override
  public List<Role> findRole(IgniteBiPredicate<String,Role> prd) {
    return roles.stream().filter(r -> prd.apply(r.getName(), r)).collect(Collectors.toList());
  }


  @Override
  public Optional<Role> getRole(String name) {
    return roles.stream().filter(u -> u.getName().equals(name)).findAny();
  }


  @Override
  public Set<Resource> getResources() {
    return resources;
  }


  @Override
  public List<Resource> findResource(IgniteBiPredicate<String,Resource> prd) {
    return resources.stream().filter(r -> prd.apply(r.getName(), r)).collect(Collectors.toList());
  }


  @Override
  public Optional<Resource> getResource(String name) {
    return resources.stream().filter(u -> u.getName().equals(name)).findAny();
  }


  @Override
  public int hashCode() {
    int hash = 7;
    hash = 13 * hash + Objects.hashCode(this.users);
    hash = 13 * hash + Objects.hashCode(this.groups);
    hash = 13 * hash + Objects.hashCode(this.roles);
    hash = 13 * hash + Objects.hashCode(this.resources);
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
    final SecurityConfigImpl other = (SecurityConfigImpl) obj;
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
    return "SecurityConfig{" + "users=" + users + ", groups=" + groups + ", roles=" + roles + ", resources=" + resources + '}';
  }
  
}
