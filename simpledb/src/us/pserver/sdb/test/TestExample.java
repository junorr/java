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

import us.pserver.sdb.Document;
import us.pserver.sdb.query.Query;
import us.pserver.sdb.query.QueryBuilder;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 13/11/2014
 */
public class TestExample {

  
  public static void main(String[] args) {
    Document doc = new Document("server")
        .put("name", "102")
        .put("ip", "172.29.14.102");
    doc.put("creds", new Document("credentials")
        .put("user", "juno")
        .put("pass", "1234"));
    doc.<Document>getAs("creds")
        .put("id", new Document("identification")
            .put("uid", "98765"));
    
    System.out.println("* doc: "+ doc);
    
    Query qry = QueryBuilder.builder().fromExample(doc);
    System.out.println("* query: "+ qry);
    
    System.out.println("* query.exec(doc): "+ qry.exec(doc));
  }
  
}
