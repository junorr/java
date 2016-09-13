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

package br.com.bb.disec.micro.util.parser;

import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Date;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 31/08/2016
 */
public interface DateParser {

  public default Date toDate() {
    return new Date(toInstant().toEpochMilli());
  }
  
  public default java.sql.Date toSqlDate() {
    return new java.sql.Date(toInstant().toEpochMilli());
  }
  
  public default java.sql.Timestamp toTimestamp() {
    return new java.sql.Timestamp(toInstant().toEpochMilli());
  }
  
  public default Instant toInstant() {
    return toLocalDateTime().toInstant(ZoneOffset.UTC);
  }
  
  public default LocalDateTime toLocalDateTime() {
    LocalDateTime dtm;
    try {
      dtm = LocalDateTime.from(parse());
    } catch(DateTimeException e) {
      dtm = LocalDate.from(parse()).atStartOfDay();
    }
    return dtm;
  }
  
  public default ZonedDateTime toZonedDateTime() {
    ZonedDateTime dtm;
    try {
      dtm = ZonedDateTime.from(parse());
    } catch(DateTimeException e) {
      dtm = ZonedDateTime.of(toLocalDateTime(), ZoneId.systemDefault());
    }
    return dtm;
  }
  
  public TemporalAccessor parse();
  
  
  public static DateParser parser(String str) {
    DateParser ret = null;
    if(IsoDateTime.canParse(str)) ret = new IsoDateTime(str);
    else if(IsoDate.canParse(str)) ret = new IsoDate(str);
    else if(BRDateTime.canParse(str)) ret = new BRDateTime(str);
    else if(BRDate.canParse(str)) ret = new BRDate(str);
    return ret;
  }
  
  
  public static boolean isDateString(String str) {
    return parser(str) != null;
  }
  
  
  
  
  
  static class IsoDateTime implements DateParser {
    
    private final String str;
    
    public IsoDateTime(String str) {
      if(!canParse(str)) {
        throw new IllegalArgumentException("Bad Date Format (Allowed: \"2011-12-31T23:59:59Z\")");
      }
      this.str = str;
    }
    
    public static boolean canParse(String str) {
      return !(str == null
          || str.length() != 20
          || str.charAt(4) != '-' 
          || str.charAt(10) != 'T' 
          || str.charAt(19) != 'Z');
    }
    
    @Override
    public TemporalAccessor parse() {
      return ZonedDateTime.parse(str);
    }
    
  }
  
  
  
  
  
  static class IsoDate implements DateParser {
    
    private final String str;
    
    public IsoDate(String str) {
      if(!canParse(str)) {
        throw new IllegalArgumentException("Bad Date Format (Allowed: \"2011-12-31\")");
      }
      this.str = str;
    }
    
    public static boolean canParse(String str) {
      return !(str == null
          || str.length() != 10
          || str.charAt(4) != '-' 
          || str.charAt(7) != '-');
    }
    
    @Override
    public TemporalAccessor parse() {
      return LocalDate.parse(str);
    }
    
  }
  
  
  
  
  
  static class BRDate implements DateParser {
    
    private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
    private final String str;
    
    public BRDate(String str) {
      if(!canParse(str)) {
        throw new IllegalArgumentException("Bad Date Format (Allowed: \"31/12/2011\")");
      }
      this.str = str;
    }
    
    public static boolean canParse(String str) {
      return !(str == null
          || str.length() != 10
          || str.charAt(2) != '/' 
          || str.charAt(5) != '/');
    }
    
    @Override
    public TemporalAccessor parse() {
      return LocalDate.parse(str, fmt);
    }
    
  }
  
  
  
  
  
  
  static class BRDateTime implements DateParser {
    
    private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy' 'HH:mm:ss");
    
    private final String str;
    
    public BRDateTime(String str) {
      if(!canParse(str)) {
        throw new IllegalArgumentException("Bad Date Format (Allowed: \"31/12/2011 23:59:59\")");
      }
      this.str = str;
    }
    
    public static boolean canParse(String str) {
      return !(str == null
          || str.length() != 19
          || str.charAt(2) != '/' 
          || str.charAt(10) != ' '
          || str.charAt(16) != ':');
    }
    
    @Override
    public TemporalAccessor parse() {
      return LocalDateTime.parse(str, fmt);
    }
    
  }
}
