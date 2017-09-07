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

package test.nitrite;

import java.util.Objects;
import us.pserver.tools.NotNull;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 12/08/2017
 */
public class Access {
  
  private Long id;
  
  private String name;
  
  private Role role;
  
  private boolean auth;
  
  
  private Access() {
  }
  
  
  public Access(String name) {
    this(name, null, false);
  }
  
  
  public Access(String name, Role role, boolean auth) {
    this.name = name != null ? name : "";
    this.role = role;
    this.auth = auth;
  }
  
  
  public Long getId() {
    return id;
  }
  
  
  public Role getRole() {
    return role;
  }
  
  
  public boolean isAuthorized() {
    return auth;
  }
  
  
  public boolean isDenied() {
    return !auth;
  }
  
  
  public String getName() {
    return name;
  }
  
  
  public boolean tryGrant(Access acs) {
    return this.name.equals(NotNull.of(acs).getOrFail("Access").name) 
        && role == acs.role
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
    hash = 97 * hash + Objects.hashCode(this.role);
    hash = 97 * hash + Objects.hashCode(this.auth);
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
    return role == other.role && this.auth == other.auth;
  }


  @Override
  public String toString() {
    return "{" + "name:" + name + ", role:" + role + ", auth:" + auth + '}';
  }
  
}
