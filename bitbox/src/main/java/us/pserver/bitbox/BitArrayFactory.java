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
import java.nio.channels.ReadableByteChannel;
import static us.pserver.bitbox.BitArray.ID;
import us.pserver.tools.io.DynamicByteBuffer;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 11/12/2018
 */
public class BitArrayFactory implements BitBoxFactory<BitArray> {
  
  private static final BitArrayFactory _instance = new BitArrayFactory();
  
  private BitArrayFactory() {}
  
  public static BitArrayFactory get() {
    return _instance;
  }
  
  public BitRegion createFrom(int offset, int length) {
    return new BitRegion.Region(offset, length);
  }

  @Override
  public BitArray createFrom(ByteBuffer buf) {
    int pos = buf.position();
    int lim = buf.limit();
    if(buf.getInt() != BitArray.ID) {
      throw new IllegalArgumentException("Not a BinaryArray content");
    }
    int len = buf.getInt();
    buf.position(pos).limit(pos + len);
    BitArray.Array array = new BitArray.Array(buf.slice());
    buf.limit(lim);
    return array;
  }

  @Override
  public BitArray createFrom(ReadableByteChannel ch) throws IOException {
    ByteBuffer idlen = ByteBuffer.allocate(Integer.BYTES * 2);
    ch.read(idlen);
    idlen.flip();
    int id = idlen.getInt();
    if(id != ID) {
      throw new IllegalArgumentException("Not a BinaryArray content");
    }
    int len = idlen.getInt();
    ByteBuffer buf = ByteBuffer.allocate(len);
    buf.putInt(id);
    buf.putInt(len);
    ch.read(buf);
    buf.flip();
    return new BitArray.Array(buf);
  }

  @Override
  public BitArray createFrom(DynamicByteBuffer buf) {
    return createFrom(buf.toByteBuffer());
  }

}
