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

package us.pserver.micron.config;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.apache.ignite.Ignite;
import org.apache.ignite.lang.IgniteBiPredicate;
import us.pserver.micron.config.impl.IgniteSecurityConfig;
import us.pserver.micron.config.impl.SecurityConfigBuilderImpl;
import us.pserver.micron.security.Group;
import us.pserver.micron.security.Resource;
import us.pserver.micron.security.Role;
import us.pserver.micron.security.User;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 27/01/2019
 */
public interface SecurityConfig {

  public Set<User> getUsers();
  
  public List<User> findUser(IgniteBiPredicate<String,User> prd);
  
  public Optional<User> getUser(String name);
  
  public Set<Group> getGroups();
  
  public List<Group> findGroup(IgniteBiPredicate<String,Group> prd);
  
  public Optional<Group> getGroup(String name);
  
  public Set<Role> getRoles();
  
  public List<Role> findRole(IgniteBiPredicate<String,Role> prd);
  
  public Optional<Role> getRole(String name);
  
  public Set<Resource> getResources();
  
  public List<Resource> findResource(IgniteBiPredicate<String,Resource> prd);
  
  public Optional<Resource> getResource(String name);
  
  
  
  public static SecurityConfigBuilder builder() {
    return new SecurityConfigBuilderImpl();
  }
  
  
  public static SecurityConfig of(Ignite ignite) {
    return new IgniteSecurityConfig(ignite);
  }
  
  
  
  
  
  public interface SecurityConfigBuilder {
    
    public Set<User> getUsers();
    
    public Set<Group> getGroups();
    
    public Set<Role> getRoles();
    
    public Set<Resource> getResources();
    
    public SecurityConfigBuilder addUser(User ... user);
    
    public SecurityConfigBuilder addGroup(Group ... group);
    
    public SecurityConfigBuilder addRole(Role ... role);
    
    public SecurityConfigBuilder addResource(Resource ... resource);
    
    public SecurityConfigBuilder addUsers(Collection<User> users);
    
    public SecurityConfigBuilder addGroups(Collection<Group> groups);
    
    public SecurityConfigBuilder addRoles(Collection<Role> roles);
    
    public SecurityConfigBuilder addResources(Collection<Resource> resources);
    
    public SecurityConfigBuilder clearUsers();
    
    public SecurityConfigBuilder clearGroups();
    
    public SecurityConfigBuilder clearRoles();
    
    public SecurityConfigBuilder clearResources();
    
    public SecurityConfig build();
    
    public MicronConfig.MicronConfigBuilder setBuild();
    
  }
  
}
