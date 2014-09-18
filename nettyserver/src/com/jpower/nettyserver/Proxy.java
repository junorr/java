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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.ClientSocketChannelFactory;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

/**
 * 
 */
public class Proxy {
  
  public static final String CONFIG_FILE = "proxy.conf";
  
  public static final String DEFAULT_ADDRESS = "127.0.0.1";
  
  public static final String DEFAULT_REMOTE_ADDRESS = "127.0.0.1";
  
  public static final String DEFAULT_TARGET_ADDRESS = "";
  
  public static final int DEFAULT_PORT = 10001;
  
  public static final int DEFAULT_REMOTE_PORT = 20002;
  
  public static final int DEFAULT_TARGET_PORT = 0;
  
  
  public static final String 
      KEY_REMOTE_ADDRESS = "remote_address",
      KEY_ADDRESS = "address",
      KEY_REMOTE_PORT = "remote_port",
      KEY_PORT = "port",
      KEY_CIPHER_KEY = "cipher_key",
      KEY_ENCODED_KEY = "encoded_key",
      KEY_TARGET_ADDRESS = "target_address",
      KEY_TARGET_PORT = "target_port",
      KEY_PROXY_AUTH = "proxy_auth",
      KEY_SEND_SERVER = "send_server",
      CONFIG_COMMENTS = "Netty Proxy Config File";
  
  
  private static String configFile = CONFIG_FILE;
  
  
  private String address;
  
  private String remoteAddress;
  
  private String targetAddress;
  
  private int port;
  
  private int remotePort;
  
  private int targetPort;
  
  private String cipherKey;
  
  private String proxyAuth;
  
  private boolean encodedKey;
  
  private boolean sendServer;
  
  private ServerBootstrap server;
  
  
  public Proxy() {
    address = null;
    remoteAddress = null;
    targetAddress = "";
    encodedKey = true;
    port = 0;
    remotePort = 0;
    targetPort = 0;
    cipherKey = null;
    sendServer = true;
    server = null;
    proxyAuth = "";
  }
  
  
  private boolean readConfig() {
    Properties p = new Properties();
    File f = new File(configFile);
    
    try (FileInputStream fis = new FileInputStream(f)) {
      p.load(fis);
      
      port = Integer.parseInt(p.getProperty(KEY_PORT));
      remotePort = Integer.parseInt(p.getProperty(KEY_REMOTE_PORT));
      targetPort = Integer.parseInt(p.getProperty(KEY_TARGET_PORT));
      address = p.getProperty(KEY_ADDRESS);
      remoteAddress = p.getProperty(KEY_REMOTE_ADDRESS);
      targetAddress = p.getProperty(KEY_TARGET_ADDRESS);
      proxyAuth = p.getProperty(KEY_PROXY_AUTH);
      sendServer = Boolean.parseBoolean(
          p.getProperty(KEY_SEND_SERVER));
      encodedKey = Boolean.parseBoolean(
          p.getProperty(KEY_ENCODED_KEY));
      
      cipherKey = p.getProperty(KEY_CIPHER_KEY);
      if(encodedKey) {
        cipherKey = this.getCipherKey();
      }
      return true;
      
    } catch(IOException | NumberFormatException ex) {
      return false;
    }
  }
  
  
  private boolean writeConfig() {
    Properties p = new Properties();
    p.setProperty(KEY_ADDRESS, address);
    p.setProperty(KEY_PORT, String.valueOf(port));
    p.setProperty(KEY_REMOTE_ADDRESS, remoteAddress);
    p.setProperty(KEY_REMOTE_PORT, String.valueOf(remotePort));
    p.setProperty(KEY_TARGET_ADDRESS, targetAddress);
    p.setProperty(KEY_TARGET_PORT, String.valueOf(targetPort));
    p.setProperty(KEY_CIPHER_KEY, cipherKey);
    p.setProperty(KEY_ENCODED_KEY, String.valueOf(encodedKey));
    p.setProperty(KEY_SEND_SERVER, String.valueOf(sendServer));
    p.setProperty(KEY_PROXY_AUTH, proxyAuth);
    File f = new File(configFile);
    
    try (FileOutputStream fos = new FileOutputStream(f)) {
      p.store(fos, CONFIG_COMMENTS);
      return true;
      
    } catch(IOException ex) {
      return false;
    }
  }
  
  
  private void writeDefaults() {
    address = DEFAULT_ADDRESS;
    remoteAddress = DEFAULT_REMOTE_ADDRESS;
    targetAddress = DEFAULT_TARGET_ADDRESS;
    port = DEFAULT_PORT;
    remotePort = DEFAULT_REMOTE_PORT;
    targetPort = DEFAULT_TARGET_PORT;
    proxyAuth = "";
    cipherKey = "$X000XY111Y$";
    encodedKey = false;
    sendServer = true;
    this.writeConfig();
  }


