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
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.Objects;
import java.util.function.IntFunction;
import us.pserver.tools.log.Logger;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 20/10/2018
 */
public class DirectBuffer implements Buffer {
  
  private final ByteBuffer buffer;
  
  private volatile int rindex;
  
  private volatile int windex;
  
  private volatile int rmark;
  
  private volatile int wmark;
  
  
  private DirectBuffer(ByteBuffer buf, int rindex, int windex, int rmark, int wmark) {
    this.buffer = buf;
    this.rindex = rindex;
    this.windex = windex;
    this.rmark = rmark;
    this.wmark = wmark;
  }
  
  
  public DirectBuffer(int size) {
    if(size < 1) {
      throw new IllegalArgumentException("Bad size: " + size);
    }
    buffer = ByteBuffer.allocateDirect(size);
    rindex = 0;
    windex = 0;
    rmark = 0;
    wmark = 0;
  }

  @Override
  public int capacity() {
    return buffer.capacity();
  }
  
  
  @Override
  public int readLength() {
    return windex - rindex;
  }
  
  
  @Override
  public int writeLength() {
    return buffer.limit() - windex;
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
    buffer.clear();
    rindex = 0;
    windex = 0;
    rmark = 0;
    wmark = 0;
    return this;
  }
  
  
  @Override
  public Buffer readMark() {
    rmark = buffer.position();
    return this;
  }
  
  
  @Override
  public Buffer readReset() {
    buffer.position(rmark);
    return this;
  }
  
  
  @Override
  public Buffer writeMark() {
    wmark = buffer.position();
    return this;
  }
  
  
  @Override
  public Buffer writeReset() {
    buffer.position(wmark);
    return this;
  }


