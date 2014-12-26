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

package us.pserver.test;

import com.cedarsoftware.util.io.JsonReader;
import com.cedarsoftware.util.io.JsonWriter;
import java.io.IOException;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 26/12/2014
 */
public class TestJson {

  
  static class Creds {
    String user;
    String pass;
    boolean enabled;
    public String toString() {
      return "Creds{" + "user=" + user + ", pass=" + pass + ", enabled=" + enabled + '}';
    }
  }
  
  static class Server {
    int port;
    String ip;
    String name;
    boolean hasdb;
    Creds creds;
    public String toString() {
      return "Server{" + "port=" + port + ", ip=" + ip + ", name=" + name + ", hasdb=" + hasdb + ", creds="+ creds+ '}';
    }
  }
  
  
  public static void main(String[] args) throws IOException {
    Creds creds = new Creds();
    creds.enabled = true;
    creds.pass = "1234";
    creds.user = "juno";
    
    Server s102 = new Server();
    s102.creds = creds;
    s102.hasdb = true;
    s102.ip = "172.29.14.102";
    s102.name = "102";
    s102.port = 22;
    
    System.out.println("* s102: "+ s102);
    String json = JsonWriter.objectToJson(s102);
    System.out.println("* json: "+ json);
    s102 = (Server) JsonReader.jsonToJava(json);
    System.out.println("* s102: "+ s102);
  }
  
}
