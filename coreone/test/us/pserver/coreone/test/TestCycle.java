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
import us.pserver.coreone.Cycle;
import us.pserver.coreone.Duplex;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 16/10/2017
 */
public class TestCycle {

  
  public static void main(String[] args) {
    Duplex<String,Object> da = Cycle.of(s->{return String.format("%s: >>> %s <<<", Thread.currentThread().getName(), s);}).start();
    Duplex<String,Object> db = Cycle.of(s->{return String.format("%s: >>> %s <<<", Thread.currentThread().getName(), s);}).start();
    
    da.cycle().suspend(1000);
    da.input().onAvailable(System.out::println);
    da.input().onError(System.err::println);
    db.input().onAvailable(System.out::println);
    db.input().onError(System.err::println);
    for(int i = 1; i <= 20; i++) {
      System.out.println("- pushing: "+ i+ " - "+ da.output().push(i)+ " -");
      db.output().push(i);
    }
    da.close();
    db.close();
    Core.get().waitShutdown();
  }
  
}
