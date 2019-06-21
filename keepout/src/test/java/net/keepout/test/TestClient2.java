/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.keepout.test;

import net.keepout.client2.ClientMain2;
import org.junit.jupiter.api.Test;



/**
 *
 * @author juno
 */
public class TestClient2 {
  
  @Test
  public void start_server() {
    try {
      ClientMain2.main(null);
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  
}
