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
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import static us.pserver.xprops.XmlStream.XHEADER;
import us.pserver.xprops.util.Valid;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 13/07/2015
 */
public class XmlFile extends XmlStream {
  
  private final File file;
  

  public XmlFile(String file) {
    this(new File(Valid.off(file)
        .getOrFail("Invalid File Name: "))
    );
  }
  
  
  public XmlFile(File file) {
    super(createFileInput(file));
    this.file = file;
  }
  
  
  public XmlFile(String file, XTag root) {
    super(Valid.off(root).getOrFail(XTag.class));
    this.file = new File(file);
  }
  
  
  public XmlFile(File file, XTag root) {
    super(Valid.off(root).getOrFail(XTag.class));
    this.file = file;
  }
  
  
  public static InputStream createFileInput(File f) {
    try {
      return Files.newInputStream(
          Valid.off(f).getOrFail(File.class).toPath(),
          StandardOpenOption.READ,
          StandardOpenOption.CREATE
      );
    }
    catch(IOException e) {
      throw new IllegalArgumentException(e.getLocalizedMessage(), e);
    }
  }
  
  
  public boolean save() throws IOException {
    if(root == null) return false;
    PrintStream ps = new PrintStream(file);
    ps.println(XHEADER);
    if(root == null && input != null) {
      Valid.off(this.read()).testNull(XTag.class);
    }
    ps.println(root.toXml());
    ps.flush();
    ps.close();
    return true;
  }
  
}
