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

package us.pserver.jose.test;

import com.jsoniter.output.JsonStream;
import java.util.Map;
import us.pserver.jose.json.JsonMapReader;
import us.pserver.jose.json.iterator.ByteIterator;
import us.pserver.jose.json.iterator.ByteIteratorFactory;
import us.pserver.tools.timer.Timer;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 17/03/2017
 */
public class TestJsonMapReader {

  
  public static void main(String[] args) {
    /**/
    String json = (
        "{"
          + "'type@': 'us.pserver.Test', "
          + "'str1': 'hello', "
          + "'str2': 'world', "
          + "'array': [1, 2, 3, 4, 5], "
          + "'num': 500, "
          + "'child': {"
            + "'hello': 'world', "
            + "'bool': false"
          + "}, "
          + "'child2': {"
            + "'world': 'hello', "
            + "'sub': [{'n': 500.1},{'n': 500.2}], "
            + "'bool': true"
          + "}"
        + "}").replace("'", "\"");
    System.out.println("* json: "+ json);
    /**/
    /*
    String json = "{" +
      "\"serverAddress\": \"0.0.0.0\"," +
      "\"serverPort\": 9088," +
      "\"ioThreads\": 4," +
      "\"maxWorkerThreads\": 10," +
      "\"debugHeaders\": true," +
      "\"dispatcherEnabled\": false," +
      "\"shutdownHandlerEnabled\": true," +
      "\"authenticationShield\": true," +
      "\"jwtAuthKey\": \"F8BFDDC2130A81CDD33E63D63B1A39BE815EDB49272F442CB2E50090ABD40D88\"," +
      "\"handlers\": {" +
      "\"/jwt\": \"br.com.bb.disec.micro.handler.JWTAuthHandler\"," +
      "\"/simulate\": \"br.com.bb.disec.micro.handler.SimulateUserHandler\"," +
      "\"/time\": \"br.com.bb.disec.micro.handler.TimeHandler\"," +
      "\"/cache\": \"br.com.bb.disec.micros.handler.PublicCacheHandler\"," +
      "\"/download\": \"br.com.bb.disec.micros.handler.DownloadHandler\"," +
      "\"/export\": \"br.com.bb.disec.micros.handler.SqlExportHandler\"," +
      "\"/sql\": \"br.com.bb.disec.micros.handler.SqlHandler\"," +
      "\"/upload\": \"br.com.bb.disec.micros.handler.FileUploadHandler\"" +
      "}," +
      "\"sslContext\": {" +
      "\"sslEnabled\": true," +
      "\"sslAddress\": \"0.0.0.0\"," +
      "\"sslPort\": 9443," +
      "\"keystore\": \"/resources/micro.jks\"," +
      "\"keystorePass\": \"32132155\"" +
      "}" +
      "}";
    System.out.println(json);
    /**/
    ByteIterator bi = ByteIteratorFactory.of(json);
    JsonMapReader rdr = JsonMapReader.of(bi);
    Timer tm = new Timer.Nanos().start();
    Map<String,Object> map = rdr.readMap();
    tm.stop();
    System.out.println(map);
    System.out.println(tm);
    
    tm.clear().start();
    json = JsonStream.serialize(map);
    tm.stop();
    System.out.println(json);
    System.out.println(tm);
  }
  
}
