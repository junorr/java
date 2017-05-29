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

package us.pserver.jose.test;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import us.pserver.jose.store.UID;
import us.pserver.tools.timer.Timer;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 16/03/2017
 */
public class TestUID {

  public static void main(String[] args) {
    List<Date> ls = new LinkedList<>();
    long time = System.currentTimeMillis();
    System.out.println("* time = ["+ time+ "]");
    ls.add(new Date(time));
    ls.add(new Date((time += 300000)));
    ls.add(new Date((time += 300000)));
    ls.add(new Date((time += 300000)));
    System.out.println(ls);
    Timer tm = new Timer.Nanos().start();
    UID uid = UID.of(ls);
    System.out.println(uid);
    System.out.println(tm.stop());
    
    System.out.println("------------------------");
    
    ls.set(2, new Date((time += 600000)));
    System.out.println(ls);
    tm.clear().start();
    uid = UID.of(ls);
    System.out.println(uid);
    System.out.println(tm.stop());
    
    System.out.println("------------------------");
    
    Date d = new Date(1489686824174L);
    System.out.println(Objects.toString(d));
    tm.clear().start();
    uid = UID.of(d);
    System.out.println(uid);
    System.out.println(tm.stop());
    
    System.out.println("------------------------");
    
    int i = 5;
    System.out.println(Objects.toString(5));
    tm.clear().start();
    uid = UID.of(i);
    System.out.println(uid);
    System.out.println(tm.stop());
  }
  
}
