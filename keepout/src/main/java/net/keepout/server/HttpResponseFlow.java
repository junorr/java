/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.keepout.server;

import net.keepout.*;
import io.undertow.connector.PooledByteBuffer;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousCloseException;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.Objects;
import java.util.function.Consumer;
import static net.keepout.Unchecked.unchecked;
import org.jboss.logging.Logger;


/**
 *
 * @author juno
 */
public class HttpResponseFlow implements Runnable {
  
  protected final String channelID;
  
  protected final ReadableByteChannel input;

  protected final WritableByteChannel output;

  protected final PooledByteBuffer buffer;
  
  protected final Consumer<String> onclose;

  public HttpResponseFlow(String channelID, ReadableByteChannel input, WritableByteChannel output, PooledByteBuffer buf, Consumer<String> onclose) {
    this.channelID = Objects.requireNonNull(channelID);
    this.input = Objects.requireNonNull(input);
    this.output = Objects.requireNonNull(output);
    this.buffer = Objects.requireNonNull(buf);
    this.onclose = onclose;
  }
  
  public HttpResponseFlow(String channelID, ReadableByteChannel input, WritableByteChannel output, PooledByteBuffer buf) {
    this(channelID, input, output, buf, null);
  }
  
  private ByteBuffer buffer() {
    return buffer.getBuffer();
  }

  @Override
  public void run() {
    Logger log = Logger.getLogger(getClass());
    log.infof("OPEN Flow %s...", channelID);
    long total = 0;
    try (output; buffer) {
      int read;
      while(input.isOpen() && (read = input.read(buffer())) > 0) {
        if(!output.isOpen()) break;
        total += read;
        buffer().flip();
        int write = 0;
        while((write += output.write(buffer())) < read);
        log.infof("WRITED Flow %s: %d", channelID, write);
        buffer().clear();
      }
    }
    catch(AsynchronousCloseException ax) {}
    catch(IOException ex) {
      throw unchecked(ex);
    }
    finally {
      log.infof("CLOSE Flow %s: %d bytes transferred", channelID, total);
      if(onclose != null) onclose.accept(channelID);
    }
  }
    
}
