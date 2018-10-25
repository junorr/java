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
import us.pserver.tools.UTF8String;
import us.pserver.tools.log.Logger;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 22/10/2018
 */
public class TestExpansibleBuffer2 {
  
  
  //                                            1...5...10...15...20...25
  private final byte[] bytes = UTF8String.from("Hello World dlroW olleH").getBytes();
  private final Buffer buffer = Buffer.expansibleHeapFactory().create(50);
  
  
  private void resetBuffer() {
    buffer.clear().fillBuffer(bytes);
  }
  

  @Test
  public void testReadLength() {
    try {
      resetBuffer();
      Assertions.assertEquals(bytes.length, buffer.readLength());
      Assertions.assertTrue(buffer.isReadable());
      Assertions.assertTrue(buffer.isWritable());
      buffer.clear();
      Logger.debug("after clear: %s", buffer);
      buffer.fillBuffer(Buffer.heapFactory().create(bytes));
      Logger.debug("after fill : %s", buffer);
      Assertions.assertEquals(bytes.length, buffer.readLength());
      Assertions.assertTrue(buffer.isReadable());
      Assertions.assertTrue(buffer.isWritable());
    } 
    catch(Exception e) {
      Logger.error(e);
      throw e;
    }
  }
  
  @Test
  public void testWriteToByteArray() {
    try {
      resetBuffer();
      byte[] out = new byte[50];
      int read = buffer.writeTo(out);
      Logger.debug("buffer.writeTo( byte[] ): bytes=%d", read);
      Assertions.assertEquals(UTF8String.from(bytes).toString(), UTF8String.from(out, 0, read).toString());
    } 
    catch(Exception e) {
      Logger.error(e);
      throw e;
    }
  }
  
  @Test
  public void testWriteToByteBuffer() {
    try {
      resetBuffer();
      Logger.debug("buffer.getContentAsString(): '%s'", buffer.getContentAsString());
      ByteBuffer out = ByteBuffer.allocateDirect(50);
      int read = buffer.writeTo(out);
      out.flip();
      Logger.debug("buffer.writeTo( byte[] ): bytes=%d", read);
      Assertions.assertEquals(UTF8String.from(bytes).toString(), UTF8String.from(out).toString());
    } 
    catch(Exception e) {
      Logger.error(e);
      throw e;
    }
  }
  
}
