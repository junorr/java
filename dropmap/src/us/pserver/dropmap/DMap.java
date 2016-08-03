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

package us.pserver.dropmap;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import us.pserver.dropmap.impl.DropMap;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 03/08/2016
 */
public interface DMap<K,V> extends Map<K,V> {

  public DMap<K,V> put(K k, V v, long timeout, TimeUnit unit);
  
  public DMap<K,V> put(K k, V v, Duration dur, Consumer<DEntry<K,V>> cs);
  
  public DMap<K,V> drop(K k, long timeout, TimeUnit unit);
  
  public DMap<K,V> drop(K k, Duration dur, Consumer<DEntry<K,V>> cs);
  
  public DMap<K,V> drop(K k);
  
  public DEntry<K,V> entry(K k);
  
  
  public static <T,U> DMap<T,U> newMap() {
    return new DropMap();
  }
  
  
  
  
  public static interface DEntry<K,V> extends Comparable<DEntry<K,V>> {
    
    public K getKey();
    
    public V getValue();
    
    public DMap<K,V> getMap();
    
    public Duration getDuration();
    
    public Duration getTTL();
    
    public Instant getStoredInstant();
    
    public Instant getLastUpdate();
    
  }
  
  
  
  
  public static interface DEngine<K,V> {
    
    public DEngine<K,V> start();
    
    public DEngine<K,V> stop();
    
    public boolean isRunning();
    
    public DEngine<K,V> reset();
    
    public boolean isTracking(DEntry<K,V> e);
    
    public DEngine<K,V> add(DEntry<K,V> e, Consumer<DEntry<K,V>> c);
    
    public DEngine<K,V> remove(DEntry<K,V> e);
    
  }
  
}
