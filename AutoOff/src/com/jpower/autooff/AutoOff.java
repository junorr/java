/*
 * Direitos Autorais Reservados (c) 2011 Juno Roesler
 * Contato: juno.rr@gmail.com
 * 
 * Esta biblioteca é software livre; você pode redistribuí-la e/ou modificá-la sob os
 * termos da Licença Pública Geral Menor do GNU conforme publicada pela Free
 * Software Foundation; tanto a versão 2.1 da Licença, ou (a seu critério) qualquer
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
package com.jpower.autooff;

import com.jpower.date.DateDiff;
import com.jpower.date.SimpleDate;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Properties;


/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 17/02/2012
 */
public class AutoOff {

  public static final String 
      CMD = "cmd /c",
      SHUTDOWN = "shutdown",
      ARGS = "/s /t",
      COMMENT = "/c";
  
  public static final String 
      KEY_DELAY = "DELAY",
      KEY_COMMENT = "COMMENT",
      KEY_TIME = "TIME",
      KEY_WEEKEND_TIME = "WEEKEND_TIME";
  
  public static final String 
      DEFAULT_COMMENT = "O computador será desligado em 30 segundos...",
      DEFAULT_TIME = "22:00:00",
      DEFAULT_WEEKEND_TIME = "00:00:00";
  
  public static final int
      DEFAULT_DELAY = 30;
      
  public static final String
      CONFIG_FILE = "autooff.properties";
  
  
  private Properties prop;
  
  private SystemCommand cmd;
  
  private SimpleDate time, weekend;
  
  private int delay;
  
  private String comment;
  
  
  public AutoOff() {
    cmd = new SystemCommand();
    prop = new Properties();
    if(!this.configExists()) 
      this.createDefaultConfig();
    else
      this.load();
  }
  
  
  public void setTime(int hh, int mm, int ss) {
    time = new SimpleDate();
    time.setUseDaylightTime(true);
    time.hour(hh);
    time.minute(mm);
    time.second(ss);
    if(hh == 0)
      time.addDay(1);
  }
  
  
  public void setWeekend(int hh, int mm, int ss) {
    weekend = new SimpleDate();
    weekend.setUseDaylightTime(true);
    weekend.hour(hh);
    weekend.minute(mm);
    weekend.second(ss);
    if(hh == 0)
      weekend.addDay(1);
  }


  public SystemCommand getCmd() {
    return cmd;
  }


  public void setCmd(SystemCommand cmd) {
    this.cmd = cmd;
  }


  public String getComment() {
    return comment;
  }


  public void setComment(String comment) {
    this.comment = comment;
  }


  public int getDelay() {
    return delay;
  }


  public void setDelay(int delay) {
    this.delay = delay;
  }


  public SimpleDate getTime() {
    return time;
  }


  public void setTime(SimpleDate time) {
    this.time = time;
  }


  public SimpleDate getWeekend() {
    return weekend;
  }


