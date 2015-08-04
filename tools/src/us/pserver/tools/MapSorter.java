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

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 04/08/2015
 */
public class MapSorter<T> {

  private final Comparator<T> comp;
  
  
  public MapSorter(Comparator<T> compare) {
    comp = Valid.off(compare)
        .forNull()
        .getOrFail(Comparator.class);
  }
  
  
  public Comparator<T> getComparator() {
    return comp;
  }
  
  
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
  
  
  public <V> Map<T,V> newSortedByKey(Map<T,V> map) {
    TreeMap<T,V> sorted = new TreeMap<>(comp);
    Set<Entry<T,V>> entries = map.entrySet();
    for(Entry<T,V> e : entries) {
      sorted.put(e.getKey(), e.getValue());
    }
    return sorted;
  }
  
  
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
