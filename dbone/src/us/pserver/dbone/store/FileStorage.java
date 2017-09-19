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

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.IntFunction;
import us.pserver.dbone.store.tx.DefaultTransaction;
import us.pserver.dbone.store.tx.RollbackStoreAllocationLog;
import us.pserver.dbone.store.tx.Transaction;
import us.pserver.tools.NotNull;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 18/09/2017
 */
public class FileStorage implements Storage {
  
  public static final BiFunction<ByteBuffer,Integer,ByteBuffer> ALLOC_POLICY_SHARED = (b,i)->b.slice();
  
  public static final BiFunction<ByteBuffer,Integer,ByteBuffer> ALLOC_POLICY_DIRECT = (b,i)->ByteBuffer.allocateDirect(i);
  
  public static final BiFunction<ByteBuffer,Integer,ByteBuffer> ALLOC_POLICY_HEAP = (b,i)->ByteBuffer.allocate(i);
  
  public static final int MINIMUM_BLOCK_SIZE = 1024;
  
  public static final int DEFAULT_BLOCK_SIZE = 4096;
  
  public static final Region HEADER_REGION = Region.of(0, 1024);
  
  
  private final FileChannel channel;
  
  private final List<Region> frees;
  
  private final BiFunction<ByteBuffer,Integer,ByteBuffer> malloc;
  
  private final int blockSize;
  
  
  protected FileStorage(FileChannel channel, BiFunction<ByteBuffer,Integer,ByteBuffer> allocPolicy, List<Region> freeBlocks, int blockSize) {
    this.channel = NotNull.of(channel).getOrFail("Bad null FileChannel");
    this.malloc = NotNull.of(allocPolicy).getOrFail("Bad null alloc policy");
    this.frees = NotNull.of(freeBlocks).getOrFail("Bad null free blocks list");
    this.blockSize = blockSize;
  }
  
  
  private Region nextRegion() throws BlockAllocationException {
    try {
      long len = channel.size();
      if(len < 1024) {
        len = 1024;
      }
      else if(len > 1024 && (len-1024) % this.blockSize > 0) {
        len = ((len / this.blockSize + 1) * this.blockSize);
      }
      return Region.of(len, this.blockSize);
    }
    catch(IOException e) {
      throw new BlockAllocationException(e.toString(), e);
    }
  }
  
  
  @Override
  public Transaction<Block> allocate() {
    ByteBuffer buf = malloc.apply(null, blockSize);
    Region reg = null;
    if(this.frees.isEmpty()) {
      reg = this.nextRegion();
    }
    else {
      reg = this.frees.remove(0);
    }
    Block blk = new DefaultBlock(reg, buf).setNext(Region.of(0, 0));
    DefaultTransaction<Block> tx = new DefaultTransaction(null, blk);
    tx.logs().add(new RollbackStoreAllocationLog(blk, this));
    System.out.println("* Storage.allocate: success="+ tx.isSuccessful()+ ", error="+ tx.getError()+ ", value="+ tx.value());
    return tx;
  }


  @Override
  public void reallocate(Block blk) throws BlockAllocationException {
    this.frees.remove(blk.region());
  }


  @Override
  public void deallocate(Block blk) throws BlockAllocationException {
    NotNull.of(blk).failIfNull("Bad null Block");
    try {
      if(blk.region().offset() + blk.region().length() >= channel.size()) {
        //System.out.println("* Storage.deallocate: channel.truncate => "+ blk.region());
        channel.truncate(blk.region().offset());
      }
      else {
        //System.out.println("* Storage.deallocate: frees.add => "+ blk.region());
        this.frees.add(blk.region());
      }
    }
    catch(IOException e) {
      throw new BlockAllocationException(e.toString(), e);
    }
  }


  @Override
  public Transaction<Block> get(Region reg) {
    NotNull.of(reg).failIfNull("Bad null Region");
    try {
      channel.position(reg.offset());
      ByteBuffer buf = malloc.apply(null, reg.intLength());
      int read = channel.read(buf);
      if(read > 0) buf.flip();
      return new DefaultTransaction(null, new DefaultBlock(reg, buf));
    }
    catch(IOException e) {
      return new DefaultTransaction(e, null);
    }
  }


  @Override
  public Transaction<Void> put(Block blk) throws StoreException {
    NotNull.of(blk).failIfNull("Bad null Block");
    try {
      blk.buffer().position(0);
      blk.buffer().limit(blk.buffer().capacity());
      channel.position(blk.region().offset());
      channel.write(blk.buffer());
      DefaultTransaction<Void> tx = new DefaultTransaction<>(null, null);
      tx.logs().add(new RollbackStoreAllocationLog(blk, this));
      return tx;
    }
    catch(IOException e) {
      return new DefaultTransaction<>(e, null);
    }
  }


  @Override
  public List<Region> getFreeBlocks() {
    return this.frees;
  }


  @Override
  public int getBlockSize() {
    return this.blockSize;
  }
  
  
  @Override
  public IntFunction<ByteBuffer> getAllocationPolicy() {
    return this.malloc;
  }
  
  
  @Override
  public void close() throws StoreException {
    try {
      Block blk = this.get(HEADER_REGION);
      ByteBuffer buf = blk.buffer();
      //System.out.println("FileStorage.get: remaining="+ buf.remaining()+ ", capacity="+ buf.capacity());
      buf.putShort((short)0);
      buf.putInt(blockSize);
      for(Region r : frees) {
        buf.putLong(r.offset());
        buf.putLong(r.length());
      }
      while(buf.remaining() >= 8) {
        buf.putLong(0l);
      }
      buf.flip();
      channel.position(blk.region().offset());
      channel.write(buf);
    }
    catch(IOException e) {
      throw new StoreException(e.toString(), e);
    }
  }
  
}
