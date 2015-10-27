/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.streams.test;

import java.io.IOException;
import us.pserver.streams.SequenceInputStream;


/**
 *
 * @author juno
 */
public class TestSequenceInputStream {
  
  
  public static void main(String[] args) throws IOException {
    SequenceInputStream in = new SequenceInputStream(100);
    int read = 0;
    System.out.println("* Reading...");
    while(true) {
      read = in.read();
      if(read == -1) break;
      System.out.println(".: "+ read);
    }
    System.out.println("* Done!");
  }
  
}
