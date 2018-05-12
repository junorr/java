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
import java.util.concurrent.LinkedBlockingDeque;
import java.util.function.IntFunction;
import us.pserver.tools.io.ByteBufferOutputStream;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 11/05/2018
 */
public class FileChannelStorage implements Storage {
  
  private final Path path;
  
  private final FileChannel channel;
  
  private final StorageHeader header;
  
  private final LinkedBlockingDeque<Region> freebs;
  
  private final IntFunction<ByteBuffer> alloc;
  
  private final int writable;
  
  
  public FileChannelStorage(Path path, FileChannel ch, StorageHeader shd, LinkedBlockingDeque<Region> freeRegions, IntFunction<ByteBuffer> alloc) {
    this.path = Objects.requireNonNull(path, "Bad null Path");
    this.channel = Objects.requireNonNull(ch, "Bad null FileChannel");
    this.header = Objects.requireNonNull(shd, "Bad null StorageHeader");
    this.freebs = Objects.requireNonNull(freeRegions, "Bad null free regions Deque");
    this.alloc = Objects.requireNonNull(alloc, "Bad null alloc policy IntFunction<ByteBuffer>");
    this.writable = shd.getBlockSize() - Block.META_BYTES;
  }
  
  
  @Override
  public Region put(ByteBuffer ... buf) throws IOException {
    Region first = nextFree();
    Region cur = first;
    for(int i = 0; i < buf.length; i++) {
      if(buf[i].remaining() > 0) {
        cur = putOne(cur, buf[i]);
      }
      else {
        freebs.add(cur);
      }
      if(buf.length > (i + 1)) {
        Region next = nextFree();
        setNext(cur, next);
        cur = next;
      }
    }
    return first;
  }
  
  
  private Region putOne(Region reg, ByteBuffer buf) throws IOException {
    if(buf.remaining() > writable) {
      return putLarger(reg, buf);
    }
    else {
      return putSmaller(reg, buf);
    }
  }
  
  
  private void setNext(Region reg, Region next) throws IOException {
    channel.position(reg.offset());
    channel.write(next.toByteBuffer());
  }
  
  
  private Region putLarger(Region reg, ByteBuffer buf) throws IOException {
    int lim = buf.limit();
    buf.limit(buf.position() + writable);
    Block blk = Block.root(reg, buf.slice(), Region.invalid());
    buf.limit(lim);
    Region last = Region.invalid();
    while(blk.region().isValid() && blk.buffer().remaining() > 0) {
      if(buf.remaining() > writable) {
        last = nextFree();
        blk = blk.withNext(last);
      }
      channel.position(blk.region().offset());
      channel.write(blk.toByteBuffer());
      buf.limit(Math.min(lim, buf.position() + writable));
      blk = Block.node(last, buf.slice(), Region.invalid());
      buf.limit(lim);
    }
    return last;
  }
  
  
  private Region putSmaller(Region reg, ByteBuffer buf) throws IOException {
    Block blk = Block.root(reg, buf, Region.invalid());
    blk.writeTo(channel);
    return blk.region();
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
  public ByteBuffer remove(Region reg) throws IOException {
    if(reg != null && reg.isValid()) {
      ByteBuffer buf = get(reg);
      freebs.add(reg);
      return buf;
    }
    return ByteBuffer.wrap(new byte[]{});
  }


  @Override
  public long size() throws IOException {
    return channel.size();
  }


  @Override
  public void close() throws IOException {
    Region freeRegion;
    if(freebs.isEmpty()) {
      freeRegion = Region.invalid();
    }
    else {
      ByteBuffer buf = ByteBuffer.allocateDirect(Region.BYTES * freebs.size());
      freebs.forEach(r->r.writeTo(buf));
      buf.flip();
      freeRegion = this.put(buf);
    }
    StorageHeader hd = StorageHeader.of(freeRegion, header.getBlockSize());
    Block blk = Block.root(hd.toByteBuffer(), Region.invalid());
    channel.position(0);
    channel.write(blk.toByteBuffer());
  }
  
  
  private Region nextFree() throws IOException {
    Region reg;
    if(!freebs.isEmpty()) {
      reg = freebs.poll();
    }
    else {
      reg = Region.of(channel.size(), header.getBlockSize());
    }
    return reg;
  }
  
  
}
