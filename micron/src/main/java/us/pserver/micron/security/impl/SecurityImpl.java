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
import us.pserver.micron.config.SecurityConfig;
import us.pserver.micron.security.Group;
import us.pserver.tools.Match;
import us.pserver.micron.security.User;
import us.pserver.micron.security.Resource;
import us.pserver.micron.security.Role;
import us.pserver.micron.security.Security;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 25/01/2019
 */
public class SecurityImpl implements Security {
  
  private final SecurityConfig cfg;
  
  
  public SecurityImpl(SecurityConfig cfg) {
    this.cfg = Match.notNull(cfg).getOrFail("Bad null SecurityConfig");
  }
  
  
  @Override
  public SecurityConfig getConfig() {
    return cfg;
  }
  
  
  @Override
  public List<User> findUserByEmail(String email) {
    return cfg.getUsers()
        .stream()
        .filter(u -> u.getEmail().equals(email))
        .collect(Collectors.toList());
  }
  
  
  @Override
  public Optional<User> authenticateUser(String name, char[] password) {
    Optional<User> opt = cfg.getUser(name);
    return opt.isPresent() && opt.get().authenticate(name, password) ? opt : Optional.empty();
  }
  
  
  @Override
  public boolean authorize(String resource, User user) {
    Optional<Resource> opt = cfg.getResource(resource);
    return opt.isPresent() ? authorize(opt.get(), user) : false;
  }
  
  @Override
  public boolean authorize(Resource res, User user) {
    List<Role> roles = cfg.findRole((s,r) -> res.containsRole(r));
    List<Group> groups = cfg.findGroup((s,g) -> g.containsUser(user));
    //contains at least one allowed role with user's groups
    return roles.stream().anyMatch(r -> groups.stream().anyMatch(g -> r.containsGroup(g)) && r.isAllowed())
        //and do not contains NOT allowed roles with any user's groups
        && roles.stream().noneMatch(r -> groups.stream().anyMatch(g -> r.containsGroup(g)) && !r.isAllowed());
  }
  
}
