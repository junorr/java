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

import com.jpower.simplemail.MailServer;
import com.jpower.simplemail.Message;
import com.jpower.simplemail.SMail;
import com.jpower.simplemail.SMailException;
import com.jpower.sys.security.AES256Cipher;
import com.jpower.sys.security.Password;
import com.jpower.sys.security.SHA256;
import java.io.File;
import java.io.Serializable;
import us.pserver.date.SimpleDate;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 03/10/2012
 */
public class Email implements Serializable {
  
  public static final File IMG_HEADER = new File("./header-mail.png");

  
  private SMail smail;
  
  private MailServer server;
  
  private NotificationConf conf;
  
  private Snapshot snapshot;
  
  private boolean messageSent;
  
  private SimpleDate lastSend;
  
  
  public Email() {
    conf = new NotificationConf();
    snapshot = null;
    initServer();
    smail = new SMail(server);
    messageSent = false;
  }
  
  
  private void initServer() {
    if(conf.getConfigFile().exists()) {
      server = new MailServer()
          .setServer(conf.getEmailServerAddr())
          .setPort(conf.getEmailServerPort())
          .setProtocol(conf.getEmailProtocol())
          .setUser(conf.getEmailSender())
          .setPassword(conf.getEmailPasswd());
    }
    else {
      server = MailServer.GMAIL_SMTP_SERVER;
    }
  }


  public NotificationConf getConf() {
    return conf;
  }


  public Email setConf(NotificationConf conf) {
    this.conf = conf;
    return this;
  }


  public Snapshot getSnapshot() {
    return snapshot;
  }


  public Email setSnapshot(Snapshot snapshot) {
    this.snapshot = snapshot;
    return this;
  }
  
  
  private void setMessageContent(Message msg) {
    msg.setContentHtml(
        "<div style='border: solid 2px gray; width: 500px; background-color: #E2E2E0;'>"
      + msg.embedImage(IMG_HEADER)
      + "  <h4 style='text-indent: 10px;'>Critical Limit Reached!</h4>"
      + "  <table>"
      + "    <tr>"
      + "      <td style='width: 20px;'></td>"
      + "      <td colspan='2' style='width: 470px;'>"
      + "        The resources of the machine \"<i>"+ conf.getMachineName()+ "</i>\","
      + "        reached the critical limit configured:"
      + "      </td>"
      + "      <td style='width: 10px;'></td>"
      + "    </tr>"
      + "    <tr>"
      + "      <td style='width: 20px;'></td>"
      + "      <td>"
      + "        <b>CPU Usage:</b>"
      + "      </td>"
      + "      <td>"+ snapshot.getCpu().getUsedCpu()+ " % </td>"
      + "      <td style='width: 10px;'></td>"
      + "    </tr>"
      + "    <tr>"
      + "      <td style='width: 20px;'></td>"
      + "      <td>"
      + "        <b>Memory Usage:</b>"
      + "      </td>"
      + "      <td>"+ snapshot.getMem().usedPerc()+ " % </td>"
      + "      <td style='width: 10px;'></td>"
      + "    </tr>"
      + "    <tr>"
      + "      <td style='width: 20px;'></td>"
      + "      <td>"
      + "        <b>Disk Usage ("+ conf.getDiskDevice()+ "):</b>"
      + "      </td>"
      + "      <td>"+ conf.getDisk(snapshot.getDisks())
                          .getUsedPerc() + " % </td>"
      + "      <td style='width: 10px;'></td>"
      + "    </tr>"
      + "    <tr>"
      + "      <td style='width: 20px;'></td>"
      + "      <td>"
      + "        <b>Network Load Avrg ("+ conf.getIFNetName()+ "):</b>"
      + "      </td>"
      + "      <td>"+ conf.getNet(snapshot.getInterfaces())
                          .getLoad().getTotalAverage()+ " bytes/s </td>"
      + "      <td style='width: 10px;'></td>"
      + "    </tr>"
      + "  </table>"
      + "  <h4 style='text-indent: 10px;'>PowerStat Diagnostic System.</h4>"
      + "</div>");
  }
  
  
  public boolean send() {
    SimpleDate now = new SimpleDate();
    if(lastSend != null && lastSend.equalsDate(now)) {
      if(!messageSent)
        Log.logger().info("Email already sent in "
            + lastSend.format(SimpleDate.DDMMYYYY_SLASH));
      
      messageSent = true;
      return false;
    }
    
    conf.load();
    
    if((snapshot == null || snapshot.getCpu() == null
        || snapshot.getDisks() == null 
        || snapshot.getInterfaces() == null
        || snapshot.getMem() == null)
        && !messageSent) {
      Log.logger().error("Invalid Snapshot!");
      return false;
    }
    
    Log.logger().info("Sending e-mail...");
    
    if(snapshot.getCpu().getUsedCpu() < 0 
        || snapshot.getMem().usedPerc() < 0 
        || conf.getDisk(snapshot.getDisks()) == null
        || conf.getDisk(snapshot.getDisks()).getUsedPerc() < 0
        || conf.getNet(snapshot.getInterfaces()) == null
        || conf.getNet(snapshot.getInterfaces()).getLoad() == null
        || conf.getMachineName() == null) {
      Log.logger().error("Fail on send. Invalid Data.");
      return false;
    }
    
    if(conf.getAdminEmail() == null 
        || conf.getAdminEmail().isEmpty()) {
      Log.logger().error("Fail on send. Invalid email address.");
      return false;
    }
    
    if(conf.getEmailSender() == null 
        || conf.getEmailSender().isEmpty()) {
      Log.logger().error("Fail on send. Invalid email configurations.");
      return false;
    }
    
    if(conf.getEmailPasswd() == null 
        || conf.getEmailPasswd().isEmpty()) {
      Log.logger().error("Fail on send. Invalid email configurations.");
      return false;
    }
    
    Password pass = Password.rawPassword(conf.getDecryptHash());
    AES256Cipher aes = new AES256Cipher(pass);
    byte[] bytes = SHA256.fromHexString(conf.getEmailPasswd());
    String passwd = new String(aes.decrypt(bytes));
    
    server.setUser(conf.getEmailSender())
        .setPassword(passwd);
    smail.setServer(server);
    
    Message msg = new Message();
    msg.setSubject("PowerStat: Critical Limit Reached!");
    msg.from(conf.getEmailSender());
    msg.to(conf.getAdminEmail());
    this.setMessageContent(msg);

    lastSend = new SimpleDate();
    
    try {
      smail.send(msg);
    } catch(SMailException ex) {
      throw new RuntimeException(ex);
    }
    
    Log.logger().info("Email sent!");
    Log.logger().info("    Subject: PowerStat: Critical Limit Reached!");
    Log.logger().info("    To     : "+ conf.getAdminEmail());
    return true;
  }

}
