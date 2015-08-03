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
import static us.pserver.xprops.XInputStream.XHEADER;
import us.pserver.xprops.util.Validator;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 13/07/2015
 */
public class XFile extends XInputStream {
  
  private final File file;
  

  public XFile(String file) {
    this(new File(Validator.off(file)
        .forEmpty()
        .getOrFail("Invalid File Name: "))
    );
  }
  
  
  public XFile(File file) {
    super(createFileInput(file));
    this.file = file;
  }
  
  
  public XFile(String file, XTag root) {
    super(Validator.off(root).forNull().getOrFail(XTag.class));
    this.file = new File(file);
  }
  
  
  public XFile(File file, XTag root) {
    super(Validator.off(root).forNull().getOrFail(XTag.class));
    this.file = file;
  }
  
  
  public static InputStream createFileInput(File f) {
    try {
      return Files.newInputStream(
          Validator.off(f).forNull().getOrFail(File.class).toPath(),
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
      Validator.off(this.read()).forNull().fail(XTag.class);
    }
    ps.println(root.toXml());
    ps.flush();
    ps.close();
    return true;
  }
  
}
