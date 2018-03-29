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

package us.pserver.neo4j.ogm.test;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;
import us.pserver.tools.Hash;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 29/03/2018
 */
@NodeEntity
public class User extends Entity {

  private final String name;
  
  private final byte[] pwdHash;
  
  @Relationship(type = "HAS", direction = Relationship.OUTGOING)
  private List<SecurityRole> roles;


  public User() {
    name = null;
    pwdHash = null;
    roles = null;
  }
  
  
  public User(String name, byte[] pwdHash, SecurityRole ... roles) {
    this(name, pwdHash, Arrays.asList(roles));
  }
  
  
  public User(String name, byte[] pwdHash, List<SecurityRole> roles) {
    this.name = name;
    this.pwdHash = pwdHash;
    this.roles = roles;
  }
  
  
  public String getName() {
    return name;
  }
  
  
  public User setName(String name) {
    return new User(name, pwdHash);
  }
  
  public User setPwdHash(byte[] pwdHash) {
    return new User(name, pwdHash);
  }
  
  
  public List<SecurityRole> getRoles() {
    return roles;
  }
  
  public User setRoles(List<SecurityRole> roles) {
    return new User(name, pwdHash, roles);
  }
  
  
  public boolean authenticate(User usr) {
    return Arrays.equals(pwdHash, usr.pwdHash);
  }
  
  public boolean authenticate(Hash hash) {
    return Arrays.equals(pwdHash, hash.getBytes());
  }


  @Override
  public int hashCode() {
    int hash = 3;
    hash = 79 * hash + Objects.hashCode(this.name);
    hash = 79 * hash + Arrays.hashCode(this.pwdHash);
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
    final User other = (User) obj;
    if (!Objects.equals(this.name, other.name)) {
      return false;
    }
    if (!Arrays.equals(this.pwdHash, other.pwdHash)) {
      return false;
    }
    return true;
  }


  @Override
  public String toString() {
    return "User{" + "name=" + name + ", roles=" + roles + '}';
  }
  
}
