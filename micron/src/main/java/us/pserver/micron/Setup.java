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

package us.pserver.micron;

import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.DataStorageConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import us.pserver.micron.security.User;
import us.pserver.tools.Match;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 22/01/2019
 */
public class Setup {

  private final Ignite ignite;
  
  
  public Setup(IgniteConfiguration config) {
    this.ignite = Ignition.start(Match.notNull(config).getOrFail("Bad null IgniteConfiguration"));
  }
  
  
  public Ignite ignite() {
    return ignite;
  }
  
  
  public static Setup create(int ... portRange) {
    IgniteConfiguration cfg = new IgniteConfiguration();
    CacheConfiguration ccf = new CacheConfiguration();
    ccf.setCacheMode(CacheMode.REPLICATED);
    cfg.setCacheConfiguration(ccf);
    cfg.setClassLoader(User.class.getClassLoader());
    DataStorageConfiguration scf = new DataStorageConfiguration();
    scf.getDefaultDataRegionConfiguration().setPersistenceEnabled(true);
    cfg.setDataStorageConfiguration(scf);
    
  }
  
}
