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
import java.util.function.Consumer;
import us.pserver.dropmap.impl.DropMap;

/**
 * ConcurrentHashMap with entry discarding (TTL) capabilities.
 * A key-value pair can be inserted with a time-to-live argument,
 * and DropMap will discard this entry after the given TTL.
 * 
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 03/08/2016
 * @param <K> Key Type.
 * @param <V> Value Type.
 */
public interface DMap<K,V> extends Map<K,V> {

  /**
   * Puts an entry and schedule the drop time.
   * @param k Key
   * @param v Value
   * @param dur Amount of time until entry drop.
   * @return This instance of DropMap.
   */
  public DMap<K,V> put(K k, V v, Duration dur);
  
  /**
   * Puts an entry and schedule the drop time.
   * @param k Key
   * @param v Value
   * @param dur Amount of time until entry drop.
   * @param cs Consumer invoked wneh entry is dropped.
   * @return This instance of DropMap.
   */
  public DMap<K,V> put(K k, V v, Duration dur, Consumer<DEntry<K,V>> cs);
  
  /**
   * Schedule the drop of an existing map entry.
   * @param k Key
   * @param dur Amount of time until entry drop.
   * @return This instance of DropMap.
   */
  public DMap<K,V> drop(K k, Duration dur);
  
  /**
   * Schedule the drop of an existing map entry.
   * @param k Key
   * @param dur Amount of time until entry drop.
   * @param cs Consumer invoked wneh entry is dropped.
   * @return This instance of DropMap.
   */
  public DMap<K,V> drop(K k, Duration dur, Consumer<DEntry<K,V>> cs);
  
  /**
   * Get the entry of the given key.
   * @param k Key
   * @return DEntry of the key.
   */
  public DEntry<K,V> getEntry(K k);
  
  /**
   * Get the DropEngine of this DropMap instance.
   * @return the DropEngine of this DropMap instance.
   */
  public DEngine<K,V> getEngine();
  
  
  /**
   * Create a new instance of DropMap.
   * @param <T> Key Type
   * @param <U> Value Type
   * @return The new instance of DropMap.
   */
  public static <T,U> DMap<T,U> newMap() {
    return new DropMap();
  }
  
  
  
  
  /**
   * Entry of DropMap to keep MetaData of key-value pairs.
   * @param <K> Key Type.
   * @param <V> Value Type.
   */
  public static interface DEntry<K,V> extends Comparable<DEntry<K,V>> {
    
    /**
     * Get the key.
     * @return The Key.
     */
    public K getKey();
    
    /**
     * Get the value.
     * @return The Value.
     */
    public V getValue();
    
    /**
     * Get the DropMap instance holding this entry.
     * @return The DropMap instance holding this entry.
     */
    public DMap<K,V> getMap();
    
    /**
     * Get the original amount of time until entry drop.
     * @return Original amount of time until entry drop, 
     * or Duration.ZERO if not setted.
     */
    public Duration getDuration();
    
    /**
     * Get the time left (time-to-live) from now until entry drop.
     * @return Time left (time-to-live) from now until entry drop, 
     * or Duration.ZERO if not setted.
     */
    public Duration getTTL();
    
    /**
     * Get the creation time of this entry.
     * @return creation time of this entry.
     */
    public Instant getStoredInstant();
    
    /**
     * Get the last access time (Map::get) of this entry.
     * @return last access time of this entry.
     */
    public Instant getLastAccess();
    
    /**
     * Update the last access time of this entry.
     * @return This instance of DropEntry.
     */
    public DEntry<K,V> updateLastAccess();
    
    /**
     * Get the last update time of this entry.
     * @return last update time of this entry.
     */
    public Instant getLastUpdate();
    
  }
  
  
  
  
  /**
   * Drop Engine for DMap. The drop engine uses only one 
   * daemon thread with minimum priority.
   * Final users usually do not need to care about this class.
   * @param <K> Key Type.
   * @param <V> Value Type.
   */
  public static interface DEngine<K,V> {
    
    /**
     * Starts the DropEngine Thread.
     * @return This instance of DropEngine.
     */
    public DEngine<K,V> start();
    
    /**
     * Stops the Drop Engine Thread. Once Stopped, 
     * the DropEngine can not be used again.
     * @return This instance of DropEngine.
     */
    public DEngine<K,V> stop();
    
    /**
     * Return true if DropEngine (thread) is running, false otherwise.
     * @return true if DropEngine (thread) is running, false otherwise.
     */
    public boolean isRunning();
    
    /**
     * Resets the DropEngine, canceling all drop schedules.
     * The DropEngine is enabled for reusing after this.
     * @return This instance of DropEngine.
     */
    public DEngine<K,V> reset();
    
    /**
     * Return true if DropEngine is current 
     * trackind the given entry for dropping.
     * @param e DEntry
     * @return true if DropEngine is current 
     * trackind the given entry, false otherwise.
     */
    public boolean isTracking(DEntry<K,V> e);
    
    /**
     * Adds an entry for dropping.
     * @param e DEntry for dropping.
     * @param c Consumer called when entry is dropped.
     * @return This instance of DropEngine.
     */
    public DEngine<K,V> add(DEntry<K,V> e, Consumer<DEntry<K,V>> c);
    
    /**
     * Removes the given entry of DropEngine, 
     * canceling the drop schedule.
     * @param e DEntry for remove.
     * @return This instance of DropEngine.
     */
    public DEngine<K,V> remove(DEntry<K,V> e);
    
  }
  
}
