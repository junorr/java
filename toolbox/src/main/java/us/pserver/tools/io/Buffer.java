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
import java.util.Objects;
import java.util.function.IntFunction;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 18/10/2018
 */
public interface Buffer extends Cloneable {
  
  public static final int DEFAULT_BUFFER_SIZE = 32*1024;
  

  public int capacity();
  
  public int readLength();
  
  public int writeLength();
  
  public boolean isReadable();
  
  public boolean isWritable();
  
  public Buffer clear();
  
  public Buffer readMark();
  
  public Buffer readReset();
  
  public Buffer writeMark();
  
  public Buffer writeReset();
  
  public Buffer clone();
  
  public Buffer cloneShared();
  
  public boolean find(byte[] cont);
  
  public boolean find(byte[] cont, int ofs, int len);
  
  public boolean find(Buffer buf);
  
  public int fillBuffer(InputStream in) throws IOException;
  
  public int fillBuffer(InputStream in, int length) throws IOException;
  
  public int fillBuffer(ByteBuffer buf);
  
  public int fillBuffer(ByteBuffer buf, int length);
  
  public int fillBuffer(Buffer buf);
  
  public int fillBuffer(Buffer buf, int length);
  
  public int fillBuffer(byte[] src);
  
  public int fillBuffer(byte[] src, int ofs, int len);
  
  public int writeTo(OutputStream out) throws IOException;
  
  public int writeTo(OutputStream out, int length) throws IOException;
  
  public int writeTo(ByteBuffer out);
  
  public int writeTo(ByteBuffer out, int length);
  
  public int writeTo(Buffer out);
  
  public int writeTo(Buffer out, int length);
  
  public int writeTo(byte[] out);
  
  public int writeTo(byte[] out, int ofs, int len);
  
  public Buffer put(byte b);
  
  public Buffer put(short number);
  
  public Buffer put(int number);
  
  public Buffer put(long number);
  
  public Buffer put(float number);
  
  public Buffer put(double number);
  
  public byte get();
  
  public int getInt();
  
  public short getShort();
  
  public long getLong();
  
  public float getFloat();
  
  public double getDouble();
  
  public ByteBuffer toByteBuffer(IntFunction<ByteBuffer> allocPolicy);
  
  public ByteBuffer toByteBuffer();
  
  public byte[] toByteArray();
  
  public String getContentAsString(Charset cs);
  
  public default String getContentAsString() {
    return getContentAsString(StandardCharsets.UTF_8);
  }
  
  
  
  public static BufferFactory heapFactory() {
    return new HeapBufferFactory();
  }
  
  public static BufferFactory directFactory() {
    return new DirectBufferFactory();
  }
  
  public static BufferFactory expansibleHeapFactory() {
    return new ExpansibleBufferFactory(heapFactory());
  }
  
  public static BufferFactory expansibleDirectFactory() {
    return new ExpansibleBufferFactory(directFactory());
  }
  
  
  
  
  
  public static interface BufferFactory {
    
    public Buffer create(int size);
    
    public Buffer create(byte[] src);
    
    public Buffer create(byte[] src, int ofs, int len);
    
    public Buffer create(ByteBuffer src);
    
    public Buffer create(ByteBuffer src, int length);
    
    public Buffer create(String src);
    
    public Buffer create(String src, Charset cs);
    
  }
  
  
  
  public static class HeapBufferFactory implements BufferFactory {
    
    @Override
    public HeapBuffer create(int size) {
      if(size < 1) {
        throw new IllegalArgumentException("Bad size: " + size);
      }
      return new HeapBuffer(size);
    }
    
    @Override
    public HeapBuffer create(byte[] src) {
      return create(Objects.requireNonNull(src), 0, src.length);
    }
    
    @Override
    public HeapBuffer create(byte[] src, int ofs, int length) {
      if(src == null || src.length == 0) {
        throw new IllegalArgumentException(String.format("Bad source byte array: %s", (src == null ? src : src.length)));
      }
      if(ofs < 0 || length < 1 || ofs + length > src.length) {
        throw new IllegalArgumentException(String.format("Bad offset/length: %d/%d", ofs, length));
      }
      HeapBuffer buf = new HeapBuffer(length);
      buf.fillBuffer(src, ofs, length);
      return buf;
    }
    
    @Override
    public HeapBuffer create(ByteBuffer src) {
      return create(src, src.remaining());
    }
    
    @Override
    public HeapBuffer create(ByteBuffer src, int length) {
      if(length < 1) return HeapBuffer.EMPTY_BUFFER;
      Objects.requireNonNull(src);
      HeapBuffer buf = new HeapBuffer(length);
      buf.fillBuffer(src, length);
      return buf;
    }
    
    @Override
    public HeapBuffer create(String src) {
      return create(src, StandardCharsets.UTF_8);
    }
    
