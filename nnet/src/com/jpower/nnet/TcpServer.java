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
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;


/**
 * Servidor TCP assíncrono suportado internamente 
 * pela biblioteca netty.
 * <a href="https://netty.io/Main/WebHome">Netty Project</a>
 * 
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 2012-07-20
 */
public class TcpServer {
  
  private InetSocketAddress address;
  
  private ConnectionHandlerFactory factory;
  
  private ServerBootstrap bootstrap;
  
  private TcpChannel channel;
  
  private FrameControl fcontrol;
  
  
  /**
   * Construtor que recebe o endereço no qual o servidor irá escutar
   * por conexões, e uma fábrica de handlers para tratar as conexões 
   * aceitas.
   * 
   * @param address Endereço no qual o servidor irá escutar por
   * conexões.
   * @param factory Fábrica de handlers para tratar as conexões aceitas.
   * @param control Configuração de controle de janelas de dados. Pode ser 
   * <code>null</code>, habilitando o controle no envio e recebimento.
   */
  public TcpServer(InetSocketAddress address, 
      ConnectionHandlerFactory factory, FrameControl control) {
    
    if(address == null) 
      throw new IllegalArgumentException(
          "Invalid SocketAddress: "+ address);
    if(factory == null) 
      throw new IllegalArgumentException(
          "Invalid ServerHandlerFactory: "+ factory);
    
    this.address = address;
    this.factory = factory;
    fcontrol = (control == null ? new FrameControl() : control);
  }


  /**
   * Construtor que recebe como argumentos um objeto de configuração
   * <code>Configuration</code>, e uma fábrica de handlers para
   * tratar as conexões aceitas.
   * @param conf Objeto de configuração <code>Configuration</code>.
   * @param factory Fábrica de handlers para tratar as conexões aceitas.
   */
  public TcpServer(Configuration conf, ConnectionHandlerFactory factory) {
    
    if(conf == null) 
      throw new IllegalArgumentException(
          "Invalid Configuration: "+ conf);
    if(factory == null) 
      throw new IllegalArgumentException(
          "Invalid ServerHandlerFactory: "+ factory);
    
    this.factory = factory;
    this.fcontrol = conf.getFrameControl();
    
    if(conf.getLocalAddress() == null
        || conf.getLocalAddress().trim().isEmpty())
      this.address = new InetSocketAddress(conf.getLocalPort());
    else
      this.address = new InetSocketAddress(
          conf.getLocalAddress(), conf.getLocalPort());
  }


  /**
   * Inicia o servidor TCP.
   * @return Esta instância modificada de TcpServer.
   */
  public TcpServer start() {
    System.out.print("* Starting TcpServer... ");
    
    ChannelFactory fac =
        new NioServerSocketChannelFactory(
            Executors.newCachedThreadPool(),
            Executors.newCachedThreadPool());
    
    bootstrap = new ServerBootstrap(fac);
    
    bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
      @Override
        public ChannelPipeline getPipeline() {
          return Channels.pipeline(
              new GeneralTcpHandler(factory.create())
              .setFcontrol(fcontrol));
        }
    });
    
    bootstrap.setOption("child.tcpNoDelay", true);
    bootstrap.setOption("child.keepAlive", true);
 
    channel = new TcpChannel(bootstrap.bind(address));
    System.out.println("  [OK]");
    
    System.out.println("* TcpServer listening on:");
    System.out.println("  [ "+ address.getAddress().getHostAddress()
        + " : "+ address.getPort() + " ]");
    
    return this;
  }
  
  
  /**
   * Pára o servidor TCP.
   * @return Esta instância modificada de TcpServer.
   */
  public TcpServer stop() {
    channel.close();
    return this;
  }
  
}
