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

package us.pserver.tools;

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
public class ValidThrows<T, E extends Exception> {

  private final T obj;
  
  private final String message;
  
  private final String defmsg;
  
  private final Class<E> exc;
  
  private final boolean error;
  
  
  public ValidThrows(T obj, Class<E> exc) {
    this(obj, null, null, exc, false);
  }
  
  
  ValidThrows(T obj, String msg, String defmsg, Class<E> exc, boolean error) {
    if(exc == null) {
      throw new ValidatorException(MSG_ARG, "exc", exc);
    }
    this.obj = obj;
    this.message = msg;
    this.exc = exc;
    this.error = error;
    this.defmsg = defmsg;
  }
  
  
  public static <X,Y extends Exception> ValidThrows<X,Y> off(X obj, Class<Y> exc) {
    return new ValidThrows(obj, exc);
  }
  
  
  public <X,Y extends Exception> ValidThrows<X,Y> valid(X obj, Class<Y> exc) {
    return new ValidThrows(obj, exc);
  }
  
  
  public ValidThrows<T,E> message(final String msg) {
    if(msg == null || msg.isEmpty()) {
      throw new ValidatorException(MSG_ARG, "msg", msg);
    }
    String str = msg;
    if(msg.trim().endsWith(":")) {
      str = f("%s %s", msg.trim(), obj);
    }
    return new ValidThrows(obj, str, null, exc, error);
  }
  
  
  public ValidThrows<T,E> message(final String msg, final Object ... args) {
    if(msg == null || msg.isEmpty()) {
      throw new ValidatorException(MSG_ARG, "msg", msg);
    }
    return message(String.format(msg, args));
  }
  
  
  public ValidThrows<T,E> message(final Class cls) {
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
  
  
  public ValidThrows<T,E> fail() throws E {
    if(error) throwException();
    return this;
  }
  
  
  public ValidThrows<T,E> fail(String msg) throws E {
    return this.message(msg).fail();
  }
  
  
  public ValidThrows<T,E> fail(String msg, Object ... args) throws E {
    return this.message(msg, args).fail();
  }
  
  
  public ValidThrows<T,E> fail(Class cls) throws E {
    return this.message(cls).fail();
  }
  
  
  public Valid<T> valid() {
    return Valid.off(obj);
  }
  
  
  public <X> Valid<X> valid(X obj) {
    return Valid.off(obj);
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
  
  
  public ValidThrows<T,E> forNull() {
    return new ValidThrows(
        obj, message, 
        f(MSG_NULL, obj), 
        exc, obj == null
    );
  }
  
  
  public ValidThrows<T,E> forEmpty() throws E {
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
    return new ValidThrows(obj, message, 
        f(MSG_EMPTY, obj.getClass().getSimpleName(), obj),
        exc, empty
    );
  }
  
  
  public ValidThrows<T,E> forTest(boolean test) throws E {
    forNull().fail();
    return new ValidThrows(
        obj, message, 
        f(MSG_TEST, obj.getClass().getSimpleName(), obj), 
        exc, test
    );
  }
  
  
  public ValidThrows<T,E> forTypeMatch(Class cls) throws E {
    forNull().fail();
    if(cls == null) {
      throw new ValidatorException(MSG_ARG, "cls", cls);
    }
    return new ValidThrows(
        obj, message, 
        f(MSG_INSTANCE, obj, cls.getName()),
        exc, !cls.isAssignableFrom(obj.getClass())
    );
  }
  
  
  public ValidThrows<T,E> forLesserThen(Number n) throws E {
    forNull().fail()
        .forTypeMatch(Number.class)
        .fail();
    if(n == null) {
      throw new ValidatorException(MSG_ARG, "n", n);
    }
    BigDecimal bo = new BigDecimal(((Number)obj).doubleValue());
    BigDecimal bn = new BigDecimal(n.doubleValue());
    return new ValidThrows(
        obj, message, 
        f(MSG_LESSER, obj, n), 
        exc, bo.compareTo(bn) < 0
    );
  }
  
  
  public ValidThrows<T,E> forLesserEquals(Number n) throws E {
    forNull().fail()
        .forTypeMatch(Number.class)
        .fail();
    if(n == null) {
      throw new ValidatorException(MSG_ARG, "n", n);
    }
    BigDecimal bo = new BigDecimal(((Number)obj).doubleValue());
    BigDecimal bn = new BigDecimal(n.doubleValue());
    return new ValidThrows(
        obj, message, 
        f(MSG_LESSER_EQ, obj, n), 
        exc, bo.compareTo(bn) <= 0
    );
  }
  
  
  public ValidThrows<T,E> forGreaterThen(Number n) throws E {
    forNull().fail()
        .forTypeMatch(Number.class)
        .fail();
    if(n == null) {
      throw new ValidatorException(MSG_ARG, "n", n);
    }
    BigDecimal bo = new BigDecimal(((Number)obj).doubleValue());
    BigDecimal bn = new BigDecimal(n.doubleValue());
    return new ValidThrows(
        obj, message, 
        f(MSG_GREATER, obj, n), 
        exc, bo.compareTo(bn) > 0
    );
  }
  
  
  public ValidThrows<T,E> forNotEquals(Object o) throws E {
    if(o == null) {
      throw new ValidatorException(MSG_ARG, "o", o);
    }
    forNull().fail();
    return new ValidThrows(
        obj, message, 
        f(MSG_NOT_EQUALS, obj, o), 
        exc, !obj.equals(o)
    );
  }
  
  
  public ValidThrows<T,E> not() {
    return new ValidThrows(
        obj, message, defmsg, exc, !error
    );
  }
  
  
  public ValidThrows<T,E> forGreaterEquals(Number n) throws E {
    forNull().fail()
        .forTypeMatch(Number.class)
        .fail();
    if(n == null) {
      throw new ValidatorException(MSG_ARG, "n", n);
    }
    BigDecimal bo = new BigDecimal(((Number)obj).doubleValue());
    BigDecimal bn = new BigDecimal(n.doubleValue());
    return new ValidThrows(
        obj, message, 
        f(MSG_GREATER_EQ, obj, n), 
        exc, bo.compareTo(bn) >= 0
    );
  }
  
  
  public ValidThrows<T,E> forNotBetween(Number start, Number end) throws E {
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
    return new ValidThrows(
        obj, message, 
        f(MSG_BETWEEN, obj, start, end), 
        exc, bo.compareTo(bs) < 0 || bo.compareTo(be) > 0
    );
  }
  
  
  public ValidThrows<T,E> forLesserThen(Date dt) throws E {
    forNull().fail()
        .forTypeMatch(Date.class)
        .fail();
    if(dt == null) {
      throw new ValidatorException(MSG_ARG, "d", dt);
    }
    Date date = ((Date)obj);
    return new ValidThrows(
        obj, message, 
        f(MSG_LESSER, obj, dt), 
        exc, date.before(dt)
    );
  }
  
  
  public ValidThrows<T,E> forLesserEquals(Date dt) throws E {
    forNull().fail()
        .forTypeMatch(Number.class)
        .fail();
    if(dt == null) {
      throw new ValidatorException(MSG_ARG, "dt", dt);
    }
    Date date = ((Date)obj);
    return new ValidThrows(
        obj, message, 
        f(MSG_LESSER_EQ, obj, dt), 
        exc, date.before(dt) || date.equals(dt)
    );
  }
  
  
  public ValidThrows<T,E> forGreaterThen(Date dt) throws E {
    forNull().fail()
        .forTypeMatch(Number.class)
        .fail();
    if(dt == null) {
      throw new ValidatorException(MSG_ARG, "dt", dt);
    }
    Date date = ((Date)obj);
    return new ValidThrows(
        obj, message, 
        f(MSG_GREATER, obj, dt), 
        exc, date.after(dt)
    );
  }
  
  
  public ValidThrows<T,E> forGreaterEquals(Date dt) throws E {
    forNull().fail()
        .forTypeMatch(Number.class)
        .fail();
    if(dt == null) {
      throw new ValidatorException(MSG_ARG, "dt", dt);
    }
    Date date = ((Date)obj);
    return new ValidThrows(
        obj, message, 
        f(MSG_GREATER_EQ, obj, dt), 
        exc, date.after(dt) || date.equals(dt)
    );
  }
  
  
  public ValidThrows<T,E> forNotBetween(Date start, Date end) throws E {
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
    return new ValidThrows(
        obj, message, 
        f(MSG_BETWEEN, obj, start, end), 
        exc, date.before(start) || date.after(end)
    );
  }
  
  
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
