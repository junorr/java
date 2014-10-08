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

import java.io.IOException;
import us.pserver.sdb.Document;
import us.pserver.sdb.SimpleDB;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 03/10/2014
 */
public class TestSimpleDB1 {

  
  public static void main(String[] args) throws IOException {
    SimpleDB sdb = new SimpleDB("./simple.sdb");
    Document doc = new Document("server");
    doc.put("name", "102")
        .put("ip", "172.29.14.102")
        .put("port", 22)
        .put("enabled", true)
        .put("user", "john")
        .put("pass", "nhoj")
        .put("ip_", "172.29.14.102")
        .put("port_", 22)
        .put("enabled_", true)
        .put("user_", "john")
        .put("pass_", "nhoj")
        .put("_ip", "172.29.14.102")
        .put("_port", 22)
        .put("_enabled", true)
        .put("_user", "john")
        .put("_pass", "nhoj");
    
    doc = sdb.put(doc);
    sdb.close();
  }
  
}
