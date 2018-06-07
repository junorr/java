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

import us.pserver.dbone.region.Region;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.Objects;
import java.util.function.IntFunction;
import us.pserver.tools.Match;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 19/05/2018
 */
public interface StorageHeader extends Writable {
  
  public static final int BYTES = Region.BYTES * 2;

  public Region getRecycleRegion();
  
  public Region getReservedRegion();
  
  public int getBlockSize();
  
  
  
  public static StorageHeader of(Region recycle, Region reserved) {
    return new DefaultHeader(recycle, reserved);
  }
  
  
  public static StorageHeader read(ByteBuffer buf) {
    Match.notNull(buf).and(b->b.remaining() >= BYTES).failIfNotMatch("Bad ByteBuffer = %s", buf);
    return new DefaultHeader(Region.of(buf), Region.of(buf));
  }
  
  
  public static StorageHeader read(ReadableByteChannel ch) throws IOException {
    Match.notNull(ch).failIfNotMatch("Bad null ReadableByteChannel");
    ByteBuffer buf = ByteBuffer.allocate(StorageHeader.BYTES);
    ch.read(buf);
    buf.flip();
    return read(buf);
  }
  
  
  
  
  
  public static class DefaultHeader implements StorageHeader {
    
    private final Region recycle;
    
    private final Region reserved;
    
    public DefaultHeader(Region recycle, Region reserved) {
      this.recycle = Match.notNull(recycle).getOrFail("Bad null recycle Region");
      this.reserved = Match.notNull(reserved).getOrFail("Bad null reserved Region");
    }
    
    @Override
    public Region getRecycleRegion() {
      return recycle;
    }
    
    @Override
    public Region getReservedRegion() {
      return reserved;
    }
    
    @Override
    public int getBlockSize() {
      return recycle.intLength();
    }
    
    @Override
    public int writeTo(WritableByteChannel ch, IntFunction<ByteBuffer> alloc) throws IOException {
      ch.write(toByteBuffer(alloc));
      return BYTES;
    }
    
    @Override
    public int writeTo(ByteBuffer wb) {
      Match.notNull(wb).and(b->b.remaining() >= BYTES).failIfNotMatch("Bad ByteBuffer = %s", wb);
      recycle.writeTo(wb);
      reserved.writeTo(wb);
      return BYTES;
    }
    
    @Override
    public ByteBuffer toByteBuffer(IntFunction<ByteBuffer> alloc) {
      ByteBuffer buf = ByteBuffer.allocate(BYTES);
      writeTo(buf);
      buf.flip();
      return buf;
    }
    
    @Override
    public int hashCode() {
      int hash = 5;
      hash = 97 * hash + Objects.hashCode(this.recycle);
      hash = 97 * hash + Objects.hashCode(this.reserved);
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
      final DefaultHeader other = (DefaultHeader) obj;
      if (!Objects.equals(this.recycle, other.recycle)) {
        return false;
      }
      return Objects.equals(this.reserved, other.reserved);
    }


    @Override
    public String toString() {
      return "StorageHeader{" + "recycle=" + recycle + ", reserved=" + reserved + '}';
    }
    
  }
  
}