    @Override
    public HeapBuffer create(String src, Charset cs) {
      Objects.requireNonNull(src);
      if(src.isEmpty()) return HeapBuffer.EMPTY_BUFFER;
      Objects.requireNonNull(cs);
      ByteBuffer bb = cs.encode(src);
      HeapBuffer buf = new HeapBuffer(bb.remaining());
      buf.fillBuffer(bb);
      return buf;
    }
    
  }
  
  
  
  public static class DirectBufferFactory implements BufferFactory {
    
    @Override
    public DirectBuffer create(int size) {
      if(size < 1) {
        throw new IllegalArgumentException("Bad size: " + size);
      }
      return new DirectBuffer(size);
    }
    
    @Override
    public DirectBuffer create(byte[] src) {
      return create(Objects.requireNonNull(src), 0, src.length);
    }
    
    @Override
    public DirectBuffer create(byte[] src, int ofs, int length) {
      if(src == null || src.length == 0) {
        throw new IllegalArgumentException(String.format("Bad source byte array: %s", (src == null ? src : src.length)));
      }
      if(ofs < 0 || length < 1 || ofs + length > src.length) {
        throw new IllegalArgumentException(String.format("Bad offset/length: %d/%d", ofs, length));
      }
      DirectBuffer buf = new DirectBuffer(length);
      buf.fillBuffer(src, ofs, length);
      return buf;
    }
    
    @Override
    public DirectBuffer create(ByteBuffer src) {
      return create(src, src.remaining());
    }
    
    @Override
    public DirectBuffer create(ByteBuffer src, int length) {
      if(length < 1) {
        throw new IllegalArgumentException("Bad length: " + length);
      }
      Objects.requireNonNull(src);
      DirectBuffer buf = new DirectBuffer(length);
      buf.fillBuffer(src, length);
      return buf;
    }
    
    @Override
    public DirectBuffer create(String src) {
      return create(src, StandardCharsets.UTF_8);
    }
    
    @Override
    public DirectBuffer create(String src, Charset cs) {
      Objects.requireNonNull(src);
      if(src.isEmpty()) {
        throw new IllegalArgumentException(String.format("Bad source String: '%s'", src));
      }
      Objects.requireNonNull(cs);
      ByteBuffer bb = cs.encode(src);
      DirectBuffer buf = new DirectBuffer(bb.remaining());
      buf.fillBuffer(bb);
      return buf;
    }
    
  }
  
  
  
  public static class ExpansibleBufferFactory implements BufferFactory {
    
    private final BufferFactory factory;
    
    public ExpansibleBufferFactory(BufferFactory factory) {
      this.factory = Objects.requireNonNull(factory);
    }
    
    @Override
    public ExpansibleBuffer create(int size) {
      if(size < 1) {
        throw new IllegalArgumentException("Bad size: " + size);
      }
      return new ExpansibleBuffer(factory, size);
    }
    
    @Override
    public ExpansibleBuffer create(byte[] src) {
      return create(Objects.requireNonNull(src), 0, src.length);
    }
    
    @Override
    public ExpansibleBuffer create(byte[] src, int ofs, int length) {
      if(src == null || src.length == 0) {
        throw new IllegalArgumentException(String.format("Bad source byte array: %s", (src == null ? src : src.length)));
      }
      if(ofs < 0 || length < 1 || ofs + length > src.length) {
        throw new IllegalArgumentException(String.format("Bad offset/length: %d/%d", ofs, length));
      }
      ExpansibleBuffer buf = new ExpansibleBuffer(factory, length);
      buf.fillBuffer(src, ofs, length);
      return buf;
    }
    
    @Override
    public ExpansibleBuffer create(ByteBuffer src) {
      return create(src, src.remaining());
    }
    
    @Override
    public ExpansibleBuffer create(ByteBuffer src, int length) {
      if(length < 1) {
        throw new IllegalArgumentException("Bad length: " + length);
      }
      Objects.requireNonNull(src);
      ExpansibleBuffer buf = new ExpansibleBuffer(factory, length);
      buf.fillBuffer(src, length);
      return buf;
    }
    
    @Override
    public ExpansibleBuffer create(String src) {
      return create(src, StandardCharsets.UTF_8);
    }
    
    @Override
    public ExpansibleBuffer create(String src, Charset cs) {
      Objects.requireNonNull(src);
      if(src.isEmpty()) {
        throw new IllegalArgumentException(String.format("Bad source String: '%s'", src));
      }
      Objects.requireNonNull(cs);
      ByteBuffer bb = cs.encode(src);
      ExpansibleBuffer buf = new ExpansibleBuffer(factory, bb.remaining());
      buf.fillBuffer(bb);
      return buf;
    }
    
  }
  
}
