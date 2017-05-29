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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import us.pserver.jose.json.JsonType;
import static us.pserver.jose.json.JsonType.BOOLEAN;
import static us.pserver.jose.json.JsonType.NUMBER;
import us.pserver.jose.json.JsonValue;
import us.pserver.jose.json.iterator.ByteIterator;
import us.pserver.jose.json.iterator.ByteIteratorFactory;
import us.pserver.tools.UTF8String;
import us.pserver.tools.timer.Timer;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 03/03/2017
 */
public class TestByteIterator {
  
  
  public static InputStream toStream(String str) {
    return new ByteArrayInputStream(UTF8String.from(str).getBytes());
  }

  
  public static void main(String[] args) throws IOException {
    String json = "{'hello': 'world', 'bean': {'bool': True, 'num': -52.5}, 'Null': Null, 'array': [10,20,30]}".replace("'", "\"");
    /*
    json = "{" +
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
    /**/
    ByteIterator bi = ByteIteratorFactory.of(json);
    //ByteIterator bi = ByteIteratorFactory.of(toStream(json));
    Timer tm = new Timer.Nanos().start();
    String ident = "";
    while(bi.hasNext()) {
      JsonType tk = bi.next();
      switch(tk) {
        case START_OBJECT:
          ident += "  ";
          System.out.println("{");
        case FIELD:
          String f = bi.readField();
          System.out.print(ident+ "- "+ f);
          break;
        case END_OBJECT:
          if(ident.length() > 2)
            ident = ident.substring(0, ident.length()-2);
          break;
        case END_ARRAY:
          System.out.println("]");
          break;
        case START_ARRAY:
          System.out.print("[ ");
          break;
        case BOOLEAN:
          Boolean b = bi.readBoolean();
          if(bi.isInsideArray()) {
            System.out.print(b+ ", ");
          } else {
            System.out.println(b + " ("+ b.getClass().getSimpleName()+ ")");
          }
          break;
        case NUMBER:
          JsonValue val = bi.read(tk);
          if(bi.isInsideArray()) {
            System.out.print(val.getAsNumber()+ "("+ val.getNumberType()+ "), ");
          } else {
            System.out.println(val);
          }
          break;
        case STRING:
          String s = bi.readString();
          if(bi.isInsideArray()) {
            System.out.print(s+ ", ");
          } else {
            System.out.println(s+ " (String)");
          }
          break;
        case NULL:
          Object nill = bi.readNull();
          if(bi.isInsideArray()) {
            System.out.print(nill+ ", ");
          } else {
            System.out.println(nill+ " (null)");
          }
          break;
        case VALUE:
          System.out.print(" = ");
          break;
      }
    }
    System.out.println(tm.stop());
  }
  
}
