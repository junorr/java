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
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import us.pserver.tools.Match;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 22/01/2019
 */
public class Group {
  
  private final String name;
  
  private final Set<String> usernames;
  
  private final Instant created;
  
  
  public Group(String name, Set<String> usernames, Instant created) {
    this.name = Match.notEmpty(name).getOrFail("Bad null/empty name String");
    this.usernames = Collections.unmodifiableSet(
        Match.notNull(usernames).getOrFail("Bad null/empty usernames Set")
    );
    this.created = Match.notNull(created).getOrFail("Bad null created Instant");
  }
  
  public Group(String name, Set<String> usernames) {
    this(name, usernames, Instant.now());
  }
  
  
  public String getName() {
    return name;
  }
  
  public Instant getCreated() {
    return created;
  }
  
  public Set<String> getUsernames() {
    return usernames;
  }
  
  public boolean contains(User user) {
    return usernames.contains(user.getName());
  }
  
  public GroupBuilder edit() {
    return builder()
        .setName(name)
        .setCreated(created)
        .setUsernames(usernames);
  }


  @Override
  public int hashCode() {
    int hash = 3;
    hash = 47 * hash + Objects.hashCode(this.name);
    hash = 47 * hash + Objects.hashCode(this.usernames);
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
    if (!Objects.equals(this.usernames, other.usernames)) {
      return false;
    }
    return true;
  }


  @Override
  public String toString() {
    return "Group{" + "name=" + name + ", usernames=" + usernames + ", created=" + created + '}';
  }
  
  
  
  public static GroupBuilder builder() {
    return new GroupBuilder();
  }
  
  
  
  
  
  public static class GroupBuilder {

    private String name;
    
    private Instant created = Instant.now();
    
    private Set<String> usernames = new HashSet<>();
    
    
    public String getName() {
      return name;
    }
    
    public GroupBuilder setName(String name) {
      this.name = name;
      return this;
    }
    
    
    public Instant getCreated() {
      return created;
    }
    
    public GroupBuilder setCreated(Instant created) {
      this.created = created;
      return this;
    }
    
    
    public Set<String> getUsernames() {
      return usernames;
    }
    
    public GroupBuilder setUsernames(Set<String> usernames) {
      this.usernames = new HashSet(usernames);
      return this;
    }
    
    public GroupBuilder clearUsernames() {
      this.usernames.clear();
      return this;
    }
    
    public GroupBuilder addUsername(String username) {
      usernames.add(Match.notEmpty(username).getOrFail("Bad null/empty username"));
      return this;
    }
    
    public GroupBuilder addUser(User user) {
      usernames.add(Match.notNull(user).getOrFail("Bad null user").getName());
      return this;
    }
    
    
    public Group build() {
      return new Group(name, usernames, created);
    }
    
  }

}
