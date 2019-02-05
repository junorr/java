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

import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;
import us.pserver.micron.security.User;
import us.pserver.tools.Hash;
import us.pserver.tools.Match;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 31/01/2019
 */
public class UserBuilderImpl implements User.UserBuilder {

  private String name;

  private String fullName;

  private String email;

  private String hash;

  private LocalDate birth;

  private Instant created;

  public UserBuilderImpl() {
    created = Instant.now();
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public User.UserBuilder setName(String name) {
    this.name = name;
    return this;
  }

  @Override
  public String getFullName() {
    return fullName;
  }

  @Override
  public User.UserBuilder setFullName(String fullName) {
    this.fullName = fullName;
    return this;
  }

  @Override
  public String getEmail() {
    return email;
  }

  @Override
  public User.UserBuilder setEmail(String email) {
    this.email = email;
    return this;
  }

  @Override
  public String getHash() {
    return hash;
  }

  @Override
  public User.UserBuilder setHash(String hash) {
    this.hash = hash;
    return this;
  }

  @Override
  public User.UserBuilder setPassword(char[] password) {
    byte[] bpass = StandardCharsets.UTF_8.encode(
      CharBuffer.wrap(
          Match.notNull(password).getOrFail("Bad null password"))
    ).array();
    hash = Hash.sha256().of(bpass);
    return this;
  }

  @Override
  public LocalDate getBirth() {
    return birth;
  }

  @Override
  public User.UserBuilder setBirth(LocalDate birth) {
    this.birth = birth;
    return this;
  }

  @Override
  public Instant getCreated() {
    return created;
  }

  @Override
  public User.UserBuilder setCreated(Instant created) {
    this.created = created;
    return this;
  }
  
  @Override
  public User clone(User user) {
    return this.setBirth(user.getBirth())
        .setCreated(user.getCreated())
        .setEmail(user.getEmail())
        .setFullName(user.getFullName())
        .setHash(user.getHash())
        .setName(user.getName())
        .build();
  }

  @Override
  public User build() {
    return new UserImpl(name, fullName, email, hash, birth, created);
  }
  
  @Override
  public int hashCode() {
    int hash = 3;
    hash = 73 * hash + Objects.hashCode(this.name);
    hash = 73 * hash + Objects.hashCode(this.fullName);
    hash = 73 * hash + Objects.hashCode(this.email);
    hash = 73 * hash + Objects.hashCode(this.hash);
    hash = 73 * hash + Objects.hashCode(this.birth);
    hash = 73 * hash + Objects.hashCode(this.created);
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
    if(getClass() != obj.getClass()) {
      return false;
    }
    final UserBuilderImpl other = (UserBuilderImpl) obj;
    if(!Objects.equals(this.name, other.name)) {
      return false;
    }
    if(!Objects.equals(this.fullName, other.fullName)) {
      return false;
    }
    if(!Objects.equals(this.email, other.email)) {
      return false;
    }
    if(!Objects.equals(this.hash, other.hash)) {
      return false;
    }
    if(!Objects.equals(this.birth, other.birth)) {
      return false;
    }
    return Objects.equals(this.created, other.created);
  }
  
  @Override
  public String toString() {
    return "UserBuilder{" + "name=" + name + ", fullName=" + fullName + ", email=" + email + ", hash=" + hash + ", birth=" + birth + ", created=" + created + '}';
  }
  
}
