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
import us.pserver.bitbox.spec.ObjectSpec;
import us.pserver.bitbox.transform.*;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 11 de abr de 2019
 */
public class BitBoxConfiguration {
  
  private final Set<BitTransform> transforms;
  
  private final Set<ObjectSpec> specs;
  
  private BitTransform global;
  
  private MethodHandles.Lookup lookup;
  
  
  public BitBoxConfiguration() {
    this.transforms = new CopyOnWriteArraySet<>();
    this.specs = new CopyOnWriteArraySet<>();
    this.global = new GlobalObjectTransform(this);
    this.lookup = MethodHandles.lookup();
    this.initTransforms();
  }
  
  
  private void initTransforms() {
    transforms.add(new ArrayTransform(this));
    transforms.add(new BooleanTransform());
    transforms.add(new ByteArrayTransform());
    transforms.add(new ByteTransform());
    transforms.add(new CharArrayTransform());
    transforms.add(new CharSequenceTransform());
    transforms.add(new ClassTransform(this));
    transforms.add(new CollectionTransform(this));
    transforms.add(new DoubleArrayTransform());
    transforms.add(new DoubleTransform());
    transforms.add(new EnumTransform(this));
    transforms.add(new FloatArrayTransform());
    transforms.add(new FloatTransform());
    transforms.add(new InetAddressTransform(this));
    transforms.add(new InstantTransform());
    transforms.add(new IntArrayTransform());
    transforms.add(new IntTransform());
    transforms.add(new ListTransform(this));
    transforms.add(new LocalDateTimeTransform());
    transforms.add(new LocalDateTransform());
    transforms.add(new LongArrayTransform());
    transforms.add(new LongTransform());
    transforms.add(new MapTransform(this));
    transforms.add(new SetTransform(this));
    transforms.add(new ShortArrayTransform());
    transforms.add(new ShortTransform());
    transforms.add(new VoidTransform());
    transforms.add(new ZonedDateTimeTransform());
  }
  
  
  public MethodHandles.Lookup lookup() {
    return lookup;
  }
  
  
  public BitBoxConfiguration lookup(MethodHandles.Lookup lookup) {
    this.lookup = Objects.requireNonNull(lookup);
    return this;
  }
  
  
  public BitBoxConfiguration addSpec(ObjectSpec o) {
    specs.add(Objects.requireNonNull(o));
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
  
  
  public BitBoxConfiguration replaceSpec(Class c, ObjectSpec o) {
    Optional<ObjectSpec> opt = specs.stream().filter(s -> s.match(c)).findAny();
    opt.ifPresent(specs::remove);
    specs.add(Objects.requireNonNull(o));
    return this;
  }
  
  
  public boolean containsTransform(Class cls) {
    return transforms.stream()
        .anyMatch(t -> t.match(cls));
  }
  
  
  public <T> BitTransform<T> getTransform(Class<T> cls) {
    return transformFor(cls).orElse(global);
  }
  

  public <T> Optional<BitTransform<T>> transformFor(Class<T> cls) {
    return transforms.stream()
        .filter(t -> t.match(cls))
        .map(t -> (BitTransform<T>)t)
        .findAny();
  }
  
  
  public BitBoxConfiguration setGlobalTransform(BitTransform transform) {
    global = Objects.requireNonNull(transform);
    return this;
  }
  
  
  public BitTransform getGlobalTransform() {
    return global;
  }
  
  
  public BitBoxConfiguration addTransform(BitTransform<?> transform) {
    transforms.add(Objects.requireNonNull(transform));
    return this;
  }
  
  
  public BitBoxConfiguration replaceTransform(Class<?> cls, BitTransform<?> transform) {
    Optional<BitTransform> opt = transforms.stream()
        .filter(t -> t.match(cls))
        .findAny();
    opt.ifPresent(transforms::remove);
    transforms.add(transform);
    return this;
  }


  @Override
  public int hashCode() {
    int hash = 7;
    hash = 79 * hash + Objects.hashCode(this.transforms);
    hash = 79 * hash + Objects.hashCode(this.specs);
    hash = 79 * hash + Objects.hashCode(this.global);
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
    final BitBoxConfiguration other = (BitBoxConfiguration) obj;
    if (!Objects.equals(this.transforms, other.transforms)) {
      return false;
    }
    if (!Objects.equals(this.specs, other.specs)) {
      return false;
    }
    if (!Objects.equals(this.global, other.global)) {
      return false;
    }
    return true;
  }


  @Override
  public String toString() {
    return "BitBoxConfiguration{" + "transforms=" + transforms + ", specs=" + specs + ", global=" + global + '}';
  }
  
}
