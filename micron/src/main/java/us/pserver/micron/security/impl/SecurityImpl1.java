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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.ignite.cache.query.ScanQuery;
import us.pserver.micron.config.SecurityConfig;
import us.pserver.tools.Match;
import us.pserver.micron.security.User;
import us.pserver.micron.security.Resource;
import us.pserver.micron.security.Security;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 25/01/2019
 */
public class SecurityImpl1 implements Security {
  
  private final SecurityConfig cfg;
  
  
  public SecurityImpl1(SecurityConfig cfg) {
    this.cfg = Match.notNull(cfg).getOrFail("Bad null SecurityConfig");
  }
  
  
  @Override
  public List<User> findUserByEmail(String email) {
    return cfg.getUsers()
        .stream()
        .filter(u -> u.getEmail().equals(email))
        .collect(Collectors.toList());
    return getUserCache().query(
        new ScanQuery<String,User>((n,u) -> u.getEmail().equals(email)),
        e -> e.getValue()
    ).getAll();
  }
  
  
  public List<User> getUserByEmail(String email) {
    return getUserCache().query(
        new ScanQuery<String,User>((n,u) -> u.getEmail().equals(email)),
        e -> e.getValue()
    ).getAll();
  }
  
  
  @Override
  public Optional<User> authenticateUser(String name, char[] password) {
    Optional<User> opt = Optional.ofNullable(getUserCache().get(name));
    return opt.isPresent() && opt.get().authenticate(name, password) ? opt : Optional.empty();
  }
  
  
  @Override
  public boolean authorize(String resource, User user) {
    return authorize(getResourceCache().get(resource), user);
  }
  
  @Override
  public boolean authorize(Resource res, User user) {
    //list roles from resource
    List<RoleImpl> roles = getRoleCache().query(
        new ScanQuery<String,RoleImpl>((s,r) -> res.containsRole(r)),
        e -> e.getValue()
    ).getAll();
    //list groups that contains the specified user
    List<GroupImpl> groups = getGroupCache().query(
        new ScanQuery<String,GroupImpl>((n,g) -> g.containsUser(user)),
        e -> e.getValue()
    ).getAll();
           //contains at least one allowed role with an user's group
    return roles.stream().anyMatch(r -> groups.stream().anyMatch(g -> r.containsGroup(g)) && r.isAllowed())
        //and do not contains NOT allowed roles with any user's group
        && roles.stream().noneMatch(r -> groups.stream().anyMatch(g -> r.containsGroup(g)) && !r.isAllowed());
  }
  
}
