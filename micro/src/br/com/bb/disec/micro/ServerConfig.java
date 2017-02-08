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

package br.com.bb.disec.micro;

import br.com.bb.disec.micro.util.JsonClass;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.undertow.server.HttpHandler;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import org.jboss.logging.Logger;

/**
 * Configurações do servidor de rede, encapsula 
 * informações como endereço e porta de escuta 
 * do serviço, threads e HttpHandler's para atendimento 
 * de requisições, entre outras. Pode ser criado a partir 
 * de um arquivo no formato json.
 * @author Juno Roesler - juno.roesler@bb.com.br
 * @version 1.0.201607
 */
public class ServerConfig {
  
  /**
   * Quantidade padrão de threads primárias para 
   * atender as requisições do servidor 
   * (DEFAULT_IO_THREADS = 4).
   */
  public static final int DEFAULT_IO_THREADS = 4;
  
  /**
   * Quantidade padrão de threads secundárias para 
   * executar trabalhos "blocantes"
   * (DEFAULT_MAX_WORKER_THREADS = 10).
   */
  public static final int DEFAULT_MAX_WORKER_THREADS = 10;
  
  
  private final String address;
  
  private final int port;
  
  private final boolean dispatcherEnabled;
  
  private final boolean shutdownHandler;
  
  private final boolean authenticationShield;
  
  private final int maxWorkerThreads;
  
  private final int ioThreads;
  
  private final Map<String,Class> handlers;
  
  
  /**
   * Construtor padrão, recebe todas as 
   * informações necessárias para o servidor.
   * @param address Endereço de escuta do serviço de rede.
   * @param port Porta de escuta do serviço de rede.
   * @param dispatcherEnabled Configura se os HttpHandler's 
   * serão criados sob demanda (true) ou apenas na 
   * inicialização do servidor (false).
   * @param shutdownHandler Configura se o servidor possuirá
   * uma URI configurada para parar o serviço de rede.
   * @param authShield Configura se o servidor possuiŕa
   * controle de acesso aos serviços disponíveis.
   * @param ioThreads Quantidade de threads primárias para
   * atendimento de requisições.
   * @param maxWorkerThreads Quantidade máxima de threads
   * secundárias para executar trabalhos "blocantes".
   * @param map Mapa com as classes dos HttpHandler's e 
   * respectivas URIs às quais estão associados.
   */
  public ServerConfig(
      String address, 
      int port, 
      boolean dispatcherEnabled, 
      boolean shutdownHandler, 
      boolean authShield,
      int ioThreads, 
      int maxWorkerThreads, 
      Map<String,Class> map
  ) {
    if(address == null || address.trim().isEmpty()) {
      throw new IllegalArgumentException("Invalid Address: "+ address);
    }
    if(port <= 0 || port > 65535) {
      throw new IllegalArgumentException("Invalid Port: "+ port);
    }
    this.address = address;
    this.port = port;
    this.dispatcherEnabled = dispatcherEnabled;
    this.shutdownHandler = shutdownHandler;
    this.authenticationShield = authShield;
    this.ioThreads = (ioThreads > 0 
        ? ioThreads 
        : DEFAULT_IO_THREADS
    );
    this.maxWorkerThreads = (maxWorkerThreads > 0 
        ? maxWorkerThreads 
        : DEFAULT_MAX_WORKER_THREADS
    );
    if(map != null) {
      this.handlers = Collections.unmodifiableMap(map);
    }
    else {
      this.handlers = Collections.EMPTY_MAP;
    }
  }
  
  
  /**
   * Retorna uma instância da classe contrutora de ServerConfig.
   * @return Nova instância da classe contrutora de ServerConfig.
   */
  public static Builder builder() {
    return new Builder();
  }


  /**
   * Retorna o endereço de rede no qual o 
   * servidor irá escutar.
   * @return String
   */
  public String getAddress() {
    return address;
  }


  /**
   * Retorna a porta de rede na qual o servidor
   * irá escutar.
   * @return int
   */
  public int getPort() {
    return port;
  }
  
  
  /**
   * Verifica se os HttpHandler's serão criados 
   * sob demanda (true) ou apenas na inicialização 
   * do servidor (false).
   * @return true se os HttpHandler's serão criados 
   * sob demanda, false se serão criados apenas na 
   * inicialização do servidor.
   */
  public boolean isDispatcherEnabled() {
    return dispatcherEnabled;
  }
  
  
  /**
   * Verifica se o servidor possuirá uma URI 
   * configurada para parar o serviço de rede.
   * @return true se o servidor possuirá uma URI 
   * configurada para parar o serviço de rede, 
   * false caso contrário.
   */
  public boolean isShutdownHandlerEnabled() {
    return shutdownHandler;
  }


