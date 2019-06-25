/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.keepout.client2;

import net.keepout.ChannelFlow;
import java.util.concurrent.Executor;
import io.undertow.connector.ByteBufferPool;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketTimeoutException;
import java.net.StandardSocketOptions;
import java.nio.channels.Pipe;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import static net.keepout.Unchecked.unchecked;
import net.keepout.config.Config;
import org.apache.commons.codec.binary.Hex;
import org.jboss.logging.Logger;


/**
 *
 * @author juno
 */
public class HttpProxyChannel implements Runnable {
  
  private final Config config;
  
  private final Executor exec;
  
  private final ByteBufferPool pool;
  
  private volatile boolean run = true;


  public HttpProxyChannel(Config cfg, Executor exec, ByteBufferPool pool) {
    this.config = Objects.requireNonNull(cfg);
    this.exec = Objects.requireNonNull(exec);
    this.pool = Objects.requireNonNull(pool);
  }
  
  public void shutdown() {
    run = false;
  }
  
  private void accept(ServerSocketChannel server, Logger log) throws IOException {
    try {
      SocketChannel client = server.socket().accept().getChannel();
      log.infof("ACCEPT Channel: [%s]", client.getRemoteAddress());
      exec.execute(new AcceptingChannel(client));
    }
    catch(SocketTimeoutException ex) {}
  }
  
  @Override
  public void run() {
    try (ServerSocketChannel server = ServerSocketChannel.open()) {
      Logger log = Logger.getLogger(getClass());
      server.socket().setSoTimeout(5000);
      server.setOption(StandardSocketOptions.SO_REUSEADDR, true);
      server.bind(new InetSocketAddress(config.getClient().getBind().getAddress(), config.getClient().getBind().getPort()));
      log.infof("START Server: [%s]", config.getClient().getBind());
      while(run) {
        accept(server, log);
      }
    } 
    catch(IOException ex) {
      throw unchecked(ex);
    }
  }
  
  
  
  
  
  public class AcceptingChannel implements Runnable {
    
    private final SocketChannel client;
    

    public AcceptingChannel(SocketChannel client) {
      this.client = client;
    }
    
    @Override
    public void run() {
      try {
        String clientid = String.format("(%s)==>(%s)", client.getRemoteAddress(), config.getClient().getTarget());
        String hex_clid = Hex.encodeHexString(StandardCharsets.UTF_8.encode(clientid), false);
        String targetid = String.format("(%s)==>(%s)", config.getClient().getTarget(), client.getRemoteAddress());
        Pipe clientIn = Pipe.open();
        Pipe clientOut = Pipe.open();
        HttpProxyRequest request = new HttpProxyRequest(config.getClient().getTarget(), config.getTokens().get(0), pool);
        exec.execute(new ChannelFlow(clientid, client, clientIn.sink(), pool.allocate()));
        exec.execute(new HttpRequestFlow(hex_clid, request, exec, pool, clientIn.source(), clientOut.sink()));
        exec.execute(new ChannelFlow(targetid, clientOut.source(), client, pool.allocate()));
      }
      catch(IOException ex) {
        throw unchecked(ex);
      } 
    }
        
  }
  
}
