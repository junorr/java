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

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.security.MessageDigest;
import us.pserver.tools.Hash;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 04/12/2018
 */
public final class DynamicByteBuffer implements BinaryForm {

  private ByteBuffer buffer;

  public DynamicByteBuffer(int cap, boolean useDirectBuffer) {
      buffer = useDirectBuffer ? ByteBuffer.allocateDirect(cap) : ByteBuffer.allocate(cap);
  }

  public DynamicByteBuffer(byte[] array) {
      buffer = array != null ? ByteBuffer.wrap(array) : ByteBuffer.allocate(0);
  }

  public DynamicByteBuffer(byte[] array, int offset, int length) {
      buffer = ByteBuffer.wrap(array, offset, length);
  }

  public DynamicByteBuffer(ByteBuffer buffer) {
      this.buffer = buffer;
  }

  public DynamicByteBuffer compact() {
      buffer.compact();
      return this;
  }

  public byte get() {
      return buffer.get();
  }

  public byte get(int index) {
      return buffer.get(index);
  }

  public DynamicByteBuffer get(byte[] dst) {
      buffer.get(dst);
      return this;
  }

  public DynamicByteBuffer get(byte[] dst, int offset, int length) {
      buffer.get(dst, offset, length);
      return this;
  }

  public char getChar() {
      check();
      return buffer.getChar();
  }

  public char getChar(int index) {
      check();
      return buffer.getChar(index);
  }

  public double getDouble() {
      check();
      return buffer.getDouble();
  }

  public double getDouble(int index) {
      check();
      return buffer.getDouble(index);
  }

  public float getFloat() {
      check();
      return buffer.getFloat();
  }

  public float getFloat(int index) {
      check();
      return buffer.getFloat(index);
  }

  public int getInt() {
      check();
      return buffer.getInt();
  }

  public int getInt(int index) {
      check();
      return buffer.getInt(index);
  }

  public long getLong() {
      check();
      return buffer.getLong();
  }

  public long getLong(int index) {
      check();
      return buffer.getLong(index);
  }

  public short getShort() {
      check();
      return buffer.getShort();
  }

  public short getShort(int index) {
      check();
      return buffer.getShort(index);
  }

  public DynamicByteBuffer put(byte b) {
      ensureSize(1);
      buffer.put(b);
      return this;
  }

  public DynamicByteBuffer put(int index, byte b) {
      ensureSize(1);
      buffer.put(index, b);
      return this;
  }

  public DynamicByteBuffer put(byte[] src) {
      ensureSize(src.length);
      buffer.put(src);
      return this;
  }

  public DynamicByteBuffer put(byte[] src, int offset, int length) {
      ensureSize(length);
      buffer.put(src, offset, length);
      return this;
  }

  public DynamicByteBuffer put(ByteBuffer src) {
      ensureSize(src.remaining());
      buffer.put(src);
      return this;
  }

  public DynamicByteBuffer putChar(int index, char value) {
      ensureSize(2);
      buffer.putChar(index, value);
      return this;
  }

  public DynamicByteBuffer putChar(char value) {
      ensureSize(2);
      buffer.putChar(value);
      return this;
  }

  public DynamicByteBuffer putDouble(int index, double value) {
      ensureSize(8);
      buffer.putDouble(index, value);
      return this;
  }

  public DynamicByteBuffer putDouble(double value) {
      ensureSize(8);
      buffer.putDouble(value);
      return this;
  }

  public DynamicByteBuffer putFloat(int index, float value) {
      ensureSize(4);
      buffer.putFloat(index, value);
      return this;
  }

  public DynamicByteBuffer putFloat(float value) {
      ensureSize(4);
      buffer.putFloat(value);
      return this;
  }

  public DynamicByteBuffer putInt(int index, int value) {
      ensureSize(4);
      buffer.putInt(index, value);
      return this;
  }

  public DynamicByteBuffer putInt(int value) {
      ensureSize(4);
      buffer.putInt(value);
      return this;
  }

  public DynamicByteBuffer putLong(int index, long value) {
      ensureSize(8);
      buffer.putLong(index, value);
      return this;
  }

  public DynamicByteBuffer putLong(long value) {
      ensureSize(8);
      buffer.putLong(value);
      return this;
  }

  public DynamicByteBuffer putShort(int index, short value) {
      ensureSize(2);
      buffer.putShort(index, value);
      return this;
  }

  public DynamicByteBuffer putShort(short value) {
      ensureSize(2);
      buffer.putShort(value);
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

  public DynamicByteBuffer duplicate() {
      check();
      return new DynamicByteBuffer(buffer.duplicate());
  }

  public DynamicByteBuffer slice() {
      check();
      return new DynamicByteBuffer(buffer.slice());
  }

  public DynamicByteBuffer clear() {
      check();
      buffer.clear();
      return this;
  }

  public DynamicByteBuffer flip() {
      check();
      buffer.flip();
      return this;
  }

  public int limit() {
      check();
      return buffer.limit();
  }

  public DynamicByteBuffer limit(int newLimit) {
      check();
      buffer.limit(newLimit);
      return this;
  }

  public DynamicByteBuffer mark() {
      check();
      buffer.mark();
      return this;
  }

  public int position() {
      check();
      return buffer.position();
  }

  public DynamicByteBuffer position(int newPosition) {
      check();
      buffer.position(newPosition);
      return this;
  }

  public int remaining() {
      check();
      return buffer.remaining();
  }

  public DynamicByteBuffer reset() {
      check();
      buffer.reset();
      return this;
  }

  public DynamicByteBuffer rewind() {
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

  public DynamicByteBuffer order(ByteOrder order) {
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
      final StringBuilder sb = new StringBuilder("DynamicByteBuffer{");
      if (buffer != null) {
          sb.append("position=").append(buffer.position());
          sb.append(", limit=").append(buffer.limit());
          sb.append(", capacity=").append(buffer.capacity());
          sb.append(", order=").append(buffer.order());
          sb.append(", direct=").append(buffer.isDirect());
      } else {
          sb.append("<CLOSED>");
      }
      sb.append('}');
      return sb.toString();
  }
  
  
  @Override
  public byte[] toByteArray() {
    if(!hasRemaining()) return new byte[0];
    int pos = position();
    byte[] bs = new byte[remaining()];
    get(bs);
    position(pos);
    return bs;
  }


  @Override
  public String sha256sum() {
    return Hash.sha256().of(toByteArray());
  }


  @Override
  public ByteBuffer toByteBuffer() {
    return buffer.duplicate();
  }


  @Override
  public DynamicByteBuffer writeTo(ByteBuffer buf) {
    int min = Math.min(buf.remaining(), remaining());
    int lim = limit();
    int pos = position();
    limit(min);
    buf.put(buffer);
    position(pos);
    limit(lim);
    return this;
  }


  @Override
  public DynamicByteBuffer writeTo(WritableByteChannel chl) {
    
  }


  @Override
  public BinaryForm readFrom(ReadableByteChannel chl) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }


  @Override
  public BinaryForm readFrom(ByteBuffer buf) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }


  @Override
  public int compareTo(BinaryForm o) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
}