/*
 * Direitos Autorais Reservados (c) 2011 Juno Roesler
 * Contato: juno.rr@gmail.com
 * 
 * Esta biblioteca é software livre; você pode redistribuí-la e/ou modificá-la sob os
 * termos da Licença Pública Geral Menor do GNU conforme publicada pela Free
 * Software Foundation; tanto a versão 2.1 da Licença, ou qualquer
 * versão posterior.
 * 
 * Esta biblioteca é distribuída na expectativa de que seja útil, porém, SEM
 * NENHUMA GARANTIA; nem mesmo a garantia implícita de COMERCIABILIDADE
 * OU ADEQUAÇÃO A UMA FINALIDADE ESPECÍFICA. Consulte a Licença Pública
 * Geral Menor do GNU para mais detalhes.
 * 
 * Você deve ter recebido uma cópia da Licença Pública Geral Menor do GNU junto
 * com esta biblioteca; se não, acesse 
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html, 
 * ou escreva para a Free Software Foundation, Inc., no
 * endereço 59 Temple Street, Suite 330, Boston, MA 02111-1307 USA.
 */

package us.pserver.xprops.util;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 01/08/2015
 */
public class ThrowsValidator<T, E extends Exception> {
  
  private static final String MSG_NULL = "Invalid Null Object: %s";

  private static final String MSG_EMPTY = "Invalid Empty %s: %s";
  
  private static final String MSG_ARG = "Invalid Argument: %s=%s";
  
  private static final String MSG_TYPE = "Invalid %s: %s";
  
  private static final String MSG_TEST = "Test Not Passed for %s: %s";
  
  private static final String MSG_INSTANCE = "%s is Not a Type of %s";
  
  private static final String MSG_EXC_TYPE = "Exception Type not has a <init>( String ) Constructor: %s";
  
  private static final String MSG_LESSER = "Argument is Lesser Then %2$s (%1$s < %2$s)";
  
  private static final String MSG_LESSER_EQ = "Argument is Lesser Or Equals %2$s (%1$s <= %2$s)";
  
  private static final String MSG_GREATER = "Argument is Greater Then %2$s (%1$s > %2$s)";
  
  private static final String MSG_GREATER_EQ = "Argument is Greater Or Equals %2$s (%1$s >= %2$s)";
  
  private static final String MSG_BETWEEN = "%s is Not Between Range [%s..%s]";
  
  private static final String MSG_NOT_EQUALS = "%s is Not Equals %s (%1$s != %2$s)";
  

  private final T obj;
  
  private final String message;
  
  private final String defmsg;
  
  private final Class<E> exc;
  
