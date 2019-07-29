/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.bitbox.spec;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import us.pserver.bitbox.annotation.BitCreate;
import us.pserver.tools.Unchecked;


/**
 *
 * @author juno
 */
public interface ConstructorTarget<T> extends Function<Object[],T> {
  
  public List<Class> getParameterTypes();
  
  public List<String> getParameterNames();
  
  
  
  public static <U> ConstructorTarget<U> of(Constructor<U> cct, MethodHandles.Lookup lookup) {
    final MethodHandle cmh = Unchecked.call(() -> lookup.unreflectConstructor(cct));
    final List<String> names = getParameterNames(cct);
    return new ConstructorTarget<>() {
      public List<Class> getParameterTypes() {
        return Arrays.asList(cct.getParameterTypes());
      }
      public List<String> getParameterNames() {
        return names;
      }
      public U apply(Object[] args) { 
        //Logger.debug("args: {}", () -> Arrays.toString(args));
        return (U) Unchecked.call(() -> cmh.invokeWithArguments(Arrays.asList(args)));
      }
      public String toString() {
        return String.format("%s( %s )", cct.getName(), Arrays.toString(cct.getParameters()));
      }
    };
  }
  
  private static List<String> getParameterNames(Executable exc) {
    List<String> names;
    BitCreate abc = (BitCreate) exc.getAnnotation(BitCreate.class);
    if(abc != null && abc.value().length == exc.getParameterCount()) {
      names = Arrays.asList(abc.value());
    }
    else {
      names = Arrays.asList(exc.getParameters())
          .stream()
          .map(Parameter::getName)
          .collect(Collectors.toList());
    }
    return names;
  }
  
  public static <U> ConstructorTarget<U> of(Method mth, MethodHandles.Lookup lookup) {
    final MethodHandle cmh = Unchecked.call(() -> lookup.unreflect(mth));
    final List<String> names = getParameterNames(mth);
    return new ConstructorTarget<>() {
      public List<Class> getParameterTypes() {
        return Arrays.asList(mth.getParameterTypes());
      }
      public List<String> getParameterNames() {
        return names;
      }
      public U apply(Object[] args) { 
        //Logger.debug("args: {}", () -> Arrays.toString(args));
        return (U) Unchecked.call(() -> cmh.invokeWithArguments(Arrays.asList(args)));
      }
      public String toString() {
        return String.format("%s( %s )", mth.getName(), Arrays.toString(mth.getParameters()));
      }
    };
  }
  
}
