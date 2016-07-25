/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.streams.test;

import java.nio.ByteBuffer;


/**
 *
 * @author juno
 */
public class TestByteBuffer {
  
  
  public static void main(String[] args) {
    ByteBuffer buf = ByteBuffer.allocate(10);
    System.out.println(" * buf.capacity().: "+ buf.capacity());
    System.out.println(" * buf.limit()....: "+ buf.limit());
    System.out.println(" * buf.position().: "+ buf.position());
    System.out.println(" * buf.remaining(): "+ buf.remaining());
    buf.flip();
    System.out.println("=> buf.flip();");
    System.out.println(" * buf.capacity().: "+ buf.capacity());
    System.out.println(" * buf.limit()....: "+ buf.limit());
    System.out.println(" * buf.position().: "+ buf.position());
    System.out.println(" * buf.remaining(): "+ buf.remaining());
    buf.clear();
    buf.put((byte)1);
    System.out.println("=> buf.clear().put(1);");
    System.out.println(" * buf.capacity().: "+ buf.capacity());
    System.out.println(" * buf.limit()....: "+ buf.limit());
    System.out.println(" * buf.position().: "+ buf.position());
    System.out.println(" * buf.remaining(): "+ buf.remaining());
    buf.flip();
    System.out.println("=> buf.flip();");
    System.out.println(" * buf.capacity().: "+ buf.capacity());
    System.out.println(" * buf.limit()....: "+ buf.limit());
    System.out.println(" * buf.position().: "+ buf.position());
    System.out.println(" * buf.remaining(): "+ buf.remaining());
  }
  
}
