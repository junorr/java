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
package com.jpower.stunnel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;


/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 17/07/2012
 */
public class Server {
  
  public static final int SO_TIMEOUT = 1000;
  
  private Configuration config;
  
  private HandlerFactory factory;
  
  private ServerSocket server;
  
  private boolean run;
  
  
  public Server(HandlerFactory factory, Configuration conf) {
    if(factory == null)
      throw new IllegalArgumentException(
          "Invalid HandlerFactory: "+ factory);
    
    if(conf == null)
      throw new IllegalArgumentException(
          "Invalid Configuration: "+ conf);
    
    this.factory = factory;
    this.config = conf;
  }


  public Configuration getConfig() {
    return config;
  }


  public void setConfig(Configuration config) {
    this.config = config;
  }


  public HandlerFactory getFactory() {
    return factory;
  }


  public void setFactory(HandlerFactory factory) {
    this.factory = factory;
  }
  
  
  public void start() throws IOException {
    server = new ServerSocket();
    server.setSoTimeout(SO_TIMEOUT);
    run = true;
    
    InetSocketAddress addr = null;
    
    if(config.getLocalAddress() == null 
        || config.getLocalAddress().isEmpty())
      addr = new InetSocketAddress(config.getPort());
    
    else
      addr = new InetSocketAddress(config.getLocalAddress(), 
          config.getPort());
    
    server.bind(addr);
    
    System.out.println("* Server started at:");
    System.out.println("  [ "+ addr.getAddress().getHostAddress()+ " : "+ addr.getPort()+ " ]");
    
    while(run) {
      Socket client = null;
      try {
        client = server.accept();
      } catch(SocketTimeoutException ex) {
        continue;
      }
      
      System.out.println("* New client connected:");
      System.out.println("  ["+ client.getInetAddress()+ "]");
      new HandlerThread(client)
          .setHandler(factory.create())
          .start();
    }
    
    server.close();
  }
  
  
  public void stop() {
    run = false;
  }
  
}
