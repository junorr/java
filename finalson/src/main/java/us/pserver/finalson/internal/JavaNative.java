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

package us.pserver.finalson.internal;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import us.pserver.finalson.tools.NotNull;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 09/12/2017
 */
public enum JavaNative implements Native {

  STRING(CharSequence.class::isAssignableFrom, o->new JsonPrimitive(o.toString())),
  
  NUMBER(Number.class::isAssignableFrom, o->new JsonPrimitive((Number)o)),
  
  INT(c->int.class == c || Integer.class.isAssignableFrom(c), o->new JsonPrimitive((Number)o)),
  
  SHORT(c->short.class == c || Short.class.isAssignableFrom(c), o->new JsonPrimitive((Number)o)),
  
  BYTE(c->byte.class == c || Byte.class.isAssignableFrom(c), o->new JsonPrimitive((Number)o)),
  
  CHAR(c->char.class == c || Character.class.isAssignableFrom(c), o->new JsonPrimitive((Number)o)),
  
  BOOLEAN(c->boolean.class == c || Boolean.class.isAssignableFrom(c), o->new JsonPrimitive((Number)o)),
  
  LONG(c->long.class == c || Long.class.isAssignableFrom(c), o->new JsonPrimitive((Number)o)),
  
  DOUBLE(c->double.class == c || Double.class.isAssignableFrom(c), o->new JsonPrimitive((Number)o)),
  
  FLOAT(c->float.class == c || Float.class.isAssignableFrom(c), o->new JsonPrimitive((Number)o)),
  
  BIG_DECIMAL(BigDecimal.class::isAssignableFrom, o->new JsonPrimitive((Number)o)),
  
  BIG_INTEGER(BigInteger.class::isAssignableFrom, o->new JsonPrimitive((Number)o));
  
  
  private JavaNative(Predicate<Class> prd, Function<Object,JsonElement> jfun) {
    this.is = prd;
    this.fun = jfun;
  }
  
  private final Predicate<Class> is;
  
  private final Function<Object,JsonElement> fun;
  
  @Override
  public boolean is(Class cls) {
    return is.test(cls);
  }
  
  @Override
  public JsonElement toJsonElement(Object obj) {
    if(!this.is(obj.getClass())) {
      throw new IllegalArgumentException("Not a "+ this.name());
    }
    return fun.apply(obj);
  }
  
  public static boolean isJavaPrimitive(Class cls) {
    return Arrays.asList(values()).stream().anyMatch(p->p.is(cls));
  }
  
  public static JavaNative of(Class cls) {
    NotNull.of(cls).failIfNull("Bad null Class");
    Optional<JavaNative> opt = Arrays.asList(values()).stream().filter(p->p.is(cls)).findAny();
    if(!opt.isPresent()) {
      throw new IllegalArgumentException(String.format("Class (%s) is not a Java primitive", cls.getName()));
    }
    return opt.get();
  }
  
  
  public static JsonElement primitiveToJson(Object obj) {
    Optional<JavaNative> opt = Arrays.asList(values()).stream()
        .filter(p->p.is(obj.getClass())).findAny();
    if(!opt.isPresent()) {
      throw new IllegalArgumentException(obj + " not a java primitive");
    }
    return opt.get().toJsonElement(obj);
  }
  
}
