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
import java.net.Proxy;
import java.net.Socket;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 16/05/2013
 */
public class Client {

  private String proxyAddress;
  
  private int proxyPort;
  
  private String address;
  
  private int port;
  
  private Socket socket;
  
  private Logger logger;
  
  
  public Client(String proxyAddress, int proxyPort, String address, int port) {
    if(port < 0 || port > 65535)
      throw new IllegalArgumentException("Invalid port: "+ port);
    if(address == null || address.trim().isEmpty())
      throw new IllegalArgumentException("Invalid address: "+ address);
    
    this.proxyAddress = proxyAddress;
    this.proxyPort = proxyPort;
    this.address = address;
    this.port = port;
    logger = new Logger();
    logger.add(new LogFile("./com.jpower.sockets.client.log"));
    logger.add(new LogPrinter());
  }
  
  
  public void start() {
    try {
      logger.info("Starting client...");
      if(proxyAddress != null && proxyPort > 0) {
        Proxy proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(proxyAddress, proxyPort));
        logger.info("Setting proxy: "+ proxyAddress+ ":"+ proxyPort);
        socket = new Socket(proxy);
      } else {
        socket = new Socket();
      }
      logger.info("Client started.");
      logger.info("Connecting...");
      socket.connect(new InetSocketAddress(address, port));
      logger.info("Client connected to  "+ address+ ":"+ port);
      logger.info("Waiting for controll word...");
      int b = -1;
      StringBuilder sb = new StringBuilder();
      while((b = socket.getInputStream().read()) != -1) {
        sb.append((char) (byte) b);
      }
      logger.info("Content received: "+ sb.toString());
      logger.info("Exiting...");
      socket.getInputStream().close();
      socket.close();
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
    Client c = new Client(null, 0, "localhost", 10080);
    c.start();
  }
  
}
