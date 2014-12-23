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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.Iterator;
import us.pserver.sdb.Document;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 23/12/2014
 */
public class TestGson {
  
  
  public static void print(String pref, Document doc) {
    if(doc == null) return;
    if(pref == null) pref = "";
    System.out.println(pref+ "* Document: "+ doc.label()+ " {");
    Iterator<String> keys = doc.map().keySet().iterator();
    while(keys.hasNext()) {
      String k = keys.next();
      Object o = doc.get(k);
      if(o instanceof Document)
        print(pref+ "   ", (Document)o);
      else
        System.out.println(pref+ "   . "+ k+ " = "+ o+ " \t ("+ o.getClass()+ ")");
      
    }
    System.out.println(pref+ "}");
  }

  
  public static void main(String[] args) {
    Document doc = new Document("server")
        .put("name", "102")
        .put("ip", "172.29.14.102")
        .put("port", 22)
        .put("apps", 4)
        .put("db", true)
        .put("latency", 28.5)
        .put("creds", new Document("credentials")
            .put("user", "juno")
            .put("pass", new Document("password")
                .put("str", "1234")));
    
    System.out.println("* doc = "+ doc);
    print(null, doc);
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    String docjson = gson.toJson(doc);
    System.out.println("* gson= "+ docjson);
    doc = gson.fromJson(docjson, Document.class);
    System.out.println("* doc = "+ doc.toString());
    print(null, doc);
  }
  
}
