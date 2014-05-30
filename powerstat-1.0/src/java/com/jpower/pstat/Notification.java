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

package com.jpower.pstat;

import com.jpower.sys.NotificationConf;
import java.io.File;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 04/10/2012
 */
@ManagedBean
@SessionScoped
public class Notification implements Serializable {
  
  public static final String CONF_FILE = "/notification.conf";

  private String
      adminEmail,
      diskDevice,
      emailServer,
      emailProtocol,
      emailAccount,
      emailPasswd,
      machineName,
      ifnetworkName;
  
  private int emailPort;
  
  private double
      cpuLimit,
      memLimit,
      diskLimit,
      ifnetworkLimit;
  
  private NotificationConf conf;
  
  @ManagedProperty(value="#{security}")
  private SecurityController security;
  
  @ManagedProperty(value="#{webConfig}")
  private WebConfig webc;
  
  
  public Notification() {}


  public WebConfig getWebc() {
    return webc;
  }


  public void setWebc(WebConfig webc) {
    this.webc = webc;
    if(webc != null) {
      conf = new NotificationConf(
          new File(webc.getSyscheckDir() + CONF_FILE));
    }
  }


  public SecurityController getSecurity() {
    return security;
  }


  public void setSecurity(SecurityController security) {
    this.security = security;
  }

  
  public String getAdminEmail() {
    return adminEmail;
  }

  
  public void setAdminEmail(String adminEmail) {
    this.adminEmail = adminEmail;
  }

  
  public double getCpuLimit() {
    this.load();
    return cpuLimit;
  }

  
  public void setCpuLimit(double cpuLimit) {
    this.cpuLimit = cpuLimit;
  }

  
  public double getMemLimit() {
    return memLimit;
  }

  
  public void setMemLimit(double memLimit) {
    this.memLimit = memLimit;
  }

  
  public double getDiskLimit() {
    return diskLimit;
  }

  
  public void setDiskLimit(double diskLimit) {
    this.diskLimit = diskLimit;
  }

  
  public String getDiskDevice() {
    return diskDevice;
  }

  
  public void setDiskDevice(String diskAlias) {
    this.diskDevice = diskAlias;
  }

  
  public String getEmailServer() {
    return emailServer;
  }

  
  public void setEmailServer(String emailServer) {
    this.emailServer = emailServer;
  }

  
  public int getEmailPort() {
    return emailPort;
  }

  
  public void setEmailPort(int emailPort) {
    this.emailPort = emailPort;
  }

  
  public String getEmailProtocol() {
    return emailProtocol;
  }

  
  public void setEmailProtocol(String emailProtocol) {
    this.emailProtocol = emailProtocol;
  }

  
  public String getEmailAccount() {
    return emailAccount;
  }

  
  public void setEmailAccount(String emailAccount) {
    this.emailAccount = emailAccount;
  }

  
  public String getEmailPasswd() {
    return "";
  }

  
  public void setEmailPasswd(String emailPasswd) {
    this.emailPasswd = emailPasswd;
  }


  public String getMachineName() {
    return machineName;
  }


  public void setMachineName(String machineName) {
    this.machineName = machineName;
  }


  public String getIfNetworkName() {
    return ifnetworkName;
  }


  public void setIfNetworkName(String ifnetworkName) {
    this.ifnetworkName = ifnetworkName;
  }


  public double getIfNetworkLimit() {
    return ifnetworkLimit;
  }


  public void setIfNetworkLimit(double ifnetworkLimit) {
    this.ifnetworkLimit = ifnetworkLimit;
  }
  
  
  public void save() {
    conf.setAdminEmail(adminEmail)
        .setCpuLimit(cpuLimit)
        .setDiskDevice(diskDevice)
        .setDiskLimit(diskLimit)
        .setEmailPasswd(emailPasswd)
        .setEmailSender(emailAccount)
        .setEmailProtocol(emailProtocol)
        .setEmailServerAddr(emailServer)
        .setEmailServerPort(emailPort)
        .setMachineName(machineName)
        .setMemoryLimit(memLimit)
        .setIFNetName(ifnetworkName)
        .setIFNetLimit(ifnetworkLimit)
        .setDecryptHash(security.puid.getHash())
        .save();
  }
  
  
  public void load() {
    conf.load();
    adminEmail = conf.getAdminEmail();
    cpuLimit = conf.getCpuLimit();
    diskDevice = conf.getDiskDevice();
    diskLimit = conf.getDiskLimit();
    emailPasswd = conf.getEmailPasswd();
    emailAccount = conf.getEmailSender();
    emailProtocol = conf.getEmailProtocol();
    emailServer = conf.getEmailServerAddr();
    emailPort = conf.getEmailServerPort();
    machineName = conf.getMachineName();
    memLimit = conf.getMemoryLimit();
    ifnetworkName = conf.getIFNetName();
    ifnetworkLimit = conf.getIFNetLimit();
  }
  
}
