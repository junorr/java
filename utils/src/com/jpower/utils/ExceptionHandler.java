package com.jpower.utils;

/**
 *
 * @author F6036477
 */
public interface ExceptionHandler 
{

  public void handle(Throwable th);

  public void handle();

  public Throwable get();

  public void set(Throwable th);

}
