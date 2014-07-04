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

package us.pserver.streams;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.commons.codec.digest.Crypt;
import us.pserver.cdr.crypt.CryptAlgorithm;
import us.pserver.cdr.crypt.CryptKey;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 02/07/2014
 */
public class TestMegaBuffer {

  public static Path path(String str) {
    return Paths.get(str);
  }
  
  public static void main(String[] args) throws IOException {
    MegaBuffer buf = new MegaBuffer();
    CryptKey key = new CryptKey("123456", CryptAlgorithm.DESede_ECB_PKCS5);
    
    System.out.println("* buf.readEncoding()");
    buf.setCryptCoderEnabled(true, key)
        .readEncoding(path("d:/pic.jpg"));
    System.out.println("* buf.isAnyCoderEnabled(): "+ buf.isAnyCoderEnabled());
    System.out.println("* buf.size() = "+ buf.size());
    
    System.out.println("* buf.write()");
    buf.write(path("d:/pic.cry"));
    
    System.out.println("* buf.reset()");
    buf.reset();
    System.out.println("* buf.isAnyCoderEnabled(): "+ buf.isAnyCoderEnabled());
    
    System.out.println("* buf.readDecoding()");
    buf.setCryptCoderEnabled(true, key);
    buf.readDecoding(path("d:/pic.cry"));
    System.out.println("* buf.size() = "+ buf.size());
    
    System.out.println("* buf.write()");
    buf.write(path("d:/pic-mb.jpg"));
    buf.close();
  }
  
}
