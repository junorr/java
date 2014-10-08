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
import us.pserver.sdb.Query;
import us.pserver.sdb.SimpleDB;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 03/10/2014
 */
public class TestSimpleDB {

  
  public static void main(String[] args) throws IOException {
    SimpleDB sdb = new SimpleDB("./simple.sdb");
    Document doc = new Document("server");
    doc.put("name", "102")
        .put("ip", "172.29.14.102")
        .put("port", 22)
        .put("enabled", true);
    //doc = sdb.put(doc);
    doc = sdb.getOne(doc);
    System.out.println("* doc: "+ doc);
    System.out.println("* doc.block="+ doc.block());
    
    System.out.println("* remove(doc): "+ sdb.remove(doc));
    
    doc = new Document("server");
    doc.put("name", "105")
        .put("ip", "172.29.14.105")
        .put("port", 22)
        .put("enabled", false);
    //doc = sdb.put(doc);
    doc = sdb.getOne(doc);
    System.out.println("* doc: "+ doc);
    System.out.println("* doc.block="+ doc.block());
    
    doc  = null;
    System.out.println("* doc: "+ doc);
    Query q = new Query("enabled")
        .not().equal("false");
    System.out.println("* "+ q);
    doc = sdb.getOne("server", q);
    System.out.println("** doc: "+ doc);
    System.out.println("** doc.block="+ (doc != null ? doc.block() : "null"));
    sdb.close();
  }
  
}
