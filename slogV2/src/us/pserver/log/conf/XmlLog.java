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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import us.pserver.log.Log;
import us.pserver.log.format.PatternOutputFormatter;
import us.pserver.log.output.LogOutput;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 09/07/2015
 */
public class XmlLog extends XmlObject {
  
  public static final String LOG = "log";

  private List<XmlLogOutput> outputs;
  
  private PatternOutputFormatter pattern;
  
  
  public XmlLog(Class<? extends Log> type, String name) {
    super(type, name);
    outputs = new ArrayList<XmlLogOutput>();
  }
  
  
  public List<XmlLogOutput> outputs() {
    return outputs;
  }
  
  
  protected Constructor findStringConstructor() {
    try {
      return type.getDeclaredConstructor(String.class);
    } catch(NoSuchMethodException e) {
      return null;
    }
  }
  
  
  public XmlLog setPatternFormatter(String spat) {
    if(spat != null) {
      pattern = new PatternOutputFormatter(spat);
    }
    return this;
  }
  
  
  public PatternOutputFormatter getPatternFormatter() {
    return pattern;
  }
  
  
  @Override
  public Log create() throws InstantiationException {
    Constructor c = findEmptyConstructor();
    if(c == null) {
      c = findStringConstructor();
    }
    if(c == null) {
      throw new InstantiationException(
          "Class <"+ type.getName()+ "> has no valid constructors"
      );
    }
    Log log = (Log) (c.getParameterCount() > 0 
        ? create(c, name) : create());
    log.levels().copyFrom(levels());
    if(pattern != null) {
      log.setOutputFormatter(pattern);
    }
    if(outputs.isEmpty()) return log;
    for(XmlLogOutput xl : outputs) {
      log.put(xl.getName(), xl.create());
    }
    return log;
  }
  
  
  @Override
  public Element createElement(Document doc) {
    if(doc == null) return null;
    Element elt = super.createElement(doc, LOG);
    if(pattern != null) {
      Element ep = doc.createElement("pattern");
      ep.appendChild(doc.createTextNode(pattern.getPattern()));
      elt.appendChild(ep);
    }
    if(outputs.isEmpty()) return elt;
    for(XmlLogOutput xl : outputs) {
      elt.appendChild(xl.createElement(doc));
    }
    return elt;
  }
  
  
  public static XmlLog from(Node node) throws ClassNotFoundException {
    if(node == null) return null;
    XmlObject xo = XmlObject.from(node);
    XmlLog xlog = new XmlLog(xo.classType(), xo.getName());
    xlog.levels().copyFrom(xo.levels());
    NodeList nl = node.getChildNodes();
    if(nl.getLength() < 1) return xlog;
    for(int i = 0; i < nl.getLength(); i++) {
      Node n = nl.item(i);
      if(XmlLogOutput.OUTPUT.equals(n.getNodeName())) {
        xlog.outputs().add(XmlLogOutput.from(n));
      }
    }
    Node npat = XmlObject.find(node, "pattern");
    if(npat == null) return xlog;
    xlog.setPatternFormatter(npat.getTextContent());
    return xlog;
  }
  
  
  public static XmlLog from(Log log) {
    if(log == null) return null;
    XmlLog x = new XmlLog(log.getClass(), log.getLogName());
    x.levels().copyFrom(log.levels());
    if(log.outputsMap().isEmpty())
      return x;
    for(Map.Entry<String, LogOutput> e : log.outputsMap().entrySet()) {
      x.outputs().add(XmlLogOutput.from(e.getValue(), e.getKey()));
    }
    return x;
  }
  

  @Override
  public String toString() {
    return "XmlLog{" + "class=" + type.getName() + ", name=" + name + ", levels="+ levels+ ", outputs="+ outputs+ '}';
  }
  
}
