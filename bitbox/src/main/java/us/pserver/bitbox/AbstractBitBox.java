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

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;
import us.pserver.tools.Hash;
import us.pserver.tools.io.DynamicByteBuffer;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 12/12/2018
 */
public abstract class AbstractBitBox implements BitBox {
  
  protected final ByteBuffer buffer;
  
  public AbstractBitBox(ByteBuffer buf) {
    this.buffer = buf;
  }

  @Override
  public int boxID() {
    buffer.position(0);
    return buffer.getInt();
  }

  @Override
  public int boxSize() {
    buffer.position(Integer.BYTES);
    return buffer.getInt();
  }
  
  @Override
  public String sha256sum() {
    return Hash.sha256().of(toByteArray());
  }

  @Override
  public ByteBuffer toByteBuffer() {
    buffer.position(0);
    return buffer.duplicate();
  }

  @Override
  public byte[] toByteArray() {
    int size = boxSize();
    if(buffer.hasArray() && buffer.array().length == size) {
      return buffer.array();
    }
    byte[] bs = new byte[size];
    buffer.position(0);
    buffer.get(bs);
    return bs;
  }

  @Override
  public int writeTo(ByteBuffer buf) {
    buffer.position(0);
    buf.put(buffer);
    return boxSize();
  }

  @Override
  public int writeTo(WritableByteChannel ch) throws IOException {
    buffer.position(0);
    return ch.write(buffer);
  }

  @Override
  public int writeTo(DynamicByteBuffer buf) {
    buffer.position(0);
    buf.put(buffer);
    return boxSize();
  }

  @Override
  public String toString() {
    return String.format("BitBox{id=%d, size=%d}", boxID(), boxSize());
  }

}
