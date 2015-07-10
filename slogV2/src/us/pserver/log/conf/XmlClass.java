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

package us.pserver.log.conf;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 09/07/2015
 */
public class XmlClass {

  public static final String CLASS = "class";
  
  Class type;
  
  
  public XmlClass(Class type) {
    if(type == null)
      throw new IllegalArgumentException("Invalid Class type: "+ type);
    this.type = type;
  }
  
  
  protected Constructor findEmptyConstructor() {
    Constructor[] cons = type.getDeclaredConstructors();
    for(Constructor c : cons) {
      if(c.getParameterCount() == 0) {
        if(!c.isAccessible()) {
          c.setAccessible(true);
        }
        return c;
      }
    }
    return null;
  }
  
  
  public <T> T create() throws InstantiationException {
    Constructor con = findEmptyConstructor();
    if(con == null) 
      throw new InstantiationException(
          "Class <"+ type+ "> has no empty constructor");
    return create(con, null);
  }
  
  
  public <T> T create(Constructor c, Object ... args) throws InstantiationException {
    if(c == null) 
      throw new InstantiationException("Invalid constructor: "+ c);
    try {
      return (T) c.newInstance(args);
    } catch(IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
      throw new InstantiationException(e.toString());
    }
  }
  
  
  public Class classType() {
    return type;
  }
  
  
  public static XmlClass from(Node node) throws ClassNotFoundException {
    if(node == null || !node.getNodeName().equals(CLASS))
      throw new IllegalArgumentException("Invalid Node: "+ node);
    NodeList nl = node.getChildNodes();
    if(nl.getLength() < 1)
      throw new IllegalArgumentException("Node does not contains Class type");
    return new XmlClass(Class.forName(nl.item(0).getTextContent()));
  }
  
  
  public XmlClass setFrom(Node node) throws ClassNotFoundException {
    XmlClass xc = from(node);
    type = xc.classType();
    return this;
  }
  
  
  public Element createElement(Document doc) {
    if(doc == null) return null;
    Element e = doc.createElement(CLASS);
    e.appendChild(
        doc.createTextNode(type.getName())
    );
    return e;
  }


  @Override
  public String toString() {
    return "XmlClass{" + "class=" + type.getName() + '}';
  }
  
}
