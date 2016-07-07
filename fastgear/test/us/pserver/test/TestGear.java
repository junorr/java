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

package us.pserver.test;

import java.util.function.Function;
import us.pserver.fastgear.Gear;
import us.pserver.fastgear.Running;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 03/06/2016
 */
public class TestGear {

  
  public static void main(String[] args) throws InterruptedException {
    Running<String,Integer> run = Gear.<Integer,String>of((Function)
        i->String.valueOf(" - "+ i+ " - ")
    ).start();
    run.input().onAvailable(System.out::println);
    for(int i = 0; i < 5; i++) {
      System.out.println("* writing: "+ i);
      run.output().push(i);
      try { Thread.sleep(1000); }
      catch(InterruptedException e) {}
    }
    System.out.println("* run.suspend(2000)...");
    run.gear().suspend(2000);
    for(int i = 0; i < 3; i++) {
      System.out.println("* writing: "+ i);
      run.output().push(i);
      //try { Thread.sleep(2000); }
      //catch(InterruptedException e) {}
    }
    //System.out.println("* run.resume()...");
    //run.gear().resume();
    for(int i = 0; i < 3; i++) {
      System.out.println("* writing: "+ i);
      run.output().push(i);
      try { Thread.sleep(1000); }
      catch(InterruptedException e) {}
    }
    run.output().closeOnEmpty();
    run.gear().join();
  }
  
}
