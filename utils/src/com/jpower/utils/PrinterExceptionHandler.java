package com.jpower.utils;

import java.io.PrintStream;

/**
 *
 * @author F6036477
 */
public class PrinterExceptionHandler extends AbstractExceptionHandler
{

  private PrintStream p;

  private String prefix;


  public PrinterExceptionHandler()
  {
    super();
    p = System.err;
    prefix = "ERROR";
  }


  public PrinterExceptionHandler(Throwable th)
  {
    super(th);
  }


  public void handle(Throwable th)
  {
    set(th);
    print();
  }


  public void handle()
  {
    print();
  }


  public String getPrefixMessage()
  {
    return prefix;
  }


  public void setPrefixMessage(String prefix)
  {
    this.prefix = prefix;
  }


  public PrintStream getPrinter()
  {
    return p;
  }


  public void setPrinter(PrintStream ps)
  {
    if(ps == null) return;
    p = ps;
  }


  public void print()
  {
    if(get() == null) return;
    p.println("[" + prefix + "]" + get().getMessage());
  }

}
