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

package us.pserver.bitbox.impl;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.StandardCharsets;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 04/12/2018
 */
public final class BitBufferImpl implements BitBuffer {

  private ByteBuffer buffer;
  
  private int maxPosition;
  
  public BitBufferImpl(int cap, boolean useDirectBuffer) {
    buffer = useDirectBuffer ? ByteBuffer.allocateDirect(cap) : ByteBuffer.allocate(cap);
  }

  public BitBufferImpl(byte[] array) {
    buffer = array != null ? ByteBuffer.wrap(array) : ByteBuffer.allocate(0);
  }

  public BitBufferImpl(byte[] array, int offset, int length) {
    buffer = ByteBuffer.wrap(array, offset, length);
  }

  public BitBufferImpl(ByteBuffer buffer) {
    this.buffer = buffer;
  }

  private BitBufferImpl(ByteBuffer buffer, int maxPosition) {
    this.buffer = buffer;
    this.maxPosition = maxPosition;
  }
  
  public int maxPosition() {
    return maxPosition;
  }
  
  public BitBuffer resetMaxPosition() {
    this.maxPosition = 0;
    return this;
  }

  public BitBuffer compact() {
    buffer.compact();
    resetMaxPosition();
    return this;
  }
  
  private void updateMax() {
    maxPosition = Math.max(maxPosition, buffer.position());
  }

  public byte get() {
    byte b = buffer.get();
    updateMax();
    return b;
  }

  public byte get(int index) {
    byte b = buffer.get(index);
    updateMax();
    return b;
  }

  public BitBuffer get(byte[] dst) {
    buffer.get(dst);
    updateMax();
    return this;
  }

  public BitBuffer get(byte[] dst, int offset, int length) {
    buffer.get(dst, offset, length);
    updateMax();
    return this;
  }

  public char getChar() {
    check();
    char c = buffer.getChar();
    updateMax();
    return c;
  }

  public char getChar(int index) {
    check();
    char c = buffer.getChar(index);
    updateMax();
    return c;
  }

  public double getDouble() {
    check();
    double c = buffer.getDouble();
    updateMax();
    return c;
  }

  public double getDouble(int index) {
    check();
    double c = buffer.getDouble(index);
    updateMax();
    return c;
  }

  public float getFloat() {
    check();
    float c = buffer.getFloat();
    updateMax();
    return c;
  }

  public float getFloat(int index) {
    check();
    float c = buffer.getFloat(index);
    updateMax();
    return c;
  }

  public int getInt() {
    check();
    int c = buffer.getInt();
    updateMax();
    return c;
  }

  public int getInt(int index) {
    check();
    int c = buffer.getInt(index);
    updateMax();
    return c;
  }

  public long getLong() {
    check();
    long c = buffer.getLong();
    updateMax();
    return c;
  }

  public long getLong(int index) {
    check();
    long c = buffer.getLong(index);
    updateMax();
    return c;
  }

  public short getShort() {
    check();
    short c = buffer.getShort();
    updateMax();
    return c;
  }

  public short getShort(int index) {
    check();
    short c = buffer.getShort();
    updateMax();
    return c;
  }

  public BitBuffer put(byte b) {
      ensureSize(1);
      buffer.put(b);
      updateMax();
      return this;
  }

  public BitBuffer put(int index, byte b) {
      ensureSize(1);
      buffer.put(index, b);
      updateMax();
      return this;
  }

  public BitBuffer put(byte[] src) {
      ensureSize(src.length);
      buffer.put(src);
      updateMax();
      return this;
  }

  public BitBuffer put(byte[] src, int offset, int length) {
      ensureSize(length);
      buffer.put(src, offset, length);
      updateMax();
      return this;
  }

  public BitBuffer put(ByteBuffer src) {
      ensureSize(src.remaining());
      buffer.put(src);
      updateMax();
      return this;
  }
  
  public BitBuffer put(BitBuffer src) {
      ensureSize(src.remaining());
      buffer.put(src.toByteBuffer());
      updateMax();
      return this;
  }
  
  public BitBuffer putChar(int index, char value) {
      ensureSize(Character.BYTES);
      buffer.putChar(index, value);
      updateMax();
      return this;
  }

  public BitBuffer putChar(char value) {
      ensureSize(Character.BYTES);
      buffer.putChar(value);
      updateMax();
      return this;
  }

  public BitBuffer putDouble(int index, double value) {
      ensureSize(Double.BYTES);
      buffer.putDouble(index, value);
      updateMax();
      return this;
  }

  public BitBuffer putDouble(double value) {
      ensureSize(Double.BYTES);
      buffer.putDouble(value);
      updateMax();
      return this;
  }

  public BitBuffer putFloat(int index, float value) {
      ensureSize(Float.BYTES);
      buffer.putFloat(index, value);
      updateMax();
      return this;
  }

  public BitBuffer putFloat(float value) {
      ensureSize(Float.BYTES);
      buffer.putFloat(value);
      updateMax();
      return this;
  }

  public BitBuffer putInt(int index, int value) {
      ensureSize(Integer.BYTES);
      buffer.putInt(index, value);
      updateMax();
      return this;
  }

