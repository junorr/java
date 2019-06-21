/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.keepout.client;

import net.keepout.Unchecked;
import io.undertow.connector.ByteBufferPool;
import java.util.Objects;
import net.keepout.config.Config;
import org.jboss.logging.Logger;
import org.xnio.ChannelListener;
import org.xnio.StreamConnection;
import org.xnio.channels.AcceptingChannel;
import org.xnio.channels.StreamSourceChannel;


/**
 *
 * @author juno
 */
public class AcceptChannelListener implements ChannelListener<AcceptingChannel<StreamConnection>> {
  
  private final ByteBufferPool pool;
  
  private final Config config;
  
  public AcceptChannelListener(Config cfg, ByteBufferPool pool) {
    this.pool = Objects.requireNonNull(pool);
    this.config = Objects.requireNonNull(cfg);
  }
  
  @Override
  public void handleEvent(AcceptingChannel<StreamConnection> channel) {
    StreamConnection con;
    while((con = Unchecked.call(()->channel.accept())) != null) {
      Logger.getLogger(getClass()).infof("Connection accepted: %s", con.getPeerAddress());
      StreamSourceChannel sch = con.getSourceChannel();
      sch.getReadSetter().set(new ReadChannelListener(config, con, pool));
      sch.resumeReads();
    }
  }
  
}
