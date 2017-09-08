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

package test.nitrite;

import org.dizitart.no2.Nitrite;
import org.dizitart.no2.objects.ObjectRepository;
import org.dizitart.no2.objects.filters.ObjectFilters;
import us.pserver.tools.timer.Timer;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 07/09/2017
 */
public class TestRecover {

  
  public static void main(String[] args) {
    Timer tm = new Timer.Nanos().start();
    Nitrite db = Nitrite.builder()
        .filePath("/storage/java/nitrite.db")
        .openOrCreate("hello", "world");
    System.out.println("* Open db time: "+ tm.lapAndStop());
    tm = new Timer.Nanos().start();
    ObjectRepository<User> repo = db.getRepository(User.class);
    System.out.println("* Open repo time: "+ tm.lapAndStop());
    tm = new Timer.Nanos().start();
    User user = repo.find(ObjectFilters.eq("name", "juno")).firstOrDefault();
    System.out.println("* find id time: "+ tm.lapAndStop());
    System.out.println(user);
    db.commit();
    repo.close();
    db.compact();
    db.close();
  }
  
}
