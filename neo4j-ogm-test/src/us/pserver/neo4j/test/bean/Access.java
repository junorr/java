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

import java.util.Objects;
import us.pserver.tools.NotNull;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 12/08/2017
 */
public class Access {
  
  private final String name;
  
  private final Transaction tran;
  
  private final boolean authorized;
  
  
  private Access() {
    this(null, null, false);
  }
  
  
  public Access(String name) {
    this(name, null, false);
  }
  
  
  public Access(String name, Transaction tran, boolean authorized) {
    this.name = name != null ? name : "";
    this.tran = tran;
    this.authorized = authorized;
  }
  
  
  public Transaction transaction() {
    return tran;
  }
  
  
  public boolean isAuthorized() {
    return authorized;
  }
  
  
  public boolean isDenied() {
    return !authorized;
  }
  
  
  public String getName() {
    return name;
  }
  
  
  public boolean tryGrant(Access acs) {
    return this.name.equals(NotNull.of(acs).getOrFail("Access").name) 
        && tran == acs.tran
        && isAuthorized();
  }
  
  
  public void grant(Access acs) throws IllegalAccessException {
    if(!tryGrant(acs)) {
      throw new IllegalAccessException();
    }
  }


  @Override
  public int hashCode() {
    int hash = 3;
    hash = 97 * hash + Objects.hashCode(this.name);
    hash = 97 * hash + Objects.hashCode(this.tran);
    hash = 97 * hash + Objects.hashCode(this.authorized);
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
    final Access other = (Access) obj;
    if (!Objects.equals(this.name, other.name)) {
      return false;
    }
    return tran == other.tran && this.authorized == other.authorized;
  }


  @Override
  public String toString() {
    return "{" + "name:" + name + ", transaction:" + tran + ", authorized:" + authorized + '}';
  }
  
}
