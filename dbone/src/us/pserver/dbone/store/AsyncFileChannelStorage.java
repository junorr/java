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
import java.nio.channels.AsynchronousFileChannel;
import java.util.LinkedList;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import us.pserver.dbone.store.fun.ConsumerHandler;
import us.pserver.tools.NotNull;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 18/09/2017
 */
public class AsyncFileChannelStorage extends AbstractStorage {
  
  private final AsynchronousFileChannel channel;
  
  
  protected AsyncFileChannelStorage(AsynchronousFileChannel channel, LinkedList<Region> freeBlocks, IntFunction<ByteBuffer> allocPolicy, int blockSize) {
    super(freeBlocks, allocPolicy, blockSize);
    this.channel = NotNull.of(channel).getOrFail("Bad null FileChannel");
  }
  
  
  @Override
  public long size() throws StorageException {
    return StorageException.rethrow(channel::size);
  }
  
  
  @Override
  public synchronized Block allocate() throws StorageException {
    return super.allocate();
  }


  @Override
  public Block get(Region reg) throws StorageException {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }


  @Override
  public void put(Block blk) throws StorageException {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
  
  
  public void get(Region reg, Consumer<Block> cs) throws StorageException {
    NotNull.of(reg).failIfNull("Bad null Region");
    ByteBuffer buf = malloc.apply(reg.intLength());
    ConsumerHandler<Block> ch = new <Block>ConsumerHandler(new DefaultBlock(reg, buf));
    channel.read(buf, reg.offset(), cs, ch);
  }


  public void put(Block blk, Consumer<Storage> cs) throws StorageException {
    NotNull.of(blk).failIfNull("Bad null Block");
    ConsumerHandler<Storage> ch = new <Storage>ConsumerHandler(this);
    channel.write(blk.buffer(), blk.region().offset(), cs, ch);
  }


  public void putAndClose(Block blk) throws StorageException {
    NotNull.of(blk).failIfNull("Bad null Block");
    ConsumerHandler<AsynchronousFileChannel> ch = new <AsynchronousFileChannel>ConsumerHandler(channel);
    channel.write(blk.buffer(), blk.region().offset(), c->StorageException.rethrow(c::close), ch);
  }


  @Override
  public void close() throws StorageException {
    this.get(HEADER_REGION, b->{
      b.buffer().putShort((short)0);
      b.buffer().putInt(blockSize);
      while(!frees.isEmpty() && b.buffer().remaining() > Region.BYTES) {
        Region r = frees.pop();
        b.buffer().putLong(r.offset());
        b.buffer().putLong(r.length());
      }
      while(b.buffer().remaining() >= Long.BYTES) {
        b.buffer().putLong(0l);
      }
      b.buffer().flip();
      putAndClose(b);
    });
  }

}
