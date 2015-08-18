/*
 * Direitos Autorais Reservados (c) 2011 Juno Roesler
 * Contato: juno.rr@gmail.com
 * 
 * Esta biblioteca e um software livre; voce pode redistribui-la e/ou modifica-la sob os
 * termos da Licenca Publica Geral Menor do GNU conforme publicada pela Free
 * Software Foundation; tanto a versao 2.1 da Licenca, ou qualquer
 * versao posterior.
 * 
 * Esta biblioteca eh distribuida na expectativa de que seja util, porem, SEM
 * NENHUMA GARANTIA; nem mesmo a garantia implicita de COMERCIABILIDADE
 * OU ADEQUACAO A UMA FINALIDADE ESPECIFICA. Consulte a Licença Publica
 * Geral Menor do GNU para mais detalhes.
 * 
 * Voce deve ter recebido uma copia da Licença Publica Geral Menor do GNU junto
 * com esta biblioteca; se nao, acesse 
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html, 
 * ou escreva para a Free Software Foundation, Inc., no
 * endereco 59 Temple Street, Suite 330, Boston, MA 02111-1307 USA.
 */

package us.pserver.tools;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Utility class for argument validations and exception throwing on fails.
 * @param <T> Type of the validated object.
 * @param <E> Type of the exception to be throwed on fail.
 * @author Juno Roesler - juno@pserver.us
 */
public class ValidThrows<T, E extends Exception> {

  private final T obj;
  
  private final String message;
  
  private final String defmsg;
  
  private final Class<E> exc;
  
  private final boolean error;
  
  
  /**
   * Default constructor receives the object 
   * to validated and the type of the exception 
   * to be throwed on fail.
   * @param obj The object to be validated.
   * @param exc The type of the exception to be throwed on fail.
   */
  public ValidThrows(T obj, Class<E> exc) {
    this(obj, null, null, exc, false);
  }
  
  
  /**
   * Protected constructor which receives the object 
   * to validated, an optional String message for the exception, 
   * a default message for the exception, the type of the exception 
   * to be throwed on fail and a boolean indicating if a previos 
   * validation fail.
   * @param obj The object to be validated.
   * @param msg Optional message for the exception.
   * @param defmsg Default message for the exception.
   * @param exc The type of the exception to be throwed on fail.
   * @param error boolean indicating if a previous validation fail.
   */
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
  
  
  /**
   * Creates a new ValidThrows object for the specified object and exception type.
   * @param <X> Type of the object.
   * @param <Y> Type of the exception.
   * @param obj The object to be validated.
   * @param exc The exception type to be throwed.
   * @return a new ValidThrows object.
   */
  public static <X,Y extends Exception> ValidThrows<X,Y> off(X obj, Class<Y> exc) {
    return new ValidThrows(obj, exc);
  }
  
  
  /**
   * Creates a new ValidThrows object for the specified object and exception type.
   * @param <X> Type of the object.
   * @param <Y> Type of the exception.
   * @param obj The object to be validated.
   * @param exc The exception type to be throwed.
   * @return a new ValidThrows object.
   */
  public <X,Y extends Exception> ValidThrows<X,Y> valid(X obj, Class<Y> exc) {
    return new ValidThrows(obj, exc);
  }
  
  
  /**
   * Configure an error message for the exception on fail.
   * @param msg an error message for the exception on fail.
   * @return a new configured ValidThrows object.
   */
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
  
  
  /**
   * Configure an error message for the exception on fail.
   * @param msg an error message for the exception on fail.
   * @param args Objects to be interpolated in the string message.
   * @return a new configured ValidThrows object.
   */
  public ValidThrows<T,E> message(final String msg, final Object ... args) {
    if(msg == null || msg.isEmpty()) {
      throw new ValidatorException(MSG_ARG, "msg", msg);
    }
    return message(String.format(msg, args));
  }
  
  
  /**
   * Configure an error message with the class name.
   * @param cls the class which name will compose the message.
   * @return a new configured ValidThrows object.
   */
  public ValidThrows<T,E> message(final Class cls) {
    if(cls == null) {
      throw new ValidatorException(MSG_ARG, "cls", cls);
    }
    return message(MSG_TYPE, cls.getSimpleName(), obj);
  }
  
  
  /**
   * Get the object used in validations.
   * @return the object used in validations.
   */
  public T get() {
    return obj;
  }
  
  
  /**
   * Get the configured message for the exception.
   * @return the configured message for the exception.
   */
  public String message() {
    return message;
  }
  
  
  /**
   * Get the type of the exception to be throwed.
   * @return the type of the exception to be throwed.
   */
  public Class<E> exceptionType() {
    return exc;
  }
  
  
  /**
   * Check if there was an validation error.
   * @return <code>true</code> if the
   * there was an error on validation,
   * <code>false</code> otherwise.
   */
  public boolean hasError() {
    return error;
  }
  
  
  /**
   * Get the exception String constructor.
   * @return The constructor.
   * @throws NoSuchMethodException If the string constructor does not exists.
   */
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
  
  
  /**
   * Create an instance of the exception.
   * @return an instance of the exception. 
   */
  private E createException() {
    try {
      E exc = null;
      if(message != null) {
        exc = (E) getExceptionConstructor()
            .newInstance(message);
      }
      else {
        exc = (E) getExceptionConstructor()
           .newInstance(defmsg);
      }
      return exc;
    } 
    catch(Exception e) {
      throw new ValidatorException(e.getMessage(), e);
    }
  } 
  
  
  /**
   * Throw the configured exception.
   * @throws E The exception to be thrown.
   */
  public void throwException() throws E {
    throw createException();
  }
  
  
  /**
   * Check for error, throwing the exception if true.
   * @return This instance of ValidThrows.
   * @throws E The exception to be thrown in case of error.
   */
  public ValidThrows<T,E> fail() throws E {
    if(error) throwException();
    return this;
  }
  
  
  /**
   * Check for error, throwing the exception if true.
   * @param msg The exception message.
   * @return This instance of ValidThrows.
   * @throws E The exception to be thrown in case of error.
   */
  public ValidThrows<T,E> fail(String msg) throws E {
    return this.message(msg).fail();
  }
  
  
  /**
   * Check for error, throwing the exception if true.
   * @param msg The exception message.
   * @param args Objects to be interpolated in the message.
   * @return This instance of ValidThrows.
   * @throws E The exception to be thrown in case of error.
   */
  public ValidThrows<T,E> fail(String msg, Object ... args) throws E {
    return this.message(msg, args).fail();
  }
  
  
  /**
   * Check for error, throwing the exception if true.
   * @param cls The Class name in the exception message.
   * @return This instance of ValidThrows.
   * @throws E The exception to be thrown in case of error.
   */
  public ValidThrows<T,E> fail(Class cls) throws E {
    return this.message(cls).fail();
  }
  

