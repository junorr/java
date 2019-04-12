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

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicReference;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 11 de abr de 2019
 */
public enum BoxRegistry {
  
  INSTANCE;
  
  private BoxRegistry() {
    this.transforms = new ConcurrentSkipListSet<>();
    this.global = new AtomicReference<>();
  }
  
  private final Set<BitTransform> transforms;
  
  private final AtomicReference<BitTransform> global;

  public <T> BitTransform<T> transformFor(Class<T> cls) {
    return transforms.stream()
        .filter(t -> t.matching().test(cls))
        .map(t -> (BitTransform<T>)t)
        .findAny().orElse(global.get());
  }
  
  public BoxRegistry setGlobal(BitTransform transform) {
    global.set(Objects.requireNonNull(transform));
    return this;
  }
  
  public BitTransform getGlobal() {
    return global.get();
  }
  
  public BoxRegistry add(BitTransform<?> transform) {
    transforms.add(Objects.requireNonNull(transform));
    return this;
  }
  
  public BoxRegistry replaceFor(Class<?> cls, BitTransform<?> transform) {
    Optional<BitTransform> opt = removeFor(cls);
    transforms.add(transform);
    return this;
  }
  
  public BoxRegistry remove(BitTransform<?> transform) {
    transforms.remove(transform);
    return this;
  }
  
  public Optional<BitTransform> removeFor(Class<?> cls) {
    Optional<BitTransform> opt = transforms.stream()
        .filter(t -> t.matching().test(cls))
        .findAny();
    opt.ifPresent(transforms::remove);
    return opt;
  }
  
}
