/*
 * Copyright 2009 Red Hat, Inc.
 *
 * Red Hat licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.jpower.nettyserver;

import biz.source_code.base64Coder.Base64Coder;
import java.net.InetSocketAddress;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.socket.ClientSocketChannelFactory;

/**
 * 
 */
public class InboundHandler extends SimpleChannelUpstreamHandler {
  
  private final ClientSocketChannelFactory cf;
  
  private final String remoteHost;
  
  private final int remotePort;
  
  public static final String 
      POST = "POST ",
      GET = "GET ",
      HTTP = " HTTP/",
      ADDRESS_PREFIX = "http://",
      HOST = "Host: ",
      USER_AGENT = "User-Agent: ",
      CONNECT = "CONNECT ",
      COOKIE_ADDRESS = "AHRE=",
      COOKIE_HOST = "HHRE=",
      END_COOKIE = "101E0C101; ";
  
  
  private String clientAddress;
  
  private RequestParser parser;
  
  private StringBuilder request;
  
  private StringBuilder response;
  
  private DynamicBuffer inbuffer;
  
  private DynamicBuffer outbuffer;
  
  private ContentEncoder encoder;
  
  private ContentDecoder decoder;
  
  private ForwardConfig conf;
  
  
  // This lock guards against the race condition that overrides the
  // OP_READ flag incorrectly.
  // See the related discussion: http://markmail.org/message/x7jc6mqx6ripynqf
  final Object trafficLock = new Object();
  
  
  private volatile Channel outboundChannel;
  
  
  public InboundHandler(
      ClientSocketChannelFactory cf, ForwardConfig conf) {
    this.cf = cf;
    this.conf = conf;
    parser = new RequestParser();
    inbuffer = new DynamicBuffer();
    outbuffer = new DynamicBuffer();
    this.remoteHost = conf.getRemoteAddress();
    this.remotePort = conf.getRemotePort();
    this.encoder = new ContentEncoder(conf.getCipherKey());
    this.decoder = new ContentDecoder(conf.getCipherKey());
  }
  
  
  public StringBuilder getRequest() {
    return request;
  }
  
  
  public String getAddress() {
    return parser.getAddress();
  }
  
  
  public String getHost() {
    return parser.getHost();
  }
  
  
  public String getMethod() {
    return parser.getMethod();
  }
  
  
  public String getClientAddress() {
    return clientAddress;
  }
  
  
  public byte[] getInBuffer() {
    return inbuffer.toArray();
  }
  
  
  public byte[] getOutBuffer() {
    return outbuffer.toArray();
  }
  
  
  public String getUserAgent() {
    return parser.getUserAgent();
  }
  
  
  private void parseRequest() {
    parser.clear().parse(request.toString());
  } 
  
  
  private void printInfo(boolean newclient) {
    if(newclient) {
      System.out.println("\n * New Client Connected");
      System.out.println(" * [ "+ this.getClientAddress()+ "]");
    }
    System.out.println(" * Method    : ["+ this.getMethod()+ " "+ this.getAddress()+ "]");
    System.out.println(" * Host      : ["+ this.getHost()+ "]");
    System.out.println(" * User Agent: ["+ this.getUserAgent()+ "]");
    System.out.println(" * Printing Content...");
    System.out.println("--------------------------");
    System.out.println(request.toString());
    System.out.println("--------------------------");
    System.out.println(" * Content Print Finished!");
  }
  
  
  private ChannelBuffer encodeRequest(DynamicBuffer buf) {
    if(buf == null || buf.isEmpty()) return null;
    
    HttpRequest hr = new HttpRequest();
    hr.setHost(conf.getTargetAddress());
    hr.setAddress(hr.getAddress()
        .concat(":").concat(
        String.valueOf(conf.getTargetPort())));
    
    if(conf.getProxyAuth() != null 
        && !conf.getProxyAuth().isEmpty())
      hr.setProxyAuth(conf.getProxyAuth());
    
    hr.setContent(encoder.encode(buf.toArray()));
    ChannelBuffer ch = ChannelBuffers.dynamicBuffer();
    ch.writeBytes(hr.build().getBytes());
    return ch;
  }
  
  
  private ChannelBuffer decodeRequest(DynamicBuffer buf) {
    if(buf == null || buf.isEmpty()) return null;
    
    request = new StringBuilder(new String(inbuffer.toArray()));
    int start = request.indexOf(HttpRequest.CONTENT_START);
    int end = request.indexOf(HttpRequest.CONTENT_END);
    
    start += HttpRequest.CONTENT_START.length();
    String content = request.substring(start, end);
    ChannelBuffer ch = ChannelBuffers.dynamicBuffer();
    try {
      ch.writeBytes(decoder.decode(Base64Coder.decode(content)));
    } catch(Exception ex) {}
    return ch;
  }
  
  
  private ChannelBuffer encodeResponse(DynamicBuffer buf) {
    if(buf == null || buf.isEmpty()) return null;
    
    HttpResponse hs = new HttpResponse();
    
    hs.setContent(encoder.encode(buf.toArray()));
    ChannelBuffer ch = ChannelBuffers.dynamicBuffer();
    ch.writeBytes(hs.build().getBytes());
    return ch;
  }
  
  
  private ChannelBuffer decodeResponse(DynamicBuffer buf) {
    if(buf == null || buf.isEmpty()) return null;
    
    response = new StringBuilder(new String(outbuffer.toArray()));
    int start = response.indexOf(HttpResponse.CONTENT_START);
    int end = response.indexOf(HttpResponse.CONTENT_END);
    
    start += HttpResponse.CONTENT_START.length();
    String content = response.substring(start, end);
    ChannelBuffer ch = ChannelBuffers.dynamicBuffer();
    try {
      ch.writeBytes(decoder.decode(Base64Coder.decode(content)));
    } catch(Exception ex) {}
    return ch;
  }
  
  
  private boolean isIncompleteRequest() {
    if(request.indexOf(HttpRequest.CONTENT_END) < 0) {
      System.out.println("__#_ INCOMPLETE REQUEST. RETURN...");
      return true;
    }
    
    return false;
  }
  
  
  private boolean isIncompleteResponse() {
    if(response.indexOf(HttpResponse.CONTENT_END) < 0) {
      System.out.println("__#_ INCOMPLETE REQUEST. RETURN...");
      return true;
    }
    
    return false;
  }
  
  
  private ChannelBuffer readInput(ChannelBuffer ch) {
    ChannelBuffer buf = ChannelBuffers.dynamicBuffer();
        
    request = new StringBuilder();
    while(ch.readable()) {
      byte b = ch.readByte();
      buf.writeByte(b);
      inbuffer.write(b);
      request.append((char) b);
    }
    return buf;
  }
  
  
  private ChannelBuffer readOutput(ChannelBuffer ch) {
    ChannelBuffer buf = ChannelBuffers.dynamicBuffer();
        
    response = new StringBuilder();
    while(ch.readable()) {
      byte b = ch.readByte();
      buf.writeByte(b);
      outbuffer.write(b);
      response.append((char) b);
    }
    return buf;
  }
  
  
  @Override
  public void channelOpen(ChannelHandlerContext ctx, ChannelStateEvent e)
        throws Exception {
    // Suspend incoming traffic until connected to the remote host.
    final Channel inboundChannel = e.getChannel();
    inboundChannel.setReadable(false);

    // Start the connection attempt.
    ClientBootstrap cb = new ClientBootstrap(cf);
    cb.getPipeline().addLast("handler", 
        new OutboundHandler(e.getChannel()));
    
    ChannelFuture f = cb.connect(
        new InetSocketAddress(remoteHost, remotePort));
    
    outboundChannel = f.getChannel();
    f.addListener(new ChannelFutureListener() {
        public void operationComplete(ChannelFuture future) throws Exception {
          if(future.isSuccess()) {
            // Connection attempt succeeded:
            // Begin to accept incoming traffic.
            inboundChannel.setReadable(true);
          } else {
            // Close the connection if the connection attempt has failed.
            inboundChannel.close();
          }
        }
    });
  }
  
  
  @Override
  public void messageReceived(ChannelHandlerContext ctx, final MessageEvent e)
      throws Exception {
      
    ChannelBuffer buf = (ChannelBuffer) e.getMessage();
    
    synchronized (trafficLock) {
          
      InetSocketAddress sa = (InetSocketAddress) e.getRemoteAddress();
      clientAddress = sa.getHostString();
      
      ChannelBuffer buf2 = this.readInput(buf);
      this.parseRequest();
      this.printInfo(true);
        
      if(conf.isSendServer()) {
        buf2 = this.encodeRequest(inbuffer);
        inbuffer.clear();
        
      } else {
        
        if(this.isIncompleteRequest())
          return;
        
        System.out.println("__#_ ALL CONTENT RECEIVED");
        buf2 = this.decodeRequest(inbuffer);
        buf2 = this.readInput(buf2);
        this.parseRequest();
        this.printInfo(false);
        inbuffer.clear();
      }
      
      outboundChannel.write(buf2);
        
      //outboundChannel.write(buf2);
      // If outboundChannel is saturated, do not read until notified in
      // OutboundHandler.channelInterestChanged().
      if(!outboundChannel.isWritable()) {
        e.getChannel().setReadable(false);
      }
    }
  }

