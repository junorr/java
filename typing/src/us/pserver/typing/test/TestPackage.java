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

package us.pserver.typing.test;

import java.nio.file.Paths;
import us.pserver.timer.Timer;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 08/07/2015
 */
public class TestPackage {

  
  public static void main(String[] args) {
    System.out.println("* Timer.class.getResourceAsStream(\"/us/pserver/tcp/BasicSocketHandler.class\")");
    System.out.print("   ");
    System.out.println(Timer.class.getResourceAsStream("/us/pserver/tcp/BasicSocketHandler.class"));
    System.out.println("* Timer.class.getResource(\"\").getPath()");
    System.out.print("   ");
    System.out.println(Timer.class.getResource("").getPath());
    System.out.println("* Timer.class.getProtectionDomain().getCodeSource().getLocation()");
    System.out.print("   ");
    System.out.println(Timer.class.getProtectionDomain().getCodeSource().getLocation());
    System.out.println("* TestPackage.class.getResource(\"\").getPath()");
    System.out.print("   ");
    System.out.println(TestPackage.class.getResource("").getPath());
    System.out.println("* TestPackage.class.getProtectionDomain().getCodeSource().getLocation()");
    System.out.print("   ");
    System.out.println(TestPackage.class.getProtectionDomain().getCodeSource().getLocation());
    System.out.println("* TestPackage.class.getProtectionDomain().getClassLoader().getResource(\"\")");
    System.out.print("   ");
    System.out.println(TestPackage.class.getProtectionDomain().getClassLoader().getResource(""));
    System.out.println("* Paths.get(\"\").toAbsolutePath()");
    System.out.print("   ");
    System.out.println(Paths.get("").toAbsolutePath());
  }
  
}
