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

package us.pserver.sdb.filedriver.test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import static com.fasterxml.jackson.core.JsonToken.VALUE_EMBEDDED_OBJECT;
import java.io.IOException;
import us.pserver.tools.timer.Timer;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 19/02/2017
 */
public class TestJacksonParse {

  public static long calcMem() {
    for(int i = 0; i < 10; i++) {
      System.gc();
    }
    return Runtime.getRuntime().freeMemory();
  }
  
  
  public static void parseObject(JsonParser par) throws IOException {
    System.out.println("---- OBJECT ----");
    Object o = null;
    String s = null;
    Number n = null;
    Boolean b = null;
    JsonToken t = null;
    while((t = par.nextToken()) != JsonToken.END_OBJECT) {
      switch(t) {
        case START_ARRAY:
          System.out.print("[");
          while(par.nextToken() != JsonToken.END_ARRAY) {
            o = par.getCurrentValue();
            System.out.print(o + ", ");
          }
          System.out.println("]");
          break;
        case FIELD_NAME:
          s = par.getCurrentName();
          System.out.print(" - "+ s+ "=");
          break;
        case VALUE_TRUE:
        case VALUE_FALSE:
          b = par.getBooleanValue();
          System.out.println(b);
          break;
        case VALUE_EMBEDDED_OBJECT:
          System.out.println(VALUE_EMBEDDED_OBJECT);
          parseObject(par);
          break;
        case VALUE_NUMBER_FLOAT:
        case VALUE_NUMBER_INT:
          n = par.getNumberValue();
          System.out.println(n);
          break;
        case VALUE_STRING:
          s = par.getValueAsString();
          System.out.println(s);
          break;
        case VALUE_NULL:
          o = par.getCurrentValue();
          System.out.println(o);
          break;
      }
    }
  }
  
  
  public static void main(String[] args) throws IOException {
    Timer tm = new Timer.Nanos();
    String str = "{" +
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
    //ByteArrayInputStream bis = new ByteArrayInputStream(
        //UTF8String.from(str).getBytes()
    //);
    //for(int i = 0; i < 2; i++) {
      
    System.out.println(str);
    long mem = calcMem();
    System.out.println("* free: "+ new FSize(mem));
    tm.start();
    
    JsonFactory fac = new JsonFactory();
    JsonParser par = fac.createParser(str);
    parseObject(par);
    
    tm.stop();
    long mem2 = calcMem();
    System.out.println("* free: "+ new FSize(mem2));
    System.out.println(tm);
    System.out.println("* diff: "+ new FSize(mem2 - mem));
    //}
  }
  
}
