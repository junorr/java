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

package us.pserver.tools;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 11/01/2018
 */
public class TestMatch {

  @Test
  public void matchNullOnStringNull() {
    String str = null;
    Match<String> match = Match.of(str, s->s != null).failWith("Bad null String");
    //System.out.println("* matchNullOnStringNull: "+ match);
    Assertions.assertThrows(IllegalArgumentException.class, ()->
        System.out.println(match.getOrFail()));
  }
  
  @Test
  public void matchEmptyOnStringNull() {
    String str = null;
    Match<String> match = Match.of(str, s->s != null).failWith("Bad null String")
        .and(s->!s.isEmpty()).failWith("Bad empty String");
    //System.out.println("* matchEmptyOnStringNull: "+ match);
    //System.out.println(match.getOrFail());
    Assertions.assertThrows(IllegalArgumentException.class, ()->
        System.out.println(match.getOrFail()));
  }
  
  @Test
  public void matchEmptyOnStringEmpty() {
    String str = "";
    Match<String> match = Match.of(str, s->s != null).failWith("Bad null String")
        .and(s->!s.isEmpty()).failWith("Bad empty String");
    //System.out.println("* matchEmptyOnStringEmpty: "+ match);
    //System.out.println(match.getOrFail());
    Assertions.assertThrows(IllegalArgumentException.class, ()->
        System.out.println(match.getOrFail()));
  }
  
}
