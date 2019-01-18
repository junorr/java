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

package us.pserver.orb.test;

import java.nio.file.Paths;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import us.pserver.orb.Orb;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 05/01/2018
 */
public class TestEnvironment {

  @Test
  //@Disabled
  public void unixEnvConfig() {
    UnixEnvConfig cfg = Orb.create().fromEnvironment().create(UnixEnvConfig.class);
    System.out.println(cfg);
    Assertions.assertEquals("juno", cfg.getUser());
    Assertions.assertEquals(Paths.get("/home/juno"), cfg.getHome());
    Assertions.assertEquals(1543, cfg.getSshAgentPid());
    Assertions.assertEquals(1, cfg.getQtAccessibility());
  }
  
  @Disabled
  @Test
  public void windowsEnvConfig() throws NoSuchMethodException {
    WindowsEnvConfig cfg = Orb.create().fromEnvironment().create(WindowsEnvConfig.class);
    System.out.println(cfg);
    cfg.defmeth();
    Assertions.assertEquals(4, cfg.getNumberOfProcessors());
    Assertions.assertEquals(10, cfg.setNumberOfProcessors(10).getNumberOfProcessors());
    Assertions.assertEquals("Windows_NT", cfg.getOS());
    Assertions.assertEquals("juno", cfg.getUsername());
    Assertions.assertEquals(Paths.get("c:/windows"), cfg.getWindir());
  }
  
}
