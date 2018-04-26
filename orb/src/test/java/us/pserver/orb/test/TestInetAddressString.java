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

package us.pserver.orb.test;

import java.net.InetAddress;
import java.net.UnknownHostException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import us.pserver.tools.fn.Rethrow;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 10/02/2018
 */
public class TestInetAddressString {
  
  private static final String IP_127_0_0_1 = "127.0.0.1";
  
  private static final InetAddress addr = Rethrow.unchecked().apply(()->InetAddress.getByName(IP_127_0_0_1));
  
  @Test
  public void addressToString() {
    System.out.printf("{ %s }.getHostAddress() -> %s%n", addr, addr.getHostAddress());
    Assertions.assertEquals(IP_127_0_0_1, addr.getHostAddress());
  }
  
  @Test
  public void stringToAddress() throws UnknownHostException {
    Assertions.assertEquals(addr, InetAddress.getByName(IP_127_0_0_1));
  }
  
}
