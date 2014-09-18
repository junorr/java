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

package com.jpower.sys;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.management.ManagementFactory;
import java.nio.file.Files;
import java.nio.file.Paths;
import us.pserver.log.SLogV2;
import us.pserver.scronV6.SCronV6;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 02/10/2012
 */
public class Syscheck {
  
  public static final String PID_FILE_NAME = "./sys.pid";
  
  public static final String SYSCHECK = Syscheck.class.getCanonicalName();
  
  
  private SysConfig conf;
  
  private SCronV6 cron;
  
  private ProcessKiller killer;
  
  private ClearMemoryWatcher cleaner;
  
  private SnapshotJob snapjob;
  
  private LastSnapshotJob lastjob;
  
  private NotificationJob notjob;
  
  private StopperJob stop;
  
  private final SLogV2 log;
  
  
  public Syscheck() {
    cron = new SCronV6();
    cron.dataMap().put(SYSCHECK, this);
    log = Log.logger();
    conf = new SysConfig();
  }
  
  
  public void writePid() {
    try (PrintStream ps = 
        new PrintStream(PID_FILE_NAME);) {
      
      String pidname = ManagementFactory
          .getRuntimeMXBean().getName();
      
      if(pidname == null) {
        String msg = "Invalid PID. Could not retrieve from JVM";
        log.fatal(msg);
        throw new IllegalStateException(msg);
      }
      
      ps.println(pidname.substring(0, 
          pidname.indexOf("@")));
      ps.flush();
      
    } catch(IOException e) {
      String error = "Error writing Syscheck PID";
      log.fatal(error);
      log.fatal(e.toString());
      throw new RuntimeException(error);
    }
  }
  
  
  public void deletePid() {
    try {
      Files.delete(Paths.get(PID_FILE_NAME));
    } catch(IOException e) {
      log.error(e.toString());
    }
  }
  
  
  public int getPid() {
    try (BufferedReader br = 
        new BufferedReader(
        new FileReader(PID_FILE_NAME))) {
      
      return Integer.parseInt(br.readLine());
      
    } catch(Exception e) {
      return -1;
    }
  }


  public void startServices() {
    log.info("Syscheck init...");
    
    this.writePid();
    int pid = this.getPid();
    if(pid == -1) {
      log.error("Invalid PID. Exiting...");
      System.exit(1);
    }
    log.info("Syscheck PID="+ pid);
    
    log.info("Starting Services...");
    DB.setDbMaxSize(conf.getDbMaxSize());
    DB.getServer();
    
    killer = new ProcessKiller();
    cleaner = new ClearMemoryWatcher();
    snapjob = new SnapshotJob();
    snapjob.setInterval(conf.getSyscheckInterval());
    lastjob = new LastSnapshotJob();
    lastjob.setInterval(conf.getInterfaceInterval());
    notjob = new NotificationJob();
    stop = new StopperJob();
    
    killer.start();
    log.info("ProcessKiller...\t[OK]");
    
    cleaner.start();
    log.info("MemoryCleaner...\t[OK]");
    
    cron.put(snapjob.getSchedule(), snapjob);
    log.info("SnapshotJob...  \t[OK]");
    
    cron.put(lastjob.getSchedule(), lastjob);
    log.info("LastSnapshotJob...\t[OK]");
    
    cron.put(notjob.getSchedule(), notjob);
    log.info("NotificationJob...\t[OK]");
    
    cron.put(stop.getSchedule(), stop);
    log.info("Syscheck started and running!");
  }
  
  
  public void stopServices() {
    log.info(" ");
    log.info("Stopping services...");
    
    killer.stop();
    log.info("ProcessKiller...\t[Stopped]");
    
    cleaner.stop();
    log.info("MemoryCleaner...\t[Stopped]");
    
    cron.stop();
    log.info("SCronV6...   \t[Stopped]");
    
    log.stop();
    log.info("SLogV2...   \t[Stopped]");
    
    log.info("Exiting now!");
    DB.getServer().close();
    System.exit(0);
  }
  
  
  public static void main(String[] args) {
    Syscheck sys = new Syscheck();
    sys.startServices();
  }
  
}
