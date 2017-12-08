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

package us.pserver.dbone.index;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;
import us.pserver.tools.Tuple;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 08/12/2017
 */
public class MethodHandleUtils {

    public static Optional<Method> getAnnotatedComparableMethod(Class cls, Class annotation) {
      return Arrays.asList(cls.getDeclaredMethods())
          .stream().filter(m->m.getAnnotation(annotation) != null 
              && m.getParameterCount() == 0 
              && (m.getReturnType().isPrimitive() 
              || Comparable.class.isAssignableFrom(m.getReturnType())))
          .findAny();
    }
    
    public static Optional<Field> getAnnotatedComparableField(Class cls, Class annotation) {
      return Arrays.asList(cls.getDeclaredFields())
          .stream().filter(f->f.getAnnotation(annotation) != null 
              && (f.getType().isPrimitive() 
              || Comparable.class.isAssignableFrom(f.getType())))
          .findAny();
    }
    
    public static Stream<Method> getAnnotatedComparableMethods(Class cls, Class annotation) {
      return Arrays.asList(cls.getDeclaredMethods())
          .stream().filter(m->m.getAnnotation(annotation) != null 
              && m.getParameterCount() == 0 
              && (m.getReturnType().isPrimitive() 
              || Comparable.class.isAssignableFrom(m.getReturnType())));
    }
    
    public static Stream<Field> getAnnotatedComparableFields(Class cls, Class annotation) {
      return Arrays.asList(cls.getDeclaredFields())
          .stream().filter(f->f.getAnnotation(annotation) != null 
              && (f.getType().isPrimitive() 
              || Comparable.class.isAssignableFrom(f.getType())));
    }
    
    public static Stream<MethodHandle> getAnnotatedMethodHandles(Class cls, Class annotation, MethodHandles.Lookup lookup) throws IllegalAccessException {
      return Stream.concat(
          getAnnotatedComparableMethods(cls, annotation).map(m->unreflect(lookup, m)),
          getAnnotatedComparableFields(cls, annotation).map(m->unreflect(lookup, m))
      );
    }
    
    public static MethodHandle unreflect(MethodHandles.Lookup lookup, Method meth) {
      try {
        return lookup.unreflect(meth);
      }
      catch(IllegalAccessException e) {
        throw new RuntimeException(e.toString(), e);
      }
    }
    
    public static MethodHandle unreflect(MethodHandles.Lookup lookup, Field field) {
      try {
        return lookup.unreflectGetter(field);
      }
      catch(IllegalAccessException e) {
        throw new RuntimeException(e.toString(), e);
      }
    }
    
    public static Stream<Tuple<String,MethodHandle>> getAnnotatedMethodHandlesWithName(Class cls, Class annotation, MethodHandles.Lookup lookup) throws IllegalAccessException {
      return Stream.concat(
          getAnnotatedComparableMethods(cls, annotation).map(m->unreflect(lookup, m)),
          getAnnotatedComparableFields(cls, annotation).map(m->unreflect(lookup, m))
      );
    }
    
    public static Optional<MethodHandle> getAnnotatedMethodHandle(Class cls, Class annotation, MethodHandles.Lookup lookup) throws IllegalAccessException {
      Optional<Method> oh = getAnnotatedComparableMethod(cls, annotation);
      Optional<MethodHandle> opt = Optional.empty();
      if(oh.isPresent()) {
        opt = Optional.of(lookup.unreflect(oh.get()));
      }
      else {
        Optional<Field> of = getAnnotatedComparableField(cls, annotation);
        if(of.isPresent()) {
          opt = Optional.of(lookup.unreflectGetter(of.get()));
        }
      }
      return opt;
    }
    
    public static Optional<Tuple<String,MethodHandle>> getAnnotatedMethodHandleWithName(Class cls, Class annotation, MethodHandles.Lookup lookup) throws IllegalAccessException {
      Optional<Method> oh = getAnnotatedComparableMethod(cls, annotation);
      Optional<Tuple<String,MethodHandle>> opt = Optional.empty();
      if(oh.isPresent()) {
        opt = Optional.of(new Tuple<>(oh.get().getName(), lookup.unreflect(oh.get())));
      }
      else {
        Optional<Field> of = getAnnotatedComparableField(cls, annotation);
        if(of.isPresent()) {
          opt = Optional.of(new Tuple<>(of.get().getName(), lookup.unreflectGetter(of.get())));
        }
      }
      return opt;
    }
    
    public static Optional<MethodHandle> getComparableMethodHandle(Class cls, String name, MethodHandles.Lookup lookup) throws IllegalAccessException {
      Optional<Method> om = Arrays.asList(cls.getDeclaredMethods())
          .stream().filter(m->m.getName().equals(name) 
              && m.getParameterCount() == 0 
              && (m.getReturnType().isPrimitive() 
              || Comparable.class.isAssignableFrom(m.getReturnType())))
          .findAny();
      Optional<MethodHandle> opt = Optional.empty();
      if(om.isPresent()) {
        opt = Optional.of(lookup.unreflect(om.get()));
      }
      return opt;
    }
    
    public static Optional<MethodHandle> getComparableFieldHandle(Class cls, String name, MethodHandles.Lookup lookup) throws IllegalAccessException {
      Optional<Field> of = Arrays.asList(cls.getDeclaredFields())
          .stream().filter(f->f.getName().equals(name) 
              && (f.getType().isPrimitive() 
              || Comparable.class.isAssignableFrom(f.getType())))
          .findAny();
      Optional<MethodHandle> opt = Optional.empty();
      if(of.isPresent()) {
        opt = Optional.of(lookup.unreflectGetter(of.get()));
      }
      return opt;
    }
    
}
