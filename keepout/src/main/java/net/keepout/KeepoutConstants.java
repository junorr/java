/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.keepout;

import io.undertow.util.AttachmentKey;
import java.nio.channels.SocketChannel;


/**
 *
 * @author juno
 */
public abstract class KeepoutConstants {
  
  private KeepoutConstants() {}
  
  public static final String HEADER_TARGET = "x-target";
  
  public static final String HEADER_CLIENT_ID = "x-client-id";
  
  public static final String AUTH_COOKIE = "keepout";
  
  public static final String CONTENT_TYPE_OCTET_STREAM = "application/octet-stream";
  
  public static final AttachmentKey<SocketChannel> ATTACHMENT_SOCKET = AttachmentKey.create(SocketChannel.class);
  
}
