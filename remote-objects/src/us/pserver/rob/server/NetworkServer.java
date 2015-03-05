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

package us.pserver.rob.server;

import us.pserver.rob.factory.DefaultFactoryProvider;
import us.pserver.rob.container.AuthenticationException;
import us.pserver.rob.container.ObjectContainer;
import us.pserver.rob.factory.SocketChannelFactory;
import us.pserver.rob.channel.Channel;
import com.jpower.rfl.Reflector;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import static us.pserver.chk.Checker.nullarg;
import us.pserver.log.LogProvider;
import us.pserver.rob.MethodChain;
import us.pserver.rob.MethodInvocationException;
import us.pserver.rob.NetConnector;
import us.pserver.rob.OpResult;
import us.pserver.rob.RemoteMethod;
import us.pserver.rob.channel.Transport;

/**
 * Servidor de rede para objetos remotos, cujos métodos 
 * serão invocados remotamente.
 * 
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 11/11/2013
 */
public class NetworkServer extends AbstractServer {
  
  public static final String SERVER_KEY = NetworkServer.class.getName();
  
  public static final int SOCK_SO_TIMEOUT = 500;
  
  
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
    factory = DefaultFactoryProvider.factory()
        .enableGZipCompression()
        .enableCryptography()
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
    setRunning(true);
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
      server.setSoTimeout(SOCK_SO_TIMEOUT);
      LogProvider.getSimpleLog().info("Channel: "
          + factory.createChannel(new Socket()).getClass().getCanonicalName());
      LogProvider.getSimpleLog().info("Listening on: "+ con.toString());
      LogProvider.getSimpleLog().info("Server started!\n");
      
      while(isRunning()) {
        try {
          Socket sock = server.accept();
          exec.submit(new SocketHandler(
              factory.createChannel(sock), sock));
        } catch(SocketTimeoutException se) {}
      }
    } catch(IOException e) {
      LogProvider.getSimpleLog().fatal(
          new IOException("Error running NetworkServer", e), true);
    }
    exec.shutdown();
  }
  
  
  
