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

package br.com.bb.micro.test;

import br.com.bb.disec.micro.util.json.JsonTransformer;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mongodb.util.JSON;
import java.util.Objects;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 01/09/2016
 */
public class TestJsonElementString {

  
  public static void main(String[] args) {
    JsonObject el = new JsonObject();
    el.addProperty("boolean", true);
    el.addProperty("number", 5.12);
    el.addProperty("string", "abc");
    System.out.println("* boolean: "+ Objects.toString(el.get("boolean")));
    System.out.println("* string: "+ Objects.toString(el.get("string")));
    System.out.println("* number: "+ Objects.toString(el.get("number")));
    
    JsonArray array = new JsonArray();
    for(int i = 0; i < 10; i++) {
      array.add(i);
    }
    el.add("array", array);
    
    JsonTransformer jt = new JsonTransformer();
    System.out.println("* array: "+ Objects.toString(array));
    System.out.println("* toList: "+ Objects.toString(jt.toList(array)));
    System.out.println("* toDoc: "+ JSON.serialize(jt.toDocument(el)));
  }
  
}