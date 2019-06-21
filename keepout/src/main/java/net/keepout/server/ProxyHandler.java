/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.keepout.server;

import io.undertow.connector.PooledByteBuffer;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import java.nio.channels.SocketChannel;
import net.keepout.KeepoutConstants;
import net.keepout.ChannelFlow;
import org.xnio.channels.StreamSourceChannel;


/**
 *
 * @author juno
 */
public class ProxyHandler implements HttpHandler {
  
  @Override
  public void handleRequest(HttpServerExchange hse) throws Exception {
    if(hse.isInIoThread()) {
      hse.dispatch(this);
      return;
    }
    SocketChannel target = hse.getAttachment(KeepoutConstants.ATTACHMENT_SOCKET);
    StreamSourceChannel source = hse.getRequestChannel();
    PooledByteBuffer bufin = hse.getConnection().getByteBufferPool().allocate();
    PooledByteBuffer bufout = hse.getConnection().getByteBufferPool().allocate();
    String clientid = String.format("(%s)==>(%s)", hse.getConnection().getPeerAddress(), target.getRemoteAddress());
    String targetid = String.format("(%s)==>(%s)", target.getRemoteAddress(), hse.getConnection().getPeerAddress());
    hse.getIoThread().getWorker().execute(new ChannelFlow(clientid, source, new UncloseableWriteChannel(target), bufin));
    hse.getIoThread().getWorker().execute(new ChannelFlow(targetid, target, new HttpResponseChannel(hse), bufout));
  }
  
}
