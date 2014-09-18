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
package com.jpower.stunnel;

import com.jpower.nnet.DynamicBuffer;
import com.jpower.nnet.http.HttpRequest;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.concurrent.Executors;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;


/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 25/07/2012
 */
public class NettyGETest extends SimpleChannelHandler {

  private ClientBootstrap client;


  public NettyGETest() {
    ChannelFactory fac = new NioClientSocketChannelFactory(
        Executors.newCachedThreadPool(),
        Executors.newCachedThreadPool());

    client = new ClientBootstrap(fac);

    client.setPipelineFactory(new ChannelPipelineFactory() {
      @Override
      public ChannelPipeline getPipeline() {
        return Channels.pipeline(NettyGETest.this);
      }
    });

    ChannelFuture fut = client.connect(
        new InetSocketAddress("172.24.74.38", 6060));
    
    fut.addListener(new ChannelFutureListener() {
      @Override
      public void operationComplete(ChannelFuture cf) throws Exception {
        System.out.println(" * connected");
        System.out.println(" * isDone = "+ cf.isDone());
        System.out.println(" * isSuccess = "+ cf.isSuccess());
        if(cf.getCause() != null)
          cf.getCause().printStackTrace();
      }
    });
  }


  @Override
  public void messageReceived(ChannelHandlerContext c, MessageEvent e) {
    System.out.println(" * messageReceived:");
    DynamicBuffer buf = new DynamicBuffer();
    buf.write((ChannelBuffer) e.getMessage());
    System.out.println(buf.readString());
    buf.printBytes(20);
  }


  @Override
  public void channelConnected(ChannelHandlerContext c, ChannelStateEvent e) {
    System.out.println(" * channelConnected");
    HttpRequest req = new HttpRequest()
        .setHost("www.google.com");
        //.setProxyAuth("f6036477:65465411");
    System.out.println(" * sending request:");
    String r = req.build();
    System.out.println(r);
    
    DynamicBuffer buf = new DynamicBuffer();
    buf.writeString(r);
    int eot = 0x0d0a;
    byte[] b = new byte[4];
    b[3] = (byte) (eot >> 8);
    b[2] = (byte) (0x00ff & eot);
    b[1] = (byte) (eot >> 8);
    b[0] = (byte) (0x00ff & eot);
    System.out.println("b[3] = "+ b[3]);
    System.out.println("b[2] = "+ b[2]);
    System.out.println("b[1] = "+ b[1]);
    System.out.println("b[0] = "+ b[0]);
    buf.write(b);
    
    Channel ch = e.getChannel();
    ChannelBuffer cb = ChannelBuffers.buffer(buf.size());
    buf.read(cb);
    
    ch.write(cb);
  }


  @Override
  public void channelClosed(ChannelHandlerContext c, ChannelStateEvent e) {
    System.out.println(" * channelClosed");
    e.getChannel().close();
    //System.exit(0);
  }
  
  
  public static void main(String[] args) {
    new NettyGETest();
  }
  
}
