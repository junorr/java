package com.jpower.utils;

import java.io.IOException;

/**
 *
 * @author Juno Roesler
 */
public interface IInputStream
{

  public int available();

  public void close();

  public void mark(int readlimit);

  public boolean markSupported();

  public int read();

  public int read(byte[] b);

  public int read(byte[] b, int off, int len);

  public void reset();

  public long skip(long n);

}
