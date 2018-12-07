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
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 05/12/2018
 */
public class UTFBinString implements BinString, Comparable<BinString> {
  
  private final DynamicByteBuffer buffer;
  
  private int length;
  
  
  public UTFBinString(int initialSize) {
    this.buffer = new DynamicByteBuffer(ByteBuffer.allocate(initialSize));
    length = 0;
  }
  
  
  public UTFBinString() {
    this(Long.BYTES * 2);
  }
  
  
  public UTFBinString(ByteBuffer buf) {
    length = buf.getInt();
    ByteBuffer ibuf = buf.isDirect() 
        ? ByteBuffer.allocateDirect(length) 
        : ByteBuffer.allocate(length);
    this.buffer = new DynamicByteBuffer(ibuf);
    buffer.putInt(length);
    buf.limit(buf.position() + length);
    buffer.put(buf);
  }
  
  
  public UTFBinString(DynamicByteBuffer buf) {
    length = buf.getInt();
    ByteBuffer ibuf = buf.isDirect() 
        ? ByteBuffer.allocateDirect(length) 
        : ByteBuffer.allocate(length);
    this.buffer = new DynamicByteBuffer(ibuf);
    buffer.putInt(length);
    buf.limit(buf.position() + length);
    buffer.put(buf);
  }
  
  
  public UTFBinString(int length, ByteBuffer content) {
    this.length = length;
    ByteBuffer ibuf = content.isDirect() 
        ? ByteBuffer.allocateDirect(length + Integer.BYTES) 
        : ByteBuffer.allocate(length + Integer.BYTES);
    this.buffer = new DynamicByteBuffer(ibuf);
    buffer.putInt(length);
    buffer.put(content);
  }
  
  
  public UTFBinString(byte[] bs) {
    this(Objects.requireNonNull(bs), 0, bs.length);
  }
  
  
  public UTFBinString(byte[] bs, int off, int len) {
    Objects.requireNonNull(bs);
    if(off < 0 || len < 1 || off + len > bs.length) {
      throw new IllegalArgumentException("Bad array offset/length: " + off + "/" + len);
    }
    this.buffer = new DynamicByteBuffer(ByteBuffer.allocate(len + Integer.BYTES));
    length = len;
    buffer.putInt(length);
    buffer.put(bs, off, len);
  }
  
  
  public UTFBinString(String str) {
    ByteBuffer bs = StandardCharsets.UTF_8.encode(str);
    this.buffer = new DynamicByteBuffer(ByteBuffer.allocate(bs.remaining() + Integer.BYTES));
    this.length = bs.remaining();
    buffer.putInt(bs.remaining());
    buffer.put(bs);
  }
  
  
  @Override
  public ByteBuffer getContentBuffer() {
    buffer.position(Integer.BYTES);
    buffer.limit(Integer.BYTES + length);
    return buffer.slice().toByteBuffer();
  }
  
  
  @Override
  public byte[] getContentBytes() {
    ByteBuffer buf = this.getContentBuffer();
    byte[] bs = new byte[buf.remaining()];
    buf.get(bs);
    return bs;
  }
  
  
  @Override
  public BinString append(String str) {
    ByteBuffer bs = StandardCharsets.UTF_8.encode(str);
    int len = bs.remaining();
    buffer.position(0)
        .putInt(length + len)
        .position(Integer.BYTES + length)
        .put(bs);
    length += len;
    return this;
  }
  
  
  @Override
  public BinString append(BinString str) {
    buffer.position(0).putInt(length + str.length());
    buffer.position(length + Integer.BYTES);
    buffer.put(str.getContentBuffer());
    length += str.length();
    return this;
  }
  
  
  @Override
  public int indexOf(String str, int start) {
    ByteBuffer bs = StandardCharsets.UTF_8.encode(str);
    int len = bs.remaining();
    int readed = 0;
    int readlen = length;
    while((readed + len) <= readlen) {
      buffer.position(Integer.BYTES + start + readed);
      int count = 0;
      for(int i = 0; i < len; i++) {
        if(buffer.get() == bs.get()) count++;
      }
      bs.position(0);
      if(count == len) {
        return buffer.position() - len - Integer.BYTES;
      }
      readed++;
    }
    return -1;
  }
  
  
  @Override
  public int indexOf(BinString str, int start) {
    ByteBuffer bs = str.getContentBuffer();
    int pos = bs.position();
    int len = bs.remaining();
    int readed = 0;
    int readlen = length;
    while((readed + len) <= readlen) {
      buffer.position(Integer.BYTES + start + readed);
      int count = 0;
      for(int i = 0; i < len; i++) {
        if(buffer.get() == bs.get()) count++;
      }
      bs.position(pos);
      if(count == len) {
        return buffer.position() - len - Integer.BYTES;
      }
      readed++;
    }
    return -1;
  }
  
  
  @Override
  public boolean contains(String str) {
    return indexOf(str, 0) >= 0;
  }
  
  
  @Override
  public boolean contains(BinString str) {
    return indexOf(str, 0) >= 0;
  }
  
  
  @Override
  public int length() {
    return length;
  }


