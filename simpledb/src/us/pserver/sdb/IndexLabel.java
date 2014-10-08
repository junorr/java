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
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 24/09/2014
 */
public class IndexLabel {

  private final Map<String, List<Long>> map;
  
  
  public IndexLabel() {
    map = new LinkedHashMap<>();
  }
  
  
  protected Map<String, List<Long>> map() {
    return map;
  }
  
  
  public List<Long> getList(String label) {
    if(map.containsKey(label))
      return map.get(label);
    return null;
  }
  
  
  public boolean contains(String label, long idx) {
    return map.containsKey(label)
        && map.get(label).contains(idx);
  }
  
  
  public boolean put(Document doc) {
    if(doc == null) return false;
    return put(doc.label(), doc.block());
  }
  
  
  public boolean put(String label, long block) {
    if(label != null
        && block != -1) 
    {
      if(map.containsKey(label)) {
        List<Long> ls = map.get(label);
        if(!ls.contains(block))
          ls.add(block);
      }
      else {
        List<Long> ls = new LinkedList<>();
        ls.add(block);
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
    
    if(map.containsKey(doc.label())) {
      List<Long> ls = map.get(doc.label());
      int idx = ls.indexOf(doc.block());
      if(idx >= 0) {
        ls.remove(idx);
        return true;
      }
    }
    return false;
  }
  
  
  public static IndexLabel fromXml(String xml) {
    XStream x = new XStream();
    return (IndexLabel) x.fromXML(xml);
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
