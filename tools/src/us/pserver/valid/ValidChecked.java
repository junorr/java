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

package us.pserver.valid;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;


/**
 * Utility class for argument validations and exception throwing on fails.
 * @param <T> Type of the validated object.
 * @param <E> Type of the exception to be throwed on fail.
 * @author Juno Roesler - juno@pserver.us
 */
public class ValidChecked<T, E extends Exception> {

  private T obj;
  
  private String message;
  
  private boolean error, or, and;
  
  private ExceptionFactory fact;
  
  
  /**
   * Default constructor receives the object 
   * to validated and the type of the exception 
   * to be throwed on fail.
   * @param obj The object to be validated.
   * @param exc The type of the exception to be throwed on fail.
   */
  public ValidChecked(T obj, ExceptionFactory factory) {
    with(factory);
    on(obj);
    error = or = and = false;
  }
  
  
  /**
   * Creates a new ValidThrows object for the specified object and exception type.
   * @param <X> Type of the object.
   * @param <Y> Type of the exception.
   * @param obj The object to be validated.
   * @param exc The exception type to be throwed.
   * @return a new ValidThrows object.
   */
  public static <X,Y extends Exception> ValidChecked<X,Y> off(X object, ExceptionFactory factory) {
    return new ValidChecked(object, factory);
  }
  
  
  public ValidChecked<T,E> on(T obj) {
    this.obj = obj;
    return this;
  }
  
  
  public ValidChecked<T,E> with(ExceptionFactory factory) {
    if(factory == null) {
      throw new IllegalArgumentException("Invalid ExceptionFactory: "+ factory);
    }
    fact = factory;
    return this;
  }
  
  
  /**
   * Configure an error message for the exception on fail.
   * @param msg an error message for the exception on fail.
   * @return a new configured ValidThrows object.
   */
  private ValidChecked<T,E> message(String msg) {
    if(msg == null || msg.isEmpty()) {
      throw new ValidatorException(MSG_ARG, "msg", msg);
    }
    message = msg;
    if(msg.trim().endsWith(":")) {
      message = f("%s %s", msg.trim(), obj);
    }
    return this;
  }
  
  
  /**
   * Configure an error message for the exception on fail.
   * @param msg an error message for the exception on fail.
   * @param args Objects to be interpolated in the string message.
   * @return a new configured ValidThrows object.
   */
  private ValidChecked<T,E> message(String msg, Object ... args) {
    if(msg == null || msg.isEmpty()) {
      throw new ValidatorException(MSG_ARG, "msg", msg);
    }
    return message(f(msg, args));
  }
  
  
  /**
   * Configure an error message with the class name.
   * @param cls the class which name will compose the message.
   * @return a new configured ValidThrows object.
   */
  private ValidChecked<T,E> message(Class cls) {
    if(cls == null) {
      throw new ValidatorException(MSG_ARG, "cls", cls);
    }
    return message(MSG_TYPE, cls.getSimpleName(), obj);
  }
  
  
  /**
   * Get the object used in validations.
   * @return the object used in validations.
   */
  public T object() {
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
  public ExceptionFactory exceptionFactory() {
    return fact;
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
   * Throw the configured exception.
   * @throws E The exception to be thrown.
   */
  public void throwException(String msg) throws E {
    throw (E) fact.create(msg);
  }
  
  
  public ValidChecked<T,E> or() {
    or = true;
    return this;
  }
  
  
  public ValidChecked<T,E> and() {
    and = true;
    return this;
  }
  
  
  /**
   * Check for error, throwing the exception if true.
   * @return This instance of ValidThrows.
   * @throws E The exception to be thrown in case of error.
   */
  public ValidChecked<T,E> fail() throws E {
    try {
      if(error) throwException(message);
    } finally {
      message = null;
    }
    return this;
  }
  
  
  /**
   * Check for error, throwing the exception if true.
   * @param msg The exception message.
   * @return This instance of ValidThrows.
   * @throws E The exception to be thrown in case of error.
   */
  public ValidChecked<T,E> fail(String msg) throws E {
    return this.message(msg).fail();
  }
  
  
  /**
   * Check for error, throwing the exception if true.
   * @param msg The exception message.
   * @param args Objects to be interpolated in the message.
   * @return This instance of ValidThrows.
   * @throws E The exception to be thrown in case of error.
   */
  public ValidChecked<T,E> fail(String msg, Object ... args) throws E {
    return this.message(msg, args).fail();
  }
  
  
  /**
   * Check for error, throwing the exception if true.
   * @param cls The Class name in the exception message.
   * @return This instance of ValidThrows.
   * @throws E The exception to be thrown in case of error.
   */
  public ValidChecked<T,E> fail(Class cls) throws E {
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
  
  
  private void error(boolean b) {
    if(or) {
      error = error || b;
      or = false;
    }
    else if(and) {
      error = error && b;
      and = false;
    }
    else {
      error = b;
    }
  }
  
  
  private void msg(String msg) {
    if(message == null && msg != null) {
      message = msg;
    }
  }
  
  
  /**
   * Check if the object is null.
   * @return A new instance of ValidThrows.
   */
  public ValidChecked<T,E> forNull() {
    error(obj == null);
    msg(MSG_NULL);
    return this;
  }
  
  
  /**
   * Check if the object is empty. The object is first 
   * tested <code>forNull()</code> and than for empty 
   * objects like String, Arrays and Lists.
   * @return A new instance of ValidThrows.
   * @throws E In case of validation error.
   * @see ValidThrows#forNull() 
   */
  public ValidChecked<T,E> forEmpty() throws E {
    forNull().fail();
    if(obj.getClass().isArray()) {
      error(Array.getLength(obj) < 1);
    }
    else if(List.class.isAssignableFrom(obj.getClass())
        && ((List)obj).isEmpty()) {
      error(((List)obj).isEmpty());
    }
    else if(Map.class.isAssignableFrom(obj.getClass())
        && ((Map)obj).isEmpty()) {
      error(((Map)obj).isEmpty());
    }
    else {
      error(Objects.toString(obj).isEmpty());
    }
    message(MSG_EMPTY);
    return this;
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
  public ValidChecked<T,E> forTest(boolean test) throws E {
    forNull().fail();
    error(test);
    msg(MSG_TEST);
    return this;
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
  public ValidChecked<T,E> forTypeMatch(Class cls) throws E {
    forNull().fail();
    if(cls == null) {
      throw new ValidatorException(MSG_ARG, "cls", cls);
    }
    error(!cls.isAssignableFrom(obj.getClass()));
    msg(MSG_TYPE);
    return this;
  }
  
  
  /**
   * Check if the object is a number lesser than the given argument. 
   * The object is first tested <code>forNull()</code> 
   * and then for the comparison.
   * @param n The number to be compared.
   * @return A new instance of ValidThrows.
   * @throws E In case of validation error.
   * @see ValidThrows#forNull() 
   */
  public ValidChecked<T,E> forLesserThan(Number n) throws E {
    forNull().fail()
        .forTypeMatch(Number.class)
        .fail();
    if(n == null) {
      throw new ValidatorException(MSG_ARG, "n", n);
    }
    BigDecimal bo = new BigDecimal(((Number)obj).doubleValue());
    BigDecimal bn = new BigDecimal(n.doubleValue());
    error(bo.compareTo(bn) < 0);
    msg(MSG_LESSER);
    return this;
  }
  
  
  /**
   * Check if the object is a number lesser or equals than the given argument. 
   * The object is first tested <code>forNull()</code> 
   * and then for the comparison.
   * @param n The number to be compared.
   * @return A new instance of ValidThrows.
   * @throws E In case of validation error.
   * @see ValidThrows#forNull() 
   */
  public ValidChecked<T,E> forLesserEquals(Number n) throws E {
    forNull().fail()
        .forTypeMatch(Number.class)
        .fail();
    if(n == null) {
      throw new ValidatorException(MSG_ARG, "n", n);
    }
    BigDecimal bo = new BigDecimal(((Number)obj).doubleValue());
    BigDecimal bn = new BigDecimal(n.doubleValue());
    error(bo.compareTo(bn) <= 0);
    msg(MSG_LESSER_EQ);
    return this;
  }
  
  
  /**
   * Check if the object is a number greater than the given argument. 
   * The object is first tested <code>forNull()</code> 
   * and then for the comparison.
   * @param n The number to be compared.
   * @return A new instance of ValidThrows.
   * @throws E In case of validation error.
   * @see ValidThrows#forNull() 
   */
  public ValidChecked<T,E> forGreaterThan(Number n) throws E {
    forNull().fail()
        .forTypeMatch(Number.class)
        .fail();
    if(n == null) {
      throw new ValidatorException(MSG_ARG, "n", n);
    }
    BigDecimal bo = new BigDecimal(((Number)obj).doubleValue());
    BigDecimal bn = new BigDecimal(n.doubleValue());
    error(bo.compareTo(bn) > 0);
    msg(MSG_GREATER);
    return this;
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
  public ValidChecked<T,E> forNotEquals(Object o) throws E {
    if(o == null) {
      throw new ValidatorException(MSG_ARG, "o", o);
    }
    forNull().fail();
    error(!obj.equals(o));
    msg(MSG_NOT_EQUALS);
    return this;
  }
  
  
  /**
   * Invert the validation test.
   * @return A new ValidThrows configured instance.
   */
  public ValidChecked<T,E> not() {
    error = !error;
    return this;
  }
  
  
  /**
   * Check if the object is a number greater or equals than the given argument. 
   * The object is first tested <code>forNull()</code> 
   * and then for the comparison.
   * @param n The number to be compared.
   * @return A new instance of ValidThrows.
   * @throws E In case of validation error.
   * @see ValidThrows#forNull() 
   */
  public ValidChecked<T,E> forGreaterEquals(Number n) throws E {
    forNull().fail()
        .forTypeMatch(Number.class)
        .fail();
    if(n == null) {
      throw new ValidatorException(MSG_ARG, "n", n);
    }
    BigDecimal bo = new BigDecimal(((Number)obj).doubleValue());
    BigDecimal bn = new BigDecimal(n.doubleValue());
    error(bo.compareTo(bn) >= 0);
    msg(MSG_GREATER_EQ);
    return this;
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
  public ValidChecked<T,E> forNotBetween(Number start, Number end) throws E {
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
    error(bo.compareTo(bs) < 0 || bo.compareTo(be) > 0);
    msg(MSG_BETWEEN);
    return this;
  }
  
  
  /**
   * Check if the object is a date before than the given argument. 
   * The object is first tested <code>forNull()</code> 
   * and then for the comparison.
   * @param dt The date argument to compare with the object.
   * @return A new instance of ValidThrows.
   * @throws E In case of validation error.
   * @see ValidThrows#forNull() 
   */
  public ValidChecked<T,E> forLesserThan(Date dt) throws E {
    forNull().fail()
        .forTypeMatch(Date.class)
        .fail();
    if(dt == null) {
      throw new ValidatorException(MSG_ARG, "d", dt);
    }
    Date date = ((Date)obj);
    error(date.before(dt));
    msg(MSG_LESSER);
    return this;
  }
  
  
  /**
   * Check if the object is a date before or equals than the given argument. 
   * The object is first tested <code>forNull()</code> 
   * and then for the comparison.
   * @param dt The date argument to compare with the object.
   * @return A new instance of ValidThrows.
   * @throws E In case of validation error.
   * @see ValidThrows#forNull() 
   */
  public ValidChecked<T,E> forLesserEquals(Date dt) throws E {
    forNull().fail()
        .forTypeMatch(Number.class)
        .fail();
    if(dt == null) {
      throw new ValidatorException(MSG_ARG, "dt", dt);
    }
    Date date = ((Date)obj);
    error(date.before(dt) || date.equals(dt));
    msg(MSG_LESSER_EQ);
    return this;
  }
  
  
  /**
   * Check if the object is a date after than the given argument. 
   * The object is first tested <code>forNull()</code> 
   * and then for the comparison.
   * @param dt The date argument to compare with the object.
   * @return A new instance of ValidThrows.
   * @throws E In case of validation error.
   * @see ValidThrows#forNull() 
   */
  public ValidChecked<T,E> forGreaterThan(Date dt) throws E {
    forNull().fail()
        .forTypeMatch(Number.class)
        .fail();
    if(dt == null) {
      throw new ValidatorException(MSG_ARG, "dt", dt);
    }
    Date date = ((Date)obj);
    error(date.after(dt));
    msg(MSG_GREATER);
    return this;
  }
  
  
  /**
   * Check if the object is a date after or equals than the given argument. 
   * The object is first tested <code>forNull()</code> 
   * and then for the comparison.
   * @param dt The date argument to compare with the object.
   * @return A new instance of ValidThrows.
   * @throws E In case of validation error.
   * @see ValidThrows#forNull() 
   */
  public ValidChecked<T,E> forGreaterEquals(Date dt) throws E {
    forNull().fail()
        .forTypeMatch(Number.class)
        .fail();
    if(dt == null) {
      throw new ValidatorException(MSG_ARG, "dt", dt);
    }
    Date date = ((Date)obj);
    error(date.after(dt) || date.equals(dt));
    msg(MSG_GREATER_EQ);
    return this;
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
  public ValidChecked<T,E> forNotBetween(Date start, Date end) throws E {
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
    error(date.before(start) || date.after(end));
    msg(MSG_BETWEEN);
    return this;
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
   * <code>MSG_LESSER = "Argument is Lesser Than %2$s (%1$s < %2$s)";</code><br>
   * Default message for argument lesser than.
   */
  private static final String MSG_LESSER = "Argument is Lesser Than %2$s (%1$s < %2$s)";
  
  /**
   * <code>MSG_LESSER_EQ = "Argument is Lesser Or Equals %2$s (%1$s <= %2$s)";</code><br>
   * Default message for argument lesser or equals than.
   */
  private static final String MSG_LESSER_EQ = "Argument is Lesser Or Equals %2$s (%1$s <= %2$s)";
  
  /**
   * <code>MSG_GREATER = "Argument is Greater Than %2$s (%1$s > %2$s)";</code><br>
   * Default message for argument greater than.
   */
  private static final String MSG_GREATER = "Argument is Greater Than %2$s (%1$s > %2$s)";
  
  /**
   * <code>MSG_GREATER_EQ = "Argument is Greater Or Equals %2$s (%1$s >= %2$s)";</code><br>
   * Default message for argument greater or equals than.
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
