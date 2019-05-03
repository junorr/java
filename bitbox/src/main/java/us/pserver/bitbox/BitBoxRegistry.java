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

package us.pserver.bitbox;

import java.lang.invoke.MethodHandles;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Predicate;
import us.pserver.bitbox.spec.ObjectSpec;
import us.pserver.bitbox.transform.*;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 11 de abr de 2019
 */
public enum BitBoxRegistry {
  
  INSTANCE;
  
  private BitBoxRegistry() {
    this.transforms = new CopyOnWriteArraySet<>();
    this.specs = new CopyOnWriteArraySet<>();
    this.btlisteners = new CopyOnWriteArraySet<>();
    this.splisteners = new CopyOnWriteArraySet<>();
    this.global = new AtomicReference<>(new GlobalObjectTransform());
    this.lookup = new AtomicReference<>(MethodHandles.lookup());
    this.initTransforms();
  }
  
  private final Set<BitTransform> transforms;
  
  private final Set<ObjectSpec> specs;
  
  private final Set<BitTransformListener> btlisteners;
  
  private final Set<ObjectSpecListener> splisteners;
  
  private final AtomicReference<BitTransform> global;
  
  private final AtomicReference<MethodHandles.Lookup> lookup;
  
  
  private void initTransforms() {
    transforms.add(new ArrayTransform());
    transforms.add(new BooleanTransform());
    transforms.add(new ByteArrayTransform());
    transforms.add(new ByteTransform());
    transforms.add(new CharArrayTransform());
    transforms.add(new CharSequenceTransform());
    transforms.add(new ClassTransform());
    transforms.add(new CollectionTransform());
    transforms.add(new DoubleArrayTransform());
    transforms.add(new DoubleTransform());
    transforms.add(new EnumTransform());
    transforms.add(new FloatArrayTransform());
    transforms.add(new FloatTransform());
    transforms.add(new InetAddressTransform());
    transforms.add(new InstantTransform());
    transforms.add(new IntArrayTransform());
    transforms.add(new IntTransform());
    transforms.add(new ListTransform());
    transforms.add(new LocalDateTimeTransform());
    transforms.add(new LocalDateTransform());
    transforms.add(new LongArrayTransform());
    transforms.add(new LongTransform());
    transforms.add(new MapTransform());
    transforms.add(new SetTransform());
    transforms.add(new ShortArrayTransform());
    transforms.add(new ShortTransform());
    transforms.add(new VoidTransform());
    transforms.add(new ZonedDateTimeTransform());
  }
  
  
  public MethodHandles.Lookup lookup() {
    return lookup.get();
  }
  
  
  public BitBoxRegistry lookup(MethodHandles.Lookup lookup) {
    this.lookup.set(Objects.requireNonNull(lookup));
    return this;
  }
  
  
  public BitBoxRegistry addTransformListener(Class type, Consumer<BitTransform> cs) {
    btlisteners.add(new BitTransformListener(type, cs));
    return this;
  }
  
  
  public BitBoxRegistry addTransformListener(Consumer<BitTransform> cs) {
    btlisteners.add(new BitTransformListener(null, cs));
    return this;
  }
  
  
  public BitBoxRegistry addSpecListener(Class type, Consumer<ObjectSpec> cs) {
    splisteners.add(new ObjectSpecListener(type, cs));
    return this;
  }
  
  
  public BitBoxRegistry addSpecListener(Consumer<ObjectSpec> cs) {
    splisteners.add(new ObjectSpecListener(null, cs));
    return this;
  }
  
  
  public boolean removeTransformListener(Class type, Consumer<BitTransform> cs) {
    return btlisteners.remove(new BitTransformListener(type, cs));
  }
  
  
  public boolean removeTransformListener(Consumer<BitTransform> cs) {
    return btlisteners.remove(new BitTransformListener(null, cs));
  }
  
  
  public BitBoxRegistry clearTransformListeners() {
    btlisteners.clear();
    return this;
  }
  
  
  public boolean removeSpecListener(Class type, Consumer<ObjectSpec> cs) {
    return splisteners.remove(new ObjectSpecListener(type, cs));
  }
  
  
  public boolean removeSpecListener(Consumer<ObjectSpec> cs) {
    return splisteners.remove(new ObjectSpecListener(null, cs));
  }
  
  
  public BitBoxRegistry clearSpecListeners() {
    splisteners.clear();
    return this;
  }
  
  
  public boolean containsSpec(Class cls) {
    return specs.stream()
        .anyMatch(s -> s.match(cls));
  }
  
  
  public <T> Optional<ObjectSpec<T>> specFor(Class<T> cls) {
    return specs.stream()
        .filter(s -> s.match(cls))
        .map(s -> (ObjectSpec<T>)s)
        .findAny();
  }
  
  
  public BitBoxRegistry addSpec(ObjectSpec o) {
    specs.add(Objects.requireNonNull(o));
    return this;
  }
  
  
  public BitBoxRegistry replaceSpec(Class c, ObjectSpec o) {
    Optional<ObjectSpec> opt = specs.stream().filter(s -> s.match(c)).findAny();
    opt.ifPresent(specs::remove);
    specs.add(Objects.requireNonNull(o));
    return this;
  }
  
  
  public BitBoxRegistry removeSpec(Class c) {
    Optional<ObjectSpec> opt = specs.stream().filter(s -> s.match(c)).findAny();
    opt.ifPresent(specs::remove);
    return this;
  }
  
  
  public BitBoxRegistry removeSpec(ObjectSpec os) {
    specs.remove(os);
    return this;
  }
  
  
  public BitBoxRegistry clearSpecs() {
    specs.clear();
    return this;
  }
  
  
  public boolean containsTransform(Class cls) {
    return transforms.stream()
        .anyMatch(t -> t.match(cls));
  }
  
  
  public <T> BitTransform<T> getAnyTransform(Class<T> cls) {
    return transformFor(cls).orElse(global.get());
  }
  

