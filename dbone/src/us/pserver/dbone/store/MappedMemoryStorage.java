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

import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.LinkedList;
import us.pserver.tools.NotNull;
import us.pserver.tools.rfl.Reflector;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 18/09/2017
 */
public class MappedMemoryStorage extends AbstractStorage {
  
  private final FileChannel channel;
  
  private final MappedByteBuffer buffer;
  
  private Region mapregion;
  
  
  protected MappedMemoryStorage(FileChannel ch, LinkedList<Region> freeBlocks, int blockSize) throws StorageException {
    super(freeBlocks, ALLOC_POLICY_DIRECT, blockSize);
    this.channel = NotNull.of(ch).getOrFail("Bad null FileChannel");
    this.mapregion = Region.of(0, Math.min(Integer.MAX_VALUE, getFileSize()));
    this.buffer = this.createMappedMemory();
  }
  
  
  private MappedByteBuffer createMappedMemory() throws StorageException {
    return StorageException.rethrow(()->channel.map(
        FileChannel.MapMode.READ_WRITE, 
        mapregion.offset(), mapregion.length())
    );
  }
  
  
  @Override
  public long size() {
    return buffer.capacity();
  }
  
  
  public final long getFileSize() throws StorageException {
    return StorageException.rethrow(channel::size);
  }
  
  
  @Override
  protected Region nextRegion() {
    Region reg = super.nextRegion();
    if(reg.length() + reg.offset() > size()) {
      throw new BufferOverflowException();
    }
    return reg;
  }
  
  
  @Override
  public Block allocate() {
    Region reg = frees.isEmpty() ? nextRegion() : frees.pop();
    return get(reg);
  }


  @Override
  public void deallocate(Block blk) {
    NotNull.of(blk).failIfNull("Bad null Block");
    this.frees.push(blk.region());
  }


  @Override
  public Block get(Region reg) {
    NotNull.of(reg).failIfNull("Bad null Region");
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
    if(isSharedBuffer(blk.buffer())) {
      return;
    }
    buffer.limit(blk.region().intLength() + blk.region().intOffset());
    buffer.position(blk.region().intOffset());
    Block.copy(blk.buffer(), buffer);
  }


  @Override
  public void close() throws StorageException {
    Block blk = this.get(HEADER_REGION);
    ByteBuffer buf = blk.buffer();
    buf.putShort((short)0);
    buf.putInt(blockSize);
    for(Region r : frees) {
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
