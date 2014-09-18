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

import com.jpower.date.SimpleDate;
import java.io.IOException;


/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 17/07/2012
 */
public class TimeReceiver implements Runnable {
  
  private Client client;
  
  private boolean receive;
  
  private String content;
  
  private ReceiveTimeListener listener;
  

  public TimeReceiver( Client c ) {
    client = c;
  }


  public Client getClient() {
    return client;
  }


  public TimeReceiver setClient(Client client) {
    if(client != null)
      this.client = client;
    return this;
  }
  

  public String getContent() {
    return content;
  }
  
  
  public void connect() throws IOException {
    client.connect();
  }
  
  
  public void sendRequest() throws IOException {
    HttpRequest req = new HttpRequest();
    String addr = client.getConfig().getRemoteAddress()
        + ":"+ String.valueOf(
        client.getConfig().getPort());
    
    if(addr.contains("://"))
      req.setAddress(client.getConfig().getRemoteAddress());
    else
      req.setHost(addr);
    
    if(client.getConfig().getProxyUser() != null
        && !client.getConfig().getProxyUser().isEmpty())
      req.setProxyAuth(client.getConfig().getProxyUser()
          + ":" + client.getConfig().getProxyPass());
    System.out.println("* Request:");
    System.out.println(req.build());
    client.send(req.build().getBytes());
  }


  @Override
  public void run() {
    try {
      
      receive = true;
      
      while(receive) {
        DynamicBuffer buffer = client.receiveBlocking(true);
        if(buffer.isEmpty()) continue;
        
        String sret = new String(buffer.toArray());
        System.out.println("* Return: ");
        System.out.println(sret);
        
        SimpleDate date = SimpleDate.parseDate(sret,
            SimpleDate.YYYYMMDD_HHMMSS_DASH);
        buffer.clear();
        if(listener != null)
          listener.received(date);
      }
      
    } catch(Exception ex) {
      System.out.println("TimeReceiver.run() [102]: exception catched");
      if(listener != null)
        listener.error(ex);
      try {
        client.close();
      } catch(IOException e) {}
    }
  }
  
  
  public ReceiveTimeListener getListener() {
    return listener;
  }


  public TimeReceiver setListener(ReceiveTimeListener listener) {
    if(listener != null)
      this.listener = listener;
    return this;
  }
  
  
  public void startListen() {
    Thread th = new Thread(this);
    th.setDaemon(true);
    th.start();
  }
  
  
  public void stop() {
    receive = false;
    try {
      client.close();
    } catch(Exception ex) {}
  }
  
}
