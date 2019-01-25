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
import java.util.Collection;
import java.util.Set;
import us.pserver.tools.Match;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 25/01/2019
 */
public class Group extends AbstractGroup {

  public Group(String name, Set<String> members, Instant created) {
    super(name, members, created);
  }
  
  public Group(String name, Set<String> members) {
    super(name, members);
  }
  
  public Set<String> getMembers() {
    return items;
  }
  
  public boolean containsUser(User user) {
    return contains(user.getName());
  }
  
  @Override
  public GroupBuilder edit() {
    return (GroupBuilder) new GroupBuilder()
        .setCreated(created)
        .setItems(items)
        .setName(name);
  }
  
  
  
  public static GroupBuilder builder() {
    return new GroupBuilder();
  }
  
  
  
  
  
  public static class GroupBuilder extends AbstractGroupBuilder<Group> {
    
    public GroupBuilder() {
      super();
    }
    
    public GroupBuilder addUsers(Collection<User> users) {
      Match.notNull(users).getOrFail("Bad null users Collection").forEach(this::addUser);
      return this;
    }
    
    public GroupBuilder addUser(User user) {
      this.items.add(Match.notNull(user).getOrFail("Bad null User").getName());
      return this;
    }
    
    public GroupBuilder clearUsers() {
      this.items.clear();
      return this;
    }
    
    @Override
    public Group build() {
      return new Group(name, items, created);
    }
    
  }
  
}
