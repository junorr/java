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

package us.pserver.remote;

import com.jpower.rfl.Reflector;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import us.pserver.log.LogProvider;

/**
 * Servidor de rede para objetos remotos, cujos métodos 
 * serão invocados remotamente.
 * 
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 11/11/2013
 */
public class NetworkServer extends AbstractServer {
  
  public static final String SERVER_KEY = "NetworkServer";
  
  
  private transient Reflector ref;
  
  private transient NetConnector con;
  
  private transient SocketChannelFactory factory;
  
  private ExecutorService exec;
  
  
  /**
   * Construtor que recebe como argumento o 
   * container de objetos <code>ObjectContainer</code>.
   * e informações de conexão de rede <code>NetConnector</code>.
   * Cria um servidor com conexão de rede padrão com canal de 
   * comunicação do tipo <code>NetworkChannel</code>.
   * @param cont container de objetos <code>ObjectContainer</code>.
   * @see us.pserver.remote.ObjectContainer
   * @see us.pserver.remote.NetConnector
   */
  public NetworkServer(ObjectContainer cont) {
    super(cont);
    cont.put(SERVER_KEY, this);
    ref = new Reflector();
    con = new NetConnector();
    factory = DefaultFactoryProvider
        .getSocketXmlChannelFactory();
  }
  
  
  /**
   * Construtor que recebe como argumentos o 
   * container de objetos <code>ObjectContainer</code> 
   * e a conexão de rede <code>NetConnector</code>.
   * Cria um servidor com o canal de 
   * comunicação do tipo <code>NetworkChannel</code>.
   * @param cont container de objetos <code>ObjectContainer</code>.
   * @param netCon Conexão de rede <code>NetConnector</code>.
   * @see us.pserver.remote.ObjectContainer
   * @see us.pserver.remote.NetConnector
   */
  public NetworkServer(ObjectContainer cont, NetConnector netCon) {
    this(cont);
    cont.put(SERVER_KEY, this);
    if(con == null) throw new
        IllegalArgumentException(
        "Invalid NetConnector: "+ con);
    this.con = netCon;
  }
  
  
  /**
   * Construtor que recebe como argumentos o 
   * container de objetos <code>ObjectContainer</code>,
   * as informações de conexão de rede <code>NetConnector</code> 
   * e a fábrica do canal de comunicação de objetos na rede.
   * @param cont container de objetos <code>ObjectContainer</code>.
   * @param netCon Conexão de rede <code>NetConnector</code>.
   * @param fact Fábrica do canal de comunicação de objetos na
   * rede <code>ChannelFactory&lt;Socket&gt;</code>
   * @see us.pserver.remote.ObjectContainer
   * @see us.pserver.remote.NetConnector
   */
  public NetworkServer(ObjectContainer cont, NetConnector netCon, SocketChannelFactory fact) {
    this(cont, netCon);
    cont.put(SERVER_KEY, this);
    if(fact == null)
      throw new IllegalArgumentException(
          "Invalid ChannelFactory ["+ fact+ "]");
    this.factory = fact;
  }
  
  
  /**
   * Retorna as informações de rede encapsuladas
   * por <code>NetConnector</code>.
   * @return <code>NetConnector</code>.
   */
  public NetConnector getNetConnector() {
    return con;
  }


  /**
   * Define as informações de rede encapsuladas
   * por <code>NetConnector</code>.
   * @param con <code>NetConnector</code>.
   * @return Esta instância modificada de ObjectServer.
   */
  public NetworkServer setNetConnector(NetConnector con) {
    this.con = con;
    return this;
  }


  /**
   * Retorna a fábrica do canal de comunicação de objetos na rede.
   * @return Fábrica do canal de comunicação de objetos na rede.
   */
  public SocketChannelFactory getChannelFactory() {
    return factory;
  }


  /**
   * Define a fábrica do canal de comunicação de objetos na rede.
   * @param fact fábrica do canal de comunicação de objetos na rede.
   * @return Esta instância modificada de ObjectServer.
   */
  public NetworkServer setChannelFactory(SocketChannelFactory fact) {
    if(fact != null)
      this.factory = fact;
    return this;
  }
  
  
  /**
   * Valida pré-requisitos para execução do servidor.
   */
  private void preStart() {
    if(con == null)
      throw new IllegalStateException(
          "Invalid NetConnector ["+ con + "]");
    if(factory == null)
      throw new IllegalArgumentException(
          "Invalid ChannelFactory ["+ factory+ "]");
    if(container == null)
      throw new IllegalArgumentException(
          "Invalid ObjectContainer ["+ container+ "]");
    
    LogProvider.getSimpleLog().info("Starting NetworkServer...");
    running = true;
    exec = Executors.newFixedThreadPool(availableThreads);
  }
  
  
  /**
   * Inicia o servidor de objetos.
   */
  @Override
  public void start() {
    preStart();
    run();
  }
  
  
  /**
   * Inicia o servidor de objetos em uma nova <code>Thread</code>.
   * @return Esta instância modificada de ObjectServer.
   */
  public NetworkServer startNewThread() {
    preStart();
    new Thread(this, "NetworkServer").start();
    return this;
  }
  
  
  /**
   * Não invocar diretamente. 
   * Executa as funções de ObjectServer.
   */
  @Override
  public void run() {
    try(ServerSocket server = con.connectServerSocket();) {
      LogProvider.getSimpleLog().info("Channel: "
          + factory.createChannel(new Socket()).getClass().getCanonicalName());
      LogProvider.getSimpleLog().info("Listening on: "+ con.toString());
      LogProvider.getSimpleLog().info("Server started!\n");
      
      while(running) {
        Socket sock = server.accept();
        exec.execute(new SocketHandler(
            factory.createChannel(sock), sock));
      }
    } catch(IOException e) {
      LogProvider.getSimpleLog().fatal("Error running");
      LogProvider.getSimpleLog().fatal(e.toString());
    }
    exec.shutdown();
  }
  
  
  
// Início da classe interna <code>SocketHandler</code>.
/*****************************************************************/
/*****************************************************************/

  
  