  /**
   * Create a new Valid instance.
   * @return a new Valid instance. 
   */
  public Valid<T> valid() {
    return Valid.off(obj);
  }
  
  
  /**
   * Create a new Valid instance.
   * @param obj The object to validate.
   * @param <X> The type of the object.
   * @return a new Valid instance. 
   */
  public <X> Valid<X> valid(X obj) {
    return Valid.off(obj);
  }
  
  
  /**
   * Get the object or fail in case of validation error.
   * @return The object. 
   * @throws E In case of validation error.
   */
  public T getOrFail() throws E {
    this.fail();
    return obj;
  }
  
  
  /**
   * Get the object or fail in case of validation error.
   * @param msg The exception message.
   * @return The object. 
   * @throws E In case of validation error.
   */
  public T getOrFail(String msg) throws E {
    return this.message(msg).getOrFail();
  }
  
  
  /**
   * Get the object or fail in case of validation error.
   * @param msg The exception message.
   * @param args Objects to be interpolated in the exception message.
   * @return The object. 
   * @throws E In case of validation error.
   */
  public T getOrFail(String msg, Object ... args) throws E {
    return this.message(msg, args).getOrFail();
  }
  
  
  /**
   * Get the object or fail in case of validation error.
   * @param cls The Class name in the exception message.
   * @return The object. 
   * @throws E In case of validation error.
   */
  public T getOrFail(Class cls) throws E {
    return this.message(cls).getOrFail();
  }
  

