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

package com.jpower.net.echo;

import com.jpower.log.LogFile;
import com.jpower.log.LogFormatterImpl;
import com.jpower.log.LogLevel;
import com.jpower.log.LogPrinter;
import com.jpower.log.Logger;
import com.jpower.net.ConnectionHandler;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Random;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 30/05/2013
 */
public class EchoClientHandler implements ConnectionHandler {
  
  public static final Logger log = new Logger()
      .add(new LogFile("./echoclient.log"))
      .add(new LogPrinter(
      new LogFormatterImpl("# (DATE) [LEVEL]: MESSAGE")), 
      LogLevel.ERROR, LogLevel.FATAL)
      .add(new LogPrinter(), LogLevel.INFO, LogLevel.WARN);
  
  public static final String ss = "clientsocket";


  private String string;
  
  private Random rand;
  
  int id;
  
  
  public EchoClientHandler() {
    rand = new Random();
    id = -1;
  }
  
  
  public EchoClientHandler(int i) {
    this();
    id = i;
  }


  @Override
  public void received(ByteBuffer buffer) {
    byte[] bs = new byte[buffer.limit()];
    buffer.get(bs);
    String s = new String(bs);
    log.info("RECEIVED: "+ s);
    fillString();
  }


  @Override
  public void connected(SocketChannel channel) {
    log.info("ClientHandler: Connected!");
    fillString();
  }
  
  
  private void fillString() {
    if(id >= 0) {
      string  = ss + "("+ id+ ")_"+ String.valueOf(rand.nextInt()) + "\n\r";
    } else {
      string  = ss + String.valueOf(rand.nextInt()) + "\n\r";
    }
  }


  @Override
  public void error(Throwable th) {
    log.error(th);
  }


  @Override
  public ByteBuffer sending() {
    if(string != null) {
      ByteBuffer buf = ByteBuffer.allocateDirect(string.length());
      buf.put(string.getBytes());
      string = null;
      return buf;
    } else return null;
  }


  @Override
  public boolean isSending() {
    return string != null;
  }


  @Override
  public void sent(int bytes) {
    log.info("Message sent("+ bytes+ ")!");
  }


  @Override
  public void disconnected(SocketChannel channel) {
    log.info("Handler: Connection closed!");
  }
  
}
