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
import java.util.LinkedList;
import java.util.function.IntFunction;
import us.pserver.tools.NotNull;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 18/09/2017
 */
public class FileChannelStorage implements Storage {
  
  public static final IntFunction<ByteBuffer> ALLOC_POLICY_DIRECT = ByteBuffer::allocateDirect;
  
  public static final IntFunction<ByteBuffer> ALLOC_POLICY_HEAP = ByteBuffer::allocate;
  
  
  public static final int MINIMUM_BLOCK_SIZE = 1024;
  
  public static final int DEFAULT_BLOCK_SIZE = 2048;
  
  public static final Region HEADER_REGION = Region.of(0, 1024);
  
  
  private final FileChannel channel;
  
  private final LinkedList<Region> frees;
  
  private final IntFunction<ByteBuffer> malloc;
  
  private final int blockSize;
  
  
  protected FileChannelStorage(FileChannel channel, IntFunction<ByteBuffer> allocPolicy, LinkedList<Region> freeBlocks, int blockSize) {
    this.channel = NotNull.of(channel).getOrFail("Bad null FileChannel");
    this.malloc = NotNull.of(allocPolicy).getOrFail("Bad null alloc policy");
    this.frees = NotNull.of(freeBlocks).getOrFail("Bad null free blocks list");
    this.blockSize = blockSize;
  }
  
  
  private Region nextRegion() throws StorageException {
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
      throw new StorageException(e.toString(), e);
    }
  }
  
  
  @Override
  public Block allocate() {
    ByteBuffer buf = malloc.apply(blockSize);
    Region reg = frees.isEmpty() ? nextRegion() : frees.pop();
    return new DefaultBlock(reg, buf).setNext(Region.of(0, 0));
  }


  @Override
  public void reallocate(Block blk) throws StorageException {
    try {
      this.frees.remove(blk.region());
      if(channel.size() <= blk.region().offset() 
          && blk.buffer().hasRemaining()) {
        this.put(blk);
      }
    }
    catch(IOException e) {
      throw new StorageException(e.toString(), e);
    }
  }


  @Override
  public void deallocate(Block blk) throws StorageException {
    NotNull.of(blk).failIfNull("Bad null Block");
    try {
      if(blk.region().offset() + blk.region().length() >= channel.size()) {
        //System.out.println("* Storage.deallocate: channel.truncate => "+ blk.region());
        channel.truncate(blk.region().offset());
      }
      else {
        //System.out.println("* Storage.deallocate: frees.add => "+ blk.region());
        this.frees.push(blk.region());
      }
    }
    catch(IOException e) {
      throw new StorageException(e.toString(), e);
    }
  }


  @Override
  public Block get(Region reg) throws StorageException {
    NotNull.of(reg).failIfNull("Bad null Region");
    try {
      channel.position(reg.offset());
      ByteBuffer buf = malloc.apply(reg.intLength());
      int read = channel.read(buf);
      if(read > 0) buf.flip();
      return new DefaultBlock(reg, buf);
    }
    catch(IOException e) {
      throw new StorageException(e.toString(), e);
    }
  }


  @Override
  public void put(Block blk) throws StorageException {
    NotNull.of(blk).failIfNull("Bad null Block");
    try {
      blk.buffer().position(0);
      blk.buffer().limit(blk.buffer().capacity());
      channel.position(blk.region().offset());
      channel.write(blk.buffer());
    }
    catch(IOException e) {
      throw new StorageException(e.toString(), e);
    }
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
  public void close() throws StorageException {
    try {
      Block blk = this.get(HEADER_REGION);
      ByteBuffer buf = blk.buffer();
      //System.out.println("FileChannelStorage.get: remaining="+ buf.remaining()+ ", capacity="+ buf.capacity());
      buf.putShort((short)0);
      buf.putInt(blockSize);
      for(Region r : frees) {
        buf.putLong(r.offset());
        buf.putLong(r.length());
      }
      while(buf.remaining() >= Long.BYTES) {
        buf.putLong(0l);
      }
      buf.flip();
      channel.position(blk.region().offset());
      channel.write(buf);
    }
    catch(IOException e) {
      throw new StorageException(e.toString(), e);
    }
  }
  
  
  @Override
  public StorageTransaction startTransaction() {
    return new StorageTransaction(this);
  }
  
}
