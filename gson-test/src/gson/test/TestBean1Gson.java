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

package gson.test;

import com.google.gson.Gson;
import us.pserver.tools.timer.Timer;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 08/07/2016
 */
public class TestBean1Gson {

  
  public static void main(String[] args) {
    Gson gson = new Gson();
    Bean1 b = new Bean1().add(new Bean1().add(new Bean1().add(new Bean1())));
    System.out.println("* b: "+ b);
    Timer tm = new Timer.Nanos().start();
    String json = gson.toJson(b);
    System.out.println("* toJson: "+ tm.stop());
    gson = new Gson();
    System.out.println("* j: "+ json);
    tm = new Timer.Nanos().start();
    b = gson.fromJson(json, Bean1.class);
    System.out.println("* fromJson: "+ tm.stop());
    System.out.println("* b: "+ b);
    System.out.println("* b.lst.class="+ b.lst.getClass());
  }
  
}
