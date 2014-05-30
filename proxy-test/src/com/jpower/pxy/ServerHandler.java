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

package com.jpower.pxy;

import com.jpower.net.ConnectionHandler;
import com.jpower.net.ConnectionHandlerFactory;
import com.jpower.net.NetConfig;
import com.jpower.net.NioClient;
import com.jpower.net.http.RequestParser;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 12/07/2013
 */
public class ServerHandler implements ConnectionHandler, ConnectionHandlerFactory {
  
  private NetConfig conf;
  
  private NioClient client;
  
  private RequestParser rp;
  
  private LinkedList<ByteBuffer> rcvd;
  
  
  public ServerHandler() {
    conf = new NetConfig();
    rp = new RequestParser();
    rcvd = new LinkedList<>();
  }
  
  
  public void addBuffer(ByteBuffer buf) {
    if(buf == null) return;
    LogProvider.getLogger().info("ServerHandler.addBuffer()");
    rcvd.add(buf);
    if(buf.limit() < conf.getBufferSize())
      client.close();
  }


  @Override
  public void received(ByteBuffer buffer) {
    byte[] bs = new byte[buffer.limit()];
    buffer.get(bs);
    String req = new String(bs);
    rp = new RequestParser();
    rp.parse(req);
    LogProvider.getLogger().info("Received from Browser:")
        .info(rp.getMethod())
        .info(rp.getAddress())
        .info(rp.getVersion())
        .info(rp.getHost())
        .info(rp.getUserAgent())
        .info("# # # # #\n-----------------------------------------\n" + req)
        .info("# # # # #\n-----------------------------------------");
    
    conf = new NetConfig();
    conf.setAddress(this.getResolvedIP(rp.getHost()))
        .setPort((rp.getAddress().startsWith("https") ? 443 : 80))
        .setLogger(LogProvider.getLogger());
    
    try {
      client = new NioClient(conf, new ClientHandler(this, buffer));
      client.connect();
      client.run();
    } catch (IOException ex) {
      ex.printStackTrace();
      LogProvider.getLogger().fatal(ex);
    }
  }


  @Override
  public void connected(SocketChannel channel) {
    try {
      LogProvider.getLogger().info("Browser Connected!")
          .info(channel.getRemoteAddress().toString());
    } catch (IOException ex) {
      LogProvider.getLogger().warn(ex.toString());
    }
  }


  @Override
  public void error(Throwable th) {
    LogProvider.getLogger().error(th);
  }


  @Override
  public ByteBuffer sending() {
    LogProvider.getLogger().info("ServerHandler: sending to browser...");
    return (rcvd.isEmpty() ? null : rcvd.removeFirst());
  }


  @Override
  public boolean isSending() {
    return !rcvd.isEmpty();
  }


  @Override
  public void sent(int bytes) {
    LogProvider.getLogger().info("Sent to browser: "+ bytes+ " bytes");
  }
  
  
  public String getResolvedIP(String hostname) {
    System.out.println("* hostname="+ hostname);
    hostname = hostname.trim();
    InetAddress is = null;
    try {
      is = InetAddress.getByName(hostname);
    } catch (UnknownHostException ex) {
      ex.printStackTrace();
    }
    System.out.println("* is="+ is);
    return (is != null ? is.getHostAddress() : null);
  }


  @Override
  public ConnectionHandler createConnectionHandler() {
    return new ServerHandler();
  }


  @Override
  public void disconnected(SocketChannel channel) {
    LogProvider.getLogger().info("ServerHandler: Connection closed!");
  }

}
