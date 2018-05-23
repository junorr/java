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

package us.pserver.filex;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 23/05/2018
 */
public class TestByteSearch {

  private static final ByteBuffer search = StandardCharsets.UTF_8.encode("is not");
  
  private static final ByteBuffer content = StandardCharsets.UTF_8.encode("The i world is not enought");
  
  private static int find(ByteBuffer search, ByteBuffer content) {
    int spos = search.position();
    int size = search.remaining();
    int count = 0;
    while(content.hasRemaining() && count != size) {
      if(content.get() == search.get()) {
        count++;
      }
      else {
        count = 0;
        search.position(spos);
      }
    }
    return count > 0 ? content.position() - count : -1;
  }
  
  
  public static void main(String[] args) {
    System.out.printf("* find( %s, %s ): %d", search, content, find(search, content));
  }
  
}
