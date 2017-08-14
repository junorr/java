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

package us.pserver.morphia.test.bean;

import java.util.ArrayList;
import java.util.List;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import us.pserver.tools.NotNull;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 12/08/2017
 */
@Entity
public class DefUser implements User {

  @Id private final ObjectId id;
  
  private final String name;
  
  private final String hash;
  
  @Embedded
  private final List<Access> access;
  
  
  private DefUser() {
    this.id = null;
    this.name = null;
    this.hash = null;
    this.access = null;
  }
  
  
  public DefUser(String name, String hash) {
    this(name, hash, new ArrayList<>());
  } 
  
  
  public DefUser(String name, String hash, List<Access> acs) {
    this.name = NotNull.of(name).getOrFail("Bad null name");
    this.hash = NotNull.of(hash).getOrFail("Bad null hash");
    this.access = NotNull.of(acs).getOrFail("Bad null Access list");
    this.id = null;
  }
  
  
  @Override
  public ObjectId getID() {
    return id;
  }
  
  
  @Override
  public String getName() {
    return name;
  }
  
  
  @Override
  public List<Access> access() {
    return access;
  }
  
  
  public boolean tryValidation(User another) {
    return this.name.equals(NotNull.of(another).getOrFail("Bad null User").getName())
        && this.hash.equals(another.getHash())
        && another.access().stream().allMatch(a->access.stream().anyMatch(ac->ac.tryGrant(a)));
  }
  
  
  public void validate(User another) throws IllegalAccessException {
    if(!tryValidation(another)) {
      throw new IllegalAccessException();
    }
  }
  
  
  public static User
  
}
