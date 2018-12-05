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

package us.pserver.tools.io;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import us.pserver.tools.Hash;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 05/12/2018
 */
public class TestBinString {

  @Test
  public void testLength() {
    String msg = "Hello World!";
    BinString str = BinString.of(msg);
    Assertions.assertEquals(msg.length(), str.length());
  }
  
  @Test
  public void testToByteBuffer() {
    String msg = "Hello World!";
    BinString str = BinString.of(msg);
    ByteBuffer buf = str.toByteBuffer();
    Assertions.assertEquals(msg.length() + Integer.BYTES, buf.remaining());
  }
  
  @Test
  public void testToByteArray() {
    String msg = "Hello World!";
    BinString str = BinString.of(msg);
    byte[] bs = str.toByteArray();
    Assertions.assertEquals(msg.length() + Integer.BYTES, bs.length);
  }
  
  @Test
  public void testGetContentBuffer() {
    String msg = "Hello World!";
    BinString str = BinString.of(msg);
    ByteBuffer buf = str.getContentBuffer();
    Assertions.assertEquals(msg.length(), buf.remaining());
  }
  
  @Test
  public void testGetContentBytes() {
    String msg = "Hello World!";
    BinString str = BinString.of(msg);
    byte[] bs = str.getContentBytes();
    Assertions.assertEquals(msg.length(), bs.length);
  }
  
  @Test
  public void testAppendString() {
    String msg1 = "Hello ";
    String msg2 = "World!";
    BinString str = BinString.of(msg1);
    str.append(msg2);
    Assertions.assertEquals(msg1.length() + msg2.length(), str.length());
    Assertions.assertEquals(msg1.length() + msg2.length(), str.getContentBuffer().remaining());
  }
  
  @Test
  public void testAppendBinString() {
    String msg1 = "Hello ";
    String msg2 = "World!";
    BinString str = BinString.of(msg1);
    BinString str2 = BinString.of(msg2);
    str.append(str2);
    Assertions.assertEquals(msg1.length() + msg2.length(), str.length());
    Assertions.assertEquals(msg1.length() + msg2.length(), str.getContentBuffer().remaining());
  }
  
  @Test
  public void testIndexOfString() {
    String msg1 = "Hello ";
    String msg2 = "World!";
    BinString str = BinString.of(msg1);
    str.append(msg2);
    Assertions.assertEquals(msg1.length(), str.indexOf(msg2, 0));
  }
  
  @Test
  public void testIndexOfBinString() {
    String msg1 = "Hello ";
    String msg2 = "World!";
    BinString str = BinString.of(msg1);
    BinString str2 = BinString.of(msg2);
    str.append(str2);
    Assertions.assertEquals(msg1.length(), str.indexOf(str2, 0));
  }
  
  @Test
  public void testContainsString() {
    String msg1 = "Hello ";
    String msg2 = "World!";
    BinString str = BinString.of(msg1);
    str.append(msg2);
    Assertions.assertTrue(str.contains(msg2));
  }
  
  @Test
  public void testContainsBinString() {
    String msg1 = "Hello ";
    String msg2 = "World!";
    BinString str = BinString.of(msg1);
    BinString str2 = BinString.of(msg2);
    str.append(str2);
    Assertions.assertTrue(str.contains(str2));
  }
  
  @Test
  public void testSliceOffset() {
    String msg1 = "Hello ";
    String msg2 = "World!";
    BinString str = BinString.of(msg1);
    str.append(msg2);
    Assertions.assertEquals(msg2, str.slice(6).toString());
    Assertions.assertEquals(msg2.length(), str.slice(6).length());
  }
  
  @Test
  public void testSliceOffsetLength() {
    String msg1 = "Hello ";
    String msg2 = "World!";
    BinString str = BinString.of(msg1);
    str.append(msg2);
    Assertions.assertEquals(msg2, str.slice(6, 6).toString());
    Assertions.assertEquals(msg2.length(), str.slice(6, 6).length());
  }
  
  @Test
  public void testCompareTo() {
    String msg1 = "Hello";
    String msg2 = "Hello";
    BinString str = BinString.of(msg1);
    BinString str2 = BinString.of(msg2);
    Assertions.assertEquals(0, str.compareTo(str2));
  }
  
  @Test
  public void testSha256Sum() {
    String msg = "Hello World!";
    BinString str = BinString.of(msg);
    DynamicByteBuffer buf = new DynamicByteBuffer(Integer.BYTES + msg.length(), false)
        .putInt(msg.length())
        .putUTF8(msg)
        .position(0)
        .limit(Integer.BYTES + msg.length());
    Assertions.assertEquals(buf.sha256sum(), str.sha256sum());
  }
  
  @Test
  public void testWriteToByteBuffer() {
    String msg = "Hello World!";
    BinString str = BinString.of(msg);
    ByteBuffer buf = ByteBuffer.allocate(Integer.BYTES + msg.length());
    str.writeTo(buf);
    buf.flip();
    Assertions.assertEquals(msg.length() + Integer.BYTES, buf.remaining());
    Assertions.assertEquals(msg.length(), buf.getInt());
    Assertions.assertEquals(msg, StandardCharsets.UTF_8.decode(buf).toString());
  }
  
  @Test
  public void testWriteToDynamicByteBuffer() {
    String msg = "Hello World!";
    BinString str = BinString.of(msg);
    DynamicByteBuffer buf = new DynamicByteBuffer(Integer.BYTES + msg.length(), false);
    str.writeTo(buf);
    buf.flip();
    Assertions.assertEquals(msg.length() + Integer.BYTES, buf.remaining());
    Assertions.assertEquals(msg.length(), buf.getInt());
    Assertions.assertEquals(msg, StandardCharsets.UTF_8.decode(buf.toByteBuffer()).toString());
  }
  
  @Test
  public void testreadFromByteBuffer() {
    String msg = "Hello World!";
    ByteBuffer buf = ByteBuffer.allocate(Integer.BYTES + msg.length());
    System.out.printf("* hashCode empty = %d%n", buf.hashCode());
    buf.putInt(msg.length());
    buf.put(msg.getBytes(StandardCharsets.UTF_8));
    System.out.printf("* hashCode full = %d%n", buf.hashCode());
    buf.flip();
    System.out.printf("* hashCode flip = %d%n", buf.hashCode());
    BinString str = BinString.empty();
    str.readFrom(buf);
    buf.flip();
    Assertions.assertEquals(str.length(), buf.remaining() - Integer.BYTES);
    Assertions.assertEquals(msg.length(), str.length());
    Assertions.assertEquals(msg, str.toString());
    Assertions.assertTrue(str.contains(msg));
  }
  
  @Test
  public void testreadFromDynamicByteBuffer() {
    String msg = "Hello World!";
    DynamicByteBuffer buf = new DynamicByteBuffer(Integer.BYTES + msg.length(), false);
    buf.putInt(msg.length());
    buf.putUTF8(msg);
    buf.flip();
    System.out.println(buf);
    Assertions.assertEquals(msg.length() + Integer.BYTES, buf.remaining());
    BinString str = BinString.empty();
    str.readFrom(buf);
    buf.position(0).limit(Integer.BYTES + msg.length());
    Assertions.assertEquals(str.length(), buf.remaining() - Integer.BYTES);
    Assertions.assertEquals(msg.length(), str.length());
    Assertions.assertEquals(msg, str.toString());
    Assertions.assertTrue(str.contains(msg));
  }
  
}
