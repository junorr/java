package com.jpower.utils;

/**
 *
 * @author F6036477
 */
public class UnreportedExceptionHandler extends AbstractExceptionHandler
{

  public enum ExceptionType {
    NULL_POINTER, ILLEGAL_ARGUMENT, ILLEGAL_STATE
  }


  private ExceptionType type;


  public UnreportedExceptionHandler()
  {
    super();
    type = ExceptionType.ILLEGAL_STATE;
  }


  public UnreportedExceptionHandler(Throwable th)
  {
    super(th);
  }


  public void handle(Throwable th)
  {
    set(th);
    handle();
  }


  public void handle()
  {
    switch(type) {
      case ILLEGAL_ARGUMENT:
        throwIllegalArgumentException();
        break;
      case NULL_POINTER:
        throwNullPointerException();
        break;
      default:
        throwIllegalStateException();
        break;
    }
  }


  public void throwIllegalStateException()
  {
    throw new IllegalStateException(get());
  }


  public void throwNullPointerException()
  {
    throw new NullPointerException(get().getMessage());
  }


  public void throwIllegalArgumentException()
  {
    throw new IllegalArgumentException(get());
  }

}
