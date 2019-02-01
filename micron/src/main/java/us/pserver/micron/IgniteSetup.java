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
import javax.cache.expiry.AccessedExpiryPolicy;
import javax.cache.expiry.Duration;
import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.cluster.ClusterNode;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.DataStorageConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.spi.communication.tcp.TcpCommunicationSpi;
import us.pserver.micron.security.Security;
import us.pserver.micron.security.User;
import us.pserver.micron.security.impl.SecurityImpl;
import us.pserver.tools.Match;
import us.pserver.tools.misc.Sleeper;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 22/01/2019
 */
public class IgniteSetup {
  
  public static final String CACHE_PUBLIC = "cache.public";
  
  public static final String DEFAULT_LOCAL_ADDRESS = "0.0.0.0";
  
  public static final int DEFAULT_LOCAL_PORT = 5555;
  
  private final Ignite ignite;
  
  
  public IgniteSetup(IgniteConfiguration config) {
    //System.out.println(">>>=== Micron Setup ======");
    //System.out.println(">>>  ignite.start()...");
    this.ignite = Ignition.start(Match.notNull(config).getOrFail("Bad null IgniteConfiguration"));
    //System.out.println(">>>  ignite.start()...[Ok]");
    //System.out.println(">>>  witing 5s for join nodes...");
    Sleeper.of(5000).sleep();
    //System.out.println(">>>  witing 5s for join nodes...[Ok]");
    ignite.cluster().active(true);
    Collection<ClusterNode> nodes = ignite.cluster().forServers().nodes();
    //System.out.println(">>>  Baseline Topology:");
    //nodes.forEach(n -> System.out.printf(">>>   - %s%n", n));
    ignite.cluster().setBaselineTopology(nodes);
    //System.out.println("<<<=== Micron Setup ======");
  }
  
  
  public Ignite ignite() {
    return ignite;
  }
  
  
  public Security security() {
    return Security.create(ignite());
  }
  
  
  public static IgniteSetup create() {
    return create(DEFAULT_LOCAL_ADDRESS, DEFAULT_LOCAL_PORT);
  }
  
  
  public static IgniteSetup create(String address, int port) {
    IgniteConfiguration cfg = new IgniteConfiguration();
    
    CacheConfiguration[] caches = new CacheConfiguration[5];
    caches[0] = new CacheConfiguration(SecurityImpl.CACHE_GROUP)
        .setCacheMode(CacheMode.REPLICATED)
        .setBackups(1);
    caches[1] = new CacheConfiguration(SecurityImpl.CACHE_RESOURCE)
        .setCacheMode(CacheMode.REPLICATED)
        .setBackups(1);
    caches[2] = new CacheConfiguration(SecurityImpl.CACHE_ROLE)
        .setCacheMode(CacheMode.REPLICATED)
        .setBackups(1);
    caches[3] = new CacheConfiguration(SecurityImpl.CACHE_USER)
        .setCacheMode(CacheMode.REPLICATED)
        .setBackups(1);
    caches[4] = new CacheConfiguration(CACHE_PUBLIC)
        .setCacheMode(CacheMode.REPLICATED)
        .setExpiryPolicyFactory(AccessedExpiryPolicy.factoryOf(Duration.TEN_MINUTES));
    cfg.setCacheConfiguration(caches);
    
    cfg.setClassLoader(User.class.getClassLoader());
    DataStorageConfiguration scf = new DataStorageConfiguration();
    scf.getDefaultDataRegionConfiguration().setPersistenceEnabled(true);
    scf.setStoragePath("d:/ignite/db")
        .setWalPath("d:/ignite/db/wal")
        .setWalArchivePath("d:/ignite/db/wal/archive")
        .getDefaultDataRegionConfiguration()
        .setPersistenceEnabled(true);
    cfg.setDataStorageConfiguration(scf);
    TcpCommunicationSpi tcp = new TcpCommunicationSpi();
    tcp.setLocalAddress("0.0.0.0")
        .setLocalPort(port);
    cfg.setCommunicationSpi(tcp);
    return new IgniteSetup(cfg);
  }
  
}
