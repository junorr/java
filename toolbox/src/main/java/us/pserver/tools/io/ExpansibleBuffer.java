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
    windex = -1;
    rmark = 0;
    wmark = 0;
  }
  
  
  @Override
  public int capacity() {
    return buffers.stream()
        .mapToInt(Buffer::capacity)
        .sum();
  }
  
  
  private Buffer wget() {
    Buffer b = buffers.size() > windex && windex >= 0 ? buffers.get(windex) : null;
    Logger.debug("buffers.get(windex): %s - isWritable=%s", b, b != null ? b.isWritable() : "null");
    if(buffers.isEmpty() || buffers.size() <= windex || !buffers.get(windex).isWritable()) {
      buffers.add(factory.create(growSize));
      windex++;
    }
    return buffers.get(windex);
  }
  
  
  private Buffer rget() {
    if(!isReadable()) {
      throw new BufferUnderflowException();
    }
    for(int i = rindex; i < windex; i++) {
      Buffer b = buffers.get(i);
      if(b.isReadable()) return b;
      else rindex++;
    }
    return null;
  }
  
  
  @Override
  public int readLength() {
    Logger.debug("readLength=%d", buffers.stream()
        .mapToInt(Buffer::readLength)
        .sum());
    return buffers.stream()
        .mapToInt(Buffer::readLength)
        .sum();
  }
  
  
  @Override
  public int writeLength() {
    return buffers.stream()
        .mapToInt(Buffer::writeLength)
        .sum();
  }
  
  
  @Override
  public boolean isReadable() {
    return readLength() > 0;
  }
  
  
  @Override
  public boolean isWritable() {
    return writeLength() > 0;
  }
  
  
  @Override
  public Buffer clear() {
    buffers.forEach(b -> {
      b.clear();
      Logger.debug("clear: %s", b);
    });
    rindex = 0;
    windex = 0;
    rmark = 0;
    wmark = 0;
    return this;
  }
  
  
  @Override
  public Buffer readMark() {
    if(!buffers.isEmpty()) {
      buffers.get(rindex).readMark();
      rmark = rindex;
    }
    return this;
  }
  
  
  @Override
  public Buffer readReset() {
    rindex = rmark;
    buffers.get(rindex).readReset();
    return this;
  }
  
  
  @Override
  public Buffer writeMark() {
    wget().writeMark();
    wmark = windex;
    return this;
  }
  
  
  @Override
  public Buffer writeReset() {
    windex = wmark;
    buffers.get(rindex).writeReset();
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
  public boolean find(byte[] cont) {
    return find(Objects.requireNonNull(cont), 0, cont.length);
  }
  
  
  @Override
  public boolean find(byte[] cont, int ofs, int length) {
    if(cont == null || cont.length == 0) return false;
    if(ofs < 0 || length < 1 || ofs + length > cont.length) {
      throw new IllegalArgumentException(String.format("Bad offset/length: %d/%d", ofs, length));
    }
    for(int i = rindex; i < windex; i++) {
      if(buffers.get(i).find(cont, ofs, length)) return true;
    }
    return false;
  }
  
  
  @Override
  public boolean find(Buffer buf) {
    for(int i = rindex; i < windex; i++) {
      if(buffers.get(i).find(buf)) return true;
    }
    return false;
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
    int read = -1;
    int count = 0;
    while(buf.hasRemaining()) {
      read = wget().fillBuffer(buf);
      count += read;
    }
    return count;
  }
  
  
  @Override
  public int fillBuffer(ByteBuffer buf, int length) {
    int read = -1;
    int count = 0;
    int len = length;
    while(len > 0 && buf.hasRemaining()) {
      count += read;
      len -= read;
    }
    return count;
  }
  
  
  @Override
  public int fillBuffer(Buffer buf) {
    int read = -1;
    int count = 0;
    byte[] bs = new byte[Math.min(buf.readLength(), growSize)];
    while(buf.isReadable()) {
      read = buf.writeTo(bs);
      this.fillBuffer(bs, 0, read);
      count += read;
    }
    return count;
  }
  
  
  @Override
  public int fillBuffer(Buffer buf, int length) {
    int read = -1;
    int count = 0;
    int len = length;
    byte[] bs = new byte[length];
    while(len > 0 && buf.isReadable()) {
      read = buf.writeTo(bs);
      this.fillBuffer(bs, 0, read);
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
    if(src == null || src.length == 0) return 0;
    if(offset < 0 || length < 1 || offset + length > src.length) {
      throw new IllegalArgumentException(String.format("Bad offset/length: %d/%d", offset, length));
    }
    int read = -1;
    int count = 0;
    int len = length;
    int ofs = offset;
    while(len > 0) {
      read = wget().fillBuffer(src, ofs, len);
      Logger.debug("wget: buffers.size=%d, windex=%d, get(windex)=%s", buffers.size(), windex, buffers.get(windex));
      count += read;
      len -= read;
      ofs += read;
    }
    Logger.debug("count: %d", count);
    return count;
  }
  
  
  @Override
  public int writeTo(OutputStream out) throws IOException {
    Objects.requireNonNull(out);
    int count = 0;
    for(int i = rindex; i < buffers.size(); i++) {
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
    Objects.requireNonNull(out);
    int count = 0;
    int len = length;
    for(int i = rindex; i < buffers.size() && len > 0; i++) {
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
    Objects.requireNonNull(out);
    int count = 0;
    for(int i = rindex; i < buffers.size() && out.hasRemaining(); i++) {
      Buffer b = buffers.get(i);
      if(b.isReadable()) {
        count += b.writeTo(out);
      }
      rindex++;
    }
    return count;
  }
  
  
  @Override
  public int writeTo(ByteBuffer out, int length) {
    if(length < 1) return length;
    Objects.requireNonNull(out);
    int count = 0;
    int len = length;
    for(int i = rindex; i < buffers.size() && len > 0 && out.hasRemaining(); i++) {
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
    Objects.requireNonNull(out);
    int count = 0;
    for(int i = rindex; i < buffers.size() && out.isWritable(); i++) {
      Buffer b = buffers.get(i);
      if(b.isReadable()) {
        count += b.writeTo(out);
      }
      rindex++;
    }
    return count;
  }
  
  
  @Override
  public int writeTo(Buffer out, int length) {
    if(length < 1) return length;
    Objects.requireNonNull(out);
    int count = 0;
    int len = length;
    for(int i = rindex; i < buffers.size() && len > 0 && out.isWritable(); i++) {
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
    if(out == null || out.length == 0) return 0;
    if(offset < 0 || length < 1 || offset + length > out.length) {
      throw new IllegalArgumentException(String.format("Bad offset/length: %d/%d", offset, length));
    }
    int count = 0;
    int len = length;
    int ofs = offset;
    for(int i = rindex; i < buffers.size() && len > 0; i++) {
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
    wget().put(b);
    return this;
  }
  
  
  @Override
  public Buffer put(short number) {
    wget().put(number);
    return this;
  }
  
  
  @Override
  public Buffer put(int number) {
    wget().put(number);
    return this;
  }
  
  
  @Override
  public Buffer put(long number) {
    wget().put(number);
    return this;
  }
  
  
  @Override
  public Buffer put(float number) {
    wget().put(number);
    return this;
  }
  
  
  @Override
  public Buffer put(double number) {
    wget().put(number);
    return this;
  }
  
  
  @Override
  public byte get() {
    return rget().get();
  }
  
  
  @Override
  public int getInt() {
    return rget().getInt();
  }
  
  
  @Override
  public short getShort() {
    return rget().getShort();
  }
  
  
  @Override
  public long getLong() {
    return rget().getLong();
  }
  
  
  @Override
  public float getFloat() {
    return rget().getFloat();
  }
  
  
  @Override
  public double getDouble() {
    return rget().getDouble();
  }
  
  
  @Override
  public ByteBuffer toByteBuffer(IntFunction<ByteBuffer> allocPolicy) {
    ByteBuffer buf = allocPolicy.apply(readLength());
    for(int i = rindex; i < windex; i++) {
      Buffer b = buffers.get(i);
      if(b.isReadable()) {
        b.writeTo(buf);
      }
    }
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
    for(int i = rindex; i < windex; i++) {
      Buffer b = buffers.get(i);
      if(b.isReadable()) {
        sb.append(b.getContentAsString(cs));
      }
    }
    return sb.toString();
  }
  
  
  @Override
  public String toString() {
    return String.format("ExpansibleBuffer[bufferFactory=%s, capacity=%d, buffers.size=%d, rindex=%d, windex=%d, rmark=%d, wmark=%d]", factory.getClass().getSimpleName(), capacity(), buffers.size(), rindex, windex, rmark, wmark);
  }
  
}
