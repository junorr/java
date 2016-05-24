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

import static us.pserver.insane.Check.isModOf;
import us.pserver.insane.Insane;
import static us.pserver.insane.Check.isBetween;
import static us.pserver.insane.Check.isGreaterThan;
import static us.pserver.insane.Check.isLesserThan;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 23/05/2016
 */
public class TestNumberInsane {

  
  public static void main(String[] args) throws NoSuchFieldException {
    int i = 5;
    System.out.println(Insane.of(i, NoSuchFieldException.class).get(isBetween(1, 10)));
    System.out.println(Insane.of(i, NoSuchFieldException.class).with(isBetween(1, 10)).and(isLesserThan(6)).and(isGreaterThan(4)).get());
    System.out.println(Insane.of(8, NoSuchFieldException.class).not(isBetween(-1, 1)).and(isGreaterThan(10).negate()).get());
    System.out.println(Insane.of(15, NoSuchFieldException.class).not(isGreaterThan(10)).and(isModOf(5)).get());
  }
  
}
