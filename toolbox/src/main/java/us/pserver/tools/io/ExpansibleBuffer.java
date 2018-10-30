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
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.IntFunction;
import us.pserver.tools.log.Logger;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 21/10/2018
 */
public class ExpansibleBuffer implements Buffer {
  
  private final Buffer.BufferFactory factory;
  
  private final int growSize;
  
  private final List<Buffer> buffers;
  
  private volatile int rindex;
  
  private volatile int windex;
  
  private volatile int rmark;
  
  private volatile int wmark;
  
  
  private ExpansibleBuffer(Buffer.BufferFactory factory, int growSize, List<Buffer> buffers, int rindex, int windex, int rmark, int wmark) {
    this.factory = factory;
    this.growSize = growSize;
    this.buffers = buffers;
    this.rindex = rindex;
    this.windex = windex;
    this.rmark = rmark;
    this.wmark = wmark;
  }
  
  
  public ExpansibleBuffer(Buffer.BufferFactory factory, int growSize) {
    this.factory = Objects.requireNonNull(factory);
    if(growSize < 1) {
      throw new IllegalArgumentException("Bad grow buffer size: "+ growSize);
    }
    this.growSize = growSize;
    buffers = new CopyOnWriteArrayList<>();
    rindex = 0;
    windex = 0;
    rmark = 0;
    wmark = 0;
  }
  
  
  @Override
  public int capacity() {
    return buffers.stream()
        .mapToInt(Buffer::capacity)
        .sum();
  }
  
  
  @Override
  public int readLength() {
    int len = 0;
    for(int i = rindex; i <= windex; i++) {
      len += buffers.get(i).readLength();
    }
    return len;
  }
  
  
  @Override
  public int writeLength() {
    int len = 0;
    for(int i = windex; i < buffers.size(); i++) {
      len += buffers.get(i).writeLength();
    }
    return len;
  }
  
  
  @Override
  public boolean isReadable() {
    return readLength() > 0;
  }
  
  
  @Override
  public boolean isWritable() {
    return true;
  }
  
  
  @Override
  public Buffer clear() {
    buffers.forEach(Buffer::clear);
    rindex = 0;
    windex = 0;
    rmark = 0;
    wmark = 0;
    return this;
  }
  
  
  @Override
  public Buffer readMark() {
    if(!buffers.isEmpty() && rindex <= windex) {
      buffers.get(rindex).readMark();
      rmark = rindex;
    }
    return this;
  }
  
  
  @Override
  public Buffer readReset() {
    if(!buffers.isEmpty()) {
      rindex = rmark;
      buffers.get(rindex).readReset();
    }
    return this;
  }
  
  
  @Override
  public Buffer writeMark() {
    if(!buffers.isEmpty() && windex < buffers.size()) {
      buffers.get(windex).readMark();
      rmark = rindex;
    }
    return this;
  }
  
  
  @Override
  public Buffer writeReset() {
    if(!buffers.isEmpty()) {
      windex = wmark;
      buffers.get(windex).writeReset();
    }
    return this;
  }
  
  
  @Override
  public Buffer clone() {
    CopyOnWriteArrayList<Buffer> bfs = new CopyOnWriteArrayList<>();
    buffers.forEach(b -> bfs.add(b.clone()));
    return new ExpansibleBuffer(factory, growSize, bfs, rindex, windex, rmark, wmark);
  }
  
  
  @Override
  public Buffer cloneShared() {
    CopyOnWriteArrayList<Buffer> bfs = new CopyOnWriteArrayList<>();
    buffers.forEach(b -> bfs.add(b.cloneShared()));
    return new ExpansibleBuffer(factory, growSize, bfs, rindex, windex, rmark, wmark);
  }
  
  
  @Override
  public int find(byte[] cont) {
    return find(Objects.requireNonNull(cont), 0, cont.length);
  }
  
  
  @Override
  public int find(byte[] cont, int ofs, int length) {
    if(cont == null || cont.length == 0) return -1;
    if(ofs < 0 || length < 1 || ofs + length > cont.length) {
      throw new IllegalArgumentException(String.format("Bad offset/length: %d/%d", ofs, length));
    }
    int len = 0;
    for(int i = rindex; i <= windex; i++) {
      Buffer b = buffers.get(i);
      int rlen = b.readLength();
      int idx = buffers.get(i).find(cont, ofs, length);
      len += (idx >= 0 ? idx : rlen);
      if(idx >= 0) {
        return len;
      }
    }
    return -1;
  }
  
  
  @Override
  public int find(Buffer buf) {
    for(int i = rindex; i <= windex; i++) {
      int idx = buffers.get(i).find(buf);
      if(idx >= 0) return idx;
    }
    return -1;
  }
  
  
  private Buffer getWriteBuffer() {
    Buffer buf = null;
    if(windex >= buffers.size()) {
      buf = factory.create(growSize);
      buffers.add(buf);
    }
    else if(!buffers.get(windex).isWritable()) {
      buf = factory.create(growSize);
      buffers.add(buf);
      windex++;
    }
    else {
      buf = buffers.get(windex);
    }
    return buf;
  }
  
  
  @Override
  public int fillBuffer(InputStream in) throws IOException {
    int read = -1;
    int count = 0;
    byte[] bs = new byte[growSize];
    while((read = in.read(bs)) != -1) {
      this.fillBuffer(bs, 0, read);
      count += read;
    }
    return count;
  }
  
  
  @Override
  public int fillBuffer(InputStream in, int length) throws IOException {
    int read = -1;
    int count = 0;
    int len = length;
    byte[] bs = new byte[growSize];
    while(len > 0 && (read = in.read(bs)) != -1) {
      this.fillBuffer(bs, 0, read);
      count += read;
      len -= read;
    }
    return count;
  }
  
  
  @Override
  public int fillBuffer(ByteBuffer buf) {
    return fillBuffer(Objects.requireNonNull(buf), buf.remaining());
  }
  
  
  @Override
  public int fillBuffer(ByteBuffer buf, int length) {
    if(length < 1) return length;
    Objects.requireNonNull(buf);
    int read = -1;
    int count = 0;
    int len = length;
    while(len > 0 && buf.hasRemaining()) {
      read = getWriteBuffer().fillBuffer(buf, len);
      count += read;
      len -= read;
    }
    return count;
  }
  
  
  @Override
  public int fillBuffer(Buffer buf) {
    return fillBuffer(Objects.requireNonNull(buf), buf.readLength());
  }
  
  
  @Override
  public int fillBuffer(Buffer buf, int length) {
    int read = -1;
    int count = 0;
    int len = length;
    while(len > 0 && buf.isReadable()) {
      read = getWriteBuffer().fillBuffer(buf, len);
      count += read;
      len -= read;
    }
    return count;
  }
  
  
  @Override
  public int fillBuffer(byte[] src) {
    return fillBuffer(Objects.requireNonNull(src), 0, src.length);
  }
  
  
  @Override
  public int fillBuffer(byte[] src, int offset, int length) {
    if(src == null || src.length == 0) {
      throw new IllegalArgumentException("Bad byte array: "+ (src == null ? src : src.length));
    }
    if(offset < 0 || length < 1 || offset + length > src.length) {
      throw new IllegalArgumentException(String.format("Bad offset/length: %d/%d", offset, length));
    }
    int read = -1;
    int count = 0;
    int len = length;
    int ofs = offset;
    while(len > 0) {
      Buffer b = getWriteBuffer();
      read = getWriteBuffer().fillBuffer(src, ofs, len);
      count += read;
      len -= read;
      ofs += read;
    }
    return count;
  }
  
  
  @Override
  public int writeTo(OutputStream out) throws IOException {
    Objects.requireNonNull(out);
    if(!isReadable()) return 0;
    int count = 0;
    for(int i = rindex; i <= windex; i++) {
      Buffer b = buffers.get(i);
      if(b.isReadable()) {
        count += b.writeTo(out);
      }
      rindex++;
    }
    return count;
  }
  
  
  @Override
  public int writeTo(OutputStream out, int length) throws IOException {
    if(length < 1) return length;
    if(!isReadable()) return 0;
    Objects.requireNonNull(out);
    int count = 0;
    int len = length;
    for(int i = rindex; i <= windex && len > 0; i++) {
      Buffer b = buffers.get(i);
      if(b.isReadable()) {
        int w = b.writeTo(out, len);
        count += w;
        len -= w;
      }
      rindex++;
    }
    return count;
  }
  
  
  @Override
  public int writeTo(ByteBuffer out) {
    return writeTo(Objects.requireNonNull(out), out.remaining());
  }
  
  
  @Override
  public int writeTo(ByteBuffer out, int length) {
    if(length < 1) return length;
    if(!Objects.requireNonNull(out).hasRemaining()) return 0;
    int count = 0;
    int len = Math.min(length, Math.min(out.remaining(), readLength()));
    for(int i = rindex; i <= windex && len > 0; i++) {
      Buffer b = buffers.get(i);
      if(b.isReadable()) {
        int w = b.writeTo(out, len);
        count += w;
        len -= w;
      }
      rindex++;
    }
    return count;
  }
  
  
  @Override
  public int writeTo(Buffer out) {
    if(!Objects.requireNonNull(out).isWritable()) return 0;
    if(!isReadable()) throw new BufferUnderflowException();
    int count = 0;
    for(int i = rindex; i <= windex; i++) {
      Buffer b = buffers.get(i);
      if(b.isReadable()) {
        int w = b.writeTo(out);
        count += w;
      }
      rindex++;
    }
    return count;
  }
  
  
  @Override
  public int writeTo(Buffer out, int length) {
    if(!Objects.requireNonNull(out).isWritable()) return 0;
    if(length < 1) return length;
    if(length > readLength()) throw new BufferUnderflowException();
    int count = 0;
    int len = length;
    for(int i = rindex; i <= windex && len > 0; i++) {
      Buffer b = buffers.get(i);
      if(b.isReadable()) {
        int w = b.writeTo(out, len);
        count += w;
        len -= w;
      }
      rindex++;
    }
    return count;
  }
  
  
  @Override
  public int writeTo(byte[] out) {
    return writeTo(Objects.requireNonNull(out), 0, out.length);
  }
  
  
  @Override
  public int writeTo(byte[] out, int offset, int length) {
    if(out == null || out.length == 0) {
      throw new IllegalArgumentException("Bad byte array: "+ (out == null ? out : out.length));
    }
    if(offset < 0 || length < 1 || offset + length > out.length) {
      throw new IllegalArgumentException(String.format("Bad offset/length: %d/%d", offset, length));
    }
    int count = 0;
    int len = Math.min(out.length, Math.min(readLength(), length));;
    int ofs = offset;
    for(int i = rindex; i <= windex && len > 0; i++) {
      Buffer b = buffers.get(i);
      if(b.isReadable()) {
        int w = b.writeTo(out, ofs, len);
        count += w;
        len -= w;
        ofs += w;
      }
      rindex++;
    }
    return count;
  }
  
  
  @Override
  public Buffer put(byte b) {
    getWriteBuffer().put(b);
    return this;
  }
  
  
  @Override
  public Buffer put(short number) {
    getWriteBuffer().put(number);
    return this;
  }
  
  
  @Override
  public Buffer put(int number) {
    getWriteBuffer().put(number);
    return this;
  }
  
  
  @Override
  public Buffer put(long number) {
    getWriteBuffer().put(number);
    return this;
  }
  
  
  @Override
  public Buffer put(float number) {
    getWriteBuffer().put(number);
    return this;
  }
  
  
  @Override
  public Buffer put(double number) {
    getWriteBuffer().put(number);
    return this;
  }
  
  
  @Override
  public byte get() {
    if(readLength() < 1) throw new BufferUnderflowException();
    return buffers.get(rindex).get();
  }
  
  
  @Override
  public int getInt() {
    if(readLength() < Integer.BYTES) throw new BufferUnderflowException();
    return buffers.get(rindex).getInt();
  }
  
  
  @Override
  public short getShort() {
    if(readLength() < Short.BYTES) throw new BufferUnderflowException();
    return buffers.get(rindex).getShort();
  }
  
  
  @Override
  public long getLong() {
    if(readLength() < Long.BYTES) throw new BufferUnderflowException();
    return buffers.get(rindex).getLong();
  }
  
  
  @Override
  public float getFloat() {
    if(readLength() < Float.BYTES) throw new BufferUnderflowException();
    return buffers.get(rindex).getFloat();
  }
  
  
  @Override
  public double getDouble() {
    if(readLength() < Double.BYTES) throw new BufferUnderflowException();
    return buffers.get(rindex).getDouble();
  }
  
  
  @Override
  public ByteBuffer toByteBuffer(IntFunction<ByteBuffer> allocPolicy) {
    ByteBuffer buf = allocPolicy.apply(readLength());
    for(int i = rindex; i <= windex; i++) {
      Buffer b = buffers.get(i);
      if(b.isReadable()) {
        b.readMark().writeTo(buf);
        b.readReset();
      }
    }
    buf.flip();
    return buf;
  }
  
  
  @Override
  public ByteBuffer toByteBuffer() {
    return toByteBuffer(ByteBuffer::allocate);
  }
  
  
  @Override
  public byte[] toByteArray() {
    return toByteBuffer().array();
  }
  
  
  @Override
  public String getContentAsString(Charset cs) {
    StringBuilder sb = new StringBuilder();
    for(int i = rindex; i <= windex; i++) {
      Buffer b = buffers.get(i);
      if(b.isReadable()) {
        sb.append(b.getContentAsString(cs));
      }
    }
    return sb.toString();
  }
  
  
  @Override
  public String toString() {
    return String.format("ExpansibleBuffer[%s, capacity=%d, buffers.size=%d, readLenght=%d, writeLength=%d, rmark=%d, wmark=%d]", factory.getClass().getSimpleName(), capacity(), buffers.size(), readLength(), writeLength(), rmark, wmark);
  }
  
}
