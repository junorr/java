package com.jpower.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author Juno Roesler
 */
public class StaticByteBuffer
{

  public final InputStream IN;

  public final OutputStream OUT;

  private byte[] buffer;

  private int pointer;

  private int mark;

  private int readlimit;


  public StaticByteBuffer(int size)
  {
    if(size <= 0)
      throw new IllegalArgumentException("Invalid size: " + size + ".");

    IN = new ForwardInputStream(new InnerInputStream());
    OUT = new ForwardOutputStream(new InnerOutputStream());
    buffer = new byte[size];
    pointer = 0;
    mark = 0;
    readlimit = 0;
  }

  public int length()
  {
    return buffer.length;
  }

  public int size()
  {
    return pointer;
  }

  public void transfer(OutputStream out)
      throws IOException
  {
    if(out == null) return;
    out.write(buffer, 0, pointer);
  }

  public byte[] getBuffer()
  {
    return buffer;
  }

  public void seek(long pos)
      throws IOException
  {
    if(pos < 0 || pos > buffer.length)
      throw new IOException("Illegal position on seek(long pos): "+ pos);

    pointer = (int) pos;
  }


  private class InnerInputStream
      implements IInputStream
  {
    public int available()
    {
      return buffer.length - pointer;
    }

    public void close()
    {
      buffer = null;
    }

    public void mark(int rlimit)
    {
      if(readlimit <= 0)
        readlimit = Integer.MAX_VALUE;

      readlimit = rlimit;
      mark = pointer;
    }

    public boolean markSupported()
    {
      return true;
    }

    public int read()
    {
      if(pointer >= buffer.length)
        return -1;

      readlimit--;
      if(readlimit <= 0)
        mark = 0;

      return buffer[pointer++];
    }

    public int read(byte[] b)
    {
      return this.read(b, 0, b.length);
    }

    public int read(byte[] b, int off, int len)
    {
      if(b == null || b.length == 0)
        return -1;
      if(off < 0 || off > len)
        return -1;
      if(len > b.length || len < off)
        return -1;

      int reads = 0;
      for(int i = 0; (i < buffer.length || (i < len || i < b.length)); i++)
      {
        b[i] = (byte) this.read();
        reads++;
      }//for

      return reads;
    }

    public void reset()
    {
      pointer = mark;
    }

    public long skip(long n)
    {
      long skipped = 0;

      for(int i = 0; i < n; i++)
      {
        if(this.read() != -1)
          skipped++;
        else
          break;
      }//for
    
      return skipped;
    }
  }//InnerInputStream


  private class InnerOutputStream
      implements IOutputStream
  {
    public void close()
    {
      buffer = null;
    }

    public void flush() { }

    public void write(int b)
        throws IOException
    {
      if(pointer >= buffer.length)
        throw new IOException("End of buffer stream reached: "+ pointer+ " / "+ buffer.length);

      buffer[pointer] = (byte) b;
      pointer++;
    }

    public void write(byte[] b)
        throws IOException
    {
      this.write(b, 0, b.length);
    }

    public void write(byte[] b, int off, int len)
        throws IOException
    {
      if(b == null)
        throw new IOException("Byte array must be not null.");
      if(off < 0 || (off >= len || len > b.length))
        throw new IOException("Illegal arguments: int off / int len - ("+ off+ " / "+ len+ ")");

      for(int i = off; i < len; i++)
      {
        this.write((int) b[i]);
      }//for
    }
  }//InnerOutputStream


  public static void main(String[] args)
      throws IOException
  {
    StaticByteBuffer buf = new StaticByteBuffer(5);
    for(int i = 0; i < 4; i++)
    {
      buf.OUT.write(i);
    }//for
    buf.IN.reset();
    for(int i = 0; i < 10; i++)
    {
      if(i == 2)
        buf.IN.mark(3);
      System.out.println( buf.IN.read() );
      if(i % 3 == 0)
        buf.IN.reset();
    }//for
  }

}
