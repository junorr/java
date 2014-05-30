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

import com.jpower.log.Logger;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * <code>NioServer</code> implementa um socket servidor de
 * rede, manipulando as conexões recebidas
 * com Java 7 NIO em uma única <code>Thread</code>.
 * Sua utilização se dá através das interfaces 
 * <code>ConnectionHandler</code> para tratamento de conexões e 
 * <code>ConnectionHandlerFactory</code>, que cria instâncias
 * de <code>ConnectionHandler</code> para cada conexão recebida.
 * Configurações do socket, como endereço de rede e porta, são feitas
 * através da classe <code>NetConfig</code>.
 * @see com.jpower.net.ConnectionHandler
 * @see com.jpower.net.ConnectionHandlerFactory
 * @see com.jpower.net.NetConfig
 * 
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 28/05/2013
 */
public class NioServer implements Runnable {
  
  private NetConfig config;
  
  private ServerSocketChannel schannel;
  
  private Selector selector;
  
  private SelectionKey skey;
  
  private Logger log;
  
  private ConnectionHandlerFactory factory;
  
  private boolean running;
  
  private ReceiveFilter filter;
  
  private DynamicBuffer dyn;
  
  private HostPool pool;
  
  
  /**
   * Construtor padrão que recebe um objeto de configuração
   * <code>NetConfig</code> e a fábrica de instâncias de
   * <code>ConnectionHandler</code>. O socket servidor é criado
   * na construção, porém não é vinculado ao endereço.
   * @param conf objeto de configuração <code>NetConfig</code>.
   * @param factory fábrica de instâncias de
   * <code>onnectionHandler</code>.
   * @throws IOException Caso ocorra erro na criação do
   * socket.
   */
  public NioServer(NetConfig conf, ConnectionHandlerFactory factory) throws IOException {
    String error = "";
    if(conf == null)
      error += "Invalid Configuration Object: "+ conf;
    if(factory == null)
      error += "\nInvalid Connection Handler: "+ factory;
    if(!error.isEmpty())
      throw new IllegalArgumentException(error);
    
    log = conf.getLogger();
    this.factory = factory;
    
    running = false;
    config = conf;
    filter = config.getReceiveFilter();
    dyn = new DynamicBuffer();
    pool = new HostPool();
    skey = null;
    
    init();
  }
  
  
  /**
   * Cria o socket servidor.
   * @throws IOException Caso ocorra erro na criação do socket.
   */
  private void init() throws IOException {
    try {
      log.debug("Init Server...");
    
      schannel = ServerSocketChannel.open();
    
      log.debug("Setting SO_RCVBUF...");
    
      schannel.setOption(
          StandardSocketOptions.SO_RCVBUF,
          config.getBufferSize());
    
      schannel.configureBlocking(false);
    
      selector = Selector.open();
      log.debug("Init Server Done!");
      
    } catch(IOException ex) {
      log.fatal("Fatal Error initializing server");
      log.fatal(ex);
      throw ex;
    }
  }
  
  
  /**
   * Pára a execução do servidor e fecha
   * as conexões.
   */
  public void stopServer() {
    running = false;
  }
  
  
  /**
   * Cria o <code>SocketAddress</code> utilizado pelo servidor.
   * @return <code>SocketAddress</code>
   */
  private SocketAddress createAddress() {
    InetSocketAddress addr;
    
    if(config.getAddress().equals("*"))
      addr = new InetSocketAddress(
          config.getPort());
    else
      addr = new InetSocketAddress(
          config.getAddress(), config.getPort());
    
    return addr;
  }
  
  
  /**
   * Vincula o socket ao endereço configurado e
   * inicia a execução do servidor em uma nova Thread.
   */
  public void startServer() {
    this.preStart();
    new Thread(this, "NioServer").start();
  }
  
  
  /**
   * Chama o loop principal <code>loop()</code>.
   * @throws RuntimeException 
   */
  @Override
  public void run() throws RuntimeException {
    try {
      this.loop();
    } catch(IOException ex) {
      log.fatal(ex);
      throw new RuntimeException(ex);
    }
  }
  
  
  /**
   * Vincula o socket ao endereço configurado.
   * @throws RuntimeException Caso ocorra erro na vinculação.
   */
  private void preStart() throws RuntimeException {
    try {
      log.info("Starting NioServer... ");
      schannel.bind(this.createAddress());
    
      log.info("Server started and bound to "
          + config.getAddress()+ ":"+ config.getPort());
    
      running = true;
      
      log.debug("Registering selector...");
      skey = schannel.register(selector, SelectionKey.OP_ACCEPT);
      
      log.debug("Selector registered");
      
    } catch(IOException ex) {
      log.fatal("Fatal Error biding server");
      log.fatal(ex);
      throw new RuntimeException(ex);
    }
  }
  
  
  /**
   * Loop principal que escuta e trata as conexões recebidas.
   * @throws IOException 
   */
  private void loop() throws IOException {
    try {
      while(running) {
        
        this.registerWriteInterest();
        skey.interestOps(SelectionKey.OP_ACCEPT);
        
        if(selector.select() <= 0) continue;
        Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
      
        while(keys.hasNext()) {
          SelectionKey key = keys.next();
          keys.remove();
          
          if(!key.isValid()) continue;
        
          //Desabilita os interesses prontos temporariamente,
          //para evitar duplicação de chamadas.
          //Eles serão reabilitados nos métodos específicos.
          int readyOps = key.readyOps();
          key.interestOps(key.interestOps() & ~readyOps);
    
          if(key.isAcceptable())
            this.accept();
        
          else if(key.isReadable())
            this.read((VirtualHost) key.attachment());
        
          else if(key.isWritable())
            this.write((VirtualHost) key.attachment());
        }
      }
      schannel.close();
      selector.close();
    } catch(IOException ex) {
      log.error("Error selecting event");
      log.error(ex);
      throw ex;
    }
  }
  
  
  private void registerWriteInterest() {
    for(int i = 0; i < pool.size(); i++) {
      VirtualHost vh = pool.get(i);
      if(vh.isSending())
        vh.key().interestOps(SelectionKey.OP_WRITE);
    }
  }
  
  
  /**
   * Aceita conexões recebidas.
   * @throws IOException Caso ocorra erro aceitando a conexão.
   */
  private void accept() throws IOException {
    SocketChannel ch = schannel.accept();
    
    log.debug("New connection attempt");
    
    ch.configureBlocking(false);
    ConnectionHandler handler = factory.createConnectionHandler();
    VirtualHost vhost = new VirtualHost(ch, handler);
    
    log.debug("Registering interests OP_READ...");
    
    vhost.key(ch.register(selector, SelectionKey.OP_READ, vhost));
    
    log.info("Connection stablished: "+ ch.getRemoteAddress());
    
    vhost.connected(ch);
    pool.add(vhost);
  }
  
