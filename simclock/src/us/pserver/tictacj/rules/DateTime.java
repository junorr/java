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

package us.pserver.tictacj.rules;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalUnit;
import java.util.Date;
import us.pserver.tictacj.util.NotNull;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 28/11/2015
 */
public interface DateTime {

  public Date toDate();
  
  public LocalDateTime toLocalDT();
  
  public ZonedDateTime toZonedDT();
  
  public long toTime();
  
  public Instant toInstant();
  
  public DateTime plus(long amount, TemporalUnit tu);
  
  public DateTime minus(long amount, TemporalUnit tu);
  
  
  public static DateTime of(Date date) {
    return new ImplDateTime(date);
  }
  
  
  public static DateTime of(long time) {
    return new ImplDateTime(time);
  }
  
  
  public static DateTime of(LocalDateTime ldt) {
    return new ImplDateTime(ldt);
  }
  
  
  public static DateTime of(ZonedDateTime zdt) {
    return new ImplDateTime(zdt);
  }
  
  
  public static DateTime of(Instant ist) {
    return new ImplDateTime(ist);
  }
  
  
  public static DateTime of(DateTime dtm) {
    return new ImplDateTime(dtm);
  } 
  
  
  public static DateTime now() {
    return new ImplDateTime(ZonedDateTime.now());
  }
  
  
  
  
  static class ImplDateTime implements DateTime {
    
    private final Date date;
    
    
    ImplDateTime() {
      date = new Date();
    }
    
    
    ImplDateTime(Date date) {
      this.date = new Date(NotNull.of(date).getOrFail().getTime());
    }
    
    
    ImplDateTime(ZonedDateTime zd) {
      this.date = Date.from(NotNull.of(zd).getOrFail()
          .toInstant()
      );
    }
    
    
    ImplDateTime(LocalDateTime ld) {
      this.date = Date.from(NotNull.of(ld).getOrFail()
          .atZone(ZoneId.systemDefault()).toInstant()
      );
    }
    
    
    ImplDateTime(long millis) {
      date = new Date(millis);
    }
    
    
    ImplDateTime(Instant it) {
      this.date = Date.from(NotNull.of(it).getOrFail());
    }
    
    
    ImplDateTime(DateTime dt) {
      this.date = new Date(NotNull.of(dt).getOrFail().toDate().getTime());
    }
    

    @Override
    public Date toDate() {
      return date;
    }
    
    
    @Override
    public ImplDateTime plus(long amount, TemporalUnit tu) {
      return new ImplDateTime(this.toZonedDT().plus(amount, tu));
    }


    @Override
    public ImplDateTime minus(long amount, TemporalUnit tu) {
      return new ImplDateTime(this.toZonedDT().minus(amount, tu));
    }


    @Override
    public LocalDateTime toLocalDT() {
      return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }


    @Override
    public ZonedDateTime toZonedDT() {
      return date.toInstant().atZone(ZoneId.systemDefault());
    }


    @Override
    public long toTime() {
      return date.getTime();
    }
    
    
    @Override
    public Instant toInstant() {
      return date.toInstant();
    }
    
    
    @Override
    public String toString() {
      return this.toZonedDT().toString();
    }
    
  }

  
  public static void main(String[] args) {
    DateTime now = DateTime.now();
    ImplDateTime dt = new ImplDateTime(new Date());
    ImplDateTime dtlo = new ImplDateTime(System.currentTimeMillis());
    ImplDateTime dtzd = new ImplDateTime(ZonedDateTime.now());
    ImplDateTime dtld = new ImplDateTime(LocalDateTime.now());
    System.out.println("now().....: "+ now);
    System.out.println("fromDate(): "+ dt);
    System.out.println("fromLong(): "+ dtlo);
    System.out.println("fromZoDt(): "+ dtzd);
    System.out.println("fromLoDt(): "+ dtld);
  }
  
}
