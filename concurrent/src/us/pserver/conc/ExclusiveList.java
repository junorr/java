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

package us.pserver.conc;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import java.util.stream.Stream;


/**
 * <b>ExclusiveList</b> implementa uma <code>java.util.List</code>
 * com acesso exclusivo entre <code>Threads</code> via
 * <code>java.util.concurrent.locks.ReentrantLock</code>.
 * 
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 22/04/2014
 * @param <T> Tipo de elemento armazenado na lista.
 */
public class ExclusiveList<T> implements List<T> {

  private final List<T> list;
  
  private final ReentrantLock lock;
  
  
  /**
   * Construtor padrão e sem argumentos.
   */
  public ExclusiveList() {
    list = new LinkedList<T>();
    lock = new ReentrantLock();
  }
  
  
  /**
   * Adiciona o elemento ao final da lista.
   * @param t Elemento a ser adicionado.
   * @return Esta instância modificiada de <code>ExclusiveList</code>.
   */
  public synchronized ExclusiveList<T> put(T t) {
    if(t != null) {
      try {
        lock.lock();
        list.add(t);
      } finally {
        lock.unlock();
      }
    }
    return this;
  }
  
  
  /**
   * Retorna o primeiro elemento <code>(0)</code> da lista.
   * @return Primeiro elemento <code>(0)</code> da lista.
   */
  public synchronized T peek() {
    if(isEmpty()) return null;
    try {
      lock.lock();
      return list.get(0);
    } finally {
      lock.unlock();
    }
  }
  

  /**
   * Retorna o último elemento <code>(size() - 1)</code> da lista.
   * @return último elemento <code>(size() - 1)</code> da lista.
   */
  public synchronized T peekLast() {
    if(isEmpty()) return null;
    try {
      lock.lock();
      return list.get(size()-1);
    } finally {
      lock.unlock();
    }
  }
  

  /**
   * <b>Remove</b> e retorna o primeiro elemento 
   * <code>(0)</code> da lista.
   * @return O elemento removido.
   */
  public synchronized T pop() {
    if(isEmpty()) return null;
    try {
      lock.lock();
      return list.remove(0);
    } finally {
      lock.unlock();
    }
  }
  

