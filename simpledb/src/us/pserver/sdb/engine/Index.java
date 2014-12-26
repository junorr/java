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

package us.pserver.sdb.engine;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.CompactWriter;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import us.pserver.sdb.Document;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 24/09/2014
 */
public class Index {

  private Map<String, List<int[]>> map;
  
  private transient SerialEngine serial;
  
  
  public Index() {
    map = new LinkedHashMap<>();
    serial = null;
  }
  
  
  public Index(SerialEngine seng) {
    if(seng == null)
      throw new IllegalArgumentException(
          "Invalid SerialEngine: "+ seng+ " - [Index.init]");
    map = new LinkedHashMap<>();
    serial = seng;
  }
  
  
  public Index setSerialEngine(SerialEngine se) {
    serial = se;
    return this;
  }
  
  
  public SerialEngine getSerialEngine() {
    return serial;
  }
  
  
  protected Map<String, List<int[]>> map() {
    return map;
  }
  
  
  public List<int[]> getList(String label) {
    if(map.containsKey(label))
      return map.get(label);
    return null;
  }
  
  
  public boolean contains(String label, long block) {
    if(!map.containsKey(label)) 
      return false;
    
    List<int[]> ls = map.get(label);
    for(int[] is : ls) {
      if(is[0] == block) return true;
    }
    return false;
  }
  
  
  public boolean contains(List<int[]> ls, int block) {
    if(ls == null || ls.isEmpty()) return false;
    for(int[] is : ls) {
      if(is[0] == block)
        return true;
    }
    return false;
  }
  
  
  public int indexOf(String label, long block) {
    if(!map.containsKey(label)) 
      return -1;
    
    List<int[]> ls = map.get(label);
    for(int i = 0; i < ls.size(); i++) {
      if(ls.get(i)[0] == block)
        return i;
    }
    return -1;
  }
  
  
  public int indexOf(long block) {
    if(block < 0) return -1;
    Iterator<String> it = map.keySet().iterator();
    while(it.hasNext()) {
      String lbl = it.next();
      int idx = indexOf(lbl, block);
      if(idx >= 0) return idx;
    }
    return -1;
  }
  
  
  public int[] get(String label, int block) {
    int idx = indexOf(label, block);
    if(idx < 0) return null;
    return map.get(label).get(idx);
  }
  
  
  public int remove(long block) {
    if(block < 0) return -1;
    Iterator<String> it = map.keySet().iterator();
    while(it.hasNext()) {
      String lbl = it.next();
      if(contains(lbl, block))
        return remove(lbl, (int)block)[1];
    }
    return -1;
  }
  
  
  public int getBlockSize(Document doc) {
    if(doc == null) return 0;
    int len = serial.serialize(doc).length;
    int bls = len / FileHandler.BLOCK_SIZE;
    if(len % FileHandler.BLOCK_SIZE > 0)
      bls++;
    return bls;
  }
  
  
  public boolean put(Document doc) {
    if(doc == null) return false;
    return put(doc.label(), 
        (int)doc.block(), getBlockSize(doc));
  }
  
  
  public boolean put(String label, int block, int size) {
    if(label != null
        && block != -1) 
    {
      if(map.containsKey(label)) {
        List<int[]> ls = map.get(label);
        boolean cont = contains(ls, block);
        if(!cont) {
          ls.add(new int[]{block, size});
        }
      }
      else {
        List<int[]> ls = new LinkedList<>();
        ls.add(new int[]{block, size});
        map.put(label, ls);
      }
      return true;
    }
    return false;
  }
  
  
  public boolean remove(Document doc) {
    if(doc == null 
        || doc.label() == null
        || doc.block() < 0)
      return false;
    
    return remove(doc.label(), (int)doc.block()) != null;
  }
  
  
  public int[] remove(String label, int block) {
    if(label == null) return null;
    int idx = indexOf(label, block);
    if(idx >= 0) {
      List<int[]> ls = map.get(label);
      int[] is = ls.remove(idx);
      if(ls.isEmpty())
        map.remove(label);
      return is;
    }
    return null;
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
  
}
