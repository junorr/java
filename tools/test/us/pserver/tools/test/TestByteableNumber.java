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

package us.pserver.tools.test;

import us.pserver.tools.io.ByteableNumber;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 06/11/2017
 */
public class TestByteableNumber {

  
  public static void main(String[] args) {
    short s = Short.MAX_VALUE;
    ByteableNumber n = ByteableNumber.of(s);
    System.out.printf("%d == %s  (%s)%n", s, n, n.getClass().getSimpleName());
    int i = Integer.MAX_VALUE;
    n = ByteableNumber.of(i);
    System.out.printf("%d == %s  (%s)%n", i, n, n.getClass().getSimpleName());
    long l = Long.MAX_VALUE;
    n = ByteableNumber.of(l);
    System.out.printf("%d == %s  (%s)%n", l, n, n.getClass().getSimpleName());
    float f = 500.005f;
    n = ByteableNumber.of(f);
    System.out.printf("%s == %s  (%s)%n", f, n, n.getClass().getSimpleName());
    double d = 500.005;
    n = ByteableNumber.of(d);
    System.out.printf("%s == %s  (%s)%n", d, n, n.getClass().getSimpleName());
  }
  
}
