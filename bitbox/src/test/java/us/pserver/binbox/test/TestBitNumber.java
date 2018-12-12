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
import us.pserver.bitbox.BitNumber;
import us.pserver.bitbox.BitRegion;
import us.pserver.tools.io.DynamicByteBuffer;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 12/12/2018
 */
public class TestBitNumber {

  @Test
  public void testCreateByteFromByteBuffer() {
    ByteBuffer buf = ByteBuffer.allocate(Integer.BYTES * 10);
    buf.putInt(BitNumber.ID_BYTE);
    buf.putInt(Integer.BYTES * 2 + Byte.BYTES);
    buf.put((byte)5);
    buf.flip();
    BitNumber num = BitNumber.factory().createFrom(buf);
    Assertions.assertEquals(BitNumber.ID_BYTE, num.boxID());
    Assertions.assertEquals(Integer.BYTES * 2 + Byte.BYTES, num.boxSize());
    Assertions.assertEquals(Byte.valueOf((byte)5), num.get());
    Assertions.assertEquals((byte)5, num.getByte());
  }
  
  @Test
  public void testCreateByteFromDynamicByteBuffer() {
    DynamicByteBuffer buf = new DynamicByteBuffer(Integer.BYTES * 10, true);
    buf.putInt(BitNumber.ID_BYTE);
    buf.putInt(Integer.BYTES * 2 + Byte.BYTES);
    buf.put((byte)5);
    buf.flip();
    BitNumber num = BitNumber.factory().createFrom(buf);
    Assertions.assertEquals(BitNumber.ID_BYTE, num.boxID());
    Assertions.assertEquals(Integer.BYTES * 2 + Byte.BYTES, num.boxSize());
    Assertions.assertEquals(Byte.valueOf((byte)5), num.get());
    Assertions.assertEquals((byte)5, num.getByte());
  }
  
  @Test
  public void testCreateShortFromByteBuffer() {
    ByteBuffer buf = ByteBuffer.allocate(Integer.BYTES * 10);
    buf.putInt(BitNumber.ID_SHORT);
    buf.putInt(Integer.BYTES * 2 + Short.BYTES);
    buf.putShort((short)5);
    buf.flip();
    BitNumber num = BitNumber.factory().createFrom(buf);
    Assertions.assertEquals(BitNumber.ID_SHORT, num.boxID());
    Assertions.assertEquals(Integer.BYTES * 2 + Short.BYTES, num.boxSize());
    Assertions.assertEquals(Short.valueOf((short)5), num.get());
    Assertions.assertEquals((short)5, num.getShort());
  }
  
  @Test
  public void testCreateIntFromByteBuffer() {
    ByteBuffer buf = ByteBuffer.allocate(Integer.BYTES * 10);
    buf.putInt(BitNumber.ID_INT);
    buf.putInt(Integer.BYTES * 2 + Integer.BYTES);
    buf.putInt(5);
    buf.flip();
    BitNumber num = BitNumber.factory().createFrom(buf);
    Assertions.assertEquals(BitNumber.ID_INT, num.boxID());
    Assertions.assertEquals(Integer.BYTES * 2 + Integer.BYTES, num.boxSize());
    Assertions.assertEquals(Integer.valueOf(5), num.get());
    Assertions.assertEquals(5, num.getInt());
  }
  
  @Test
  public void testCreateFloatFromByteBuffer() {
    ByteBuffer buf = ByteBuffer.allocate(Integer.BYTES * 10);
    buf.putInt(BitNumber.ID_FLOAT);
    buf.putInt(Integer.BYTES * 2 + Float.BYTES);
    buf.putFloat((float)5.0);
    buf.flip();
    BitNumber num = BitNumber.factory().createFrom(buf);
    Assertions.assertEquals(BitNumber.ID_FLOAT, num.boxID());
    Assertions.assertEquals(Integer.BYTES * 2 + Float.BYTES, num.boxSize());
    Assertions.assertEquals(Float.valueOf((float)5.0), num.get());
    Assertions.assertEquals((float)5.0, num.getFloat());
  }
  
  @Test
  public void testCreateLongFromByteBuffer() {
    ByteBuffer buf = ByteBuffer.allocate(Integer.BYTES * 10);
    buf.putInt(BitNumber.ID_LONG);
    buf.putInt(Integer.BYTES * 2 + Long.BYTES);
    buf.putLong((long)5);
    buf.flip();
    BitNumber num = BitNumber.factory().createFrom(buf);
    Assertions.assertEquals(BitNumber.ID_LONG, num.boxID());
    Assertions.assertEquals(Integer.BYTES * 2 + Long.BYTES, num.boxSize());
    Assertions.assertEquals(Long.valueOf((long)5), num.get());
    Assertions.assertEquals((long)5, num.getLong());
  }
  
  @Test
  public void testCreateDoubleFromByteBuffer() {
    ByteBuffer buf = ByteBuffer.allocate(Integer.BYTES * 10);
    buf.putInt(BitNumber.ID_DOUBLE);
    buf.putInt(Integer.BYTES * 2 + Double.BYTES);
    buf.putDouble((double)5.0);
    buf.flip();
    BitNumber num = BitNumber.factory().createFrom(buf);
    Assertions.assertEquals(BitNumber.ID_DOUBLE, num.boxID());
    Assertions.assertEquals(Integer.BYTES * 2 + Double.BYTES, num.boxSize());
    Assertions.assertEquals(Double.valueOf((double)5.0), num.get());
    Assertions.assertEquals((double)5, num.getDouble());
  }
  
}
