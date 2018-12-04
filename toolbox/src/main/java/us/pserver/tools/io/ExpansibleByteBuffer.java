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

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.IntFunction;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 02/12/2018
 */
public class ExpansibleByteBuffer {

  public static enum AllocPolicy {
    
    HEAP_ALLOC_POLICY(ByteBuffer::allocate),
    
    DIRECT_ALLOC_POLICY(ByteBuffer::allocateDirect);
    
    private AllocPolicy(IntFunction<ByteBuffer> allocPolicy) {
      this.allocPolicy = allocPolicy;
    }
    
    private final IntFunction<ByteBuffer> allocPolicy;
    
    public ByteBuffer allocate(int size) {
      return allocPolicy.apply(size);
    }
    
  }
  
  
  private final AllocPolicy allocPolicy;
  
  private final int initialCapacity;
  
  private final ArrayList<ByteBuffer> buffers;
  
  private volatile int index, mark;
  
  
  private ExpansibleByteBuffer(Collection<ByteBuffer> buffers, AllocPolicy allocPolicy, int initialCapacity, int index, int mark, int position, int limit) {
    this.allocPolicy = Objects.requireNonNull(allocPolicy);
    if(initialCapacity <= 0) {
      throw new IllegalArgumentException("initialCapacity must be greater than 0");
    }
    this.initialCapacity = initialCapacity;
    this.buffers = new ArrayList<>(buffers);
    this.index = index;
    this.mark = mark;
    limit(limit);
    position(position);
  }
  
  
  public ExpansibleByteBuffer(AllocPolicy allocPolicy, int initialCapacity) {
    this(Arrays.asList(allocPolicy.allocate(initialCapacity)), allocPolicy, initialCapacity, 0, 0, 0, initialCapacity);
  }
  
  
  public boolean hasArray() {
    return allocPolicy == AllocPolicy.HEAP_ALLOC_POLICY;
  }


  public Set<byte[]> array() {
    if(allocPolicy == AllocPolicy.DIRECT_ALLOC_POLICY) {
      throw new UnsupportedOperationException("Buffer not backed by array");
    }
    HashSet<byte[]> set = new HashSet<>();
    buffers.forEach(b -> set.add(b.array()));
    return Collections.unmodifiableSet(set);
  }
  
  
  public int arrayOffset() {
    return buffers.stream().mapToInt(b -> b.position()).sum();
  }


  public boolean isDirect() {
    return allocPolicy == AllocPolicy.DIRECT_ALLOC_POLICY;
  }


  public ExpansibleByteBuffer slice() {
    return new ExpansibleByteBuffer(buffers, allocPolicy, initialCapacity, index, 0, position(), limit());
  }


