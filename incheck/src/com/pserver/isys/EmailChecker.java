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

package com.pserver.isys;

import com.jpower.conf.Config;
import com.jpower.inet.INotesConnector;
import com.jpower.inet.INotesHeaderMail;
import us.pserver.cdr.b64.Base64StringCoder;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.management.ManagementFactory;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import us.pserver.log.LogHelper;
import us.pserver.log.Logging;
import us.pserver.scron.ExecutionContext;
import us.pserver.scron.Job;
import us.pserver.scronv6.SCronV6;
import us.pserver.scron.Schedule;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 14/08/2013
 */
public class EmailChecker {

  public static final int 
      DEF_MAILCHECK_SECONDS = 180,
      DEF_PIDCHECK_SECONDS = 30;
  
  public static final String 
      
      PID_FILE_NAME = "./incheck.pid",
      
      CONF_FILE_NAME = "./incheck.conf",
      
      TEMP_DIR_NAME = "./temp/",
      
      SCRIPT_DIR_NAME = "./scp/",
      
      
      KEY_CHECK_TIME_SECONDS = "CHECK_TIME_SECONDS",
      
      KEY_CREDENTIALS_STR = "CREDENTIALS",
      
      KEY_TEMP_DIR = "TEMP_DIR";
  
  public static final LogHelper LOG = Logging.getConfigured(EmailChecker.class);
  
  
  protected class CheckMailJob implements Job {
    @Override
    public void execute(ExecutionContext context) throws Exception {
      LOG.info("Checking e-mail...");
      EmailChecker.this.parseMails(connector.refreshInbox());
    }
    @Override
    public void error(Throwable th) {
      LOG.error(th.toString());
    }
  }
  
  
  protected class PidChecker implements Job {
    @Override
    public void execute(ExecutionContext context) throws Exception {
      int pid = EmailChecker.this.getPid();
      if(pid <= 0) {
        LOG.info("PID not found. EXITING...");
        EmailChecker.this.stop();
        System.exit(0);
      }
    }
    @Override
    public void error(Throwable th) {}
  }
  
  
  private Path tempDir;
  
  private String credentials;
  
  private int checkTime;
  
  private Config conf;
  
  private SCronV6 cron;
  
  private INotesConnector connector;
  
  private EmailParser eparser;
  
  
  public EmailChecker() {
    conf = new Config(CONF_FILE_NAME);
    checkTime = DEF_MAILCHECK_SECONDS;
    cron = new SCronV6();
    cron.setShutdownAtEmpty(false);
    eparser = new EmailParser(this);
    connector = new INotesConnector(LOG);
    init();
  }
    
    
  public void init() {
    if(conf.getFile().exists()) {
      conf.load();
      checkTime = conf.getInt(KEY_CHECK_TIME_SECONDS);
      tempDir = Paths.get(conf.get(KEY_TEMP_DIR));
      if(!Files.exists(tempDir))
        try { Files.createDirectories(tempDir); }
        catch(IOException ex) {}
      credentials = conf.get(KEY_CREDENTIALS_STR);
    }
    else {
      conf.put(KEY_CHECK_TIME_SECONDS, checkTime);
      conf.put(KEY_CREDENTIALS_STR, " ");
      tempDir = Paths.get(TEMP_DIR_NAME);
      if(!Files.exists(tempDir))
        try { Files.createDirectories(tempDir); }
        catch(IOException ex) {}
      conf.put(KEY_TEMP_DIR, tempDir);
      conf.save();
    }
    this.writePid();
  }
  
  
  public void writePid() {
    try (PrintStream ps = 
        new PrintStream(PID_FILE_NAME);) {
      
      String pidname = ManagementFactory
          .getRuntimeMXBean().getName();
      
      if(pidname == null) {
        String msg = "Invalid PID. Could not retrieve from JVM";
        LOG.fatal(msg);
        throw new IllegalStateException(msg);
      }
      
      ps.println(pidname.substring(0, 
          pidname.indexOf("@")));
      ps.flush();
      
    } catch(IOException e) {
      String error = "Error writing EmailChecker PID";
      LOG.fatal(error);
      LOG.fatal(e.toString());
      throw new RuntimeException(
          "Error writing EmailChecker PID", e);
    }
  }
  
  
  public void deletePid() {
    try {
      Files.delete(Paths.get(PID_FILE_NAME));
    } catch(IOException e) {
      LOG.error(e.toString());
    }
  }
  
  
  public int getPid() {
    try (BufferedReader br = 
        new BufferedReader(
        new FileReader(PID_FILE_NAME))) {
      
      return Integer.parseInt(br.readLine());
      
    } catch(Exception e) {
      LOG.error("Error getting PID");
      return -1;
    }
  }


