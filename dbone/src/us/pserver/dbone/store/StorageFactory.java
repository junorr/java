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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.LinkedList;
import java.util.function.IntFunction;
import static us.pserver.dbone.store.AbstractStorage.HEADER_REGION;
import us.pserver.tools.NotNull;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 18/09/2017
 */
public class StorageFactory {
  
  public static final IntFunction<ByteBuffer> ALLOC_POLICY_DIRECT = ByteBuffer::allocateDirect;
  
  public static final IntFunction<ByteBuffer> ALLOC_POLICY_HEAP = ByteBuffer::allocateDirect;
  
  public static final int MINIMUM_BLOCK_SIZE = 1024;
  
  public static final int DEFAULT_BLOCK_SIZE = 2048;
  

  private final Path path;
  
  private final int blockSize;
  
  private final IntFunction<ByteBuffer> malloc;
  
  private final boolean openForced;
  
  
  private StorageFactory(Path path, int blockSize, IntFunction<ByteBuffer> malloc, boolean openForced) {
    this.path = path;
    this.blockSize = blockSize;
    this.malloc = malloc;
    this.openForced = openForced;
  }
  
  
  public static StorageFactory newFactory() {
    return new StorageFactory(null, DEFAULT_BLOCK_SIZE, ALLOC_POLICY_HEAP, false);
  }
  
  
  public StorageFactory setFile(String file) {
    return new StorageFactory(
        Paths.get(NotNull.of(file).getOrFail("Bad null file name")), 
        this.blockSize, this.malloc, this.openForced
    );
  }
  
  
  public StorageFactory setFile(Path file) {
    return new StorageFactory(
        NotNull.of(file).getOrFail("Bad null file path"), 
        this.blockSize, this.malloc, this.openForced
    );
  }
  
  
  public StorageFactory setOpenForced() {
    return new StorageFactory(
        this.path, this.blockSize, this.malloc, true
    );
  }
  
  
  public StorageFactory setAllocationPolicy(IntFunction<ByteBuffer> malloc) {
    return new StorageFactory(
        this.path, this.blockSize, 
        NotNull.of(malloc).getOrFail("Bad null allocation policy"), 
        this.openForced
    );
  }
  
  
  public StorageFactory setBlockSize(int blockSize) {
    return new StorageFactory(
        this.path, blockSize, this.malloc, this.openForced
    );
  }
  
  
  public Storage createDirect(int size) {
    LinkedList<Region> frees = new LinkedList<>();
    int offset = HEADER_REGION.intLength();
    while(offset < size) {
      frees.add(Region.of(offset, blockSize));
      offset += blockSize;
    }
    return new DirectMemoryStorage(size, frees, this.blockSize);
  }
  
  
  public Storage create() throws IOException {
    NotNull.of(this.path).failIfNull("Bad null file path");
    if(this.blockSize < MINIMUM_BLOCK_SIZE) {
      throw new StorageException("Bad block size. Minimum allowed: "+ MINIMUM_BLOCK_SIZE);
    }
    FileChannel ch = FileChannel.open(path, 
        StandardOpenOption.CREATE, 
        StandardOpenOption.READ, 
        StandardOpenOption.WRITE
    );
    long size = ch.size();
    if(size > 0) {
      if(this.blockSize != DEFAULT_BLOCK_SIZE) {
        throw new IOException("Can not set block size for an existent storage");
      }
      return this.open(ch);
    }
    else {
      return this.create(ch);
    }
  }
  
  
  private FileChannelStorage create(FileChannel ch) throws IOException {
    ByteBuffer buf = this.malloc.apply(FileChannelStorage.HEADER_REGION.intLength());
    buf.putShort((short)1);
    buf.putInt(this.blockSize);
    while(buf.remaining() >= Long.BYTES) {
      buf.putLong(0l);
    }
    buf.flip();
    ch.position(FileChannelStorage.HEADER_REGION.offset());
    ch.write(buf);
    return new FileChannelStorage(ch, new LinkedList<>(), this.malloc, this.blockSize);
  }
  
  
  private FileChannelStorage open(FileChannel ch) throws IOException {
    if(this.blockSize != DEFAULT_BLOCK_SIZE) {
      throw new IOException("Can not set block size for an existent storage");
    }
    LinkedList<Region> freeblks = new LinkedList<>();
    ByteBuffer buf = this.malloc.apply(FileChannelStorage.HEADER_REGION.intLength());
    ch.position(FileChannelStorage.HEADER_REGION.offset());
    ch.read(buf);
    buf.flip();
    if(buf.getShort() == 1 && !this.openForced) {
      throw new IOException("File storage is locked by another process");
    }
    int blksize = buf.getInt();
    while(buf.remaining() >= Region.BYTES) {
      Region r = Region.of(buf.getLong(), buf.getLong());
      if(r.offset() + r.length() > 0) {
        freeblks.add(r);
      }
    }
    return new FileChannelStorage(ch, freeblks, this.malloc, blksize);
  }
  
}
