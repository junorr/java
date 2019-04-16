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

package us.pserver.tools;

import us.pserver.tools.date.DateDiff;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * A Timer object for measuring time intervals.
 * It has two default implementations for Timer, 
 * <code>Timer.Nanos</code> for nanoseconds precision
 * and <code>Timer.Millis</code> for milliseconds precision.
 * @author Juno Roesler - juno@pserver.us
 */
public interface Timer {
  
  /**
   * Close this instance.
   * @return A new cloned Timer instance.
   */
  public Timer cloneTimer();
  
  /**
   * Start the Timer.
   * @return This modified Timer instance.
   */
  public Timer start();
  
  /**
   * Stop the Timer.
   * @return This modified Timer instance.
   */
  public Timer stop();

  /**
   * Adds a new lap time.
   * @return This modified Timer instance.
   */
  public Timer lap();

  /**
   * Adds a new lap time and stop the Timer.
   * @return This modified Timer instance.
   */
  public Timer lapAndStop();

  /**
   * 
   * @return This modified Timer instance.
   */
  public Timer clear();
  
  /**
   * Get the laps List.
   * @return the laps List.
   */
  public List<Long> laps();
  
  /**
   * Get the sum of all laps.
   * @return the sum of all laps.
   */
  public long lapsSum();
  
  /**
   * Get the number of laps in list.
   * @return the number of laps in list.
   */
  public int lapsSize();
  
  /**
   * Get the average of all laps.
   * @return the average of all laps.
   */
  public double lapsAverage();
  
  /**
   * Get the minimum lap in List.
   * @return the minimum lap in List.
   */
  public long lapsMin();
  
  /**
   * Get the maximum lap in List.
   * @return the maximum lap in List.
   */
  public long lapsMax();
  
  /**
   * Get the total amount of time elapsed between start and stop.
   * @return the total amount of time elapsed between start and stop.
   */
  public long elapsed();
  
  /**
   * Get the total amount of time in millis elapsed between start and stop.
   * @return the total amount of time in millis elapsed between start and stop.
   */
  public double elapsedMillis();

  /**
   * Get the start time.
   * @return the start time.
   */
  public long getStart();
  
  /**
   * Get the stop time.
   * @return the stop time.
   */
  public long getStop();

  /**
   * 
   * @return This modified Timer instance.
   */
  public Timer lapsElapsedFromStart();
  
  /**
   * 
   * @return This modified Timer instance.
   */
  public Timer lapsElapsedFromLast();
  
  public List<Double> lapsToMillis();
  
  
  /**
   * Convert a list with milliseconds values to a nanoseconds.
   * @param list List with milliseconds values to be converted.
   * @return New List with nanoseconds values.
   */
  public static List<Double> nanosToMillis(List<Long> list) {
    if(list == null || list.isEmpty())
      return Collections.EMPTY_LIST;
    ArrayList<Double> dl = new ArrayList<>(list.size());
    list.forEach(l->dl.add(nanosToMillis(l)));
    return dl;
  }

  
  /**
   * Convert millisenconds to nanoseconds.
   * @param lng Millisenconds value.
   * @return Nanoseconds value.
   */
  public static double nanosToMillis(long lng) {
    return lng / 1_000_000.0;
  }
  
  
  
  /**
   * Abstract implementation of Timer.
   */
  static abstract class AbstractTimer implements Timer {
    
    long start;
    
    long end;
    
    List<Long> laps;
    
    
    /**
     * Default constructor without arguments.
     */
    protected AbstractTimer() {
      start = -1;
      end = -1;
      laps = Collections.synchronizedList(new ArrayList<Long>());
    }
    
    
    /**
     * Clone Timer into a new instance with same values.
     * @param tm The Timer to clone.
     * @return The cloned Timer.
     */
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
    
    
    /**
     * Adds a new lap in the laps list.
     * @param lapRaw Time of the lap.
     */
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
      double avr = (double)lapsSum() / lapsSize();
      if(this instanceof Timer.Nanos) {
       avr = round(nanosToMillis((long)avr), 4);
      }
      return avr;
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
    public long getStop() {
      return end;
    }
    
    
    /**
     * Rounds a double value to the given decimal precision.
     * @param num The double to be rounded.
     * @param dec Decimal precision.
     * @return The rounded value.
     */
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
  
  
  /**
   * Nanoseconds precision <code>Timer</code> implementation.
   */
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
      return lapAndStop();
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
  
  
  /**
   * The milliseconds precision Timer implementation.
   */
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
      return lapAndStop();
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
    
    
    /**
     * Get amount difference of time between laps.
     * @return List with the amount difference of time between laps.
     */
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
    
    
    /**
     * Get the total amount difference of time elapsed between start and stop.
     * @return the total amount difference of time elapsed between start and stop.
     */
    public DateDiff getDateDiff() {
      if(start < 0 || end < 0)
        return null;
      return new DateDiff(new Date(start), new Date(end));
    }
    
  }
  
}
