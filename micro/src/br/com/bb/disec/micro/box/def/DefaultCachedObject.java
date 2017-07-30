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

package br.com.bb.disec.micro.box.def;

import br.com.bb.disec.micro.box.ObjectBox.CachedObject;
import java.time.Instant;
import java.util.Objects;
import us.pserver.insane.Checkup;
import us.pserver.insane.Sane;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 30/07/2017
 */
public class DefaultCachedObject implements CachedObject {
  
  private final Object object;
  
  private final Instant instant;
  
  
  public DefaultCachedObject(Object obj, Instant ins) {
    this.object = Sane.of(obj).with("Bad null object").get(Checkup.isNotNull());
    this.instant = Sane.of(ins).with("Bad null Instant").get(Checkup.isNotNull());
  }
  
  
  public DefaultCachedObject(Object obj) {
    this(obj, Instant.now());
  }
  

  @Override
  public Object getObject() {
    return object;
  }


  @Override
  public Instant getInstant() {
    return instant;
  }


  @Override
  public int compareTo(CachedObject o) {
    return this.instant.compareTo(o.getInstant());
  }


  @Override
  public int hashCode() {
    int hash = 3;
    hash = 43 * hash + Objects.hashCode(this.object);
    hash = 43 * hash + Objects.hashCode(this.instant);
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
    final DefaultCachedObject other = (DefaultCachedObject) obj;
    if (!Objects.equals(this.object, other.object)) {
      return false;
    }
    return Objects.equals(this.instant, other.instant);
  }

}
