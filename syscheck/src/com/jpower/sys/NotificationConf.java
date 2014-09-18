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


package com.jpower.sys;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 *
 * @version 0.0 - 18/01/2013
 * @author Juno Roesler - juno.rr@gmail.com
 */
public class NotificationConf {

  public static final File CONF_FILE = 
      new File("./notification.conf");
  
  public static final String
      
      KEY_DECRYPT_HASH = "decrypt_hash",
      
      KEY_CPU_LIMIT = "cpu.limit",
      
      KEY_MEM_LIMIT = "memory.limit",
      
      KEY_DISK_DEV = "disk.device",
      
      KEY_DISK_LIMIT = "disk.limit",
      
      KEY_IFNET_NAME = "ifnet.name",
      
      KEY_IFNET_LIMIT = "ifnet.limit",
      
      KEY_MACHINE_NAME = "machine.name",
      
      KEY_ADMIN_EMAIL = "admin.email",
      
      KEY_EMAIL_SENDER = "email.sender",
      
      KEY_EMAIL_PASSWD = "email.passwd",
      
      KEY_EMAIL_SERVER_ADDR = "email.server_addr",
      
      KEY_EMAIL_SERVER_PORT = "email.server_port",
      
      KEY_EMAIL_PROTOCOL = "email.protocol";
  
  
  private Config conf;
  
  private File file;
  
  private double cpulimit;
  
  private double memlimit;
  
  private double disklimit;
  
  private String diskdev;
  
  private String ifname;
  
  private double iflimit;
  
  private String machineName;
  
  private String adminEmail;
  
  private String emailSender;
  
  private String emailPasswd;
  
  private String emailServerAddr;
  
  private int emailServerPort;
  
  private String emailProtocol;
  
  private String decryptHash;
  
  
  public NotificationConf() {
    initDefaults();
    file = CONF_FILE;
    conf = new Config(file);
    
    if(!file.exists()) {
      try {
        file.createNewFile();
        file.setWritable(true, false);
      } catch(IOException ex) {}
      this.save();
    } else {
      this.load();
    }
  }
  
  
  public NotificationConf(File f) {
    if(f == null)
      throw new IllegalArgumentException(
          "Invalid config file: "+ f);
    
    initDefaults();
    file = f;
    conf = new Config(file);
    
    if(!file.exists()) {
      try {
        file.createNewFile();
        file.setWritable(true, false);
      } catch(IOException ex) {}
      this.save();
    } else {
      this.load();
    }
  } 
  
  
  private void initDefaults() {
    decryptHash= "";
    cpulimit = 0;
    memlimit = 0;
    diskdev = "";
    disklimit = 0;
    ifname = "";
    iflimit = 0;
    machineName = "localhost";
    adminEmail = "";
    emailSender = "";
    emailPasswd = "";
    emailServerAddr = "smtp.gmail.com";
    emailServerPort = 465;
    emailProtocol = "smtps";
  }
  
  
  public NotificationConf load() {
    conf.load();
    decryptHash = conf.get(KEY_DECRYPT_HASH);
    cpulimit = conf.getDouble(KEY_CPU_LIMIT);
    memlimit = conf.getDouble(KEY_MEM_LIMIT);
    disklimit = conf.getDouble(KEY_DISK_LIMIT);
    diskdev = conf.get(KEY_DISK_DEV);
    ifname = conf.get(KEY_IFNET_NAME);
    iflimit = conf.getDouble(KEY_IFNET_LIMIT);
    machineName = conf.get(KEY_MACHINE_NAME);
    adminEmail = conf.get(KEY_ADMIN_EMAIL);
    emailSender = conf.get(KEY_EMAIL_SENDER);
    emailPasswd = conf.get(KEY_EMAIL_PASSWD);
    emailServerAddr = conf.get(KEY_EMAIL_SERVER_ADDR);
    emailServerPort = conf.getInt(KEY_EMAIL_SERVER_PORT);
    emailProtocol = conf.get(KEY_EMAIL_PROTOCOL);
    return this;
  }
  
  
  public NotificationConf save() {
    conf.put(KEY_CPU_LIMIT, cpulimit)
        .put(KEY_MEM_LIMIT, memlimit)
        .put(KEY_DISK_DEV, diskdev)
        .put(KEY_DISK_LIMIT, disklimit)
        .put(KEY_IFNET_NAME, ifname)
        .put(KEY_IFNET_LIMIT, iflimit)
        .put(KEY_MACHINE_NAME, machineName)
        .put(KEY_ADMIN_EMAIL, adminEmail)
        .put(KEY_EMAIL_SENDER, emailSender)
        .put(KEY_EMAIL_PASSWD, emailPasswd)
        .put(KEY_EMAIL_SERVER_ADDR, emailServerAddr)
        .put(KEY_EMAIL_SERVER_PORT, String.valueOf(emailServerPort))
        .put(KEY_EMAIL_PROTOCOL, emailProtocol)
        .put(KEY_DECRYPT_HASH, decryptHash)
        .save();
    return this;
  }
  
  
  public File getConfigFile() {
    return file;
  }
  
  
  public NotificationConf setConfigFile(File f) {
    if(f == null || !f.exists())
      throw new IllegalArgumentException(
          "Invalid file: "+ f);
    
    this.file = f;
    conf.setFile(file);
    conf.load();
    return this;
  }


