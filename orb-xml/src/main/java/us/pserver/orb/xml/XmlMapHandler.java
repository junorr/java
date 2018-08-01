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

package us.pserver.orb.xml;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;
import us.pserver.tools.misc.Tuple;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 30/07/2018
 */
public class XmlMapHandler extends DefaultHandler {

  private final LinkedList<Tuple<String,Map<String,Object>>> elements;
  
  
  public XmlMapHandler() {
    this.elements = new LinkedList<>();
  }
  
  public Map<String,Object> getMap() {
    return null;
  }
  
  private Map<String,Object> getCurrentMap(boolean create) {
    return null;
  }
  
  public List<Map<String,Object>> makeAsList(String element, Map<String,Object> map) {
    
  }
  
  @Override
  public void startElement(String uri, String localName, String qName, Attributes attributes) {
    if(elements.isEmpty()) {
      elements.add(new Tuple<>(qName, new HashMap<>()));
    }
    setAttributes(qName, attributes);
  }
  
  
  private int reverseIndexOf(String elm) {
    for(int i = (elements.size() -1); i >= 0; i--) {
      Tuple<String,Map<String,Object>> t = elements.get(i);
      if(t.key().equals(elm)) return i;
    }
    return -1;
  }
  
  private void setAttributes(String elm, Attributes attrs) {
    int ixe = reverseIndexOf(elm);
    if(ixe < 0) {
      
    }
    Tuple<String,Map<String,Object>> t = elements.get(ixe);
    for(int i = 0; i < attrs.getLength(); i++) {
      t.value().put(attrs.getQName(i), attrs.getValue(i));
    }
  }
  
  @Override
  public void characters(char[] ch, int start, int length) {
    String value = new String(ch, start, length);
  }
  
  @Override
  public void endElement(String uri, String localName, String qName) {
    elements.pollLast();
  }
  
}
