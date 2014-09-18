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
import java.util.List;
import murlen.util.fscript.ParserListener;
import us.pserver.log.SimpleLog;
import us.pserver.scronv6.hide.Pair;
import us.pserver.scronv6.SCronV6;
import us.pserver.scron.Schedule;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 29/04/2014
 */
public class Timeouts implements ParserListener {
  
  public static final String 
      LOG_FILE = "timeouts.log",
      CONF_FILE = "timeouts.xml";

  
  private final SCronV6 cron;
  
  private SimpleLog log;
  
  private final ScriptExecutor sexec;
  
  private transient XStream xs;
  
  
  public Timeouts() {
    cron = new SCronV6();
    log = new SimpleLog(LOG_FILE);
    cron.setLogger(log)
        .setLogEnabled(true);
    sexec = new ScriptExecutor(cron);
    xs = new XStream();
  }
  
  
  public SimpleLog getLogger() {
    return log;
  }
  
  
  public SCronV6 getCron() {
    return cron;
  }
  
  
  public ScriptExecutor getScriptExecutor() {
    return sexec;
  }
  
  
  public void load() {
    Path p = Paths.get(CONF_FILE);
    if(Files.exists(p)) {
      try {
        List<Pair> list = (List<Pair>) xs.fromXML(
            Files.newInputStream(
            p, StandardOpenOption.READ));
        list.forEach(this::set);
        cron.list().addAll(list);
      log.debug("Timeouts Loaded!");
      } catch(IOException e) {
        log.error("Error loading config file");
        log.error(e.toString());
      }
    }
  }
  
  
  private Pair set(Pair p) {
    if(p == null 
        || p.job() == null ||
        !(p.job() instanceof ScriptJob))
      return p;
    ((ScriptJob) p.job()).setExecutor(sexec);
    return p;
  }
  
  
  public void save() {
    if(cron.list().isEmpty())
      return;
    Path p = Paths.get(CONF_FILE);
    try {
      boolean run = cron.isRunning();
      if(run) cron.stop();
      xs.toXML(cron.list().copy(), 
          Files.newOutputStream(p, 
          StandardOpenOption.WRITE, 
          StandardOpenOption.CREATE));
      if(run) cron.start();
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
    cron.start();
    log.info("Timeouts Started!");
  }
  
  
  public void stop() {
    this.save();
    cron.stop();
    log.close().clearOutputs();
    log = null;
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
    cron.put(sched, new ScriptJob(script, sexec));
  }
  
  
  public void add(ScriptPair p) {
    if(p == null) {
      throw new IllegalArgumentException(
          "Invalid Pair: "+ p);
    }
    if(!isRunning()) start();
    this.set(p);
    cron.put(p.schedule(), p.job());
  }
  
}
