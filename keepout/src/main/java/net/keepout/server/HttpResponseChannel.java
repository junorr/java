/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.keepout.server;

import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.WritableByteChannel;
import java.util.Objects;
import net.keepout.KeepoutConstants;
import org.apache.commons.codec.binary.Base64OutputStream;


/**
 *
 * @author juno
 */
public class HttpResponseChannel implements WritableByteChannel {
  
  private final HttpServerExchange exchange;
  
  private final WritableByteChannel response;
  
  private volatile boolean closed;
  
  public HttpResponseChannel(HttpServerExchange hse) {
    this.exchange = Objects.requireNonNull(hse);
    this.exchange.getResponseHeaders().add(
        Headers.CONTENT_TYPE, KeepoutConstants.CONTENT_TYPE_TEXT_HTML
    );
    //this.response = exchange.getResponseChannel();
    this.response = Channels.newChannel(new Base64OutputStream(
        Channels.newOutputStream(exchange.getResponseChannel()), true)
    );
    this.closed = false;
  }

  @Override
  public int write(ByteBuffer src) throws IOException {
    try {
      return response.write(src);
    }
    catch(ClosedChannelException ex) {
      this.close();
      return 0;
    }
  }
  
  @Override
  public boolean isOpen() {
    return !closed;
  }
  
  @Override
  public void close() throws IOException {
    this.closed = true;
    try {
      this.response.close();
    }
    catch(ClosedChannelException ex) {}
    exchange.endExchange();
  }
  
}