  /**
   * Escreve dados no socket.
   * @param key Chave de escrita.
   */
  private void write(VirtualHost vhost) {
    if(vhost == null || vhost.key() == null 
        || !vhost.key().isValid()) return;
    
    ByteBuffer buffer = null;
    if(!vhost.isSending() || (buffer = vhost.sending()) == null 
        || buffer.flip().limit() == 0) {
      
      log.warn("Buffer not ready to writeOp");
      vhost.key().interestOps(SelectionKey.OP_READ);
      return;
    }
    
    log.debug("Writing to: "+ vhost.getHost());
    
    try {
      int write = vhost.channel().write(buffer);
      vhost.key().interestOps(SelectionKey.OP_READ);
      if(write > 0) vhost.sent(write);
      
    } catch(IOException e) {
      closeConnection(vhost, e);
    }
  }
  
  
  /**
   * Lê dados do socket.
   * @param key Chave de leitura.
   */
  private void read(VirtualHost vhost) {
    if(vhost == null || vhost.key() == null 
        || !vhost.key().isValid()) return;
    
    try {
      ByteBuffer buffer = ByteBuffer
          .allocateDirect(config.getBufferSize());
      int read = vhost.channel().read(buffer);
      
      if(read == 0) return;
      else if(read < 0) {
        log.info("Connection closed by client");
        closeConnection(vhost, null);
        return;
      }
      
      buffer.flip();
      log.debug("Reading from: "+ vhost.getHost());
      
      boolean receive = true;
      if(config.isAutoFilterActivated() && filter != null) {
        
        boolean match = filter.match(buffer);
        
        if(match && config.isAutoFilterActivated())
          buffer = filter.filter(buffer);
        
        dyn.write(buffer);
        
        if(!match) {
          receive = false;
          
        } else if(!dyn.isEmpty()) {
          buffer = dyn.toByteBuffer();
          dyn.clear();
        }
      }
      
      if(receive) {
        vhost.received(buffer);
      }
      
      //Habilita leitura
      vhost.key().interestOps(SelectionKey.OP_READ);
      
    } catch(IOException ex) {
      closeConnection(vhost, ex);
    }
  }
  
  
  /**
   * Fecha a conexão.
   * @param key Chave da conexão.
   * @param th Erro caso tenha ocorrido antes do fechamento.
   */
  private void closeConnection(VirtualHost host, Throwable th) {
    try {
      log.info("Connection shutdown: "+ host.getHost());
      host.key().cancel();
      host.channel().close();
      host.disconnected(host.channel());
      pool.remove(host);
    } catch(IOException ex) {}
  }
  
  
  /**
   * Retorna a classe de configuração utilizada.
   * @return <code>NetConfig</code>
   */
  public NetConfig getConfiguration() {
    return this.config;
  }


  /**
   * Retorna o Logger utilizado.
   * @return <code>Logger</code>
   * @see com.jpower.log.Logger
   */
  public Logger getLogger() {
    return log;
  }


  /**
   * Define o objeto de log.
   * @param log <code>Logger</code>
   * @return Esta instância modificada de <code>NioServer</code>.
   */
  public NioServer setLogger(Logger log) {
    this.log = log;
    return this;
  }


  /**
   * Retorna o filtro de recebimento utilizado, caso exista.
   * @return <code>ReceiveFilter</code> ou <code>null</code>.
   */
  public ReceiveFilter getReceiveFilter() {
    return filter;
  }


  /**
   * Define um filtro de recebimento de dados.
   * @param filter <code>ReceiveFilter</code>.
   * @return Esta instância modificada de <code>NioServer</code>.
   */
  public NioServer setReceiveFilter(ReceiveFilter filter) {
    this.filter = filter;
    return this;
  }

}
