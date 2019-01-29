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

import java.util.List;
import java.util.Optional;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.cache.query.ScanQuery;
import us.pserver.micron.security.api.GroupApi;
import us.pserver.micron.security.api.ResourceApi;
import us.pserver.micron.security.api.RoleApi;
import us.pserver.micron.security.api.UserApi;
import us.pserver.tools.Match;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 25/01/2019
 */
public class Security {
  
  public static final String CACHE_USER = "security.user";
  
  public static final String CACHE_GROUP = "security.group";
  
  public static final String CACHE_ROLE = "security.role";
  
  public static final String CACHE_RESOURCE = "security.resource";
  
  
  private final Ignite ignite;
  
  
  public Security(Ignite ignite) {
    this.ignite = Match.notNull(ignite).getOrFail("Bad null Ignite");
  }
  
  
  public Ignite ignite() {
    return ignite;
  }
  
  
  public IgniteCache<String,UserApi> getUserCache() {
    return ignite.getOrCreateCache(CACHE_USER);
  }
  
  public IgniteCache<String,GroupApi> getGroupCache() {
    return ignite.getOrCreateCache(CACHE_GROUP);
  }
  
  public IgniteCache<String,RoleApi> getRoleCache() {
    return ignite.getOrCreateCache(CACHE_ROLE);
  }
  
  public IgniteCache<String,ResourceApi> getResourceCache() {
    return ignite.getOrCreateCache(CACHE_RESOURCE);
  }
  
  
  public List<User> getUserByEmail(String email) {
    return getUserCache().query(
        new ScanQuery<String,User>((n,u) -> u.getEmail().equals(email)),
        e -> e.getValue()
    ).getAll();
  }
  
  
  public Optional<UserApi> authenticateUser(String name, char[] password) {
    Optional<UserApi> opt = Optional.ofNullable(getUserCache().get(name));
    return opt.isPresent() && opt.get().authenticate(name, password) ? opt : Optional.empty();
  }
  
  
  public boolean authorize(String resource, UserApi user) {
    return authorize(getResourceCache().get(resource), user);
  }
  
  public boolean authorize(ResourceApi res, UserApi user) {
    //list roles from resource
    List<Role> roles = getRoleCache().query(
        new ScanQuery<String,Role>((s,r) -> res.containsRole(r)),
        e -> e.getValue()
    ).getAll();
    //list groups that contains the specified user
    List<Group> groups = getGroupCache().query(
        new ScanQuery<String,Group>((n,g) -> g.containsUser(user)),
        e -> e.getValue()
    ).getAll();
           //contains at least one allowed role with an user's group
    return roles.stream().anyMatch(r -> groups.stream().anyMatch(g -> r.containsGroup(g)) && r.isAllowed())
        //and do not contains NOT allowed roles with any user's group
        && roles.stream().noneMatch(r -> groups.stream().anyMatch(g -> r.containsGroup(g)) && !r.isAllowed());
  }
  
}
