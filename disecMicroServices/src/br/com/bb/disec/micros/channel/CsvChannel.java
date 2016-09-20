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
public class CsvChannel extends AbstractCachedChannel {
  
  public static final char DEFAULT_COL_DELIMITER = ';';
  
  public static final char DEFAULT_TEXT_DELIMITER = '"';
  
  
  private final char coldelim;
  
  private final char textdelim;
  
  private final DecimalFormat df;
  
  
  public CsvChannel(WritableByteChannel chn, char colDelimiter, char textDelimiter) {
    super(chn);
    this.coldelim = colDelimiter;
    this.textdelim = textDelimiter;
    df = new DecimalFormat("#0.00#######");
    df.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.US));
  }
  
  
  public CsvChannel(WritableByteChannel chn) {
    this(chn, DEFAULT_COL_DELIMITER, DEFAULT_TEXT_DELIMITER);
  }
  
  
  public char getColumnDelimiter() {
    return coldelim;
  }
  
  
  public char getTextDelimiter() {
    return textdelim;
  }
  
  
  @Override
  public CsvChannel put(Object o) {
    if(o == null) {
      put("");
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
  public CsvChannel put(String s) {
    write(String.valueOf(textdelim));
    if(s != null) {
      write(s.replace("\"", "'").replace(";", ","));
    }
    write(String.valueOf(textdelim));
    return this;
  }
  
  
  @Override
  public CsvChannel put(Number n) {
    if(n != null) {
      write((n.toString().contains(".") 
          ? df.format(n) : Objects.toString(n)));
    }
    return this;
  }
  
  
  @Override
  public CsvChannel put(Boolean b) {
    if(b != null) {
      write(Objects.toString(b));
    }
    return this;
  }
  
  
  @Override
  public CsvChannel put(Date d) {
    if(d != null) {
      this.put(Objects.toString(
          LocalDateTime.ofInstant(
              Instant.ofEpochMilli(d.getTime()), 
              ZoneId.systemDefault()))
      );
    }
    return this;
  }
  
  
  public CsvChannel nextElement() {
    write(String.valueOf(coldelim));
    return this;
  }
  
}
