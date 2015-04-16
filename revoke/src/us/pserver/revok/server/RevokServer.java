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

import com.jpower.rfl.Reflector;
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
import us.pserver.revok.channel.Transport;
import us.pserver.revok.container.AuthenticationException;
import us.pserver.revok.container.ObjectContainer;
import us.pserver.revok.factory.ChannelFactory;
import us.pserver.revok.factory.HttpFactoryProvider;

/**
 * Servidor de rede para objetos remotos, cujos métodos 
 * serão invocados remotamente.
 * 
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 11/11/2013
 */
public class RevokServer extends AbstractServer {
  
  public static final String SERVER_KEY = RevokServer.class.getName();
  
  public static final int SOCK_SO_TIMEOUT = 500;
  
  /**
   * <code>
   *  HTTP_CONN_BUFFER_SIZE = 8*1024
   * </code><br>
   * Tamanho de buffer da conexao HTTP.
   */
  public static final int HTTP_CONN_BUFFER_SIZE = 8*1024;
  
  
  private transient Reflector ref;
  
  private transient HttpConnector con;
  
  private transient ChannelFactory<HttpServerConnection> factory;
  
  private ExecutorService exec;
  
  
  /**
   * Construtor que recebe como argumento o 
   * container de objetos <code>ObjectContainer</code>.
   * e informações de conexão de rede <code>HttpConnector</code>.
   * Cria um servidor com conexão de rede padrão com canal de 
   * comunicação do tipo <code>NetworkChannel</code>.
   * @param cont container de objetos <code>ObjectContainer</code>.
   * @see us.pserver.remote.ObjectContainer
   * @see us.pserver.remote.NetConnector
   */
  public RevokServer(ObjectContainer cont) {
    super(cont);
    cont.put(SERVER_KEY, this);
    ref = new Reflector();
    con = new HttpConnector();
    factory = HttpFactoryProvider.factory()
        .enableGZipCompression()
        .enableCryptography()
        .getHttpResponseChannelFactory();
  }
  
  
  /**
   * Construtor que recebe como argumentos o 
   * container de objetos <code>ObjectContainer</code> 
   * e a conexão de rede <code>HttpConnector</code>.
   * Cria um servidor com o canal de 
   * comunicação do tipo <code>NetworkChannel</code>.
   * @param cont container de objetos <code>ObjectContainer</code>.
   * @param hcon Conexão de rede <code>HttpConnector</code>.
   * @see us.pserver.remote.ObjectContainer
   * @see us.pserver.remote.NetConnector
   */
  public RevokServer(ObjectContainer cont, HttpConnector hcon) {
    this(cont);
    cont.put(SERVER_KEY, this);
    if(con == null) throw new
        IllegalArgumentException(
            "[RevokServer( ObjectContainer, HttpConnector )] "
                + "Invalid NetConnector: "+ con);
    this.con = hcon;
  }
  
  
  /**
   * Retorna as informações de rede encapsuladas
   * por <code>HttpConnector</code>.
   * @return <code>HttpConnector</code>.
   */
  public HttpConnector getConnector() {
    return con;
  }


  /**
   * Define as informações de rede encapsuladas
   * por <code>HttpConnector</code>.
   * @param con <code>HttpConnector</code>.
   * @return Esta instância modificada de ObjectServer.
   */
  public RevokServer setConnector(HttpConnector con) {
    this.con = con;
    return this;
  }


  /**
   * Retorna a fábrica do canal de comunicação de objetos na rede.
   * @return Fábrica do canal de comunicação de objetos na rede.
   */
  public ChannelFactory<HttpServerConnection> getChannelFactory() {
    return factory;
  }


  /**
   * Define a fábrica do canal de comunicação de objetos na rede.
   * @param fact fábrica do canal de comunicação de objetos na rede.
   * @return Esta instância modificada de ObjectServer.
   */
  public RevokServer setChannelFactory(ChannelFactory<HttpServerConnection> fact) {
    if(fact != null)
      this.factory = fact;
    return this;
  }
  
  
  /**
   * Valida pré-requisitos para execução do servidor.
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
  public RevokServer startNewThread() {
    preStart();
    new Thread(this, "RevokServer").start();
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
      LogProvider.getSimpleLog().info("Listening on: "+ con.toString());
      LogProvider.getSimpleLog().info("Server started!\n");
      
      while(isRunning()) {
        try {
          Socket sock = server.accept();
          DefaultBHttpServerConnection conn = 
              new DefaultBHttpServerConnection(
                  HTTP_CONN_BUFFER_SIZE);
          exec.submit(new HttpConnectionHandler(
              factory.createChannel(conn), conn));
        } catch(SocketTimeoutException se) {}
      }
    } catch(IOException e) {
      LogProvider.getSimpleLog().fatal(
          new IOException("Error running NetworkServer", e), true);
    }
    exec.shutdown();
  }
  
  
  
// Início da classe interna HttpConnectionHandler.
/*****************************************************************/
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
            "[HttpConnectionHandler.invoke( Object, RemoteMethod )] "
                + "Invalid invocation object ["+ obj+ "]");
      if(rm == null || rm.method() == null)
        throw new MethodInvocationException(
            "[HttpConnectionHandler.invoke( Object, RemoteMethod )] "
                + "Invalid RemoteMethod ["+ rm+ "]");
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
      
      this.write(handleInvoke(trp));
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
        conn.close();
      } catch(Exception e) {}
    }
    
  }
  
}
