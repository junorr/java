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

import java.time.Instant;
import java.util.Objects;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 29/03/2018
 */
@NodeEntity(label="Log")
public class ResourceLog extends Entity {

  private final Instant instant;
  
  @Relationship(type = "ACCESSED")
  private final User user;
  
  
  private ResourceLog() {
    instant = null;
    user = null;
  }


  public ResourceLog(Instant instant, User user) {
    this.instant = instant;
    this.user = user;
  }


  public Instant getInstant() {
    return instant;
  }
  
  public ResourceLog setInstant(Instant inst) {
    return new ResourceLog(inst, user);
  }


  public User getUser() {
    return user;
  }
  
  public ResourceLog setUser(User usr) {
    return new ResourceLog(instant, usr);
  }


  @Override
  public int hashCode() {
    int hash = 7;
    hash = 37 * hash + Objects.hashCode(this.instant);
    hash = 37 * hash + Objects.hashCode(this.user);
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
    final ResourceLog other = (ResourceLog) obj;
    if (!Objects.equals(this.instant, other.instant)) {
      return false;
    }
    if (!Objects.equals(this.user, other.user)) {
      return false;
    }
    return true;
  }


  @Override
  public String toString() {
    return "ResourceLog{" + "instant=" + instant + ", user=" + user + '}';
  }
  
}
