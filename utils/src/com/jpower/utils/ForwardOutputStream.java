package com.jpower.utils;

import java.io.IOException;
import java.io.OutputStream;

/**
 *
 * @author Juno Roesler
 */
public class ForwardOutputStream 
    extends OutputStream
{

  private IOutputStream out;


  public ForwardOutputStream(IOutputStream out)
  {
    this.out = out;
  }

  @Override
  public void close()
  {
    out.close();
  }

  @Override
  public void flush()
  {
    out.flush();
  }

  @Override
  public void write(byte[] b)
      throws IOException
  {
    out.write(b);
  }

  @Override
  public void write(int b)
      throws IOException
  {
    out.write(b);
  }

  @Override
  public void write(byte[] b, int off, int len)
      throws IOException
  {
    out.write(b, off, len);
  }

}
