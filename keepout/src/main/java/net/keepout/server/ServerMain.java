/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.keepout.server;

import io.undertow.Undertow;
import net.keepout.config.Config;


/**
 *
 * @author juno
 */
public class ServerMain {
  
  
  public static void main(String[] args) throws Exception {
    Config config = Config.loadClasspath("config.yml");
    ConnectionsHandler cons = new ConnectionsHandler(config, new ProxyHandler());
    Undertow server = Undertow.builder().addHttpListener(
        config.getServer().getBind().getPort(), 
        config.getServer().getBind().getAddress(), 
        new AuthHandler(config, cons)
    ).build();
    server.start();
  }
  
}
