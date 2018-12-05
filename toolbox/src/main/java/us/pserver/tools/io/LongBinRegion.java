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
import us.pserver.tools.Hash;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 05/12/2018
 */
public class LongBinRegion implements Region, BinaryForm {
  
  public static final int BYTES = Long.BYTES * 2;
  
  private final ByteBuffer buffer;
  
  
  public LongBinRegion() {
    buffer = ByteBuffer.allocate(BYTES);
  }
  
  public LongBinRegion(long offset, long length) {
    this();
    buffer.putLong(offset);
    buffer.putLong(length);
  }
  
  @Override
  public long offset() {
    buffer.position(0);
    return buffer.getLong();
  }

  @Override
  public long length() {
    buffer.position(Long.BYTES);
    return buffer.getLong();
  }

  @Override
  public long end() {
    return this.offset() + this.length();
  }

  @Override
  public int intOffset() {
    return (int) this.offset();
  }

  @Override
  public int intLength() {
    return (int) this.length();
  }

  @Override
  public int intEnd() {
    return this.intOffset() + this.intLength();
  }

  @Override
  public boolean isValid() {
    return this.offset() >= 0 && this.length() >= 1;
  }

  @Override
  public boolean contains(Region r) {
    return this.offset() <= r.offset() && this.end() >= r.end();
  }

  @Override
  public ByteBuffer toByteBuffer() {
    ByteBuffer buf = ByteBuffer.allocate(BYTES);
    this.writeTo(buf);
    buf.flip();
    return buf;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 89 * hash + (int) (this.offset() ^ (this.offset() >>> 32));
    hash = 89 * hash + (int) (this.length() ^ (this.length() >>> 32));
    return hash;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!Region.class.isAssignableFrom(obj.getClass())) {
      return false;
    }
    final Region other = (Region) obj;
    return this.offset() == other.offset() 
        && this.length() == other.length();
  }

  @Override
  public String toString() {
    return String.format("Region{offset=%d, length=%d}", offset(), length());
  }


  @Override
  public String sha256sum() {
    return Hash.sha256().of(buffer.array());
  }


  @Override
  public byte[] toByteArray() {
    return buffer.array();
  }


  @Override
  public int writeTo(ByteBuffer buf) {
    buffer.position(0);
    buf.put(buffer);
    return BYTES;
  }


  @Override
  public int writeTo(DynamicByteBuffer buf) {
    buffer.position(0);
    buf.put(buffer);
    return BYTES;
  }


  @Override
  public int writeTo(WritableByteChannel ch) throws IOException {
    return ch.write(toByteBuffer());
  }


  @Override
  public int readFrom(ReadableByteChannel ch) throws IOException {
    buffer.position(0);
    return ch.read(buffer);
  }


  @Override
  public int readFrom(ByteBuffer buf) {
    buffer.position(0);
    buf.put(buffer);
    return BYTES;
  }


  @Override
  public int readFrom(DynamicByteBuffer buf) {
    buffer.position(0);
    buf.put(buffer);
    return BYTES;
  }
    
}
