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

package us.pserver.dbone.store;

import org.junit.jupiter.api.Test;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 17/06/2018
 */
public class TestSuperclass {

  @Test
  public void testStringSuperclass() {
    Class cls = String.class;
    while(cls.getSuperclass() != null) {
      testSuperclass(cls);
      cls = cls.getSuperclass();
    }
  }
  
  public void testSuperclass(Class cls) {
    Class supr = cls.getSuperclass();
    Class[] clss = cls.getInterfaces();
    System.out.printf("----- %s -----%n", cls.getSimpleName());
    System.out.printf("  \\- extends: %s%n", supr.getSimpleName());
    System.out.printf("  \\- implements: [ %d ]\n", clss.length);
    for(Class i : clss) {
      System.out.printf("    \\- %s%n", i.getSimpleName());
    }
  }
  
}