  public Path getTempDir() {
    return tempDir;
  }


  public EmailChecker setTempDir(Path tempDir) {
    this.tempDir = tempDir;
    if(tempDir != null)
      conf.put(KEY_TEMP_DIR, tempDir).save();
    return this;
  }


  public String getCredentials() {
    return credentials;
  }


  public EmailChecker setCredentials(String credentials) {
    this.credentials = credentials;
    if(credentials != null && !credentials.isEmpty())
      conf.put(KEY_CREDENTIALS_STR, credentials).save();
    return this;
  }
  
  
  public EmailChecker setCredentials(String user, String password) {
    if(user != null && password != null
        && !user.isEmpty() 
        && !password.isEmpty()) {
      Base64StringCoder sc = new Base64StringCoder();
      credentials = sc.encode(
          user.concat(":").concat(password));
      conf.put(KEY_CREDENTIALS_STR, credentials).save();
    }
    return this;
  }


  public int getCheckTime() {
    return checkTime;
  }


  public EmailChecker setCheckTime(int checkTime) {
    this.checkTime = checkTime;
    if(checkTime > 0)
      conf.put(KEY_CHECK_TIME_SECONDS, checkTime).save();
    return this;
  }


  public SCronV6 getSCronV6() {
    return cron;
  }


  public INotesConnector getINotesConnector() {
    return connector;
  }


  public LogHelper getLog() {
    return LOG;
  }


  public void stop() {
    cron.stop();
  }
  
  
  public EmailChecker start() {
    if(credentials == null
        || credentials.isEmpty())
      return this;
    
    LOG.info("Starting EmailChecker (PID="+ this.getPid()+ ")...");
    
    Base64StringCoder sc = new Base64StringCoder();
    String dec = sc.decode(credentials);
    connector.setCredentials(
        dec.substring(0, dec.indexOf(":")), 
        dec.substring(dec.indexOf(":")+1));
    connector.setTempDir(tempDir.toString());
    connector.connect();
    LOG.info("Checking e-mail...");
    
    cron.put(new Schedule()
          .repeatInSeconds(checkTime).startNow(), 
          new CheckMailJob())
        .put(new Schedule()
          .repeatInSeconds(DEF_PIDCHECK_SECONDS)
          .startNow(), new PidChecker());
    System.out.println("* CheckMail: "+ new Schedule().repeatInSeconds(checkTime).getCountdown());
    return this;
  }
  
  
  protected void parseMails(List<INotesHeaderMail> mails) {
    if(mails == null || mails.isEmpty())
      return;
    
    boolean found = false;
    for(INotesHeaderMail hmail : mails) {
      String subj = hmail.getSubject();
      if(subj == null || subj.trim().isEmpty())
        continue;
      if(subj.equals(EmailDefinition
          .TITLE_EXEC_SYSTEM_COMMAND.getTag())
          || subj.equals(EmailDefinition
          .TITLE_EXEC_ATTACHED_SCRIPT.getTag())
          || subj.equals(EmailDefinition
          .TITLE_EXEC_DEFINED_SCRIPT.getTag())) {
        
        LOG.info("!!! Inportant e-mail received: "+ subj);
        eparser.parse(hmail);
        found = true;
      }
    }//for
    if(!found) LOG.info("No important e-mail found");
  }
  
}