  public ExpansibleByteBuffer duplicate() {
    return new ExpansibleByteBuffer(buffers, allocPolicy, initialCapacity, index, mark, position(), limit());
  }
  
  
  public int position() {
    return buffers.stream().mapToInt(ByteBuffer::position).sum();
  }
  
  
  public ExpansibleByteBuffer mark() {
    mark = position();
    return this;
  }
  
  
  public ExpansibleByteBuffer reset() {
    position(mark);
    return this;
  }
  
  
  @Override
  public String toString() {
    return String.format("ExpansibleByteBuffer[index=%d, position=%d, limit=%d, capacity=%d]", index, position(), limit(), capacity());
  }
  
  
  public int remaining() {
    return limit() - position();
  }
  
  
  public boolean hasRemaining() {
    return remaining() > 0;
  }
  
  
  public int capacity() {
    return buffers.stream().mapToInt(ByteBuffer::capacity).sum();
  }
  
  
  public int limit() {
    return buffers.stream().mapToInt(ByteBuffer::limit).sum();
  }
  
  
  public ExpansibleByteBuffer rewind() {
    mark = 0;
    return position(0);
  }
  
  
  public ExpansibleByteBuffer clear() {
    mark = 0;
    limit(capacity());
    return position(0);
  }
  
  
  public ExpansibleByteBuffer position(int pos) {
    if(pos < 0 || pos > limit()) {
      throw new IndexOutOfBoundsException("Bad position: " + pos + " (0 <= position < " + limit() + ")");
    }
    buffers.forEach(b -> b.position(0));
    int position = pos;
    for(int i = 0; i < buffers.size(); i++) {
      ByteBuffer b = buffers.get(i);
      int p = Math.min(position, b.limit());
      b.position(p);
      position -= p;
      if(position <= 0) {
        index = i;
        break;
      }
    }
    return this;
  }
  
  
  public ExpansibleByteBuffer limit(int lim) {
    if(lim < 0 || lim > capacity()) {
      throw new IndexOutOfBoundsException("Bad limit: " + lim + " (0 <= limit <= " + capacity() + ")");
    }
    int limit = lim;
    for(int i = 0; i < buffers.size(); i++) {
      ByteBuffer b = buffers.get(i);
      int p = Math.min(limit, b.capacity());
      b.position(p);
      limit -= p;
      if(limit <= 0) {
        index = i;
        break;
      }
    }
    return this;
  }
  
  
  public ExpansibleByteBuffer flip() {
    index = 0;
    buffers.forEach(ByteBuffer::flip);
    return this;
  }
  
  
  private void checkRemaining(int len) {
    if(len > remaining()) {
      throw new BufferUnderflowException();
    }
  }
  
  
  private void checkRemaining(int idx, int len) {
    if((position() + remaining()) < (idx + len)) {
      throw new BufferUnderflowException();
    }
  }
  
  
  public ExpansibleByteBuffer put(byte[] bs) {
    return put(Objects.requireNonNull(bs), 0, bs.length);
  }
  
  
  public ExpansibleByteBuffer put(byte[] bs, int off, int len) {
    Objects.requireNonNull(bs);
    if(off < 0 || len < 1 || off + len < bs.length) {
      throw new IllegalArgumentException(String.format("Bad offset/length: %d/%d (0 >= off <= %d and 1 >= len <= %d)", off, len, (bs.length - len), (bs.length - off)));
    }
    ensureCapacity(len);
    int offset = off;
    int length = len;
    while(length > 0) {
      ByteBuffer b = buffers.get(index);
      if(!b.hasRemaining()) {
        b = buffers.get(++index);
      }
      int min = Math.min(length, b.remaining());
      b.put(bs, offset, min);
      length -= min;
      offset += min;
    }
    return this;
  }
  
  
  public ExpansibleByteBuffer put(ByteBuffer buf) {
    Objects.requireNonNull(buf);
    if(buf == null || !buf.hasRemaining()) {
      throw new IllegalArgumentException(String.format("Bad buffer: %s", (buf == null ? null : buf.remaining())));
    }
    ensureCapacity(buf.remaining());
    while(buf.hasRemaining()) {
      ByteBuffer b = buffers.get(index);
      if(!b.hasRemaining()) {
        b = buffers.get(++index);
      }
      int min = Math.min(buf.remaining(), b.remaining());
      int lim = buf.limit();
      buf.limit(buf.position() + min);
      b.put(buf);
      buf.limit(lim);
    }
    return this;
  }
  
  
  public ExpansibleByteBuffer get(byte[] bs) {
    return get(Objects.requireNonNull(bs), 0, bs.length);
  }
  
  
  public ExpansibleByteBuffer get(byte[] bs, int off, int len) {
    Objects.requireNonNull(bs);
    if(off < 0 || len < 1 || off + len < bs.length) {
      throw new IllegalArgumentException(String.format("Bad offset/length: %d/%d (0 >= off <= %d and 1 >= len <= %d)", off, len, (bs.length - len), (bs.length - off)));
    }
    checkRemaining(len);
    int offset = off;
    int length = len;
    while(length > 0) {
      ByteBuffer b = buffers.get(index);
      if(!b.hasRemaining()) {
        b = buffers.get(++index);
      }
      int min = Math.min(length, b.remaining());
      b.get(bs, offset, min);
      length -= min;
      offset += min;
    }
    return this;
  }
  
  
  public ExpansibleByteBuffer get(ByteBuffer buf) {
    Objects.requireNonNull(buf);
    if(buf == null || !buf.hasRemaining()) {
      throw new IllegalArgumentException(String.format("Bad buffer: %s", (buf == null ? null : buf.remaining())));
    }
    ensureLength(buf.remaining());
    while(buf.hasRemaining()) {
      ByteBuffer b = buffers.get(index);
      if(!b.hasRemaining()) {
        b = buffers.get(++index);
      }
      int min = Math.min(buf.remaining(), b.remaining());
      int lim = b.limit();
      b.limit(b.position() + min);
      buf.put(b);
      b.limit(lim);
    }
    return this;
  }
  
  
  public byte get() {
    ensureLength(Byte.BYTES);
    return buffers.get(index).get();
  }


