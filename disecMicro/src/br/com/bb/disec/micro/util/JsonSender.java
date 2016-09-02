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
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 25/08/2016
 */
public class JsonSender {
  
  private static final IoCallback callback = new IoCallback() {
      public void onComplete(HttpServerExchange hse, Sender sender) {}
      public void onException(HttpServerExchange hse, Sender sender, IOException ioe) {}
  };
  
  private final Sender sender;
  
  private ByteBuffer buffer;
  
  private final Charset charset;
  
  
  public JsonSender(Sender snd) {
    if(snd == null) {
      throw new IllegalArgumentException("Bad Null Sender");
    }
    this.sender = snd;
    charset = Charset.forName("UTF-8");
    buffer = ByteBuffer.allocateDirect(4096);
  }
  
  
  public Sender getSender() {
    return sender;
  }
  
  
  public JsonSender flush() {
    if(buffer.position() > 0) {
      buffer.flip();
      sender.send(buffer, callback);
      buffer.clear();
    }
    return this;
  }
  
  
  public JsonSender write(String s) {
    if(s == null || s.isEmpty()) return this;
    return this.write(s.getBytes(charset));
  }
  
  
  private JsonSender write(byte[] bs) {
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
  
  
  public JsonSender put(String s) {
    if(s == null || s.isEmpty()) return this;
    return this.write("\"").write(s).write("\"");
  }
  
  
  public JsonSender put(Number n) {
    if(n == null) return this;
    return this.write(Objects.toString(n));
  }
  
  
  public JsonSender put(Boolean b) {
    if(b == null) return this;
    return this.write(Objects.toString(b));
  }
  
  
  public JsonSender put(Date d) {
    if(d == null) return this;
    return this.put(Objects.toString(
        LocalDateTime.ofInstant(
            Instant.ofEpochMilli(d.getTime()), 
            ZoneId.systemDefault()))
    );
  }
  
  
  public JsonSender startObject() {
    return this.write("{");
  }
  
  
  public JsonSender startObject(String key) {
    return this.put(key).write(":{");
  }
  
  
  public JsonSender endObject() {
    return this.write("}");
  }
  
  
  public JsonSender startArray(String key) {
    return this.put(key).write(":[");
  }
  
  
  public JsonSender startArray() {
    return this.write("[");
  }
  
  
  public JsonSender endArray() {
    return this.write("]");
  }
  
  
  public JsonSender nextElement() {
    return this.write(",");
  }
  
  
  public JsonSender put(String key, String value) {
    if(key == null 
        || key.isEmpty() 
        || value == null 
        || value.isEmpty()) {
      return this;
    }
    return this.put(key).write(":").put(value);
  }
  
  
  public JsonSender put(String key, Number value) {
    if(key == null 
        || key.isEmpty() 
        || value == null) {
      return this;
    }
    return this.put(key).write(":").put(value);
  }
  
  
  public JsonSender put(String key, Boolean value) {
    if(key == null 
        || key.isEmpty() 
        || value == null) {
      return this;
    }
    return this.put(key).write(": ").put(value);
  }
  
  
  
  public JsonSender put(String key, Object value) {
    if(key == null 
        || key.isEmpty() 
        || value == null) {
      return this;
    }
    this.put(key).write(": ");
    if(CharSequence.class.isAssignableFrom(value.getClass())) {
      this.put(value.toString());
    } else {
      this.write(Objects.toString(value));
    }
    return this;
  }
  
  
  public JsonSender put(String key, Date value) {
    if(key == null 
        || key.isEmpty() 
        || value == null) {
      return this;
    }
    return this.put(key).write(": ").put(value);
  }
  
}
