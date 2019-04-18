/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.bitbox.transform;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import us.pserver.bitbox.BitProperty;
import us.pserver.bitbox.BoxRegistry;
import us.pserver.tools.Reflect;
import us.pserver.bitbox.TypeMatching;


/**
 *
 * @author juno
 */
public interface ObjectSpec<T> extends TypeMatching {
  
  public ConstructorTarget<T> constructor();
  
  public Set<GetterTarget<T,Object>> getters();
  
  
  
  public static <U> ObjectSpec<U> createSpec(Class<U> cls) {
    return new ObjectSpecBuilder<>(cls).build();
  }
  
  
  
  
  
  public static class ObjectSpecBuilder<T> {
    
    public static final String GET = "get";
    
    private final Class<T> cls;
    
    public ObjectSpecBuilder(Class<T> cls) {
      this.cls = Objects.requireNonNull(cls);
    }
    
    private boolean isGetter(Method m) {
      return (m.getName().startsWith(GET)
          && !m.getName().endsWith("Class"))
          || m.getAnnotation(BitProperty.class) != null;
    }
    
    private String getPropertyName(Method m) {
      String name = m.getName();
      if(name.startsWith(GET)) {
        return name.substring(3, 4).toLowerCase() + name.substring(4);
      }
      else {
        BitProperty bp = m.getAnnotation(BitProperty.class);
        return bp.value().isBlank() ? name : bp.value();
      }
    }
    
    private GetterTarget<T,Object> toGetterTarget(Method m) {
      return (GetterTarget<T,Object>) GetterTarget.of(
          getPropertyName(m), m.getReturnType(),
          Reflect.of(cls, BoxRegistry.INSTANCE.lookup()).selectMethod(m).dynamicSupplierMethod()
      );
    }
    
    private Set<GetterTarget<T,Object>> scanGetters() {
      return Reflect.of(cls, BoxRegistry.INSTANCE.lookup()).streamMethods()
          .filter(m -> m.getParameterCount() == 0)
          .filter(m -> m.getReturnType() != void.class)
          .filter(this::isGetter)
          .map(this::toGetterTarget)
          .collect(Collectors.toSet());
    }
    
    private Optional<ConstructorTarget<T>> scanConstructor(Set<GetterTarget<T,Object>> getters) {
      return Reflect.of(cls, BoxRegistry.INSTANCE.lookup()).streamConstructors()
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
          .map(c -> (ConstructorTarget<T>) ConstructorTarget.of(c))
          .findFirst();
    }
    
    public ObjectSpec<T> build() {
      Set<GetterTarget<T,Object>> getters = this.scanGetters();
      ConstructorTarget<T> fct = this.scanConstructor(getters)
          .orElseThrow(() -> new IllegalStateException("Compatible constructor not found"));
      return new ObjectSpec<>() {
        public Predicate<Class> matching() { return Predicate.isEqual(cls); }
        public ConstructorTarget<T> constructor() { return fct; }
        public Set<GetterTarget<T,Object>> getters() { return getters; }
      };
    }
    
  }
  
}