  public ExpansibleByteBuffer put(byte b) {
    ensureCapacity(Byte.BYTES);
    buffers.get(index).put(b);
    return this;
  }


  public byte get(int idx) {
    ensureLength(idx, Byte.BYTES);
    return buffers.get(index).get();
  }


  public ExpansibleByteBuffer put(int idx, byte b) {
    ensureCapacity(idx, Byte.BYTES);
    buffers.get(index).put(b);
    return this;
  }


  public ExpansibleByteBuffer compact() {
    buffers.forEach(ByteBuffer::compact);
    return this;
  }


  public char getChar() {
    ensureLength(Character.BYTES);
    ByteBuffer bb = ByteBuffer.allocate(Character.BYTES);
    get(bb);
    bb.flip();
    return bb.getChar();
  }


  public ExpansibleByteBuffer putChar(char value) {
    ByteBuffer bb = ByteBuffer.allocate(Character.BYTES);
    bb.putChar(value);
    bb.flip();
    put(bb);
    return this;
  }


  public char getChar(int idx) {
    ensureLength(idx, Character.BYTES);
    ByteBuffer bb = ByteBuffer.allocate(Character.BYTES);
    get(bb);
    bb.flip();
    return bb.getChar();
  }


  public ExpansibleByteBuffer putChar(int idx, char value) {
    ensureCapacity(idx, Character.BYTES);
    ByteBuffer bb = ByteBuffer.allocate(Character.BYTES);
    bb.putChar(value);
    bb.flip();
    put(bb);
    return this;
  }


  public short getShort() {
    ensureLength(Short.BYTES);
    ByteBuffer bb = ByteBuffer.allocate(Short.BYTES);
    get(bb);
    bb.flip();
    return bb.getShort();
  }


  public ExpansibleByteBuffer putShort(short value) {
    ByteBuffer bb = ByteBuffer.allocate(Short.BYTES);
    bb.putShort(value);
    bb.flip();
    put(bb);
    return this;
  }


  public short getShort(int idx) {
    ensureLength(idx, Short.BYTES);
    ByteBuffer bb = ByteBuffer.allocate(Short.BYTES);
    get(bb);
    bb.flip();
    return bb.getShort();
  }


  public ExpansibleByteBuffer putShort(int idx, short value) {
    ensureCapacity(idx, Short.BYTES);
    ByteBuffer bb = ByteBuffer.allocate(Short.BYTES);
    bb.putShort(value);
    bb.flip();
    put(bb);
    return this;
  }


  public int getInt() {
    ensureLength(Integer.BYTES);
    ByteBuffer bb = ByteBuffer.allocate(Integer.BYTES);
    get(bb);
    bb.flip();
    return bb.getInt();
  }


  public ExpansibleByteBuffer putInt(int value) {
    ByteBuffer bb = ByteBuffer.allocate(Integer.BYTES);
    bb.putInt(value);
    bb.flip();
    put(bb);
    return this;
  }


  public int getInt(int idx) {
    ensureLength(idx, Integer.BYTES);
    ByteBuffer bb = ByteBuffer.allocate(Integer.BYTES);
    get(bb);
    bb.flip();
    return bb.getInt();
  }


