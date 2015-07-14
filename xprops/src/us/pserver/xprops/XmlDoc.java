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
import java.nio.file.Files;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import us.pserver.xprops.util.Valid;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 13/07/2015
 */
public class XmlDoc extends XTag {
  
  private final File file;


  public XmlDoc(String value) {
    super(value);
    file = new File(value);
  }
  
  
  public XmlDoc(File file) {
    super(Valid.off(file).getOrFail(
        File.class).getAbsolutePath()
    );
    this.file = file;
  }
  
  
  public XmlDoc off(String file) {
    XmlDoc xd = new XmlDoc(Valid.off(file)
        .getOrFail("Invalid File Name: ")
    );
    if(!this.childs().isEmpty()) {
      xd.childs().addAll(this.childs());
    }
    return xd;
  }

  
  public XmlDoc off(File file) {
    XmlDoc xd = new XmlDoc(Valid.off(file)
        .getOrFail(File.class)
    );
    if(!this.childs().isEmpty()) {
      xd.childs().addAll(this.childs());
    }
    return xd;
  }
  
  
  public void read() throws IOException {
    XMLInputFactory fact = XMLInputFactory.newFactory();
    try {
      XMLStreamReader reader = fact
          .createXMLStreamReader(
              Files.newBufferedReader(file.toPath()));
      String tagCont = null;
      XTag curtag = null;
      while(reader.hasNext()) {
        int evt = reader.next();
        switch(evt) {
          case XMLStreamConstants.START_ELEMENT:
            curtag
        }
      }
    } catch(XMLStreamException e) {
      throw new IOException(e.toString(), e);
    }
  }
  
  
  public void save() throws IOException {
    
  }

}
