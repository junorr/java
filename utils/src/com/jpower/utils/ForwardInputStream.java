package com.jpower.utils;

import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author Juno Roesler
 */
public class ForwardInputStream 
    extends InputStream
{

  private IInputStream in;


  public ForwardInputStream(IInputStream in)
  {
    this.in = in;
  }

  @Override
  public int available()
  {
    return in.available();
  }

  @Override
  public void close()
  {
    in.close();
  }

  @Override
  public void mark(int readlimit)
  {
    in.mark(readlimit);
  }

  @Override
  public boolean markSupported()
  {
    return in.markSupported();
  }

  @Override
  public int read()
  {
    return in.read();
  }

  @Override
  public int read(byte[] b)
  {
    return in.read(b);
  }

  @Override
  public int read(byte[] b, int off, int len)
  {
    return in.read(b, off, len);
  }

  @Override
  public void reset()
  {
    in.reset();
  }

  @Override
  public long skip(long n)
  {
    return in.skip(n);
  }

}
