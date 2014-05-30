/*
 * Direitos Autorais Reservados (c) 2011 Juno Roesler
 * Contato: juno.rr@gmail.com
 * 
 * Esta biblioteca é software livre; você pode redistribuí-la e/ou modificá-la sob os
 * termos da Licença Pública Geral Menor do GNU conforme publicada pela Free
 * Software Foundation; tanto a versão 2.1 da Licença, ou (a seu critério) qualquer
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

package com.jpower.sockets;

import com.jpower.log.LogFile;
import com.jpower.log.LogPrinter;
import com.jpower.log.Logger;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 16/05/2013
 */
public class Server {

  private String address;
  
  private int port;
  
  private String controllWord;
  
  private ServerSocket server;
  
  private Logger logger;
  
  
  public Server(String address, int port, String controllWord) {
    if(address != null && address.trim().isEmpty())
      throw new IllegalArgumentException("Invalid address: "+ address);
    if(controllWord == null || controllWord.trim().isEmpty())
      throw new IllegalArgumentException("Invalid control word: "+ controllWord);
    if(port < 0 || port > 65535)
      throw new IllegalArgumentException("Invalid port: "+ port);
    
    this.address = address;
    this.port = port;
    this.controllWord = controllWord;
    logger = new Logger();
    logger.add(new LogFile("./com.jpower.sockets.server.log"));
    logger.add(new LogPrinter());
  }
  
  
  public void start() {
    try {
      logger.info("Starting server...");
      server = new ServerSocket();
      if(address == null)
        server.bind(new InetSocketAddress(port));
      else
        server.bind(new InetSocketAddress(address, port));
      
      port = server.getLocalPort();
      logger.info("Server started!");
      logger.info("Listening on  "+ (address == null ? "0.0.0.0" : address)
          + ":"+ port);
      
      server.setSoTimeout(10000);
      boolean run = true;
      while(run) {
        try {
          Socket client = server.accept();
          logger.info("Client connected: "+ client.getInetAddress());
          logger.info("Sending controll word ("+ controllWord+ ")...");
          client.getOutputStream().write(controllWord.getBytes());
          client.getOutputStream().flush();
          logger.info("Success. Controll word sent.");
          logger.info("Exiting...");
          client.getOutputStream().close();
          client.close();
          run = false;
        } catch(SocketTimeoutException e) {
          logger.info("No client connected...");
        }
      }
    } catch(Exception e) {
      logger.fatal(e.toString());
      this.flushException(e);
      System.exit(1);
    }
  }
  
  
  private void flushException(Exception ex) {
    if(ex == null) return;
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    PrintStream ps = new PrintStream(bos);
    ex.printStackTrace(ps);
    ps.flush();
    logger.error(bos.toString());
  }
  
  
  public static void main(String[] args) {
    Server s = new Server(null, 10080, "calypso");
    s.start();
  }
  
}
