/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.bitbox.transform;

import java.lang.invoke.MethodHandle;
import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import us.pserver.bitbox.BoxRegistry;
import us.pserver.tools.Unchecked;


/**
 *
 * @author juno
 */
public interface ConstructorTarget<T> extends Function<Object[],T> {
  
  public List<Parameter> getParameters();
  
  
  
  public static <U> ConstructorTarget<U> of(Constructor<U> cct) {
    final MethodHandle cmh = Unchecked.call(() -> BoxRegistry.INSTANCE.lookup().unreflectConstructor(cct));
    return new ConstructorTarget<>() {
      public List<Parameter> getParameters() {
        return Arrays.asList(cct.getParameters());
      }
      public U apply(Object[] args) { 
        return (U) Unchecked.call(() -> cmh.invokeWithArguments(Arrays.asList(args)));
      }
      public String toString() {
        return String.format("%s( %s )", cct.getName(), Arrays.toString(cct.getParameters()));
      }
    };
  }
  
}
