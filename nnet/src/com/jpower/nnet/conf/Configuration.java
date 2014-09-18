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
package com.jpower.nnet.conf;

import com.jpower.nnet.FrameControl;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;


/**
 * Configurações de conexão, encapsula através de um arquivo 
 * de configuração, dados modificáveis como porta, endereço,
 * endereço de proxy e dados de autenticação.
 * 
 * 
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 2012-07-23
 */
public class Configuration {
  
  public static final String 
      KEY_REMOTE_ADDRESS = "remote_address",
      KEY_LOCAL_ADDRESS = "local_address",
      KEY_REMOTE_PORT = "remote_port",
      KEY_LOCAL_PORT = "local_port",
      KEY_PROXY_ADDRESS = "proxy_address",
      KEY_PROXY_PORT = "proxy_port",
      KEY_PROXY_USER = "proxy_user",
      KEY_PROXY_PASS = "proxy_pass",
      KEY_FRAME_CONTROL_SEND = "frame_control_send",
      KEY_FRAME_CONTROL_RECEIVE = "frame_control_receive",
      
      CONF_COMMENTS = "com.jpower.nnet.Configuration";
  
  public static final File DEFAULT_CONFIG_FILE = 
      new File("./nnet.conf");
  
  
  private Properties props = new Properties();
  
  private int lport, rport;
  
  private String raddress, laddress;
  
  private File configFile;
  
  private String proxyUser, proxyPass, proxyAddress;
  
  private int proxyPort;
  
  private FrameControl fcontrol;
  
  
  /**
   * Construtor padrão e sem argumentos.
   */
  public Configuration() {
    props = new Properties();
    this.initDefaults();
  }
  
  
  /**
   * Cria e retorna um objeto <code>Configuration</code>
   * com as mesmas propriedades da instância atual.
   * @return novo objeto <code>Configuration</code>.
   */
  @Override
  public Configuration clone() {
    this.putValues();
    Configuration c = new Configuration();
    c.props = props;
    c.getValues();
    c.props = new Properties();
    c.putValues();
    return c;
  }
  
  
  /**
   * inicia os valore padrões de configuração.
   * @return 
   */
  public Configuration initDefaults() {
    lport = 0;
    rport = 0;
    laddress = "";
    configFile = DEFAULT_CONFIG_FILE;
    raddress = "";
    proxyAddress = "";
    proxyPort = 0;
    proxyUser = "";
    proxyPass = "";
    fcontrol = new FrameControl();
    return this;
  }
  
  
  /**
   * insere os dados de configuração no objeto <code>Properties</code>.
   */
  private void putValues() {
    props.setProperty(KEY_LOCAL_ADDRESS, laddress);
    props.setProperty(KEY_REMOTE_ADDRESS, raddress);
    props.setProperty(KEY_LOCAL_PORT, String.valueOf(lport));
    props.setProperty(KEY_REMOTE_PORT, String.valueOf(rport));
    props.setProperty(KEY_PROXY_PORT, String.valueOf(proxyPort));
    props.setProperty(KEY_PROXY_ADDRESS, proxyAddress);
    props.setProperty(KEY_PROXY_USER, proxyUser);
    props.setProperty(KEY_PROXY_PASS, proxyPass);
    props.setProperty(KEY_FRAME_CONTROL_SEND, 
        String.valueOf(fcontrol.isEnabledOnSend()));
    props.setProperty(KEY_FRAME_CONTROL_RECEIVE, 
        String.valueOf(fcontrol.isEnabledOnReceive()));
  }
  
  
  /**
   * Carrega do dados de configuração do objeto <code>Properties</code>.
   */
  private void getValues() {
    laddress = props.getProperty(KEY_LOCAL_ADDRESS);
    raddress = props.getProperty(KEY_REMOTE_ADDRESS);
    proxyAddress = props.getProperty(KEY_PROXY_ADDRESS);
    proxyUser = props.getProperty(KEY_PROXY_USER);
    proxyPass = props.getProperty(KEY_PROXY_PASS);
    try {
      lport = Integer.parseInt(props.getProperty(KEY_LOCAL_PORT));
      rport = Integer.parseInt(props.getProperty(KEY_REMOTE_PORT));
      proxyPort = Integer.parseInt(props.getProperty(KEY_PROXY_PORT));
      boolean b = Boolean.parseBoolean(props.getProperty(KEY_FRAME_CONTROL_SEND));
      fcontrol.setEnabledOnSend(b);
      b = Boolean.parseBoolean(props.getProperty(KEY_FRAME_CONTROL_RECEIVE));
      fcontrol.setEnabledOnReceive(b);
    } catch(Exception ex) {
      lport = 0;
      rport = 0;
      proxyPort = 0;
      fcontrol.setEnabled(true);
    }
  }
  
  
  /**
   * Salva o conteúdo no arquivo de configuração.
   * @return <code>true</code> se o arquivo for salvo
   * com sucesso, <code>false</code> caso contrário.
   */
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
  
  
  /**
   * Carrega o conteúdo do arquivo de configuração.
   * @return <code>true</code> se o arquivo for carregado
   * com sucesso, <code>false</code> caso contrário.
   */
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
  
  
  /**
   * Verifica se o arquivo de configuração existe,
   * neste caso carregando o conteúdo automaticamente,
   * ou cria um arquivo de configuração, salvando o conteúdo.
   */
  public void checkConfigFile() {
    if(configFile.exists())
      this.load();
    else 
      this.save();
  }
  
  
  /**
   * Retorna o arquivo de configuração.
   * @return <code>File</code>
   */
  public File getConfigFile() {
    return configFile;
  }
  
  
  /**
   * Define o arquivo de configuração.
   * @param f <code>File</code>
   * @return Esta instância modificada de <code>Configuration</code>
   */
  public Configuration setConfigFile(File f) {
    if(f != null) configFile = f;
    return this;
  }


