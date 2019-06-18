/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.keepout.client;

import io.undertow.connector.ByteBufferPool;
import io.undertow.server.DefaultByteBufferPool;
import java.net.InetSocketAddress;
import net.keepout.config.Config;
import org.jboss.logging.Logger;
import org.xnio.OptionMap;
import org.xnio.Options;
import org.xnio.StreamConnection;
import org.xnio.Xnio;
import org.xnio.XnioWorker;
import org.xnio.channels.AcceptingChannel;


/**
 *
 * @author juno
 */
public class ClientMain {
  
  public static void main(String[] args) throws Exception {
    Xnio xnio = Xnio.getInstance();
    int ioThreads = Math.max(Runtime.getRuntime().availableProcessors(), 2);
    int workerThreads = ioThreads * 8;

    XnioWorker worker = xnio.createWorker(OptionMap.builder()
        .set(Options.WORKER_IO_THREADS, ioThreads)
        .set(Options.WORKER_TASK_CORE_THREADS, workerThreads)
        .set(Options.WORKER_TASK_MAX_THREADS, workerThreads)
        .set(Options.TCP_NODELAY, true)
        .getMap());

    OptionMap socketOptions = OptionMap.builder()
        .set(Options.WORKER_IO_THREADS, ioThreads)
        .set(Options.TCP_NODELAY, true)
        .set(Options.REUSE_ADDRESSES, true)
        .getMap();
    
    ByteBufferPool pool = createByteBufferPool();
    Config config = Config.loadClasspath("config.yml");
    AcceptingChannel<StreamConnection> server = worker.createStreamConnectionServer(
        new InetSocketAddress(config.getClient().getBind().getAddress(), config.getClient().getBind().getPort()), 
        new AcceptChannelListener(config, pool), 
        socketOptions
    );
    Logger.getLogger(ClientMain.class).infof("Client started: %s", config.getClient().getBind());
    server.resumeAccepts();
  }
  
  
  private static ByteBufferPool createByteBufferPool() {
    long maxMemory = Runtime.getRuntime().maxMemory();
    boolean directBuffers = false;
    int bufferSize = 0;
    //smaller than 64mb of ram we use 512b buffers
    if(maxMemory < 64 * 1024 * 1024) {
      //use 512b buffers
      directBuffers = false;
      bufferSize = 512;
    } 
    else if (maxMemory < 128 * 1024 * 1024) {
      //use 1k buffers
      directBuffers = true;
      bufferSize = 1024;
    } 
    else {
      //use 16k buffers for best performance
      //as 16k is generally the max amount of data that can be sent in a single write() call
      directBuffers = true;
      bufferSize = 1024 * 16;
    }
    return new DefaultByteBufferPool(directBuffers, bufferSize, -1, 4);
  }
  
}
