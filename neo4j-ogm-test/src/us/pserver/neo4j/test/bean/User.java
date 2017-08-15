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

package us.pserver.neo4j.test.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import us.pserver.tools.NotNull;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 12/08/2017
 */
public class User {

  private final String name;
  
  private final Hash hash;
  
  private final List<Access> access;
  
  
  private User() {
    this.name = null;
    this.hash = null;
    this.access = null;
  }
  
  
  public User(String name, Hash hash) {
    this(name, hash, new ArrayList<>());
  } 
  
  
  public User(String name, Hash hash, List<Access> acs) {
    this.name = NotNull.of(name).getOrFail("Bad null name");
    this.hash = NotNull.of(hash).getOrFail("Bad null hash");
    this.access = NotNull.of(acs).getOrFail("Bad null Access list");
  }
  
  
  public String getName() {
    return name;
  }
  
  
  public Hash getHash() {
    return hash;
  }
  
  
  public List<Access> access() {
    return access;
  }


  @Override
  public int hashCode() {
    int hash = 5;
    hash = 23 * hash + Objects.hashCode(this.name);
    hash = 23 * hash + Objects.hashCode(this.hash);
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
    return Objects.equals(this.hash, other.hash);
  }
  
  
  public boolean tryValidation(User another) {
    return this.name.equals(NotNull.of(another).getOrFail("Bad null User").getName())
        && this.hash.equals(another.getHash())
        && another.access().stream().allMatch(a->access.stream().anyMatch(ac->ac.tryGrant(a)));
  }
  
  
  public void validate(User another) throws IllegalAccessException {
    boolean name = this.name.equals(NotNull.of(another).getOrFail("Bad null User").getName());
    boolean hash = this.hash.equals(another.getHash());
    boolean acss = another.access().stream().allMatch(a->access.stream().anyMatch(ac->ac.tryGrant(a)));
    String msg = null;
    if(!name) msg = "Name do not match";
    if(!hash) msg = "Hash do not match";
    if(!acss) msg = "Access denied";
    if(!tryValidation(another)) {
      throw new IllegalAccessException(msg);
    }
  }


  @Override
  public String toString() {
    return "User{" + "name:" + name + ", hash:" + hash + ", access:" + access + '}';
  }
  
}
