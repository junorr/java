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

package us.pserver.streams;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 17/10/2015
 */
public class PathCollection implements List<Path>, Iterator<Path> {
  
  
  private Iterator<Path> iterator;
  
  
  private final LinkedList<Path> list;
  
  
  public PathCollection() {
    list = new LinkedList<>();
    iterator = null;
  }


  @Override
  public boolean hasNext() {
    if(this.isEmpty()) {
      return false;
    }
    if(iterator == null) {
      iterator = this.iterator();
    }
    boolean hasnext = iterator.hasNext();
    if(!hasnext) iterator = null;
    return hasnext;
  }


  @Override
  public Path next() {
    if(!hasNext()) return null;
    return iterator.next();
  }


  @Override
  public int size() {
    return list.size();
  }


  @Override
  public boolean isEmpty() {
    return list.isEmpty();
  }


  @Override
  public boolean contains(Object o) {
    return list.contains(o);
  }


  @Override
  public Iterator<Path> iterator() {
    return iterator;
  }
  
  
  public Path[] toPathArray() {
    if(list.isEmpty()) {
      return null;
    }
    Path[] pts = new Path[list.size()];
    return this.toArray(pts);
  }


  @Override
  public Object[] toArray() {
    return list.toArray();
  }


  @Override
  public <T> T[] toArray(T[] a) {
    return list.toArray(a);
  }


  @Override
  public boolean add(Path e) {
    return list.add(e);
  }
  
  
  public boolean add(String path) {
    if(path == null || !path.trim().isEmpty()) {
      return false;
    }
    return list.add(Paths.get(path));
  }


  @Override
  public boolean remove(Object o) {
    return list.remove(o);
  }


  @Override
  public boolean containsAll(Collection<?> c) {
    return list.containsAll(c);
  }


  @Override
  public boolean addAll(Collection<? extends Path> c) {
    return list.addAll(c);
  }


  @Override
  public boolean addAll(int index, Collection<? extends Path> c) {
    return list.addAll(index, c);
  }


  @Override
  public boolean removeAll(Collection<?> c) {
    return list.retainAll(c);
  }


  @Override
  public boolean retainAll(Collection<?> c) {
    return list.retainAll(c);
  }


  @Override
  public void clear() {
    list.clear();
  }


  @Override
  public Path get(int index) {
    return list.get(index);
  }


  @Override
  public Path set(int index, Path element) {
    return list.set(index, element);
  }


  @Override
  public void add(int index, Path element) {
    list.add(index, element);
  }


  @Override
  public Path remove(int index) {
    return list.remove(index);
  }


  @Override
  public int indexOf(Object o) {
    return list.indexOf(o);
  }


  @Override
  public int lastIndexOf(Object o) {
    return list.lastIndexOf(o);
  }


  @Override
  public ListIterator<Path> listIterator() {
    return list.listIterator();
  }


  @Override
  public ListIterator<Path> listIterator(int index) {
    return list.listIterator(index);
  }


  @Override
  public List<Path> subList(int fromIndex, int toIndex) {
    return list.subList(fromIndex, toIndex);
  }

}
