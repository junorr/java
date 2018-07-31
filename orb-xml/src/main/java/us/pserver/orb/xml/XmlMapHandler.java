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

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 30/07/2018
 */
public class XmlMapHandler extends DefaultHandler {

  private final Map<String,Object> map;
  
  private final Deque<String> elements;
  
  
  public XmlMapHandler(Map<String,Object> map) {
    this.map = Objects.requireNonNull(map);
    this.elements = new ArrayDeque<>();
  }
  
  public XmlMapHandler() {
    this(new TreeMap<>());
  }
  
  public Map<String,Object> getMap() {
    return map;
  }
  
  private Map<String,Object> getCurrentMap(boolean create) {
    Iterator<String> it = elements.iterator();
    Map<String,Object> cur = null;
    while(it.hasNext()) {
      String el = it.next();
      System.out.println("element = "+ el);
      if(cur == null) {
        System.out.println("map == null");
        cur = map;
      }
      else if(cur.containsKey(el)) {
        System.out.println("map.containsKey( "+ el+ " )");
        cur = (Map<String,Object>) cur.get(el);
      }
      else if(!el.equals(elements.peekLast()) || create) {
        System.out.println("map = new TreeMap()");
        Map<String,Object> m = new TreeMap<>();
        cur.put(el, m);
        cur = m;
      }
    }
    return cur;
  }
  
  @Override
  public void startElement(String uri, String localName, String qName, Attributes attributes) {
    elements.add(qName);
    if(attributes.getLength() > 0) {
      Map<String,Object> cm = getCurrentMap(true);
      System.out.printf("* startEl<%s>.getCurrentMap(): %s%n", qName, cm);
      for(int i = 0; i < attributes.getLength(); i++) {
        cm.put(attributes.getQName(i), attributes.getValue(i));
      }
    }
  }
  
  @Override
  public void characters(char[] ch, int start, int length) {
    String value = new String(ch, start, length);
    if(value.trim().isEmpty()) return;
    Map<String,Object> cm = getCurrentMap(false);
    System.out.printf("* char<%s>.getCurrentMap(): %s%n", value, cm);
    cm.put(elements.peekLast(), new String(ch, start, length));
  }
  
  @Override
  public void endElement(String uri, String localName, String qName) {
    elements.pollLast();
  }
  
}
