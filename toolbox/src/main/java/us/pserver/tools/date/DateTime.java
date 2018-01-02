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

package us.pserver.tools.date;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalUnit;
import java.util.Date;
import us.pserver.tools.NotNull;

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
	
	public default String format(String pattern) {
		return DateTimeFormatter
				.ofPattern(pattern)
				.format(this.toLocalDT());
	}
  
  
  public static DateTime of(Date date) {
    return new DateTimeImpl(date);
  }
  
  
  public static DateTime of(long time) {
    return new DateTimeImpl(time);
  }
	
	
	public static DateTime of(int year, int month, int day, int hour, int min, int sec) {
		return of(LocalDateTime.of(year, month, day, hour, min, sec));
	}
  
  
	public static DateTime of(int ... fields) {
		if(fields == null || fields.length == 0) {
			throw new IllegalArgumentException(
					"Invalid empty DateTime fields"
			);
		}
		return of(LocalDateTime.of(
				(fields.length >= 1 ? fields[0] : 0), 
				(fields.length >= 2 ? fields[1] : 1), 
				(fields.length >= 3 ? fields[2] : 1), 
				(fields.length >= 4 ? fields[3] : 0), 
				(fields.length >= 5 ? fields[4] : 0), 
				(fields.length >= 6 ? fields[5] : 0)
		));
	}
  
  
  public static DateTime of(LocalDateTime ldt) {
    return new DateTimeImpl(ldt);
  }
  
  
  public static DateTime of(ZonedDateTime zdt) {
    return new DateTimeImpl(zdt);
  }
  
  
  public static DateTime of(Instant ist) {
    return new DateTimeImpl(ist);
  }
  
  
  public static DateTime of(DateTime dtm) {
    return new DateTimeImpl(dtm);
  } 
  
  
  public static DateTime now() {
    return new DateTimeImpl(ZonedDateTime.now());
  }
  
  
  
  
  static class DateTimeImpl implements DateTime {
    
    private Date date;
    
    
    DateTimeImpl() {
      date = new Date();
    }
    
    
    DateTimeImpl(Date date) {
      this.date = new Date(NotNull.of(date).getOrFail().getTime());
    }
    
    
    DateTimeImpl(ZonedDateTime zd) {
      this.date = Date.from(NotNull.of(zd).getOrFail()
          .toInstant()
      );
    }
    
    
    DateTimeImpl(LocalDateTime ld) {
      this.date = Date.from(NotNull.of(ld).getOrFail()
          .atZone(ZoneId.systemDefault()).toInstant()
      );
    }
    
    
    DateTimeImpl(long millis) {
      date = new Date(millis);
    }
    
    
    DateTimeImpl(Instant it) {
      this.date = Date.from(NotNull.of(it).getOrFail());
    }
    
    
    DateTimeImpl(DateTime dt) {
      this.date = new Date(NotNull.of(dt).getOrFail().toDate().getTime());
    }
    

    @Override
    public Date toDate() {
      return date;
    }
    
    
    @Override
    public DateTimeImpl plus(long amount, TemporalUnit tu) {
      return new DateTimeImpl(this.toZonedDT().plus(amount, tu));
    }


    @Override
    public DateTimeImpl minus(long amount, TemporalUnit tu) {
      return new DateTimeImpl(this.toZonedDT().minus(amount, tu));
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
    DateTimeImpl dt = new DateTimeImpl(new Date());
    DateTimeImpl dtlo = new DateTimeImpl(System.currentTimeMillis());
    DateTimeImpl dtzd = new DateTimeImpl(ZonedDateTime.now());
    DateTimeImpl dtld = new DateTimeImpl(LocalDateTime.now());
    System.out.println("now().....: "+ now);
    System.out.println("fromDate(): "+ dt);
    System.out.println("fromLong(): "+ dtlo);
    System.out.println("fromZoDt(): "+ dtzd);
    System.out.println("fromLoDt(): "+ dtld);
    System.out.println("zone="+ ZoneId.systemDefault());
  }
  
}
