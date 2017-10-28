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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.function.IntFunction;
import us.pserver.tools.NotNull;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 27/10/2017
 */
public class NFileStorage implements NStorage {
  
  public static final Path FILE_STORAGE = Paths.get("./storage.dat");
  
  public static final Path FILE_FREE_BLOCKS = Paths.get("./freeblocks.dat");
  
  public final int MIN_BLOCK_SIZE = 128;
  
  public final int BUFFER_SIZE = 4096;
  
  
  private final int blksize;
  
  private final int writelenght;
  
  private final IntFunction<ByteBuffer> allocPolicy;
  
  private final RegionAllocPolicy ralloc;
  
  private final FileChannel channel;
  
  
  
  public NFileStorage(Path directory, int blockSize, IntFunction<ByteBuffer> allocPolicy) {
    if(directory == null || Files.isDirectory(directory)) {
      throw new IllegalArgumentException("Bad directory Path");
    }
    if(blockSize < MIN_BLOCK_SIZE) {
      throw new IllegalArgumentException(
          String.format("Bad block size (<%d)", MIN_BLOCK_SIZE)
      );
    }
    this.blksize = blockSize;
    this.writelenght = blksize - Region.BYTES;
    this.allocPolicy = NotNull.of(allocPolicy).getOrFail("Bad null IntFunction<ByteBuffer> alloc policy");
    this.ralloc = new FileSizeAllocPolicy(directory.resolve(FILE_STORAGE), 0, blksize, 2, 200);
    this.channel = openRW(directory.resolve(FILE_STORAGE));
    this.readFreeBlocks();
  }
  
  
  private FileChannel openRW(Path path) {
    try {
      return FileChannel.open(path, 
          StandardOpenOption.CREATE, 
          StandardOpenOption.SYNC, 
          StandardOpenOption.READ, 
          StandardOpenOption.WRITE
      );
    }
    catch(IOException e) {
      throw new StorageException(e.toString(), e);
    }
  }
  
  
  private FileChannel openR(Path path) {
    try {
      return FileChannel.open(path, 
          StandardOpenOption.READ
      );
    }
    catch(IOException e) {
      throw new StorageException(e.toString(), e);
    }
  }
  
  
  private void readFreeBlocks() {
    if(!Files.exists(FILE_FREE_BLOCKS)) return;
    try {
      ByteBuffer bb = allocPolicy.apply(BUFFER_SIZE);
      FileChannel ch = openR(FILE_FREE_BLOCKS);
      while(ch.read(bb) != -1) {
        bb.flip();
        while(bb.remaining() >= Long.BYTES * 2) {
          ralloc.offer(Region.of(bb.getLong(), bb.getLong()));
        }
        bb.compact();
      }
    }
    catch(IOException e) {
      throw new StorageException(e.toString(), e);
    }
  }
  
  
  @Override
  public Region put(ByteBuffer buf) throws StorageException {
    if(!buf.hasRemaining()) return Region.of(0, 0);
    Region reg = ralloc.next();
    put(buf, reg);
    return reg;
  }
  
  
  private void put(ByteBuffer buf, Region reg) throws StorageException {
    if(!buf.hasRemaining()) return;
    if(buf.remaining() > writelenght) {
      Region next = putLargestThanBlockSize(buf, reg);
      put(buf, next);
    }
    else {
      putSmallerThanBlockSize(buf, reg);
    }
  }
  
  
  private void putSmallerThanBlockSize(ByteBuffer buf, Region reg) {
    ByteBuffer dif = ByteBuffer.allocate(blksize - buf.remaining());
    StorageException.rethrow(()->channel.write(buf, reg.offset()));
    StorageException.rethrow(()->channel.write(dif));
  }
  
  
  private Region putLargestThanBlockSize(ByteBuffer buf, Region reg) {
    int lim = buf.limit();
    buf.limit(buf.position() + writelenght);
    Region next = ralloc.next();
    StorageException.rethrow(()->channel.write(buf.slice(), reg.offset()));
    StorageException.rethrow(()->channel.write(next.toByteBuffer()));
    buf.limit(lim);
    return next;
  }


  @Override
  public ByteBuffer get(Region reg) throws StorageException {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }


  @Override
  public long size() throws StorageException {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }


  @Override
  public void close() throws StorageException {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
  
}
