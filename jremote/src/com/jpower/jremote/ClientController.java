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

package com.jpower.jremote;

import com.jpower.nnet.ConnectionHandler;
import com.jpower.nnet.DynamicBuffer;
import com.jpower.nnet.FrameControl;
import com.jpower.nnet.TcpChannel;
import com.jpower.nnet.TcpClient;
import com.jpower.nnet.http.HttpRequest;
import com.jpower.nnet.http.ResponseParser;
import java.awt.Point;
import java.net.InetSocketAddress;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 18/09/2012
 */
public class ClientController implements ConnectionHandler {
  
  private TcpClient client;
  
  private String address;
  
  private int port;
  
  private String proxyAuth;
  
  private String proxyAddr;
  
  private int proxyPort;
  
  private TcpChannel channel;
  
  private FrontEnd frontend;
  
  
  public ClientController(FrontEnd fe) {
    frontend = fe;
  }
  
  
  public void start() {
    if(proxyAddr != null && proxyPort > 0)
      client = new TcpClient(
          new InetSocketAddress(proxyAddr, proxyPort), 
          this, 
          new FrameControl().setEnabled(true));
    else
      client = new TcpClient(
          new InetSocketAddress(address, port), 
          this, 
          new FrameControl().setEnabled(true));
    
    client.connect();
  }
  
  
  public String getAddress() {
    return address;
  }


  public ClientController setAddress(String address) {
    if(address != null && !address.isEmpty())
      this.address = address;
    return this;
  }


  public int getPort() {
    return port;
  }


  public ClientController setPort(int port) {
    if(port > 0)
      this.port = port;
    return this;
  }
  
  
  public ClientController setProxyAuth(String auth) {
    if(auth != null && !auth.isEmpty())
      proxyAuth = auth;
    return this;
  }
  
  
  public String getProxyAuth() {
    return proxyAuth;
  }
  
  
  public ClientController setProxy(String address, int port) {
    if(address != null && !address.isEmpty())
      proxyAddr = address;
    if(port > 0)
      proxyPort = port;
    return this;
  }
  
  
  public int getProxyPort() {
    return proxyPort;
  }
  
  
  public String getProxyAddress() {
    return proxyAddr;
  }


  public ClientController send(Command c) {
    if(c == null || channel == null)
      return this;;
    
    HttpRequest hr = new HttpRequest();
    hr.setHost(address);
    hr.setAddress(address+ ":"+ port);
    DynamicBuffer buf = new DynamicBuffer();
    buf.writeObject(c);
    hr.setContent(buf);
    channel.write(hr.build().getBytes());
    return this;
  }


  @Override
  public void connected(TcpChannel tc) {
    channel = tc;
    this.send(new Command());
  }


  @Override
  public void disconnected(TcpChannel tc) {
    channel = null;
  }


  @Override
  public void closed(TcpChannel tc) {
    channel = null;
  }


  @Override
  public void error(Throwable thrwbl, TcpChannel tc) {
    thrwbl.printStackTrace();
  }


  @Override
  public void received(DynamicBuffer db, TcpChannel tc) {
    ResponseParser rp = new ResponseParser();
    rp.parse(db.readString());
    Object o = rp.getContent().readObject();
    System.out.println("rcv: "+ o);
    if(o == null || !(o instanceof Screenshot))
      return;
    frontend.show(((Screenshot) o).asImage());
  }
  
  
  public static void main(String[] args) {
    ClientController cs = new ClientController(null);
    Command c = new Command(new MouseAction(Action.TYPE.PRESS_ACTION, new Point(500, 500), MouseAction.BUTTON1, -1), new KeyAction(Action.TYPE.PRESS_ACTION, 66, 0));
    cs.setAddress("127.0.0.1");
    cs.setPort(20443);
    cs.start();
    cs.send(c);
  }

}
