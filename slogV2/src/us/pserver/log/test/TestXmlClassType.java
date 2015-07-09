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
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import us.pserver.log.conf.XmlClass;
import us.pserver.log.conf.XmlObject;

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
    NodeList nl = elt.getChildNodes();
    if(nl.getLength() > 0) System.out.println("{");
    for(int i = 0; i < nl.getLength(); i++) {
      print(nl.item(i), ident+"  ");
    }
    if(nl.getLength() > 0) System.out.print(ident+"}");
    System.out.println();
  }

  
  public static void main(String[] args) throws Exception {
    DocumentBuilderFactory fact = DocumentBuilderFactory.newInstance();
    DocumentBuilder build = fact.newDocumentBuilder();
    
    XmlClass xc = new XmlClass(String.class);
    System.out.println(xc);
    Element elt = xc.createElement(build.newDocument());
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
  }
  
}
