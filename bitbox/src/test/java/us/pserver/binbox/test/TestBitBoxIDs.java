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

import org.junit.jupiter.api.Test;
import us.pserver.bitbox.BitArray;
import us.pserver.bitbox.BitNumber;
import us.pserver.bitbox.BitRegion;
import us.pserver.bitbox.BitString;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 12/12/2018
 */
public class TestBitBoxIDs {

  @Test
  public void testBitBoxIDs() {
    System.out.printf("* BitRegion.ID=%d%n", BitRegion.ID);
    System.out.printf("* BitString.ID=%d%n", BitString.ID);
    System.out.printf("* BitArray.ID=%d%n", BitArray.ID);
    System.out.printf("* BitNumber.ID_BYTE=%d%n", BitNumber.ID_BYTE);
    System.out.printf("* BitNumber.ID_SHORT=%d%n", BitNumber.ID_SHORT);
    System.out.printf("* BitNumber.ID_INT=%d%n", BitNumber.ID_INT);
    System.out.printf("* BitNumber.ID_FLOAT=%d%n", BitNumber.ID_FLOAT);
    System.out.printf("* BitNumber.ID_LONG=%d%n", BitNumber.ID_LONG);
    System.out.printf("* BitNumber.ID_DOUBLE=%d%n", BitNumber.ID_DOUBLE);
  }
  
}
