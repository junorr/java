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

package br.com.bb.disec.micros.util;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 01/09/2016
 */
public interface DateFormatter {
  
  public Instant getInstant();
  
  public default String toIsoDateTime() {
    return getInstant().toString();
  }

  public default String toIsoDate() {
    return DateTimeFormatter.ofPattern("yyyy-MM-dd")
        .format(getInstant().atZone(ZoneId.systemDefault()));
  }

  public default String toBRDateTime() {
    return DateTimeFormatter.ofPattern("dd/MM/yyyy' 'HH:mm:ss")
        .format(getInstant().atZone(ZoneId.systemDefault()));
  }

  public default String toBRDate() {
    return DateTimeFormatter.ofPattern("dd/MM/yyyy")
        .format(getInstant().atZone(ZoneId.systemDefault()));
  }
  
  
  public static DateFormatter of(Instant i) {
    return new DefaultFormatter(i);
  }
  
  
  public static DateFormatter of(Date d) {
    return new DefaultFormatter(d);
  }
  
  
  public static DateFormatter of(Timestamp t) {
    return new DefaultFormatter(t);
  }
  
  
  
  
  
  
  static class DefaultFormatter implements DateFormatter {
    
    private final Instant instant;
    
    public DefaultFormatter(Instant i) {
      if(i == null) {
        throw new IllegalArgumentException("Bad Null Instant");
      }
      this.instant = i;
    }

    public DefaultFormatter(Date d) {
      if(d == null) {
        throw new IllegalArgumentException("Bad Null Date");
      }
      this.instant = Instant.ofEpochMilli(d.getTime());
    }

    public DefaultFormatter(Timestamp t) {
      if(t == null) {
        throw new IllegalArgumentException("Bad Null Timestamp");
      }
      this.instant = Instant.ofEpochMilli(t.getTime());
    }

    @Override
    public Instant getInstant() {
      return instant;
    }
    
  }

}
