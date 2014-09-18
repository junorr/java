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
package com.jpower.httpsocket;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Properties;


/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 26/06/2012
 */
public class HttpServer {
  
  public static final int SO_TIMEOUT = 1000;
  
  public static final int DEFAULT_PORT = 10001;
  
  public static final String ADDRESS = "localhost";
  
  public static final String CONFIG_FILE = "httpserver.properties";
  
  public static final String CONFIG_COMMENT = "Server Listen Configuration";
  
  public static final String 
      KEY_PORT = "port",
      KEY_TYPE = "handler_type";
  
  
  private ServerSocket server;
  
  private int port;
  
  private boolean running;
  
  private Class<? extends ConnectionHandler> handlerType;
  
  
  public HttpServer() throws IOException {
    server = new ServerSocket();
    port = 0;
    running = false;
    handlerType = null;
  }
  
  
  private void readProperties() {
    Properties p = new Properties();
    File f = new File(CONFIG_FILE);
    
    try {
      p.load(new FileInputStream(f));
      port = Integer.parseInt(p.getProperty(KEY_PORT));
      handlerType = (Class<? extends ConnectionHandler>) 
          Class.forName(p.getProperty(KEY_TYPE));
      
    } catch(IOException | NumberFormatException 
        | ClassNotFoundException 
        | ClassCastException ex) {
      
      port = DEFAULT_PORT;
      handlerType = PrinterHandler.class;
      this.saveProperties();
    }
  }
  
  
  private void saveProperties() {
    Properties p = new Properties();
    p.setProperty(KEY_PORT, String.valueOf(port));
    p.setProperty(KEY_TYPE, handlerType.getName());
    File f = new File(CONFIG_FILE);
    
    try {
      p.store(new FileOutputStream(f), CONFIG_COMMENT);
    } catch(IOException ex) {}
  }
  
  
  public HttpServer setHandlerType(Class<? extends ConnectionHandler> type) {
    handlerType = type;
    return this;
  }
  
  
  public HttpServer setPort(int p) {
    port = p;
    return this;
  }
  
  
  public int getPort() {
    return port;
  }
  
  
  public Class<? extends ConnectionHandler> getHandlerType() {
    return handlerType;
  }
  
  
  public boolean isRunning() {
    return running;
  }
  
  
  private ConnectionHandler newHandler() {
    if(handlerType == null) return null;
    try {
      return handlerType.newInstance();
    } catch(IllegalAccessException | InstantiationException ex) {
      return null;
    }
  }
  
  
  public void start() throws IOException {
    if(port == 0 || handlerType == null)
      this.readProperties();
    else
      this.saveProperties();
    
    running = true;
    server.bind(new InetSocketAddress(ADDRESS, port));
    server.setSoTimeout(SO_TIMEOUT);
    
    System.out.println(" * Starting HttpServer...");
    System.out.println(" * Listenning on ["+ server.getInetAddress()+ ":"+ server.getLocalPort()+ "]");
    System.out.println();
    
    while(running) {
      Socket s = null;
      try {
        s = server.accept();
      } catch(SocketTimeoutException ex) {}
      
      if(s == null) continue;
      
      ConnectionThread ct = 
          new ConnectionThread(
              this.newHandler().setSocket(s));
      ct.start();
    }
  }
  
  
  public static void main(String[] args) throws IOException {
    HttpServer server = new HttpServer();
    server.setPort(DEFAULT_PORT)
        .setHandlerType(ForwardRequestHandler.class);
    server.start();
  }
  
}