  public String getDecryptHash() {
    return decryptHash;
  }


  public NotificationConf setDecryptHash(String decryptHash) {
    this.decryptHash = decryptHash;
    return this;
  }
  
  
  public double getCpuLimit() {
    return cpulimit;
  }


  public NotificationConf setCpuLimit(double cpulimit) {
    this.cpulimit = cpulimit;
    return this;
  }


  public double getMemoryLimit() {
    return memlimit;
  }


  public NotificationConf setMemoryLimit(double memlimit) {
    this.memlimit = memlimit;
    return this;
  }


  public String getDiskDevice() {
    return diskdev;
  }


  public NotificationConf setDiskDevice(String diskdev) {
    this.diskdev = diskdev;
    return this;
  }
  
  
  public double getDiskLimit() {
    return disklimit;
  }
  
  
  public NotificationConf setDiskLimit(double disklimit) {
    this.disklimit = disklimit;
    return this;
  }


  public String getIFNetName() {
    return ifname;
  }


  public NotificationConf setIFNetName(String ifname) {
    this.ifname = ifname;
    return this;
  }
  
  
  public double getIFNetLimit() {
    return iflimit;
  }


  public NotificationConf setIFNetLimit(double iflimit) {
    this.iflimit = iflimit;
    return this;
  }


  public String getMachineName() {
    return machineName;
  }


  public NotificationConf setMachineName(String machineName) {
    this.machineName = machineName;
    return this;
  }
  
  
  public String getAdminEmail() {
    return adminEmail;
  }


  public NotificationConf setAdminEmail(String adminEmail) {
    this.adminEmail = adminEmail;
    return this;
  }


  public String getEmailSender() {
    return emailSender;
  }


  public NotificationConf setEmailSender(String emailSender) {
    this.emailSender = emailSender;
    return this;
  }


  public String getEmailPasswd() {
    return emailPasswd;
  }


  public NotificationConf setEmailPasswd(String emailPasswd) {
    this.emailPasswd = emailPasswd;
    return this;
  }


  public String getEmailServerAddr() {
    return emailServerAddr;
  }


  public NotificationConf setEmailServerAddr(String emailServerAddr) {
    this.emailServerAddr = emailServerAddr;
    return this;
  }


  public int getEmailServerPort() {
    return emailServerPort;
  }


  public NotificationConf setEmailServerPort(int emailServerPort) {
    this.emailServerPort = emailServerPort;
    return this;
  }


  public String getEmailProtocol() {
    return emailProtocol;
  }


  public NotificationConf setEmailProtocol(String emailProtocol) {
    this.emailProtocol = emailProtocol;
    return this;
  }
  
  
  public Disk getDisk(List<Disk> disks) {
    if(disks == null || disks.isEmpty() 
        || diskdev == null) return null;
    
    for(Disk d : disks) {
      if(d.getDevice() != null
          && d.getDevice().equals(diskdev))
        return d;
    }
    return null;
  }
  
  
  public IFNetwork getNet(List<IFNetwork> ifs) {
    if(ifname == null || ifs == null
        || ifs.isEmpty()) return null;
    
    for(IFNetwork n : ifs) {
      if(n.getName().equals(ifname))
        return n;
    }
    return null;
  }
  
  
  public boolean match(Snapshot snap) {
    if(cpulimit != 0 && snap.getCpu()
        .getUsedCpu() >= cpulimit)
      return true;
    
    if(memlimit != 0 && snap.getMem()
        .usedPerc() >= memlimit)
      return true;
    
    Disk d = this.getDisk(snap.getDisks());
    if(d != null && d.getUsedPerc() >= disklimit)
      return true;
    
    IFNetwork n = this.getNet(snap.getInterfaces());
    if(n != null && n.getLoad()
        .getTotalAverage() >= iflimit)
      return true;
    
    return false;
  }
  
}
