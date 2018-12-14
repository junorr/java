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

package us.pserver.bitbox.array;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import us.pserver.bitbox.BitBoxFactory;
import us.pserver.tools.io.DynamicByteBuffer;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 11/12/2018
 */
public class BitByteArrayFactory implements BitBoxFactory<BitByteArray> {
  
  private static final BitByteArrayFactory _instance = new BitByteArrayFactory();
  
  private BitByteArrayFactory() {}
  
  public static BitByteArrayFactory get() {
    return _instance;
  }
  
  public BitByteArray createFrom(byte[] bytes) {
    ByteBuffer buf = ByteBuffer.allocate(Integer.BYTES * 3 + Byte.BYTES * bytes.length);
    buf.putInt(BitByteArray.ID);
    buf.putInt(buf.capacity());
    buf.putInt(bytes.length);
    for(byte i : bytes) {
      buf.put(i);
    }
    buf.flip();
    return new BitByteArray.ByteArray(buf);
  }

  @Override
  public BitByteArray createFrom(ByteBuffer buf) {
    int pos = buf.position();
    int lim = buf.limit();
    int id = buf.getInt();
    if(id != BitIntArray.ID) {
      throw new IllegalArgumentException("Not a BitByteArray content");
    }
    int len = buf.getInt();
    buf.position(pos).limit(pos + len);
    BitByteArray array = new BitByteArray.ByteArray(buf.slice());
    buf.limit(lim);
    return array;
  }

  @Override
  public BitByteArray createFrom(ReadableByteChannel ch) throws IOException {
    ByteBuffer buf = ByteBuffer.allocate(Integer.BYTES * 2);
    ch.read(buf);
    buf.flip();
    int id = buf.getInt();
    int len = buf.getInt();
    buf = ByteBuffer.allocate(len);
    buf.putInt(id);
    buf.putInt(len);
    ch.read(buf);
    buf.flip();
    return new BitByteArray.ByteArray(buf);
  }

  @Override
  public BitByteArray createFrom(DynamicByteBuffer buf) {
    return createFrom(buf.toByteBuffer());
  }

}
