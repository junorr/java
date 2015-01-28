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

package us.pserver.tmo;

import com.thoughtworks.xstream.XStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import murlen.util.fscript.ParserListener;
import us.pserver.log.SimpleLog;
import us.pserver.scronv6.hide.Pair;
import us.pserver.scron.Schedule;
import us.pserver.scron.SimpleCron;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 29/04/2014
 */
public class Timeouts implements ParserListener {
  
  public static final String 
      LOG_FILE = "timeouts.log",
      CONF_FILE = "timeouts.xml";

  
  private final SimpleCron cron;
  
  private SimpleLog log;
  
  private final ScriptExecutor sexec;
  
  private transient XStream xs;
  
  private List<ScriptPair> list;
  
  
  public Timeouts() {
    list = new ArrayList<>();
    cron = new SimpleCron();
    log = new SimpleLog(LOG_FILE);
    cron.setLogger(log)
        .setLogEnabled(true);
    sexec = new ScriptExecutor(cron);
    xs = new XStream();
  }
  
  
  public SimpleLog getLogger() {
    return log;
  }
  
  
  public List<ScriptPair> list() {
    return list;
  }
  
  
  public ScriptExecutor getScriptExecutor() {
    return sexec;
  }
  
  
  public void update() {
    if(cron.jobs().isEmpty())
      return;
    list.forEach(sp-> {
      if(sp.schedule().isRepeatEnabled()
          && sp.schedule().getCountdown() <= 0)
        sp.schedule().reeschedule();
    });
  }
  
  
  public void load() {
    Path p = Paths.get(CONF_FILE);
    if(Files.exists(p)) {
      try {
        list.addAll((List<ScriptPair>) xs.fromXML(
            Files.newInputStream(
            p, StandardOpenOption.READ)));
        list.forEach(this::set);
        list.forEach(sp->cron.put(sp.schedule(), sp.job()));
      log.debug("Timeouts Loaded!");
      } catch(IOException e) {
        log.error("Error loading config file");
        log.error(e.toString());
      }
    }
  }
  
  
  protected ScriptPair set(ScriptPair p) {
    if(p == null 
        || p.job() == null)
      return p;
    p.job().setExecutor(sexec);
    return p;
  }
  
  
  public void save() {
    Path p = Paths.get(CONF_FILE);
    try {
      xs.toXML(list, 
          Files.newOutputStream(p, 
          StandardOpenOption.WRITE, 
          StandardOpenOption.CREATE));
      log.debug("Timeouts Saved!");
    } catch(IOException e) {
      log.error("Error saving schedules");
      log.error(e.toString());
    }
  }


  @Override
  public void lineUpdate(String line) {
    log.info("> "+ line);
  }
  
  
  public boolean isRunning() {
    return cron.isRunning() && log != null;
  }
  
  
  public void start() {
    if(log == null) {
      log = new SimpleLog(LOG_FILE);
      cron.setLogger(log);
    }
    this.load();
    if(!cron.isRunning())
      cron.start();
    log.info("Timeouts Started!");
  }
  
  
  public void stop() {
    cron.stop();
    this.save();
    log.close().clearOutputs();
    log.info("Timeouts Stopped!");
  }
  
  
  public void add(String script, Schedule sched) {
    if(script == null) {
      throw new IllegalArgumentException(
          "Invalid Script: "+ script);
    }
    if(sched == null || !sched.isValid()) {
      throw new IllegalArgumentException(
          "Invalid Schedule: "+ sched);
    }
    if(!isRunning()) start();
    ScriptPair sp = new ScriptPair(sched, new ScriptJob(script, sexec));
    list.add(set(sp));
    save();
    cron.put(sched, sp.job());
  }
  
  
  public void add(ScriptPair p) {
    if(p == null) {
      throw new IllegalArgumentException(
          "Invalid Pair: "+ p);
    }
    if(!isRunning()) start();
    list.add(set(p));
    save();
    cron.put(p.schedule(), p.job());
  }
  
  
  private void updateCron() {
    cron.stop();
    cron.jobs().clear();
    if(!list.isEmpty())
      list.forEach(sp->cron.put(sp.schedule(), sp.job()));
    cron.start();
  }
  
  
  public void edit(int index, ScriptPair sp) {
    if(index < 0 || index >= list.size()
        || sp == null) return;
    list.set(index, set(sp));
    save();
    updateCron();
  }
  
  
  public void remove(int index) {
    if(index < 0 || index >= list.size()) 
      return;
    list.remove(index);
    save();
    updateCron();
  }
  
  
  public ScriptPair get(int index) {
    if(index < 0 || index >= list.size()) 
      return null;
    return list.get(index);
  }
  
}
