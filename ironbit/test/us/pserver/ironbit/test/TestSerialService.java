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

package us.pserver.ironbit.test;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import us.pserver.ironbit.IronbitConfiguration;
import us.pserver.ironbit.IronbitException;
import us.pserver.ironbit.SerialService;
import us.pserver.ironbit.record.SerialRecord;
import us.pserver.tools.timer.Timer;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 05/10/2017
 */
public class TestSerialService {
  
  
  public static List<Integer> getList(int size) {
    List<Integer> ls = new ArrayList<>();
    for(int i = 0; i < size; i++) {
      ls.add((int)(Math.random() * 100 + i));
    }
    return ls;
  }
  
  
  public static List<Integer> exec(List<Integer> list, SerialService<List> ser) {
    Timer tm = new Timer.Nanos().start();
    SerialRecord rec = ser.serialize(list);
    System.out.printf("-- time to   serialize list of %d: %s --%n", list.size(), tm.stop());
    tm.clear().start();
    List<Integer> l = ser.deserialize(rec);
    System.out.printf("-- time to DEserialize list of %d: %s --%n", l.size(), tm.stop());
    return l;
  }

  
  public static List<Integer> exec(List<Integer> list) {
    Timer tm = new Timer.Nanos().start();
    Optional<SerialService<List>> op = IronbitConfiguration.get().findSerialService(list.getClass());
    SerialRecord rec = op.get().serialize(list);
    System.out.printf("-- time to   serialize list of %d: %s --%n", list.size(), tm.stop());
    //System.out.println(rec);
    tm.clear().start();
    List<Integer> l = op.get().deserialize(rec);
    System.out.printf("-- time to DEserialize list of %d: %s --%n", l.size(), tm.stop());
    return l;
  }
  
  
  public static final PrintStream stdout = System.out;
  
  public static final PrintStream nullout = IronbitException.rethrow(()->new PrintStream("/dev/null"));
  
  
  public static void enableStdOut() {
    System.setOut(stdout);
  }
  
  
  public static void disableStdOut() {
    System.setOut(nullout);
  }
  
  
  public static void main(String[] args) {
    List ls = getList(200);
    System.out.println("* warming up 100x...");
    disableStdOut();
    for(int i = 0; i < 100; i++) {
      exec(ls);
    }
    enableStdOut();
    for(int i = 0; i < 5; i++) {
      exec(ls);
    }
    
    Optional<SerialService<List>> op = IronbitConfiguration.get().findSerialService(ls.getClass());
    System.out.println();
    System.out.println("* warming up 100x...");
    disableStdOut();
    for(int i = 0; i < 100; i++) {
      exec(ls, op.get());
    }
    enableStdOut();
    for(int i = 0; i < 5; i++) {
      exec(ls, op.get());
    }
  }
  
}
