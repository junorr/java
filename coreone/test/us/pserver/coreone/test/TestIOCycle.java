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

package us.pserver.coreone.test;

import us.pserver.coreone.Core;
import us.pserver.coreone.Duplex;
import us.pserver.tools.Sleeper;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 13/10/2017
 */
public class TestIOCycle {

  
  public static void main(String[] args) {
    Duplex<String,Object> du1 = Core.cycle(i->{return String.format("1>>> %d <<<1", i);}).start();
    Duplex<String,Object> du2 = Core.cycle(i->{return String.format("2>>> %d <<<2", i);}).start();
    
    du1.cycle().suspend(2000);
    for(int i = 0; i < 5; i++) {
      du1.output().push(i);
      du2.output().push(i);
      System.out.println(du1.input().pull());
      System.out.println(du2.input().pull());
      Sleeper.of(1000).sleep();
    }
  }
  
}
