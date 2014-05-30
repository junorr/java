/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jpower.pxy;

import com.jpower.net.ConnectionHandler;
import com.jpower.net.NioClient;
import com.jpower.net.http.RequestParser;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;


/**
 *
 * @author juno
 */
public class ClientHandler implements ConnectionHandler {
  
  private ServerHandler sh;
  
  private ByteBuffer buffer;
  
  
  public ClientHandler(ServerHandler sh, ByteBuffer buf) {
    if(sh == null) throw new 
        IllegalArgumentException("Invalid ServerHandler: "+ sh);
    if(buf == null) throw new 
        IllegalArgumentException("Invalid ByteBuffer: "+ buf);
    
    this.sh = sh;
    this.buffer = buf;
  }


  @Override
  public void received(ByteBuffer buffer) {
    byte[] bs = new byte[buffer.limit()];
    buffer.get(bs);
    String req = new String(bs);
    RequestParser rp = new RequestParser();
    rp.parse(req);
    LogProvider.getLogger().info("Received from Remote "+ bs.length+ " bytes:")
        .info(rp.getMethod())
        .info(rp.getAddress())
        .info(rp.getVersion())
        .info(rp.getHost())
        .info(rp.getUserAgent())
        .info("# # # # #\n-----------------------------------------\n" + req)
        .info("# # # # #\n-----------------------------------------")
        .info("Sending to Browser...");
    sh.addBuffer(buffer);
  }


  @Override
  public void connected(SocketChannel channel) {
    try {
      LogProvider.getLogger().info("ClientHandler: Connected to remote: "+ channel.getRemoteAddress().toString());
    } catch (IOException ex) {
      LogProvider.getLogger().warn(ex.toString());
    }
  }


  @Override
  public void error(Throwable th) {
    LogProvider.getLogger().fatal(th);
  }


  @Override
  public ByteBuffer sending() {
    ByteBuffer b = buffer;
    buffer = null;
    return b;
  }


  @Override
  public boolean isSending() {
    return buffer != null;
  }


  @Override
  public void sent(int bytes) {
    LogProvider.getLogger().info("Sent to remote "+ bytes+ " bytes from server");
  }


  @Override
  public void disconnected(SocketChannel channel) {
    LogProvider.getLogger().info("ClientHandler: Connection closed!");
  }
  
}
