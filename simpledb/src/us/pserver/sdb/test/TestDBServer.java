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

package us.pserver.sdb.test;

import java.net.InetSocketAddress;
import us.pserver.rob.container.Credentials;
import us.pserver.sdb.net.DBServer;
import us.pserver.sdb.SimpleDB;
import us.pserver.sdb.engine.CachedEngine;
import us.pserver.sdb.engine.FileEngine;
import us.pserver.sdb.engine.JsonSerialEngine;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 29/12/2014
 */
public class TestDBServer {

  
  public static void main(String[] args) {
    FileEngine fe = new FileEngine(new JsonSerialEngine(), "./remote.db");
    InetSocketAddress addr = new InetSocketAddress("0.0.0.0", 25000);
    DBServer server = new DBServer(addr, new SimpleDB(new CachedEngine(fe)));
    server.getCredentialsSource().put(new Credentials("juno", new StringBuffer("1234")));
    server.start();
  }
  
}
