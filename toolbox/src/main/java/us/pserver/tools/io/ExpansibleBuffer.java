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

package us.pserver.tools.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 19/10/2018
 */
public class ExpansibleBuffer implements IBuffer {
  
  private final List<IBuffer> buffers;
  
  private final int growSize;
  
  private int bufidx;
  
  
  public ExpansibleBuffer(int growSize) {
    this.growSize = growSize;
    this.buffers = new LinkedList<>();
    bufidx = 0;
  }
  

  @Override
  public int capacity() {
    return buffers.stream()
        .mapToInt(IBuffer::capacity)
        .sum();
  }


  @Override
  public int size() {
    return buffers.stream()
        .skip(bufidx)
        .mapToInt(IBuffer::size)
        .sum();
  }


  @Override
  public boolean isEmpty() {
    return buffers.isEmpty();
  }
  
  
  public int seek(int pos) {
    if(pos < 0 || pos > size()) {
      throw new IllegalArgumentException("Bad position: "+ pos);
    }
    int count = pos;
    bufidx = 0;
    while(count > 0 && bufidx < buffers.size()) {
      IBuffer b = buffers.get(bufidx);
      b.seek(0);
      int len = Math.min(b.size(), count);
      b.seek(len);
      count -= len;
      bufidx++;
    }
    return pos - count;
  }


  @Override
  public int indexOf(byte[] cont) {
    return indexOf(Buffer.of(cont, 0, cont.length));
  }


  @Override
  public int indexOf(IBuffer buf) {
    return buffers.stream()
        .skip(bufidx)
        .mapToInt(b -> b.indexOf(buf))
        .filter(i -> i > 0)
        .findAny()
        .orElse(-1);
  }


  @Override
  public int fill(InputStream in) throws IOException {
    Buffer b = new Buffer(growSize);
    b.fill(in);
    buffers.add(b);
    return b.size();
  }


  @Override
  public int fill(ByteBuffer buf) {
    Buffer b = Buffer.of(buf);
    buffers.add(b);
    return b.size();
  }


  @Override
  public int fill(IBuffer buf) {
    Buffer b = Buffer.of(buf);
    buffers.add(b);
    return b.size();
  }
  
  
  @Override
  public int fill(IBuffer buf, int length) {
    if(length < 1) return length;
    int len = Math.min(length, buf.size());
    Buffer b = new Buffer(len);
    b.fill(buf, len);
    buffers.add(b);
    return b.size();
  }
  
  
  @Override
  public int fill(byte[] src, int ofs, int len) {
    Objects.requireNonNull(src, "Bad null byte array");
    if(ofs < 0 || len < 1 || ofs + len > src.length) {
      throw new IllegalArgumentException(String.format("Bad offset/length: %d/%d", ofs, len));
    }
    Buffer b = new Buffer(len);
    b.fill(src, ofs, len);
    buffers.add(b);
    return b.size();
  }


  @Override
  public int writeTo(OutputStream out) throws IOException {
    int count = 0;
    for(IBuffer b : buffers) {
      count += b.writeTo(out);
    }
    return count;
  }


  @Override
  public int writeTo(OutputStream out, int length) throws IOException {
    int count = 0;
    for(IBuffer b : buffers) {
      count += b.writeTo(out, Math.min(length - count, b.size()));
      if(count >= length) break;
    }
    return count;
  }


  @Override
  public int writeTo(ByteBuffer out) {
    int count = 0;
    for(IBuffer b : buffers) {
      count += b.writeTo(out, Math.min(out.remaining() - count, b.size()));
      if(!out.hasRemaining()) break;
    }
    return count;
  }


  @Override
  public int writeTo(ByteBuffer out, int length) {
    int count = 0;
    for(IBuffer b : buffers) {
      count += b.writeTo(out, Math.min(length - count, b.size()));
      if(count >= length) break;
    }
    return count;
  }


  @Override
  public int writeTo(IBuffer out) {
    int count = 0;
    for(IBuffer b : buffers) {
      count += b.writeTo(out, Math.min(out.capacity() - out.size(), b.size()));
      if(out.size() >= out.capacity()) break;
    }
    return count;
  }


  @Override
  public int writeTo(IBuffer out, int length) {
    int count = 0;
    for(IBuffer b : buffers) {
      count += b.writeTo(out, Math.min(length - count, b.size()));
      if(count >= length) break;
    }
    return count;
  }


  @Override
  public int writeTo(byte[] out, int ofs, int len) {
    Objects.requireNonNull(out, "Bad null byte array");
    if(ofs < 0 || len < 1 || ofs + len > out.length) {
      throw new IllegalArgumentException(String.format("Bad offset/length: %d/%d", ofs, len));
    }
    int count = 0;
    for(IBuffer b : buffers) {
      count += b.writeTo(out, count, Math.min(len - count, b.size()));
      if(count >= len) break;
    }
    return count;
  }


  @Override
  public ByteBuffer toByteBuffer() {
    ByteBuffer bb = ByteBuffer.allocate(this.size());
    this.writeTo(bb);
    bb.flip();
    return bb;
  }


  @Override
  public byte[] toByteArray() {
    byte[] bs = new byte[this.size()];
    this.writeTo(bs, 0, bs.length);
    return bs;
  }


  @Override
  public String toString() {
    return String.format("ExpansibleBuffer[capacity=%d, size=%d]", capacity(), size());
  }
  

  @Override
  public String toString(Charset cs) {
    return cs.decode(toByteBuffer()).toString();
  }

}
