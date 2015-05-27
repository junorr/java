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

package us.pserver.revok.server;

import us.pserver.revok.reflect.Invoker;
import us.pserver.revok.protocol.FakeInputStreamRef;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.http.HttpServerConnection;
import org.apache.http.impl.DefaultBHttpServerConnection;
import static us.pserver.chk.Checker.nullarg;
import us.pserver.log.LogProvider;
import us.pserver.revok.HttpConnector;
import us.pserver.revok.MethodChain;
import us.pserver.revok.MethodInvocationException;
import us.pserver.revok.OpResult;
import us.pserver.revok.RemoteMethod;
import us.pserver.revok.channel.Channel;
import us.pserver.revok.protocol.Transport;
import us.pserver.revok.container.AuthenticationException;
import us.pserver.revok.container.ObjectContainer;
import us.pserver.revok.factory.ChannelFactory;
import us.pserver.revok.factory.HttpFactoryBuilder;
import us.pserver.revok.protocol.JsonSerializer;
import us.pserver.revok.protocol.ObjectSerializer;

/**
 * Network HTTP object server for remoting method invocation.
 * 
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.1 - 20150422
 */
public class RevokServer extends AbstractServer {
  
  /**
   * <code>
   *   SERVER_KEY = RevokServer.class.getSimpleName();
   * </code><br/>
   * Key used to store the server on ObjectContainer.
   */
  public static final String SERVER_KEY = RevokServer.class.getSimpleName();
  
  /**
   * <code>
   *   SOCK_SO_TIMEOUT = 500;
   * </code><br/>
   * Default socket timeout.
   */
  public static final int SOCK_SO_TIMEOUT = 500;
  
  /**
   * <code>
   *  HTTP_CONN_BUFFER_SIZE = 8*1024
   * </code><br>
   * Default HTTP buffer size.
   */
  public static final int HTTP_CONN_BUFFER_SIZE = 8*1024;
  
  
  private transient HttpConnector con;
  
  private transient ChannelFactory<HttpServerConnection> factory;
  
  private ExecutorService exec;
  
  private ObjectSerializer serial;
  
  
  /**
   * Default constructor receives the <code>ObjectContainer</code>
   * with the objects whose methods will be invoked.
   * @param cont The <code>ObjectContainer</code>
   * with the objects whose methods will be invoked.
   * @see us.pserver.remote.ObjectContainer
   */
  public RevokServer(ObjectContainer cont) {
    super(cont);
    cont.put(ObjectContainer.NAMESPACE_GLOBAL, SERVER_KEY, this);
    con = new HttpConnector();
    factory = HttpFactoryBuilder.builder()
        .enableGZipCompression()
        .enableCryptography()
        .getHttpResponseChannelFactory();
    serial = new JsonSerializer();
  }
  
  
  /**
   * Constructor which receives the <code>ObjectContainer</code>
   * with the objects whose methods will be invoked and the
   * network information object <code>HttpConnector</code>.
   * @param cont The <code>ObjectContainer</code>
   * with the objects whose methods will be invoked.
   * @param hcon The network information object 
   * <code>HttpConnector</code>.
   * @see us.pserver.remote.ObjectContainer
   * @see us.pserver.remote.NetConnector
   */
  public RevokServer(ObjectContainer cont, HttpConnector hcon) {
    this(cont);
    if(con == null) throw new
        IllegalArgumentException(
            "[RevokServer( ObjectContainer, HttpConnector )] "
                + "Invalid NetConnector: "+ con);
    this.con = hcon;
  }
  
  
  /**
   * Constructor which receives the <code>ObjectContainer</code>,
   * the network information <code>HttpConnector</code> and
   * the default object serializer for encoding transmitted objects.
   * @param cont The <code>ObjectContainer</code>
   * with the objects whose methods will be invoked.
   * @param hcon The network information object 
   * <code>HttpConnector</code>.
   * @param serial The default object serializer for encoding 
   * transmitted objects.
   */
  public RevokServer(ObjectContainer cont, HttpConnector hcon, ObjectSerializer serial) {
    this(cont, hcon);
    if(serial == null)
      serial = new JsonSerializer();
    this.serial = serial;
  }
  
  
  /**
   * Get the <code>ObjectSerializer</code> for objects serialization.
   * @return <code>ObjectSerializer</code> for objects serialization.
   */
  public ObjectSerializer getObjectSerializer() {
    return serial;
  }
  
  
  /**
   * Set the <code>ObjectSerializer</code> for objects serialization.
   * @param serializer <code>ObjectSerializer</code> for objects serialization.
   */
  public RevokServer setObjectSerializer(ObjectSerializer serializer) {
    if(serializer != null) {
      serial = serializer;
    }
    return this;
  }
  
  
  /**
   * Get the network information object <code>HttpConnector</code>.
   * @return The network information object <code>HttpConnector</code>.
   */
  public HttpConnector getConnector() {
    return con;
  }


