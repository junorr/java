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
import java.util.Collections;
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
  
  private transient long block;
  
  
  public Index() {
    map = new LinkedHashMap<>();
    label = null;
    block = 0;
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
  
  
  public long block() {
    return block;
  }
  
  
  public Index block(long blk) {
    block = blk;
    return this;
  }
  
  
  protected Map<String, List<ValueIndex>> map() {
    return map;
  }
  
  
  public boolean put(Document doc) {
    if(doc != null && doc.label()
        .equalsIgnoreCase(label)
        && doc.block() != -1) {
      
      Iterator<String> it = doc.map()
          .keySet().iterator();
      while(it.hasNext()) {
        String key = it.next();
        if(key.startsWith("@_")) continue;
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
      return true;
    }
    return false;
  }
  
  
  public boolean remove(Document doc) {
    if(doc == null || !doc.label().equalsIgnoreCase(label)
        || doc.map().isEmpty() || doc.block() < 0)
      return false;
    Iterator<String> it = map.keySet().iterator();
    boolean success = false;
    LinkedList<String> rmkeys = new LinkedList<>();
    while(it.hasNext()) {
      String k = it.next();
      List<ValueIndex> lvi = map.get(k);
      int index = -1;
      for(int i = 0; i < lvi.size(); i++) {
        if(lvi.get(i).getIndex() == doc.block()) {
          index = i;
          success = true;
          break;
        }
      }
      if(index >= 0) {
        //lvi.remove(index);
        System.out.println("* index.remove: "+ lvi.remove(index));
      }
      if(map.get(k).isEmpty())
        rmkeys.add(k);
    }
    for(String key : rmkeys) {
      map.remove(key);
    }
    return success;
  }
  
  
  public long findOne(Document doc) {
    if(doc != null && doc.label()
        .equalsIgnoreCase(label)) {
      return findOne(Query.fromExample(doc));
    }
    return -1;
  }
  
  
  public long findOne(String key, Object value) {
    if(key == null || key.isEmpty()
        || value == null || value.toString().isEmpty())
      return -1;
    if(!map.containsKey(key))
      return -1;
    List<ValueIndex> list = map.get(key);
    int id = 0;
    for(int i = 0; i < list.size(); i++) {
      if(list.get(i).getValue().equals(value.toString())) {
        id = i;
        break;
      }
    }
    if(id >= 0) return list.get(id).getIndex();
    return -1;
  }
  
  
  public long findOne(Query q) {
    List<Long> list = find(q);
    if(list.isEmpty()) return -1;
    return list.get(0);
  }
  
  
  public List<Long> find(Query q) {
    List<Long> list = new LinkedList<>();
    List<Long> rm = new LinkedList<>();
    q = q.head();
    if(q == null 
        || q.key() == null 
        || q.method() == null 
        || q.value() == null)
      return list;
    
    while(q != null && q.key() != null 
        && q.value() != null) {
      if(!map.containsKey(q.key())) {
        q = q.next();
        continue;
      }
      List<ValueIndex> lvi = map.get(q.key());
      for(ValueIndex v : lvi) {
        boolean b = q.exec(v.getValue()).getResult();
        System.out.println(" - find.ValueIndex: "+ v.getValue()+ ", exec: "+ b);
        if(b) {
          if(!list.contains(v.getIndex()) 
              && !rm.contains(v.getIndex()))
            list.add(v.getIndex());
        } else if(q.prev() != null && q.prev().isAnd()) {
          if(list.contains(v.getIndex())) {
            list.remove(v.getIndex());
            rm.add(v.getIndex());
          }
        }
      }
      q = q.next();
    }
    return list;
  }
  
  
  public List<Long> find(Document doc) {
    if(doc != null && doc.label()
        .equalsIgnoreCase(label)) {
      return find(Query.fromExample(doc));
    }
    return Collections.EMPTY_LIST;
  }
  
  
  public List<Long> find(String key, Object value) {
    List<Long> list = Collections.EMPTY_LIST;
    if(key == null || key.isEmpty()
        || value == null || value.toString().isEmpty())
      return list;
    
    if(!map.containsKey(key)) 
      return list;
    
    List<ValueIndex> lvi = map.get(key);
    list = new LinkedList<>();
    for(int i = 0; i < lvi.size(); i++) {
      if(lvi.get(i).getValue().equals(value.toString()))
        list.add(lvi.get(i).getIndex());
    }
    return list;
  }
  
  
  public List<Long> getAllBlocks() {
    List<Long> list = Collections.EMPTY_LIST;
    if(map.isEmpty()) return list;
    list = new LinkedList<>();
    Iterator<String> it = map.keySet().iterator();
    while(it.hasNext()) {
      String key = it.next();
      List<ValueIndex> lvi = map.get(key);
      for(ValueIndex v : lvi) {
        if(!list.contains(v.getIndex()))
          list.add(v.getIndex());
      }
    }
    return list;
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
    List<Document> ls = new LinkedList<>();
    ls.add(new Document("Server")
        .put("name", "102")
        .put("ip", "172.29.14.102")
        .put("port", 22)
        .put("enabled", true)
        .block(5));
    ls.add(new Document("Server")
        .put("name", "101")
        .put("ip", "172.29.14.101")
        .put("port", 22)
        .put("enabled", true)
        .block(4));
    ls.add(new Document("Server")
        .put("name", "103")
        .put("ip", "172.29.14.103")
        .put("port", 22)
        .put("enabled", true)
        .block(6));
    ls.add(new Document("Server")
        .put("name", "104")
        .put("ip", "172.29.14.104")
        .put("port", 22)
        .put("enabled", true)
        .block(7));
    ls.add(new Document("Server")
        .put("name", "105")
        .put("ip", "172.29.14.105")
        .put("port", 22)
        .put("enabled", false)
        .block(8));
    ls.add(new Document("Server")
        .put("name", "106")
        .put("ip", "172.29.14.106")
        .put("port", 22)
        .put("enabled", true)
        .block(9));
    ls.add(new Document("Server")
        .put("name", "107")
        .put("ip", "172.29.14.107")
        .put("port", 22)
        .put("enabled", true)
        .block(10));
    ls.add(new Document("Server")
        .put("name", "108")
        .put("ip", "172.29.14.108")
        .put("port", 22)
        .put("enabled", true)
        .block(11));
    
    Index i = new Index("server");
    for(Document c : ls)
      i.put(c);
    
    String str = i.toString();
    System.out.println("* xml = "+ str);
    System.out.println("* index length = "+ str.length());
    //System.out.println("* idx = "+ Index.fromXml(str));
    
    str = "";
    for(Document c : ls)
      str += c.toXml();
    
    System.out.println("* docs  length = " + str.length());
    System.out.println("* i.find(\"name\", \"105\"): "+ i.findOne("name", "105"));
    
    System.out.println("* i.findFirst(port, 22): "+ i.findOne("port", 22));
    
    List<Long> ll = i.find("port", 22);
    System.out.println("* i.find(port, 22): "+ ll.size());
    for(long l : ll)
      System.out.println(" - "+ l);
    
    
    Query q = new Query("name")
        .contains("0")
        //.and().contains("2")
        .and("enabled").equal(true)
        .and("ip").startsWith("172");
    
    System.out.println("* " + q);
    ll = i.find(q);
    System.out.println("* i.find: "+ ll.size());
    for(long l : ll)
      System.out.println(" - "+ l);
  }
  
}
