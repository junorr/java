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

package us.pserver.zerojs.test;

import java.io.IOException;
import java.io.StringReader;
import us.pserver.zerojs.JsonHandler;
import us.pserver.zerojs.JsonReader;
import us.pserver.zerojs.exception.JsonParseException;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 17/04/2016
 */
public class TestParser {

  
  public static void main(String[] args) throws IOException {
    String json = "{'hello\"escaped quotes\"': 'world '''of {:}{:[]:[]}', 'array': [1, 2, 3, 4], 'objs': [{'a': 0}, {'b': 1}, {'c': 2}]}";
    System.out.println("* json = "+ json);
    JsonReader rd = JsonReader.defaultReader(new StringReader(json));
    JsonHandler hd = new JsonHandler() {
      boolean value = false;
      @Override
      public void startObject() throws JsonParseException {
        if(value) {
          System.out.print(", ");
          value = false;
        }
        System.out.print("{ ");
      }
      @Override
      public void endObject() throws JsonParseException {
        System.out.print(" }");
      }
      @Override
      public void startArray() throws JsonParseException {
        System.out.print("[ ");
      }
      @Override
      public void endArray() throws JsonParseException {
        System.out.print(" ]");
      }
      @Override
      public void name(String str) {
        if(value) {
          System.out.print(", ");
          value = false;
        }
        System.out.print("'"+ str+ "': ");
      }
      @Override
      public void value(String str) {
        if(value) {
          System.out.print(", ");
        }
        System.out.print(str);
        value = true;
      }
    };
    rd.addHandler(hd).read();
  }
  
}