  /**
   * Set the network information object <code>HttpConnector</code>.
   * @param con The network information object <code>HttpConnector</code>.
   * @return This modified <code>RevokServer</code> instance.
   */
  public RevokServer setConnector(HttpConnector con) {
    this.con = con;
    return this;
  }


  /**
   * Get the network channel factory object.
   * @return The network channel factory object.
   */
  public ChannelFactory<HttpServerConnection> getChannelFactory() {
    return factory;
  }


  /**
   * Set the network channel factory object.
   * @param fact The network channel factory object.
   * @return This modified <code>RevokServer</code> instance.
   */
  public RevokServer setChannelFactory(ChannelFactory<HttpServerConnection> fact) {
    if(fact != null)
      this.factory = fact;
    return this;
  }
  
  
  /**
   * Validates and start the necessary components for server execution.
   */
  private void preStart() {
    if(con == null)
      throw new IllegalStateException("[RevokServer.preStart()] "
          + "Invalid NetConnector ["+ con + "]");
    if(factory == null)
      throw new IllegalArgumentException("[RevokServer.preStart()] "
          + "Invalid ChannelFactory ["+ factory+ "]");
    if(container == null)
      throw new IllegalArgumentException("[RevokServer.preStart()] "
          + "Invalid ObjectContainer ["+ container+ "]");
    
    LogProvider.getSimpleLog().info("Starting RevokServer...");
    setRunning(true);
    exec = Executors.newFixedThreadPool(availableThreads);
  }
  
  
  @Override
  public void start() {
    preStart();
    run();
  }
  
  
  /**
   * Starts the server execution in a new <code>Thread</code>.
   * @return This modified <code>RevokServer</code> instance.
   */
  public RevokServer startNewThread() {
    preStart();
    new Thread(this, "RevokServer").start();
    return this;
  }
  
  
  /**
   * Not invoke directly. Executes server routines.
   */
  @Override
  public void run() {
    try(ServerSocket server = con.connectServerSocket();) {
      server.setSoTimeout(SOCK_SO_TIMEOUT);
      LogProvider.getSimpleLog().info("Listening on: "+ con.toString());
      LogProvider.getSimpleLog().info("RevokServer started!\n");
      
      while(isRunning()) {
        try {
          Socket sock = server.accept();
          DefaultBHttpServerConnection conn = 
              new DefaultBHttpServerConnection(
                  HTTP_CONN_BUFFER_SIZE);
          conn.bind(sock);
          exec.submit(new HttpConnectionHandler(
              factory.createChannel(conn, serial), conn));
        } catch(SocketTimeoutException se) {}
      }//while
    } catch(IOException e) {
      LogProvider.getSimpleLog().fatal(
          new IOException("Error running NetworkServer", e), true);
    }
    LogProvider.getSimpleLog().info("Stopping ExecutorService...");
    exec.shutdown();
    LogProvider.getSimpleLog().info("RevokServer Shutdown!");
  }
  
  
  
/*****************************************************************/
/**   START OF INNER CLASS DECLARATION HttpConnectionHandler    **/
/*****************************************************************/

  
  