  /**
   * <b>Remove</b> e retorna o último elemento 
   * <code>(size() - 1)</code> da lista.
   * @return O elemento removido.
   */
  public synchronized T popLast() {
    if(isEmpty()) return null;
    try {
      lock.lock();
      return list.remove(size()-1);
    } finally {
      lock.unlock();
    }
  }
  
  
  private String justify(int i) {
    int len = String.valueOf(list.size()).length();
    String si = String.valueOf(i);
    int slen = si.length();
    for(int j = 0; j < len - slen; j++) {
      si += " ";
    }
    return si;
  }
  
  
  /**
   * Imprime na saída padrão <code>System.out</code>
   * o conteúdo da lista.
   * @return Esta instância modificiada de <code>ExclusiveList</code>.
   */
  public synchronized ExclusiveList<T> printAll() {
    return this.printAll(System.out);
  }
  
  
  /**
   * Imprime no stream de saída informada <code>PrintStream</code>
   * o conteúdo da lista.
   * @return Esta instância modificiada de <code>ExclusiveList</code>.
   */
  public synchronized ExclusiveList<T> printAll(PrintStream ps) {
    if(ps == null)
      throw new IllegalArgumentException(
          "Invalid PrintStream: "+ ps);
    ps.print("* ExclusiveList( "+ list.size()+ " ) {");
    if(isEmpty()) {
      ps.println("}");
      return this;
    }
    try {
      lock.lock();
      ps.println();
      for(int i = 0; i < list.size(); i++) {
        ps.println("  "+ justify(i)+ ". "+ list.get(i));
      }
      ps.println("}");
    } finally {
      lock.unlock();
    }
    return this;
  }
  
  
  /**
   * Retorna uma lista do tipo <code>java.util.ArrayList</code>
   * contendo referências a todos os objetos contidos
   * nesta instância de <code>ExclusiveList</code>.
   * @return <code>java.util.ArrayList</code>.
   */
  public synchronized List<T> copy() {
    if(isEmpty()) return Collections.emptyList();
    try {
      lock.lock();
      ArrayList<T> copy = new ArrayList<>(size());
      copy.addAll(list);
      return copy;
    } finally {
      lock.unlock();
    }
  }
  
  
  /**
   * Retorna uma coleção do tipo <code>java.util.Collection</code>
   * contendo referências a todos os objetos contidos
   * nesta instância de <code>ExclusiveList</code>.
   * @return <code>java.util.Collection</code>.
   */
  public synchronized Collection<T> getAll() {
    return this.copy();
  }
  
  
  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder();
    PrintStream ps = new PrintStream(new OutputStream() {
      @Override
      public void write(int b) throws IOException {
        sb.append((char) b);
      }
    });
    this.printAll(ps);
    return sb.toString();
  }
  

  @Override
  public synchronized int size() {
    return list.size();
  }


  @Override
  public synchronized boolean isEmpty() {
    return list.isEmpty();
  }


  @Override
  public synchronized boolean contains(Object o) {
    try {
      lock.lock();
      return list.contains(o);
    } finally {
      lock.unlock();
    }
  }


  @Override
  public synchronized Iterator<T> iterator() {
    try {
      lock.lock();
      return list.iterator();
    } finally {
      lock.unlock();
    }
  }


  @Override
  public synchronized Object[] toArray() {
    try {
      lock.lock();
      return list.toArray();
    } finally {
      lock.unlock();
    }
  }


  @Override
  public synchronized <T> T[] toArray(T[] a) {
    try {
      lock.lock();
      return list.toArray(a);
    } finally {
      lock.unlock();
    }
  }


  @Override
  public synchronized boolean add(T e) {
    try {
      lock.lock();
      return list.add(e);
    } finally {
      lock.unlock();
    }
  }


  @Override
  public synchronized boolean remove(Object o) {
    try {
      lock.lock();
      return list.remove(o);
    } finally {
      lock.unlock();
    }
  }


  @Override
  public synchronized boolean containsAll(Collection<?> c) {
    try {
      lock.lock();
      return list.containsAll(c);
    } finally {
      lock.unlock();
    }
  }


  @Override
  public synchronized boolean addAll(Collection<? extends T> c) {
    try {
      lock.lock();
      return list.addAll(c);
    } finally {
      lock.unlock();
    }
  }


  @Override
  public synchronized boolean addAll(int index, Collection<? extends T> c) {
    try {
      lock.lock();
      return list.addAll(index, c);
    } finally {
      lock.unlock();
    }
  }


  @Override
  public synchronized boolean removeAll(Collection<?> c) {
    try {
      lock.lock();
      return list.removeAll(c);
    } finally {
      lock.unlock();
    }
  }


  @Override
  public synchronized boolean retainAll(Collection<?> c) {
    try {
      lock.lock();
      return list.retainAll(c);
    } finally {
      lock.unlock();
    }
  }


  @Override
  public synchronized void clear() {
    try {
      lock.lock();
      list.clear();
    } finally {
      lock.unlock();
    }
  }


  @Override
  public synchronized T get(int index) {
    try {
      lock.lock();
      return list.get(index);
    } finally {
      lock.unlock();
    }
  }


  @Override
  public synchronized T set(int index, T element) {
    try {
      lock.lock();
      return list.set(index, element);
    } finally {
      lock.unlock();
    }
  }


  @Override
  public synchronized void add(int index, T element) {
    try {
      lock.lock();
      list.add(index, element);
    } finally {
      lock.unlock();
    }
  }


  @Override
  public synchronized T remove(int index) {
    try {
      lock.lock();
      return list.remove(index);
    } finally {
      lock.unlock();
    }
  }


  @Override
  public synchronized int indexOf(Object o) {
    try {
      lock.lock();
      return list.indexOf(o);
    } finally {
      lock.unlock();
    }
  }


  @Override
  public synchronized int lastIndexOf(Object o) {
    try {
      lock.lock();
      return list.lastIndexOf(o);
    } finally {
      lock.unlock();
    }
  }


  @Override
  public synchronized ListIterator<T> listIterator() {
    try {
      lock.lock();
      return list.listIterator();
    } finally {
      lock.unlock();
    }
  }


  @Override
  public synchronized ListIterator<T> listIterator(int index) {
    try {
      lock.lock();
      return list.listIterator(index);
    } finally {
      lock.unlock();
    }
  }


  @Override
  public synchronized List<T> subList(int fromIndex, int toIndex) {
    try {
      lock.lock();
      return list.subList(fromIndex, toIndex);
    } finally {
      lock.unlock();
    }
  }
  
  
  @Override
  public synchronized Stream<T> stream() {
    return copy().stream();
  }
  
  
  @Override
  public synchronized void forEach(Consumer<? super T> cs) {
    if(isEmpty()) return;
    try {
      lock.lock();
      list.forEach(cs);
    } finally {
      lock.unlock();
    }
  }

}