  public String getAddress() {
    return address;
  }


  public void setAddress(String address) {
    this.address = address;
  }


  public String getProxyAuth() {
    return proxyAuth;
  }


  public void setProxyAuth(String proxyAuth) {
    this.proxyAuth = proxyAuth;
  }


  public String getCipherKey() {
    if(!encodedKey) return cipherKey;
    return Base64Coder.decodeString(cipherKey);
  }


  public void setCipherKey(String key) {
    encodedKey = true;
    this.cipherKey = Base64Coder.encodeString(key);
  }


  public int getPort() {
    return port;
  }


  public static String getConfigFile() {
    return configFile;
  }


  public static void setConfigFile(String configFile) {
    Proxy.configFile = configFile;
  }


  public String getTargetAddress() {
    return targetAddress;
  }


  public void setTargetAddress(String targetAddress) {
    this.targetAddress = targetAddress;
  }


  public int getTargetPort() {
    return targetPort;
  }


  public void setTargetPort(int targetPort) {
    this.targetPort = targetPort;
  }


  public void setPort(int port) {
    this.port = port;
  }


  public String getRemoteAddress() {
    return remoteAddress;
  }


  public void setRemoteAddress(String remoteAddress) {
    this.remoteAddress = remoteAddress;
  }


  public int getRemotePort() {
    return remotePort;
  }


  public void setRemotePort(int remotePort) {
    this.remotePort = remotePort;
  }
  
  
  private void init() {
    Executor executor = Executors.newCachedThreadPool();
      
    server = new ServerBootstrap(
        new NioServerSocketChannelFactory(executor, executor));
      
    ClientSocketChannelFactory cf =
        new NioClientSocketChannelFactory(executor, executor);
    
    ForwardConfig conf = new ForwardConfig()
        .setCipherKey(cipherKey)
        .setProxyAuth(proxyAuth)
        .setRemoteAddress(remoteAddress)
        .setRemotePort(remotePort)
        .setTargetAddress(targetAddress)
        .setTargetPort(targetPort)
        .setSendServer(sendServer);
    
    server.setPipelineFactory(
        new PipelineFactory(cf, conf));
  }
  
  
  public void preStart() {
    if(configFile == null || configFile.trim().isEmpty())
      configFile = CONFIG_FILE;
    
    if(address == null || port == 0
        || remoteAddress == null
        || remotePort == 0
        || cipherKey == null) {
      
      if(!this.readConfig())
        this.writeDefaults();
      
    } else {
      this.writeConfig();
    }
  }
  
  
  public void start() {
    this.preStart();
    this.init();
    server.bind(new InetSocketAddress(address, port));
  }
  
  
  public static String getHeaderMessage() {
    return "\n"
        + "       [ NETTY PROXY 2012 ]         \n"
        + " -----------------------------------\n"
        + "  Copyright (c) 2012 - Juno Roesler \n"
        + "         juno.rr@gmail.com          \n"
        + " -----------------------------------\n";
  }
  
  
  public static String getUsageMessage() {
    return " * Usage: proxy [ {--help | config_file} ]"
        + "\n";
  }
  
  
  public static void main(String[] args) {
    
    //args = new String[] {"--help"};
    
    System.out.println(getHeaderMessage());
    try {Thread.sleep(100);}catch(Exception e){}
    
    //Verifica argumentos de inicialização
    if(args.length > 0) {
      if(args.length > 1) {
        System.err.println(" # Wrong Number of Arguments");
        System.err.println(getUsageMessage());
        System.exit(1);
      } else if(args[0].equals("--help")) {
        System.out.println(getUsageMessage());
        System.exit(0);
      } else {
        configFile = args[0];
      }
    }
    
    Proxy proxy = new Proxy();
    /*
    proxy.setAddress("localhost");
    proxy.setPort(10001);
    proxy.setRemoteAddress("172.24.74.38");
    proxy.setRemotePort(6060);
    proxy.setCipherKey("2");
    */
    
    //Verifica se foi informado um arquivo de 
    //configuração e se o arquivo é válido.
    if(!configFile.equalsIgnoreCase(CONFIG_FILE)
        && !proxy.readConfig()) {
      System.err.println(" # Invalid Config File: "+ configFile+ "\n");
      System.exit(1);
    }
    
    System.out.print(" * Starting Server...");
    
    //Start server
    try { proxy.start(); }
    catch(Exception ex) { 
      System.err.println();
      System.err.println(" # Error initing server: \n");
      ex.printStackTrace();
      System.exit(1);
    }
    
    System.out.println("  [OK]\n");
    
    System.out.println(" * Listening on : [ " 
        + proxy.getAddress() + ":"
        + proxy.getPort() + " ]");
    
    System.out.println(" * Forwarding to: [ "
        + proxy.getRemoteAddress() + ":"
        + proxy.getRemotePort() + " ]");
    System.out.println();
  }
  
}