    @Override
    public void channelInterestChanged(ChannelHandlerContext ctx,
            ChannelStateEvent e) throws Exception {
        // If inboundChannel is not saturated anymore, continue accepting
        // the incoming traffic from the outboundChannel.
        synchronized (trafficLock) {
            if (e.getChannel().isWritable()) {
                outboundChannel.setReadable(true);
            }
        }
    }
    
    
    @Override
    public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e)
            throws Exception {
        if (outboundChannel != null) {
            closeOnFlush(outboundChannel);
        }
    }
    
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
            throws Exception {
        e.getCause().printStackTrace();
        closeOnFlush(e.getChannel());
    }
    
    
    private class OutboundHandler extends SimpleChannelUpstreamHandler {
      
        private final Channel inboundChannel;
        
        OutboundHandler(Channel inboundChannel) {
            this.inboundChannel = inboundChannel;
        }

        @Override
        public void messageReceived(ChannelHandlerContext ctx, 
        final MessageEvent e) throws Exception {
          
          ChannelBuffer msg = (ChannelBuffer) e.getMessage();
          
          
          /*                  SENDER
           * [WS] <- [NT2] <- [NT1] <- [BW]
           *    |-->     |-->     |-->
           *          ENC      DEC
           *          RESP     RESP
           */
          
          
          synchronized (trafficLock) {
            
            msg = readOutput(msg);
            System.out.println("__*_ RESPONSE RECEIVED:");
            
            if(!conf.isSendServer()) {
              msg = encodeResponse(outbuffer);
              outbuffer.clear();
              
            } else {
              
              if(isIncompleteResponse())
                return;
        
              System.out.println("__*_ ALL CONTENT RECEIVED");
              msg = decodeResponse(outbuffer);
              msg = readOutput(msg);
              System.out.println(response.toString());
              outbuffer.clear();
            }
            
            //System.out.println(" *** CONTENT RECIEVED ***");
            //System.out.println(received);
            //System.out.println(" *** *** ***  *** *** ***");
            
            inboundChannel.write(msg);
            // If inboundChannel is saturated, do not read until notified in
            // InboundHandler.channelInterestChanged().
            if(!inboundChannel.isWritable()) {
               e.getChannel().setReadable(false);
            }
          }
        }
        
        
        @Override
        public void channelInterestChanged(ChannelHandlerContext ctx,
                ChannelStateEvent e) throws Exception {
            // If outboundChannel is not saturated anymore, continue accepting
            // the incoming traffic from the inboundChannel.
            synchronized (trafficLock) {
                if (e.getChannel().isWritable()) {
                    inboundChannel.setReadable(true);
                }
            }
        }
        
        
        @Override
        public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e)
                throws Exception {
            closeOnFlush(inboundChannel);
        }
        
        
        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
                throws Exception {
            e.getCause().printStackTrace();
            closeOnFlush(e.getChannel());
        }
    }
    
    
    /**
     * Closes the specified channel after all queued write requests are flushed.
     */
    static void closeOnFlush(Channel ch) {
        if (ch.isConnected()) {
            ch.write(ChannelBuffers.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
        }
    }
}
