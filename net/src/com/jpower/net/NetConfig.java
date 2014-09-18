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

package com.jpower.net;

import com.jpower.conf.Config;
import com.jpower.log.LogFile;
import com.jpower.log.LogPrinter;
import com.jpower.log.Logger;

/**
 * Classe de configuração utilizada na criação
 * de objetos <code>NioClient</code> e <code>NioServer</code>.
 * Pode armazenar as configurações em arquivo.
 * @see com.jpower.net.NioClient
 * @see com.jpower.net.NioServer
 * 
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 28/05/2013
 */
public class NetConfig {

  public static final String
      
      DEFAULT_FILE = "./nionet.conf",
      
      KEY_PORT = "PORT",
      
      KEY_ADDRESS = "ADDRESS",
      
      KEY_BUFFER_SIZE = "BUFFER_SIZE",
      
      KEY_USE_FILE_LOG = "USE_FILE_LOG",
      
      KEY_USE_CONSOLE_LOG = "USE_CONSOLE_LOG",
      
      KEY_LOG_FILE = "LOG_FILE";
  
  public static final int 
      
      DEFAULT_PORT = 10001,
      
      DEFAULT_BUFFER_SIZE = 4096;
  
  
  private int port;
  
  private int bufsize;
  
  private String address;
  
  private Config config;
  
  private Logger log;
  
  private ReceiveFilter filter;
  
  private boolean saveOnFile;
  
  private boolean autoFilterActivated;
  
  private boolean useConsoleLog, useFileLog;
  
  private String logFile;
  
  
  /**
   * Construtor padrão e sem argumentos,
   * lê e carrega as informações automaticamente de um 
   * arquivo de configurações, caso exista no caminho 
   * relativo <code>./nionet.conf</code>.
   */
  public NetConfig() {
    log = new Logger();
    config = new Config(DEFAULT_FILE);
    saveOnFile = true;
    autoFilterActivated = false;
    filter = null;
    if(!config.getFile().exists())
      createDefault();
    else
      loadConfig();
  }
  
  
  private void createDefault() {
    port = DEFAULT_PORT;
    bufsize = DEFAULT_BUFFER_SIZE;
    address = "*";
    useFileLog = false;
    logFile = null;
    log = new Logger();
    this.setUsingConsoleLog(true);
    this.writeValues();
  }
  
  
  private void loadConfig() {
    config.load();
    port = config.getInt(KEY_PORT);
    if(port <= 0 || port >= 65536)
      port = DEFAULT_PORT;
    bufsize = config.getInt(KEY_BUFFER_SIZE);
    if(bufsize <= 10) 
      bufsize = DEFAULT_BUFFER_SIZE;
    address = config.get(KEY_ADDRESS);
    if(address == null || address.trim().isEmpty())
      address = "*";
    logFile = config.get(KEY_LOG_FILE);
    useFileLog = Boolean.parseBoolean(
        config.get(KEY_USE_FILE_LOG));
    useConsoleLog = Boolean.parseBoolean(
        config.get(KEY_USE_CONSOLE_LOG));
    if(useFileLog && (logFile == null 
        || logFile.trim().isEmpty()))
      logFile = "./nionet.log";
    this.writeValues();
  }
  
  
  /**
   * Escreve as configurações para um arquivo.
   * @return <code>true</code> se a escrita
   * for bem sucedida, <code>false</code> caso
   * ocorra erro.
   */
  public boolean writeValues() {
    config.put(KEY_PORT, port);
    config.put(KEY_ADDRESS, address);
    config.put(KEY_BUFFER_SIZE, bufsize);
    config.put(KEY_USE_CONSOLE_LOG, useConsoleLog);
    config.put(KEY_USE_FILE_LOG, useFileLog);
    config.put(KEY_LOG_FILE, logFile);
    if(saveOnFile)
      return config.save();
    else return false;
  }


  /**
   * Retorna o número da porta usada na conexão.
   * @return <code>int: 0 - 65535</code>.
   */
  public int getPort() {
    return port;
  }


  /**
   * Define o número da porta usada na conexão.
   * @param port <code>int: 0 - 65535</code>.
   * @return Esta instância modificada de <code>NetConfig</code>.
   */
  public NetConfig setPort(int port) {
    this.port = port;
    return this;
  }


  /**
   * Retorna o endereço de rede usado na conexão.
   * @return Endereço IP ou nome do host na rede.
   */
  public String getAddress() {
    return address;
  }


  /**
   * Define o endereço de rede usado na conexão.
   * @param address Endereço IP ou nome do host na rede.
   * @return Esta instância modificada de <code>NetConfig</code>.
   */
  public NetConfig setAddress(String address) {
    this.address = address;
    return this;
  }


  /**
   * Retorna o tamanho padrão de buffer utilizado na conexão
   * para tráfego de dados.
   * @return <code>int</code>.
   */
  public int getBufferSize() {
    return bufsize;
  }


  /**
   * Define o tamanho padrão de buffer utilizado na conexão
   * para tráfego de dados.
   * @param bufsize <code>int</code>.
   * @return Esta instância modificada de <code>NetConfig</code>.
   */
  public NetConfig setBufferSize(int bufsize) {
    this.bufsize = bufsize;
    return this;
  }


