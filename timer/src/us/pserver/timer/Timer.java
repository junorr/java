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

package us.pserver.timer;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import us.pserver.date.DateDiff;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 29/06/2015
 */
public interface Timer {
  
  public Timer cloneTimer();
  
  public Timer start();
  
  public Timer stop();
  
  public Timer lap();
  
  public Timer lapAndStop();
  
  public Timer clear();
  
  public List<Long> laps();
  
  public long lapsSum();
  
  public int lapsSize();
  
  public double lapsAverage();
  
  public long lapsMin();
  
  public long lapsMax();
  
  public long elapsed();
  
  public double elapsedMillis();
  
  public long getStart();
  
  public long getEnd();
  
  public Timer lapsElapsedFromStart();
  
  public Timer lapsElapsedFromLast();
  
  public List<Double> lapsToMillis();
  
  
  public static List<Double> nanosToMillis(List<Long> list) {
    if(list == null || list.isEmpty())
      return Collections.EMPTY_LIST;
    ArrayList<Double> dl = new ArrayList<>(list.size());
    list.forEach(l->dl.add(nanosToMillis(l)));
    return dl;
  }
  
  public static double nanosToMillis(long lng) {
    return lng / 1_000_000.0;
  }
  
  
  
  static abstract class AbstractTimer implements Timer {
    
    long start;
    
    long end;
    
    List<Long> laps;
    
    
    protected AbstractTimer() {
      start = -1;
      end = -1;
      laps = Collections.synchronizedList(new ArrayList<Long>());
    }
    
    
    protected Timer clone(AbstractTimer tm) {
      tm.start = start;
      tm.end = end;
      tm.laps.clear();
      tm.laps.addAll(laps);
      return tm;
    }
    
    
    @Override
    protected Timer clone() {
      return cloneTimer();
    }
    
    
    protected void addLap(long lapRaw) {
      if(start < 0) start = lapRaw;
      laps.add(lapRaw);
    }
    
    
    @Override
    public Timer clear() {
      start = end = 0;
      laps.clear();
      return this;
    }
    
    
    @Override
    public List<Long> laps() {
      return laps;
    }
    
    
    @Override
    public Timer lapsElapsedFromStart() {
      if(laps.isEmpty()) return new Timer.Millis();
      AbstractTimer tm = (AbstractTimer) this.cloneTimer();
      ArrayList<Long> ls = new ArrayList<>(laps.size());
      laps.forEach(l->ls.add(l - start));
      tm.laps = ls;
      return tm;
    }
    
    
    @Override
    public Timer lapsElapsedFromLast() {
      if(laps.isEmpty()) return new Timer.Millis();
      AbstractTimer tm = (AbstractTimer) this.cloneTimer();
      ArrayList<Long> ls = new ArrayList<>(laps.size());
      AtomicLong ant = new AtomicLong(start);
      laps.forEach(l->{
        ls.add(l - ant.get());
        ant.set(l);
      });
      tm.laps = ls;
      return tm;
    }
    
    
    @Override
    public long lapsSum() {
      AtomicLong sum = new AtomicLong(0L);
      laps.forEach(l->sum.set(sum.get() + l));
      return sum.get();
    }
    
    
    @Override
    public int lapsSize() {
      return laps.size();
    }
    
    
    @Override
    public double lapsAverage() {
      if(laps.isEmpty()) return 0;
      return (double)lapsSum() / lapsSize();
    }
    
    
    @Override
    public long lapsMin() {
      if(laps.isEmpty()) return 0;
      return Collections.min(laps);
    }
    
    
    @Override
    public long lapsMax() {
      if(laps.isEmpty()) return 0;
      return Collections.max(laps);
    }
    
    
    @Override
    public long elapsed() {
      if(start < 0 || end < 0) return 0;
      return end - start;
    }
    
    
    @Override
    public long getStart() {
      return start;
    }
    
    
    @Override
    public long getEnd() {
      return end;
    }
    
    
    public double round(double num, int dec) {
      long i = (long) num;
      long d = Math.round((num - i) * Math.pow(10, dec));
      return i + d / Math.pow(10, dec);
    }
    
    
    @Override
    public String toString() {
      StringBuffer sb = new StringBuffer();
      DecimalFormat df = new DecimalFormat("0.000#ms");
      Timer tc = this.lapsElapsedFromLast();
      double min, avr, max;
      min = tc.lapsMin();
      max = tc.lapsMax();
      avr = tc.lapsAverage();
      if(tc instanceof Timer.Nanos) {
        min = round(nanosToMillis((long)min), 4);
        max = round(nanosToMillis((long)max), 4);
        avr = round(nanosToMillis((long)avr), 4);
      }
      sb.append("Timer{ elapsed: ")
          .append(df.format(round(tc.elapsedMillis(), 4)))
          .append(", laps: ")
          .append(laps.size())
          .append(", min-max: ")
          .append(df.format(min))
          .append("-")
          .append(df.format(max))
          .append(", average: ")
          .append(df.format(avr))
          .append(" }");
      return sb.toString();
    }
    
  }
  
  
  public static final class Nanos extends AbstractTimer {
    
    @Override
    public Timer cloneTimer() {
      return clone(new Timer.Nanos());
    }
    
    
    @Override
    public Nanos start() {
      start = System.nanoTime();
      return this;
    }


    @Override
    public Nanos stop() {
      end = System.nanoTime();
      return this;
    }


    @Override
    public Nanos lap() {
      addLap(System.nanoTime());
      return this;
    }
    
    
    @Override
    public Nanos lapAndStop() {
      long n = System.nanoTime();
      addLap(n);
      end = n;
      return this;
    }
    
    
    @Override
    public double elapsedMillis() {
      if(start < 0 || end < 0) return 0;
      return elapsed() / 1_000_000.0;
    }
    
    
    @Override
    public List<Double> lapsToMillis() {
      return nanosToMillis(laps);
    }
    
  }
  
  
  public static final class Millis extends AbstractTimer {
    
    @Override
    public Timer cloneTimer() {
      return clone(new Timer.Millis());
    }
    
    
    @Override
    public Millis start() {
      start = System.currentTimeMillis();
      return this;
    }


    @Override
    public Millis stop() {
      end = System.currentTimeMillis();
      return this;
    }


    @Override
    public Millis lap() {
      addLap(System.currentTimeMillis());
      return this;
    }
    
    
    @Override
    public Millis lapAndStop() {
      long n = System.currentTimeMillis();
      addLap(n);
      end = n;
      return this;
    }
    
    
    @Override
    public double elapsedMillis() {
      return elapsed();
    }
    
    
    @Override
    public List<Double> lapsToMillis() {
      if(laps.isEmpty()) return Collections.EMPTY_LIST;
      ArrayList<Double> ls = new ArrayList<>(laps.size());
      laps.forEach(l->ls.add(l.doubleValue()));
      return ls;
    }
    
    
    public List<DateDiff> lapsToDiff() {
      if(laps.isEmpty()) return Collections.EMPTY_LIST;
      long ant = start;
      List<DateDiff> ls = new ArrayList<>(laps.size());
      for(long l : laps) {
        ls.add(new DateDiff(new Date(ant), new Date(l)));
        ant = l;
      }
      return ls;
    }
    
    
    public DateDiff getDateDiff() {
      if(start < 0 || end < 0)
        return null;
      return new DateDiff(new Date(start), new Date(end));
    }
    
  }
  
}
