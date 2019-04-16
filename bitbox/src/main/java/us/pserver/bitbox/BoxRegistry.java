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

import us.pserver.bitbox.transform.*;

import java.lang.invoke.MethodHandles;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicReference;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 11 de abr de 2019
 */
public enum BoxRegistry {
  
  INSTANCE;
  
  private BoxRegistry() {
    this.transforms = new CopyOnWriteArraySet<>();
    this.schemas = new CopyOnWriteArraySet<>();
    this.global = new AtomicReference<>();
    this.lookup = new AtomicReference<>(MethodHandles.lookup());
    this.initTransforms();
  }
  
  private final Set<BitTransform> transforms;
  
  private final Set<ObjectSchema> schemas;
  
  private final AtomicReference<BitTransform> global;
  
  private final AtomicReference<MethodHandles.Lookup> lookup;
  
  
  private void initTransforms() {
    transforms.add(new BooleanTransform());
    transforms.add(new ByteTransform());
    transforms.add(new CharSequenceTransform());
    transforms.add(new DoubleTransform());
    transforms.add(new FloatTransform());
    transforms.add(new InstantTransform());
    transforms.add(new IntTransform());
    transforms.add(new InetAddressTransform());
    transforms.add(new LocalDateTransform());
    transforms.add(new LocalDateTimeTransform());
    transforms.add(new LongTransform());
    transforms.add(new ShortTransform());
    transforms.add(new ZonedDateTimeTransform());
    transforms.add(new ClassTransform());
  }
  
  
  public MethodHandles.Lookup lookup() {
    return lookup.get();
  }
  
  
  public BoxRegistry lookup(MethodHandles.Lookup lookup) {
    this.lookup.set(Objects.requireNonNull(lookup));
    return this;
  }
  
  
  public boolean containsSchema(Class cls) {
    return schemas.stream()
        .anyMatch(s -> s.matching().test(cls));
  }
  
  
  public <T> Optional<ObjectSchema<T>> schemaFor(Class<T> cls) {
    return schemas.stream()
        .filter(s -> s.matching().test(cls))
        .map(s -> (ObjectSchema<T>)s)
        .findAny();
  }
  
  
  public BoxRegistry addSchema(ObjectSchema o) {
    schemas.add(Objects.requireNonNull(o));
    return this;
  }
  
  
  public BoxRegistry replaceSchema(Class c, ObjectSchema o) {
    Optional<ObjectSchema> opt = schemas.stream().filter(s -> s.matching().test(c)).findAny();
    opt.ifPresent(schemas::remove);
    schemas.add(Objects.requireNonNull(o));
    return this;
  }
  
  
  public BoxRegistry removeSchema(Class c) {
    Optional<ObjectSchema> opt = schemas.stream().filter(s -> s.matching().test(c)).findAny();
    opt.ifPresent(schemas::remove);
    return this;
  }
  
  
  public BoxRegistry removeSchema(ObjectSchema os) {
    schemas.remove(os);
    return this;
  }
  
  
  public BoxRegistry clearSchemas() {
    schemas.clear();
    return this;
  }
  
  
  public boolean containsTransform(Class cls) {
    return transforms.stream()
        .anyMatch(t -> t.matching().test(cls));
  }
  
  
  public <T> BitTransform<T> getAnyTransform(Class<T> cls) {
    return transformFor(cls).orElse(global.get());
  }
  

  public <T> BitTransform<T> getTransform(Class<T> cls) {
    return transformFor(cls).orElse(global.get());
  }
  

  public <T> Optional<BitTransform<T>> transformFor(Class<T> cls) {
    return transforms.stream()
        .filter(t -> t.matching().test(cls))
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
        .filter(t -> t.matching().test(cls))
        .findAny();
    opt.ifPresent(transforms::remove);
    return opt;
  }
  
  
  public BoxRegistry clearTransforms() {
    transforms.clear();
    return this;
  }
  
}
