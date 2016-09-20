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

package br.com.bb.disec.micros.test;

import br.com.bb.disec.micro.Server;
import br.com.bb.disec.micros.handler.SqlHandler;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 20/09/2016
 */
public class TestResource {

  Class cls;
  
  public TestResource(Class cls) {
    this.cls = cls;
  }
  
  public String relativeResource(String res) throws IOException {
    String def = cls.getResource("/"+ cls.getName().replace(".", "/")+ ".class").toString();
    def = def.replace("/"+ cls.getName().replace(".", "/")+ ".class", "");
    System.out.println("def: "+ def);
    Enumeration<URL> en = cls.getClassLoader().getResources((res.startsWith("/") ? res.substring(1) : res));
    String rel = null;
    while(en.hasMoreElements()) {
      rel = en.nextElement().toString();
      System.out.println("rel: "+ rel);
      System.out.println("rel.contains(def): "+rel.contains(def));
      if(rel.contains(def)) break;
    }
    return rel;
  }
  
  
  public static void main(String[] args) throws IOException {
    //System.out.println("/"+TestResource.class.getName().replace(".", "/"));
    //System.out.println(TestResource.class.getResource("/"+TestResource.class.getName().replace(".", "/")+ ".class"));
    String res = "resources/serverconf.json";
    TestResource local = new TestResource(SqlHandler.class);
    TestResource other = new TestResource(Server.class);
    
    System.out.println("* local: "+ local.relativeResource(res));
    System.out.println("* other: "+ other.relativeResource(res));
  }
  
}
