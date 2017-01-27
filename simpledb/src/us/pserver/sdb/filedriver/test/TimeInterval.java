package us.pserver.sdb.filedriver.test;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class TimeInterval implements Comparable {

	private final long start;
	
	private final long end;
	
	
	public TimeInterval(long start, long end) {
		this.start = start;
		this.end = end;
	}
	
	
	public long getStartTime() {
		return start;
	}
	
	
	public long getEndTime() {
		return end;
	}
	
	
	public long getInterval() {
		return end - start;
	}
	
	
	public TimeInterval until(TimeInterval end) {
		return new TimeInterval(start, end.getEndTime());
	}
	
	
	public TimeInterval from(TimeInterval start) {
		return new TimeInterval(start.getStartTime(), end);
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (end ^ (end >>> 32));
		result = prime * result + (int) (start ^ (start >>> 32));
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TimeInterval other = (TimeInterval) obj;
		if (end != other.end)
			return false;
		return start == other.start;
	}


	@Override
	public int compareTo(Object o) {
		if(equals(o)) return 0;
		if(o == null || !getClass()
				.isAssignableFrom(o.getClass())) {
			return -1;
		}
		TimeInterval ti = (TimeInterval) o;
		return this.getInterval() < ti.getInterval() ? -1 : 0;
	}
	
	
	@Override
	public TimeInterval clone() {
		return new TimeInterval(start, end);
	}
  
  
	public long getMillisInterval() {
    long time = this.getInterval();
		return time % 1000;
	}
	
	
	public long getSecondsInterval() {
    long time = this.getInterval();
    return time / 1000 >= 60 
        ? time % (1000 * 60) / 1000 
        : time / 1000;
	}
	
	
	public long getMinutesInterval() {
    long time = this.getInterval();
    return time / (1000 * 60) >= 60 
        ? time % (1000 * 60 * 60) / 1000 
        : time / (1000 * 60);
	}
	
	
	public long getHoursInterval() {
    long time = this.getInterval();
		return time / (1000 * 60 * 60) >= 60 
        ? time % (1000 * 60 * 60 * 24) / 1000 
        : time / (1000 * 60 * 60);
	}
	
	
	public long getDaysInterval() {
    long time = this.getInterval();
		return time / (1000 * 60 * 60 * 24);
	}
	
	
  @Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		NumberFormat nf = new DecimalFormat("00");
		long ms = this.getMillisInterval();
    long s = this.getSecondsInterval();
    long m = this.getMinutesInterval();
		long h = this.getHoursInterval();
		long d = this.getDaysInterval();
    if(d > 0) sb.append(d).append(" days ");
    if(h > 0) sb.append(nf.format(h)).append("h ");
    if(m > 0) sb.append(nf.format(m)).append("m ");
    if(s > 0) sb.append(nf.format(s)).append("s ");
    return sb.append(ms).append("ms").toString();
	}
	
	
	public static TimeInterval start() {
		return new TimeInterval(System.currentTimeMillis(), 0);
	}
	
	
	public static TimeInterval stop() {
		return new TimeInterval(0, System.currentTimeMillis());
	}
	
	
	public static TimeInterval stop(TimeInterval start) {
		return stop().from(start);
	}
	
	
	public static void main(String[] args) throws InterruptedException {
    long plus = 1000*60*2 + 1000*3 + 800;
    System.out.println(plus);
    System.out.println(plus / (1000 * 60));
		TimeInterval s = TimeInterval.start();
    //Thread.sleep(1000 * 60 * 2 + 1000 * 10 + 800);
		TimeInterval e = TimeInterval.stop();
		e = new TimeInterval(s.getStartTime(), e.getEndTime() + plus);
		System.out.println("-> "+ e.toString());
	}
	
}
