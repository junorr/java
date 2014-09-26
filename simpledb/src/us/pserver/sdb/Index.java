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

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.CompactWriter;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 24/09/2014
 */
public class Index {

  private String label;
  
  private final Map<String, List<ValueIndex>> map;
  
  
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
    this.label = lbl;
    return this;
  }
  
  
  public Index put(Document doc) {
    if(doc != null && doc.label()
        .equalsIgnoreCase(label)
        && doc.block() != -1) {
      
      Iterator<String> it = doc.map()
          .keySet().iterator();
      while(it.hasNext()) {
        String key = it.next();
        ValueIndex vi = new ValueIndex(doc.map().get(key).toString(), doc.block());
        if(map.containsKey(key)) {
          List<ValueIndex> list = map.get(key);
          if(list.contains(vi)) {
            list.remove(vi);
          }
          list.add(vi);
        }
        else {
          List<ValueIndex> list = new LinkedList<>();
          list.add(vi);
          map.put(key, list);
        }
      }
    }
    return this;
  }
  
  
  public Document get(Document doc) {
    if(doc != null && doc.label()
        .equalsIgnoreCase(label)) {
      
      Iterator<String> it = doc.map()
          .keySet().iterator();
      while(it.hasNext()) {
        String key = it.next();
        if(!map.containsKey(key)) continue;
        ValueIndex vi = new ValueIndex(doc.map().get(key).toString());
        List<ValueIndex> list = map.get(key);
        int id = list.indexOf(vi);
        if(id < 0) continue;
        else {
          doc.block(list.get(id).getIndex());
          break;
        }
      }
    }
    return doc;
  }
  
  
  public long find(String key, String value) {
    if(key == null || key.isEmpty()
        || value == null || value.isEmpty())
      return -1;
    if(!map.containsKey(key))
      return -1;
    List<ValueIndex> list = map.get(key);
    ValueIndex vi = new ValueIndex(value);
    int id = list.indexOf(vi);
    if(id >= 0) return list.get(id).getIndex();
    return -1;
  }
  
  
  public static Index fromXml(String xml) {
    XStream x = new XStream();
    return (Index) x.fromXML(xml);
  }
  
  
  public String toXml() {
    XStream x = new XStream();
    StringWriter sw = new StringWriter();
    x.marshal(this, new CompactWriter(sw));
    return sw.toString();
  }
  
  
  @Override
  public String toString() {
    return toXml();
  }
  
  
  public static void main(String[] args) {
    Document d1 = new Document("Server")
        .put("name", "102")
        .put("ip", "172.29.14.102")
        .put("port", 22)
        .put("enabled", true)
        .block(5);
    Document d2 = new Document("Server")
        .put("name", "101")
        .put("ip", "172.29.14.101")
        .put("port", 22)
        .put("enabled", true)
        .block(4);
    Index i = new Index("server")
        .put(d1)
        .put(d2);
    String str = i.toString();
    System.out.println("* xml = "+ str);
    System.out.println("* idx = "+ Index.fromXml(str));
    d1.block(-1);
    System.out.println("* doc1: "+ d1.block());
    i.get(d1);
    System.out.println("* doc1: block="+ d1.block()+ " >> "+ d1);
  }
  
}
