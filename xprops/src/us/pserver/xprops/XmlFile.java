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

package us.pserver.xprops;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Stack;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;
import us.pserver.xprops.util.Valid;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 13/07/2015
 */
public class XmlFile {
  
  public static final String XHEADER = 
      "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
  
  private final File file;
  
  private XTag root;
  
  private final Stack<XTag> stack;


  public XmlFile(String file) {
    this(new File(Valid.off(file)
        .getOrFail("Invalid File Name: "))
    );
  }
  
  
  public XmlFile(File file) {
    this.file = Valid.off(file)
        .getOrFail(File.class);
    stack = new Stack<>();
    root = null;
  }
  
  
  public XmlFile(String file, XTag root) {
    this(file);
    this.root = Valid.off(root)
        .getOrFail(XTag.class);
  }
  
  
  public XmlFile(File file, XTag root) {
    this(file);
    this.root = Valid.off(root)
        .getOrFail(XTag.class);
  }
  
  
  public XTag getRoot() {
    return root;
  }
  
  
  public XTag read() throws IOException {
    try {
      SAXParser par = SAXParserFactory
          .newInstance().newSAXParser();
      stack.clear();
      par.parse(file, new XmlHandler(this));
      return root;
    } catch(Exception e) {
      throw new IOException(e.toString(), e);
    }
  }
  
  
  public boolean save() throws IOException {
    if(root == null) return false;
    PrintStream ps = new PrintStream(file);
    ps.println(XHEADER);
    ps.println(root.toXml());
    ps.flush();
    ps.close();
    return true;
  }
  
  
  private void startElement(String name, Attributes attrs) {
    if(name == null || name.trim().isEmpty())
      return;
    System.out.println("* element="+ name);
    XTag tag = new XTag(name);
    if(root == null) root = tag;
    if(!stack.isEmpty()) {
      stack.peek().addChild(tag);
    }
    stack.push(tag);
    for(int i = 0; i < attrs.getLength(); i++) {
      tag.addNewAttr(
          attrs.getQName(i), 
          attrs.getValue(i)
      );
    }
  }
  
  
  private void endElement(String name) {
    stack.pop();
  }
  
  
  private void text(String txt) {
    if(txt == null || txt.trim().isEmpty())
      return;
    System.out.println("* text="+ txt);
    stack.peek().addNewValue(txt);
  }
  
  
  
  private class XmlHandler extends DefaultHandler {
    
    private final XmlFile xfile;
    
    public XmlHandler(final XmlFile xf) {
      this.xfile = xf;
    }
    
    @Override
    public void startElement(String uri, String lname, String qname, Attributes attrs) {
      xfile.startElement(qname, attrs);
    }
    
    @Override
    public void endElement(String uri, String lname, String qname) {
      xfile.endElement(qname);
    }
    
    @Override
    public void characters(char[] ch, int start, int len) {
      xfile.text(new String(ch, start, len));
    }
    
  }

}
