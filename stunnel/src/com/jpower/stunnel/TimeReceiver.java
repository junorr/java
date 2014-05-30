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
import com.jpower.nnet.ConnectionHandler;
import com.jpower.nnet.DynamicBuffer;
import com.jpower.nnet.TcpChannel;
import com.jpower.nnet.TcpClient;
import com.jpower.nnet.conf.Configuration;
import com.jpower.nnet.http.HtmlBuilder;
import com.jpower.nnet.http.HttpRequest;
import java.io.IOException;


/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 17/07/2012
 */
public class TimeReceiver implements ConnectionHandler {
  
  private TcpClient client;
  
  private Configuration conf;
  
  private String content;
  
  private ReceiveListener listener;
  

  public TimeReceiver( Configuration c, ReceiveListener l ) {
    if(c == null) 
      throw new IllegalArgumentException(
        "Invalid Configuration: "+ c);
    
    if(l == null) 
      throw new IllegalArgumentException(
        "Invalid ReceiveListener: "+ l);
    
    conf = c;
    listener = l;
    client = new TcpClient(conf, this);
  }


  public TcpClient getClient() {
    return client;
  }


  public TimeReceiver setClient(TcpClient client) {
    if(client != null)
      this.client = client;
    return this;
  }
  
  
  public Configuration getConfiguration() {
    return conf;
  }
  
  
  public TimeReceiver setConfiguration(Configuration c) {
    if(c != null) {
      conf = c;
      client.setConfiguration(conf);
    }
    return this;
  }
  

  public String getContent() {
    return content;
  }
  
  
  public void connect() throws IOException {
    client.connect();
  }
  
  
  public ReceiveListener getListener() {
    return listener;
  }


  public TimeReceiver setListener(ReceiveListener listener) {
    if(listener != null)
      this.listener = listener;
    return this;
  }
  
  
  public void stop() {
    try {
      client.close();
    } catch(Exception ex) {
      listener.error(ex);
    }
  }


  @Override
  public void connected(TcpChannel ch) {
    HttpRequest req = new HttpRequest();
    
    String addr = conf.getRemoteAddress()
        + ":" + String.valueOf(conf.getRemotePort());
    
    if(addr.contains("://"))
      req.setAddress(conf.getRemoteAddress());
    else
      req.setHost(addr);
    
    if(conf.getProxyUser() != null
        && !conf.getProxyUser().isEmpty())
      req.setProxyAuth(conf.getProxyUser()
          + ":" + conf.getProxyPass());
    System.out.println("* Request:");
    System.out.println(req.build());
    
    ch.write(req.build().getBytes());
    ch.eof();
  }


  @Override
  public void disconnected(TcpChannel ch) {
    this.stop();
  }


  @Override
  public void closed(TcpChannel ch) {
    this.stop();
  }


  @Override
  public void error(Throwable th, TcpChannel ch) {
    listener.error(th);
    this.stop();
  }


  @Override
  public void received(DynamicBuffer buffer, TcpChannel ch) {
    
    System.out.println("* size = "+ buffer.size());
    if(buffer.isEmpty()) return;
    
    HtmlBuilder h = new HtmlBuilder(buffer.readString());
    String sdate = h.getContentOfTag("<h2>");
    SimpleDate date = SimpleDate.parseDate(sdate, SimpleDate.YYYYMMDD_HHMMSS_DASH);
    
    System.out.println("* RESPONSE:");
    System.out.println(h.getHtml());
    
    listener.received(date);
  }
  
}
