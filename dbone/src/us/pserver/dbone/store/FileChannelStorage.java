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
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.LinkedList;
import java.util.function.IntFunction;
import us.pserver.tools.NotNull;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 18/09/2017
 */
public class FileChannelStorage extends AbstractStorage {
  
  private final FileChannel channel;
  
  
  protected FileChannelStorage(FileChannel channel, LinkedList<Region> freeBlocks, IntFunction<ByteBuffer> allocPolicy, int blockSize) {
    super(freeBlocks, allocPolicy, blockSize);
    this.channel = NotNull.of(channel).getOrFail("Bad null FileChannel");
  }
  
  
  @Override
  public long size() throws StorageException {
    return StorageException.rethrow(channel::size);
  }


  @Override
  public Block get(Region reg) throws StorageException {
    NotNull.of(reg).failIfNull("Bad null Region");
    StorageException.rethrow(()->channel.position(reg.offset()));
    ByteBuffer buf = malloc.apply(reg.intLength());
    FileLock lock = StorageException.rethrow(()->channel.lock(reg.offset(), reg.length(), true));
    int read = StorageException.rethrow(()->channel.read(buf));
    if(read > 0) buf.flip();
    StorageException.rethrow(lock::release);
    return new DefaultBlock(reg, buf);
  }


  @Override
  public void put(Block blk) throws StorageException {
    NotNull.of(blk).failIfNull("Bad null Block");
    blk.buffer().position(0);
    blk.buffer().limit(blk.buffer().capacity());
    FileLock lock = StorageException.rethrow(()->
        channel.lock(blk.region().offset(), blk.region().length(), false)
    );
    StorageException.rethrow(()->channel.position(blk.region().offset()));
    StorageException.rethrow(()->channel.write(blk.buffer()));
    StorageException.rethrow(lock::release);
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
    buf.flip();
    FileLock lock = StorageException.rethrow(()->
        channel.lock(blk.region().offset(), blk.region().length(), false)
    );
    StorageException.rethrow(()->channel.position(blk.region().offset()));
    StorageException.rethrow(()->channel.write(buf));
    StorageException.rethrow(lock::release);
    StorageException.rethrow(channel::close);
  }
  
}
