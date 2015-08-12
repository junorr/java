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

package us.pserver.xprops.transformer;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import us.pserver.tools.Valid;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 12/07/2015
 */
public class SocketAddressTransformer extends AbstractStringTransformer<SocketAddress> {

  @Override
  public SocketAddress fromString(String str) throws IllegalArgumentException {
    final String msg = "Invalid String to Transform: ";
    Valid.off(str).forEmpty().fail(msg)
        .forTest(!str.contains(":")).fail(msg);
    int id = str.indexOf(":");
    Valid.off(str).forTest(id >= str.length()).fail(msg);
    NumberTransformer tn = new NumberTransformer();
    String addr = str.substring(0, id);
    int port = tn.fromString(str.substring(id+1)).intValue();
    return new InetSocketAddress(addr, port);
  }
  
}
