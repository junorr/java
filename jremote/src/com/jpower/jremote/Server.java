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
import com.jpower.nnet.ConnectionHandlerFactory;
import com.jpower.nnet.DynamicBuffer;
import com.jpower.nnet.FrameControl;
import com.jpower.nnet.TcpChannel;
import com.jpower.nnet.TcpServer;
import com.jpower.nnet.http.HttpResponse;
import com.jpower.nnet.http.RequestParser;
import java.net.InetSocketAddress;
import java.util.concurrent.LinkedBlockingQueue;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 18/09/2012
 */
public class Server implements ConnectionHandler, ConnectionHandlerFactory {
  
  private TcpServer server;
  
  private LinkedBlockingQueue<Command> cmds;
  
  private LinkedBlockingQueue<TcpChannel> clients;
  
  private TcpChannel machine;
  
  private Screenshot screen;
  
  private Screenshot last;
  
  private int port;
  
  
  public Server() {
    cmds = new LinkedBlockingQueue<>();
    clients = new LinkedBlockingQueue<>();
    port = 20443;
  }
  
  
  public Server start() {
    FrameControl fc = new FrameControl();
    fc.setEnabled(true);
    server = new TcpServer(new InetSocketAddress(port), this, fc);
    server.start();
    return this;
  }
  
  
  public Server setPort(int port) {
    if(port > 0) {
      this.port = port;
    }
    return this;
  }


  @Override
  public void connected(TcpChannel tc) {}


  @Override
  public void disconnected(TcpChannel tc) {
    System.out.println("# CHANNEL DISCONNECTED!");
    if(clients.contains(tc))
      clients.remove(tc);
    if(tc.equals(machine))
      machine = null;
  }


  @Override
  public void closed(TcpChannel tc) {
    System.out.println("# CLOSED CONNECTION!");
    if(clients.contains(tc))
      clients.remove(tc);
    if(tc.equals(machine))
      machine = null;
  }


  @Override
  public void error(Throwable th, TcpChannel tc) {
    th.printStackTrace();
  }


  @Override
  public void received(DynamicBuffer db, TcpChannel tc) {
    RequestParser rp = new RequestParser();
    rp.parse(db.toArray());
    Object o = rp.getContent().readObject();
    System.out.println("rcv: "+ o);
    
    if(o instanceof Command) {
      if(!clients.contains(tc)) 
        clients.offer(tc);
      
      Command c = (Command) o;
      if(!c.isEmpty()) 
        cmds.offer(c);
      
      this.sendCommands();
    }
    
    else if(o instanceof Screenshot) {
      screen = (Screenshot) o;
      if(machine == null) machine = tc;
      this.updateClients();
    }
  }
  
  
  public void sendCommands() {
    if(cmds.isEmpty() || machine == null)
      return;
    
    Command c = null;
    while(!cmds.isEmpty()) {
      c = cmds.poll();
      DynamicBuffer db = new DynamicBuffer();
      HttpResponse hr = new HttpResponse();
      db.writeObject(c);
      hr.setContent(db);
      machine.write(hr.build().getBytes());
    }
  }
  
  
  public void updateClients() {
    if(clients.isEmpty()) return;
    if(last != null && last.equals(screen)) return;
    
    DynamicBuffer db = new DynamicBuffer();
    HttpResponse hr = new HttpResponse();
    db.writeObject(screen);
    last = screen;
    hr.setContent(db);
    for(TcpChannel c : clients) {
      c.write(hr.build().getBytes());
    }
  }
  
  
  @Override
  public ConnectionHandler create() {
    return new ConnectionHandler() {
      @Override
      public void connected(TcpChannel tc) {
        Server.this.connected(tc);
      }
      @Override
      public void disconnected(TcpChannel tc) {
        Server.this.disconnected(tc);
      }
      @Override
      public void closed(TcpChannel tc) {
        Server.this.closed(tc);
      }
      @Override
      public void error(Throwable thrwbl, TcpChannel tc) {
        Server.this.error(thrwbl, tc);
      }
      @Override
      public void received(DynamicBuffer db, TcpChannel tc) {
        Server.this.received(db, tc);
      }
    };
  }
  
  
  public static void main(String[] args) {
    Server s = new Server();
    s.setPort(20443);
    s.start();
  }

}
