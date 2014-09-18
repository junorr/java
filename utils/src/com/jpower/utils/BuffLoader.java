package com.jpower.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.Observable;

/**
 *
 * @author Juno Roesler
 */
public class BuffLoader
    extends Observable
{

  private InputStream in;

  private RandomAccessFile raf;


  private BuffLoader(InputStream in, RandomAccessFile raf)
  {
    if(in == null)
      throw new IllegalArgumentException("InputStream must be not null.");

    this.in = in;
    this.raf = raf;
  }

  public BuffLoader(InputStream in)
  {
    this(in, null);
  }

  public BuffLoader(RandomAccessFile raf)
  {
    this(null, raf);
  }

  public void load(StaticByteBuffer buffer, boolean wait)
      throws IOException
  {
    Object lock = null;
    if(wait) lock = new Object();

    Loader l = new Loader(buffer, lock);
    try {
      if(wait)
        lock.wait();
    } catch (InterruptedException ex) {
      throw new IOException("Error waiting Thread [lock.wait()].");
    }

    this.setChanged();
    this.notifyObservers();
    this.clearChanged();

    if(l.getError() != null)
      throw l.getError();
  }


  private class Loader extends Thread
  {
    private StaticByteBuffer buffer;
    private Object lock;
    private IOException exception;
    public Loader(StaticByteBuffer buffer, Object lock)
    {
      this.buffer = buffer;
      this.exception = null;
      this.lock = lock;
    }
    @Override
    public void run()
    {
      if(buffer == null || in == null)
        return;

      try {
        byte[] b = new byte[buffer.length()];

        if(in != null)
          in.read(b);
        else
          raf.read(b);

        buffer.OUT.write(b);
      } catch(IOException ex) {
        ex.printStackTrace();
        exception = ex;
      } finally {
        if(lock != null)
          lock.notifyAll();
      }
    }
    public IOException getError()
    {
      return this.exception;
    }
  }//Loader

}
