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
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Objects;
import java.util.function.IntFunction;
import us.pserver.tools.log.Logger;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 20/10/2018
 */
public class HeapBuffer implements Buffer {
  
  public static final HeapBuffer EMPTY_BUFFER = new HeapBuffer(new byte[0], -1, -1, -1, -1);
  
  
  private final byte[] buffer;
  
  private int rindex;
  
  private int windex;
  
  private int rmark;
  
  private int wmark;
  
  
  private HeapBuffer(byte[] buffer, int rindex, int windex, int rmark, int wmark) {
    this.buffer = Objects.requireNonNull(buffer);
    this.rindex = rindex;
    this.windex = windex;
    this.rmark = rmark;
    this.wmark = wmark;
  }
  
  
  /* Tested */
  public HeapBuffer(int size) {
    if(size < 1) {
      throw new IllegalArgumentException(String.format("Bad buffer size: %d", size));
    }
    buffer = new byte[size];
    rindex = 0;
    windex = 0;
    rmark = 0;
    wmark = 0;
  }
  
  
  /* Tested */
  @Override
  public int capacity() {
    return buffer.length;
  }
  
  
  /* Tested */
  @Override
  public int readLength() {
    return windex - rindex;
  }
  
  
  /* Tested */
  @Override
  public int writeLength() {
    return buffer.length - windex;
  }
  
  
  /* Tested */
  @Override
  public boolean isReadable() {
    return readLength() > 0;
  }
  
  
  /* Tested */
  @Override
  public boolean isWritable() {
    return writeLength() > 0;
  }
  
  
  /* Tested */
  @Override
  public Buffer clear() {
    rindex = 0;
    windex = 0;
    rmark = 0;
    wmark = 0;
    return this;
  }
  
  
  /* Tested */
  @Override
  public Buffer readMark() {
    rmark = rindex;
    return this;
  }
  
  
  /* Tested */
  @Override
  public Buffer readReset() {
    rindex = rmark;
    return this;
  }
  
  
  /* Tested */
  @Override
  public Buffer writeMark() {
    wmark = windex;
    return this;
  }
  
  
  /* Tested */
  @Override
  public Buffer writeReset() {
    windex = wmark;
    return this;
  }
  
  
  /* Tested */
  @Override
  public Buffer clone() {
    HeapBuffer buf = new HeapBuffer(buffer.length);
    System.arraycopy(buffer, 0, buf.buffer, 0, buffer.length);
    buf.rindex = rindex;
    buf.windex = windex;
    buf.rmark = rmark;
    buf.wmark = wmark;
    return buf;
  }
  
  
  /* Tested */
  @Override
  public Buffer cloneShared() {
    return new HeapBuffer(buffer, rindex, windex, rmark, wmark);
  }
  
  
  /* Tested */
  @Override
  public boolean find(byte[] cont) {
    return find(Objects.requireNonNull(cont), 0, cont.length);
  }
  
  
  /* Tested */
  @Override
  public boolean find(byte[] cont, int ofs, int len) {
    if(cont == null || cont.length == 0) return false;
    if(ofs < 0 || len < 1 || ofs + len > cont.length) {
      throw new IllegalArgumentException(String.format("Bad offset/length: %d/%d", ofs, len));
    }
    int idx = rindex;
    while((idx + len) < readLength()) {
      int count = 0;
      for(int i = ofs; i < len; i++) {
        if(buffer[idx + i] == cont[i]) count++;
      }
      if(count == len) return true;
      idx++;
    }
    return false;
  }
  
  
  /* Tested */
  @Override
  public boolean find(Buffer buf) {
    if(buf == null || !buf.isReadable()) {
      throw new IllegalArgumentException("Bad buffer: "+ buf);
    }
    buf.readMark();
    int rlen = buf.readLength();
    int idx = rindex;
    while((idx + rlen) < readLength()) {
      int count = 0;
      for(int i = 0; i < rlen; i++) {
        if(buffer[idx + i] == buf.get()) count++;
      }
      buf.readReset();
      if(count == rlen) return true;
      idx++;
    }
    return false;
  }
  
  
  /* Tested */
  @Override
  public int fillBuffer(InputStream in) throws IOException {
    return fillBuffer(in, writeLength());
  }
  
  
  /* Tested */
  @Override
  public int fillBuffer(InputStream in, int length) throws IOException {
    if(length < 1) return length;
    Objects.requireNonNull(in);
    if(!isWritable()) throw new BufferOverflowException();
    int len = Math.min(writeLength(), length);
    int read = in.read(buffer, windex, len);
    windex += len;
    return read;
  }
  
  
  /* Tested */
  @Override
  public int fillBuffer(ByteBuffer buf) {
    return fillBuffer(buf, writeLength());
  }
  
  
  /* Tested */
  @Override
  public int fillBuffer(ByteBuffer buf, int length) {
    if(length < 1) return length;
    Objects.requireNonNull(buf);
    if(!isWritable()) throw new BufferOverflowException();
    int len = Math.min(length, Math.min(writeLength(), buf.remaining()));
    buf.get(buffer, rindex, len);
    windex += len;
    return len;
  }
  
  
  /* Tested */
  @Override
  public int fillBuffer(Buffer buf) {
    return fillBuffer(buf, writeLength());
  }
  
  
  /* Tested */
  @Override
  public int fillBuffer(Buffer buf, int length) {
    if(length < 1) return length;
    Objects.requireNonNull(buf);
    if(!isWritable()) throw new BufferOverflowException();
    int len = Math.min(length, Math.min(writeLength(), buf.readLength()));
    buf.writeTo(buffer, windex, len);
    windex += len;
    return len;
  }
  
  
  /* Tested */
  @Override
  public int fillBuffer(byte[] src) {
    return fillBuffer(src, 0, src.length);
  }
  
  
  /* Tested */
  @Override
  public int fillBuffer(byte[] src, int ofs, int length) {
    if(src == null || src.length == 0) return -1;
    if(ofs < 0 || length < 1 || ofs + length > src.length) {
      throw new IllegalArgumentException(String.format("Bad offset/length: %d/%d", ofs, length));
    }
    int len = Math.min(length, Math.min(writeLength(), src.length));
    Logger.debug("length = min(%d, %d, %d): %d", length, writeLength(), src.length, len);
    System.arraycopy(src, ofs, buffer, windex, len);
    windex += len;
    return len;
  }
  
  
  /* Tested */
  @Override
  public int writeTo(OutputStream out) throws IOException {
    return writeTo(out, readLength());
  }
  
  
  /* Tested */
  @Override
  public int writeTo(OutputStream out, int length) throws IOException {
    if(length < 1) return length;
    Objects.requireNonNull(out);
    int len = Math.min(readLength(), length);
    out.write(buffer, rindex, len);
    rindex += len;
    return len;
  }
  
  
  /* Tested */
  @Override
  public int writeTo(ByteBuffer out) {
    return writeTo(out, readLength());
  }
  
  
  /* Tested */
  @Override
  public int writeTo(ByteBuffer out, int length) {
    if(length < 1) return length;
    Objects.requireNonNull(out);
    int len = Math.min(out.remaining(), Math.min(readLength(), length));
    out.put(buffer, rindex, len);
    rindex += len;
    return len;
  }
  
  
  /* Tested */
  @Override
  public int writeTo(Buffer out) {
    return writeTo(out, readLength());
  }
  
  
  /* Tested */
  @Override
  public int writeTo(Buffer out, int length) {
    if(length < 1) return length;
    Objects.requireNonNull(out);
    int len = Math.min(out.writeLength(), Math.min(readLength(), length));
    out.fillBuffer(buffer, rindex, len);
    rindex += len;
    return len;
  }
  
  
  /* Tested */
  @Override
  public int writeTo(byte[] out) {
    return writeTo(out, 0, out.length);
  }
  
  
  /* Tested */
  @Override
  public int writeTo(byte[] out, int ofs, int length) {
    if(out == null || out.length == 0) return -1;
    if(ofs < 0 || length < 1 || ofs + length > out.length) {
      throw new IllegalArgumentException(String.format("Bad offset/length: %d/%d", ofs, length));
    }
    int len = Math.min(out.length, Math.min(readLength(), length));
    System.arraycopy(buffer, rindex, out, ofs, len);
    rindex += len;
    return len;
  }
  
  
  /* Tested */
  @Override
  public Buffer put(byte b) {
    if(writeLength() < 1) throw new BufferOverflowException();
    buffer[windex++] = b;
    return this;
  }
  
  
  /* Tested */
  @Override
  public Buffer put(short number) {
    if(writeLength() < Short.BYTES) throw new BufferOverflowException();
    int shift = (Short.BYTES -1) * 8;
    for(int i = rindex; i < rindex + Short.BYTES; i++) {
      buffer[i] = (byte)(number >>> shift);
      shift -= 8;
    }
    windex += Short.BYTES;
    return this;
  }
  
  
  @Override
  /* Tested */
  public Buffer put(int number) {
    if(writeLength() < Integer.BYTES) throw new BufferOverflowException();
    int shift = (Integer.BYTES -1) * 8;
    for(int i = rindex; i < rindex + Integer.BYTES; i++) {
      buffer[i] = (byte)(number >>> shift);
      shift -= 8;
    }
    windex += Integer.BYTES;
    return this;
  }
  
  
  /* Tested */
  @Override
  public Buffer put(long number) {
    if(writeLength() < Long.BYTES) throw new BufferOverflowException();
    int shift = (Long.BYTES -1) * 8;
    for(int i = rindex; i < rindex + Long.BYTES; i++) {
      buffer[i] = (byte)(number >>> shift);
      shift -= 8;
    }
    windex += Long.BYTES;
    return this;
  }
  
  
  /* Tested */
  @Override
  public Buffer put(float number) {
    if(writeLength() < Integer.BYTES) throw new BufferOverflowException();
    int n = Float.floatToIntBits(number);
    int shift = (Integer.BYTES -1) * 8;
    for(int i = rindex; i < rindex + Integer.BYTES; i++) {
      buffer[i] = (byte)(n >>> shift);
      shift -= 8;
    }
    windex += Integer.BYTES;
    return this;
  }
  
  
  /* Tested */
  @Override
  public Buffer put(double number) {
    if(writeLength() < Long.BYTES) throw new BufferOverflowException();
    long n = Double.doubleToLongBits(number);
    for(int i = Long.BYTES -1 + rindex; i >= rindex; i--) {
      buffer[i] = (byte)(n & 0xFF);
      n >>= Long.BYTES;
    }
    windex += Long.BYTES;
    return this;
  }
  
  
  /* Tested */
  @Override
  public byte get() {
    if(readLength() < 1) throw new BufferUnderflowException();
    return buffer[rindex++];
  }
  
  
  /* Tested */
  @Override
  public int getInt() {
    if(readLength() < Integer.BYTES) throw new BufferUnderflowException();
    int result = 0;
    int shift = 0;
    for(int i = rindex + Integer.BYTES -1; i >= rindex; i--) {
      result |= (buffer[i] & 0xFF) << shift;
      shift += 8;
    }
    rindex += Integer.BYTES;
    return result;
  }
  
  
  /* Tested */
  @Override
  public short getShort() {
    if(readLength() < Short.BYTES) throw new BufferUnderflowException();
    short result = 0;
    int shift = 0;
    for(int i = rindex + Short.BYTES -1; i >= 0; i--) {
      result |= (buffer[i] & 0xFF) << shift;
      shift += 8;
    }
    rindex += Short.BYTES;
    return result;
  }
  
  
  /* Tested */
  @Override
  public long getLong() {
    if(readLength() < Long.BYTES) throw new BufferUnderflowException();
    long result = 0;
    for(int i = rindex; i < rindex + Long.BYTES; i++) {
      result <<= Long.BYTES;
      result |= (buffer[i] & 0xFF);
    }
    rindex += Long.BYTES;
    return result;
  }
  
  
  /* Tested */
  @Override
  public float getFloat() {
    if(readLength() < Integer.BYTES) throw new BufferUnderflowException();
    int result = 0;
    int shift = 0;
    for(int i = rindex + Integer.BYTES -1; i >= rindex; i--) {
      result |= (buffer[i] & 0xFF) << shift;
      shift += 8;
    }
    rindex += Integer.BYTES;
    return Float.intBitsToFloat(result);
  }
  
  
  /* Tested */
  @Override
  public double getDouble() {
    if(readLength() < Long.BYTES) throw new BufferUnderflowException();
    long result = 0;
    for(int i = rindex; i < rindex + Long.BYTES; i++) {
      result <<= Long.BYTES;
      result |= (buffer[i] & 0xFF);
    }
    rindex += Long.BYTES;
    return Double.longBitsToDouble(result);
  }
  
  
  /* Tested */
  @Override
  public ByteBuffer toByteBuffer(IntFunction<ByteBuffer> allocPolicy) {
    if(readLength() < 1) return ByteBuffer.wrap(new byte[0]);
    ByteBuffer buf = allocPolicy.apply(readLength());
    buf.put(buffer, rindex, readLength());
    buf.flip();
    return buf;
  }
  
  
  /* Tested */
  @Override
  public ByteBuffer toByteBuffer() {
    return ByteBuffer.wrap(buffer, rindex, readLength());
  }
  
  
  /* Tested */
  @Override
  public byte[] toByteArray() {
    if(readLength() == buffer.length) return buffer;
    byte[] buf = new byte[readLength()];
    System.arraycopy(buffer, rindex, buf, 0, readLength());
    return buf;
  }
  
  
  /* Tested */
  @Override
  public String getContentAsString(Charset cs) {
    return cs.decode(ByteBuffer.wrap(buffer, rindex, readLength())).toString();
  }

  
  /* Tested */
  @Override
  public String toString() {
    return String.format("HeapBuffer[capacity=%d, rindex=%d, windex=%d, rmark=%d, wmark=%d]", buffer.length, rindex, windex, rmark, wmark);
  }
  
}