// Início da classe interna SocketHandler.
/*****************************************************************/
/*****************************************************************/

  
  
  /**
   * SocketHandler trata as conexões recebidas pelo servidor.
   */
  public class SocketHandler implements Runnable {
    
    public static final String 
        READ_ERROR = "Invalid length readed",
        CONN_RESET = "Connection reset";
    
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
      LogProvider.getSimpleLog()
          .info("------------------------------")
          .info("Handling socket: "+ sock.toString());
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
        String msg = e.getMessage();
        if(msg != null 
            && !msg.contains(READ_ERROR)
            && !msg.contains(CONN_RESET)) {
          LogProvider.getSimpleLog()
              .error("Error reading from channel")
              .error(e, true);
        }
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
            new IOException("Error writing response", e), false);
      }
    }
    
    
    private Object getObject(RemoteMethod rm) 
        throws MethodInvocationException, AuthenticationException {
      nullarg(RemoteMethod.class, rm);
      if(!container.contains(rm.objectName())) {
        throw new MethodInvocationException(
            "Object not found ["+ rm.objectName()+ "]");
      }
      Object o = null;
      if(container.isAuthEnabled()) {
        LogProvider.getSimpleLog().info(
            "Authentication Enabled: "+ rm.credentials());
        o = container.get(rm.credentials(), rm.objectName());
      }
      else {
        o = container.get(rm.objectName());
      }
      return o;
    }
    
    
    private OpResult invoke(RemoteMethod rm) {
      nullarg(RemoteMethod.class, rm);
      OpResult op = new OpResult();
      try {
        op.setReturn(
            this.invoke(
                this.getObject(rm), rm));
        op.setSuccessOperation(true);
      }
      catch(AuthenticationException | MethodInvocationException e) {
        op.setSuccessOperation(false);
        op.setError(e);
        LogProvider.getSimpleLog()
            .warning("Error invoking method ["+ rm.method()+ "]")
            .warning(e, true);
      }
      return op;
    }
    
    
    private OpResult invoke(MethodChain chain) {
      nullarg(MethodChain.class, chain);
      OpResult op = new OpResult();
      try {
        if(chain.current() == null)
          throw new MethodInvocationException(
              "Empty MethodChain. No method to invoke");
        
        Object obj = getObject(chain.current());
        do {
          this.logChain(obj, chain);
          obj = invoke(obj, chain.current());
        } while(chain.next() != null);
        op.setSuccessOperation(true);
        op.setReturn(obj);
      } 
      catch(AuthenticationException | MethodInvocationException e) {
        op.setSuccessOperation(false);
        op.setError(e);
        LogProvider.getSimpleLog()
            .warning("Error invoking method ["+ chain.current()+ "]")
            .warning(e, true);
      }
      return op;
    }
    
    
    private void logChain(Object obj, MethodChain chain) {
      if(obj == null || chain == null 
          || chain.current() == null) return;
      String msg = "";
      if(chain.current().objectName() == null)
        msg = obj.getClass().getSimpleName();
      msg += chain.current().toString();
      LogProvider.getSimpleLog().info("Invoking: "+ msg);
    }
    
    
    private Object invoke(Object obj, RemoteMethod rm) 
        throws AuthenticationException, MethodInvocationException {
      if(obj == null)
        throw new MethodInvocationException(
            "Invalid invocation object ["+ obj+ "]");
      if(rm == null || rm.method() == null)
        throw new MethodInvocationException(
            "Invalid RemoteMethod ["+ rm+ "]");
      Invoker iv = new Invoker(obj, rm);
      return iv.invoke();
    }
    
    
    private Transport pack(OpResult op) {
      nullarg(OpResult.class, op);
      Transport t = new Transport();
      Object ret = op.getReturn();
      if(ret != null && InputStream.class
          .isAssignableFrom(ret.getClass())) {
        t.setInputStream((InputStream) ret);
        op.setReturn(null);
      }
      t.setObject(op);
      return t;
    }
    
    
    /**
     * Trata a invocação do método remoto.
     * @param trp <code>Transport</code> 
     * contendo os dados da invocação remota.
     * @return Novo objeto <code>Transport</code>
     * com o resultado da invocação.
     */
    private Transport handleInvoke(Transport trp) {
      if(trp == null || trp.getObject() == null) 
        return null;
      LogProvider.getSimpleLog().info("Remote request: "+ trp.getObject());
      if(trp.isObjectFromType(RemoteMethod.class)) {
        RemoteMethod rm = trp.castObject();
        this.checkInputStreamReference(rm, trp);
        return pack(invoke(rm));
      }
      else if(trp.isObjectFromType(MethodChain.class)) {
        MethodChain chain = trp.castObject();
        this.checkInputStreamReference(chain.current(), trp);
        return pack(invoke(chain));
      }
      else return invalidType(trp);
    }
    
    
    private Transport invalidType(Transport t) {
      OpResult op = new OpResult();
      op.setSuccessOperation(false);
      op.setError(new MethodInvocationException(
          "Server can not handle this object type: "
              + t.getObject().getClass()));
      return pack(op);
    }
    
    
    /**
     * Trata a conexão recebida.
     */
    @Override
    public void run() {
      if(isClosed()) {
        close();
        return;
      }
    
      Transport trp = this.read();
      if(trp == null) {
        LogProvider.getSimpleLog().info("Connection closed by client.");
        close();
        return;
      }
      
      this.write(handleInvoke(trp));
      this.close();
      //this.run();
    }
    
    
    public boolean isClosed() {
      return sock == null
          || sock.isClosed()
          || !sock.isConnected()
          || sock.isInputShutdown()
          || sock.isOutputShutdown();
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
      if(trp == null 
          || !trp.hasContentEmbedded() 
          || rmt == null 
          || rmt.params() == null
          || rmt.params().isEmpty())
        return;
      
      for(int i = 0; i < rmt.params().size(); i++) {
        Object o = rmt.params().get(i);
        if(o != null && FakeInputStreamRef.class
            .isAssignableFrom(o.getClass())) {
          rmt.params().set(i, trp.getInputStream());
        }
      }//for
    }
    
    
    public void close() {
      try { channel.close(); }
      catch(Exception e) {}
      try { 
        sock.shutdownInput();
        sock.shutdownOutput();
        sock.close();
      } catch(Exception e) {}
    }
    
  }
  
}
