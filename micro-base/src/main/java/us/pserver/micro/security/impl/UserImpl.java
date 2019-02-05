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

package us.pserver.micro.security.impl;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;
import us.pserver.tools.Match;
import us.pserver.micro.security.User;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 22/01/2019
 */
public class UserImpl implements User {

  private final String name;
  
  private final String fullName;
  
  private final String email;
  
  private final String hash;
  
  private final LocalDate birth;
  
  private final Instant created;
  
  
  public UserImpl(String name, String fullName, String email, String hash, LocalDate birth, Instant created) {
    this.name = Match.notEmpty(name).getOrFail("Bad null/empty name");
    this.fullName = fullName;
    this.email = Match.notEmpty(email).getOrFail("Bad null/empty email");
    this.hash = Match.notEmpty(hash).getOrFail("Bad null/empty hash");
    this.birth = birth;
    this.created = created;
  }
  
  public UserImpl(String name, String fullName, String email, String hash, LocalDate birth) {
    this(name, fullName, email, hash, birth, Instant.now());
  }
  
  
  @Override
  public String getName() {
    return name;
  }
  
  @Override
  public String getFullName() {
    return fullName;
  }
  
  @Override
  public String getEmail() {
    return email;
  }
  
  @Override
  public String getHash() {
    return hash;
  }
  
  @Override
  public LocalDate getBirth() {
    return birth;
  }
  
  @Override
  public Instant getCreated() {
    return created;
  }
  
  
  @Override
  public boolean authenticate(String name, String hash) {
    return authenticate(new UserBuilderImpl()
        .setName(name)
        .setEmail(email)
        .setHash(hash)
        .build()
    );
  }
  
  @Override
  public boolean authenticate(String name, char[] password) {
    return authenticate(new UserBuilderImpl()
        .setName(name)
        .setEmail(email)
        .setPassword(password)
        .build()
    );
  }
  
  @Override
  public boolean authenticate(User user) {
    return user != null
        && this.name.equals(user.getName())
        && this.hash.equals(user.getHash());
  }
  
  @Override
  public UserBuilder edit() {
    return new UserBuilderImpl()
        .setName(name)
        .setEmail(email)
        .setFullName(fullName)
        .setHash(hash)
        .setBirth(birth)
        .setCreated(created);
  }
  
  @Override
  public int hashCode() {
    int hash = 7;
    hash = 41 * hash + Objects.hashCode(this.name);
    hash = 41 * hash + Objects.hashCode(this.email);
    hash = 41 * hash + Objects.hashCode(this.hash);
    return hash;
  }
  
  @Override
  public boolean equals(Object obj) {
    if(this == obj) {
      return true;
    }
    if(obj == null) {
      return false;
    }
    if(!User.class.isAssignableFrom(obj.getClass())) {
      return false;
    }
    final User other = (User) obj;
    if(!Objects.equals(this.name, other.getName())) {
      return false;
    }
    if(!Objects.equals(this.email, other.getEmail())) {
      return false;
    }
    return Objects.equals(this.hash, other.getHash());
  }
  
  @Override
  public String toString() {
    return "User{" + "name=" + name + ", fullName=" + fullName + ", email=" + email + ", birth=" + birth + ", hash=" + hash + ", created=" + created + '}';
  }
  
}
