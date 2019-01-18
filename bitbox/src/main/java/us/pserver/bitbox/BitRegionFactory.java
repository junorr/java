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
import us.pserver.bitbox.util.Region;
import us.pserver.tools.io.DynamicByteBuffer;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 11/12/2018
 */
public class BitRegionFactory extends AbstractBitBoxFactory<BitRegion,Region> {
  
  public BitRegionFactory(BitBoxConfiguration conf) {
    super(conf);
  }
  
  @Override
  public BitRegion createFrom(Region reg) {
    return createFrom(reg.offset(), reg.length());
  }
  
  public BitRegion createFrom(int offset, int length) {
    ByteBuffer buf = ByteBuffer.allocate(BitRegion.BYTES);
    buf.putInt(BitRegion.ID);
    buf.putInt(BitRegion.BYTES);
    buf.putInt(offset);
    buf.putInt(length);
    buf.flip();
    return new BitRegion(buf);
  }

  @Override
  public BitRegion createFrom(ByteBuffer buf) {
    int lim = buf.limit();
    buf.limit(buf.position() + BitRegion.BYTES);
    BitRegion reg = new BitRegion(buf.slice());
    buf.limit(lim);
    return reg;
  }

  @Override
  public BitRegion createFrom(ReadableByteChannel ch) throws IOException {
    ByteBuffer buf = ByteBuffer.allocate(BitRegion.BYTES);
    ch.read(buf);
    buf.flip();
    return new BitRegion(buf);
  }

  @Override
  public BitRegion createFrom(DynamicByteBuffer buf) {
    return createFrom(buf.toByteBuffer());
  }

}
