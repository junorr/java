/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.keepout.client2;

import io.undertow.connector.ByteBufferPool;
import io.undertow.server.DefaultByteBufferPool;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import net.keepout.config.Config;


/**
 *
 * @author juno
 */
public class ClientMain2 {
  
  public static void main(String[] args) throws Exception {
    ExecutorService exec = Executors.newCachedThreadPool();
    ByteBufferPool pool = createByteBufferPool();
    Config config = Config.loadClasspath("config.yml");
    //ProxyChannel proxy = new ProxyChannel(exec, pool, config.getClient());
    HttpProxyChannel proxy = new HttpProxyChannel(config, exec, pool);
    proxy.run();
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
