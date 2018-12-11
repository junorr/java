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
import us.pserver.tools.Hash;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 05/12/2018
 */
public class UTFBinString implements BinaryString, Comparable<BinaryString> {
  
  private final ByteBuffer buffer;
  
  private final int length;
  
  
  public UTFBinString(ByteBuffer buf) {
    this.buffer = buf;
    this.length = buf.getInt();
  }
  
  
  public UTFBinString(int length, ByteBuffer content) {
    this.length = length;
    ByteBuffer ibuf = content.isDirect() 
        ? ByteBuffer.allocateDirect(length + Integer.BYTES) 
        : ByteBuffer.allocate(length + Integer.BYTES);
    this.buffer = ibuf;
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
    this.buffer = ByteBuffer.allocate(len + Integer.BYTES);
    this.length = len;
    buffer.putInt(length);
    buffer.put(bs, off, len);
  }
  
  
  public UTFBinString(String str) {
    ByteBuffer bs = StandardCharsets.UTF_8.encode(str);
    this.buffer = ByteBuffer.allocate(bs.remaining() + Integer.BYTES);
    this.length = str.length();
    buffer.putInt(length);
    buffer.put(bs);
  }
  
  
  @Override
  public ByteBuffer getContentBuffer() {
    buffer.position(Integer.BYTES);
    buffer.limit(Integer.BYTES + length);
    return buffer.slice();
  }
  
  
  @Override
  public byte[] getContentBytes() {
    ByteBuffer buf = this.getContentBuffer();
    byte[] bs = new byte[buf.remaining()];
    buf.get(bs);
    return bs;
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
  public int indexOf(BinaryString str, int start) {
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
  public boolean contains(BinaryString str) {
    return indexOf(str, 0) >= 0;
  }
  
  
  @Override
  public int length() {
    return length;
  }


  @Override
  public BinaryString slice(int offset, int length) {
    if(offset < 0 || length < 1 || offset + length > this.length) {
      throw new IllegalArgumentException(String.format("Bad offset/length: %d/%d (maxLength=%d)", offset, length, this.length));
    }
    buffer.position(Integer.BYTES + offset).limit(Integer.BYTES + offset + length);
    return new UTFBinString(length, buffer.slice());
  }
  
  
  @Override
  public BinaryString slice(int offset) {
    if(offset < 0) {
      throw new IllegalArgumentException(String.format("Bad offset: %d", offset));
    }
    buffer.position(Integer.BYTES + offset).limit(Integer.BYTES + length);
    return new UTFBinString(length - offset, buffer.slice());
  }
  
  
  @Override
  public String sha256sum() {
    return Hash.sha256().of(toByteArray());
  }


  @Override
  public ByteBuffer toByteBuffer() {
    return buffer;
  }


  @Override
  public byte[] toByteArray() {
    if(length < 1) return new byte[0];
    buffer.position(0).limit(length + Integer.BYTES);
    byte[] bs = new byte[buffer.remaining()];
    buffer.get(bs);
    return bs;
  }
  
  
  @Override
  public int writeTo(ByteBuffer buf) {
    buffer.position(0).limit(length + Integer.BYTES);
    buf.put(buffer);
    return length + Integer.BYTES;
  }
  
  
  @Override
  public int writeTo(DynamicByteBuffer buf) {
    buffer.position(0).limit(length + Integer.BYTES);
    buf.put(buffer);
    return length + Integer.BYTES;
  }
  
  
  @Override
  public int writeTo(WritableByteChannel ch) throws IOException {
    buffer.position(0).limit(length + Integer.BYTES);
    return ch.write(buffer);
  }


  @Override
  public int compareTo(BinaryString o) {
    buffer.position(0).limit(Integer.BYTES + length);
    return buffer.compareTo(o.toByteBuffer());
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
    if (!BinaryString.class.isAssignableFrom(obj.getClass())) {
      return false;
    }
    final BinaryString other = (BinaryString) obj;
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
