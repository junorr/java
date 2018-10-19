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
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 18/10/2018
 */
public class Buffer implements IBuffer {

  private final byte[] buffer;
  
  private int size;
  
  private int offset;
  
  
  public Buffer(int bufsize) {
    if(bufsize < 1) {
      throw new IllegalArgumentException("Bad buffer size: "+ bufsize);
    }
    buffer = new byte[bufsize];
    size = 0;
    offset = 0;
  }
  
  @Override
  public int capacity() {
    return buffer.length;
  }
  
  @Override
  public int size() {
    return size - offset;
  }
  
  @Override
  public boolean isEmpty() {
    return size() <= 0;
  }
  
  @Override
  public int seek(int pos) {
    if(pos < 0 || pos >= size) {
      throw new IllegalArgumentException("Bad position: "+ pos);
    }
    offset = pos;
    return offset;
  }
  
  @Override
  public int indexOf(byte[] cont) {
    int idx = 0;
    while((idx + cont.length) < size) {
      int count = 0;
      for(int i = 0; i < cont.length; i++) {
        if(buffer[idx + i] == cont[i]) count++;
      }
      if(count == cont.length) return idx;
      idx++;
    }
    return -1;
  }
  
  @Override
  public int indexOf(IBuffer buf) {
    return indexOf(buf.toByteArray());
  }
  
  @Override
  public int fill(InputStream in) throws IOException {
    size = in.read(buffer);
    offset = 0;
    return size;
  }
  
  @Override
  public int fill(byte[] src, int ofs, int len) {
    Objects.requireNonNull(src, "Bad null byte array");
    if(ofs < 0 || len < 1 || ofs + len > src.length) {
      throw new IllegalArgumentException(String.format("Bad offset/length: %d/%d", ofs, len));
    }
    size = Math.min(len, size());
    offset = 0;
    System.arraycopy(src, ofs, buffer, 0, size);
    return size;
  }
  
  @Override
  public int fill(ByteBuffer buf) {
    size = Math.min(buffer.length, buf.remaining());
    offset = 0;
    buf.get(buffer, 0, size);
    return size;
  }
  
  @Override
  public int fill(IBuffer buf) {
    size = Math.min(buffer.length, buf.size());
    offset = 0;
    System.arraycopy(((Buffer)buf).buffer, 0, buffer, 0, size);
    return size;
  }
  
  @Override
  public int fill(IBuffer buf, int length) {
    if(length < 1) return length;
    size = Math.min(capacity(), Math.min(length, buf.size()));
    offset = 0;
    System.arraycopy(((Buffer)buf).buffer, 0, buffer, 0, size);
    return size;
  }
  
  @Override
  public int writeTo(OutputStream out) throws IOException {
    int len = size();
    out.write(buffer, offset, len);
    offset += len;
    return size;
  }
  
  @Override
  public int writeTo(OutputStream out, int length) throws IOException {
    if(length < 1) return length;
    int len = Math.min(size(), length);
    out.write(buffer, offset, len);
    offset += len;
    return size;
  }
  
  @Override
  public int writeTo(ByteBuffer out) {
    int len = Math.min(size(), out.remaining());
    out.put(buffer, offset, len);
    offset += len;
    return len;
  }
  
  @Override
  public int writeTo(ByteBuffer out, int length) {
    if(length < 1) return length;
    int len = Math.min(size(), Math.min(length, out.remaining()));
    out.put(buffer, offset, len);
    offset += len;
    return len;
  }
  
  @Override
  public int writeTo(IBuffer out) {
    int len = Math.min(size(), out.capacity());
    out.fill(this, len);
    offset += len;
    return len;
  }
  
  @Override
  public int writeTo(byte[] out, int ofs, int len) {
    Objects.requireNonNull(out, "Bad null byte array");
    if(ofs < 0 || len < 1 || ofs + len > out.length) {
      throw new IllegalArgumentException(String.format("Bad offset/length: %d/%d", ofs, len));
    }
    int min = Math.min(len, size());
    System.arraycopy(buffer, offset, out, ofs, min);
    offset += min;
    return min;
  }
  
  @Override
  public int writeTo(IBuffer out, int length) {
    if(length < 1) return length;
    int len = Math.min(length, Math.min(size(), out.capacity()));
    out.fill(this, len);
    offset += len;
    return len;
  }
  
  @Override
  public ByteBuffer toByteBuffer() {
    int size = this.size();
    if(size < 1) {
      return ByteBuffer.wrap(new byte[0]);
    }
    ByteBuffer bb = ByteBuffer.allocate(size);
    bb.put(buffer, offset, size);
    bb.flip();
    return bb;
  }
  
  @Override
  public byte[] toByteArray() {
    int size = this.size();
    if(size < 1) {
      return new byte[0];
    }
    if(size == capacity()) {
      return buffer;
    }
    byte[] bs = new byte[size];
    System.arraycopy(buffer, offset, bs, 0, size);
    return bs;
  }
  
  @Override
  public String toString() {
    return String.format("Buffer[capacity=%d, offset=%d, size=%d]", capacity(), offset, size());
  }
  
  @Override
  public String toString(Charset cs) {
    return cs.decode(toByteBuffer()).toString();
  }
  
  
  
  public static Buffer of(ByteBuffer bb) {
    Buffer buf = new Buffer(bb.remaining());
    buf.fill(bb);
    return buf;
  }
  
  
  public static Buffer of(byte[] src, int ofs, int len) {
    Objects.requireNonNull(src, "Bad null byte array");
    if(ofs < 0 || len < 1 || ofs + len > src.length) {
      throw new IllegalArgumentException(String.format("Bad offset/length: %d/%d", ofs, len));
    }
    Buffer buf = new Buffer(len);
    buf.fill(src, ofs, len);
    return buf;
  }
  
  
  public static Buffer of(String str) {
    return of(str, StandardCharsets.UTF_8);
  }
  
  
  public static Buffer of(String str, Charset cs) {
    return of(cs.encode(str));
  }
  
  
  public static Buffer of(IBuffer ... bfs) {
    return of(Arrays.asList(bfs));
  }
  
  
  public static Buffer of(Collection<IBuffer> bfs) {
    int size = bfs.stream()
        .filter(b -> !b.isEmpty())
        .mapToInt(b -> b.size())
        .sum();
    Buffer buf = new Buffer(size);
    bfs.forEach(b -> buf.fill(b));
    return buf;
  }
  
}
