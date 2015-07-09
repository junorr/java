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

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import us.pserver.log.LogLevel;
import static us.pserver.log.conf.XmlClass.CLASS;
import us.pserver.log.impl.LevelEntry;
import us.pserver.log.impl.LogLevels;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 09/07/2015
 */
public abstract class XmlObject extends XmlClass {
  
  public static final String NAME = "name";
  
  public static final String LEVEL = "level";

  LogLevels levels;
  
  String name;
  
  
  public XmlObject(Class type, String name) {
    super(type);
    if(name == null)
      throw new IllegalArgumentException("Invalid name: "+ name);
    levels = new LogLevels();
    this.name = name;
  }
  
  
  public String getName() {
    return name;
  }
  
  
  public LogLevels levels() {
    return levels;
  }
  
  
  public static XmlObject from(Element elt) throws ClassNotFoundException {
    if(elt == null)
      throw new IllegalArgumentException("Invalid XmlObject Element: "+ elt);
    NodeList nl = elt.getElementsByTagName(CLASS);
    if(nl.getLength() < 1)
      throw new IllegalArgumentException("Element does not contains Class node");
    Node nc = nl.item(0);
    XmlObject xo = new XmlObject(
        Class.forName(nc.getTextContent()), 
        elt.getAttribute(NAME)
    ){};
    nl = elt.getElementsByTagName(LEVEL);
    if(nl.getLength() > 0) {
      nl = nl.item(0).getChildNodes();
      for(int i = 0; i < nl.getLength(); i++) {
        Node n = nl.item(i);
        if(LogLevel.DEBUG.name()
            .equalsIgnoreCase(n.getNodeName())) {
          xo.levels().setLevelEnabled(
              LogLevel.DEBUG, 
              Boolean.parseBoolean(n.getTextContent())
          );
        } else if(LogLevel.INFO.name()
            .equalsIgnoreCase(n.getNodeName())) {
          xo.levels().setLevelEnabled(
              LogLevel.INFO, 
              Boolean.parseBoolean(n.getTextContent())
          );
        } else if(LogLevel.WARN.name()
            .equalsIgnoreCase(n.getNodeName())) {
          xo.levels().setLevelEnabled(
              LogLevel.WARN, 
              Boolean.parseBoolean(n.getTextContent())
          );
        } else {
          xo.levels().setLevelEnabled(
              LogLevel.ERROR, 
              Boolean.parseBoolean(n.getTextContent())
          );
        }
      }
    }
    return xo;
  }
  
  
  public Element createElement(Document doc, String element) {
    if(doc == null || element == null)
      return null;
    Element e = doc.createElement(element);
    e.setAttribute(NAME, name);
    e.appendChild(super.createElement(doc));
    Element elv = null;
    for(LevelEntry le : levels.entries()) {
      if(le.isEnabled()) {
        if(elv == null) {
          elv = doc.createElement(LEVEL);
        }
        Element l = doc.createElement(le.level().name().toLowerCase());
        l.appendChild(doc.createTextNode(Boolean.TRUE.toString()));
        elv.appendChild(l);
      }
    }
    if(elv != null) e.appendChild(elv);
    return e;
  }


  @Override
  public String toString() {
    return "XmlObject{" + "class=" + type.getName() + ", name=" + name + '}';
  }
  
}
