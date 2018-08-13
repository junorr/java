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

package us.pserver.jpx.pool.impl;

import java.util.Objects;
import java.util.UUID;
import us.pserver.cdr.digest.DigestAlgorithm;
import us.pserver.cdr.digest.Digester;
import us.pserver.cdr.hex.HexCoder;
import us.pserver.jpx.pool.Pool;
import us.pserver.jpx.pool.Pooled;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 11/08/2018
 */
public class DefaultPooled<T> implements Pooled<T> {
  
  private final String uid;
  
  private final T value;
  
  private final Pool<T> pool;
  
  
  public DefaultPooled(String uid, T value, Pool<T> pool) {
    this.pool = Objects.requireNonNull(pool);
    this.value = Objects.requireNonNull(value);
    this.uid = uid;
  }
  
  public DefaultPooled(T value, Pool<T> pool) {
    this.pool = Objects.requireNonNull(pool);
    this.value = Objects.requireNonNull(value);
    this.uid = HexCoder.encode(Digester.digest(
        UUID.randomUUID().toString(), 
        DigestAlgorithm.SHA_256)
    );
  }
  
  
  @Override
  public T get() {
    return value;
  }
  
  
  @Override
  public void release() {
    pool.release(this);
  }
  
  
  @Override
  public String uid() {
    return uid;
  }


  @Override
  public int hashCode() {
    int hash = 7;
    hash = 37 * hash + Objects.hashCode(this.uid);
    hash = 37 * hash + Objects.hashCode(this.value);
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
    final DefaultPooled<?> other = (DefaultPooled<?>) obj;
    if (!Objects.equals(this.uid, other.uid)) {
      return false;
    }
    if (!Objects.equals(this.value, other.value)) {
      return false;
    }
    return true;
  }


  @Override
  public String toString() {
    return "Pooled{" + "uid=" + uid + ", value=" + value + '}';
  }
  
}
