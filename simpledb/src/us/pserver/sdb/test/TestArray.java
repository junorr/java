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

import java.util.LinkedList;
import java.util.List;
import us.pserver.sdb.Document;
import us.pserver.sdb.query.Query1;
import us.pserver.sdb.query.QueryBuilder;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 14/11/2014
 */
public class TestArray {

  
  public static void main(String[] args) {
    List ls = new LinkedList();
    ls.add(30.6);
    ls.add(30.7);
    ls.add(30.7);
    ls.add(28.5);
    Document doc = new Document("server")
        .put("name", "102")
        .put("ip", "172.29.14.102")
        .put("latency", ls);
    
    Query1 q = QueryBuilder.builder("server")
        .field("latency").contains(28.5).create();
    
    System.out.println("* doc: "+ doc);
    System.out.println("* qry: "+ q);
    System.out.println("* qry.exec(doc): "+ q.exec(doc));
  }
  
}
