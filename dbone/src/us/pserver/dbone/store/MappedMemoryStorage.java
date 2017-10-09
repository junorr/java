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

import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Collections;
import java.util.LinkedList;
import us.pserver.tools.NotNull;
import us.pserver.tools.rfl.Reflector;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 18/09/2017
 */
public class MappedMemoryStorage extends AbstractStorage {
  
  public static final int MEMORY_ALLOC_SIZE = 32*1024;
  
  private final FileChannel channel;
  
  private MappedByteBuffer buffer;
  
  private Region mapreg;
  
  
  protected MappedMemoryStorage(FileChannel ch, LinkedList<Region> freeBlocks, int blockSize) throws StorageException {
    super(freeBlocks, ALLOC_POLICY_DIRECT, blockSize);
    this.channel = NotNull.of(ch).getOrFail("Bad null FileChannel");
    mapreg = Region.of(0, 1);
    this.remapMemory(Region.of(0, Math.max(size(), MEMORY_ALLOC_SIZE)));
  }
  
  
  private void remapMemory(Region r) throws StorageException {
    if(mapreg.contains(r)) return;
    long off = Math.min(mapreg.offset(), r.offset());
    mapreg = Region.of(off, Math.min(r.end(), Integer.MAX_VALUE));
    if(size() < mapreg.end()) {
      Region next = super.nextRegion();
      while(next.end() < mapreg.end()) {
        frees.push(next);
        next = Region.of(next.end(), next.length());
      }
      Collections.sort(frees);
    }
    this.buffer = StorageException.rethrow(()->channel.map(
        FileChannel.MapMode.READ_WRITE, 
        mapreg.offset(), mapreg.length())
    );
  }
  
  
  @Override
  public long size() throws StorageException {
    return StorageException.rethrow(channel::size);
  }
  
  
  @Override
  public Block allocate() {
    Region reg = frees.isEmpty() ? nextRegion() : frees.pop();
    return get(reg);
  }


  @Override
  public Block get(Region reg) {
    NotNull.of(reg).failIfNull("Bad null Region");
    this.remapMemory(reg);
    buffer.limit(reg.intLength() + reg.intOffset());
    buffer.position(reg.intOffset());
    ByteBuffer buf = buffer.slice();
    return new DefaultBlock(reg, buffer.slice());
  }
  
  
  public boolean isSharedBuffer(ByteBuffer shared) {
    Object att = Reflector.of(shared).selectField("att").get();
    return att != null && att.equals(buffer);
  }


  @Override
  public void put(Block blk) throws StorageException {
    NotNull.of(blk).failIfNull("Bad null Block");
    if(!isSharedBuffer(blk.buffer())) {
      buffer.limit(blk.region().intLength() + blk.region().intOffset());
      buffer.position(blk.region().intOffset());
      Block.copy(blk.buffer(), buffer);
    }
  }


  @Override
  public void close() throws StorageException {
    Block blk = this.get(HEADER_REGION);
    ByteBuffer buf = blk.buffer();
    buf.putShort((short)0);
    buf.putInt(blockSize);
    while(!frees.isEmpty() && buf.remaining() > Region.BYTES) {
      Region r = frees.pop();
      buf.putLong(r.offset());
      buf.putLong(r.length());
    }
    while(buf.remaining() >= Long.BYTES) {
      buf.putLong(0l);
    }
    this.put(blk);
    StorageException.rethrow(channel::close);
  }
  
  
  @Override
  public StorageTransaction startTransaction() {
    return new StorageTransaction(this);
  }
  
}
