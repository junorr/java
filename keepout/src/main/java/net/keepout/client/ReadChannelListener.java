/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.keepout.client;

import io.undertow.connector.ByteBufferPool;
import io.undertow.connector.PooledByteBuffer;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Objects;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import org.jboss.logging.Logger;
import org.xnio.ChannelListener;
import org.xnio.Pool;
import org.xnio.Pooled;
import org.xnio.StreamConnection;
import org.xnio.channels.StreamSinkChannel;
import org.xnio.channels.StreamSourceChannel;


/**
 *
 * @author juno
 */
public class ReadChannelListener implements ChannelListener<StreamSourceChannel> {
  
  private final StreamConnection connection;
  
  private final ByteBufferPool pool;
  
  private final BlockingDeque<PooledByteBuffer> writes;
  
  public ReadChannelListener(StreamConnection con, ByteBufferPool pool) {
    this.connection = Objects.requireNonNull(con);
    this.pool = Objects.requireNonNull(pool);
    this.writes = new LinkedBlockingDeque<>();
    con.getSinkChannel().setWriteListener(new WriteChannelListener());
    Logger.getLogger(getClass()).infof(" created ");
  }
  
  @Override
  public void handleEvent(StreamSourceChannel sch) {
    //Logger.getLogger(getClass()).infof(" handle channel: %s", sch);
    try {
      int read;
      PooledByteBuffer buf = pool.allocate();
      while((read = sch.read(buf.getBuffer())) > 0) {
        Logger.getLogger(getClass()).infof(" StreamSourceChannel.read: %d", read);
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
    
    public WriteChannelListener() {
      this.channel = null;
      Logger.getLogger(getClass()).infof(" created ");
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
        write(buf);
      }
      channel.suspendWrites();
    }
    
    private void write(PooledByteBuffer buf) {
      try (buf) {
        int length = buf.getBuffer().remaining();
        int write = 0;
        while((write += channel.write(buf.getBuffer())) < length);
        //Logger.getLogger(getClass()).infof(" StreamSourceChannel.write: %d", write);
      }
      catch(IOException ex) {
        Unchecked.unchecked(ex);
      }
    }
    
  }
  
}
