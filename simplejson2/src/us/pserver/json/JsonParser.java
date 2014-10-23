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

package us.pserver.json;

import java.util.StringTokenizer;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 25/09/2014
 */
public class JsonParser {

  public JsonParser() {
    
  }
  
  
  public Number getNumber(String str) {
    try {
      return Double.parseDouble(str);
    } catch(NumberFormatException e) {
      return null;
    }
  }
  
  
  public JsonArray parseArray(String json) {
    if(json == null || json.isEmpty()
        || !json.trim().startsWith("["))
      return null;
    
    StringTokenizer st = new StringTokenizer(json, ",");
    JsonArray array = new JsonArray();
    boolean ignore = false;
    while(st.hasMoreElements()) {
      Object o = st.nextElement();
      if(o == null) continue;
      String s = o.toString();
      if(ignore) continue;
      if(s.trim().startsWith("["))
        ignore = true;
      if(s.trim().endsWith("]"))
        ignore = false;
      array.add(parse(s));
    }
    return array;
  }
  
  
  public Element parse(String json) {
    if(json == null || json.isEmpty())
      throw new IllegalArgumentException(
          "Invalid json: "+ json);
    
    json = json.trim();
    if(getNumber(json) != null)
      return new Element(getNumber(json));
    if(json.startsWith("[")) {
      return parseArray(json.substring(1, json.length() -1));
    }
    else if(json.startsWith("'")) {
      
    }
    
  }
  
}
