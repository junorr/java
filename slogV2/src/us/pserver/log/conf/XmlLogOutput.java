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

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import us.pserver.log.impl.LevelEntry;
import us.pserver.log.output.LogOutput;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 09/07/2015
 */
public class XmlLogOutput extends XmlObject {
  
  public static final String OUTPUT = "output";

  
  public XmlLogOutput(Class<? extends LogOutput> type, String name) {
    super(type, name);
  }
  
  
  @Override
  public LogOutput create() throws InstantiationException {
    LogOutput out = super.create();
    for(LevelEntry e : levels.entries()) {
      out.setLevelEnabled(e.level(), e.isEnabled());
    }
    return out;
  }
  
  
  public static XmlLogOutput from(LogOutput out, String name) {
    if(out == null) return null;
    XmlLogOutput xout = new XmlLogOutput(out.getClass(), name);
    for(LevelEntry e : out.levels().entries()) {
      xout.levels()
          .setLevelEnabled(e.level(), e.isEnabled());
    }
    return xout;
  }
  
  
  public static XmlLogOutput from(Element elt) throws ClassNotFoundException {
    if(elt == null || !elt.getTagName().equals(OUTPUT))
      throw new IllegalArgumentException("Invalid XmlLogOutput Element: "+ elt);
    NodeList nl = elt.getElementsByTagName(CLASS);
    if(nl.getLength() < 1)
      throw new IllegalArgumentException("Element does not contains Class node");
    Node nc = nl.item(0);
    try {
      XmlLogOutput xout = new XmlLogOutput(
          (Class<? extends LogOutput>) Class.forName(nc.getNodeValue()), 
          elt.getAttribute(NAME)
      );
    } catch(ClassNotFoundException e) {
      return null;
    }
  }
  
  
  @Override
  public Element createElement(Document doc) {
    return super.createElement(doc, OUTPUT);
  }
  
}
