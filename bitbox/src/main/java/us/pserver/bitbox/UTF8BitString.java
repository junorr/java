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

package us.pserver.bitbox;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 05/12/2018
 */
public class UTF8BitString extends AbstractBitBox implements BitString {
  
  private final int length;
  
  
  public UTF8BitString(ByteBuffer buf) {
    super(buf);
    int id = buffer.getInt();
    if(id != BitString.ID) {
      throw new IllegalArgumentException("Not a BitString content");
    }
    this.length = buf.getInt() - BitBox.HEADER_BYTES;
  }
  
  
  public UTF8BitString(int length, ByteBuffer content) {
    super(content.isDirect() 
        ? ByteBuffer.allocateDirect(length + BitBox.HEADER_BYTES) 
        : ByteBuffer.allocate(length + BitBox.HEADER_BYTES));
    this.length = length;
    buffer.putInt(ID);
    buffer.putInt(length + BitBox.HEADER_BYTES);
    buffer.put(content);
  }
  
  
  @Override
  public String get() {
    return toString();
  }
  
  
  @Override
  public ByteBuffer getContentBuffer() {
    buffer.position(BitBox.HEADER_BYTES);
    buffer.limit(BitBox.HEADER_BYTES + length);
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
      buffer.position(BitBox.HEADER_BYTES + start + readed);
      int count = 0;
      for(int i = 0; i < len; i++) {
        if(buffer.get() == bs.get()) count++;
      }
      bs.position(0);
      if(count == len) {
        return buffer.position() - len - BitBox.HEADER_BYTES;
      }
      readed++;
    }
    return -1;
  }
  
  
  @Override
  public int indexOf(BitString str, int start) {
    ByteBuffer bs = str.getContentBuffer();
    int pos = bs.position();
    int len = bs.remaining();
    int readed = 0;
    int readlen = length;
    while((readed + len) <= readlen) {
      buffer.position(BitBox.HEADER_BYTES + start + readed);
      int count = 0;
      for(int i = 0; i < len; i++) {
        if(buffer.get() == bs.get()) count++;
      }
      bs.position(pos);
      if(count == len) {
        return buffer.position() - len - BitBox.HEADER_BYTES;
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
  public boolean contains(BitString str) {
    return indexOf(str, 0) >= 0;
  }
  
  
  @Override
  public int length() {
    return length;
  }
  
  
  @Override
  public BitString slice(int offset, int length) {
    if(offset < 0 || length < 1 || offset + length > this.length) {
      throw new IllegalArgumentException(String.format("Bad offset/length: %d/%d (maxLength=%d)", offset, length, this.length));
    }
    buffer.position(BitBox.HEADER_BYTES + offset)
        .limit(BitBox.HEADER_BYTES + offset + length);
    return new UTF8BitString(length, buffer.slice());
  }
  
  
  @Override
  public BitString slice(int offset) {
    if(offset < 0) {
      throw new IllegalArgumentException(String.format("Bad offset: %d", offset));
    }
    buffer.position(BitBox.HEADER_BYTES + offset)
        .limit(BitBox.HEADER_BYTES + length);
    return new UTF8BitString(length - offset, buffer.slice());
  }
  
  
  @Override
  public UTF8BitString toUpperCase() {
    return new UTF8BitString(length(), StandardCharsets.UTF_8.encode(toString().toUpperCase()));
  }
  
  
  @Override
  public UTF8BitString toLowerCase() {
    return new UTF8BitString(length(), StandardCharsets.UTF_8.encode(toString().toLowerCase()));
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
    if (!BitString.class.isAssignableFrom(obj.getClass())) {
      return false;
    }
    final BitString other = (BitString) obj;
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
