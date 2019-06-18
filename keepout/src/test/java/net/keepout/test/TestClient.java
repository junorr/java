/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.keepout.test;

import net.keepout.client.ClientMain;
import org.junit.jupiter.api.Test;



/**
 *
 * @author juno
 */
public class TestClient {
  
  //@Test
  public void start_server() {
    try {
      ClientMain.main(null);
      Thread.sleep(15000);
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  
}
