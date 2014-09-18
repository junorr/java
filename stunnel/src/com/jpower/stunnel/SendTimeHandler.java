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
import com.jpower.nnet.http.HtmlBuilder;


/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 17/07/2012
 */
public class SendTimeHandler implements ConnectionHandler {
  
  public static int SEND_INTERVAL = 5000;
  
  private boolean send;
  
  
  @Override
  public void connected(TcpChannel ch) {}


  @Override
  public void disconnected(TcpChannel ch) {
    send = false;
    ch.close();
  }


  @Override
  public void closed(TcpChannel ch) {
    System.out.println("CLOSED");
    send = false;
  }


  @Override
  public void error(Throwable th, TcpChannel ch) {
    send = false;
    ch.close();
  }


  @Override
  public void received(DynamicBuffer buffer, TcpChannel ch) {
    send = true;
    System.out.println(" * REQUEST RECEIVED:");
    System.out.println(buffer.readString());
    System.out.println("--------------------------------");
    while(send && ch.isWritable()) {
      SimpleDate date = new SimpleDate();
      System.out.println("* Sending date info...");
      String sdate = date.format(SimpleDate.YYYYMMDD_HHMMSS_DASH);
      System.out.println("  [ "+ sdate + " ]");
      
      HtmlBuilder hb = new HtmlBuilder();
      hb.openH(2).append(sdate);
      
      DynamicBuffer buf = new DynamicBuffer();
      buf.writeString(hb.build());
      buf.writeString("\n");
      
      ch.write(buf);
      ch.flush();
      
      try { Thread.sleep(SEND_INTERVAL); } 
      catch(InterruptedException ex) {}
    }
  }
  
}