  @Override
  public Buffer clone() {
    DirectBuffer buf = new DirectBuffer(capacity());
    buffer.position(0);
    buf.fillBuffer(buffer);
    buf.rindex = rindex;
    buf.windex = windex;
    buf.rmark = rmark;
    buf.wmark = wmark;
    return buf;
  }
  
  
  @Override
  public Buffer cloneShared() {
    return new DirectBuffer(buffer, rindex, windex, rmark, wmark);
  }
  
  
  @Override
  public boolean find(byte[] cont) {
    return find(cont, 0, cont.length);
  }
  
  
  @Override
  public boolean find(byte[] cont, int ofs, int len) {
    if(cont == null || cont.length == 0) return false;
    if(ofs < 0 || len < 1 || ofs + len > cont.length) {
      throw new IllegalArgumentException(String.format("Bad offset/length: %d/%d", ofs, len));
    }
    int idx = rindex;
    while((idx + len) < readLength()) {
      buffer.position(idx);
      int count = 0;
      for(int i = ofs; i < len; i++) {
        if(buffer.get() == cont[i]) count++;
      }
      if(count == len) return true;
      idx++;
    }
    return false;
  }
  
  
  @Override
  public boolean find(Buffer buf) {
    if(buf == null || !buf.isReadable()) {
      throw new IllegalArgumentException("Bad buffer: "+ buf);
    }
    buf.readMark();
    int rlen = buf.readLength();
    int idx = rindex;
    while((idx + rlen) < readLength()) {
      buffer.position(idx);
      int count = 0;
      for(int i = 0; i < rlen; i++) {
        if(buffer.get() == buf.get()) count++;
      }
      buf.readReset();
      if(count == rlen) return true;
      idx++;
    }
    return false;
  }
  
  
  @Override
  public int fillBuffer(InputStream in) throws IOException {
    return fillBuffer(in, writeLength());
  }
  
  
  @Override
  public int fillBuffer(InputStream in, int length) throws IOException {
    Objects.requireNonNull(in);
    if(length < 1) return length;
    if(!isWritable()) throw new BufferOverflowException();
    int len = Math.min(writeLength(), length);
    byte[] bs = new byte[len];
    int read = in.read(bs, windex, len);
    buffer.position(windex);
    buffer.put(bs);
    windex += len;
    return read;
  }
  
  
  @Override
  public int fillBuffer(ByteBuffer buf) {
    return fillBuffer(buf, writeLength());
  }
  
  
  @Override
  public int fillBuffer(ByteBuffer buf, int length) {
    Objects.requireNonNull(buf);
    if(length < 1) return length;
    if(!isWritable()) throw new BufferOverflowException();
    int len = Math.min(length, Math.min(writeLength(), buf.remaining()));
    int lim = buf.limit();
    buf.limit(len);
    buffer.position(windex);
    buffer.put(buf);
    windex += len;
    buf.limit(lim);
    return len;
  }
  
  
  @Override
  public int fillBuffer(Buffer buf) {
    return fillBuffer(buf, writeLength());
  }
  
  
  @Override
  public int fillBuffer(Buffer buf, int length) {
    Objects.requireNonNull(buf);
    if(length < 1) return length;
    if(!isWritable()) throw new BufferOverflowException();
    int len = Math.min(length, Math.min(writeLength(), buf.readLength()));
    buffer.position(windex);
    buf.writeTo(buffer, len);
    windex += len;
    return len;
  }
  
  
  @Override
  public int fillBuffer(byte[] src) {
    return fillBuffer(src, 0, src.length);
  }
  
  
  @Override
  public int fillBuffer(byte[] src, int ofs, int length) {
    if(src == null || src.length == 0) return -1;
    if(ofs < 0 || length < 1 || ofs + length > src.length) {
      throw new IllegalArgumentException(String.format("Bad offset/length: %d/%d", ofs, length));
    }
    int len = Math.min(length, Math.min(writeLength(), src.length));
    Logger.debug("length = min(%d, %d, %d): %d", length, writeLength(), src.length, len);
    buffer.position(windex);
    buffer.put(src, ofs, len);
    windex += len;
    return len;
  }
  
  
  @Override
  public int writeTo(OutputStream out) throws IOException {
    return writeTo(out, readLength());
  }
  
  
  @Override
  public int writeTo(OutputStream out, int length) throws IOException {
    if(length < 1) return length;
    Objects.requireNonNull(out);
    int len = Math.min(readLength(), length);
    byte[] bs = new byte[len];
    buffer.position(rindex);
    buffer.get(bs, 0, len);
    out.write(bs, 0, len);
    rindex += len;
    return len;
  }
  
  
  @Override
  public int writeTo(ByteBuffer out) {
    return writeTo(out, readLength());
  }
  
  
  @Override
  public int writeTo(ByteBuffer out, int length) {
    Objects.requireNonNull(out);
    if(length < 1) return length;
    int len = Math.min(out.remaining(), Math.min(readLength(), length));
    int lim = buffer.limit();
    buffer.position(rindex);
    buffer.limit(rindex + len);
    out.put(buffer);
    buffer.limit(lim);
    rindex += len;
    return len;
  }
  
  
  @Override
  public int writeTo(Buffer out) {
    return writeTo(out, out.readLength());
  }
  
  
  @Override
  public int writeTo(Buffer out, int length) {
    if(length < 1) return length;
    Objects.requireNonNull(out);
    int len = Math.min(out.writeLength(), Math.min(readLength(), length));
    buffer.position(rindex);
    out.fillBuffer(buffer, len);
    rindex += len;
    return len;
  }
  
  
  @Override
  public int writeTo(byte[] out) {
    return writeTo(out, 0, out.length);
  }
  
  
  @Override
  public int writeTo(byte[] out, int ofs, int length) {
    if(out == null || out.length == 0) return -1;
    if(ofs < 0 || length < 1 || ofs + length > out.length) {
      throw new IllegalArgumentException(String.format("Bad offset/length: %d/%d", ofs, length));
    }
    int len = Math.min(out.length, Math.min(readLength(), length));
    buffer.position(rindex);
    buffer.get(out, ofs, len);
    rindex += len;
    return len;
  }
  
  
  @Override
  public Buffer put(byte b) {
    buffer.position(windex++);
    buffer.put(b);
    return this;
  }
  
  
  @Override
  public Buffer put(short number) {
    buffer.position(windex);
    buffer.putShort(number);
    windex += Short.BYTES;
    return this;
  }
  
  
  @Override
  public Buffer put(int number) {
    buffer.position(windex);
    buffer.putInt(number);
    windex += Integer.BYTES;
    return this;
  }
  
  
  @Override
  public Buffer put(long number) {
    buffer.position(windex);
    buffer.putLong(number);
    windex += Long.BYTES;
    return this;
  }
  
  
  @Override
  public Buffer put(float number) {
    buffer.position(windex);
    buffer.putFloat(number);
    windex += Integer.BYTES;
    return this;
  }
  
  
  @Override
  public Buffer put(double number) {
    buffer.position(windex);
    buffer.putDouble(number);
    windex += Long.BYTES;
    return this;
  }
  
  
  @Override
  public byte get() {
    buffer.position(rindex++);
    return buffer.get();
  }
  
  
  @Override
  public int getInt() {
    buffer.position(rindex);
    rindex += Integer.BYTES;
    return buffer.getInt();
  }
  
  
  @Override
  public short getShort() {
    buffer.position(rindex);
    rindex += Short.BYTES;
    return buffer.getShort();
  }
  
  
  @Override
  public long getLong() {
    buffer.position(rindex);
    rindex += Long.BYTES;
    return buffer.getLong();
  }
  
  
  @Override
  public float getFloat() {
    buffer.position(rindex);
    rindex += Integer.BYTES;
    return buffer.getFloat();
  }
  
  
  @Override
  public double getDouble() {
    buffer.position(rindex);
    rindex += Long.BYTES;
    return buffer.getDouble();
  }
  
  
  @Override
  public ByteBuffer toByteBuffer(IntFunction<ByteBuffer> allocPolicy) {
    ByteBuffer buf = allocPolicy.apply(readLength());
    int lim = buffer.limit();
    buffer.limit(rindex + readLength());
    buffer.position(rindex);
    buf.put(buffer);
    buf.flip();
    buffer.limit(lim);
    return buf;
  }
  
  
  @Override
  public ByteBuffer toByteBuffer() {
    return toByteBuffer(ByteBuffer::allocate);
  }
  
  
  @Override
  public byte[] toByteArray() {
    byte[] buf = new byte[readLength()];
    buffer.position(rindex);
    buffer.get(buf, 0, readLength());
    return buf;
  }
  
  
  @Override
  public String getContentAsString(Charset cs) {
    int lim = buffer.limit();
    buffer.limit(rindex + readLength());
    buffer.position(rindex);
    CharBuffer cb = cs.decode(buffer);
    buffer.limit(lim);
    return cb.toString();
  }
  
  
  @Override
  public String toString() {
    return String.format("DirectBuffer[capacity=%d, rindex=%d, windex=%d, rmark=%d, wmark=%d]", capacity(), rindex, windex, rmark, wmark);
  }
  
}
