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

package br.com.bb.disec.micros.channel;

import java.nio.channels.WritableByteChannel;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 25/08/2016
 */
public class JsonChannel extends AbstractCachedChannel {
  
  private final DecimalFormat df;
  
  
  public JsonChannel(WritableByteChannel chn) {
    super(chn);
    df = new DecimalFormat("#0.00#######");
    df.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.US));
  }
  
  
  @Override
  public JsonChannel put(String s) {
    if(s == null || s.isEmpty()) return this;
    write("\"").write(s.replace("\"", "'")).write("\"");
    return this;
  }
  
  
  @Override
  public JsonChannel put(Number n) {
    if(n == null) return this;
    write((n.toString().contains(".") ? df.format(n) : Objects.toString(n)));
    return this;
  }
  
  
  @Override
  public JsonChannel put(Boolean b) {
    if(b == null) return this;
    write(Objects.toString(b));
    return this;
  }
  
  
  @Override
  public JsonChannel put(Date d) {
    if(d == null) return this;
    return this.put(Objects.toString(
        LocalDateTime.ofInstant(
            Instant.ofEpochMilli(d.getTime()), 
            ZoneId.systemDefault()))
    );
  }
  
  
  @Override
  public JsonChannel put(Object o) {
    if(Date.class.isAssignableFrom(o.getClass())) {
      put((Date) o);
    }
    else if(Number.class.isAssignableFrom(o.getClass())) {
      put((Number) o);
    }
    else if(Boolean.class.isAssignableFrom(o.getClass())) {
      put((Boolean) o);
    }
    else {
      put(Objects.toString(o));
    }
    return this;
  }
  
  
  public JsonChannel startObject() {
    write("{");
    return this;
  }
  
  
  public JsonChannel startObject(String key) {
    put(key).write(":{");
    return this;
  }
  
  
  public JsonChannel endObject() {
    write("}");
    return this;
  }
  
  
  public JsonChannel startArray(String key) {
    put(key).write(":[");
    return this;
  }
  
  
  public JsonChannel startArray() {
    write("[");
    return this;
  }
  
  
  public JsonChannel endArray() {
    write("]");
    return this;
  }
  
  
  public JsonChannel nextElement() {
    write(",");
    return this;
  }
  
  
  public JsonChannel put(String key, String value) {
    if(key == null 
        || key.isEmpty() 
        || value == null 
        || value.isEmpty()) {
      return this;
    }
    put(key).write(":").put(value);
    return this;
  }
  
  
  public JsonChannel put(String key, Number value) {
    if(key == null 
        || key.isEmpty() 
        || value == null) {
      return this;
    }
    put(key).write(":").put(value);
    return this;
  }
  
  
  public JsonChannel put(String key, Boolean value) {
    if(key == null 
        || key.isEmpty() 
        || value == null) {
      return this;
    }
    put(key).write(": ").put(value);
    return this;
  }
  
  
  
  public JsonChannel put(String key, Object value) {
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
  
  
  public JsonChannel put(String key, Date value) {
    if(key == null 
        || key.isEmpty() 
        || value == null) {
      return this;
    }
    put(key).write(": ").put(value);
    return this;
  }
  
}