  /**
   * Retorna a classe interna <code>Config</code>
   * utilizada para armazenar as configurações.
   * @see com.jpower.conf.Config
   * @return <code>Config</code>.
   */
  public Config getConfig() {
    return config;
  }


  /**
   * Define a classe interna <code>Config</code>
   * utilizada para armazenar as configurações.
   * @see com.jpower.conf.Config
   * @param config <code>Config</code>.
   * @return Esta instância modificada de <code>NetConfig</code>.
   */
  public NetConfig setConfig(Config config) {
    this.config = config;
    return this;
  }


  /**
   * Retorna o Logger utilizado para escrita
   * de informações da conexão.
   * @return <code>Logger</code>.
   * @see com.jpower.log.Logger
   */
  public Logger getLogger() {
    return log;
  }
  
  
  /**
   * Define o Logger utilizado para escrita
   * de informações da conexão.
   * @param log <code>Logger</code>.
   * @see com.jpower.log.Logger
   * @return Esta instância modificada de <code>NetConfig</code>.
   */
  public NetConfig setLogger(Logger log) {
    this.log = log;
    if(log == null)
      log = new Logger();
    return this;
  }
  
  
  /**
   * Verifica se o uso de saída de log em arquivo está habilitada.
   * @return <code>true</code> se o uso de saída de log em arquivo
   * estiver habilitado, <code>false</code> caso contrário.
   */
  public boolean isUsingFileLog() {
    return useFileLog;
  }
  
  
  /**
   * Habilita/Desabilita o uso de saída de log no arquivo especificado.
   * @param b <code>true</code> se o uso de saída de log em arquivo
   * estiver habilitado, <code>false</code> caso contrário.
   * @param filename Nome e caminho do arquivo de log.
   */
  public NetConfig setUsingFileLog(boolean b, String filename) {
    this.useFileLog = b;
    if(useFileLog && filename != null 
        && !filename.trim().isEmpty()
        && log != null) {
      logFile = filename;
      log.add(new LogFile(logFile));
    }
    return this;
  }
  
  
  /**
   * Verifica se o uso de saída de log no console está habilitada.
   * @return <code>true</code> se o uso de saída de log no console
   * estiver habilitado, <code>false</code> caso contrário.
   */
  public boolean isUsingConsoleLog() {
    return useFileLog;
  }
  
  
  /**
   * Habilita/Desabilita o uso de saída de log no console.
   * @param b <code>true</code> se o uso de saída de log no console
   * estiver habilitado, <code>false</code> caso contrário.
   * @param filename Nome e caminho do arquivo de log.
   */
  public NetConfig setUsingConsoleLog(boolean b) {
    this.useConsoleLog = b;
    if(useFileLog && log != null) {
      log.add(new LogPrinter());
    }
    return this;
  }
  
  
  /**
   * Define se as configurações devem ser armazenadas em
   * arquivo de texto.
   * @param saveOnFile <code>true</code> para que as configurações
   * sejam armazenadas em arquivo de texto, <code>false</code>
   * caso contrário.
   * @return Esta instância modificada de <code>NetConfig</code>.
   */
  public NetConfig setConfigureFileSaving(boolean saveOnFile) {
    this.saveOnFile = saveOnFile;
    return this;
  }
  
  
  /**
   * Verifica se as configurações devem ser armazenadas em
   * arquivo de texto.
   * @return <code>true</code> para que as configurações
   * sejam armazenadas em arquivo de texto, <code>false</code>
   * caso contrário.
   */
  public boolean isFileSavingConfigured() {
    return this.saveOnFile;
  }


  /**
   * Retorna o filtro de recebimento de conteúdo
   * utilizado na conexão.
   * @return <code>ReceiveFilter</code>.
   * @see com.jpower.net.ReceiveFilter
   */
  public ReceiveFilter getReceiveFilter() {
    return filter;
  }


  /**
   * Define o filtro de recebimento de conteúdo
   * utilizado na conexão.
   * @param filter <code>ReceiveFilter</code>.
   * @return Esta instância modificada de <code>NetConfig</code>.
   * @see com.jpower.net.ReceiveFilter
   */
  public NetConfig setReceiveFilter(ReceiveFilter filter) {
    this.filter = filter;
    return this;
  }


  /**
   * Verifica se o filtro automático de frames
   * para transferência de conteúdo pela conexão 
   * está ativo ou não.
   * @return <code>true</code> se o filtro automático de frames
   * para transferência de conteúdo pela conexão 
   * está ativo, <code>false</code> caso contrário.
   */
  public boolean isAutoFilterActivated() {
    return autoFilterActivated;
  }


  /**
   * Define se o filtro automático de frames
   * para transferência de conteúdo pela conexão 
   * está ativo ou não.
   * @autoFilterActivated <code>true</code> se o filtro 
   * automático de frames para transferência de conteúdo 
   * pela conexão está ativo, <code>false</code> caso contrário.
   * @return Esta instância modificada de <code>NetConfig</code>.
   */
  public NetConfig setAutoFilterActivated(boolean autoFilterActivated) {
    this.autoFilterActivated = autoFilterActivated;
    return this;
  }
  
}
