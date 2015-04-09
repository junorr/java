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
import us.pserver.cdr.crypt.CryptAlgorithm;
import us.pserver.cdr.crypt.CryptKey;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 16/07/2014
 */
public class TestMultiCoderBuffer {

  
  public static void main(String[] args) throws IOException {
    MultiCoderBuffer mb = new MultiCoderBuffer();
    
    Path p1 = Paths.get("/storage/pic.jpg");
    Path p2 = Paths.get("/storage/pic.enc");
    Path p3 = Paths.get("/storage/pic-dec.jpg");
    
    CryptKey key = CryptKey.createRandomKey(CryptAlgorithm.AES_CBC_PKCS5);
    
    mb.load(p1)
        .setGZipCoderEnabled(true)
        //.setCryptCoderEnabled(true, key)
        //.setBase64CoderEnabled(true)
        //.setLzmaCoderEnabled(true)
        .encode().save(p2);
    
    mb.reset().load(p2)
        //.setLzmaCoderEnabled(true)
        //.setBase64CoderEnabled(true)
        //.setCryptCoderEnabled(true, key)
        .setGZipCoderEnabled(true)
        .decode().save(p3);
  }
  
}