  public ExpansibleByteBuffer putInt(int idx, int value) {
    ensureCapacity(idx, Integer.BYTES);
    ByteBuffer bb = ByteBuffer.allocate(Integer.BYTES);
    bb.putInt(value);
    bb.flip();
    put(bb);
    return this;
  }


  public long getLong() {
    ensureLength(Long.BYTES);
    ByteBuffer bb = ByteBuffer.allocate(Long.BYTES);
    get(bb);
    bb.flip();
    return bb.getLong();
  }


  public ExpansibleByteBuffer putLong(long value) {
    ByteBuffer bb = ByteBuffer.allocate(Long.BYTES);
    bb.putLong(value);
    bb.flip();
    put(bb);
    return this;
  }


  public long getLong(int idx) {
    ensureLength(idx, Long.BYTES);
    ByteBuffer bb = ByteBuffer.allocate(Long.BYTES);
    get(bb);
    bb.flip();
    return bb.getLong();
  }


  public ExpansibleByteBuffer putLong(int idx, long value) {
    ensureCapacity(idx, Long.BYTES);
    ByteBuffer bb = ByteBuffer.allocate(Long.BYTES);
    bb.putLong(value);
    bb.flip();
    put(bb);
    return this;
  }


  public float getFloat() {
    ensureLength(Float.BYTES);
    ByteBuffer bb = ByteBuffer.allocate(Float.BYTES);
    get(bb);
    bb.flip();
    return bb.getFloat();
  }


  public ExpansibleByteBuffer putFloat(float value) {
    ByteBuffer bb = ByteBuffer.allocate(Float.BYTES);
    bb.putFloat(value);
    bb.flip();
    put(bb);
    return this;
  }


  public float getFloat(int idx) {
    ensureLength(idx, Float.BYTES);
    ByteBuffer bb = ByteBuffer.allocate(Float.BYTES);
    get(bb);
    bb.flip();
    return bb.getFloat();
  }


  public ExpansibleByteBuffer putFloat(int idx, float value) {
    ensureCapacity(idx, Float.BYTES);
    ByteBuffer bb = ByteBuffer.allocate(Float.BYTES);
    bb.putFloat(value);
    bb.flip();
    put(bb);
    return this;
  }


  public double getDouble() {
    ensureLength(Double.BYTES);
    ByteBuffer bb = ByteBuffer.allocate(Double.BYTES);
    get(bb);
    bb.flip();
    return bb.getDouble();
  }


  public ExpansibleByteBuffer putDouble(double value) {
    ByteBuffer bb = ByteBuffer.allocate(Double.BYTES);
    bb.putDouble(value);
    bb.flip();
    put(bb);
    return this;
  }


  public double getDouble(int idx) {
    ensureLength(idx, Double.BYTES);
    ByteBuffer bb = ByteBuffer.allocate(Double.BYTES);
    get(bb);
    bb.flip();
    return bb.getDouble();
  }


  public ExpansibleByteBuffer putDouble(int idx, double value) {
    ensureCapacity(idx, Double.BYTES);
    ByteBuffer bb = ByteBuffer.allocate(Double.BYTES);
    bb.putDouble(value);
    bb.flip();
    put(bb);
    return this;
  }
  
  
  private void ensureLength(int len) {
    checkRemaining(len);
    if(!buffers.get(index).hasRemaining()) {
      buffers.get(++index).position(0);
    }
  }


  private void ensureLength(int idx, int len) {
    checkRemaining(idx, len);
    position(idx);
    if(!buffers.get(index).hasRemaining()) {
      index++;
    }
  }


  private void ensureCapacity(int len) {
    if(len > remaining()) {
      int total = len;
      while(total > 0) {
        buffers.add(allocPolicy.allocate(initialCapacity));
        total -= initialCapacity;
      }
    }
    ensureLength(len);
  }
  
  private void ensureCapacity(int idx, int len) {
    if((position() + remaining()) < (idx + len)) {
      int total = len;
      while(total > 0) {
        buffers.add(allocPolicy.allocate(initialCapacity));
        total -= initialCapacity;
      }
    }
    ensureLength(idx, len);
  }

}
