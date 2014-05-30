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
import java.net.InetSocketAddress;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 18/09/2012
 */
public class ScreenController implements Runnable, ConnectionHandler {
  
  public static final int DELAY = 500;
  
  private Shooter shooter;
  
  private Executor exec;
  
  private TcpClient client;
  
  private String address;
  
  private int port;
  
  private String proxyAuth;
  
  private String proxyAddr;
  
  private int proxyPort;
  
  private boolean run;
  
  private TcpChannel channel;
  
  
  public ScreenController() {
    shooter = new Shooter();
    exec = new Executor();
    port = 20443;
    run = false;
  }
  
  
  public void start() {
    FrameControl fc = new FrameControl();
    fc.setEnabled(true);
    
    if(proxyAddr != null && proxyPort > 0)
      client = new TcpClient(new InetSocketAddress(proxyAddr, proxyPort), this, fc);
    else
      client = new TcpClient(new InetSocketAddress(address, port), this, fc);
    
    client.connect();
  }


  @Override
  public void run() {
    if(!run || channel == null)
      return;
    
    Screenshot scs = new Screenshot();
    scs.setBytes(shooter.createScreenBytes());
    DynamicBuffer db = new DynamicBuffer();
    HttpRequest hr = new HttpRequest();
    if(proxyAuth != null)
      hr.setProxyAuth(proxyAuth);
    db.writeObject(scs);
    hr.setHost(address);
    hr.setAddress(address+ ":"+ port);
    hr.setContent(db);
      
    System.out.print("sending - "+ System.currentTimeMillis());
    channel.write(hr.build().getBytes());
    System.out.println(" ...");

    try {
      Thread.sleep(DELAY);
    } catch(InterruptedException ex) {}
    run();
  }


  @Override
  public void connected(TcpChannel ch) {
    channel = ch;
    run = true;
    Thread th = new Thread(this);
    th.setDaemon(false);
    th.start();
  }


  @Override
  public void disconnected(TcpChannel ch) {
    System.out.println("# CHANNEL DISCONNECTED!");
    channel = null;
    run = false;
  }


  @Override
  public void closed(TcpChannel ch) {
    System.out.println("# CLOSED CONNECTION!");
    channel = null;
    run = false;
  }


  @Override
  public void error(Throwable th, TcpChannel ch) {
    th.printStackTrace();
    //throw new UnsupportedOperationException("Not supported yet.");
  }


  @Override
  public void received(DynamicBuffer buffer, TcpChannel ch) {
    ResponseParser rp = new ResponseParser();
    rp.parse(buffer.readString());
    Object o = rp.getContent().readObject();
    
    System.out.println("* rcv: "+ o);
    
    if(o instanceof Command) {
      exec.exec((Command) o);
    }
  }


  public String getAddress() {
    return address;
  }


  public void setAddress(String address) {
    this.address = address;
  }


  public int getPort() {
    return port;
  }


  public ScreenController setPort(int port) {
    if(port > 0)
      this.port = port;
    return this;
  }
  
  
  public ScreenController setProxy(String address, int port) {
    if(address != null && !address.isEmpty())
      proxyAddr = address;
    if(port > 0)
      proxyPort = port;
    return this;
  }
  
  
  public String getProxyAddress() {
    return proxyAddr;
  }
  
  
  public int getProxyPort() {
    return proxyPort;
  }


  public String getProxyAuth() {
    return proxyAuth;
  }
      


  public void setProxyAuth(String proxyAuth) {
    this.proxyAuth = proxyAuth;
  }
  
  
  public static void main(String[] args) {
    ScreenController sc = new ScreenController();
    sc.setAddress("172.24.74.38");
    sc.setPort(20443);
    sc.setProxyAuth("f6036477:12345678");
    sc.start();
  }
  
}
