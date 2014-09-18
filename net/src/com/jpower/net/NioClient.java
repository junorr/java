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
import java.nio.channels.CancelledKeyException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * <code>NioClient</code> implementa um socket cliente
 * utilizando o novo pacote de entrada/saída do Java 7
 * <code>java.nio</code>. O uso de <code>NioClient</code>
 * se dá através da interface <code>ConnectionHandler</code>,
 * que fará o tratamento da conexão aberta.
 * 
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 28/05/2013
 */
public class NioClient implements Runnable {

  private SocketChannel channel;
  
  private Selector selector;
  
  private SelectionKey key;
  
  private ConnectionHandler handler;
  
  private NetConfig config;
  
  private Logger log;
  
  private boolean running;
  
  private DynamicBuffer dyn;
  
  private ReceiveFilter filter;
  
  
  /**
   * Construtor padrão que recebe como parâmetros um objeto de
   * configuração <code>NetConfig</code> e o responsável por tratar
   * a conexão aberta <code>ConnectionHandler</code>.
   * @param conf
   * @param ch
   * @throws IOException 
   */
  public NioClient(NetConfig conf, ConnectionHandler ch) throws IOException {
    String error = "";
    if(conf == null)
      error += "Invalid Configuration Object: "+ conf;
    if(ch == null)
      error += "\nInvalid Connection Handler: "+ ch;
    if(!error.isEmpty())
      throw new IllegalArgumentException(error);
    
    handler = ch;
    config = conf;
    log = config.getLogger();
    running = false;
    dyn = new DynamicBuffer();
    filter = config.getReceiveFilter();
    key = null;
  }


  /**
   * Cria o endereço utilizado para criação do socket de conexão.
   * @return <code>SocketAddress</code>.
   */
  private SocketAddress createAddress() {
    InetSocketAddress addr;
    
    if(config.getAddress().equals("*"))
      addr = new InetSocketAddress("localhost",
          config.getPort());
    else
      addr = new InetSocketAddress(
          config.getAddress(), config.getPort());
    
    return addr;
  }
  
  
  /**
   * Conecta <code>NioClient</code> ao destino de rede
   * configurado em <code>NetConfig</code>.
   * @return Esta instância modificada de <code>NioClient</code>.
   */
  public NioClient connect() {
    SocketAddress addr = this.createAddress();
    try {
      selector = Selector.open();
      
      log.info("Connecting to: "+ addr);
      
      channel = SocketChannel.open(addr);
      
      log.debug("Setting non-blocking socket");
      channel.configureBlocking(false);
      
      log.debug("Setting SO_RCVBUF: "
          + config.getBufferSize());
      
      channel.setOption(
          StandardSocketOptions.SO_RCVBUF, 
          config.getBufferSize());
      
      log.debug("Socket open and connected");
      handler.connected(channel);
      
      key = channel.register(selector, SelectionKey.OP_READ);
      
    } catch(IOException ex) {
      log.fatal("Error connecting to: " + addr);
      log.fatal(ex);
      handler.error(ex);
    }
    return this;
  }
  
  
  /**
   * Inicia a execução de <code>NioClient</code> em uma nova Thread.
   */
  public void start() {
    new Thread(this, "NioClient").start();
  }
  
  
  /**
   * Inicia a execução de <code>NioClient</code> na Thread atual.
   */
  @Override
  public void run() {
    try {
      log.debug("client running...");
      running = true;
      loop();
    } catch(CancelledKeyException ce) {
      log.warn("Connection closed!");
      log.warn(ce.toString());
      this.close();
      handler.disconnected(channel);
    } catch(IOException ex) {
      log.fatal(ex);
      this.close();
      handler.error(ex);
    }
  }
  
  
  public boolean isRunning() {
    return running;
  }
  
  
  /**
   * Encerra a execução de <code>NioClient</code> e fecha a conexão.
   */
  public void close() {
    running = false;
    try { 
      channel.close(); 
      key.cancel();
      selector.close();
    } catch(IOException e) {}
  }
  
  
  /**
   * Loop principal que realiza todas as tarefas do socket.
   * @throws IOException caso ocorra erro na conexão ou 
   * transferência de dados.
   */
  private void loop() throws IOException {
    while(running) {
      
      if(handler.isSending())
        this.registerWriteOp();
      
      int num = selector.select();
      if(num <= 0) continue;
      
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
        
        if(key.isReadable())
          this.read(key);
        
        else if(key.isWritable())
          this.write(key);
      }
    }
    this.close();
    log.info("Socket closed. Exiting...");
  }
  
  
  private void registerWriteOp() {
    key.interestOps(SelectionKey.OP_WRITE);
  }
  
  
  /**
   * Lê dados do socket.
   * @param key Chave para leitura.
   * @throws IOException Caso ocorra erro lendo dados do socket.
   */
  private void read(SelectionKey key) throws IOException {
    if(key == null || !key.isValid() 
        || !key.isReadable()) return;
    
    ByteBuffer buffer = ByteBuffer
        .allocateDirect(config.getBufferSize());
    
    try {
      int read = channel.read(buffer);
      if(read == 0) return;
      
      if(read < 0) {
        log.debug("Connection closed by server: "
            + channel.getRemoteAddress());
        this.close();
        return;
      }
      
      buffer.flip();
      
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
      
      if(receive) handler.received(buffer);
      
    } catch(IOException ex) {
      log.warn("Error reading from channel");
      log.warn("Connection closed by server");
      log.warn(ex.toString());
      this.close();
      handler.disconnected(channel);
    }
  }
  
  
  /**
   * Escreve dados no socket.
   * @param key Chave para escrita.
   * @throws IOException Caso ocorra erro escrevendo dados no socket.
   */
  private void write(SelectionKey key) throws IOException {
    if(key == null || !key.isValid() 
        || !key.isWritable()) return;

    ByteBuffer buffer = null;
    if(!handler.isSending() || (buffer = handler.sending()) == null 
        || buffer.flip().limit() == 0) {
      
      log.warn("Buffer not ready to writeOp");
      key.interestOps(SelectionKey.OP_READ);
      return;
    }
    
    try {
      int write = channel.write(buffer);
      if(write <= 0) return;
      key.interestOps(SelectionKey.OP_READ);
      
      handler.sent(write);
      
    } catch(IOException ex) {
      log.error("Error writing to channel");
      log.error(ex);
      this.close();
      handler.disconnected(channel);
    }
  }
  
  
  /**
   * Retorna o <code>SocketChannel</code> aberto por <code>NioClient</code>.
   * @return <code>SocketChannel</code> aberto por <code>NioClient</code>.
   */
  public SocketChannel getSocketChannel() {
    return channel;
  }


  /**
   * Retorna o <code>ConnectionHandler</code> utilizado por <code>NioClient</code>.
   * @return <code>ConnectionHandler</code> utilizado por <code>NioClient</code>.
   */
  public ConnectionHandler getConnectionHandler() {
    return handler;
  }


  /**
   * Retorna o <code>NetConfig</code> utilizado por <code>NioClient</code>.
   * @return <code>NetConfig</code> utilizado por <code>NioClient</code>.
   */
  public NetConfig getConfiguration() {
    return config;
  }
  
}
