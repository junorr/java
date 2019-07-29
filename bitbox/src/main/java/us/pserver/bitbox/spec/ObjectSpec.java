/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.bitbox.spec;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import us.pserver.bitbox.BitBoxConfiguration;
import us.pserver.bitbox.annotation.BitCreate;
import us.pserver.bitbox.annotation.BitIgnore;
import us.pserver.bitbox.annotation.BitProperty;
import us.pserver.bitbox.annotation.BitType;
import us.pserver.bitbox.type.SerializedType;
import us.pserver.bitbox.type.TypeMatching;
import us.pserver.tools.Reflect;


/**
 *
 * @author juno
 */
public interface ObjectSpec<T> extends TypeMatching, SerializedType {
  
  public ConstructorTarget<T> constructor();
  
  public Set<GetterTarget<T,Object>> getters();
  
  
  
  public static <U> ObjectSpec<U> createSpec(Class<U> cls, BitBoxConfiguration cfg) {
    return new ObjectSpecBuilder<>(cls, cfg).build();
  }
  
  
  
  
  
  public static class ObjectSpecBuilder<T> {
    
    public static final String GET = "get";
    
    private final Class<T> cls;
    
    private final BitBoxConfiguration cfg;
    
    public ObjectSpecBuilder(Class<T> cls, BitBoxConfiguration cfg) {
      this.cfg = Objects.requireNonNull(cfg);
      Objects.requireNonNull(cls);
      if(cls.isAnnotationPresent(BitType.class)) {
        BitType type = cls.getAnnotation(BitType.class);
        this.cls = type.value();
      }
      else {
        this.cls = cls;
      }
    }
    
    private boolean isGetter(Method m) {
      return ((m.getName().startsWith(GET)
          && !m.getName().endsWith("Class"))
          || m.isAnnotationPresent(BitProperty.class))
          && !m.isAnnotationPresent(BitIgnore.class);
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
      return (GetterTarget<T,Object>) GetterTarget.of(getPropertyName(m), m.getReturnType(),
          Reflect.of(cls, cfg.lookup()).selectMethod(m).dynamicSupplierMethod()
      );
    }
    
    private Set<GetterTarget<T,Object>> scanGetters() {
      return Reflect.of(cls, cfg.lookup()).streamMethods()
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
          //.peek(c -> Logger.debug("@BitCreate present!  {}", c))
          .filter(c -> !c.isAnnotationPresent(BitIgnore.class))
          //.peek(c -> Logger.debug("@BitIgnore NOT present!  {}", c))
          .filter(c -> c.getParameterCount() <= getters.size())
          //.peek(c -> Logger.debug("Parameter count match  {}", c))
          .sorted((m,n) -> (-1) * Integer.compare(m.getParameterCount(), n.getParameterCount()))
          .map(c -> (ConstructorTarget<T>)ConstructorTarget.of(c, cfg.lookup()))
          .findAny();
    }
    
    private Optional<ConstructorTarget<T>> scanBitCreateMethod(Reflect<T> ref, Set<GetterTarget<T,Object>> getters) {
      return ref.streamMethods()
          //.peek(m -> System.out.println("scanBitCreateMethod: " + m))
          .filter(m -> m.isAnnotationPresent(BitCreate.class))
          //.peek(m -> Logger.debug("@BitCreate present!  {}", m))
          .filter(m -> !m.isAnnotationPresent(BitIgnore.class))
          //.peek(m -> Logger.debug("@BitIgnore NOT present!  {}", m))
          .filter(m -> m.getParameterCount() <= getters.size())
          //.peek(m -> Logger.debug("Parameter count match  {}", m))
          .sorted((m,n) -> (-1) * Integer.compare(m.getParameterCount(), n.getParameterCount()))
          .map(m -> (ConstructorTarget<T>)ConstructorTarget.of(m, cfg.lookup()))
          .findFirst();
    }
    
    private Optional<ConstructorTarget<T>> guessConstructor(Reflect<T> ref, Set<GetterTarget<T,Object>> getters) {
      return ref.streamConstructors()
          .filter(c -> !c.isAnnotationPresent(BitIgnore.class))
          //.peek(c -> Logger.debug("No BitIgnore: {}", c))
          .filter(c -> c.getParameterCount() <= getters.size())
          //filter by parameter types
          //.peek(c -> Logger.debug("Param count match: {}", c))
          .filter(c -> Arrays.asList(c.getParameterTypes()).stream()
              .allMatch(t -> getters.stream()
                  .anyMatch(g -> t.isAssignableFrom(g.getReturnType()))))
          //.peek(c -> Logger.debug("Types match: {}", c))
          //sort by parameter count descending
          .sorted((c,d) -> (-1) * Integer.compare(c.getParameterCount(), d.getParameterCount()))
          .map(c -> (ConstructorTarget<T>) ConstructorTarget.of(c, cfg.lookup()))
          .findFirst();
    }
    
    private Optional<ConstructorTarget<T>> scanConstructor(Set<GetterTarget<T,Object>> getters) {
      Reflect<T> ref = Reflect.of(cls, cfg.lookup());
      return scanBitCreateConstructor(ref, getters)
          .or(() -> scanBitCreateMethod(ref, getters))
          .or(() -> guessConstructor(ref, getters));
    }
    
    public ObjectSpec<T> build() {
      Set<GetterTarget<T,Object>> getters = this.scanGetters();
      //Logger.debug("getters: {}", getters);
      ConstructorTarget<T> fct = this.scanConstructor(getters)
          .orElseThrow(() -> new IllegalStateException("No compatible constructor found"));
      //Logger.debug("construct: {}, getters: {}", fct, getters);
      return new ObjectSpec<>() {
        public Optional<Class> serialType() { return Optional.of(cls); }
        public boolean match(Class c) { return Objects.equals(c, cls); }
        public ConstructorTarget<T> constructor() { return fct; }
        public Set<GetterTarget<T,Object>> getters() { return getters; }
      };
    }
    
  }
  
}
