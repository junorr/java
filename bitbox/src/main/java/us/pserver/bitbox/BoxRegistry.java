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
import us.pserver.bitbox.inspect.ObjectSpec;
import us.pserver.bitbox.transform.*;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 11 de abr de 2019
 */
public enum BoxRegistry {
  
  INSTANCE;
  
  private BoxRegistry() {
    this.transforms = new CopyOnWriteArraySet<>();
    this.specs = new CopyOnWriteArraySet<>();
    this.global = new AtomicReference<>(new GlobalObjectTransform());
    this.lookup = new AtomicReference<>(MethodHandles.lookup());
    this.initTransforms();
  }
  
  private final Set<BitTransform> transforms;
  
  private final Set<ObjectSpec> specs;
  
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
  
  
  public BoxRegistry lookup(MethodHandles.Lookup lookup) {
    this.lookup.set(Objects.requireNonNull(lookup));
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
  
  
  public BoxRegistry addSpec(ObjectSpec o) {
    specs.add(Objects.requireNonNull(o));
    return this;
  }
  
  
  public BoxRegistry replaceSpec(Class c, ObjectSpec o) {
    Optional<ObjectSpec> opt = specs.stream().filter(s -> s.match(c)).findAny();
    opt.ifPresent(specs::remove);
    specs.add(Objects.requireNonNull(o));
    return this;
  }
  
  
  public BoxRegistry removeSpec(Class c) {
    Optional<ObjectSpec> opt = specs.stream().filter(s -> s.match(c)).findAny();
    opt.ifPresent(specs::remove);
    return this;
  }
  
  
  public BoxRegistry removeSpec(ObjectSpec os) {
    specs.remove(os);
    return this;
  }
  
  
  public BoxRegistry clearSpecs() {
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
  
  
  public BoxRegistry setGlobal(BitTransform transform) {
    global.set(Objects.requireNonNull(transform));
    return this;
  }
  
  
  public BitTransform getGlobal() {
    return global.get();
  }
  
  
  public BoxRegistry addTransform(BitTransform<?> transform) {
    transforms.add(Objects.requireNonNull(transform));
    return this;
  }
  
  
  public BoxRegistry replaceTransform(Class<?> cls, BitTransform<?> transform) {
    Optional<BitTransform> opt = removeTransform(cls);
    transforms.add(transform);
    return this;
  }
  
  
  public BoxRegistry removeTransform(BitTransform<?> transform) {
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
  
  
  public BoxRegistry clearTransforms() {
    transforms.clear();
    return this;
  }
  
}
