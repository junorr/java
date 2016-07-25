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

package us.pserver.timer.test;

import java.util.Arrays;
import us.pserver.timer.Timer;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 29/06/2015
 */
public class TestTimerNanos {

  
  public static void main(String[] args) {
    Timer.Nanos tm = new Timer.Nanos();
    String s1 = "0";
    String s2 = "01234";
    String s3 = "0123456789";
    tm.start();
    System.out.println(s1);
    System.out.flush();
    tm.lap();
    System.out.println(s2);
    System.out.flush();
    tm.lap();
    System.out.println(s3);
    System.out.flush();
    tm.lapAndStop();
    
    System.out.println("* "+ tm);
    System.out.println("* laps: "+ Arrays.toString(tm.lapsElapsedFromLast().lapsToMillis().toArray()));
    System.out.println("* laps: "+ Arrays.toString(tm.lapsElapsedFromStart().lapsToMillis().toArray()));
  }
  
}
