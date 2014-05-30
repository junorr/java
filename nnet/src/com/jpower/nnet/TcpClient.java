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
package com.jpower.nnet;

import com.jpower.nnet.conf.Configuration;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;


/**
 * Cliente TCP suportado internamente 
 * pela biblioteca netty.
 * <a href="https://netty.io/Main/WebHome">Netty Project</a>
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 2012-07-20
 */
public class TcpClient {
  
  private InetSocketAddress address;
  
  private ConnectionHandler handler;
  
  private ClientBootstrap bootstrap;
  
  private TcpChannel channel;
  
  private FrameControl fcontrol;
  
  
  /**
   * Construtor que recebe o endereço ao qual o cliente 
   * irá conectar-se e o handler que irá tratar a conexão
   * estabelecida.
   * @param address Endereço ao qual o cliente irá conectar-se.
   * @param handler Handler para tratar a conexão estabelecida.
   */
  public TcpClient(InetSocketAddress address, 
      ConnectionHandler handler, FrameControl control) {
    if(address == null) 
      throw new IllegalArgumentException(
          "Invalid SocketAddress: "+ address);
    if(handler == null) 
      throw new IllegalArgumentException(
          "Invalid ServerHandler: "+ handler);
    this.address = address;
    this.handler = handler;
    this.fcontrol = (control == null ? new FrameControl() : control);
  }
  
  
  /**
   * Construtor que recebe um objeto de configuração 
   * <code>Configuration</code> e o handler para tratar a conexão
   * a ser estabelecida.
   * 
   * @param conf Objeto de configuração <code>Configuration</code>.
   * @param handler Handler para tratar a conexão estabelecida.
   */
  public TcpClient(Configuration conf, ConnectionHandler handler) {
    if(conf == null) 
      throw new IllegalArgumentException(
          "Invalid Configuration: "+ conf);
    
    if(handler == null) 
      throw new IllegalArgumentException(
          "Invalid ServerHandler: "+ handler);
    
    this.handler = handler;
    this.setAddress(conf);
    this.fcontrol = conf.getFrameControl();
  }
  
  
  /**
   * Define o endereço para conexão a partir do
   * objeto de configuração <code>Configuration</code>.
   */
  private void setAddress(Configuration conf) {
    String addr = null;
    if(conf.getProxyAddress() != null 
        && !conf.getProxyAddress().trim().isEmpty())
      addr = conf.getProxyAddress();
    else
      addr = conf.getRemoteAddress();
    
    int port = 0;
    if(conf.getProxyPort() != 0)
      port = conf.getProxyPort();
    else
      port = conf.getRemotePort();
    
    this.address = new InetSocketAddress(addr, port);
  }
  
  
  /**
   * Define um objeto de configuração <code>Configuration</code>
   * com os parâmetros para estabelecer a conexão.
   * @param conf Objeto de configuração <code>Configuration</code>.
   * @return Esta instância modificada de TcpClient.
   */
  public TcpClient setConfiguration(Configuration conf) {
    this.setAddress(conf);
    this.fcontrol = conf.getFrameControl();
    return this;
  }
  
  
  /**
   * Conecta ao endereço remoto.
   * @return Esta instância modificada de TcpClient.
   */
  public TcpClient connect() {
    ChannelFactory fac =
        new NioClientSocketChannelFactory(
            Executors.newCachedThreadPool(),
            Executors.newCachedThreadPool());
    
    bootstrap = new ClientBootstrap(fac);
    
    bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
      @Override
        public ChannelPipeline getPipeline() {
          return Channels.pipeline(new GeneralTcpHandler(handler)
              .setFcontrol(fcontrol));
        }
    });
    
    bootstrap.setOption("tcpNoDelay", true);
    bootstrap.setOption("keepAlive", true);
 
    System.out.println("* Connecting to:");
    System.out.print("  [ "+ address.getAddress().getHostAddress()+ " : "+ address.getPort()+ " ]");    
    ChannelFuture cf = bootstrap.connect(address);
    this.wait(cf);
    System.out.println("   OK");
    
    channel = new TcpChannel(cf.getChannel())
        .setFrameControl(fcontrol);
    
    return this;
  }
  
  
  /**
   * Espera até que a operação futura do canal seja 
   * concluída, retornando um valor booleano 
   * indicando o sucesso da operação.
   * @param f Canal com operação futura pendente.
   * @return <code>true</code> se a operação
   * futura for concluída com sucesso, 
   * <code>false</code> caso contrário.
   */
  public boolean wait(ChannelFuture f) {
    if(f == null) return false;
    try {
      while(!f.isDone()) {
        Thread.sleep(20);
      }
      return f.isSuccess();
    } catch(InterruptedException ex) {
      ex.printStackTrace();
      return false;
    }
  }


  /**
   * Retorna o channel da conexão estabelecida.
   * @return <code>TcpChannel</code>.
   */
  public TcpChannel getNetChannel() {
    return channel;
  }
  
  
  /**
   * Fecha a conexão.
   * @return Esta instância modificada de TcpClient.
   */
  public TcpClient close() {
    channel.disconnect().close();
    return this;
  }
  
}
