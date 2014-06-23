/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.cdr.crypt;

import java.nio.ByteBuffer;
import us.pserver.cdr.ByteBufferConverter;
import static us.pserver.cdr.Checker.nullbuffer;



/**
 *
 * @author juno
 */
public class CryptBufferCoder implements CryptCoder<ByteBuffer> {
  
  private CryptByteCoder coder;
  
  private ByteBufferConverter bcv;
  
  
  public CryptBufferCoder(CryptKey key) {
    if(key == null || key.getSpec() == null)
      throw new IllegalArgumentException(
          "Invalid CryptKey: "+ key);
    coder = new CryptByteCoder(key);
    bcv = new ByteBufferConverter();
  }
  
  
  @Override
  public CryptKey getKey() {
    return coder.getKey();
  }
  
  
  public CryptByteCoder getCoder() {
    return coder;
  }
  
  
  @Override
  public ByteBuffer apply(ByteBuffer buf, boolean encode) {
    nullbuffer(buf);
    byte[] bs = bcv.convert(buf);
    if(bs == null) return buf;
    bs = coder.apply(bs, encode);
    return bcv.reverse(bs);
  }
  
  
  @Override
  public ByteBuffer encode(ByteBuffer buf) {
    return this.apply(buf, true);
  }
  
  
  @Override
  public ByteBuffer decode(ByteBuffer buf) {
    return this.apply(buf, false);
  }
  
}
