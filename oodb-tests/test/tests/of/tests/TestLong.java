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

package tests.of.tests;

import java.nio.ByteBuffer;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 15/12/2016
 */
public class TestLong {

  
  public static void main(String[] args) {
    System.out.println("* Long.MAX_VALUE: "+ Long.MAX_VALUE);
    System.out.println("* Long.MIN_VALUE: "+ Long.MIN_VALUE);
    System.out.println("* MAX + MIN = "+ (Long.MAX_VALUE - Long.MIN_VALUE));
    
    ByteBuffer buf = ByteBuffer.allocate(100);
    System.out.println(buf);
    buf.position(30);
    buf.limit(70);
    System.out.println(buf);
    ByteBuffer bb = buf.slice();
    System.out.println(bb);
  }
  
}
