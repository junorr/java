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
import us.pserver.tools.Valid;
import static us.pserver.xprops.XInputStream.XHEADER;

/**
 * Represents a xml file that can be created or readed, from or to a xml tag.
 * @author Juno Roesler - juno@pserver.us
 */
public class XFile extends XInputStream {
  
  private final File file;
  

  /**
   * Constructor which receives the file name.
   * @param file File name.
   */
  public XFile(String file) {
    this(new File(Valid.off(file)
        .forEmpty()
        .getOrFail("Invalid File Name: "))
    );
  }
  
  
  /**
   * Constructor which receives the file.
   * @param file The xml file.
   */
  public XFile(File file) {
    super(createFileInput(file));
    this.file = file;
  }
  
  
  /**
   * Constructor which receive the file name and the root xml 
   * tag to be rendered as the file content.
   * @param file File name
   * @param root XTag
   */
  public XFile(String file, XTag root) {
    super(Valid.off(root).forNull().getOrFail(XTag.class));
    this.file = new File(file);
  }
  
  
  /**
   * Constructor which receives the file and the root xml 
   * tag to be rendered as the file content.
   * @param file File
   * @param root XTag
   */
  public XFile(File file, XTag root) {
    super(Valid.off(root).forNull().getOrFail(XTag.class));
    this.file = file;
  }
  
  
  private static InputStream createFileInput(File f) {
    try {
      return Files.newInputStream(Valid.off(f).forNull().getOrFail(File.class).toPath(),
          StandardOpenOption.READ,
          StandardOpenOption.CREATE
      );
    }
    catch(IOException e) {
      throw new IllegalArgumentException(e.getLocalizedMessage(), e);
    }
  }
  
  
  /**
   * Save the xml file content.
   * @throws IOException In case of error saving the file.
   */
  public void save() throws IOException {
    if(root == null) return;
    PrintStream ps = new PrintStream(file);
    ps.printf(XHEADER, root.toXml());
    ps.flush();
    ps.close();
  }
  
}
