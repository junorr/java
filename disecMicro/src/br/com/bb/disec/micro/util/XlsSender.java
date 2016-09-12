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
public class XlsSender extends AbstractSender {
  
  public static final String XML_VERSION = "<?xml version=\"1.0\"?>";
  
  public static final String WORKBOOK = "<Workbook xmlns=\"urn:schemas-microsoft-com:office:spreadsheet\">";
  
  public static final String END_WORKBOOK = "</Workbook>";
  
  public static final String WORKSHEET = "<Worksheet Name=\"Sheet1\">";
  
  public static final String END_WORKSHEET = "</Worksheet>";
  
  public static final String TABLE = "<Table>";
  
  public static final String END_TABLE = "</Table>";
  
  public static final String ROW = "<Row>";
  
  public static final String END_ROW = "</Row>\r\n";
  
  public static final String CELL = "<Cell>";
  
  public static final String END_CELL = "</Cell>";
  
  public static final String DATA_STRING = "<Data Type=\"String\">";
  
  public static final String DATA_NUMBER = "<Data Type=\"Number\">";
  
  public static final String DATA_DATE = "<Data Type=\"Date\">";
  
  public static final String END_DATA = "</Data>";
  
  
  private boolean openRow, openCell;
  
  private final DecimalFormat df;
  
  
  public XlsSender(Sender snd) {
    super(snd);
    openRow = false;
    openCell = false;
    df = new DecimalFormat("#0.00#######");
    df.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.US));
  }
  
  
  public XlsSender openWorkbook() {
    write(XML_VERSION)
        .write(WORKBOOK)
        .write(WORKSHEET)
        .write(TABLE);
    return this;
  }
  
  
  public XlsSender closeWorkbook() {
    if(openCell) {
      write(END_CELL);
    }
    if(openRow) {
      write(END_ROW);
    }
    write(END_TABLE)
        .write(END_WORKSHEET)
        .write(END_WORKBOOK);
    return this;
  }
  
  
  public XlsSender nextRow() {
    if(openCell) {
      write(END_CELL);
      openCell = false;
    }
    if(openRow) {
      write(END_ROW);
    }
    openRow = true;
    write(ROW);
    return this;
  }
  
  
  public XlsSender nextCell() {
    if(openCell) {
      write(END_CELL);
    }
    openCell = true;
    write(CELL);
    return this;
  }
  
  
  @Override
  public XlsSender put(Object o) {
    if(o == null) {
      write("");
      return this;
    }
    if(Date.class.isAssignableFrom(o.getClass())) {
      put((Date)o);
    }
    else if(Number.class.isAssignableFrom(o.getClass())) {
      put((Number)o);
    }
    else {
      put(o.toString());
    }
    return this;
  }
  
  
  @Override
  public XlsSender put(String s) {
    if(s == null || s.isEmpty()) return this;
    if(!openRow) {
      nextRow();
    }
    nextCell()
        .write(DATA_STRING)
        .write(s.replace("<", "").replace(">", ""))
        .write(END_DATA);
    return this;
  }
  
  
  @Override
  public XlsSender put(Number n) {
    if(n == null) return this;
    if(!openRow) {
      nextRow();
    }
    nextCell()
        .write(DATA_NUMBER)
        .write((n.toString().contains(".") ? df.format(n) : Objects.toString(n)))
        .write(END_DATA);
    return this;
  }
  
  
  @Override
  public XlsSender put(Boolean b) {
    if(b == null) return this;
    if(!openRow) {
      nextRow();
    }
    nextCell()
        .write(DATA_STRING)
        .write(Objects.toString(b))
        .write(END_DATA);
    return this;
  }
  
  
  @Override
  public XlsSender put(Date d) {
    if(d == null) return this;
    if(!openRow) {
      nextRow();
    }
    nextCell()
        .write(DATA_DATE)
        .write(Objects.toString(
            LocalDateTime.ofInstant(
                Instant.ofEpochMilli(d.getTime()), 
                ZoneId.systemDefault())
        )).write(END_DATA);
    return this;
  }
  
}
