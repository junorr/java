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

package us.pserver.sdb;

import com.google.gson.Gson;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 24/09/2014
 */
public class Index {

  private String label;
  
  private Map<Document, Long> map;
  
  
  public Index() {
    map = new LinkedHashMap<>();
    label = null;
  }
  
  
  public Index(String label) {
    label(label);
    map = new LinkedHashMap<>();
  }
  
  
  public String label() {
    return label;
  }
  
  
  public Index label(String lbl) {
    if(lbl == null || lbl.isEmpty())
      throw new IllegalArgumentException(
          "Invalid index label: "+ lbl);
    if(lbl.length() > 50)
      lbl = lbl.substring(0, 50);
    this.label = lbl;
    return this;
  }
  
  
  public Index put(Document key, long block) {
    if(key != null 
        && !key.map().isEmpty() 
        && block >= 0 
        && (key.label().equalsIgnoreCase(label) 
        || label == null)) {
      map.put(key, block);
    }
    return this;
  }
  
  
  public Map<Document, Long> map() {
    return map;
  }
  
  
  @Override
  public String toString() {
    return new Gson().toJson(this);
  }
  
  
  public static void main(String[] args) {
    Index i = new Index("server")
        .put(new Document("Server")
            .put("name", "102")
            .put("ip", "172.29.14.102")
            .put("port", 22)
            .put("enabled", true), 5)
        .put(new Document("Server")
            .put("name", "101")
            .put("ip", "172.29.14.101")
            .put("port", 22)
            .put("enabled", true), 6);
    String str = i.toString();//.replace("\\", "");
    System.out.println("* toJson   = "+ str);
    System.out.println("* fromJson = "+ new Gson().fromJson(str, Index.class));
  }
  
}
