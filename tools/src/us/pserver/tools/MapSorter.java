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

package us.pserver.tools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import us.pserver.insane.Checkup;
import us.pserver.insane.Sane;

/**
 * Utility class for sorting Maps.
 * @author Juno Roesler - juno@pserver.us
 * @param <T> The type of key/value for map sorting.
 */
public class MapSorter<T> {

  private final Comparator<T> comp;
  

  /**
   * Constructor receives the comparator for key or values.
   * @param compare The comparator used for sorting.
   */
  public MapSorter(Comparator<T> compare) {
    comp = Sane.of(compare).get(Checkup.isNotNull());
  }
  
  
  /**
   * Get the comparator used for sorting.
   * @return the comparator used for sorting.
   */
  public Comparator<T> getComparator() {
    return comp;
  }
  
  
  /**
   * Sort the specified map by its key type.
   * @param <V> The type of the map values.
   * @param map The map to sort.
   */
  public <V> void sortByKey(Map<T,V> map) {
    Set<Entry<T,V>> entries = map.entrySet();
    Comparator<Entry<T,V>> korder = new Comparator<Entry<T,V>>() {
      @Override
      public int compare(Entry<T,V> o1, Entry<T,V> o2) {
        return comp.compare(o1.getKey(), o2.getKey());
      }
    };
    List<Entry<T,V>> ls = new ArrayList<>(entries.size());
    ls.addAll(entries);
    Collections.sort(ls, korder);
    map.clear();
    for(Entry<T,V> e : ls) {
      map.put(e.getKey(), e.getValue());
    }
  }
  
  
  /**
   * Sort the specified map by its value type.
   * @param <V> The type of the map keys.
   * @param map The map to sort.
   */
  public <V> void sortByValue(Map<V,T> map) {
    Set<Entry<V,T>> entries = map.entrySet();
    Comparator<Entry<V,T>> vorder = new Comparator<Entry<V,T>>() {
      @Override
      public int compare(Entry<V,T> o1, Entry<V,T> o2) {
        return comp.compare(o1.getValue(), o2.getValue());
      }
    };
    List<Entry<V,T>> ls = new ArrayList<>(entries.size());
    ls.addAll(entries);
    Collections.sort(ls, vorder);
    map.clear();
    for(Entry<V,T> e : ls) {
      map.put(e.getKey(), e.getValue());
    }
  }
  
  
  /**
   * Return a new sorted map by its key type.
   * @param map The map to sort.
   * @param <V> The type of the map values.
   * @return a new sorted map by its key type.
   */
  public <V> Map<T,V> newSortedByKey(Map<T,V> map) {
    TreeMap<T,V> sorted = new TreeMap<>(comp);
    Set<Entry<T,V>> entries = map.entrySet();
    for(Entry<T,V> e : entries) {
      sorted.put(e.getKey(), e.getValue());
    }
    return sorted;
  }
  
  
  /**
   * Return a new sorted map by its value type.
   * @param <V> The type of the map keys.
   * @param map The map to sort.
   * @return a new sorted map by its value type.
   */
  public <V> Map<V,T> newSortedByValue(Map<V,T> map) {
    Set<Entry<V,T>> entries = map.entrySet();
    Comparator<Entry<V,T>> vorder = new Comparator<Entry<V,T>>() {
      @Override
      public int compare(Entry<V, T> o1, Entry<V, T> o2) {
        return comp.compare(o1.getValue(), o2.getValue());
      }
    };
    List<Entry<V,T>> ls = new ArrayList<>(entries.size());
    ls.addAll(entries);
    Collections.sort(ls, vorder);
    HashMap<V,T> sorted = new HashMap<>();
    for(Entry<V,T> e : ls) {
      sorted.put(e.getKey(), e.getValue());
    }
    return sorted;
  }
  
}
