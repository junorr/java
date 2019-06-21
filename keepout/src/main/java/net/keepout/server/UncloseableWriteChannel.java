/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.keepout.server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;
import java.util.Objects;


/**
 *
 * @author juno
 */
public class UncloseableWriteChannel implements WritableByteChannel {
  
  private final WritableByteChannel channel;
  
  public UncloseableWriteChannel(WritableByteChannel channel) {
    this.channel = Objects.requireNonNull(channel);
  }
  
  @Override
  public int write(ByteBuffer src) throws IOException {
    return channel.write(src);
  }
  
  @Override
  public boolean isOpen() {
    return channel.isOpen();
  }
  
  @Override public void close() {}
  
}
