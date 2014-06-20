/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.cdr;

import java.nio.ByteBuffer;
import java.util.Arrays;



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
  

  public ByteBuffer reverse(byte[] b, int offset, int length) {
    if(b == null || b.length == 0 || offset < 0 
        || length < 1 || offset + length > b.length)
      return null;
    b = Arrays.copyOfRange(b, offset, offset+length);
    ByteBuffer buf = ByteBuffer.allocateDirect(b.length);
    buf.put(b);
    buf.flip();
    return buf;
  }
  
}
