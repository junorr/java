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

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
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
  
  public List<User> findUser(Predicate<User> prd);
  
  public Optional<User> getUser(String name);
  
  public Set<Group> getGroups();
  
  public List<Group> findGroup(Predicate<Group> prd);
  
  public Optional<Group> getGroup(String name);
  
  public Set<Role> getRoles();
  
  public List<Role> findRole(Predicate<Role> prd);
  
  public Optional<Role> getRole(String name);
  
  public Set<Resource> getResources();
  
  public List<Resource> findResource(Predicate<Resource> prd);
  
  public Optional<Resource> getResource(String name);
  
}