  public void setWeekend(SimpleDate weekend) {
    this.weekend = weekend;
  }
  
  
  private int[] parse(String s) {
    if(s == null || s.length() < 3 || !s.contains(":")) return null;
    String[] ss = s.split(":");
    int[] t = new int[3];
    try {
      t[0] = Integer.parseInt(ss[0]);
      t[1] = Integer.parseInt(ss[1]);
      t[2] = 0;
      if(ss.length > 2)
        t[2] = Integer.parseInt(ss[2]);
      return t;
    } catch(NumberFormatException ex) {
      ex.printStackTrace();
      return null;
    }
  }
  
  
  private void parseTime(String s) {
    int[] t = this.parse(s);
    if(t == null) return;
    this.setTime(t[0], t[1], t[2]);
  }
  
  
  private void parseWeekend(String s) {
    int[] t = this.parse(s);
    if(t == null) return;
    this.setWeekend(t[0], t[1], t[2]);
  }
  
  
  private void getConfig() {
    if(prop == null) return;
    try {
      this.parseTime(prop.getProperty(KEY_TIME));
      this.parseWeekend(prop.getProperty(KEY_WEEKEND_TIME));
      this.setDelay(Integer.parseInt(prop.getProperty(KEY_DELAY)));
      this.setComment(prop.getProperty(KEY_COMMENT));
    } catch(Exception ex) {
      ex.printStackTrace();
    }
  }
  
  
  public void setConfig() {
    if(time == null || weekend == null) return;
    if(prop == null)
      prop = new Properties();
    
    String stime = time.hour() +":"+ 
        time.minute() +":"+ time.second();
    prop.setProperty(KEY_TIME, stime);
    stime = weekend.hour() +":"+ 
        weekend.minute() +":"+ weekend.second();
    prop.setProperty(KEY_WEEKEND_TIME, stime);
    prop.setProperty(KEY_DELAY, String.valueOf(delay));
    prop.setProperty(KEY_COMMENT, this.comment);
  }
  
  
  private boolean configExists() {
    return new File(CONFIG_FILE).exists();
  }
  
  
  private void createDefaultConfig() {
    this.parseTime(DEFAULT_TIME);
    this.parseWeekend(DEFAULT_WEEKEND_TIME);
    comment = DEFAULT_COMMENT;
    delay = DEFAULT_DELAY;
    this.save();
  }
  
  
  public boolean shouldRun() {
    SimpleDate end = null;
    SimpleDate now = new SimpleDate();
    now.setUseDaylightTime(true);
    if(this.isWeekend()) {
      end = weekend.clone().hour(6).minute(0).second(0);
      end.setUseDaylightTime(true);
      if(weekend.hour() <= 23 && weekend.hour() >= 12)
        end.addDay(1);
      if(now.isBetween(weekend, end))
        return true;
    } else {
      end = time.clone().hour(6).minute(0).second(0);
      end.setUseDaylightTime(true);
      if(time.hour() <= 23 && time.hour() >= 12)
        end.addDay(1);
      if(now.isBetween(time, end))
        return true;
    }
    return false;
  }
  
  
  public long schedule() {
    SimpleDate now = new SimpleDate();
    now.setUseDaylightTime(true);
    SimpleDate end = null;
    if(this.isWeekend()) end = weekend;
    else end = time;
    end.setUseDaylightTime(true);
    DateDiff df = new DateDiff(now, end);
    df.calculate();
    System.out.println(now);
    System.out.println(end);
    System.out.println(df.toMinutes());
    return df.toMinutes();
  }
  
  
  public boolean isWeekend() {
    SimpleDate now = new SimpleDate();
    return (now.getCalendar().get(Calendar.DAY_OF_WEEK) 
        == Calendar.SATURDAY ||
        now.getCalendar().get(Calendar.DAY_OF_WEEK) 
        == Calendar.SUNDAY);
  }
  
  
  public void waitSchedule() {
    final Object o = new Object();
    synchronized(o) {
      try {
        o.wait(this.schedule() * 60 * 1000);
      } catch (InterruptedException ex) {
        ex.printStackTrace();
      }
    }
  }
  
  
  public boolean execute() {
    if(cmd == null) return false;
    
    if(!this.shouldRun()) {
      System.out.println("ShouldRun: "+false);
      this.waitSchedule();
    }
    
    cmd.setCommand(SHUTDOWN);
    cmd.setArgs(ARGS, String.valueOf(delay), COMMENT, "\"", comment, "\"");
    cmd.waitProcessEnd(true);
    System.out.println(cmd.toString());
    cmd.run();
    System.out.println(cmd.getCommandLine());
    System.out.println(cmd.getExitCode());
    return cmd.getExitCode() != 9;
  }
  
  
  public void cancelOff() {
    if(cmd == null) return;
    cmd.setCommand(SHUTDOWN);
    cmd.setArgs("/a");
    cmd.waitProcessEnd(false);
    cmd.run();
  }
  
  
  public void save() {
    this.setConfig();
    try {
      FileOutputStream fos = 
          new FileOutputStream(CONFIG_FILE);
      prop.store(fos, "AUTOOFF CONFIGS");
    } catch(IOException ex) {
      ex.printStackTrace();
    }
  }
  
  
  public void load() {
    try {
      FileInputStream fis = 
          new FileInputStream(CONFIG_FILE);
      prop.load(fis);
      this.getConfig();
    } catch(IOException ex) {
      ex.printStackTrace();
    }
  }
  
  
  public static void main(String[] args) throws Exception {
    AutoOff off = new AutoOff();
    off.execute();
  }
}
