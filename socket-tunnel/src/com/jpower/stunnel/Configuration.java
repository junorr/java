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
package com.jpower.stunnel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;


/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 17/07/2012
 */
public class Configuration {
  
  public static final int DEFAULT_PORT = 29209;
  
  public static final String 
      KEY_REMOTE_ADDRESS = "remote_address",
      KEY_LOCAL_ADDRESS = "local_address",
      KEY_PORT = "port",
      KEY_PROXY_ADDRESS = "proxy_address",
      KEY_PROXY_PORT = "proxy_port",
      KEY_PROXY_USER = "proxy_user",
      KEY_PROXY_PASS = "proxy_pass",
      
      CONF_COMMENTS = "socket.tunnel.configuration";
  
  public static final File DEFAULT_CONFIG_FILE = 
      new File("./socket_tunnel.properties");
  
  
  private Properties props = new Properties();
  
  private int port;
  
  private String raddress, laddress;
  
  private File configFile;
  
  private String proxyUser, proxyPass, proxyAddress;
  
  private int proxyPort;
  
  
  public Configuration() {
    props = new Properties();
    this.initDefaults();
  }
  
  
  public Configuration initDefaults() {
    port = DEFAULT_PORT;
    laddress = "";
    configFile = DEFAULT_CONFIG_FILE;
    raddress = "";
    proxyAddress = "";
    proxyPort = 0;
    proxyUser = "";
    proxyPass = "";
    return this;
  }
  
  
  private void putValues() {
    props.setProperty(KEY_LOCAL_ADDRESS, laddress);
    props.setProperty(KEY_REMOTE_ADDRESS, raddress);
    props.setProperty(KEY_PORT, String.valueOf(port));
    props.setProperty(KEY_PROXY_PORT, String.valueOf(proxyPort));
    props.setProperty(KEY_PROXY_ADDRESS, proxyAddress);
    props.setProperty(KEY_PROXY_USER, proxyUser);
    props.setProperty(KEY_PROXY_PASS, proxyPass);
  }
  
  
  private void getValues() {
    laddress = props.getProperty(KEY_LOCAL_ADDRESS);
    raddress = props.getProperty(KEY_REMOTE_ADDRESS);
    proxyAddress = props.getProperty(KEY_PROXY_ADDRESS);
    proxyUser = props.getProperty(KEY_PROXY_USER);
    proxyPass = props.getProperty(KEY_PROXY_PASS);
    try {
      port = Integer.parseInt(props.getProperty(KEY_PORT));
      proxyPort = Integer.parseInt(props.getProperty(KEY_PROXY_PORT));
    } catch(Exception ex) {
      port = DEFAULT_PORT;
      proxyPort = 0;
    }
  }
  
  
  public boolean save() {
    try {
      
      FileOutputStream fos = 
          new FileOutputStream(configFile);
      
      this.putValues();
      props.store(fos, CONF_COMMENTS);
      fos.close();
      
      return true;
      
    } catch(Exception ex) {
      ex.printStackTrace();
      return false;
    }
  }
  
  
  public boolean load() {
    try {
      
      FileInputStream fis = 
          new FileInputStream(configFile);
      
      props.load(fis);
      this.getValues();
      fis.close();
      
      return true;
      
    } catch(Exception ex) {
      ex.printStackTrace();
      return false;
    }
  }
  
  
  public void checkConfigFile() {
    if(configFile.exists())
      this.load();
    else 
      this.save();
  }
  
  
  public File getConfigFile() {
    return configFile;
  }
  
  
  public Configuration setConfigFile(File f) {
    if(f != null) configFile = f;
    return this;
  }


  public String getProxyAddress() {
    return proxyAddress;
  }


  public void setProxyAddress(String proxyAddress) {
    this.proxyAddress = proxyAddress;
  }


  public String getProxyPass() {
    return proxyPass;
  }


  public void setProxyPass(String proxyPass) {
    this.proxyPass = proxyPass;
  }


  public int getProxyPort() {
    return proxyPort;
  }


  public void setProxyPort(int proxyPort) {
    this.proxyPort = proxyPort;
  }


  public String getProxyUser() {
    return proxyUser;
  }


  public void setProxyUser(String proxyUser) {
    this.proxyUser = proxyUser;
  }


  public String getLocalAddress() {
    return laddress;
  }


  public Configuration setLocalAddress(String laddress) {
    this.laddress = laddress;
    return this;
  }


  public int getPort() {
    return port;
  }


  public Configuration setPort(int lport) {
    this.port = lport;
    return this;
  }


  public String getRemoteAddress() {
    return raddress;
  }


  public Configuration setRemoteAddress(String raddress) {
    this.raddress = raddress;
    return this;
  }
  
}
