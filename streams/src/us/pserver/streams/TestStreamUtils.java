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

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 07/07/2014
 */
public class TestStreamUtils {

  
  public static void main(String[] args) throws IOException {
    ByteArrayInputStream bis = 
        new ByteArrayInputStream((
          "--9051914041544843365972754266\n" +
          "Content-Type: text/xml\n" +
          "\n" +
          "<xml><rob enc='basic'>hULofWh0RwY26BNk6oGTJ1cTN2pOxzOoN+m8bfpD9gI=</rob></xml>\nEOF").getBytes());
    System.out.println("* content = "+ bis.available());
    
    System.out.println("_");
    StreamResult sr = StreamUtils.transferBetween(bis, System.out, "<rob enc='basic'>", "</rob>");
    System.out.println("_");
    //long total = StreamUtils.transfer(bis, System.out);
    
    System.out.println("* total = "+ sr.getSize());
  }
  
}