  /**
   * Format the arguments via <code>String.format(String, Object...)</code>.
   * @param str The String to format.
   * @param args The object arguments to format in the String.
   * @return The formatted String.
   */
  static String f(String str, Object ... args) {
    return String.format(str, args);
  }
  
  
  /**
   * Check if the object is null.
   * @return A new instance of ValidThrows.
   */
  public ValidThrows<T,E> forNull() {
    return new ValidThrows(
        obj, message, 
        f(MSG_NULL, obj), 
        exc, obj == null
    );
  }
  
  
  /**
   * Check if the object is empty. The object is first 
   * tested <code>forNull()</code> and then for empty 
   * objects like String, Arrays and Lists.
   * @return A new instance of ValidThrows.
   * @throws E In case of validation error.
   * @see ValidThrows#forNull() 
   */
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
  
  
  /**
   * Check the boolean test. The object is first 
   * tested <code>forNull()</code> and then for 
   * the given boolean test.
   * @param test A boolean test to validated.
   * @return A new instance of ValidThrows.
   * @throws E In case of validation error.
   * @see ValidThrows#forNull() 
   */
  public ValidThrows<T,E> forTest(boolean test) throws E {
    forNull().fail();
    return new ValidThrows(
        obj, message, 
        f(MSG_TEST, obj.getClass().getSimpleName(), obj), 
        exc, test
    );
  }
  
  
  /**
   * Check if the object type match with the give type. 
   * The object is first tested <code>forNull()</code> 
   * and then for the match.
   * @param cls The type for matching.
   * @return A new instance of ValidThrows.
   * @throws E In case of validation error.
   * @see ValidThrows#forNull() 
   */
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
  
  
  /**
   * Check if the object is a number lesser then the given argument. 
   * The object is first tested <code>forNull()</code> 
   * and then for the comparison.
   * @param n The number to be compared.
   * @return A new instance of ValidThrows.
   * @throws E In case of validation error.
   * @see ValidThrows#forNull() 
   */
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
  
  
  /**
   * Check if the object is a number lesser or equals then the given argument. 
   * The object is first tested <code>forNull()</code> 
   * and then for the comparison.
   * @param n The number to be compared.
   * @return A new instance of ValidThrows.
   * @throws E In case of validation error.
   * @see ValidThrows#forNull() 
   */
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
  
  
  /**
   * Check if the object is a number greater then the given argument. 
   * The object is first tested <code>forNull()</code> 
   * and then for the comparison.
   * @param n The number to be compared.
   * @return A new instance of ValidThrows.
   * @throws E In case of validation error.
   * @see ValidThrows#forNull() 
   */
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
  
  
  /**
   * Check if the object is not equals to the given argument. 
   * The object is first tested <code>forNull()</code> 
   * and then for the comparison.
   * @param o The object to be compared.
   * @return A new instance of ValidThrows.
   * @throws E In case of validation error.
   * @see ValidThrows#forNull() 
   */
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
  
  
  /**
   * Invert the validation test.
   * @return A new ValidThrows configured instance.
   */
  public ValidThrows<T,E> not() {
    return new ValidThrows(
        obj, message, defmsg, exc, !error
    );
  }
  
  
  /**
   * Check if the object is a number greater or equals then the given argument. 
   * The object is first tested <code>forNull()</code> 
   * and then for the comparison.
   * @param n The number to be compared.
   * @return A new instance of ValidThrows.
   * @throws E In case of validation error.
   * @see ValidThrows#forNull() 
   */
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
  
  
  /**
   * Check if the object is a number between the given interval 
   * <code>(start &lt;= obj &lt;= end)</code>. 
   * The object is first tested <code>forNull()</code> 
   * and then for the comparison.
   * @param start The start number of the interval.
   * @param end The end number of the interval.
   * @return A new instance of ValidThrows.
   * @throws E In case of validation error.
   * @see ValidThrows#forNull() 
   */
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
  
  
  /**
   * Check if the object is a date before then the given argument. 
   * The object is first tested <code>forNull()</code> 
   * and then for the comparison.
   * @param dt The date argument to compare with the object.
   * @return A new instance of ValidThrows.
   * @throws E In case of validation error.
   * @see ValidThrows#forNull() 
   */
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
  
  
  /**
   * Check if the object is a date before or equals then the given argument. 
   * The object is first tested <code>forNull()</code> 
   * and then for the comparison.
   * @param dt The date argument to compare with the object.
   * @return A new instance of ValidThrows.
   * @throws E In case of validation error.
   * @see ValidThrows#forNull() 
   */
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
  
  
  /**
   * Check if the object is a date after then the given argument. 
   * The object is first tested <code>forNull()</code> 
   * and then for the comparison.
   * @param dt The date argument to compare with the object.
   * @return A new instance of ValidThrows.
   * @throws E In case of validation error.
   * @see ValidThrows#forNull() 
   */
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
  
  
  /**
   * Check if the object is a date after or equals then the given argument. 
   * The object is first tested <code>forNull()</code> 
   * and then for the comparison.
   * @param dt The date argument to compare with the object.
   * @return A new instance of ValidThrows.
   * @throws E In case of validation error.
   * @see ValidThrows#forNull() 
   */
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
  
  
  /**
   * Check if the object is a date NOT between the given interval 
   * <code>(start &lt;= obj &lt;= end)</code>. 
   * The object is first tested <code>forNull()</code> 
   * and then for the comparison.
   * @param start The start date of the interval.
   * @param end The end date of the interval.
   * @return A new instance of ValidThrows.
   * @throws E In case of validation error.
   * @see ValidThrows#forNull() 
   */
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
  
  
  /**
   * <code>MSG_NULL = "Invalid Null Object: %s";</code><br>
   * Default message for null test.
   */
  private static final String MSG_NULL = "Invalid Null Object: %s";

