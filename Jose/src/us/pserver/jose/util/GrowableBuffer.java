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

package us.pserver.jose.util;

import java.nio.ByteBuffer;
import us.pserver.tools.UTF8String;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 16/03/2017
 */
public class GrowableBuffer {
  
  public static final int DEF_INIT_SIZE = 1024;
  

  private ByteBuffer buffer;
  
  private final int size;
  
  
  public GrowableBuffer() {
    this(DEF_INIT_SIZE, false);
  }
  
  
  public GrowableBuffer(int initsize, boolean direct) {
    this.size = (initsize < 2 ? DEF_INIT_SIZE : initsize);
    this.buffer = (direct 
        ? ByteBuffer.allocateDirect(size) 
        : ByteBuffer.allocate(size)
    );
  }
  
  
  private GrowableBuffer(ByteBuffer buf, int size) {
    if(buf == null) {
      throw new IllegalArgumentException("Bad Null ByteBuffer");
    }
    this.buffer = buf;
    this.size = size;
  }
  
  
  public GrowableBuffer grow() {
    int size = buffer.capacity() + this.size;
    ByteBuffer nbuf = (buffer.isDirect()
        ? ByteBuffer.allocateDirect(size) 
        : ByteBuffer.allocate(size)
    );
    buffer.flip();
    nbuf.put(buffer);
    buffer = nbuf;
    return this;
  }
  
  
  public ByteBuffer getBuffer() {
    return buffer;
  }
  
  
  public GrowableBuffer put(byte b) {
    if(buffer.remaining() < 1) grow();
    buffer.put(b);
    return this;
  }
  
  
  public GrowableBuffer put(byte[] bs) {
    if(bs != null && bs.length > 0) {
      if(buffer.remaining() < bs.length) grow();
      buffer.put(bs);
    }
    return this;
  }
  
  
  public GrowableBuffer put(byte[] bs, int off, int len) {
    if(bs != null && bs.length > 0 && off >= 0 && (off+len) <= bs.length) {
      if(buffer.remaining() < bs.length) grow();
      buffer.put(bs, off, len);
    }
    return this;
  }
  
  
  public GrowableBuffer put(int i) {
    if(buffer.remaining() < Integer.BYTES) grow();
    buffer.putInt(i);
    return this;
  }
  
  
  public GrowableBuffer put(long l) {
    if(buffer.remaining() < Long.BYTES) grow();
    buffer.putLong(l);
    return this;
  }
  
  
  public GrowableBuffer put(short s) {
    if(buffer.remaining() < Short.BYTES) grow();
    buffer.putShort(s);
    return this;
  }
  
  
  public GrowableBuffer put(float f) {
    if(buffer.remaining() < Float.BYTES) grow();
    buffer.putFloat(f);
    return this;
  }
  
  
  public GrowableBuffer put(double d) {
    if(buffer.remaining() < Double.BYTES) grow();
    buffer.putDouble(d);
    return this;
  }
  
  
  public GrowableBuffer put(String s) {
    if(s != null && !s.isEmpty()) {
      if(buffer.remaining() < s.length()) grow();
      buffer.put(UTF8String.from(s).getBytes());
    }
    return this;
  }
  
  
  public byte[] getBytes() {
    if(buffer.position() < 1) return new byte[0];
    buffer.flip();
    byte[] bs = new byte[buffer.remaining()];
    if(buffer.hasArray()) {
      System.arraycopy(buffer.array(), 0, bs, 0, buffer.remaining());
    }
    else {
      buffer.get(bs);
    }
    return bs;
  }
  
}
