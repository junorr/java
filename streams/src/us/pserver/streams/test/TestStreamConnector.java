/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.streams.test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import us.pserver.streams.SequenceInputStream;
import us.pserver.streams.StreamConnector;


/**
 *
 * @author juno
 */
public class TestStreamConnector {
  
  
  public static void main(String[] args) throws IOException {
    SequenceInputStream sin = new SequenceInputStream(100);
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    StreamConnector stream = new StreamConnector(sin, bos);
    System.out.println("* stream.connect(): "+ stream.connect());
    byte[] bs = bos.toByteArray();
    System.out.println("* Tranfered:");
    for(byte b : bs) {
      System.out.println(".:"+ b);
    }
    System.out.println("-------------------");
    sin.restart();
    bos.reset();
    System.out.println("* stream.connect(66): "+ stream.connect(66));
    bs = bos.toByteArray();
    System.out.println("* Tranfered:");
    for(byte b : bs) {
      System.out.println(".:"+ b);
    }
    System.out.println("-------------------");
    stream.close();
  }
  
}