  @Override
  public BinString slice(int offset, int length) {
    if(offset < 0 || length < 1 || offset + length > this.length) {
      throw new IllegalArgumentException(String.format("Bad offset/length: %d/%d (maxLength=%d)", offset, length, this.length));
    }
    buffer.position(Integer.BYTES + offset).limit(Integer.BYTES + offset + length);
    return new UTFBinString(length, buffer.slice().toByteBuffer());
  }
  
  
  @Override
  public BinString slice(int offset) {
    if(offset < 0) {
      throw new IllegalArgumentException(String.format("Bad offset: %d", offset));
    }
    buffer.position(Integer.BYTES + offset).limit(Integer.BYTES + length);
    return new UTFBinString(length - offset, buffer.slice().toByteBuffer());
  }
  
  
  @Override
  public String sha256sum() {
    return buffer.position(0).limit(Integer.BYTES + length).sha256sum();
  }


  @Override
  public ByteBuffer toByteBuffer() {
    return buffer.position(0).limit(Integer.BYTES + length).toByteBuffer();
  }


  @Override
  public byte[] toByteArray() {
    return buffer.position(0).limit(Integer.BYTES + length).toByteArray();
  }
  
  
  @Override
  public int writeTo(ByteBuffer buf) {
    buf.put(buffer.position(0).limit(Integer.BYTES + length).toByteBuffer());
    return length;
  }
  
  
  @Override
  public int writeTo(DynamicByteBuffer buf) {
    buf.put(buffer.position(0).limit(Integer.BYTES + length));
    return length;
  }
  
  
  @Override
  public int writeTo(WritableByteChannel ch) throws IOException {
    return ch.write(buffer.position(0).limit(Integer.BYTES + length).toByteBuffer());
  }


  @Override
  public int readFrom(ReadableByteChannel ch) throws IOException {
    ByteBuffer buf = buffer.toByteBuffer();
    buf.position(0);
    buf.limit(Integer.BYTES);
    ch.read(buf);
    buf.position(0);
    length = buf.getInt();
    if(length > 0) {
      buffer.ensureSize(length + Integer.BYTES);
      buf = buffer.toByteBuffer();
      buf.position(Integer.BYTES);
      buf.limit(length + Integer.BYTES);
      return ch.read(buf);
    }
    return length;
  }


  @Override
  public int readFrom(ByteBuffer buf) {
    length = buf.getInt();
    if(length > 0) {
      buf.limit(buf.position() + length);
      buffer.putInt(length).put(buf);
    }
    return length;
  }
  
  
  @Override
  public int readFrom(DynamicByteBuffer buf) {
    length = buf.getInt();
    if(length > 0) {
      buf.limit(buf.position() + length);
      buffer.putInt(length).put(buf);
    }
    return length;
  }
  
  
  @Override
  public int compareTo(BinString o) {
    buffer.position(0).limit(Integer.BYTES + length);
    return buffer.toByteBuffer().compareTo(o.toByteBuffer());
  }


  @Override
  public int hashCode() {
    return toString().hashCode();
  }


  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!BinString.class.isAssignableFrom(obj.getClass())) {
      return false;
    }
    final BinString other = (BinString) obj;
    if (this.length() != other.length()) {
      return false;
    }
    if (!Objects.equals(toString(), other.toString())) {
      return false;
    }
    return true;
  }
  
  
  @Override
  public String toString() {
    return StandardCharsets.UTF_8.decode(getContentBuffer()).toString();
  }

}
