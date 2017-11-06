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

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Iterator;
import java.util.function.IntFunction;
import us.pserver.dbone.store.StorageException;
import us.pserver.tools.NotNull;
import us.pserver.tools.io.ByteBufferOutputStream;
import us.pserver.tools.io.ByteableNumber;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 27/10/2017
 */
public class FileStorage implements Storage {
  
  public static final Path FILE_STORAGE = Paths.get("./storage.dat");
  
  public static final Path FILE_FREE_BLOCKS = Paths.get("./freeblocks.dat");
  
  public final int MIN_BLOCK_SIZE = Region.BYTES + Integer.BYTES + 1;
  
  public final int BUFFER_SIZE = 4096;
  
  
  private final int blksize;
  
  private final int writelenght;
  
  private final IntFunction<ByteBuffer> allocPolicy;
  
  private final RegionAllocPolicy ralloc;
  
  private final FileChannel channel;
  
  private final Path freepath;
  
  
  public FileStorage(Path directory, int blockSize, IntFunction<ByteBuffer> allocPolicy) {
    if(directory == null || !Files.isDirectory(directory)) {
      throw new IllegalArgumentException("Bad directory Path");
    }
    if(blockSize < MIN_BLOCK_SIZE) {
      throw new IllegalArgumentException(
          String.format("Bad block size (<%d)", MIN_BLOCK_SIZE)
      );
    }
    this.blksize = blockSize;
    this.writelenght = blksize - Region.BYTES - Integer.BYTES;
    this.allocPolicy = NotNull.of(allocPolicy).getOrFail("Bad null IntFunction<ByteBuffer> alloc policy");
    this.ralloc = new FileSizeAllocPolicy(directory.resolve(FILE_STORAGE), 0, blksize, 2, 200);
    this.channel = openRW(directory.resolve(FILE_STORAGE));
    this.freepath = directory.resolve(FILE_FREE_BLOCKS);
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
    if(!Files.exists(freepath)) return;
    try (
        FileChannel ch = openR(freepath);
        ) {
      ByteBuffer bb = allocPolicy.apply(BUFFER_SIZE);
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
    finally {
      StorageException.rethrow(()->Files.delete(freepath));
    }
  }
  
  
  @Override
  public Region put(ByteBuffer ... bufs) throws IOException {
    if(bufs == null || bufs.length < 1) return Region.of(-1, -1);
    Region reg = ralloc.next();
    Region cur = reg;
    channel.position(cur.offset());
    int remaining = writelenght;
    int index = 0;
    ByteBuffer buf = bufs[index];
    
    while(buf.hasRemaining() && index < bufs.length) {
      int len = Math.min(remaining, buf.remaining());
      int lim = buf.limit();
      buf.limit(buf.position() + len);
      channel.write(buf);
      buf.limit(lim);
      remaining = remaining - len;
      if((index + 1) < bufs.length) {
        if(remaining <= 0) {
          cur = ralloc.next();
          channel.write(cur.toByteBuffer());
          channel.position(cur.offset());
          remaining = writelenght;
        }
        else {
          buf = bufs[++index];
        }
      }
    }
    return reg;
  }


  public void put(ByteBuffer buf, long off, int len) throws IOException {
    if(!buf.hasRemaining()) return;
    if(channel.position() != off) {
      channel.position(off);
    }
    int size = Math.min(len, buf.remaining());
    int lim = buf.limit();
    buf.limit(buf.position() + size);
    channel.write(buf);
    buf.limit(lim);
  }
  
  
  @Override
  public Region put(ByteBuffer buf) throws StorageException {
    if(!buf.hasRemaining()) return Region.of(-1, -1);
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
    StorageException.rethrow(()->{
      channel.position(reg.offset());
      channel.write(ByteableNumber.of(buf.remaining()).toByteBuffer());
      channel.write(buf);
    });
  }
  
  
  private Region putLargestThanBlockSize(ByteBuffer buf, Region reg) {
    int lim = buf.limit();
    buf.limit(buf.position() + writelenght);
    Region next = ralloc.next();
    StorageException.rethrow(()->{
      channel.position(reg.offset());
      channel.write(ByteableNumber.of(buf.remaining()).toByteBuffer());
      channel.write(buf);
      channel.write(next.toByteBuffer());
    });
    buf.limit(lim);
    return next;
  }


  @Override
  public ByteBuffer get(Region reg) throws StorageException {
    if(reg == null || reg.offset() < 0 || reg.length() < 1) {
      throw new IllegalArgumentException("Bad Region: "+ reg);
    }
    ByteBufferOutputStream bos = new ByteBufferOutputStream(allocPolicy);
    ByteBuffer buf = allocPolicy.apply(blksize);
    get(reg, buf, bos);
    return bos.toByteBuffer();
  }
  
  
  private void get(Region r, ByteBuffer b, ByteBufferOutputStream s) {
    b.clear();
    StorageException.rethrow(()->{
      channel.position(r.offset());
      channel.read(b);
    });
    b.flip();
    if(!b.hasRemaining()) return;
    int size = b.getInt();
    b.limit(b.position() + size); 
    s.write(b);
    Region next = readNextRegion(b);
    if(next.isValid()) get(next, b, s);
  }
  
  
  private Region readNextRegion(ByteBuffer b) {
    b.limit(blksize);
    b.position(Integer.BYTES + writelenght);
    return Region.of(b.getLong(), b.getLong());
  }


  @Override
  public long size() throws StorageException {
    return StorageException.rethrow(channel::size);
  }
  
  
  @Override
  public ByteBuffer allocateBuffer(int size) {
    return allocPolicy.apply(size);
  }


  @Override
  public void close() throws StorageException {
    try (
        FileChannel ch = openRW(freepath);
        ) {
      channel.close();
      ByteBuffer buf = allocPolicy.apply(BUFFER_SIZE);
      Iterator<Region> regs = ralloc.freeRegions();
      while(regs.hasNext()) {
        Region r = regs.next();
        buf.putLong(r.offset());
        buf.putLong(r.length());
        if(buf.remaining() < Region.BYTES) {
          buf.flip();
          ch.write(buf);
          buf.compact();
        }
      }
      buf.flip();
      if(buf.hasRemaining()) {
        ch.write(buf);
      }
    }
    catch(IOException e) {
      throw new StorageException(e.toString(), e);
    }
  }
  
}
