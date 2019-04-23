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
import java.util.stream.Collectors;
import us.pserver.bitbox.BitCreate;
import us.pserver.bitbox.BitProperty;
import us.pserver.bitbox.BitType;
import us.pserver.bitbox.BoxRegistry;
import us.pserver.tools.Reflect;
import us.pserver.bitbox.TypeMatching;
import us.pserver.bitbox.SerializedType;


/**
 *
 * @author juno
 */
public interface ObjectSpec<T> extends TypeMatching, SerializedType {
  
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
    
    private Optional<ConstructorTarget<T>> scanBitCreateConstructor(Reflect<T> ref, Set<GetterTarget<T,Object>> getters) {
      return ref.streamConstructors()
          //.peek(c -> System.out.println("scanBitCreateConstructor: " + c))
          .filter(c -> c.isAnnotationPresent(BitCreate.class))
          .filter(c -> c.getParameterCount() <= getters.size())
          .sorted((m,n) -> (-1) * Integer.compare(m.getParameterCount(), n.getParameterCount()))
          .map(c -> (ConstructorTarget<T>)ConstructorTarget.of(c))
          .findAny();
    }
    
    private Optional<ConstructorTarget<T>> scanBitCreateMethod(Reflect<T> ref, Set<GetterTarget<T,Object>> getters) {
      return ref.streamMethods()
          //.peek(m -> System.out.println("scanBitCreateMethod: " + m))
          .filter(m -> m.isAnnotationPresent(BitCreate.class))
          .filter(c -> c.getParameterCount() <= getters.size())
          .sorted((m,n) -> (-1) * Integer.compare(m.getParameterCount(), n.getParameterCount()))
          .map(m -> (ConstructorTarget<T>)ConstructorTarget.of(m))
          .findFirst();
    }
    
    private Optional<ConstructorTarget<T>> guessConstructor(Reflect<T> ref, Set<GetterTarget<T,Object>> getters) {
      return ref.streamConstructors()
          .filter(c -> c.getParameterCount() <= getters.size())
          //filter by parameter types
          .filter(c -> Arrays.asList(c.getParameterTypes()).stream()
              .allMatch(t -> getters.stream()
                  .anyMatch(g -> t.isAssignableFrom(g.getReturnType()))))
          //filter by parameter names
          .filter(c -> Arrays.asList(c.getParameters()).stream()
              .map(p -> p.getName())
              .allMatch(n -> getters.stream()
                  .anyMatch(g -> g.getName().equalsIgnoreCase(n))))
          //sort by parameter count descending
          .sorted((c,d) -> (-1) * Integer.compare(c.getParameterCount(), d.getParameterCount()))
          .map(c -> (ConstructorTarget<T>) ConstructorTarget.of(c))
          .findFirst();
    }
    
    private Optional<ConstructorTarget<T>> scanConstructor(Set<GetterTarget<T,Object>> getters) {
      Reflect<T> ref = Reflect.of(cls, BoxRegistry.INSTANCE.lookup());
      return scanBitCreateConstructor(ref, getters)
          .or(() -> scanBitCreateMethod(ref, getters))
          .or(() -> guessConstructor(ref, getters));
    }
    
    public ObjectSpec<T> build() {
      BitType bt = cls.getAnnotation(BitType.class);
      Optional<Class> opt = Optional.ofNullable(bt != null ? bt.value() : null);
      Set<GetterTarget<T,Object>> getters = this.scanGetters();
      ConstructorTarget<T> fct = this.scanConstructor(getters)
          .orElseThrow(() -> new IllegalStateException("Compatible constructor not found"));
      return new ObjectSpec<>() {
        public Optional<Class> serialType() { return opt; }
        public boolean match(Class c) { return Objects.equals(c, cls); }
        public ConstructorTarget<T> constructor() { return fct; }
        public Set<GetterTarget<T,Object>> getters() { return getters; }
      };
    }
    
  }
  
}
