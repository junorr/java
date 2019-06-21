/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.keepout.client2;

import io.undertow.connector.ByteBufferPool;
import io.undertow.connector.PooledByteBuffer;
import java.io.IOException;
import java.nio.channels.AsynchronousCloseException;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.Objects;
import static net.keepout.Unchecked.call;
import static net.keepout.Unchecked.unchecked;


/**
 *
 * @author juno
 */
public class HttpRequestFlow implements Runnable {
  
  private final String clientID;
  
  private final HttpProxyRequest request;
  
  private final ByteBufferPool pool;
  
  private final ReadableByteChannel clientin;
  
  private final WritableByteChannel clientout;
  
  public HttpRequestFlow(String clientID, HttpProxyRequest req, ByteBufferPool pool, ReadableByteChannel clientin, WritableByteChannel clientout) {
    this.clientID = Objects.requireNonNull(clientID);
    this.request = Objects.requireNonNull(req);
    this.pool = Objects.requireNonNull(pool);
    this.clientin = Objects.requireNonNull(clientin);
    this.clientout = Objects.requireNonNull(clientout);
  }
  
  @Override
  public void run() {
    try (request; clientin; clientout; PooledByteBuffer pb = pool.allocate()) {
      int read;
      while(clientin.isOpen() && (read = clientin.read(pb.getBuffer())) != -1) {
        if(!clientout.isOpen()) break;
        if(read > 0) {
          pb.getBuffer().flip();
          if(request.execute(clientID, pb.getBuffer(), clientout)) break;
        }
      }
    }
    catch(AsynchronousCloseException ax) {}
    catch(IOException ex) {
      throw unchecked(ex);
    }
    finally {
      call(()->request.executeDelete(clientID));
    }
  }
  
}
