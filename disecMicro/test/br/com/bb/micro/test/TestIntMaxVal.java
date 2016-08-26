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

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 26/08/2016
 */
public class TestIntMaxVal {

  private static JsonObject createHeader() {
    JsonObject obj = new JsonObject();
    obj.addProperty("json-start", Long.MAX_VALUE);
    obj.addProperty("data-start", Long.MAX_VALUE);
    obj.addProperty("entries-count", Long.MAX_VALUE);
    obj.addProperty("entry-length", Long.MAX_VALUE);
    obj.addProperty("trash", "0");
    return obj;
  }  

  public static void main(String[] args) {
    System.out.println(Integer.MAX_VALUE);
    System.out.println(Long.MAX_VALUE);
    String header = new Gson().toJson(createHeader())+"\n\r";
    System.out.println(header);
    System.out.println("* length: "+ header.length());
  }
  
}
