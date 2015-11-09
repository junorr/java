/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.streams;

import java.io.FilterInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import us.pserver.valid.Valid;


/**
 *
 * @author juno
 */
public class SearchableInputStream extends FilterInputStream {
  
  public static final int BUFFER_SIZE = 8192;
  
  private PushbackInputStream pin;
  
  private ByteBuffer buffer;
  
  private byte[] search;
  
  private boolean found, eof, reading;
  
  
  public SearchableInputStream(PushbackInputStream pis, byte[] search) {
    this(pis, search, ByteBuffer.allocateDirect(BUFFER_SIZE));
  }
  
  
  public SearchableInputStream(PushbackInputStream pis, byte[] search, ByteBuffer buffer) {
    super(Valid.off(pis).forNull()
        .getOrFail(PushbackInputStream.class));
    pin = pis;
    this.search = Valid.off(search).forEmpty()
        .getOrFail();
    this.buffer = Valid.off(buffer).forNull()
        .getOrFail(ByteBuffer.class);
    found = eof = reading = false;
  }
  
  
  public ByteBuffer getBuffer() {
    return buffer;
  }
  
  
  public byte[] getSearchTerm() {
    return search;
  }
  
  
  public boolean isFound() {
    return found;
  }
  
  
  public boolean isEOF() {
    return eof;
  }
  
  
  public PushbackInputStream getInputStream() {
    return pin;
  }
  
  
  private int searchBuffer() {
    //System.out.println("SIN.searchBuffer()");
    setReading();
    ByteBuffer buf = buffer.duplicate();
    int count = 0;
    int index = -1;
    while(buf.hasRemaining()) {
      if(buf.get() == search[0]) {
        index = buf.position()-1;
        count++;
        for(int i = 1; i < search.length && buf.hasRemaining(); i++) {
          count += (buf.get() == search[i] ? 1 : 0);
        }
        if(count == search.length) {
          found = true;
          break;
        }
      }
      else {
        count = 0;
        index = -1;
      }
    }
    return index;
  }
  
  
  private void pushback(int index) {
    //System.out.println("SIN.pushback()");
    if(index < 0 || index >= buffer.limit() - search.length) {
      return;
    }
    setReading();
    ByteBuffer buf = buffer.duplicate();
    buf.position(index + search.length);
    while(buf.hasRemaining()) {
      pin.unread(buf.get());
    }
  }
  
  
  private void setWriting() {
    //System.out.println("SIN.setWriting()");
    if(reading) {
      buffer.clear();
      reading = false;
    }
  }
  
  
  private void setReading() {
    //System.out.println("SIN.setReading()");
    if(!reading) {
      buffer.flip();
      reading = true;
    }
  }
  
  
  private void fillBuffer() throws IOException {
    //System.out.println("SIN.fillBuffer()");
    if(eof || found) return;
    setWriting();
    byte[] b = new byte[1];
    int read = 0;
    while(buffer.hasRemaining()) {
      read = pin.read(b);
      if(read != 1) {
        eof = true;
        break;
      }
      buffer.put(b[0]);
    }
  }
  
  
  private void preRead() throws IOException {
    //System.out.println("SIN.preRead()");
    if(eof || found) return;
    setReading();
    if(!buffer.hasRemaining()) {
      fillBuffer();
    }
    int index = searchBuffer();
    if(index >= 0) {
      pushback(index);
      buffer.position(index).flip();
    }
  }
  
  
  @Override
  public int read(byte[] bs, int off, int len) throws IOException {
    //System.out.println("SIN.read()");
    Valid.off(bs).forEmpty().fail();
    Valid.off(off).forLesserThan(0)
        .fail("Invalid offset: ");
    Valid.off(len).forNotBetween(
        1, bs.length-off
    ).fail();
    
    preRead();
    if(!buffer.hasRemaining()) return -1;
    int nlen = Math.min(len, buffer.remaining());
    buffer.get(bs, off, nlen);
    return nlen;
  }
  
  
  @Override
  public int read(byte[] bs) throws IOException {
    return this.read(
        Valid.off(bs).forEmpty()
            .getOrFail(), 0, bs.length
    );
  }
  
  
  @Override
  public int read() throws IOException {
    byte[] bs = new byte[1];
    int r = this.read(bs);
    if(r > 0) r = bs[0];
    return r;
  }
  
}
