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

package us.pserver.dbone.volume;

import us.pserver.dbone.store.Region;
import java.util.Objects;
import us.pserver.tools.NotNull;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 24/11/2017
 */
public interface Record extends Comparable<Record> {
  
  public int recordID();

  public String objectUID();
  
  public Region region();
  
  @Override
  public default int compareTo(Record other) {
    NotNull.of(other).failIfNull("Bad null VolumeID");
    int cmp = Integer.compare(recordID(), other.recordID());
    return (cmp == 0 ? objectUID().compareTo(other.objectUID()) : cmp);
  }
  
  
  public static Record of(String uid, Region reg) {
    return new DefaultRecord(uid, reg);
  }
  
  
  public static Record of(int sid, String uid, Region reg) {
    return new DefaultRecord(sid, uid, reg);
  }
  
  
  
  
  
  public static class DefaultRecord implements Record {
    
    private final int rid;
    
    private final String uid;
    
    private final Region reg;
    
    
    public DefaultRecord(int recordID, String uid, Region reg) {
      this.rid = recordID;
      this.uid = NotNull.of(uid).getOrFail("Bad null UID");
      this.reg = NotNull.of(reg).getOrFail("Bad null Region");
    }
    
    
    public DefaultRecord(String uid, Region reg) {
      this(0, uid, reg);
    }
    
    
    @Override
    public int recordID() {
      return rid;
    }
    

    @Override
    public String objectUID() {
      return uid;
    }


    @Override
    public Region region() {
      return reg;
    }


    @Override
    public int hashCode() {
      int hash = 7;
      hash = 79 * hash + this.rid;
      hash = 79 * hash + Objects.hashCode(this.uid);
      hash = 79 * hash + Objects.hashCode(this.reg);
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
      final DefaultRecord other = (DefaultRecord) obj;
      if (this.rid != other.rid) {
        return false;
      }
      if (!Objects.equals(this.uid, other.uid)) {
        return false;
      }
      return Objects.equals(this.reg, other.reg);
    }


    @Override
    public String toString() {
      return "Record{" + "recid=" + rid + ", uid=" + uid + ", " + reg + '}';
    }
    
  }
  
}
