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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;
import us.pserver.tools.misc.Tuple;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 30/07/2018
 */
public class XmlMapHandler extends DefaultHandler {
  
  public static final String LIST = "list";
  
  public static final String VALUE = "value";
  

  private final LinkedList<Tuple<String,Map<String,Object>>> elements;
  
  
  public XmlMapHandler() {
    this.elements = new LinkedList<>();
  }
  
  public Map<String,Object> getMap() {
    return elements.peekFirst().value();
  }
  
  @Override
  public void startElement(String uri, String localName, String qName, Attributes attributes) {
    elements.add(new Tuple<>(qName, new HashMap<>()));
    setAttributes(qName, attributes);
  }
  
  private Tuple<String,Map<String,Object>> reverseFind(String elm) {
    return reverseFind(t->t.key().equals(elm));
  }
  
  private Tuple<String,Map<String,Object>> reverseFind(Predicate<Tuple<String,Map<String,Object>>> prd) {
    for(int i = (elements.size() -1); i >= 0; i--) {
      Tuple<String,Map<String,Object>> t = elements.get(i);
      if(prd.test(t)) {
        return t;
      }
    }
    return null;
  }
  
  private void setAttributes(String name, Attributes attrs) {
    Tuple<String,Map<String,Object>> elm = reverseFind(name);
    if(elm == null) {
      throw new IllegalStateException(String.format("Can't find element (%s) in stack", name));
    }
    Map<String,Object> map = elm.value();
    for(int i = 0; i < attrs.getLength(); i++) {
      map.put(attrs.getQName(i), attrs.getValue(i));
    }
  }
  
  @Override
  public void characters(char[] ch, int start, int length) {
    String value = new String(ch, start, length);
    if(value.trim().isEmpty()) return;
    Tuple<String,Map<String,Object>> parent = elements.peekLast();
    parent.value().put(VALUE, value);
  }
  
  @Override
  public void endElement(String uri, String localName, String qName) {
    if(elements.size() < 2) return;
    Tuple<String,Map<String,Object>> elm = elements.pollLast();
    Tuple<String,Map<String,Object>> prev = elements.peekLast();
    Object value;
    //if is a key-value element
    if(elm.value().size() == 1 && elm.value().containsKey(VALUE)) {
      value = elm.value().get(VALUE);
    }
    else {
      value = elm.value();
    }
    //duplicates are a list
    if(prev.value().containsKey(elm.key())) {
      Object pvalue = prev.value().get(elm.key());
      List lst;
      if(List.class.isAssignableFrom(pvalue.getClass())) {
        lst = (List) pvalue;
      }
      else {
        lst = new LinkedList();
        lst.add(pvalue);
      }
      lst.add(value);
      value = lst;
    }
    prev.value().put(elm.key(), value);
  }
  
}
