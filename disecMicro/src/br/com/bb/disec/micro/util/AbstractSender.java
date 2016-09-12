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

package br.com.bb.disec.micro.util;

import io.undertow.io.IoCallback;
import io.undertow.io.Sender;
import io.undertow.server.HttpServerExchange;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 12/09/2016
 */
public abstract class AbstractSender {
  
  public static final String CRLF = "\r\n";
  
  
  protected final IoCallback callback = new IoCallback() {
      @Override
      public void onComplete(HttpServerExchange hse, Sender sender) {
        sent.set(true);
      }
      @Override
      public void onException(HttpServerExchange hse, Sender sender, IOException ioe) {
        sent.set(true);
      }
  };
  
  protected final Sender sender;
  
  protected final AtomicBoolean sent;
  
  protected ByteBuffer buffer;
  
  protected final Charset charset;
  
  
  public AbstractSender(Sender snd) {
    if(snd == null) {
      throw new IllegalArgumentException("Bad Null Sender");
    }
    this.sender = snd;
    charset = Charset.forName("UTF-8");
    buffer = ByteBuffer.allocateDirect(4096);
    sent = new AtomicBoolean(Boolean.TRUE);
  }
  
  
  public Sender getSender() {
    return sender;
  }
  
  
  public AbstractSender flush() {
    while(!sent.get()) {}
    if(buffer.position() > 0) {
      buffer.flip();
      sent.set(false);
      sender.send(buffer, callback);
      buffer.clear();
    }
    return this;
  }
  
  
  public AbstractSender write(String s) {
    if(s == null || s.isEmpty()) return this;
    return this.write(s.getBytes(charset));
  }
  
  
  protected AbstractSender write(byte[] bs) {
    if(bs.length < buffer.capacity()) {
      if(bs.length > buffer.remaining()) {
        this.flush();
      }
      buffer.put(bs);
    }
    else {
      
    }
    if(bs.length < buffer.capacity()) {
      int rounds = bs.length / buffer.capacity();
      if(bs.length % buffer.capacity() > 0) {
        rounds++;
      }
      for(int i = 0; i < x.length; i++) {
        
      }
    }
    int start = 0;
    int count = 0;
    
    while(start < bs.length) {
      
      count = Math.min(
          (bs.length-start), 
          buffer.remaining()
      );
      buffer.put(bs, start, count);
      flush();
      start += count;
    }
    return this;
  }
  
  
  public AbstractSender newLine() {
    return this.write(CRLF);
  }
  
  
  public abstract AbstractSender put(Object obj);
  
  public abstract AbstractSender put(String str);
  
  public abstract AbstractSender put(Number num);
  
  public abstract AbstractSender put(Boolean bol);
  
  public abstract AbstractSender put(Date dte);
  
}
