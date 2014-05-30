/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.cdr;

import java.nio.ByteBuffer;



/**
 *
 * @author juno
 */
public class ByteBufferConverter implements Converter<ByteBuffer, byte[]> {


  @Override
  public byte[] convert(ByteBuffer a) {
    if(a == null || a.limit() == 0)
      return null;
    byte[] bs = new byte[a.limit()];
    a.get(bs);
    return bs;
  }


  @Override
  public ByteBuffer reverse(byte[] b) {
    if(b == null || b.length == 0)
      return null;
    ByteBuffer buf = ByteBuffer.allocateDirect(b.length);
    buf.put(b);
    buf.flip();
    return buf;
  }
  
}