  /**
   * SocketHandler trata as conexões recebidas pelo servidor.
   */
  public class SocketHandler implements Runnable {
    
    private Channel channel;
    
    private Socket sock;
    
    
    /**
     * Construtor padrão que recebe o Canal de comunicação 
     * de objetos na rede e o <code>Socket</code> da conexão.
     * @param ch Canal de comunicação de objetos na rede.
     * @param sc <code>Socket</code> da conexão.
     */
    public SocketHandler(Channel ch, Socket sc) {
      if(ch == null || sc == null)
        throw new IllegalArgumentException(
            "Invalid Null Arguments!");
      sock = sc;
      channel = ch;
      LogProvider.getSimpleLog().info("------------------------------");
      LogProvider.getSimpleLog().info("Handling socket: "+ sock.toString());
    }


    /**
     * Retorna o canal de comunicação de objetos na rede.
     * @return canal de comunicação de objetos na rede.
     */
    public Channel getChannel() {
      return channel;
    }


    /**
     * Define o canal de comunicação de objetos na rede.
     * @param ch canal de comunicação de objetos na rede.
     * @return Esta instância modificada de SocketHandler.
     */
    public SocketHandler setChannel(Channel ch) {
      this.channel = ch;
      return this;
    }


    /**
     * Retorna o <code>Socket</code> da conexão.
     * @return <code>Socket</code> da conexão.
     */
    public Socket getSocket() {
      return sock;
    }


    /**
     * Define o <code>Socket</code> da conexão.
     * @param sock <code>Socket</code> da conexão.
     * @return Esta instância modificada de <code>SocketHandler</code>.
     */
    public SocketHandler setSocket(Socket sock) {
      this.sock = sock;
      return this;
    }
    
    
    /**
     * Lê um objeto <code>Transport</code> do canal de rede.
     * @return <code>Transport</code> lido ou 
     * <code>null</code> caso ocorra erro.
     */
    public Transport read() {
      try {
        return channel.read();
      } catch(Exception e) {
        return null;
      }
    }
    
    
    /**
     * Escreve um objeto <code>Transport</code> na rede.
     * @param trp objeto <code>Transport</code> a ser escrito na rede.
     */
    public void write(Transport trp) {
      if(trp == null || sock.isClosed()) return;
      
      try {
        channel.write(trp);
        LogProvider.getSimpleLog().info("Response sent: "+ trp.getObject());
      } catch(IOException e) {
        LogProvider.getSimpleLog().warning(
            "Error writing response: "+ e.toString());
      }
    }
    
    
    /**
     * Trata a invocação do método remoto.
     * @param trp <code>Transport</code> 
     * contendo os dados da invocação remota.
     * @return Novo objeto <code>Transport</code>
     * com o resultado da invocação.
     */
    private Transport handleInvoke(Transport trp) {
      if(trp == null) return null;
      
      RemoteMethod rmt = trp.castObject();
      if(rmt == null) return null;
      OpResult result = new OpResult();
      Transport rtp = new Transport();
      
      LogProvider.getSimpleLog().info("Remote request: "+ rmt);
      this.checkInputStreamReference(rmt, trp);
      
      if(!container.contains(rmt.objectName())) {
        String msg = "Object Not Found ["+ rmt.objectName()+ "]";
        LogProvider.getSimpleLog().warning(msg);
        result.setSuccessOperation(false);
        result.setError(new UnsupportedOperationException(msg));
        return rtp.setObject(result);
      }
      
      Object ret = null;
      Invoker inv = new Invoker(
          container.get(rmt.objectName()), rmt);

      try {
        ret = inv.invoke();
        result.setSuccessOperation(true);
      } 
      catch(MethodInvocationException ex) {
        result.setSuccessOperation(false);
        result.setError(ex);
        LogProvider.getSimpleLog().warning(
            "Error invoking method ["+ rmt
                + "]: "+ ex.toString());
      }
      
      if(ret != null 
          && InputStream.class
          .isAssignableFrom(ret.getClass()))
        rtp.setInputStream((InputStream) ret);
      else {
        result.setReturn(ret);
      }
      rtp.setObject(result);
      return rtp;
    }
    
    
    /**
     * Trata a conexão recebida.
     */
    @Override
    public void run() {
      if(sock == null || sock.isClosed())
        return;
    
      Transport trp = this.read();
      if(trp == null) {
        LogProvider.getSimpleLog().info("Connection closed by client.");
        channel.close();
        return;
      }
      
      this.write(handleInvoke(trp));
      this.run();
    }
    
    
    /**
     * Verifica se o método a ser invocado recebe um argumento 
     * do tipo <code>InputStream</code>, que no caso é 
     * substituído por <code>Transport.getInputStream()</code>.
     * @param rmt Método remoto <code>RemoteMethod</code>.
     * @param trp <code>Transport</code> 
     * contendo os dados da invocação remota.
     */
    public void checkInputStreamReference(RemoteMethod rmt, Transport trp) {
      if(trp == null || rmt == null || rmt.argTypes() == null)
        return;
      
      Class[] types = rmt.argTypes();
      for(int i = 0; i < types.length; i++) {
        if(InputStream.class.isAssignableFrom(types[i])
            && trp.hasContentEmbedded())
          try { rmt.getArgs().set(i, trp.getInputStream()); }
          catch(Exception e) { rmt.addArg(trp.getInputStream()); }
      }
    }
    
  }
  
}
