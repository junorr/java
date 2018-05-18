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
import java.util.Objects;
import java.util.function.IntFunction;
import us.pserver.tools.Match;
import us.pserver.tools.io.ByteBufferOutputStream;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 11/05/2018
 */
public class ReadableFileStorage implements ReadableStorage {
  
  private final Path path;
  
  private final FileChannel channel;
  
  private final int blksize;
  
  private final IntFunction<ByteBuffer> alloc;
  
  
  public ReadableFileStorage(Path path, FileChannel ch, int blockSize, IntFunction<ByteBuffer> alloc) {
    this.path = Objects.requireNonNull(path, "Bad null Path");
    this.channel = Objects.requireNonNull(ch, "Bad null FileChannel");
    this.blksize = Match.of(blockSize, b->b > FileChannelStorage.MIN_BLOCK_SIZE)
        .getOrFail("Bad block size = %d (< %d)", blockSize, FileChannelStorage.MIN_BLOCK_SIZE);
    this.alloc = Objects.requireNonNull(alloc, "Bad null alloc policy IntFunction<ByteBuffer>");
  }
  
  
  @Override
  public ByteBuffer get(Region reg) throws IOException {
    if(reg == null || !reg.isValid()) {
      throw new IllegalArgumentException("get(>> Region reg <<): Bad invalid Region: "+ reg);
    }
    ByteBufferOutputStream bbos = new ByteBufferOutputStream(alloc);
    Region cur = reg;
    while(cur.isValid()) {
      channel.position(cur.offset());
      Block blk = Block.read(channel);
      bbos.write(blk.buffer());
      cur = blk.nextRegion();
    }
    return bbos.toByteBuffer(alloc);
  }
  
  
  @Override
  public long size() throws IOException {
    return channel.size();
  }


  @Override
  public void close() throws Exception {
    channel.close();
  }
  
  
  @Override
  public ByteBuffer allocBuffer(int size) {
    return alloc.apply(size);
  }
  
}
