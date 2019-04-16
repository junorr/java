/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.bitbox.transform;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import us.pserver.bitbox.BitProperty;
import us.pserver.bitbox.MatchingType;
import us.pserver.tools.Reflect;


/**
 *
 * @author juno
 */
public interface ObjectSchema<T> extends MatchingType {
  
  public Function<Object[],T> constructor();
  
  public Set<GetterTarget<T,?>> getters();
  
  
  
  public static <U> ObjectSchema<U> createSchema(Class<U> cls) {
    return new ObjectSchemaBuilder<>(cls).build();
  }
  
  
  
  
  
  public static class ObjectSchemaBuilder<T> {
    
    public static final String GET = "get";
    
    private final Class<T> cls;
    
    public ObjectSchemaBuilder(Class<T> cls) {
      this.cls = Objects.requireNonNull(cls);
    }
    
    private boolean isGetter(Method m) {
      return m.getName().startsWith(GET)
          || m.getAnnotation(BitProperty.class) != null;
    }
    
    private String getPropertyName(Method m) {
      String name = m.getName();
      if(name.startsWith(GET)) {
        return name.substring(3);
      }
      else {
        BitProperty bp = m.getAnnotation(BitProperty.class);
        return bp.value().isBlank() ? name : bp.value();
      }
    }
    
    private GetterTarget<T,?> toGetterTarget(Method m) {
      return GetterTarget.of(
          getPropertyName(m), m.getReturnType(),
          Reflect.of(cls).selectMethod(m).dynamicSupplierMethod()
      );
    }
    
    private Set<GetterTarget<T,?>> scanGetters() {
      return Reflect.of(cls).streamMethods()
          .filter(m -> m.getParameterCount() == 0)
          .filter(m -> m.getReturnType() != void.class)
          .filter(this::isGetter)
          .map(this::toGetterTarget)
          .collect(Collectors.toSet());
    }
    
    private Function<Object[],T> toConstructorFunction(Constructor c) {
      return os -> Reflect.of(cls).selectConstructor(c).create(os);
    }
    
    private Optional<Function<Object[],T>> scanConstructor(Set<GetterTarget<T,?>> getters) {
      return Reflect.of(cls).streamConstructors()
          .filter(c -> c.getParameterCount() <= getters.size())
          //filter by parameter types
          .filter(c -> Arrays.asList(c.getParameterTypes()).stream()
              .allMatch(t -> getters.stream()
                  .anyMatch(g -> t.isAssignableFrom(g.getReturnType()))))
          //filter by parameter names
          .filter(c -> Arrays.asList(c.getParameters()).stream()
              .allMatch(p -> getters.stream()
                  .anyMatch(g -> g.getName().equalsIgnoreCase(p.getName()))))
          //sort by parameter count descending
          .sorted((c,d) -> (-1) * Integer.compare(c.getParameterCount(), d.getParameterCount()))
          .map(this::toConstructorFunction)
          .findFirst();
    }
    
    public ObjectSchema<T> build() {
      Set<GetterTarget<T,?>> getters = this.scanGetters();
      Function<Object[],T> fct = this.scanConstructor(getters)
          .orElseThrow(() -> new IllegalStateException("Compatible constructor not found"));
      return new ObjectSchema<>() {
        public Predicate<Class> matching() { return Predicate.isEqual(cls); }
        public Function<Object[],T> constructor() { return fct; }
        public Set<GetterTarget<T,?>> getters() { return getters; }
      };
    }
    
  }
  
}
