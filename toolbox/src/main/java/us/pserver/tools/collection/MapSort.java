/*
 * Direitos Autorais Reservados (c) 2011 Juno Roesler
 * Contato: juno.rr@gmail.com
 * 
 * Esta biblioteca � software livre; voc� pode redistribu�-la e/ou modific�-la sob os
 * termos da Licen�a P�blica Geral Menor do GNU conforme publicada pela Free
 * Software Foundation; tanto a vers�o 2.1 da Licen�a, ou qualquer
 * vers�o posterior.
 * 
 * Esta biblioteca � distribu�da na expectativa de que seja �til, por�m, SEM
 * NENHUMA GARANTIA; nem mesmo a garantia impl�cita de COMERCIABILIDADE
 * OU ADEQUA��O A UMA FINALIDADE ESPEC�FICA. Consulte a Licen�a P�blica
 * Geral Menor do GNU para mais detalhes.
 * 
 * Voc� deve ter recebido uma c�pia da Licen�a P�blica Geral Menor do GNU junto
 * com esta biblioteca; se n�o, acesse 
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html, 
 * ou escreva para a Free Software Foundation, Inc., no
 * endere�o 59 Temple Street, Suite 330, Boston, MA 02111-1307 USA.
 */

package us.pserver.tools.collection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import us.pserver.tools.Match;

/**
 * Utility class for sorting Maps.
 * @author Juno Roesler - juno@pserver.us
 * @param <T> The type of key/value for map sorting.
 */
public class MapSort<T> {

  private final Comparator<T> comp;
  

  /**
   * Constructor receives the comparator for key or values.
   * @param compare The comparator used for sorting.
   */
  public MapSort(Comparator<T> compare) {
    comp = Match.notNull(compare).getOrFail("Bad null Comparator");
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
    Comparator<Entry<V,T>> vorder = (Entry<V,T> o1, Entry<V,T> o2) -> 
        comp.compare(o1.getValue(), o2.getValue());
    List<Entry<V,T>> ls = new ArrayList<>(entries.size());
    ls.addAll(entries);
    Collections.sort(ls, vorder);
    map.clear();
    ls.forEach(e->map.put(e.getKey(), e.getValue()));
  }
  
  
  /**
   * Return a new map sorted by key.
   * @param map The map to sort.
   * @param <V> The type of the map values.
   * @return a new sorted map by its key type.
   */
  public <V> Map<T,V> newSortedByKey(Map<T,V> map) {
    TreeMap<T,V> sorted = new TreeMap<>(comp);
    Set<Entry<T,V>> entries = map.entrySet();
    entries.forEach(e->sorted.put(e.getKey(), e.getValue()));
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
    Comparator<Entry<V,T>> vorder = (Entry<V, T> o1, Entry<V, T> o2) -> 
        comp.compare(o1.getValue(), o2.getValue());
    List<Entry<V,T>> ls = new ArrayList<>(entries.size());
    ls.addAll(entries);
    Collections.sort(ls, vorder);
    HashMap<V,T> sorted = new HashMap<>();
    ls.forEach(e->sorted.put(e.getKey(), e.getValue()));
    return sorted;
  }
  
}
