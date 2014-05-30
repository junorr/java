package com.jpower.utils;

import java.io.File;

/**
 *
 * @author Juno Roesler
 */
public class BufferConfig
{

  private double percLength;

  private long maxSize;


  public BufferConfig()
  {
    percLength = .0;
    maxSize = 0;
  }

  public BufferConfig(double percLength, long maxSize)
  {
    this.percLength = percLength;
    this.maxSize = maxSize;
  }

  public long getMaxSize()
  {
    return maxSize;
  }

  public void setMaxSize(long maxSize)
  {
    this.maxSize = maxSize;
  }

  public double getPercLength()
  {
    return percLength;
  }

  public void setPercLength(double percLength)
  {
    this.percLength = percLength;
  }

  public long calcBufferSize(File f)
  {
    if(f == null || !f.exists())
      return -1;

    long buffSize = 0;
    long fileLength = f.length();

    buffSize = (long) (fileLength * this.percLength);
    if(buffSize > this.maxSize)
      buffSize = this.maxSize;

    return buffSize;
  }

  @Override
  public String toString()
  {
    return "[ BufferConfig ] : " +
        String.valueOf(percLength * 100).substring(0,
          String.valueOf(percLength * 100).indexOf(".") + 2) +
        "% : " + maxSize / 1024 + " KB.";
  }

  public static void main(String[] args)
  {
    BufferConfig bfg = new BufferConfig(.6, 100*1024);
    System.out.println( bfg );
    File f = new File("C:/Java/Calendario_1.2b.jar");
    System.out.println( "Buffer Size for " + f.getName() + ": " + bfg.calcBufferSize(f) / 1024 + " KB.");
  }

}
