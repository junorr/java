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

package us.pserver.ignite.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 22/07/2018
 */
public class Group {
  
  private final String name;

  private List<User> users;
  
  
  public Group(String name) {
    this.name = Objects.requireNonNull(name);
    this.users = new ArrayList<>();
  }
  
  public Group(String name, List<User> users) {
    this(name);
    this.users.addAll(users);
  }
  
  public Group(String name, User ... users) {
    this(name, Arrays.asList(users));
  }
  
  public String getName() {
    return name;
  }
  
  public List<User> getUsers() {
    return users;
  }
  
  public Group addUser(User usr) {
    if(usr != null) {
      users.add(usr);
    }
    return this;
  }
  
  public Optional<User> removeUser(String username) {
    for(int i = 0; i < users.size(); i++) {
      if(users.get(i).getName().equals(username)) {
        return Optional.of(users.remove(i));
      }
    }
    return Optional.empty();
  }
  
  public boolean conteinsUser(String username) {
    return users.stream().anyMatch(u->u.getName().equals(username));
  }
  
  public Optional<User> getUser(String username) {
    return users.stream().filter(u->u.getName().equals(username)).findAny();
  }


  @Override
  public int hashCode() {
    int hash = 7;
    hash = 79 * hash + Objects.hashCode(this.name);
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
    if (getClass() != obj.getClass()) {
      return false;
    }
    final Group other = (Group) obj;
    if (!Objects.equals(this.name, other.name)) {
      return false;
    }
    return true;
  }


  @Override
  public String toString() {
    return "Group{" + "name=" + name + ", users=" + users + '}';
  }
  
}
