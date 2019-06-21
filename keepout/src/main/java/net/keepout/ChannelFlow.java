/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.keepout;

import io.undertow.connector.PooledByteBuffer;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousCloseException;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.Objects;
import static net.keepout.Unchecked.unchecked;
import org.jboss.logging.Logger;


/**
 *
 * @author juno
 */
public class ChannelFlow implements Runnable {
  
  protected final String channelID;
  
  protected final ReadableByteChannel input;

  protected final WritableByteChannel output;

  protected final PooledByteBuffer buffer;

  public ChannelFlow(String channelID, ReadableByteChannel input, WritableByteChannel output, PooledByteBuffer buf) {
    this.channelID = Objects.requireNonNull(channelID);
    this.input = Objects.requireNonNull(input);
    this.output = Objects.requireNonNull(output);
    this.buffer = Objects.requireNonNull(buf);
  }

  private ByteBuffer buffer() {
    return buffer.getBuffer();
  }

  @Override
  public void run() {
    Logger log = Logger.getLogger(getClass());
    log.infof("OPEN Flow %s...", channelID);
    long total = 0;
    try (input; output; buffer) {
      int read;
      while(input.isOpen() && (read = input.read(buffer())) != -1) {
        if(!output.isOpen()) break;
        if(read > 0) {
          total += read;
          buffer().flip();
          int write = 0;
          while((write += output.write(buffer())) < read);
          log.infof("WRITED Flow %s: %d", channelID, write);
          buffer().clear();
        }
      }
    }
    catch(AsynchronousCloseException ax) {}
    catch(IOException ex) {
      throw unchecked(ex);
    }
    finally {
      log.infof("CLOSE Flow %s: %d bytes transferred", channelID, total);
    }
  }
    
}