  /**
   * Verifica se o servidor possuiŕá controle 
   * de acesso aos serviços disponíveis.
   * @return true se o servidor possuiŕá controle 
   * de acesso aos serviços disponíveis, false
   * caso contrário.
   */
  public boolean isAuthenticationShield() {
    return authenticationShield;
  }


  /**
   * Retorna a quantidade máxima de threads
   * secundárias para executação de trabalhos 
   * "blocantes".
   * @return int quantidade máxima de threads
   * secundárias para executação de trabalhos 
   * "blocantes".
   */
  public int getMaxWorkerThreads() {
    return maxWorkerThreads;
  }


  /**
   * Retorna a quantidade de threads primárias para
   * atendimento de requisições.
   * @return int a quantidade de threads primárias para
   * atendimento de requisições.
   */
  public int getIoThreads() {
    return ioThreads;
  }


  /**
   * Mapa com as classes dos HttpHandler's e 
   * respectivas URIs às quais estão associados.
   * @return Map&lt;String, Class&gt;
   */
  public Map<String, Class> handlers() {
    return handlers;
  }
  
  
  public Optional<HttpHandler> createHandler(String path) {
    try {
      Class<HttpHandler> cls = handlers.get(path);
      Constructor<HttpHandler> cct = cls.getDeclaredConstructor(null);
      if(!cct.isAccessible()) {
        cct.setAccessible(true);
      }
      return Optional.of(cct.newInstance(null));
    }
    catch(Exception ex) {
      Logger.getLogger(getClass()).error(
          ex.getClass().getSimpleName()+ ":"+ ex.getMessage()
      );
      return Optional.empty();
    }
  }


