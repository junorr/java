package com.jpower.utils;

/**
 *
 * @author F6036477
 */
public abstract class AbstractExceptionHandler implements ExceptionHandler
{

  private Throwable th;


  protected AbstractExceptionHandler()
  {
    th = null;
  }


  protected AbstractExceptionHandler(Throwable th)
  {
    set(th);
  }


  public Throwable get()
  {
    return th;
  }

  public void set(Throwable th)
  {
    this.th = th;
  }

}
