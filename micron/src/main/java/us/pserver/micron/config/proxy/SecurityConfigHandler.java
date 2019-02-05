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

package us.pserver.micron.config.proxy;

import io.helidon.config.Config;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.apache.ignite.lang.IgniteBiPredicate;
import us.pserver.micron.config.SecurityConfig;
import us.pserver.micron.security.Group;
import us.pserver.micron.security.Resource;
import us.pserver.micron.security.Role;
import us.pserver.tools.Match;
import us.pserver.micron.security.User;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 27/01/2019
 */
public class SecurityConfigHandler implements InvocationHandler, SecurityConfig {
  
  private final Config cfg;
  
  public SecurityConfigHandler(Config cfg) {
    this.cfg = Match.notNull(cfg).getOrFail("Bad null Config");
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    switch(method.getName()) {
      case "getUsers":
        return getUsers();
      case "findUser":
        return findUser((IgniteBiPredicate<String,User>) args[0]);
      case "getUser":
        return getUser((String) args[0]);
      case "getGroups":
        return getGroups();
      case "findGroup":
        return findGroup((IgniteBiPredicate<String,Group>) args[0]);
      case "getGroup":
        return getGroup((String) args[0]);
      case "getRoles":
        return getRoles();
      case "findRole":
        return findRole((IgniteBiPredicate<String,Role>) args[0]);
      case "getRole":
        return getRole((String) args[0]);
      case "getResources":
        return getResources();
      case "findResource":
        return findResource((IgniteBiPredicate<String,Resource>) args[0]);
      case "getResource":
        return getResource((String) args[0]);
      case "hashCode":
        return hashCode();
      case "equals":
        return equals(args[0]);
      case "toString":
        return toString();
      default:
        throw new UnsupportedOperationException("Unknown method: " + getMethodString(method, args));
    }
  }
    
    
  private String getMethodString(Method meth, Object[] args) {
    StringBuilder msg = new StringBuilder(meth.getName())
        .append("( ");
    if(args == null || args.length < 1) {
      return msg.append(")").toString();
    }
    for(int i = 0; i < args.length; i++) {
      msg.append(args[i].getClass().getSimpleName()).append(", ");
    }
    return msg.delete(msg.length() - 2, msg.length()).append(" )").toString();
  }
  
  
  @Override
  public Set<User> getUsers() {
    List<Config> lst = cfg.get("users").asNodeList().orElse(Collections.EMPTY_LIST);
    if(lst.isEmpty()) return Collections.EMPTY_SET;
    Set<User> set = new HashSet();
    for(Config c : lst) {
      set.add((User) Proxy.newProxyInstance(
          User.class.getClassLoader(), 
          new Class[]{User.class}, 
          new UserConfigHandler(c))
      );
    }
    return set;
  }
  
  
  @Override
  public List<User> findUser(IgniteBiPredicate<String,User> prd) {
    return getUsers().stream().filter(u -> prd.apply(u.getName(), u)).collect(Collectors.toList());
  }
  
  
  @Override
  public Optional<User> getUser(String name) {
    return getUsers().stream().filter(u -> u.getName().equals(name)).findAny();
  }
  
  
  @Override
  public Set<Group> getGroups() {
    List<Config> lst = cfg.get("groups").asNodeList().orElse(Collections.EMPTY_LIST);
    if(lst.isEmpty()) return Collections.EMPTY_SET;
    Set<Group> set = new HashSet();
    for(Config c : lst) {
      set.add((Group) Proxy.newProxyInstance(
          Group.class.getClassLoader(), 
          new Class[]{Group.class}, 
          new GroupConfigHandler(c))
      );
    }
    return set;
  }
  
  
  @Override
  public List<Group> findGroup(IgniteBiPredicate<String,Group> prd) {
    return getGroups().stream().filter(g -> prd.apply(g.getName(), g)).collect(Collectors.toList());
  }
  
  
  @Override
  public Optional<Group> getGroup(String name) {
    return getGroups().stream().filter(g -> g.getName().equals(name)).findAny();
  }
  
  
  @Override
  public Set<Role> getRoles() {
    List<Config> lst = cfg.get("roles").asNodeList().orElse(Collections.EMPTY_LIST);
    if(lst.isEmpty()) return Collections.EMPTY_SET;
    Set<Role> set = new HashSet();
    for(Config c : lst) {
      set.add((Role) Proxy.newProxyInstance(
          Role.class.getClassLoader(), 
          new Class[]{Role.class}, 
          new RoleConfigHandler(c))
      );
    }
    return set;
  }
  
  
  @Override
  public List<Role> findRole(IgniteBiPredicate<String,Role> prd) {
    return getRoles().stream().filter(r -> prd.apply(r.getName(), r)).collect(Collectors.toList());
  }
  
  
  @Override
  public Optional<Role> getRole(String name) {
    return getRoles().stream().filter(r -> r.getName().equals(name)).findAny();
  }
  
  
  @Override
  public Set<Resource> getResources() {
    List<Config> lst = cfg.get("resources").asNodeList().orElse(Collections.EMPTY_LIST);
    if(lst.isEmpty()) return Collections.EMPTY_SET;
    Set<Resource> set = new HashSet();
    for(Config c : lst) {
      set.add((Resource) Proxy.newProxyInstance(
          Resource.class.getClassLoader(), 
          new Class[]{Resource.class}, 
          new ResourceConfigHandler(c))
      );
    }
    return set;
  }
  
  
  @Override
  public List<Resource> findResource(IgniteBiPredicate<String,Resource> prd) {
    return getResources().stream().filter(r -> prd.apply(r.getName(), r)).collect(Collectors.toList());
  }
  
  
  @Override
  public Optional<Resource> getResource(String name) {
    return getResources().stream().filter(r -> r.getName().equals(name)).findAny();
  }
  
  
  @Override
  public int hashCode() {
    int hash = 3;
    hash = 47 * hash + Objects.hashCode(this.getUsers());
    hash = 47 * hash + Objects.hashCode(this.getGroups());
    hash = 47 * hash + Objects.hashCode(this.getRoles());
    hash = 47 * hash + Objects.hashCode(this.getResources());
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
    if (!SecurityConfig.class.isAssignableFrom(obj.getClass())) {
      return false;
    }
    final SecurityConfig other = (SecurityConfig) obj;
    if (!Objects.equals(this.getUsers(), other.getUsers())) {
      return false;
    }
    if (!Objects.equals(this.getGroups(), other.getGroups())) {
      return false;
    }
    if (!Objects.equals(this.getRoles(), other.getRoles())) {
      return false;
    }
    return Objects.equals(this.getResources(), other.getResources());
  }
  
  @Override
  public String toString() {
    return "SecurityConfig{ users=" + getUsers()+ ", groups=" + getGroups() + ", roles=" + getRoles() + ", resources=" + getResources()+ " }";
  }
  
}