  private final boolean error;
  
  
  public ThrowsValidator(T obj, Class<E> exc) {
    this(obj, null, null, exc, false);
  }
  
  
  ThrowsValidator(T obj, String msg, String defmsg, Class<E> exc, boolean fail) {
    if(exc == null) {
      throw new ValidatorException(MSG_ARG, "exc", exc);
    }
    this.obj = obj;
    this.message = msg;
    this.exc = exc;
    this.error = fail;
    this.defmsg = defmsg;
  }
  
  
  public static <X,Y extends Exception> ThrowsValidator<X,Y> off(X obj, Class<Y> exc) {
    return new ThrowsValidator(obj, exc);
  }
  
  
  public <X,Y extends Exception> ThrowsValidator<X,Y> on(X obj, Class<Y> exc) {
    return new ThrowsValidator(obj, exc);
  }
  
  
  public ThrowsValidator<T,E> message(final String msg) {
    if(msg == null || msg.isEmpty()) {
      throw new ValidatorException(MSG_ARG, "msg", msg);
    }
    String str = msg;
    if(msg.trim().endsWith(":")) {
      str = f("%s %s", msg.trim(), obj);
    }
    return new ThrowsValidator(obj, str, null, exc, error);
  }
  
  
  public ThrowsValidator<T,E> message(final String msg, final Object ... args) {
    if(msg == null || msg.isEmpty()) {
      throw new ValidatorException(MSG_ARG, "msg", msg);
    }
    return message(String.format(msg, args));
  }
  
  
  public ThrowsValidator<T,E> message(final Class cls) {
    if(cls == null) {
      throw new ValidatorException(MSG_ARG, "cls", cls);
    }
    return message(MSG_TYPE, cls.getSimpleName(), obj);
  }
  
  
  public T get() {
    return obj;
  }
  
  
  public String message() {
    return message;
  }
  
  
  public Class<E> exceptionType() {
    return exc;
  }
  
  
  public boolean hasError() {
    return error;
  }
  
  
  private Constructor getExceptionConstructor() throws NoSuchMethodException {
    Constructor[] cst = exc.getDeclaredConstructors();
    Constructor found = null;
    for(Constructor c : cst) {
      if(c.getParameterCount() == 1
          && String.class.equals(c.getParameterTypes()[0])) {
        found = c;
        break;
      }
    }
    if(found == null) {
      throw new NoSuchMethodException(f(MSG_EXC_TYPE, exc));
    }
    return found;
  }
  
  
  private E createException() {
    try {
      if(message != null)
        return (E) getExceptionConstructor()
            .newInstance(message);
      return (E) getExceptionConstructor()
          .newInstance(defmsg);
    } 
    catch(Exception e) {
      throw new ValidatorException(e.getMessage(), e);
    }
  } 
  
  
  public void throwException() throws E {
    throw createException();
  }
  
  
  public ThrowsValidator<T,E> fail() throws E {
    if(error) throwException();
    return this;
  }
  
  
  public ThrowsValidator<T,E> fail(String msg) throws E {
    return this.message(msg).fail();
  }
  
  
  public ThrowsValidator<T,E> fail(String msg, Object ... args) throws E {
    return this.message(msg, args).fail();
  }
  
  
  public ThrowsValidator<T,E> fail(Class cls) throws E {
    return this.message(cls).fail();
  }
  
  
  public Validator<T> validator() {
    return Validator.off(obj);
  }
  
  
  public <X> Validator<X> validator(X obj) {
    return Validator.off(obj);
  }
  
  
  public T getOrFail() throws E {
    this.fail();
    return obj;
  }
  
  
  public T getOrFail(String msg) throws E {
    return this.message(msg).getOrFail();
  }
  
  
  public T getOrFail(String msg, Object ... args) throws E {
    return this.message(msg, args).getOrFail();
  }
  
  
  public T getOrFail(Class cls) throws E {
    return this.message(cls).getOrFail();
  }
  
  
  static String f(String str, Object ... args) {
    return String.format(str, args);
  }
  
  
  public ThrowsValidator<T,E> forNull() {
    return new ThrowsValidator(
        obj, message, 
        f(MSG_NULL, obj), 
        exc, obj == null
    );
  }
  
  
  public ThrowsValidator<T,E> forEmpty() throws E {
    forNull().fail();
    boolean empty = false;
    if(obj.getClass().isArray()) {
      empty = Array.getLength(obj) < 1;
    }
    else if(List.class.isAssignableFrom(obj.getClass())
        && ((List)obj).isEmpty()) {
      empty = ((List)obj).isEmpty();
    }
    else {
      empty = Objects.toString(obj).isEmpty();
    }
    return new ThrowsValidator(obj, message, 
        f(MSG_EMPTY, obj.getClass().getSimpleName(), obj),
        exc, empty
    );
  }
  
  
  public ThrowsValidator<T,E> forTest(boolean test) throws E {
    forNull().fail();
    return new ThrowsValidator(
        obj, message, 
        f(MSG_TEST, obj.getClass().getSimpleName(), obj), 
        exc, test
    );
  }
  
  
  public ThrowsValidator<T,E> forTypeMatch(Class cls) throws E {
    forNull().fail();
    if(cls == null) {
      throw new ValidatorException(MSG_ARG, "cls", cls);
    }
    return new ThrowsValidator(
        obj, message, 
        f(MSG_INSTANCE, obj, cls.getName()),
        exc, !cls.isAssignableFrom(obj.getClass())
    );
  }
  
  
  public ThrowsValidator<T,E> forLesserThen(Number n) throws E {
    forNull().fail()
        .forTypeMatch(Number.class)
        .fail();
    if(n == null) {
      throw new ValidatorException(MSG_ARG, "n", n);
    }
    BigDecimal bo = new BigDecimal(((Number)obj).doubleValue());
    BigDecimal bn = new BigDecimal(n.doubleValue());
    return new ThrowsValidator(
        obj, message, 
        f(MSG_LESSER, obj, n), 
        exc, bo.compareTo(bn) < 0
    );
  }
  
  
  public ThrowsValidator<T,E> forLesserEquals(Number n) throws E {
    forNull().fail()
        .forTypeMatch(Number.class)
        .fail();
    if(n == null) {
      throw new ValidatorException(MSG_ARG, "n", n);
    }
    BigDecimal bo = new BigDecimal(((Number)obj).doubleValue());
    BigDecimal bn = new BigDecimal(n.doubleValue());
    return new ThrowsValidator(
        obj, message, 
        f(MSG_LESSER_EQ, obj, n), 
        exc, bo.compareTo(bn) <= 0
    );
  }
  
  
  public ThrowsValidator<T,E> forGreaterThen(Number n) throws E {
    forNull().fail()
        .forTypeMatch(Number.class)
        .fail();
    if(n == null) {
      throw new ValidatorException(MSG_ARG, "n", n);
    }
    BigDecimal bo = new BigDecimal(((Number)obj).doubleValue());
    BigDecimal bn = new BigDecimal(n.doubleValue());
    return new ThrowsValidator(
        obj, message, 
        f(MSG_GREATER, obj, n), 
        exc, bo.compareTo(bn) > 0
    );
  }
  
  
  public ThrowsValidator<T,E> forNotEquals(Object o) throws E {
    if(o == null) {
      throw new ValidatorException(MSG_ARG, "o", o);
    }
    forNull().fail();
    return new ThrowsValidator(
        obj, message, 
        f(MSG_NOT_EQUALS, obj, o), 
        exc, !obj.equals(o)
    );
  }
  
  
  public ThrowsValidator<T,E> not() {
    return new ThrowsValidator(
        obj, message, defmsg, exc, !error
    );
  }
  
  
  public ThrowsValidator<T,E> forGreaterEquals(Number n) throws E {
    forNull().fail()
        .forTypeMatch(Number.class)
        .fail();
    if(n == null) {
      throw new ValidatorException(MSG_ARG, "n", n);
    }
    BigDecimal bo = new BigDecimal(((Number)obj).doubleValue());
    BigDecimal bn = new BigDecimal(n.doubleValue());
    return new ThrowsValidator(
        obj, message, 
        f(MSG_GREATER_EQ, obj, n), 
        exc, bo.compareTo(bn) >= 0
    );
  }
  
  
  public ThrowsValidator<T,E> forNotBetween(Number start, Number end) throws E {
    forNull().fail()
        .forTypeMatch(Number.class)
        .fail();
    if(start == null) {
      throw new ValidatorException(MSG_ARG, "nl", start);
    }
    if(end == null) {
      throw new ValidatorException(MSG_ARG, "ng", end);
    }
    BigDecimal bo = new BigDecimal(((Number)obj).doubleValue());
    BigDecimal bs = new BigDecimal(start.doubleValue());
    BigDecimal be = new BigDecimal(end.doubleValue());
    return new ThrowsValidator(
        obj, message, 
        f(MSG_BETWEEN, obj, start, end), 
        exc, bo.compareTo(bs) < 0 || bo.compareTo(be) > 0
    );
  }
  
  
  public ThrowsValidator<T,E> forLesserThen(Date dt) throws E {
    forNull().fail()
        .forTypeMatch(Date.class)
        .fail();
    if(dt == null) {
      throw new ValidatorException(MSG_ARG, "d", dt);
    }
    Date date = ((Date)obj);
    return new ThrowsValidator(
        obj, message, 
        f(MSG_LESSER, obj, dt), 
        exc, date.before(dt)
    );
  }
  
  
  public ThrowsValidator<T,E> forLesserEquals(Date dt) throws E {
    forNull().fail()
        .forTypeMatch(Number.class)
        .fail();
    if(dt == null) {
      throw new ValidatorException(MSG_ARG, "dt", dt);
    }
    Date date = ((Date)obj);
    return new ThrowsValidator(
        obj, message, 
        f(MSG_LESSER_EQ, obj, dt), 
        exc, date.before(dt) || date.equals(dt)
    );
  }
  
  
  public ThrowsValidator<T,E> forGreaterThen(Date dt) throws E {
    forNull().fail()
        .forTypeMatch(Number.class)
        .fail();
    if(dt == null) {
      throw new ValidatorException(MSG_ARG, "dt", dt);
    }
    Date date = ((Date)obj);
    return new ThrowsValidator(
        obj, message, 
        f(MSG_GREATER, obj, dt), 
        exc, date.after(dt)
    );
  }
  
  
  public ThrowsValidator<T,E> forGreaterEquals(Date dt) throws E {
    forNull().fail()
        .forTypeMatch(Number.class)
        .fail();
    if(dt == null) {
      throw new ValidatorException(MSG_ARG, "dt", dt);
    }
    Date date = ((Date)obj);
    return new ThrowsValidator(
        obj, message, 
        f(MSG_GREATER_EQ, obj, dt), 
        exc, date.after(dt) || date.equals(dt)
    );
  }
  
  
  public ThrowsValidator<T,E> forNotBetween(Date start, Date end) throws E {
    forNull().fail()
        .forTypeMatch(Number.class)
        .fail();
    if(start == null) {
      throw new ValidatorException(MSG_ARG, "start", start);
    }
    if(end == null) {
      throw new ValidatorException(MSG_ARG, "end", end);
    }
    Date date = ((Date)obj);
    return new ThrowsValidator(
        obj, message, 
        f(MSG_BETWEEN, obj, start, end), 
        exc, date.before(start) || date.after(end)
    );
  }
  
  
  
  
  
  public static class ValidatorException extends IllegalArgumentException {
    
    public ValidatorException(String msg) {
      super(msg);
    }
    
    public ValidatorException(String msg, Throwable cse) {
      super(msg, cse);
    }
    
    public ValidatorException(String msg, Object ... args) {
      super(String.format(msg, args));
    }
    
  }
  
}
