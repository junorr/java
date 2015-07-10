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

package us.pserver.log.test;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import us.pserver.log.Log;
import us.pserver.log.LogLevel;
import us.pserver.log.conf.XmlClass;
import us.pserver.log.conf.XmlLog;
import us.pserver.log.conf.XmlLogOutput;
import us.pserver.log.conf.XmlObject;
import us.pserver.log.impl.SimpleLog;
import us.pserver.log.output.NullOutput;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 09/07/2015
 */
public class TestXmlClassType {
  
  public static void print(Node elt) {
    print(elt, "");
  }
  
  public static void print(Node elt, String ident) {
    System.out.print(ident);
    System.out.print(elt.getNodeName());
    if(elt.getNodeValue() != null) {
      System.out.print(":");
      System.out.print(elt.getNodeValue());
    }
    NamedNodeMap map = elt.getAttributes();
    if(map != null) {
      if(map.getLength() > 0)
        System.out.println("{");
      for(int i = 0; i < map.getLength(); i++) {
        System.out.print(ident+ "  ");
        System.out.println(map.item(i));
      }
    }
    NodeList nl = elt.getChildNodes();
    if(map != null && map.getLength() < 1 && nl.getLength() > 0) 
      System.out.println("{");
    for(int i = 0; i < nl.getLength(); i++) {
      print(nl.item(i), ident+"  ");
    }
    if(nl.getLength() > 0) System.out.print(ident+"}");
    System.out.println();
  }

  
  public static void main(String[] args) throws Exception {
    DocumentBuilderFactory fact = DocumentBuilderFactory.newInstance();
    DocumentBuilder build = fact.newDocumentBuilder();
    Document doc = build.newDocument();
    
    XmlClass xc = new XmlClass(String.class);
    System.out.println(xc);
    Element elt = xc.createElement(doc);
    print(elt);
    xc = XmlClass.from(elt);
    System.out.println(xc);
    
    
    XmlObject xo = new XmlObject(TestXmlClassType.class, "test"){};
    xo.levels().setAllLevelsEnabled(true);
    System.out.println(xo);
    elt = xo.createElement(build.newDocument(), "test");
    print(elt);
    xo = XmlObject.from(elt);
    System.out.println(xo);
    System.out.println(xo.levels().isAnyLevelEnabled());
    
    
    XmlLogOutput xl = new XmlLogOutput(NullOutput.class, "nullout");
    System.out.println(xl);
    elt = xl.createElement(doc);
    print(elt);
    xl = XmlLogOutput.from(elt);
    System.out.println(xl);
    System.out.println(xl.create());
    
    
    XmlLog xlog = new XmlLog(SimpleLog.class, "us.pserver");
    xlog.levels().setLevelEnabled(LogLevel.INFO, true);
    xlog.outputs().add(new XmlLogOutput(NullOutput.class, "nullout"));
    System.out.println(xlog);
    elt = xlog.createElement(doc);
    print(elt);
    xlog = XmlLog.from(elt);
    System.out.println(xlog);
    Log log = xlog.create();
    System.out.println(log.getLogName()+ ": "+ log);
  }
  
}