  public <T> Optional<BitTransform<T>> transformFor(Class<T> cls) {
    return transforms.stream()
        .filter(t -> t.match(cls))
        .map(t -> (BitTransform<T>)t)
        .findAny();
  }
  
  
  public BitBoxRegistry setGlobal(BitTransform transform) {
    global.set(Objects.requireNonNull(transform));
    return this;
  }
  
  
  public BitTransform getGlobal() {
    return global.get();
  }
  
  
  public BitBoxRegistry addTransform(BitTransform<?> transform) {
    transforms.add(Objects.requireNonNull(transform));
    Predicate<Class> prd = transform::match;
    btlisteners.stream()
        .filter(c -> c.match(prd))
        .forEach(c -> c.accept(transform));
    return this;
  }
  
  
  public BitBoxRegistry replaceTransform(Class<?> cls, BitTransform<?> transform) {
    Optional<BitTransform> opt = removeTransform(cls);
    transforms.add(transform);
    btlisteners.stream()
        .filter(c -> c.match(cls))
        .forEach(c -> c.accept(transform));
    return this;
  }
  
  
  public BitBoxRegistry removeTransform(BitTransform<?> transform) {
    transforms.remove(transform);
    return this;
  }
  
  
  public Optional<BitTransform> removeTransform(Class<?> cls) {
    Optional<BitTransform> opt = transforms.stream()
        .filter(t -> t.match(cls))
        .findAny();
    opt.ifPresent(transforms::remove);
    return opt;
  }
  
  
  public BitBoxRegistry clearTransforms() {
    transforms.clear();
    return this;
  }
  
  
  
  public static class BitTransformListener implements Consumer<BitTransform>, TypeMatching {
    
    private final Optional<Class> type;
    
    private final Consumer<BitTransform> cons;
    
    public BitTransformListener(Class type, Consumer<BitTransform> cons) {
      this.type = Optional.ofNullable(type);
      this.cons = Objects.requireNonNull(cons, "Bad null Consumer");
    }
    
    @Override
    public void accept(BitTransform t) {
      cons.accept(t);
    }
    
    @Override
    public boolean match(Class c) {
      return type.orElse(c).isAssignableFrom(c);
    }
    
    public boolean match(Predicate<Class> prd) {
      return type.isPresent() ? prd.test(type.get()) : true;
    }
    
    @Override
    public int hashCode() {
      int hash = 3;
      hash = 59 * hash + Objects.hashCode(this.type);
      hash = 59 * hash + Objects.hashCode(this.cons);
      return hash;
    }
    
    @Override
    public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      }
      if (obj == null) {
        return false;
      }
      if (getClass() != obj.getClass()) {
        return false;
      }
      final BitTransformListener other = (BitTransformListener) obj;
      if (!Objects.equals(this.type, other.type)) {
        return false;
      }
      return Objects.equals(this.cons, other.cons);
    }
    
    @Override
    public String toString() {
      return "BitTransformListener{" + "type=" + type + ", cons=" + cons + '}';
    }
    
  }
  
  
  
  public static class ObjectSpecListener implements Consumer<ObjectSpec>, TypeMatching {
    
    private final Optional<Class> type;
    
    private final Consumer<ObjectSpec> cons;
    
    public ObjectSpecListener(Class type, Consumer<ObjectSpec> cons) {
      this.type = Optional.ofNullable(type);
      this.cons = Objects.requireNonNull(cons, "Bad null Consumer");
    }
    
    @Override
    public void accept(ObjectSpec t) {
      cons.accept(t);
    }
    
    @Override
    public boolean match(Class c) {
      return type.orElse(c).isAssignableFrom(c);
    }
    
    public boolean match(Predicate<Class> prd) {
      return type.isPresent() ? prd.test(type.get()) : true;
    }
    
    @Override
    public int hashCode() {
      int hash = 7;
      hash = 31 * hash + Objects.hashCode(this.type);
      hash = 31 * hash + Objects.hashCode(this.cons);
      return hash;
    }
    
    @Override
    public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      }
      if (obj == null) {
        return false;
      }
      if (getClass() != obj.getClass()) {
        return false;
      }
      final ObjectSpecListener other = (ObjectSpecListener) obj;
      if (!Objects.equals(this.type, other.type)) {
        return false;
      }
      if (!Objects.equals(this.cons, other.cons)) {
        return false;
      }
      return true;
    }
    
    @Override
    public String toString() {
      return "ObjectSpecListener{" + "type=" + type + ", cons=" + cons + '}';
    }
    
  }
  
}