  /**
   * HttpConnectionHandler trata as conexões recebidas pelo servidor.
   */
  public class HttpConnectionHandler implements Runnable {
    
    public static final String 
        READ_ERROR = "Invalid length readed",
        CONN_RESET = "Connection reset";
    
    private Channel channel;
    
    private HttpServerConnection conn;
    
    
    /**
     * Construtor padrão que recebe o Canal de comunicação 
     * de objetos na rede e o <code>Socket</code> da conexão.
     * @param ch Canal de comunicação de objetos na rede.
     * @param hsc <code>Socket</code> da conexão.
     */
    public HttpConnectionHandler(Channel ch, HttpServerConnection hsc) {
      if(ch == null)
        throw new IllegalArgumentException(
            "[HttpConnectionHandler( Channel, HttpServerConnection )] "
                + "Invalid Channel {"+ ch+ "}");
      if(hsc == null || !hsc.isOpen())
        throw new IllegalArgumentException(
            "[HttpConnectionHandler( Channel, HttpServerConnection )] "
                + "Invalid HttpServerConnection {"+ hsc+ "}");
      conn = hsc;
      channel = ch;
      LogProvider.getSimpleLog()
          .info("------------------------------")
          .info("Handling socket: "+ conn.toString());
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
     * @return Esta instância modificada de HttpConnectionHandler.
     */
    public HttpConnectionHandler setChannel(Channel ch) {
      this.channel = ch;
      return this;
    }


    /**
     * Retorna a <code>HttpServerConnection</code> da conexão.
     * @return <code>HttpServerConnection</code> da conexão.
     */
    public HttpServerConnection getConnection() {
      return conn;
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
      if(trp == null || !conn.isOpen()) return;
      
      try {
        channel.write(trp);
        LogProvider.getSimpleLog().info("Response sent: "+ trp.getObject());
      } catch(IOException e) {
        LogProvider.getSimpleLog().warning(
            new IOException("Error writing response", e), false);
      }
    }
    
    
    private OpResult invoke(RemoteMethod rm) {
      nullarg(RemoteMethod.class, rm);
      OpResult op = new OpResult();
      try {
        Invoker iv = new Invoker(container, rm.getCredentials());
        op.setReturn(iv.invoke(rm));
        op.setSuccessOperation(true);
      }
      catch(Exception e) {
        op.setSuccessOperation(false);
        op.setError(e);
        LogProvider.getSimpleLog()
            .warning("Error invoking method ["+ rm+ "}")
            .warning(e, !(e instanceof AuthenticationException));
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
        
        Invoker iv = new Invoker(container, chain.current().getCredentials());
        Object obj = iv.getObject(chain.current());
        do {
          this.logChain(obj, chain);
          obj = iv.invoke(chain.current());
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
    
    
    private Transport pack(OpResult op) {
      nullarg(OpResult.class, op);
      Transport t = new Transport();
      Object ret = op.getReturn();
      if(ret != null && InputStream.class
          .isAssignableFrom(ret.getClass())) {
        t.setInputStream((InputStream) ret);
        op.setReturn(new FakeInputStreamRef());
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
      LogProvider.getSimpleLog().info("<- Remote request: "+ trp.getObject());
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
          "[HttpConnectionHandler.InvalidType( Transport )] "
              + "Server can not handle this object type: "
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
      this.write( handleInvoke(trp) );
      if(channel.isValid())
        this.run();
      else
        this.close();
    }
    
    
    public boolean isClosed() {
      return conn == null
          || !conn.isOpen();
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
          || rmt.args() == null
          || rmt.args().isEmpty())
        return;
      
      for(int i = 0; i < rmt.args().size(); i++) {
        Object o = rmt.args().get(i);
        if(o != null && FakeInputStreamRef.class
            .isAssignableFrom(o.getClass())) {
          rmt.args().set(i, trp.getInputStream());
        }
      }//for
    }
    
    
    public void close() {
      try { channel.close(); }
      catch(Exception e) {}
      try { 
        conn.close();
      } catch(Exception e) {}
    }
    
  }
  
}
