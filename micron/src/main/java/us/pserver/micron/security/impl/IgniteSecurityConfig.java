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

package us.pserver.micron.security.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.cache.query.ScanQuery;
import us.pserver.micron.config.SecurityConfig;
import us.pserver.micron.security.Group;
import us.pserver.micron.security.Resource;
import us.pserver.micron.security.Role;
import us.pserver.micron.security.User;
import us.pserver.tools.Match;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 01/02/2019
 */
public class IgniteSecurityConfig implements SecurityConfig {
  
  public static final String SECURITY_CACHE_USER = "security_user";
  
  public static final String SECURITY_CACHE_GROUP = "security_group";
  
  public static final String SECURITY_CACHE_ROLE = "security_role";
  
  public static final String SECURITY_CACHE_RESOURCE = "security_resource";
  
  
  private final Ignite ignite;
  
  
  public IgniteSecurityConfig(Ignite ignite) {
    this.ignite = Match.notNull(ignite).getOrFail("Bad null Ignite");
  }
  
  
  @Override
  public Set<User> getUsers() {
    IgniteCache<String,User> cache = ignite.getOrCreateCache(SECURITY_CACHE_USER);
    Set<User> set = new HashSet();
    cache.iterator().forEachRemaining(e -> set.add(e.getValue()));
    return set;
  }
  
  
  @Override
  public List<User> findUser(Predicate<User> prd) {
    IgniteCache<String,User> cache = ignite.getOrCreateCache(SECURITY_CACHE_USER);
    ScanQuery<String,User> qry = new ScanQuery<>((s,u) -> prd.test(u));
    return cache.query(qry, e -> e.getValue()).getAll();
  }


  @Override
  public Optional<User> getUser(String name) {
    IgniteCache<String,User> cache = ignite.getOrCreateCache(SECURITY_CACHE_USER);
    return Optional.ofNullable(cache.get(name));
  }


  @Override
  public Set<Group> getGroups() {
    IgniteCache<String,Group> cache = ignite.getOrCreateCache(SECURITY_CACHE_GROUP);
    Set<Group> set = new HashSet();
    cache.iterator().forEachRemaining(e -> set.add(e.getValue()));
    return set;
  }


  @Override
  public List<Group> findGroup(Predicate<Group> prd) {
    IgniteCache<String,Group> cache = ignite.getOrCreateCache(SECURITY_CACHE_GROUP);
    ScanQuery<String,Group> qry = new ScanQuery<>((s,g) -> prd.test(g));
    return cache.query(qry, e -> e.getValue()).getAll();
  }


  @Override
  public Optional<Group> getGroup(String name) {
    IgniteCache<String,Group> cache = ignite.getOrCreateCache(SECURITY_CACHE_GROUP);
    return Optional.ofNullable(cache.get(name));
  }


  @Override
  public Set<Role> getRoles() {
    IgniteCache<String,Role> cache = ignite.getOrCreateCache(SECURITY_CACHE_ROLE);
    Set<Role> set = new HashSet();
    cache.iterator().forEachRemaining(e -> set.add(e.getValue()));
    return set;
  }


  @Override
  public List<Role> findRole(Predicate<Role> prd) {
    IgniteCache<String,Role> cache = ignite.getOrCreateCache(SECURITY_CACHE_USER);
    ScanQuery<String,Role> qry = new ScanQuery<>((s,r) -> prd.test(r));
    return cache.query(qry, e -> e.getValue()).getAll();
  }


  @Override
  public Optional<Role> getRole(String name) {
    IgniteCache<String,Role> cache = ignite.getOrCreateCache(SECURITY_CACHE_ROLE);
    return Optional.ofNullable(cache.get(name));
  }


  @Override
  public Set<Resource> getResources() {
    IgniteCache<String,Resource> cache = ignite.getOrCreateCache(SECURITY_CACHE_RESOURCE);
    Set<Resource> set = new HashSet();
    cache.iterator().forEachRemaining(e -> set.add(e.getValue()));
    return set;
  }
  

  @Override
  public List<Resource> findResource(Predicate<Resource> prd) {
    IgniteCache<String,Resource> cache = ignite.getOrCreateCache(SECURITY_CACHE_RESOURCE);
    ScanQuery<String,Resource> qry = new ScanQuery<>((s,r) -> prd.test(r));
    return cache.query(qry, e -> e.getValue()).getAll();
  }

  @Override
  public Optional<Resource> getResource(String name) {
    IgniteCache<String,Resource> cache = ignite.getOrCreateCache(SECURITY_CACHE_RESOURCE);
    return Optional.ofNullable(cache.get(name));
  }


}
