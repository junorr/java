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
import us.pserver.log.impl.LevelEntry;
import us.pserver.log.impl.LogLevels;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 10/07/2015
 */
public class XmlLevels {

  public static final String LEVEL = "level";

  private LogLevels levels;
  
  
  public XmlLevels() {
    levels = new LogLevels();
  }
  
  
  public LogLevels levels() {
    return levels;
  }
  
  
  public static XmlLevels from(Node node) throws ClassNotFoundException {
    if(node == null || !LEVEL.equals(node.getNodeName())) {
      throw new IllegalArgumentException("Invalid XmlLevels Node: "+ node);
    }
    XmlLevels xl = new XmlLevels();
    NodeList nl = node.getChildNodes();
    for(int i = 0; i < nl.getLength(); i++) {
      Node n = nl.item(i);
      if(LogLevel.DEBUG.name()
          .equalsIgnoreCase(n.getNodeName())) {
        xl.levels().setLevelEnabled(
            LogLevel.DEBUG, 
            Boolean.parseBoolean(n.getTextContent())
        );
      } else if(LogLevel.INFO.name()
          .equalsIgnoreCase(n.getNodeName())) {
        xl.levels().setLevelEnabled(
            LogLevel.INFO, 
            Boolean.parseBoolean(n.getTextContent())
        );
      } else if(LogLevel.WARN.name()
          .equalsIgnoreCase(n.getNodeName())) {
        xl.levels().setLevelEnabled(
            LogLevel.WARN, 
            Boolean.parseBoolean(n.getTextContent())
        );
      } else if(LogLevel.ERROR.name()
          .equalsIgnoreCase(n.getNodeName())) {
        xl.levels().setLevelEnabled(
            LogLevel.ERROR, 
            Boolean.parseBoolean(n.getTextContent())
        );
      } else {
        continue;
      }
    }
    return xl;
  }
  
  
  public Element createElement(Document doc) {
    if(doc == null) return null;
    if(!levels.isAnyLevelEnabled()) return null;
    Element e = doc.createElement(LEVEL);
    for(LevelEntry le : levels.entries()) {
      if(le.isEnabled()) {
        Element l = doc.createElement(le.level().name().toLowerCase());
        l.appendChild(doc.createTextNode(Boolean.TRUE.toString()));
        e.appendChild(l);
      }
    }
    return e;
  }
  
  
  @Override
  public String toString() {
    return levels.toString();
  }

}
