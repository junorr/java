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

import com.jsoniter.JsonIterator;
import com.jsoniter.ValueType;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import us.pserver.tools.UTF8String;
import us.pserver.tools.timer.Timer;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 19/02/2017
 */
public class TestAnyParse {

  public static long calcMem() {
    for(int i = 0; i < 10; i++) {
      System.gc();
    }
    return Runtime.getRuntime().freeMemory();
  }
  
  
  public static void parseObject(JsonIterator iter) throws IOException {
    boolean b = false;
    String s = null;
    double d = 0;
    Object o = null;
    for(String fld = iter.readObject(); fld != null; fld = iter.readObject()) {
      System.out.print(" - "+ fld+ "=");
      ValueType type = iter.whatIsNext();
      switch(type) {
        case ARRAY:
          //while(iter.readArray()) {
            //o = iter.read();
          //}
          System.out.println(iter.readAny());
          //System.out.println("ARRAY");
          break;
        case BOOLEAN:
          //b = iter.readBoolean();
          System.out.println(iter.readBoolean());
          //System.out.println("BOOLEAN");
          break;
        case INVALID:
          //System.out.println("INVALID");
          break;
        case NULL:
          //o = iter.read();
          System.out.println(iter.read());
          //System.out.println("NULL");
          break;
        case NUMBER:
          //d = iter.readDouble();
          System.out.println(iter.readDouble());
          //System.out.println("NUMBER");
          break;
        case OBJECT:
          //System.out.println("OBJECT");
          parseObject(iter);
          break;
        case STRING:
          //s = iter.readString();
          System.out.println(iter.readString());
          //System.out.println("STRING");
          break;
        default:
          throw new RuntimeException("ValueType switch default?");
      }
    }
  }
  
  
  public static void main(String[] args) throws IOException {
    Timer tm = new Timer.Nanos();
    //ByteArrayInputStream bis = new ByteArrayInputStream(
        //UTF8String.from(new JsonCreator()
            //.withDepth(4).create()).getBytes()
    //);
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
    JsonIterator iter = JsonIterator.parse(str);
    parseObject(iter);
//    
    tm.stop();
    long mem2 = calcMem();
    System.out.println("* free: "+ new FSize(mem2));
    System.out.println(tm);
    System.out.println("* diff: "+ new FSize(mem2 - mem));
    //}
  }
  
}
