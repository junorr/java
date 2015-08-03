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

package us.pserver.xprops.xtest;

import java.util.Date;
import us.pserver.xprops.util.Validator;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 01/08/2015
 */
public class TestValidator {
  
  public static void test1() {
    Validator.off("")
        .forNull()
        .fail()
    ;
    System.out.println("! Test 1 Passed");
  }

  
  public static void test2() {
    Validator.off(" ")
        .forEmpty()
        .fail()
    ;
    System.out.println("! Test 2 Passed");
  }

  
  public static void test3() {
    Validator.off(2)
        .forLesserThen(1)
        .fail()
    ;
    System.out.println("! Test 3 Passed");
  }

  
  public static void test4() {
    Validator.off(3)
        .forNotBetween(1, 5)
        .fail()
    ;
    System.out.println("! Test 4 Passed");
  }

  
  public static void test5() {
    Date d = new Date();
    Date d2 = d;
    Validator.off(d)
        .forNotEquals(d2)
        .fail()
    ;
    System.out.println("! Test 5 Passed");
  }

  
  public static void test6() {
    Validator.off(new Object())
        .forTypeMatch(String.class)
        .fail()
    ;
    System.out.println("! Test 6 Passed");
  }

  
  public static void test7() {
    Validator.off("")
        .forTypeMatch(String.class)
        .fail()
    ;
    System.out.println("! Test 7 Passed");
  }

  
  public static void test8() {
    Validator.off(new Object())
        .forNull()
        .not()
        .message("My Custom Message:")
        .fail()
    ;
    System.out.println("! Test 8 Passed");
  }

  
  public static void main(String[] args) {
    test8();
  }
  
}
