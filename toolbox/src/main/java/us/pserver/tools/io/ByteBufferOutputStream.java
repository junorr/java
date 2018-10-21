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

import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.function.IntFunction;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 16/09/2017
 */
public class ByteBufferOutputStream extends OutputStream {
  
  public static final IntFunction<ByteBuffer> ALLOC_POLICY_DIRECT = ByteBuffer::allocateDirect;
  
  public static final IntFunction<ByteBuffer> ALLOC_POLICY_HEAP = ByteBuffer::allocate;
  
  public static final int DEFAULT_INITIAL_SIZE = 32 * 1024;
  
  
  private final List<ByteBuffer> buffers;
  
  private final int bufsize;
  
  private final IntFunction<ByteBuffer> alloc;
  
  private int current;
  
  
  public ByteBufferOutputStream(int initialSize, IntFunction<ByteBuffer> allocPolicy) {
    if(initialSize < 32) {
      throw new IllegalArgumentException("Bad initial buffer size: "+ initialSize);
    }
    if(allocPolicy == null) {
      throw new IllegalArgumentException("Bad null allocation policy function");
    }
    this.bufsize = initialSize;
    this.alloc = allocPolicy;
    this.buffers = new ArrayList<>();
    this.current = 0;
    this.buffers.add(alloc.apply(bufsize));
  }
  
  
  public ByteBufferOutputStream(int initialSize) {
    this(initialSize, ALLOC_POLICY_HEAP);
  }
  
  
  public ByteBufferOutputStream(IntFunction<ByteBuffer> allocPolicy) {
    this(DEFAULT_INITIAL_SIZE, allocPolicy);
  }
  
  
  public ByteBufferOutputStream() {
    this(DEFAULT_INITIAL_SIZE, ALLOC_POLICY_HEAP);
  }
  
  
  public int size() {
    return this.buffers.stream()
        .mapToInt(ByteBuffer::position)
        .reduce(0, (i,s)->i+s);
  }
  
  
  public int pages() {
    return this.buffers.size();
  }
  
  
  private ByteBuffer getBuffer(int writeSize) {
    if(this.buffers.get(current).remaining() < writeSize) {
      buffers.add(alloc.apply(Math.max(bufsize, writeSize)));
      current = buffers.size() - 1;
    }
    return this.buffers.get(current);
  }
  
  
  @Override
  public void write(int b) {
    this.getBuffer(1).put((byte) b);
  }
  
  
  @Override
  public void write(byte[] bs, int off, int len) {
    this.getBuffer(len).put(bs, off, len);
  }
  
  
  @Override
  public void write(byte[] bs) {
    this.write(bs, 0, bs.length);
  }
  
  
  public void write(ByteBuffer buf) {
    this.getBuffer(buf.remaining()).put(buf);
  }
  
  
  public ByteBufferOutputStream clear() {
    buffers.forEach(ByteBuffer::clear);
    current = 0;
    return this;
  }
  
  
  public ByteBuffer toByteBuffer(IntFunction<ByteBuffer> allocPolicy) {
    this.buffers.forEach(ByteBuffer::flip);
    int size = this.buffers.stream()
        .mapToInt(ByteBuffer::remaining)
        .reduce(0, (i,s)->i+s);
    ByteBuffer buf = allocPolicy.apply(size+1);
    this.buffers.forEach(buf::put);
    buf.flip();
    return buf;
  }
  
  
  public ByteBuffer toByteBuffer() {
    return this.toByteBuffer(this.alloc);
  }

  
  public byte[] toByteArray() {
    this.buffers.forEach(ByteBuffer::flip);
    int size = this.buffers.stream()
        .mapToInt(ByteBuffer::remaining)
        .reduce(0, (i,s)->i+s);
    byte[] buf = new byte[size];
    int idx = 0;
    for(ByteBuffer b : buffers) {
      int rem = b.remaining();
      b.get(buf, idx, rem);
      idx += rem;
    }
    return buf;
  }
  
}