  public BitBuffer putInt(int value) {
      ensureSize(Integer.BYTES);
      buffer.putInt(value);
      updateMax();
      return this;
  }

  public BitBuffer putLong(int index, long value) {
      ensureSize(Long.BYTES);
      buffer.putLong(index, value);
      updateMax();
      return this;
  }

  public BitBuffer putLong(long value) {
      ensureSize(Long.BYTES);
      buffer.putLong(value);
      updateMax();
      return this;
  }

  public BitBuffer putShort(int index, short value) {
      ensureSize(Short.BYTES);
      buffer.putShort(index, value);
      updateMax();
      return this;
  }

  public BitBuffer putShort(short value) {
      ensureSize(Short.BYTES);
      buffer.putShort(value);
      updateMax();
      return this;
  }
  
  public BitBuffer putUTF8(String str) {
    ensureSize(str.length());
    buffer.put(StandardCharsets.UTF_8.encode(str));
    updateMax();
    return this;
  }

  private void ensureSize(int i) {
    check();
    if (buffer.remaining() < i) {
      int newCap = Math.max(buffer.limit() * 2, buffer.limit() + i);
      ByteBuffer newBuffer = buffer.isDirect() ? ByteBuffer.allocateDirect(newCap) : ByteBuffer.allocate(newCap);
      newBuffer.order(buffer.order());
      buffer.flip();
      newBuffer.put(buffer);
      buffer = newBuffer;
    }
  }

  public BitBuffer duplicate() {
    check();
    return new BitBufferImpl(buffer.duplicate(), maxPosition);
  }

  public BitBuffer slice() {
    check();
    return new BitBufferImpl(buffer.slice());
  }

  public BitBuffer clear() {
    check();
    buffer.clear();
    resetMaxPosition();
    return this;
  }

  public BitBuffer flip() {
    check();
    buffer.flip();
    resetMaxPosition();
    return this;
  }

  public int limit() {
    check();
    return buffer.limit();
  }

  public BitBuffer limit(int newLimit) {
    check();
    buffer.limit(newLimit);
    return this;
  }

  public BitBuffer mark() {
    check();
    buffer.mark();
    return this;
  }

  public int position() {
    check();
    return buffer.position();
  }

  public BitBuffer position(int newPosition) {
    check();
    buffer.position(newPosition);
    updateMax();
    return this;
  }

  public int remaining() {
    check();
    return buffer.remaining();
  }

  public BitBuffer reset() {
    check();
    buffer.reset();
    return this;
  }

  public BitBuffer rewind() {
    check();
    buffer.rewind();
    return this;
  }

  public int capacity() {
    check();
    return buffer.capacity();
  }

  public boolean hasRemaining() {
    check();
    return buffer.hasRemaining();
  }

  public byte[] array() {
    check();
    if (!buffer.isDirect()) {
      return buffer.array();
    } else {
      final ByteBuffer duplicate = buffer.duplicate();
      duplicate.flip();
      final byte[] newBuffer = new byte[duplicate.limit()];
      duplicate.get(newBuffer);
      return newBuffer;
    }
  }

  public ByteOrder order() {
    check();
    return buffer.order();
  }

  public BitBuffer order(ByteOrder order) {
    check();
    buffer.order(order);
    return this;
  }

  public void close() {
    buffer = null;
  }

  private void check() {
    if (buffer == null) {
      throw new IllegalStateException("Buffer is closed!");
    }
  }

  public boolean isDirect() {
    check();
    return buffer.isDirect();
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("BitBuffer{ ");
    if (buffer != null) {
      sb.append("position=").append(buffer.position());
      sb.append(", maxPosition=").append(maxPosition);
      sb.append(", limit=").append(buffer.limit());
      sb.append(", capacity=").append(buffer.capacity());
      sb.append(", order=").append(buffer.order());
      sb.append(", direct=").append(buffer.isDirect());
    } else {
      sb.append("<CLOSED>");
    }
    sb.append(" }");
    return sb.toString();
  }
  
  public byte[] toByteArray() {
    if(!hasRemaining()) return new byte[0];
    int pos = position();
    byte[] bs = new byte[remaining()];
    get(bs);
    position(pos);
    return bs;
  }

  public ByteBuffer toByteBuffer() {
    return buffer;
  }

  public int writeTo(ByteBuffer buf) {
    int min = Math.min(buf.remaining(), remaining());
    int lim = limit();
    int pos = position();
    limit(min);
    buf.put(buffer);
    position(pos);
    limit(lim);
    return min;
  }

  public int writeTo(BitBuffer buf) {
    int min = Math.min(buf.remaining(), remaining());
    int lim = limit();
    int pos = position();
    limit(min);
    buf.put(buffer);
    position(pos);
    limit(lim);
    return min;
  }

  public int writeTo(WritableByteChannel chl) throws IOException {
    return chl.write(buffer);
  }

  public int readFrom(ReadableByteChannel chl) throws IOException {
    return chl.read(buffer);
  }

  public int readFrom(ByteBuffer buf) {
    int min = Math.min(remaining(), buf.remaining());
    put(buf);
    return min;
  }
  
  public int readFrom(BitBuffer buf) {
    int min = Math.min(remaining(), buf.remaining());
    put(buf);
    return min;
  }
  
}