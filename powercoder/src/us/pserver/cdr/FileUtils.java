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

package us.pserver.cdr;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import static us.pserver.cdr.Checker.nullarg;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 20/06/2014
 */
public class FileUtils {
  
  public static final int BUFFER = 4096;

  
  public static Path path(String str) {
    return Paths.get(str);
  }
  
  
  public static StandardOpenOption[] optionsRead() {
    StandardOpenOption[] opts = new StandardOpenOption[1];
    opts[0] = StandardOpenOption.READ;
    return opts;
  }
  
  
  public static StandardOpenOption[] optionsWrite() {
    StandardOpenOption[] opts = new StandardOpenOption[1];
    opts[0] = StandardOpenOption.WRITE;
    return opts;
  }
  
  
  public static StandardOpenOption[] optionsWriteCreate() {
    StandardOpenOption[] opts = new StandardOpenOption[2];
    opts[0] = StandardOpenOption.WRITE;
    opts[1] = StandardOpenOption.CREATE;
    return opts;
  }
  
  
  public static InputStream inputStream(Path path) throws IOException {
    return Files.newInputStream(path, optionsRead());
  }
  
  
  public static OutputStream outputStream(Path path) throws IOException {
    return Files.newOutputStream(path, optionsWriteCreate());
  }
  
  
  public static long transfer(InputStream in, OutputStream out) throws IOException {
    nullarg(InputStream.class, in);
    nullarg(OutputStream.class, out);
    
    long total = 0;
    byte[] buf = new byte[BUFFER];
    int read = 0;
    while((read = in.read(buf)) > 0) {
      total += read;
      out.write(buf, 0, read);
    }
    out.flush();
    return total;
  }
  
}
