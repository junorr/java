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

package us.pserver.sjson.parse;

import java.util.LinkedList;
import java.util.List;
import us.pserver.sjson.JsonArray;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 23/12/2014
 */
public class ParserArray {

  private List<String> list;
  
  
  public ParserArray() {
    list = new LinkedList<String>();
  }
  
  
  public List<String> list() {
    return list;
  }
  
  
  public JsonArray parse(String str) {
    if(str == null || !str.startsWith("[") || !str.endsWith("]"))
      throw new IllegalArgumentException(
          "String is not a representation of json array: "+ str);
    str = str.substring(1, str.length()-1);
    for(int i = 0; i < str.length(); i++) {
      char c = str.charAt(i);
      if(c == '"') {
        
      }
    }
  }
  
}