  @Override
  public int hashCode() {
    int hash = 7;
    hash = 97 * hash + Objects.hashCode(this.address);
    hash = 97 * hash + this.port;
    hash = 97 * hash + Objects.hashCode(this.handlers);
    return hash;
  }


  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final ServerConfig other = (ServerConfig) obj;
    if (this.port != other.port) {
      return false;
    }
    if (!Objects.equals(this.address, other.address)) {
      return false;
    }
    if (!Objects.equals(this.handlers, other.handlers)) {
      return false;
    }
    return true;
  }
  
  
  /**
   * Grava as informações desta instância de ServerConfig
   * no arquivo informado.
   * @param path Caminho do arquivo onde serão salvas 
   * as configurações.
   * @return Esta instância de ServerConfig.
   * @throws IOException Caso ocorra erro as escrever no
   * arquivo.
   */
  public ServerConfig save(Path path) throws IOException {
    new Builder()
        .setServerAddress(address)
        .setServerPort(port)
        .setHandlers(handlers)
        .save(path);
    return this;
  }


  @Override
  public String toString() {
    return "ServerConfig{\n    " 
        + "address: " + address 
        + "\n    port: " + port 
        + "\n    ioThreads: " + ioThreads 
        + "\n    maxWorkerThreads: " + maxWorkerThreads 
        + "\n    dispatcherEnabled: " + dispatcherEnabled 
        + "\n    shutdownHandlerEnabled: " + shutdownHandler
        + "\n    authenticationShield: " + authenticationShield
        + "\n    handlers: " + handlers.toString()
            .replace("{", "{\n      - ")
            .replace(", ", "\n      - ")
            .replace("}", "\n    }") + "\n}";
  }
  
  
  
  
  
  
  /**
   * Classe construtora de objetos ServerConfig.
   */
  public static class Builder {
    
    private String serverAddress;
    
    private int serverPort;
    
    private boolean dispatcherEnabled;
    
    private boolean authenticationShield;
    
    private int maxWorkerThreads;
    
    private int ioThreads;
    
    private boolean shutdownHandlerEnabled;
    
    private Map<String,Class> handlers;
    
    
    /**
     * Construtor padrão sem argumentos.
     */
    public Builder() {
      handlers = new HashMap<>();
    }
    
    
    /**
     * Adiciona uma URI e a respectiva classe
     * HttpHandler que atenderá requisições.
     * @param path Caminho URI sob o qual o 
     * handler irá responder.
     * @param cls Classe HttpHandler para 
     * atender requisições.
     * @return Esta instância modificada de Builder.
     */
    public Builder put(String path, Class cls) {
      if(path != null && cls != null) {
        handlers.put(path, cls);
      }
      return this;
    }


    /**
     * Retorna o endereço de rede no qual o 
     * servidor irá escutar.
     * @return String
     */
    public String getServerAddress() {
      return serverAddress;
    }


    /**
     * Define o endereço de rede no qual o 
     * servidor irá escutar.
     * @param serverAddress String Endereço de 
     * rede no qual o servidor irá escutar.
     * @return Esta instância modificada de Builder.
     */
    public Builder setServerAddress(String serverAddress) {
      this.serverAddress = serverAddress;
      return this;
    }


    /**
     * Retorna a porta de rede na qual o servidor
     * irá escutar.
     * @return int
     */
    public int getServerPort() {
      return serverPort;
    }


    /**
     * Define a porta de rede na qual o servidor
     * irá escutar.
     * @param serverPort int porta de rede na qual o servidor
     * irá escutar.
     * @return Esta instância modificada de Builder.
     */
    public Builder setServerPort(int serverPort) {
      this.serverPort = serverPort;
      return this;
    }


    /**
     * Verifica se os HttpHandler's serão criados 
     * sob demanda (true) ou apenas na inicialização 
     * do servidor (false).
     * @return true se os HttpHandler's serão criados 
     * sob demanda, false se serão criados apenas na 
     * inicialização do servidor.
     */
    public boolean isDispatcherEnabled() {
      return dispatcherEnabled;
    }


    /**
     * Define se os HttpHandler's serão criados 
     * sob demanda (true) ou apenas na inicialização 
     * do servidor (false).
     * @param useDispatcher true se os HttpHandler's serão criados 
     * sob demanda, false se serão criados apenas na 
     * inicialização do servidor.
     * @return Esta instância modificada de Builder.
     */
    public Builder setDispatcherEnabled(boolean useDispatcher) {
      this.dispatcherEnabled = useDispatcher;
      return this;
    }
    
    
    /**
     * Verifica se o servidor possuiŕá controle 
     * de acesso aos serviços disponíveis.
     * @return true se o servidor possuiŕá controle 
     * de acesso aos serviços disponíveis, false
     * caso contrário.
     */
    public boolean isAuthenticationShield() {
      return authenticationShield;
    }


    /**
     * Define se o servidor possuiŕá controle 
     * de acesso aos serviços disponíveis.
     * @param authenticationShield true se o 
     * servidor possuiŕá controle de acesso 
     * aos serviços disponíveis, false caso contrário.
     * @return Esta instância modificada de Builder.
     */
    public Builder setAuthenticationShield(boolean authenticationShield) {
      this.authenticationShield = authenticationShield;
      return this;
    }
    
    
    /**
     * Verifica se o servidor possuirá uma URI 
     * configurada para parar o serviço de rede.
     * @return true se o servidor possuirá uma URI 
     * configurada para parar o serviço de rede, 
     * false caso contrário.
     */
    public boolean isShutdownHandlerEnabled() {
      return shutdownHandlerEnabled;
    }


    /**
     * Define se o servidor possuirá uma URI 
     * configurada para parar o serviço de rede.
     * @param enableShutdownHandler true se o 
     * servidor possuirá uma URI configurada 
     * para parar o serviço de rede, false caso contrário.
     * @return Esta instância modificada de Builder.
     */
    public Builder setShutdownHandlerEnabled(boolean enableShutdownHandler) {
      this.shutdownHandlerEnabled = enableShutdownHandler;
      return this;
    }
    

    /**
     * Retorna o Mapa com as classes dos HttpHandler's e 
     * respectivas URIs às quais estão associados.
     * @return Map&lt;String, Class&gt;
     */
    public Map<String, Class> getHandlers() {
      return handlers;
    }


    /**
     * Define o Mapa com as classes dos HttpHandler's e 
     * respectivas URIs às quais estão associados.
     * @param handlers Map&lt;String, Class&gt;
     * @return Esta instância modificada de Builder.
     */
    public Builder setHandlers(Map<String, Class> handlers) {
      this.handlers = handlers;
      return this;
    }


    /**
     * Retorna a quantidade máxima de threads
     * secundárias para executação de trabalhos 
     * "blocantes".
     * @return int quantidade máxima de threads
     * secundárias para executação de trabalhos 
     * "blocantes".
     */
    public int getMaxWorkerThreads() {
      return maxWorkerThreads;
    }


    /**
     * Define a quantidade máxima de threads
     * secundárias para executação de trabalhos 
     * "blocantes".
     * @param maxWorkerThreads int quantidade máxima de threads
     * secundárias para executação de trabalhos 
     * "blocantes".
     * @return Esta instância modificada de Builder.
     */
    public Builder setMaxWorkerThreads(int maxWorkerThreads) {
      this.maxWorkerThreads = maxWorkerThreads;
      return this;
    }


    /**
     * Retorna a quantidade de threads primárias para
     * atendimento de requisições.
     * @return int a quantidade de threads primárias para
     * atendimento de requisições.
     */
    public int getIoThreads() {
      return ioThreads;
    }


    /**
     * Define a quantidade de threads primárias para
     * atendimento de requisições.
     * @param ioThreads int a quantidade de threads 
     * primárias para atendimento de requisições.
     * @return Esta instância modificada de Builder.
     */
    public Builder setIoThreads(int ioThreads) {
      this.ioThreads = ioThreads;
      return this;
    }
    
    
    /**
     * Cria uma nova instância de ServerConfig a partir
     * da configuração deste Builder.
     * @return Nova instância de ServerConfig.
     */
    public ServerConfig build() {
      return new ServerConfig(
          serverAddress, 
          serverPort, 
          dispatcherEnabled, 
          shutdownHandlerEnabled, 
          authenticationShield,
          ioThreads, 
          maxWorkerThreads, 
          handlers
      );
    }
    
    
    /**
     * Grava as informações desta instância de ServerConfig
     * no arquivo informado.
     * @param path Caminho do arquivo onde serão salvas 
     * as configurações.
     * @return Esta instância de ServerConfig.
     * @throws IOException Caso ocorra erro as escrever no
     * arquivo.
     */
    public Builder save(Path path) throws IOException {
      try (FileWriter fw = new FileWriter(path.toFile())) {
        Gson gson = new GsonBuilder()
            .registerTypeAdapter(Class.class, new JsonClass())
            .setPrettyPrinting().create();
        fw.write(gson.toJson(this));
      }
      return this;
    }
    
    
    /**
     * Carrega as configurações desta instância de Builder
     * a partir do arquivo informado.
     * @param path Caminho do arquivo de onde serão recuperadas 
     * as configurações.
     * @return Esta instância modificada de Builder.
     * @throws IOException Caso ocorra erro ao lêr do
     * arquivo.
     */
    public Builder load(Path path) throws IOException {
      try (FileReader fr = new FileReader(path.toFile())) {
        Gson gson = new GsonBuilder()
            .registerTypeAdapter(Class.class, new JsonClass())
            .create();
        Builder b = gson.fromJson(fr, Builder.class);
        return this.setAuthenticationShield(b.isAuthenticationShield())
            .setDispatcherEnabled(b.isDispatcherEnabled())
            .setHandlers(b.getHandlers())
            .setIoThreads(b.getIoThreads())
            .setMaxWorkerThreads(b.getMaxWorkerThreads())
            .setServerAddress(b.getServerAddress())
            .setServerPort(b.getServerPort())
            .setShutdownHandlerEnabled(b.isShutdownHandlerEnabled());
      }
    }


    /**
     * Carrega as configurações desta instância de Builder
     * a partir do InputStream informado.
     * @param input InputStream de onde serão recuperadas 
     * as configurações.
     * @return Esta instância modificada de Builder.
     * @throws IOException Caso ocorra erro ao lêr do
     * InputStream.
     */
    public Builder load(InputStream input) throws IOException {
      try (InputStreamReader ir = new InputStreamReader(input)) {
        Gson gson = new GsonBuilder()
            .registerTypeAdapter(Class.class, new JsonClass())
            .create();
        Builder b = gson.fromJson(ir, Builder.class);
        return this.setAuthenticationShield(b.isAuthenticationShield())
            .setDispatcherEnabled(b.isDispatcherEnabled())
            .setHandlers(b.getHandlers())
            .setIoThreads(b.getIoThreads())
            .setMaxWorkerThreads(b.getMaxWorkerThreads())
            .setServerAddress(b.getServerAddress())
            .setServerPort(b.getServerPort())
            .setShutdownHandlerEnabled(b.isShutdownHandlerEnabled());
      }
    }


    @Override
    public String toString() {
      return "ServerConfig.Builder{\n    " 
          + "address: " + serverAddress 
          + "\n    port: " + serverPort 
          + "\n    dispatcherEnabled: " + dispatcherEnabled 
          + "\n    ioThreads: " + ioThreads 
          + "\n    maxWorkerThreads: " + maxWorkerThreads 
          + "\n    shutdownHandlerEnabled: " + shutdownHandlerEnabled
          + "\n    authenticationShield: " + authenticationShield
          + "\n    handlers: " + handlers.toString()
              .replace("{", "{\n      - ")
              .replace(", ", "\n      - ")
              .replace("}", "\n    }") + "\n}";
    }
    
  }
  
}
