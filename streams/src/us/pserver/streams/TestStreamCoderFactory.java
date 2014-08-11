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
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import us.pserver.cdr.crypt.CryptAlgorithm;
import us.pserver.cdr.crypt.CryptKey;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 01/08/2014
 */
public class TestStreamCoderFactory {

  
  public static void main(String[] args) throws IOException {
    // --ENCODE --
    Path pi = IO.p("c:/.local/splash.png");
    Path po = IO.p("c:/.local/splash.enc");
    
    InputStream is = IO.is(pi);
    OutputStream os = IO.os(po);
    
    CryptKey k = CryptKey.createRandomKey(CryptAlgorithm.DESede_CBC_PKCS5);
    
    os = StreamCoderFactory.getNew()
        .setGZipCoderEnabled(true)
        .setCryptCoderEnabled(true, k)
        .setBase64CoderEnabled(true)
        .create(os);
    
    IO.tc(is, os);
    os.close();
    System.out.println("* Done!");
    
    // --DECODE --
    pi = IO.p("c:/.local/splash.enc");
    po = IO.p("c:/.local/splash-d.png");
    
    is = IO.is(pi);
    os = IO.os(po);
    
    is = StreamCoderFactory.get().create(is);
    
    IO.tc(is, os);
    System.out.println("* Done!");
  }
  
}
