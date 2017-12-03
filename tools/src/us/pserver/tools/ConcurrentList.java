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

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Supplier;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 24/09/2017
 */
public class ConcurrentList<T> implements List<T>, Deque<T> {

  private final LinkedList<T> list;
  
  private final ReentrantReadWriteLock lock;
  
  
  public ConcurrentList() {
    this.list = new LinkedList<>();
    this.lock = new ReentrantReadWriteLock();
  }
  
  
  private <U> U readLocked(Supplier<U> sp) {
    lock.readLock().lock();
    try {
      return sp.get();
    } finally {
      lock.readLock().unlock();
    }
  }
  
  private void writeLocked(Runnable rn) {
    lock.readLock().lock();
    try {
      rn.run();
    } finally {
      lock.readLock().unlock();
    }
  }
  
  private <U> U writeLocked(Supplier<U> sp) {
    lock.readLock().lock();
    try {
      return sp.get();
    } finally {
      lock.readLock().unlock();
    }
  }


  @Override
  public int size() {
    return readLocked(list::size);
  }


  @Override
  public boolean isEmpty() {
    return readLocked(list::isEmpty);
  }


  @Override
  public boolean contains(Object o) {
    return readLocked(()->list.contains(o));
  }


  @Override
  public Iterator<T> iterator() {
    return readLocked(list::iterator);
  }


  @Override
  public Object[] toArray() {
    return readLocked(()->list.toArray());
  }


  @Override
  public <T> T[] toArray(T[] a) {
    return readLocked(()->list.toArray(a));
  }


  @Override
  public boolean add(T e) {
    return writeLocked(()->list.add(e));
  }


  @Override
  public boolean remove(Object o) {
    return writeLocked(()->list.remove(o));
  }


  @Override
  public boolean containsAll(Collection<?> c) {
    return readLocked(()->list.containsAll(c));
  }


  @Override
  public boolean addAll(Collection<? extends T> c) {
    return writeLocked(()->list.addAll(c));
  }


  @Override
  public boolean addAll(int index, Collection<? extends T> c) {
    return writeLocked(()->list.addAll(index, c));
  }


  @Override
  public boolean removeAll(Collection<?> c) {
    return writeLocked(()->list.removeAll(c));
  }


  @Override
  public boolean retainAll(Collection<?> c) {
    return writeLocked(()->list.retainAll(c));
  }


  @Override
  public void clear() {
    writeLocked(list::clear);
  }


  @Override
  public T get(int index) {
    return readLocked(()->list.get(index));
  }


  @Override
  public T set(int index, T element) {
    return writeLocked(()->list.set(index, element));
  }


  @Override
  public void add(int index, T element) {
    writeLocked(()->list.add(index, element));
  }


  @Override
  public T remove(int index) {
    return writeLocked(()->list.remove(index));
  }


  @Override
  public int indexOf(Object o) {
    return readLocked(()->list.indexOf(o));
  }


  @Override
  public int lastIndexOf(Object o) {
    return readLocked(()->list.lastIndexOf(o));
  }


  @Override
  public ListIterator<T> listIterator() {
    return readLocked(()->list.listIterator());
  }


  @Override
  public ListIterator<T> listIterator(int index) {
    return readLocked(()->list.listIterator(index));
  }


  @Override
  public List<T> subList(int fromIndex, int toIndex) {
    return readLocked(()->list.subList(fromIndex, toIndex));
  }


  @Override
  public void addFirst(T e) {
    writeLocked(()->list.addFirst(e));
  }


  @Override
  public void addLast(T e) {
    writeLocked(()->list.addLast(e));
  }


  @Override
  public boolean offerFirst(T e) {
    return writeLocked(()->list.offerFirst(e));
  }


  @Override
  public boolean offerLast(T e) {
    return writeLocked(()->list.offerLast(e));
  }


  @Override
  public T removeFirst() {
    return writeLocked(list::removeFirst);
  }


  @Override
  public T removeLast() {
    return writeLocked(list::removeLast);
  }


  @Override
  public T pollFirst() {
    return writeLocked(list::pollFirst);
  }


  @Override
  public T pollLast() {
    return writeLocked(list::pollLast);
  }


  @Override
  public T getFirst() {
    return readLocked(list::getFirst);
  }


  @Override
  public T getLast() {
    return readLocked(list::getLast);
  }


  @Override
  public T peekFirst() {
    return readLocked(list::peekFirst);
  }


  @Override
  public T peekLast() {
    return readLocked(list::peekLast);
  }


  @Override
  public boolean removeFirstOccurrence(Object o) {
    return writeLocked(()->list.removeFirstOccurrence(o));
  }


  @Override
  public boolean removeLastOccurrence(Object o) {
    return writeLocked(()->list.removeLastOccurrence(o));
  }


  @Override
  public boolean offer(T e) {
    return writeLocked(()->list.offer(e));
  }


  @Override
  public T remove() {
    return writeLocked(()->list.remove());
  }


  @Override
  public T poll() {
    return writeLocked(list::poll);
  }


  @Override
  public T element() {
    return readLocked(list::element);
  }


  @Override
  public T peek() {
    return readLocked(list::peek);
  }


  @Override
  public void push(T e) {
    writeLocked(()->list.push(e));
  }


  @Override
  public T pop() {
    return writeLocked(list::pop);
  }


  @Override
  public Iterator<T> descendingIterator() {
    return readLocked(list::descendingIterator);
  }
  
}
