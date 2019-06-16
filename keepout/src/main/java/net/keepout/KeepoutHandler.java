/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.keepout;

import io.undertow.server.HttpHandler;
import io.undertow.util.AttachmentKey;
import java.nio.channels.SocketChannel;


/**
 *
 * @author juno
 */
public interface KeepoutHandler extends HttpHandler {
  
  public static final String TARGET = "x-target";
  
  public static final String CLIENT_ID = "x-client-id";
  
  public static final String AUTH_COOKIE = "keepout";
  
  public static final AttachmentKey<SocketChannel> ATTACHMENT_SOCKET = AttachmentKey.create(SocketChannel.class);
  
}
