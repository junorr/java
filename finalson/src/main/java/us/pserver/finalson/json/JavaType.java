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

package us.pserver.finalson.json;

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
public enum JavaType implements JsonType {

  STRING(CharSequence.class::isAssignableFrom, o->new JsonPrimitive(o.toString()), JsonElement::getAsString),
  
  NUMBER(Number.class::isAssignableFrom, o->new JsonPrimitive((Number)o), JsonElement::getAsDouble),
  
  INT(c->int.class == c || Integer.class.isAssignableFrom(c), o->new JsonPrimitive((Number)o), JsonElement::getAsInt),
  
  SHORT(c->short.class == c || Short.class.isAssignableFrom(c), o->new JsonPrimitive((Number)o), JsonElement::getAsShort),
  
  BYTE(c->byte.class == c || Byte.class.isAssignableFrom(c), o->new JsonPrimitive((Number)o), JsonElement::getAsByte),
  
  CHAR(c->char.class == c || Character.class.isAssignableFrom(c), o->new JsonPrimitive((Number)o), e->e.getAsString().charAt(0)),
  
  BOOLEAN(c->boolean.class == c || Boolean.class.isAssignableFrom(c), o->new JsonPrimitive((Number)o), JsonElement::getAsBoolean),
  
  LONG(c->long.class == c || Long.class.isAssignableFrom(c), o->new JsonPrimitive((Number)o), JsonElement::getAsLong),
  
  DOUBLE(c->double.class == c || Double.class.isAssignableFrom(c), o->new JsonPrimitive((Number)o), JsonElement::getAsDouble),
  
  FLOAT(c->float.class == c || Float.class.isAssignableFrom(c), o->new JsonPrimitive((Number)o), JsonElement::getAsFloat),
  
  BIG_DECIMAL(BigDecimal.class::isAssignableFrom, o->new JsonPrimitive((Number)o), JsonElement::getAsBigDecimal),
  
  BIG_INTEGER(BigInteger.class::isAssignableFrom, o->new JsonPrimitive((Number)o), JsonElement::getAsBigInteger);
  
  
  private JavaType(Predicate<Class> prd, Function<Object,JsonElement> to, Function<JsonElement,Object> from) {
    this.is = prd;
    this.to = to;
    this.from = from;
  }
  
  private final Predicate<Class> is;
  
  private final Function<Object,JsonElement> to;
  
  private final Function<JsonElement,Object> from;
  
  @Override
  public boolean is(Class cls) {
    return is.test(cls);
  }
  
  @Override
  public JsonElement toJson(Object obj) {
    if(!this.is(obj.getClass())) {
      throw new IllegalArgumentException("Not a "+ this.name());
    }
    return to.apply(obj);
  }
  
  @Override
  public Object fromJson(JsonElement elt) {
    return from.apply(NotNull.of(elt).getOrFail("Bad null JsonElement"));
  }
  
  public static boolean isJavaPrimitive(Class cls) {
    return Arrays.asList(values()).stream().anyMatch(p->p.is(cls));
  }
  
  public static JavaType of(Class cls) {
    NotNull.of(cls).failIfNull("Bad null Class");
    Optional<JavaType> opt = Arrays.asList(values()).stream().filter(p->p.is(cls)).findAny();
    if(!opt.isPresent()) {
      throw new IllegalArgumentException(String.format("Class (%s) is not a Java primitive", cls.getName()));
    }
    return opt.get();
  }
  
  
  public static JsonElement javaToJson(Object obj) {
    Optional<JavaType> opt = Arrays.asList(values()).stream()
        .filter(p->p.is(obj.getClass())).findAny();
    if(!opt.isPresent()) {
      throw new IllegalArgumentException(obj + " not a java primitive");
    }
    return opt.get().toJson(obj);
  }
  
}
