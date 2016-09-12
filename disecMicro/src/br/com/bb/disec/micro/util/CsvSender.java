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
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 25/08/2016
 */
public class CsvSender extends AbstractSender {
  
  public static final char DEFAULT_COL_DELIMITER = ';';
  
  public static final char DEFAULT_TEXT_DELIMITER = '"';
  
  
  private final char coldelim;
  
  private final char textdelim;
  
  private final DecimalFormat df;
  
  
  public CsvSender(Sender snd, char colDelimiter, char textDelimiter) {
    super(snd);
    this.coldelim = colDelimiter;
    this.textdelim = textDelimiter;
    df = new DecimalFormat("#0.00#######");
    df.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.US));
  }
  
  
  public CsvSender(Sender snd) {
    this(snd, DEFAULT_COL_DELIMITER, DEFAULT_TEXT_DELIMITER);
  }
  
  
  public char getColumnDelimiter() {
    return coldelim;
  }
  
  
  public char getTextDelimiter() {
    return textdelim;
  }
  
  
  @Override
  public CsvSender put(Object o) {
    if(o == null) {
      write("");
      return this;
    }
    if(CharSequence.class.isAssignableFrom(o.getClass())) {
      put(o.toString());
    }
    else if(Date.class.isAssignableFrom(o.getClass())) {
      put((Date)o);
    }
    else if(Number.class.isAssignableFrom(o.getClass())) {
      put((Number)o);
    }
    else {
      write(Objects.toString(o));
    }
    return this;
  }
  
  
  @Override
  public CsvSender put(String s) {
    if(s == null || s.isEmpty()) return this;
    write(String.valueOf(textdelim))
        .write(s.replace("\"", "'").replace(";", ","))
        .write(String.valueOf(textdelim));
    return this;
  }
  
  
  @Override
  public CsvSender put(Number n) {
    if(n == null) return this;
    write((n.toString().contains(".") ? df.format(n) : Objects.toString(n)));
    return this;
  }
  
  
  @Override
  public CsvSender put(Boolean b) {
    if(b == null) return this;
    write(Objects.toString(b));
    return this;
  }
  
  
  @Override
  public CsvSender put(Date d) {
    if(d == null) return this;
    return this.put(Objects.toString(
        LocalDateTime.ofInstant(
            Instant.ofEpochMilli(d.getTime()), 
            ZoneId.systemDefault()))
    );
  }
  
  
  public CsvSender nextElement() {
    write(String.valueOf(coldelim));
    return this;
  }
  
}
