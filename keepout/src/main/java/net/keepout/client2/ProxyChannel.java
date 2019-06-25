/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.keepout.client2;

import java.util.concurrent.Executor;
import io.undertow.connector.ByteBufferPool;
import io.undertow.connector.PooledByteBuffer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketTimeoutException;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousCloseException;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Objects;
import static net.keepout.Unchecked.call;
import static net.keepout.Unchecked.unchecked;
import net.keepout.config.NetService;
import org.jboss.logging.Logger;


/**
 *
 * @author juno
 */
public class ProxyChannel implements Runnable {
  
  private final Executor exec;
  
  private final ByteBufferPool pool;
  
  private final NetService net;
  
  private volatile boolean run = true;


  public ProxyChannel(Executor exec, ByteBufferPool pool, NetService net) {
    this.exec = Objects.requireNonNull(exec);
    this.pool = Objects.requireNonNull(pool);
    this.net = Objects.requireNonNull(net);
  }
  
  public void shutdown() {
    run = false;
  }
  
  private void accept(ServerSocketChannel server, Logger log) throws IOException {
    try {
      SocketChannel client = server.socket().accept().getChannel();
      //log.infof("ACCEPT Channel: [%s]", client.getRemoteAddress());
      exec.execute(new AcceptChannelHandler(client));
    }
    catch(SocketTimeoutException ex) {}
  }
  
  @Override
  public void run() {
    try (ServerSocketChannel server = ServerSocketChannel.open()) {
      Logger log = Logger.getLogger(getClass());
      server.socket().setSoTimeout(5000);
      server.setOption(StandardSocketOptions.SO_REUSEADDR, true);
      server.bind(new InetSocketAddress(net.getBind().getAddress(), net.getBind().getPort()));
      log.infof("START Server: [%s]", net.getBind());
      while(run) {
        accept(server, log);
      }
    } 
    catch(IOException ex) {
      throw unchecked(ex);
    }
  }
  
  
  
  
  
  public class AcceptChannelHandler implements Runnable {
    
    private final SocketChannel client;


    public AcceptChannelHandler(SocketChannel client) {
      this.client = client;
    }
    
    @Override
    public void run() {
      InetSocketAddress addr = new InetSocketAddress(net.getTarget().getAddress(), net.getTarget().getPort());
      try {
        SocketChannel target = SocketChannel.open();
        target.setOption(StandardSocketOptions.SO_REUSEADDR, true);
        target.setOption(StandardSocketOptions.TCP_NODELAY, true);
        target.connect(addr);
        exec.execute(new ChannelFlow(client, target, pool.allocate()));
        exec.execute(new ChannelFlow(target, client, pool.allocate()));
      }
      catch(IOException ex) {
        throw unchecked(ex);
      } 
    }
        
  }
  
  
  
  
  
  
  
  public static class ChannelFlow implements Runnable {
    
    private final SocketChannel input;
    
    private final SocketChannel output;
    
    private final PooledByteBuffer buffer;
    
    public ChannelFlow(SocketChannel input, SocketChannel output, PooledByteBuffer buf) {
      this.input = input;
      this.output = output;
      this.buffer = buf;
    }
    
    private ByteBuffer buffer() {
      return buffer.getBuffer();
    }
    
    private boolean isValid(SocketChannel ch) {
      //Logger.getLogger(getClass()).infof("VALID Channel (%s): { isOpen=%s, isConnected=%s }", call(()->ch.getRemoteAddress()), ch.isOpen(), ch.isConnected());
      return ch.isOpen() && ch.isConnected();
    }
    
    @Override
    public void run() {
      String inputaddr = null;
      String outputaddr = null;
      Logger log = Logger.getLogger(getClass());
      long total = 0;
      try (input; output; buffer) {
        inputaddr = input.getRemoteAddress().toString();
        outputaddr = output.getRemoteAddress().toString();
        log.infof("OPEN Flow (%s)==>(%s)...", inputaddr, outputaddr);
        int read;
        while(isValid(input) && (read = input.read(buffer())) != -1) {
          if(!isValid(output)) break;
          log.infof("READ Flow (%s)==>(%s): %d bytes", inputaddr, outputaddr, read);
          if(read > 0) {
            total += read;
            buffer().flip();
            int write = 0;
            while((write += output.write(buffer())) < read);
            buffer().clear();
          }
        }
      }
      catch(AsynchronousCloseException ax) {}
      catch(IOException ex) {
        throw unchecked(ex);
      }
      finally {
      log.infof(
            "CLOSE Flow (%s)==>(%s): %d bytes transferred", 
            inputaddr, outputaddr, total
        );
      }
    }
    
  }
  
}