  /**
   * Retorna o endereço do proxy.
   * @return Endereço ip ou nome do host.
   */
  public String getProxyAddress() {
    return proxyAddress;
  }


  /**
   * Define o endereço do proxy.
   * @param proxyAddress Endereço ip ou nome do host.
   * @return 
   */
  public Configuration setProxyAddress(String proxyAddress) {
    this.proxyAddress = proxyAddress;
    return this;
  }


  /**
   * Retorna a senha de autenticação do proxy.
   * @return <code>String</code>
   */
  public String getProxyPass() {
    return proxyPass;
  }


  /**
   * Define a senha de autenticação do proxy.
   * @param proxyPass <code>String</code>.
   * @return Esta instância modificada de <code>Configuration</code>.
   */
  public Configuration setProxyPass(String proxyPass) {
    this.proxyPass = proxyPass;
    return this;
  }


  /**
   * Define a porta de conexão do servidor proxy.
   * @return <code>int</code>.
   */
  public int getProxyPort() {
    return proxyPort;
  }


  /**
   * Define a porta de conexão do servidor proxy.
   * @param proxyPort <code>int</code>.
   * @return Esta instância modificada de <code>Configuration</code>.
   */
  public Configuration setProxyPort(int proxyPort) {
    this.proxyPort = proxyPort;
    return this;
  }


  /**
   * Retorna o usuário de autenticação do proxy.
   * @return usuário de autenticação do proxy.
   */
  public String getProxyUser() {
    return proxyUser;
  }


  /**
   * Define o usuário de autenticação do proxy.
   * @param proxyUser usuário de autenticação do proxy.
   * @return Esta instância modificada de <code>Configuration</code>.
   */
  public Configuration setProxyUser(String proxyUser) {
    this.proxyUser = proxyUser;
    return this;
  }


  /**
   * Retorna o endereço local.
   * @return Endereço local.
   */
  public String getLocalAddress() {
    return laddress;
  }


  /**
   * Define o endereço local.
   * @param laddress Endereço local.
   * @return Esta instância modificada de <code>Configuration</code>.
   */
  public Configuration setLocalAddress(String laddress) {
    this.laddress = laddress;
    return this;
  }


  /**
   * Define a porta de conexão.
   * @return porta de conexão.
   */
  public int getLocalPort() {
    return lport;
  }


  /**
   * Define a porta de conexão.
   * @param lport porta de conexão.
   * @return Esta instância modificada de <code>Configuration.</code>
   */
  public Configuration setLocalPort(int lport) {
    this.lport = lport;
    return this;
  }


  /**
   * Define a porta de conexão.
   * @return porta de conexão.
   */
  public int getRemotePort() {
    return rport;
  }


  /**
   * Define a porta de conexão.
   * @param rport porta de conexão.
   * @return Esta instância modificada de <code>Configuration.</code>
   */
  public Configuration setRemotePort(int rport) {
    this.rport = rport;
    return this;
  }


  /**
   * Retorna o endereço remoto.
   * @return endereço remoto.
   */
  public String getRemoteAddress() {
    return raddress;
  }


  /**
   * Define o endereço remoto.
   * @param raddress endereço remoto.
   * @return Esta instância modificada de <code>Configuration</code>
   */
  public Configuration setRemoteAddress(String raddress) {
    this.raddress = raddress;
    return this;
  }
  
  
  /**
   * Retorna o <code>FrameControl</code>
   * @see com.jpower.nnet.FrameControl
   */
  public FrameControl getFrameControl() {
    return fcontrol;
  }
  
  
  /**
   * Define o <code>FrameControl</code>.
   * @return Esta instância modificada de <code>Configuration</code>.
   * @see com.jpower.nnet.FrameControl
   */
  public Configuration setFcontrol(FrameControl fcontrol) {
    if(fcontrol != null)
      this.fcontrol = fcontrol;
    return this;
  }
  
}
