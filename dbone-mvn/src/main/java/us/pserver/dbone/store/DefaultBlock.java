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
  
  protected final Region next;
  
  protected final ByteBuffer buf;
  
  protected final Type type;
  
  protected final int length;
  
  
  public DefaultBlock(Type type, ByteBuffer buf, Region reg) {
    this.type = Objects.requireNonNull(type, "Bad null Block Type");
    this.buf = Objects.requireNonNull(buf, "Bad null ByteBuffer");
    this.next = Objects.requireNonNull(reg, "Bad null Region");
    this.length = buf.remaining() + META_BYTES;
  }
  

  @Override
  public Region getNextRegion() {
    return next;
  }


  @Override
  public ByteBuffer getBuffer() {
    return buf;
  }


  @Override
  public int length() {
    return length;
  }


  @Override
  public int writeTo(WritableByteChannel ch) throws IOException {
    ByteBuffer wb = ByteBuffer.allocate(length);
    wb.put(Integer.valueOf(type.ordinal()).byteValue());
    wb.putInt(buf.remaining());
    wb.put(buf);
    next.writeTo(wb);
    wb.flip();
    ch.write(wb);
    return length;
  }


  @Override
  public int writeTo(ByteBuffer wb) {
    wb.put(Integer.valueOf(type.ordinal()).byteValue());
    wb.putInt(buf.remaining());
    wb.put(buf);
    next.writeTo(wb);
    wb.flip();
    return length;
  }


  @Override
  public ByteBuffer toByteBuffer() {
    ByteBuffer total = ByteBuffer.allocate(length);
    total.put(type.toByteBuffer());
    total.put(buf);
    total.put(next.toByteBuffer());
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

}
