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

import java.util.Objects;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 29/03/2018
 */
@NodeEntity(label="Role")
public class SecurityRole extends Entity {
  
  private final String role;
  
  @Relationship(type = "ACCESS", direction = Relationship.OUTGOING)
  private final SecuredResource resource;
  
  
  private SecurityRole() {
    role = null;
    resource = null;
  }
  
  public SecurityRole(String rule, SecuredResource res) {
    this.role = rule;
    this.resource = res;
  }


  public String getRule() {
    return role;
  }
  
  public SecurityRole setRule(String str) {
    return new SecurityRole(str, resource);
  }


  public SecuredResource getResource() {
    return resource;
  }
  
  public SecurityRole setResource(SecuredResource res) {
    return new SecurityRole(role, res);
  }


  @Override
  public int hashCode() {
    int hash = 5;
    hash = 79 * hash + Objects.hashCode(this.role);
    hash = 79 * hash + Objects.hashCode(this.resource);
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
    final SecurityRole other = (SecurityRole) obj;
    if (!Objects.equals(this.role, other.role)) {
      return false;
    }
    if (!Objects.equals(this.resource, other.resource)) {
      return false;
    }
    return true;
  }


  @Override
  public String toString() {
    return "SecurityRole{" + "role=" + role + ", resource=" + resource + '}';
  }
  
}
