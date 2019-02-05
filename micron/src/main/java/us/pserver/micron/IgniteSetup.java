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

import java.util.Collection;
import java.util.Set;
import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheRebalanceMode;
import org.apache.ignite.cluster.ClusterNode;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.DataStorageConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.spi.communication.tcp.TcpCommunicationSpi;
import us.pserver.micron.config.CacheConfig;
import us.pserver.micron.config.IgniteConfig;
import us.pserver.micron.security.User;
import us.pserver.tools.misc.Sleeper;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 22/01/2019
 */
public class IgniteSetup {
  
  public static Ignite start(IgniteConfig ic) {
    IgniteConfiguration cfg = new IgniteConfiguration();
    cfg.setClassLoader(User.class.getClassLoader());
    if(ic.getStorage().isPresent()) {
      String path = ic.getStorage().get().toString();
      DataStorageConfiguration scf = new DataStorageConfiguration();
      scf.getDefaultDataRegionConfiguration().setPersistenceEnabled(true);
      scf.setStoragePath(path)
          .setWalPath(path + "/wal")
          .setWalArchivePath(path + "/wal/archive")
          .getDefaultDataRegionConfiguration()
          .setPersistenceEnabled(true);
      cfg.setDataStorageConfiguration(scf);
    }
    if(!ic.getCacheConfigSet().isEmpty()) {
      Set<CacheConfig> caches = ic.getCacheConfigSet();
      CacheConfiguration[] array = new CacheConfiguration[caches.size()];
      int i = 0;
      for(CacheConfig c : caches) {
        array[i] = new CacheConfiguration(c.getName())
            .setCacheMode(c.getCacheMode())
            .setRebalanceMode(c.getCacheRebalanceMode())
            .setBackups(c.getBackups());
        c.getExpiryPolicyFactory()
            .ifPresent(f -> array[i].setEvictionPolicyFactory(f));
      }
    }
    TcpCommunicationSpi tcp = new TcpCommunicationSpi();
    tcp.setLocalAddress(ic.getIgniteServerConfig().getAddress())
        .setLocalPort(ic.getIgniteServerConfig().getPort());
    cfg.setCommunicationSpi(tcp);
    Ignite ignite = Ignition.start(cfg);
    //if(ic.getJoinTimeout() > 0) {
      //Sleeper.of(ic.getJoinTimeout()).sleep();
    //}
    ignite.cluster().active(true);
    Collection<ClusterNode> nodes = ignite.cluster().forServers().nodes();
    ignite.cluster().setBaselineTopology(nodes);
    return ignite;
  }
  
}
