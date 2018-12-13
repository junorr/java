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
import java.nio.charset.StandardCharsets;
import java.util.function.IntConsumer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import us.pserver.bitbox.BitStringFactory;
import us.pserver.bitbox.BitString;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 11/12/2018
 */
public class TestBinaryString {

  @Test
  public void testCreateFromString() {
    //            1....6....12
    String str = "Hello World!";
    BitString bin = BitStringFactory.get().createFrom(str);
    Assertions.assertEquals(str.length(), bin.length());
    Assertions.assertEquals(str, bin.toString());
  }
  
  @Test
  public void testCreateFromByteBuffer() {
    try {
    //            1....6....12
    String str = "Hello World!";
    ByteBuffer buf = ByteBuffer.allocate(str.length() + Integer.BYTES * 2);
    buf.putInt(BitString.ID);
    buf.putInt(str.length() + Integer.BYTES * 2);
    buf.put(StandardCharsets.UTF_8.encode(str));
    buf.flip();
    BitString bin = BitString.factory().createFrom(buf);
    Assertions.assertEquals(str.length(), bin.length());
    Assertions.assertEquals(str, bin.toString());
    } catch(Exception e) {
      e.printStackTrace();
      throw e;
    }
  }
  
  @Test
  public void testGetContentBuffer() {
    //            1....6....12
    String str = "Hello World!";
    BitString bin = BitStringFactory.get().createFrom(str);
    Assertions.assertEquals(str.length(), bin.length());
    Assertions.assertEquals(str, bin.toString());
    Assertions.assertEquals(StandardCharsets.UTF_8.encode(str), bin.getContentBuffer());
  }
  
  @Test
  public void testGetContentBytes() {
    //            1....6....12
    String str = "Hello World!";
    BitString bin = BitStringFactory.get().createFrom(str);
    Assertions.assertEquals(str.length(), bin.length());
    Assertions.assertEquals(str, bin.toString());
    Assertions.assertArrayEquals(str.getBytes(StandardCharsets.UTF_8), bin.getContentBytes());
  }
  
  @Test
  public void testIndexOfString() {
    //            1...5...10...15...20...25...30...35...40......48
    String str = "Hello ABC World Hello CDE World Hello FGH World!";
    BitString bin = BitStringFactory.get().createFrom(str);
    Assertions.assertEquals(str.length(), bin.length());
    Assertions.assertEquals(str, bin.toString());
    String world = "World";
    int idx = bin.indexOf(world, 0);
    Assertions.assertEquals(10, idx);
    idx = bin.indexOf(world, idx + 1);
    Assertions.assertEquals(26, idx);
    idx = bin.indexOf(world, idx + 1);
    Assertions.assertEquals(42, idx);
  }
  
  @Test
  public void testIndexOfBinaryString() {
    //            1...5...10...15...20...25...30...35...40......48
    String str = "Hello ABC World Hello CDE World Hello FGH World!";
    BitString bin = BitStringFactory.get().createFrom(str);
    Assertions.assertEquals(str.length(), bin.length());
    Assertions.assertEquals(str, bin.toString());
    BitString world = BitStringFactory.get().createFrom("World");
    int idx = bin.indexOf(world, 0);
    Assertions.assertEquals(10, idx);
    idx = bin.indexOf(world, idx + 1);
    Assertions.assertEquals(26, idx);
    idx = bin.indexOf(world, idx + 1);
    Assertions.assertEquals(42, idx);
  }
  
  @Test
  public void testSliceOneParameter() {
    //            1...5...10...15...20...25...30...35...40......48
    String str = "Hello ABC World Hello CDE World Hello FGH World!";
    BitString bin = BitStringFactory.get().createFrom(str);
    Assertions.assertEquals(str.length(), bin.length());
    Assertions.assertEquals(str, bin.toString());
    Assertions.assertEquals(str.substring(32), bin.slice(32).toString());
  }
  
  @Test
  public void testSliceTwoParameters() {
    //            1...5...10...15...20...25...30...35...40......48
    String str = "Hello ABC World Hello CDE World Hello FGH World!";
    BitString bin = BitStringFactory.get().createFrom(str);
    Assertions.assertEquals(str.length(), bin.length());
    Assertions.assertEquals(str, bin.toString());
    Assertions.assertEquals(str.substring(6, 25), bin.slice(6, 19).toString());
  }
  
  @Test
  public void testToUpperCase() {
    //            1....6....12
    String str = "Hello World!";
    BitString bin = BitStringFactory.get().createFrom(str);
    Assertions.assertEquals(str.length(), bin.length());
    Assertions.assertEquals(str.toUpperCase(), bin.toUpperCase().toString());
  }
  
  @Test
  public void testToLowerCase() {
    //            1....6....12
    String str = "Hello World!";
    BitString bin = BitStringFactory.get().createFrom(str);
    Assertions.assertEquals(str.length(), bin.length());
    Assertions.assertEquals(str.toLowerCase(), bin.toLowerCase().toString());
  }
  
}
