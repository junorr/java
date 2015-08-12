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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Stack;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;
import us.pserver.tools.UTF8String;
import us.pserver.tools.Valid;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 13/07/2015
 */
public class XInputStream {
  
  public static final String XHEADER = 
      "<?xml version=\"1.0\" encoding=\"UTF-8\"?>%n%s%n";
  
  XTag root;
  
  private final Stack<XTag> stack;
  
  InputStream input;


  public XInputStream(InputStream input) {
    this.input = Valid.off(input)
        .forNull()
        .getOrFail(InputStream.class);
    this.stack = new Stack<>();
    this.root = null;
  }
  
  
  public XInputStream(XTag root) {
    this.input = null;
    this.root = Valid.off(root)
        .forNull()
        .getOrFail(XTag.class);
    this.stack = new Stack<>();
  }
  
  
  public XTag getRoot() {
    return root;
  }
  
  
  public XTag read() throws IOException {
    try {
      SAXParser par = SAXParserFactory
          .newInstance().newSAXParser();
      stack.clear();
      par.parse(input, new XmlHandler(this));
      return root;
    } catch(Exception e) {
      throw new IOException(e.toString(), e);
    }
  }
  
  
  public InputStream getInputStream() {
    if(input != null) return input;
    Valid.off(root).forNull().fail("Xml Root Tag is Null: ");
    input = new ByteArrayInputStream(
        UTF8String.format(XHEADER, root.toXml()).getBytes()
    );
    return input;
  }
  
  
  private void startElement(String name, Attributes attrs) {
    if(name == null || name.trim().isEmpty())
      return;
    XTag tag = new XTag(unformat(name));
    if(root == null) root = tag;
    if(!stack.isEmpty()) {
      stack.peek().addChild(tag);
    }
    stack.push(tag);
    for(int i = 0; i < attrs.getLength(); i++) {
      tag.addNewAttr(
          unformat(attrs.getQName(i)), 
          unformat(attrs.getValue(i))
      );
    }
  }
  
  
  private String unformat(String str) {
    if(str == null) return null;
    return str.replace("\r", "").replace("\n", "").trim();
  }
  
  
  private void endElement(String name) {
    stack.pop();
  }
  
  
  private void text(String txt) {
    if(txt == null || txt.trim().isEmpty())
      return;
    //System.out.println("* text="+ txt);
    stack.peek().addNewValue(unformat(txt));
  }
  
  
  
  private class XmlHandler extends DefaultHandler {
    
    private final XInputStream xfile;
    
    public XmlHandler(final XInputStream xf) {
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
