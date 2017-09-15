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
import java.nio.ByteBuffer;
import us.pserver.tools.NotNull;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 14/09/2017
 */
public interface Region extends Comparable, Writable, Serializable {

  public long offset();
  
  public long length();
  
  public int intOffset();
  
  public int intLength();
  
  @Override
  public default int compareTo(Object r) {
    NotNull.of(r).failIfNull();
    if(!Region.class.isAssignableFrom(r.getClass())) return -1;
    return Long.compare(this.offset(), ((Region)r).offset());
  }
  
  
  public static Region of(long offset, long length) {
    return new RegionImpl(offset, length);
  }
  
  
  
  
  
  public static class RegionImpl implements Region {
    
    private final long offset;
    
    private final long length;
    
    public RegionImpl(long ofs, long len) {
      this.offset = ofs;
      this.length = len;
    }

    @Override
    public long offset() {
      return this.offset;
    }

    @Override
    public long length() {
      return this.length;
    }
    
    @Override
    public int intOffset() {
      return (int) this.offset;
    }

    @Override
    public int intLength() {
      return (int) this.length;
    }
    
    @Override
    public void writeTo(ByteBuffer buf) {
      NotNull.of(buf).failIfNull("Bad null ByteBuffer");
      if(buf.remaining() < 16) {
        throw new IllegalStateException("Insuficient size remaining");
      }
      buf.putLong(offset);
      buf.putLong(length);
    }

    @Override
    public int hashCode() {
      int hash = 7;
      hash = 89 * hash + (int) (this.offset ^ (this.offset >>> 32));
      hash = 89 * hash + (int) (this.length ^ (this.length >>> 32));
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
      final RegionImpl other = (RegionImpl) obj;
      if (this.offset != other.offset) {
        return false;
      }
      return this.length == other.length;
    }


    @Override
    public String toString() {
      return "Region{" + "offset=" + offset + ", length=" + length + '}';
    }
    
  }
  
}
