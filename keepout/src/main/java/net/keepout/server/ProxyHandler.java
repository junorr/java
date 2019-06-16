/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.keepout.server;

import io.undertow.connector.PooledByteBuffer;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import org.xnio.channels.StreamSinkChannel;
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
    SocketChannel target = hse.getAttachment(ConnectionsHandler.ATTACHMENT_SOCKET);
    StreamSourceChannel source = hse.getRequestChannel();
    try (PooledByteBuffer pooled = hse.getConnection().getByteBufferPool().allocate()) {
      transferRequest(source, target, pooled.getBuffer());
      transferResponse(hse, target, pooled.getBuffer());
    }
    hse.endExchange();
  }
  
  
  private void transferRequest(StreamSourceChannel source, SocketChannel target, ByteBuffer buffer) throws Exception {
    int read = -1;
    //Logger lg = Logger.getLogger(getClass().getName());
    //lg.infof("reading from request...");
    while((read = source.read(buffer)) != -1) {
      //lg.infof("Bytes readed = %d", read);
      buffer.flip();
      int write = 0;
      //lg.infof("writing to target...");
      while((write += target.write(buffer)) < read);
      //lg.infof("Bytes writed = %d", write);
      buffer.clear();
    }
    source.shutdownReads();
  }
  
  
  private void transferResponse(HttpServerExchange hse, SocketChannel source, ByteBuffer buffer) throws Exception {
    int read = -1;
    StreamSinkChannel response = hse.getResponseChannel();
    //Logger lg = Logger.getLogger(getClass().getName());
    //lg.infof("reading from target...");
    while((read = source.read(buffer)) > 0) {
      //lg.infof("Bytes readed = %d", read);
      hse.setResponseContentLength(read);
      boolean stopread = buffer.hasRemaining();
      buffer.flip();
      int write = 0;
      //lg.infof("writing to response...");
      while((write += response.write(buffer)) < read);
      //lg.infof("Bytes writed = %d", write);
      buffer.clear();
      if(stopread) break;
    }
    response.flush();
    response.shutdownWrites();
  }
  
}
