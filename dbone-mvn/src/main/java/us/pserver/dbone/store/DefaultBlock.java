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

package us.pserver.dbone.store;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;
import java.util.Objects;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 02/05/2018
 */
public class DefaultBlock implements Block {
  
  protected final Region region;
  
  protected final Region next;
  
  protected final ByteBuffer buf;
  
  protected final Type type;
  
  protected final int size;
  
  
  public DefaultBlock(Type type, Region reg, ByteBuffer buf, Region next) {
    this.type = Objects.requireNonNull(type, "Bad null Block Type");
    this.region = Objects.requireNonNull(reg, "Bad null Region");
    this.buf = Objects.requireNonNull(buf, "Bad null ByteBuffer");
    this.next = Objects.requireNonNull(next, "Bad null next Region");
    this.size = buf.remaining() + META_BYTES;
  }
  
  
  @Override
  public Region region() {
    return region;
  }
  
  
  @Override
  public Block withRegion(Region reg) {
    return new DefaultBlock(type, reg, buf, next);
  }
  

  @Override
  public Region nextRegion() {
    return next;
  }
  
  
  @Override
  public Block withNext(Region next) {
    return new DefaultBlock(type, region, buf, next);
  }


  @Override
  public ByteBuffer buffer() {
    return buf;
  }
  
  
  @Override
  public Block withBuffer(ByteBuffer buf) {
    return new DefaultBlock(type, region, buf, next);
  }


  @Override
  public int size() {
    return size;
  }
  
  
  @Override
  public Block asRoot() {
    return new DefaultBlock(Type.ROOT, region, buf, next);
  }
  
  
  @Override
  public Block asNode() {
    return new DefaultBlock(Type.NODE, region, buf, next);
  }
  
  
  @Override
  public int writeTo(WritableByteChannel ch) throws IOException {
    ch.write(toByteBuffer());
    return size;
  }


  @Override
  public int writeTo(ByteBuffer wb) {
    type.writeTo(wb);
    wb.putInt(buf.remaining());
    int pos = buf.position();
    wb.put(buf);
    buf.position(pos);
    wb.position(region.intLength() - Region.BYTES);
    next.writeTo(wb);
    return size;
  }


  @Override
  public ByteBuffer toByteBuffer() {
    ByteBuffer total = ByteBuffer.allocate(region.intLength());
    writeTo(total);
    total.flip();
    return total;
  }


  @Override
  public boolean isRoot() {
    return type == Type.ROOT;
  }


  @Override
  public boolean isNode() {
    return type == Type.NODE;
  }


  @Override
  public int hashCode() {
    int hash = 5;
    hash = 97 * hash + Objects.hashCode(this.region);
    hash = 97 * hash + Objects.hashCode(this.next);
    hash = 97 * hash + Objects.hashCode(this.type);
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
    if (getClass() != obj.getClass()) {
      return false;
    }
    final DefaultBlock other = (DefaultBlock) obj;
    if (!Objects.equals(this.region, other.region)) {
      return false;
    }
    if (!Objects.equals(this.next, other.next)) {
      return false;
    }
    if (this.type != other.type) {
      return false;
    }
    return true;
  }


  @Override
  public String toString() {
    return "Block{type=" + type + ", region=" + region + ", next=" + next + '}';
  }
  
}