  /**
   * <code>MSG_EMPTY = "Invalid Empty %s: %s";</code><br>
   * Default message for empty test.
   */
  private static final String MSG_EMPTY = "Invalid Empty %s: %s";
  
  /**
   * <code>MSG_ARG = "Invalid Argument: %s=%s";</code><br>
   * Default message for invalid argument.
   */
  private static final String MSG_ARG = "Invalid Argument: %s=%s";
  
  /**
   * <code>MSG_TYPE = "Invalid %s: %s";</code><br>
   * Default message for invalid type.
   */
  private static final String MSG_TYPE = "Invalid %s: %s";
  
  /**
   * <code>MSG_TEST = "Test Not Passed for %s: %s";</code><br>
   * Default message for test not passed.
   */
  private static final String MSG_TEST = "Test Not Passed for %s: %s";
  
  /**
   * <code>MSG_INSTANCE = "%s is Not a Type of %s";</code><br>
   * Default message for invalid instance type.
   */
  private static final String MSG_INSTANCE = "%s is Not a Type of %s";
  
  /**
   * <code>MSG_EXC_TYPE = "Exception Type not has a <init>( String ) Constructor: %s";</code><br>
   * Default message for invalid exception type.
   */
  private static final String MSG_EXC_TYPE = "Exception Type not has a <init>( String ) Constructor: %s";
  
  /**
   * <code>MSG_LESSER = "Argument is Lesser Then %2$s (%1$s < %2$s)";</code><br>
   * Default message for argument lesser then.
   */
  private static final String MSG_LESSER = "Argument is Lesser Then %2$s (%1$s < %2$s)";
  
  /**
   * <code>MSG_LESSER_EQ = "Argument is Lesser Or Equals %2$s (%1$s <= %2$s)";</code><br>
   * Default message for argument lesser or equals then.
   */
  private static final String MSG_LESSER_EQ = "Argument is Lesser Or Equals %2$s (%1$s <= %2$s)";
  
  /**
   * <code>MSG_GREATER = "Argument is Greater Then %2$s (%1$s > %2$s)";</code><br>
   * Default message for argument greater then.
   */
  private static final String MSG_GREATER = "Argument is Greater Then %2$s (%1$s > %2$s)";
  
  /**
   * <code>MSG_GREATER_EQ = "Argument is Greater Or Equals %2$s (%1$s >= %2$s)";</code><br>
   * Default message for argument greater or equals then.
   */
  private static final String MSG_GREATER_EQ = "Argument is Greater Or Equals %2$s (%1$s >= %2$s)";
  
  /**
   * <code>MSG_BETWEEN = "%s is Not Between Range [%s..%s]";</code><br>
   * Default message for argument not between range.
   */
  private static final String MSG_BETWEEN = "%s is Not Between Range [%s..%s]";
  
  /**
   * <code>MSG_NOT_EQUALS = "%s is Not Equals %s (%1$s != %2$s)";</code><br>
   * Default message for argument not equals.
   */
  private static final String MSG_NOT_EQUALS = "%s is Not Equals %s (%1$s != %2$s)";


  
  
  /**
   * Unchecked Exception type for validation errors.
   */
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
