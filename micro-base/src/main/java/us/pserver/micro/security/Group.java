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

package us.pserver.micro.security;

import java.util.Collection;
import java.util.Set;
import us.pserver.micro.security.impl.GroupBuilderImpl;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 27/01/2019
 */
public interface Group extends NamedSet {

  public default boolean containsUser(User user) {
    return getItems().contains(user.getName());
  }
  
  public default Set<String> getMembers() {
    return getItems();
  }
  
  @Override
  public GroupBuilder edit();
  
  
  
  public static GroupBuilder builder() {
    return new GroupBuilderImpl();
  }
  
  
  
  
  
  public interface GroupBuilder extends NamedSet.NamedSetBuilder<Group, GroupBuilder> {
    
    public default Set<String> getMembers() {
      return getItems();
    }

    public default GroupBuilder addUser(User user) {
      addItem(user.getName());
      return this;
    }

    public default GroupBuilder addUsers(Collection<User> users) {
      users.forEach(this::addUser);
      return this;
    }

    public default GroupBuilder clearUsers() {
      return clearItems();
    }

  }
  
}
