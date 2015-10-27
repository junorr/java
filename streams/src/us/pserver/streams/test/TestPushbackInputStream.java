/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.streams.test;

import java.io.IOException;
import us.pserver.streams.PushbackInputStream;
import us.pserver.streams.SequenceInputStream;


/**
 *
 * @author juno
 */
public class TestPushbackInputStream {
  
  
  public static void main(String[] args) throws IOException {
    SequenceInputStream in = new SequenceInputStream(100);
    PushbackInputStream pin = new PushbackInputStream(in, 100);
    int read = 0;
    System.out.println("* Reading...");
    while(true) {
      read = pin.read();
      if(read == -1) break;
      System.out.println(".: "+ read);
    }
    System.out.println("* Done!");
    for(int i = 50; i < 100; i++) {
      pin.unread(i);
    }
    System.out.println("* Reading...");
    while(true) {
      read = pin.read();
      if(read == -1) break;
      System.out.println(".: "+ read);
    }
    System.out.println("* Done!");
  }
  
}
