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

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;


/**
 * Proxy Handler de conexão, redireciona as
 * chamadas à <code>ConnectionHandler</code>
 * recebido no construtor.
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 2012-07-27
 */
public class GeneralTcpHandler extends SimpleChannelHandler {

  private ConnectionHandler handler;

  private DynamicBuffer receiveBuffer;

  private FrameControl fcontrol;


  /**
   * Construtor padrão, recebe o handler de conexão
   * ao qual serão redirecionados as chamadas de estado.
   * @param hand handler de conexão.
   */
  public GeneralTcpHandler(ConnectionHandler hand) {
    if(hand == null)
      throw new IllegalArgumentException(
          "Invalid ServerHandler: "+ hand);
    handler = hand;
    receiveBuffer = new DynamicBuffer();
    fcontrol = new FrameControl();
  }


  /**
   * Retorna o <code>FrameControl</code>
   * @see com.jpower.nnet.FrameControl
   */
  public FrameControl getFrameControl() {
    return fcontrol;
  }


  /**
   * Define o <code>FrameControl</code>
   * @return Esta instância modificada de <code>GeneralTcpHandler</code>.
   * @see com.jpower.nnet.FrameControl
   */
  public GeneralTcpHandler setFcontrol(FrameControl fcontrol) {
    if(fcontrol != null)
      this.fcontrol = fcontrol;
    return this;
  }


  /**
   * Método invocado no recebimento de uma mensagem do canal
   * de comunicação.
   * {@inheritDoc }
   * @param ctx {@inheritDoc }
   * @param e {@inheritDoc }
   */
  @Override
  public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
    receiveBuffer.write((ChannelBuffer) e.getMessage());
    TcpChannel channel = new TcpChannel(e.getChannel())
        .setFrameControl(fcontrol);

    //System.out.println("|"+receiveBuffer.readString()+"|");
    System.out.print("\n * GeneralTcpHandler.messageReceived = "
        + receiveBuffer.size()+ " bytes / ");

    if(fcontrol.isEnabledOnReceive()) {
      byte[] last = new byte[7];
      receiveBuffer.read(receiveBuffer.size() -7, last);
      boolean fc = FrameControl.matchFrame(last);

      if(fc) {
        //System.out.println("fc_ok");
        receiveBuffer.remove(receiveBuffer.size() -7, 7);
        handler.received(receiveBuffer, channel);
        receiveBuffer = new DynamicBuffer();
      } 

    } else {
      handler.received(receiveBuffer, channel);
      receiveBuffer = new DynamicBuffer();
    }
  }


  /**
   * {@inheritDoc }
   * @param ctx {@inheritDoc }
   * @param e {@inheritDoc }
   */
  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
    Throwable th = e.getCause();
    //System.out.println("# message  : "+ th.getMessage());
    //System.out.println("# localized: "+ th.getLocalizedMessage());
    //th.printStackTrace();
    System.out.println("\n");
    Channel ch = e.getChannel();
    handler.error(th, new TcpChannel(ch)
        .setFrameControl(fcontrol));
  }


  /**
   * {@inheritDoc }
   * @param ctx {@inheritDoc }
   * @param e {@inheritDoc }
   */
  @Override
  public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
    handler.connected(new TcpChannel(e.getChannel())
        .setFrameControl(fcontrol));
  }


  /**
   * {@inheritDoc }
   * @param ctx {@inheritDoc }
   * @param e {@inheritDoc }
   */
  @Override
  public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
    handler.disconnected(new TcpChannel(e.getChannel())
        .setFrameControl(fcontrol));
  }


  /**
   * {@inheritDoc }
   * @param ctx {@inheritDoc }
   * @param e {@inheritDoc }
   */
  @Override
  public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) {
    handler.closed(new TcpChannel(e.getChannel())
        .setFrameControl(fcontrol));
  }

}
