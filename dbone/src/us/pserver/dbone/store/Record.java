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

package us.pserver.dbone.store;

import java.io.Serializable;
import java.util.Objects;
import us.pserver.tools.NotNull;
import us.pserver.tools.mapper.ObjectUID;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 14/09/2017
 */
public interface Record extends Comparable<Record>, Serializable {

  public Region getRegion();
  
  public ObjectUID getUID();
  
  @Override
  public default int compareTo(Record idx) {
    return 0;
  }
  
  
  public static Record of(String name, Region r, ObjectUID uid) {
    return new IndexImpl(name, r, uid);
  }
  
  
  
  
  
  public static class IndexImpl implements Record {
  
    private final Region region;

    private final ObjectUID uid;

    public IndexImpl(String name, Region reg, ObjectUID uid) {
      this.region = NotNull.of(reg).getOrFail("Bad null Region");
      this.uid = NotNull.of(uid).getOrFail("Bad null ObjectUID");
    }

    @Override
    public Region getRegion() {
      return this.region;
    }

    @Override
    public ObjectUID getUID() {
      return this.uid;
    }

    @Override
    public int hashCode() {
      int hash = 7;
      hash = 83 * hash + Objects.hashCode(this.region);
      hash = 83 * hash + Objects.hashCode(this.uid);
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
      final IndexImpl other = (IndexImpl) obj;
      if (!Objects.equals(this.region, other.region)) {
        return false;
      }
      if (!Objects.equals(this.uid, other.uid)) {
        return false;
      }
      return true;
    }

    @Override
    public String toString() {
      return "Index{" + "region=" + region + ", uid=" + uid + '}';
    }

  }

  
}
