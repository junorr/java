/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.streams;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import us.pserver.valid.Valid;


/**
 *
 * @author juno
 */
public class SizedInputStream extends FilterInputStream {
  
  private final long size;
  
  private long count;
  
  
  public SizedInputStream(InputStream in, long sizeLimit) {
    super(Valid.off(in).forNull()
        .getOrFail(InputStream.class)
    );
    this.size = Valid.off(sizeLimit)
        .forLesserThan(1).getOrFail();
    this.count = 0;
  }
  
  
  public long getCount() {
    return count;
  }
  
  
  public long getSizeLimit() {
    return size;
  }
  
  
  public SizedInputStream restart() {
    count = 0;
    return this;
  }
  
  
  @Override
  public int read(byte[] bs, int off, int len) throws IOException {
    Valid.off(bs).forEmpty().fail();
    Valid.off(off).forLesserThan(0).fail();
    Valid.off(len).forNotBetween(
        1, bs.length-off
    ).fail();
    int nlen = (int) Math.min(len, size-count);
    if(nlen < 1) return -1;
    int r = super.read(bs, off, nlen);
    count += r;
    return r;
  }
  
  
  @Override
  public int read(byte[] bs) throws IOException {
    return this.read(Valid.off(bs).forEmpty()
        .getOrFail(), 0, bs.length
    );
  }
  
  
  @Override
  public int read() throws IOException {
    if(size-count < 1) return -1;
    count++;
    return super.read();
  }
  
}
