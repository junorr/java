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

package us.pserver.tools.exp;

import java.util.function.Consumer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 01/02/2018
 */
public class TestIf {

  private final int num = (int) (Math.random() * 10);
  
  
  @Test
  public void ifConsumerThen() {
    IfCs.<Integer>of(n->n > 5)
        .then((Consumer<Integer>)n->System.out.println("* Number is greater than 5: "+ n))
        .elseDo((Consumer<Integer>)n->System.out.println("* Number is lesser than 5: "+ n))
        .eval(num);
  }
  
  
  @Test
  public void ifFunctionThen() {
    int num = 8;
    Assertions.assertEquals("* Number is greater than 5: "+ num, 
        IfImpl.<Integer,String>of(n->n > 5)
            .then(n->"* Number is greater than 5: "+ n)
            .elseDo(n->"* Number is lesser than 5: "+ n)
            .eval(num)
    );
  }
  
  
  @Test
  public void ifConsumerElse() {
    IfCs.<Integer>of(n->n > 5)
        .then((Consumer<Integer>)n->System.out.println("* Number is greater than 5: "+ n))
        .elseDo((Consumer<Integer>)n->System.out.println("* Number is lesser than 5: "+ n))
        .eval(num);
  }
  
  
  @Test
  public void ifFunctionElse() {
    int num = 3;
    Assertions.assertEquals("* Number is lesser than 5", 
        IfImpl.<Integer,String>of(n->n > 5)
            .then(n->"* Number is greater than 5")
            .elseDo(n->"* Number is lesser than 5")
            .eval(num)
    );
  }
  
}
