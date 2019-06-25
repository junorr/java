/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.keepout.test;

import net.keepout.server.ServerMain;
import org.junit.jupiter.api.Test;



/**
 *
 * @author juno
 */
public class TestServer {
  
  @Test
  public void start_server() {
    try {
      ServerMain.main(null);
      Thread.sleep(60000);
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  
}
