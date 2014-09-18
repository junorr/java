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

package test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64OutputStream;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 20/06/2014
 */
public class Base64StreamTest {

  
  public static long transfer(InputStream in, OutputStream out) throws IOException {
    long total = 0;
    int read = 0;
    byte[] buf = new byte[1024];
    
    while((read = in.read(buf)) > 0) {
      total += read;
      out.write(buf, 0, read);
    }
    return total;
  }
  
  
  public static void main(String[] args) throws IOException {
    Path pi = Paths.get("d:/pic.jpg");
    Path po = Paths.get("d:/pic.b64");
    Path po2 = Paths.get("d:/pic3.jpg");

    InputStream in = Files.newInputStream(pi, StandardOpenOption.READ);
    OutputStream out = Files.newOutputStream(po, StandardOpenOption.WRITE, StandardOpenOption.CREATE);
    Base64OutputStream hout = new Base64OutputStream(out);
    System.out.println("* transferred="+transfer(in, hout));
    hout.close();
    in.close();
    
    in = Files.newInputStream(po, StandardOpenOption.READ);
    out = Files.newOutputStream(po2, StandardOpenOption.WRITE, StandardOpenOption.CREATE);
    Base64InputStream hin = new Base64InputStream(in);
    System.out.println("* transferred="+transfer(hin, out));
    out.close();
    in.close();
  }
  
}
