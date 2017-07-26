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

package br.com.bb.disec.microb.test;

import us.pserver.tools.rfl.Reflector;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 26/07/2017
 */
public class TestClassMethod {

  private final String message;
  
  public TestClassMethod(String msg) {
    this.message = msg;
  }
  
  public void run() {
    System.out.println("* "+ message);
  }
  
  private static int magic = 555;
  
  public static void method() {
    System.out.println("..:* "+ magic+ " *:..");
  }
  
  
  public static void main(String[] args) {
    Object obj = TestClassMethod.class;
    Reflector ref = new Reflector(obj);
    ref.selectField("magic").set(666).selectMethod("method").invoke();
    Reflector.of(new TestClassMethod("hello")).selectField("message").set("world").selectMethod("run").invoke();
  }
  
}
