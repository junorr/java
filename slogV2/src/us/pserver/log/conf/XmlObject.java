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

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import us.pserver.log.impl.LogLevels;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 09/07/2015
 */
public abstract class XmlObject extends XmlClass {
  
  public static final String NAME = "name";
  
  public static final String LEVEL = "level";

  XmlLevels levels;
  
  String name;
  
  
  public XmlObject(Class type, String name) {
    super(type);
    if(name == null)
      throw new IllegalArgumentException("Invalid name: "+ name);
    levels = new XmlLevels();
    this.name = name;
  }
  
  
  public String getName() {
    return name;
  }
  
  
  public XmlLevels getXmlLevels() {
    return levels;
  }
  
  
  public LogLevels levels() {
    return levels.levels();
  }
  
  
  public static Node find(Node nd, String name) {
    NodeList nl = nd.getChildNodes();
    for(int i = 0; i < nl.getLength(); i++) {
      Node n = nl.item(i);
      if(name.equalsIgnoreCase(n.getNodeName())) {
        return n;
      }
    }
    return null;
  }
  
  
  public static XmlObject from(Node node) throws ClassNotFoundException {
    if(node == null)
      throw new IllegalArgumentException("Invalid XmlObject Element: "+ node);
    Node nclass = find(node, CLASS);
    if(nclass == null)
      throw new IllegalArgumentException("Node does not contains Class node");
    NamedNodeMap map = node.getAttributes();
    Node nname = map.getNamedItem(NAME);
    if(nname == null)
      throw new IllegalArgumentException("Node does not contains Name attribute");
    XmlClass xc = XmlClass.from(nclass);
    XmlObject xo = new XmlObject(xc.classType(), nname.getNodeValue()){};
    Node nlvl = find(node, XmlLevels.LEVEL);
    if(nlvl != null) {
      xo.levels().copyFrom(XmlLevels.from(nlvl).levels());
    }
    return xo;
  }
  
  
  public Element createElement(Document doc, String element) {
    if(doc == null || element == null)
      return null;
    Element e = doc.createElement(element);
    Attr atr = doc.createAttribute(NAME);
    atr.setValue(name);
    e.setAttributeNode(atr);
    e.appendChild(super.createElement(doc));
    Node nlvl = levels.createElement(doc);
    if(nlvl != null) e.appendChild(nlvl);
    return e;
  }


  @Override
  public String toString() {
    return "XmlObject{" + "class=" + type.getName() + ", name=" + name + ", levels="+ levels+ '}';
  }
  
}
