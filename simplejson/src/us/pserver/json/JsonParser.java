/*
 * Direitos Autorais Reservados (c) 2011 Juno Roesler
 * Contato: juno.rr@gmail.com
 * 
 * Esta biblioteca � software livre; voc� pode redistribu�-la e/ou modific�-la sob os
 * termos da Licen�a P�blica Geral Menor do GNU conforme publicada pela Free
 * Software Foundation; tanto a vers�o 2.1 da Licen�a, ou qualquer
 * vers�o posterior.
 * 
 * Esta biblioteca � distribu�da na expectativa de que seja �til, por�m, SEM
 * NENHUMA GARANTIA; nem mesmo a garantia impl�cita de COMERCIABILIDADE
 * OU ADEQUA��O A UMA FINALIDADE ESPEC�FICA. Consulte a Licen�a P�blica
 * Geral Menor do GNU para mais detalhes.
 * 
 * Voc� deve ter recebido uma c�pia da Licen�a P�blica Geral Menor do GNU junto
 * com esta biblioteca; se n�o, acesse 
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html, 
 * ou escreva para a Free Software Foundation, Inc., no
 * endere�o 59 Temple Street, Suite 330, Boston, MA 02111-1307 USA.
 */

package us.pserver.json;

import java.util.LinkedList;
import java.util.List;
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
  
  
  public Document parsedoc(String str) {
    if(str == null 
        || str.isEmpty() 
        || !str.contains(":")
        || (!str.contains("{")
        &&  !str.contains("}")))
      return null;
    
    StringBuilder sb = new StringBuilder();
    char[] cs = str.substring(1, str.length()-1).toCharArray();
    Document doc = new Document();
    String key = null;
    Object value = null;
    boolean cont = false;
    int opbr = 0;
    int opsq = 0;
    for(int i = 0; i < cs.length; i++) {
      if(cs[i] == '}') {
        sb.append(cs[i]);
        opbr--;
        if(opbr == 0) {
          value = parsedoc(sb.toString());
          sb.delete(0, sb.length());
          cont = false;
        }
        continue;
      }
      if(cs[i] == ']') {
        sb.append(cs[i]);
        opsq--;
        if(opsq == 0) {
          value = parsearray(sb.toString());
          sb.delete(0, sb.length());
          cont = false;
        }
        continue;
      }
      
      if(cont) {
        sb.append(cs[i]);
        continue;
      }
      
      if(cs[i] == ':') {
        key = sb.toString().replace("'", "");
        sb.delete(0, sb.length());
      }
      else if(cs[i] == ',') {
        if(value != null) continue;
        value = sb.toString().replace("'", "");
        sb.delete(0, sb.length());
      }
      else if(cs[i] == '{') {
        sb.append(cs[i]);
        cont = true;
        opbr++;
      }
      else if(cs[i] == '[') {
        sb.append(cs[i]);
        cont = true;
        opsq++;
      }
      else {
        sb.append(cs[i]);
      }
      
      if(key != null && value != null) {
        doc.put(key, value);
        key = null;
        value = null;
      }
    }
    
    if(key != null && sb.length() > 0) {
      value = sb.toString().replace("'", "");
    }
    
    if(key != null && value != null) {
      doc.put(key, value);
    }
    if(doc.map().containsKey("class")) {
      doc.label(doc.getString("class"));
      doc.map().remove("class");
    }
    return doc;
  }
  
  
  public List parsearray(String str) {
    if(str == null 
        || str.isEmpty() 
        || !str.contains("[")
        || !str.contains("]"))
      return null;
    
    StringBuilder sb = new StringBuilder();
    char[] cs = str.substring(1, str.length()-1).toCharArray();
    List list = new LinkedList();
    Object value = null;
    boolean cont = false;
    int opbr = 0;
    int opsq = 0;
    for(int i = 0; i < cs.length; i++) {
      if(cs[i] == '}') {
        sb.append(cs[i]);
        opbr--;
        if(opbr == 0) {
          value = parsedoc(sb.toString());
          sb.delete(0, sb.length());
          cont = false;
        }
        continue;
      }
      if(cs[i] == ']') {
        sb.append(cs[i]);
        opsq--;
        if(opsq == 0) {
          value = parsearray(sb.toString());
          sb.delete(0, sb.length());
          cont = false;
        }
        continue;
      }
      
      if(cont) {
        sb.append(cs[i]);
        continue;
      }
      
      if(cs[i] == ',') {
        if(value != null) continue;
        value = sb.toString().replace("'", "");
        sb.delete(0, sb.length());
      }
      else if(cs[i] == '{') {
        sb.append(cs[i]);
        cont = true;
        opbr++;
      }
      else if(cs[i] == '[') {
        sb.append(cs[i]);
        cont = true;
        opsq++;
      }
      else {
        sb.append(cs[i]);
      }
      
      if(value != null) {
        list.add(value);
        value = null;
      }
    }
    
    if(sb.length() > 0) {
      value = sb.toString().replace("'", "");
    }
    
    if(value != null) {
      list.add(value);
    }
    return list;
  }
  
  
}
