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
import us.pserver.tools.Match;
import us.pserver.tools.exp.ForEach;
import us.pserver.tools.exp.While;
import us.pserver.tools.io.ByteBufferOutputStream;
import us.pserver.tools.io.ByteableNumber;


/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 27/10/2017
 */
public class FileStorage implements Storage {
  
  public static final int DEFAULT_BLOCK_SIZE = 128;
  
  public static final int MIN_BLOCK_SIZE = Region.BYTES + Integer.BYTES + 1;
  
  public static final Path FILE_STORAGE = Paths.get("./storage.dat");
  
  public static final Path FILE_FREE_BLOCKS = Paths.get("./freeblocks.dat");
  
  public static final int MIN_REGION_COUNT = 5;
  
  public static final int MAX_REGION_COUNT = 50;
  
  
  private final int blksize;
  
  private final int writelength;
  
  private final IntFunction<ByteBuffer> allocPolicy;
  
  private final RWRegionControl regions;
  
  private final FileChannel channel;
  
  private final Path freepath;
  
  private final Path storepath;
  
  
  public FileStorage(Path directory, int blockSize, IntFunction<ByteBuffer> allocPolicy) {
    this(directory, blockSize, allocPolicy, new RWRegionControl(new FileSizeRegionControl(
        directory.resolve(FILE_STORAGE), 0, 
        blockSize, MIN_REGION_COUNT, 
        MAX_REGION_COUNT)
    ));
    if(Files.exists(freepath)) {
      StorageException.rethrow(()->regions.readFrom(freepath));
    }
  }
  
  
  public FileStorage(Path directory, int blockSize, IntFunction<ByteBuffer> allocPolicy, RWRegionControl rgc) {
    if(directory == null || !Files.isDirectory(directory)) {
      throw new IllegalArgumentException("Bad directory Path");
    }
    if(blockSize < MIN_BLOCK_SIZE) {
      throw new IllegalArgumentException(
          String.format("Bad block size (<%d)", MIN_BLOCK_SIZE)
      );
    }
    this.freepath = directory.resolve(FILE_FREE_BLOCKS);
    this.storepath = directory.resolve(FILE_STORAGE);
    this.blksize = blockSize;
    this.writelength = blksize - Block.META_BYTES;
    this.allocPolicy = Match.notNull(allocPolicy).getOrFail("Bad null alloc policy");
    this.regions = Match.notNull(rgc).getOrFail("Bad null RegionControl");
    this.channel = openRW(storepath);
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
  
  
  private void print(ByteBuffer buf) {
    System.out.printf("-- %s --%n", buf);
    int pos = buf.position();
    while(buf.hasRemaining()) {
      System.out.printf(" %d", buf.get());
    }
    System.out.println();
    buf.position(pos);
  }
  
  
  @Override
  public Region put(ByteBuffer ... buf) throws IOException {
    if(buf == null || buf.length < 1) return Region.invalid();
    int totalSize = ForEach.of(buf).reduce(0, (t,b)->t+=b.remaining());
    for(int i = 0; i < buf.length; i++) {
      ByteBuffer b = buf[i];
      if(b.remaining() > writelength) {
        
      }
      else {
        Region reg = regions.allocate();
        channel.position(reg.offset());
        Block.root(b, Region.invalid()).writeTo(channel);
        
      }
    }
    int i = -1;
    Region reg = Region.invalid();
    Region next = Region.invalid();
    while(totalSize > 0 && ++i < buf.length) {
      next = regions.allocate();
      putNextRegion(reg, next);
      reg = next;
      totalSize -= buf[i].remaining();
      reg = put(buf[i], reg);
    }
    return reg;
  }
  
  
  public Region put2(ByteBuffer ... buf) throws IOException {
    if(buf == null || buf.length < 1) return Region.invalid();
    int size = ForEach.of(buf).reduce(0, (t,b)->t+=b.remaining());
    Region reg = regions.allocate();
    StorageException.rethrow(()->put(buf, 0, size, reg));
    return reg;
  }
  
  
  private Region put(ByteBuffer[] buf, int idx, int size, Region reg) throws IOException {
    if(idx >= buf.length || size <= 0 || !reg.isValid()) {
      if(reg.isValid()) {
        regions.offer(reg);
        putNextRegion(reg, Region.invalid());
      }
      return Region.invalid();
    }
    reg = put(buf[idx], reg);
    Region next = regions.allocate();
    putNextRegion(reg, next);
    return put(buf, ++idx, size - buf[idx].remaining(), next);
  }
  
  
  private Region put2(ByteBuffer[] buf, int idx, int size) throws IOException {
    if(idx >= buf.length || size <= 0) {
      return Region.invalid();
    }
    Region reg = regions.allocate();
    
    reg = put(buf[idx], reg);
    Region next = regions.allocate();
    putNextRegion(reg, next);
    return put(buf, ++idx, size - buf[idx].remaining(), next);
  }
  
  
  private Region put(ByteBuffer buf, Region reg) throws IOException {
    if(!buf.hasRemaining()) return Region.invalid();
    if(buf.remaining() > writelength) {
      Region next = putLargerThanBlockSize(buf, reg);
      return put(buf, next);
    }
    else {
      putSmallerThanBlockSize(buf, reg);
      return reg;
    }
  }
  
  
  private void putSmallerThanBlockSize(ByteBuffer buf, Region reg) throws IOException {
    print(buf);
    channel.position(reg.offset());
    Block.node(buf, Region.invalid()).writeTo(channel);
  }
  
  
  private Region putLargerThanBlockSize(ByteBuffer buf, Region reg) throws IOException {
    int lim = buf.limit();
    buf.limit(buf.position() + writelength);
    print(buf);
    Region next = regions.allocate();
    channel.position(reg.offset());
    //Block.
    //channel.write(ByteableNumber.of(buf.remaining()).toByteBuffer());
    channel.write(buf);
    channel.write(next.toByteBuffer());
    buf.limit(lim);
    return next;
  }


  @Override
  public ByteBuffer get(Region reg) throws IOException {
    if(reg == null || !reg.isValid()) {
      throw new IllegalArgumentException("Bad Region: "+ reg);
    }
    ByteBufferOutputStream bos = new ByteBufferOutputStream(allocPolicy);
    ByteBuffer buf = allocPolicy.apply(blksize);
    get(reg, buf, bos);
    return bos.toByteBuffer();
  }
  
  
  private void get(Region r, ByteBuffer b, ByteBufferOutputStream s) throws IOException {
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
    b.position(blksize - Region.BYTES);
    return Region.of(b);
  }


  private void putNextRegion(Region cur, Region next) {
    if(cur.isValid()) {
      StorageException.rethrow(()->{
        channel.position(cur.end() - Region.BYTES);
        channel.write(next.toByteBuffer());
      });
    }
  }
  
  
  @Override
  public long size() throws StorageException {
    return StorageException.rethrow(channel::size);
  }
  
  
  @Override
  public void close() throws IOException {
    channel.close();
    regions.writeTo(freepath);
  }


  @Override
  public ByteBuffer remove(Region reg) throws IOException {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
  
}
