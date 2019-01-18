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
import us.pserver.bitbox.BitBoxConfiguration.BufferAlloc;
import us.pserver.tools.io.DynamicByteBuffer;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 11/12/2018
 */
public class BitArrayFactory<T extends BitBox, V> extends AbstractBitBoxFactory<BitArray<T>,V[]> {
  
  private final BitBoxFactory factory;
  
  public BitArrayFactory(BitBoxFactory factory) {
    super(factory.configure());
    this.factory = factory;
  }
  
  @Override
  public BitArray<T> createFrom(V[] vals) {
    DynamicByteBuffer buf = new DynamicByteBuffer(1024, config.getBufferAlloc() == BufferAlloc.DIRECT_ALLOC);
    buf.putInt(BitArray.ID);
    buf.putInt(BitArray.HEADER_BYTES);
    buf.putInt(vals.length);
    for(V val : vals) {
      factory.createFrom(val).writeTo(buf);
    }
    buf.flip();
    int len = buf.remaining();
    buf.position(Integer.BYTES)
        .putInt(len)
        .position(0);
    return new BitArray.BArray(buf.slice().toByteBuffer());
  }
  
  @Override
  public BitArray<T> createFrom(ByteBuffer buf) {
    int pos = buf.position();
    int lim = buf.limit();
    if(buf.getInt() != BitArray.ID) {
      throw new IllegalArgumentException("Not a BitArray content");
    }
    int len = buf.getInt();
    buf.position(pos).limit(pos + len);
    BitArray.BArray array = new BitArray.BArray(buf.slice());
    buf.limit(lim);
    return array;
  }

  @Override
  public BitArray<T> createFrom(ReadableByteChannel ch) throws IOException {
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
    return new BitArray.BArray(buf);
  }

  @Override
  public BitArray<T> createFrom(DynamicByteBuffer buf) {
    return createFrom(buf.toByteBuffer());
  }

}
