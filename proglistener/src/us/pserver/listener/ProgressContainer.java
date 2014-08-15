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

package us.pserver.listener;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 15/08/2014
 */
public class ProgressContainer implements List<ProgressListener>, ProgressListener {

  private List<ProgressListener> list;
  
  
  public ProgressContainer() {
    list = new LinkedList<>();
  }
  
  
  public ProgressContainer addListener(ProgressListener pl) {
    if(pl != null)
      list.add(pl);
    return this;
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
  public Iterator<ProgressListener> iterator() {
    return list.iterator();
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
  public boolean add(ProgressListener e) {
    if(e == null) return false;
    return list.add(e);
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
  public boolean addAll(Collection<? extends ProgressListener> c) {
    return list.addAll(c);
  }


  @Override
  public boolean addAll(int index, Collection<? extends ProgressListener> c) {
    return list.addAll(index, c);
  }


  @Override
  public boolean removeAll(Collection<?> c) {
    return list.removeAll(c);
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
  public ProgressListener get(int index) {
    return list.get(index);
  }


  @Override
  public ProgressListener set(int index, ProgressListener element) {
    return list.set(index, element);
  }


  @Override
  public void add(int index, ProgressListener element) {
    list.add(index, element);
  }


  @Override
  public ProgressListener remove(int index) {
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
  public ListIterator<ProgressListener> listIterator() {
    return list.listIterator();
  }


  @Override
  public ListIterator<ProgressListener> listIterator(int index) {
    return list.listIterator(index);
  }


  @Override
  public List<ProgressListener> subList(int fromIndex, int toIndex) {
    return list.subList(fromIndex, toIndex);
  }


  @Override
  public void setMax(long max) {
    if(list.isEmpty()) return;
    for(ProgressListener pl : list)
      pl.setMax(max);
  }


  @Override
  public void update(Path path) {
    if(list.isEmpty()) return;
    for(ProgressListener pl : list)
      pl.update(path);
  }


  @Override
  public void update(long current) {
    if(list.isEmpty()) return;
    for(ProgressListener pl : list)
      pl.update(current);
  }


  @Override
  public void error(IOException ex) {
    if(list.isEmpty()) return;
    for(ProgressListener pl : list)
      pl.error(ex);
  }
  
}
