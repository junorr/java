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

package us.pserver.sdb.net;

import java.net.InetSocketAddress;
import us.pserver.rob.NetConnector;
import us.pserver.rob.container.Authenticator;
import us.pserver.rob.container.ObjectContainer;
import us.pserver.rob.factory.DefaultFactoryProvider;
import us.pserver.rob.server.NetworkServer;
import us.pserver.sdb.DBEngine;
import us.pserver.sdb.SimpleDB;
import us.pserver.sdb.engine.StorageCredentialsSource;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 29/12/2014
 */
public class DBServer {

  private DBEngine sdb;
  
  private NetworkServer server;
  
  private NetConnector connector;
  
  private ObjectContainer container;
  
  private StorageCredentialsSource crsource;
  
  
  public DBServer(NetConnector conn, DBEngine sdb) {
    if(sdb == null)
      throw new IllegalArgumentException(
          "SimpleDB must be not null: "+ sdb);
    if(conn == null)
      throw new IllegalArgumentException(
          "NetConnection must be not null: "+ conn);
    this.sdb = sdb;
    this.connector = conn;
    this.init();
  }
  
  
  public DBServer(InetSocketAddress addr, DBEngine sdb) {
    if(sdb == null)
      throw new IllegalArgumentException(
          "SimpleDB must be not null: "+ sdb);
    if(addr == null)
      throw new IllegalArgumentException(
          "InetSocketAddress must be not null: "+ addr);
    this.sdb = sdb;
    this.connector = new NetConnector(addr.getHostString(), addr.getPort());
    this.init();
  }
  
  
  private void init() {
    crsource = new StorageCredentialsSource(sdb.getEngine());
    container = new ObjectContainer(new Authenticator(crsource));
    container.put(sdb.getClass().getName(), sdb);
    container.put(this.getClass().getName(), this);
    server = new NetworkServer(container, connector, 
        DefaultFactoryProvider.getHttpResponseChannelFactory());
  }
  
  
  public NetConnector getNetConnector() {
    return connector;
  }
  
  
  public DBEngine getDBEngine() {
    return sdb;
  }
  
  
  public StorageCredentialsSource getCredentialsSource() {
    return crsource;
  }
  
  
  public void start() {
    server.start();
  }
  
  
  public void startNewThread() {
    server.startNewThread();
  }
  
  
  public void stop() {
    server.stop();
    sdb.close();
  }
  
}
