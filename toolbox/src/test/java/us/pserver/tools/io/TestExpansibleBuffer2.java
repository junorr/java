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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import us.pserver.tools.log.Logger;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 22/10/2018
 */
public class TestExpansibleBuffer2 {

  @Test
  public void testReadLength() {
    try {
      Buffer buffer = Buffer.expansibleHeapFactory().create("Hello World");
      Assertions.assertEquals(11, buffer.readLength());
      Assertions.assertTrue(buffer.isReadable());
      Assertions.assertFalse(buffer.isWritable());
      buffer.clear();
      Logger.debug("after clear: %s", buffer);
      buffer.fillBuffer(Buffer.heapFactory().create("World Hello"));
      Logger.debug("after fill : %s", buffer);
      Assertions.assertEquals(11, buffer.readLength());
      Assertions.assertTrue(buffer.isReadable());
      Assertions.assertFalse(buffer.isWritable());
    } 
    catch(Exception e) {
      e.printStackTrace();
      throw e;
    }
  }
  
}
