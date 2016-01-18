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

package us.pserver.streams.test;

import java.io.IOException;
import us.pserver.streams.PushbackInputStream;
import us.pserver.streams.deprecated.SearchableInputStream;
import us.pserver.streams.SequenceInputStream;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 19/06/2015
 */
public class TestSearchableInputStream {

  
  public static void main(String[] args) throws IOException {
    PushbackInputStream pin = new PushbackInputStream(
        new SequenceInputStream(100)
    );
    byte[] stop = {88, 89, 90};
    System.out.println("* pin.available="+ pin.available());
    SearchableInputStream sin = new SearchableInputStream(pin, stop);
    int read = 0;
    System.out.println("* Reading...");
    while(true) {
      read = sin.read();
      if(read == -1) break;
      System.out.println(".: "+ read);
    }
    System.out.println("* Done!");
    System.out.println("* sin.isEOF()..: "+ sin.isEOF());
    System.out.println("* sin.isFound(): "+ sin.isFound());
    while(pin.hasUnreadAvailable()) {
      read = pin.read();
      if(read == -1) break;
      System.out.println(".: "+ read);
    }
  }
  
}
