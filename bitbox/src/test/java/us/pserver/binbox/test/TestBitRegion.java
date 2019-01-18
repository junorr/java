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

package us.pserver.binbox.test;

import java.nio.ByteBuffer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import us.pserver.bitbox.BitRegion;
import us.pserver.bitbox.BitRegionFactory;
import us.pserver.bitbox.DefaultBitBoxConfiguration;
import us.pserver.tools.io.DynamicByteBuffer;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 12/12/2018
 */
public class TestBitRegion {
  
  public static final BitRegionFactory factory = new BitRegionFactory(new DefaultBitBoxConfiguration());

  @Test
  public void testCreateFromByteBuffer() {
    ByteBuffer buf = ByteBuffer.allocate(Integer.BYTES * 4);
    buf.putInt(BitRegion.ID);
    buf.putInt(Integer.BYTES * 4);
    buf.putInt(1024);
    buf.putInt(2048);
    buf.flip();
    BitRegion reg = factory.createFrom(buf);
    Assertions.assertEquals(BitRegion.ID, reg.boxID());
    Assertions.assertEquals(Integer.BYTES * 4, reg.boxSize());
    Assertions.assertEquals(1024, reg.offset());
    Assertions.assertEquals(2048, reg.length());
    Assertions.assertEquals(3072, reg.end());
  }
  
  @Test
  public void testCreateFromDynamicByteBuffer() {
    DynamicByteBuffer buf = new DynamicByteBuffer(Integer.BYTES * 10, true);
    buf.putInt(BitRegion.ID);
    buf.putInt(Integer.BYTES * 4);
    buf.putInt(1024);
    buf.putInt(2048);
    buf.flip();
    BitRegion reg = factory.createFrom(buf);
    Assertions.assertEquals(BitRegion.ID, reg.boxID());
    Assertions.assertEquals(Integer.BYTES * 4, reg.boxSize());
    Assertions.assertEquals(1024, reg.offset());
    Assertions.assertEquals(2048, reg.length());
    Assertions.assertEquals(3072, reg.end());
  }
  
}
