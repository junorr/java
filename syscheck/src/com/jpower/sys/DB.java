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

package com.jpower.sys;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectServer;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.cs.Db4oClientServer;
import com.db4o.cs.config.ClientConfiguration;
import com.db4o.cs.config.ServerConfiguration;
import com.db4o.query.Query;
import com.db4o.ta.TransparentActivationSupport;
import com.jpower.sys.security.User;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 27/12/2012
 */
public class DB {
  
  public static final String 
      
      KEY_PORT = "PORT",
      
      KEY_SERVER = "SERVER",
      
      KEY_DBFILE = "DBFILE",
      
      KEY_DB_MAX_SIZE = "DB_MAX_SIZE_(MB)";
  

  public static final int DEFAULT_PORT = 48000;
  
  public static final int DEFAULT_DB_MAX_SIZE = 512;
  
  public static final String LOCALHOST = "127.0.0.1";
  
  public static final String DEFAULT_DBFILE = "./database.db";
  
  public static final String DBCONF_FILE = "./database.conf";
  
  
  private static ObjectContainer container;
  
  private static ObjectServer server;
  
  private static int dbMaxSize;
  
  private static int port;
  
  private static String dbfile;
  
  private static String address;
  
  
  private static final String user = "syscheck";
  
  private static final String passwd = "q@s8e4wm*#$5";
  
  private static final Config conf = new Config(DBCONF_FILE);
  
  
  static { init(); }
  
  
  private static void init() {
    dbfile = DEFAULT_DBFILE;
    dbMaxSize = DEFAULT_DB_MAX_SIZE;
    port = DEFAULT_PORT;
    address = LOCALHOST;
    Path dbc = Paths.get(DBCONF_FILE);
    if(Files.exists(dbc))
      conf.load();
    else
      conf.save();
  }
  
  
  public static void save() {
    conf.put(KEY_DBFILE, dbfile);
    conf.put(KEY_DB_MAX_SIZE, dbMaxSize);
    conf.put(KEY_PORT, port);
    conf.put(KEY_SERVER, address);
    conf.save();
  }
  
  
  public static void load() {
    conf.load();
    dbfile = conf.get(KEY_DBFILE);
    dbMaxSize = conf.getInt(KEY_DB_MAX_SIZE);
    port = conf.getInt(KEY_PORT);
    address = conf.get(KEY_SERVER);
  }
  
  
  public static ObjectContainer getEmbeddedContainer() {
    if(container == null)
      container = createEmbeddedContainer();
    return container;
  }
  
  
  public static ObjectServer getServer() {
    if(server == null) 
      server = createServer();
    return server;
  }
  
  
  private static ObjectContainer createEmbeddedContainer() {
    EmbeddedConfiguration conf = Db4oEmbedded.newConfiguration();
    conf.common().updateDepth(9);
    conf.common().add(new TransparentActivationSupport());
    conf.common().objectClass(LastSnapshot.class).cascadeOnUpdate(true);
    conf.common().objectClass(User.class).cascadeOnUpdate(true);
    return Db4oEmbedded.openFile(conf, dbfile);
  }
  
  
  public static ObjectContainer openSocketClient() {
    ClientConfiguration conf = Db4oClientServer.newClientConfiguration();
    conf.common().updateDepth(9);
    conf.common().add(new TransparentActivationSupport());
    conf.common().objectClass(LastSnapshot.class).cascadeOnUpdate(true);
    conf.common().objectClass(User.class).cascadeOnUpdate(true);
    return Db4oClientServer.openClient(conf, LOCALHOST, DEFAULT_PORT, user, passwd);
  }
  
  
  public static ObjectContainer openLocalClient() {
    return server.openClient();
  }
  
  
  public static ObjectContainer openClient(String host, int port, String user, String passwd) {
    ClientConfiguration conf = Db4oClientServer.newClientConfiguration();
    conf.common().updateDepth(9);
    conf.common().add(new TransparentActivationSupport());
    conf.common().objectClass(LastSnapshot.class).cascadeOnUpdate(true);
    conf.common().objectClass(User.class).cascadeOnUpdate(true);
    return Db4oClientServer.openClient(conf, host, port, user, passwd);
  }
  
  
  public static ObjectServer createServer() {
    ServerConfiguration conf = Db4oClientServer.newServerConfiguration();
    conf.common().updateDepth(9);
    conf.common().add(new TransparentActivationSupport());
    conf.common().objectClass(LastSnapshot.class).cascadeOnUpdate(true);
    conf.common().objectClass(User.class).cascadeOnUpdate(true);
    ObjectServer server = Db4oClientServer.openServer(conf, dbfile, port);
    server.grantAccess(user, passwd);
    return server;
  }
  
  
  public static <T> List<T> query(Query q, ObjectContainer db) {
    if(q == null || db == null) return null;
    List<T> list = q.execute();
    if(list == null || list.isEmpty())
      return list;
    for(T t : list) {
      db.ext().refresh(t, 10);
    }
    return list;
  }
  
  
  public static void clearDB() {
    Query q = container.query();
    q.constrain(Object.class);
    List l = q.execute();
    for(Object o : l)
      container.delete(o);
  }
  
  
  public static boolean isDBMaxSizeReached() {
    Path dbf = Paths.get(dbfile);
    if(!Files.exists(dbf)) 
      return false;
    long maxSize = dbMaxSize * 1024 * 1024;
    try {
      long size = Files.size(dbf);
      return size >= maxSize;
    } catch(IOException ex) {
      return false;
    }
  }


  public static int getDbMaxSize() {
    return dbMaxSize;
  }


  public static void setDbMaxSize(int dbMaxSize) {
    DB.dbMaxSize = dbMaxSize;
  }


  public static int getPort() {
    return port;
  }


  public static void setPort(int port) {
    DB.port = port;
  }


  public static String getDbfile() {
    return dbfile;
  }


  public static void setDbfile(String dbfile) {
    DB.dbfile = dbfile;
  }


  public static String getAddress() {
    return address;
  }


  public static void setAddress(String address) {
    DB.address = address;
  }
  
}
