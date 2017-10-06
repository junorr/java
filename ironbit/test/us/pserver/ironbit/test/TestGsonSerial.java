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

import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;
import us.pserver.ironbit.IronbitException;
import us.pserver.tools.UTF8String;
import us.pserver.tools.timer.Timer;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 05/10/2017
 */
public class TestGsonSerial {
  
  
  public static List<Integer> getList(int size) {
    List<Integer> ls = new LinkedList<>();
    for(int i = 0; i < size; i++) {
      ls.add((int)(Math.random() * 100 + i));
    }
    return ls;
  }
  
  
  public static List<Integer> exec(List<Integer> list) throws IOException, ClassNotFoundException {
    Gson gson = new Gson();
    Timer tm = new Timer.Nanos().start();
    byte[] bs = UTF8String.from(gson.toJson(list)).getBytes();
    System.out.printf("-- time to   serialize list of %d: %s --%n", list.size(), tm.stop());
    System.out.println("* serialSize="+ bs.length);
    tm.clear().start();
    List<Integer> l = (List<Integer>) gson.fromJson(UTF8String.from(bs).toString(), List.class);
    System.out.printf("-- time to DEserialize list of %d: %s --%n", l.size(), tm.stop());
    System.out.println(l);
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
  
  
  public static void main(String[] args) throws IOException, ClassNotFoundException {
    List ls = getList(20000);
    System.out.println("* warming up 100x...");
    disableStdOut();
    for(int i = 0; i < 10; i++) {
      exec(ls);
    }
    enableStdOut();
    for(int i = 0; i < 5; i++) {
      exec(ls);
    }
  }
  
}
