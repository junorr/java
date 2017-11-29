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

package us.pserver.dbone.internal;

import java.io.Serializable;
import java.nio.ByteBuffer;
import us.pserver.tools.NotNull;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 14/09/2017
 */
public interface Region extends Comparable, Serializable {
  
  public static final int BYTES = Long.BYTES * 2 + Integer.BYTES;
  
  public int storeID();

  public long offset();
  
  public long length();
  
  public long end();
  
  public int intOffset();
  
  public int intLength();
  
  public int intEnd();
  
  public boolean isValid();
  
  public boolean contains(Region r);
  
  public ByteBuffer toByteBuffer();
  
  public void writeTo(ByteBuffer buf);
  
  @Override
  public default int compareTo(Object r) {
    NotNull.of(r).failIfNull();
    if(!Region.class.isAssignableFrom(r.getClass())) return -1;
    return Long.compare(this.offset(), ((Region)r).offset());
  }
  
  
  public static Region of(long offset, long length) {
    return new RegionImpl(offset, length);
  }
  
  
  public static Region of(int storeID, long offset, long length) {
    return new RegionImpl(storeID, offset, length);
  }
  
  
  public static Region of(ByteBuffer buf) {
    return Region.of(buf.getInt(), buf.getLong(), buf.getLong());
  }
  
  
  public static Region invalid() {
    return of(-1, -1);
  }
  
  
  
  
  
  public static class RegionImpl implements Region {
    
    private final long offset;
    
    private final long length;
    
    private final int storeID;
    
    
    public RegionImpl(long ofs, long len) {
      this(0, ofs, len);
    }
    
    public RegionImpl(int storeID, long ofs, long len) {
      this.storeID = storeID;
      this.offset = ofs;
      this.length = len;
    }
    
    @Override
    public int storeID() {
      return storeID;
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
    public long end() {
      return this.offset + this.length;
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
    public int intEnd() {
      return this.intOffset() + this.intLength();
    }
    
    @Override
    public boolean isValid() {
      return this.offset >= 0 && this.length >= 1;
    }
    
    @Override
    public boolean contains(Region r) {
      return this.offset <= r.offset() && this.end() >= r.end();
    }
    
    @Override
    public ByteBuffer toByteBuffer() {
      ByteBuffer buf = ByteBuffer.allocate(BYTES);
      this.writeTo(buf);
      buf.flip();
      return buf;
    }
    
    @Override
    public void writeTo(ByteBuffer buf) {
      buf.putInt(storeID);
      buf.putLong(offset);
      buf.putLong(length);
    }
    
    @Override
    public int hashCode() {
      int hash = 7;
      hash = 89 * hash + (int) (this.storeID ^ (this.storeID >>> 16));
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
      if (Region.class != obj.getClass()) {
        return false;
      }
      final Region other = (Region) obj;
      if (this.offset != other.offset()) {
        return false;
      }
      if (this.storeID != other.storeID()) {
        return false;
      }
      return this.length == other.length();
    }
    
    @Override
    public String toString() {
      return String.format("Region{storeID=%d, offset=%d, length=%d}", storeID, offset, length);
    }
    
  }
  
}
