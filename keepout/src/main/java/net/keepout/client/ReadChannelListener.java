/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.keepout.client;

import net.keepout.Unchecked;
import io.undertow.connector.ByteBufferPool;
import io.undertow.connector.PooledByteBuffer;
import io.undertow.util.Headers;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Objects;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import net.keepout.KeepoutConstants;
import net.keepout.config.Config;
import net.powercoder.cdr.hex.HexStringCoder;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.jboss.logging.Logger;
import org.xnio.ChannelListener;
import org.xnio.StreamConnection;
import org.xnio.channels.StreamSinkChannel;
import org.xnio.channels.StreamSourceChannel;


/**
 *
 * @author juno
 */
public class ReadChannelListener implements ChannelListener<StreamSourceChannel> {
  
  private final Config config;
  
  private final StreamConnection connection;
  
  private final ByteBufferPool pool;
  
  private final BlockingDeque<PooledByteBuffer> writes;
  
  public ReadChannelListener(Config cfg, StreamConnection con, ByteBufferPool pool) {
    this.config = Objects.requireNonNull(cfg);
    this.connection = Objects.requireNonNull(con);
    this.pool = Objects.requireNonNull(pool);
    this.writes = new LinkedBlockingDeque<>();
    con.getSinkChannel().setWriteListener(new WriteChannelListener());
  }
  
  @Override
  public void handleEvent(StreamSourceChannel sch) {
    //Logger.getLogger(getClass()).infof(" handle channel: %s", sch);
    try {
      int read;
      PooledByteBuffer buf = pool.allocate();
      while((read = sch.read(buf.getBuffer())) > 0) {
        //Logger.getLogger(getClass()).infof(" StreamSourceChannel.read: %d", read);
        if(!buf.getBuffer().hasRemaining()) {
          buf.getBuffer().flip();
          writes.putLast(buf);
          buf = pool.allocate();
          connection.getSinkChannel().resumeWrites();
        }
      }
      buf.getBuffer().flip();
      if(buf.getBuffer().hasRemaining()) {
        writes.putLast(buf);
        connection.getSinkChannel().resumeWrites();
      }
    }
    catch(Exception ex) {
      Unchecked.unchecked(ex);
    }
  }
  
  
  
  class WriteChannelListener implements ChannelListener<StreamSinkChannel>, Runnable {
    
    private volatile StreamSinkChannel channel;
    
    private final HexStringCoder hex;
    
    public WriteChannelListener() {
      this.channel = null;
      this.hex = new HexStringCoder();
    }
    
    @Override
    public void handleEvent(StreamSinkChannel sch) {
      //Logger.getLogger(getClass()).infof(" handle channel: %s", sch);
      this.channel = sch;
      //is in IOThread
      if(connection.getIoThread() == Thread.currentThread()) {
        //Logger.getLogger(getClass()).infof(" IOThread >> WorkerThread: %s", Thread.currentThread());
        connection.getWorker().execute(this);
      }
      else {
        run();
      }
    }
    
    @Override
    public void run() {
      //Logger.getLogger(getClass()).infof(" WorkerThread: %s", Thread.currentThread());
      PooledByteBuffer buf;
      while((buf = Unchecked.call(()->writes.pollFirst())) != null) {
        //Logger.getLogger(getClass()).infof(" forwarding: %s", StandardCharsets.UTF_8.decode(buf.getBuffer().duplicate()));
        forward(buf);
      }
      
      channel.suspendWrites();
    }
    
    private void forward(PooledByteBuffer buf) {
      try (
          buf;
          CloseableHttpClient http = HttpClients.createDefault();
          CloseableHttpResponse res = http.execute(createPost(buf))
          ) {
        Logger.getLogger(getClass()).infof("Connection handled (%s): %s", connection.getPeerAddress(), res.getStatusLine());
        //System.out.println("------ HttpResponse Headers ------");
        //Arrays.asList(res.getAllHeaders()).forEach(h -> System.out.printf("[%s] : [%s]%n", h.getName(), h.getValue()));
        //System.out.println("------ -------------------- ------");
        writeBack(res.getEntity().getContent(), buf.getBuffer().clear());
      }
      catch(Exception ex) {
        Logger.getLogger(getClass()).errorf(ex, "Error handling connection: %s", connection.getPeerAddress());
      }
    }
    
    private HttpPost createPost(PooledByteBuffer buf) {
      String clientID = hex.encode(connection.getPeerAddress().toString());
      String uri = "http://" + config.getClient().getTarget().toString();
      //String uri = "https://webhook.site/318ea209-1d5d-4a3f-81eb-fcf9b17b133d";
      String authCookie = KeepoutConstants.AUTH_COOKIE + "=" + config.getTokens().get(0);
      InputStream forward = new InputStream() {
        @Override
        public int read() throws IOException {
          if(!buf.getBuffer().hasRemaining()) {
            return -1;
          }
          return buf.getBuffer().get();
        }
      };
      HttpPost post = new HttpPost(uri);
      post.addHeader(Headers.CONTENT_TYPE_STRING, KeepoutConstants.CONTENT_TYPE_OCTET_STREAM);
      post.addHeader(KeepoutConstants.HEADER_CLIENT_ID, clientID);
      post.addHeader(Headers.COOKIE_STRING, authCookie);
      //post.addHeader(Headers.CONTENT_LENGTH_STRING, String.valueOf(buf.getBuffer().remaining()));
      post.setEntity(EntityBuilder.create().setStream(forward).build());
      return post;
    }
    
    private void writeBack(InputStream input, ByteBuffer buf) {
      try (
          ReadableByteChannel rch = Channels.newChannel(input);
          ) {
        //Logger.getLogger(getClass()).info("CALLED!");
        //buffer.flip();
        int read;
        while((read = rch.read(buf)) != -1) {
          if(read > 0) {
            buf.flip();
            //Logger.getLogger(getClass()).infof("writing back: '%s'", StandardCharsets.UTF_8.decode(buf.duplicate()));
            int write = 0;
            while((write += channel.write(buf)) < read);
            //Logger.getLogger(getClass()).infof("Content forwarded and writed back to client: %d", write);
            buf.clear();
          }
        }
      }
      catch(IOException ex) {
        throw Unchecked.unchecked(ex);
      }
    }
  
  }
  
}
