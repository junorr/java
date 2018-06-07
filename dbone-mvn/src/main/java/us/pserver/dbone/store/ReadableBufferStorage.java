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
import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.IntFunction;
import us.pserver.tools.io.ByteBufferOutputStream;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 11/05/2018
 */
public class ReadableBufferStorage implements ReadableStorage {
  
  private final ByteBuffer buffer;
  
  private final int blksize;
  
  private final IntFunction<ByteBuffer> alloc;
  
  
  public ReadableBufferStorage(ByteBuffer buffer, int blockSize, IntFunction<ByteBuffer> alloc) {
    this.buffer = Objects.requireNonNull(buffer, "Bad null MappedByteBuffer");
    Block.validateMinBlockSize(blockSize);
    this.blksize = blockSize;
    this.alloc = Objects.requireNonNull(alloc, "Bad null alloc policy IntFunction<ByteBuffer>");
  }
  
  
  @Override
  public ByteBuffer get(Region reg) {
    if(reg == null || !reg.isValid()) {
      throw new IllegalArgumentException("Bad invalid Region: "+ reg);
    }
    ByteBufferOutputStream bbos = new ByteBufferOutputStream(alloc);
    Region cur = reg;
    while(cur.isValid()) {
      int lim = buffer.limit();
      buffer.position(cur.intOffset());
      buffer.limit(buffer.position() + blksize);
      Block blk = Block.read(buffer.slice());
      bbos.write(blk.buffer());
      buffer.limit(lim);
      cur = blk.nextRegion();
    }
    return bbos.toByteBuffer(alloc);
  }
  
  
  @Override
  public List<Region> getRootRegions() {
    List<Region> blks = new LinkedList<>();
    Region reg = Region.of(StorageHeader.BYTES, blksize);
    while(reg.end() <= buffer.limit()) {
      int lim = buffer.limit();
      buffer.position(reg.intOffset());
      buffer.limit(Math.min((buffer.position() + blksize), buffer.capacity()));
      Block blk = Block.read(buffer.slice());
      buffer.limit(lim);
      if(blk.isRoot()) {
        blks.add(reg);
      }
      reg = Region.of(reg.end(), reg.length());
    }
    return blks;
  }
  
  
  @Override
  public long size() {
    return buffer.limit();
  }


  @Override
  public void close() {}
  
  
  @Override
  public IntFunction<ByteBuffer> allocBufferPolicy() {
    return alloc;
  }
  
}
