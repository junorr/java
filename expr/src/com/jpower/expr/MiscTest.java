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

package com.jpower.expr;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 06/08/2013
 */
public class MiscTest {
  
  static class A {
    String str;
    public String toString() {
      return "A.str = '"+ str+ "'";
    }
  }
  
  
  public static void modify(String s) {
    s += ".";
  }
  
  
  public static void modify(A a) {
    a = new A();
  }

  
  public static void modifyStr(A a) {
    a = new A();
    a.str = "a.str";
  }

  
  public static void main(String[] args) {
    String s = "string";
    System.out.println("* s = '"+ s+ "'");
    modify(s);
    System.out.println("* s = '"+ s+ "'");
    A a = null;
    System.out.println("* a = "+ a);
    modify(a);
    System.out.println("* a = "+ a);
    modifyStr(a);
    System.out.println("* a = "+ a);
  }
  
}
