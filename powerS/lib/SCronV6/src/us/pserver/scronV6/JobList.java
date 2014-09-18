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

package us.pserver.scronV6;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <b>JobList</b> encapsula uma <code>List&lt;Pair&gt;</code>, 
 * externalizando funções absolutamente seguras para
 * ambientes multithread. <b>JobList</b> é utilizada por
 * <code>SCronV6</code> para armazenamento, organização
 * e agendamento de execução de <code>Jobs e Schedules</code>.
 * 
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 10/04/2014
 * @see us.pserver.scronV6.Pair
 */
public class JobList {

  private final ReentrantLock lock;
  
  private final List<Pair> list;
  
 /**
  * Construtor padrão sem argumentos;
  */
  public JobList() {
    lock = new ReentrantLock(true);
    list = Collections.synchronizedList(
        new LinkedList<Pair>());
  }
  
  
  /**
   * Imprime na saída padrão o conteúdo da lista.
   */
  public synchronized void print() {
    System.out.println("* JobList { size="+ size()+ " }");
    if(isEmpty()) return;
    list.stream().forEach(p->System.out.println("   "+ p));
    System.out.println();
  }
  
  
  /**
   * Compara dois objetos <code>Pair</code> para
   * fins de ordenação, levando em consideração apenas
   * o agendamento <code>Schedule</code>.
   * @param p1 <code>Pair</code> a ser comparado.
   * @param p2 <code>Pair</code> a ser comparado.
   * @return <code>0</code> se os dois objetos forem iguais,
   * <code>-1</code> se <code>p1.schedule</code> for menor
   * do que <code>p2.schedule</code>, <code>1</code> caso contrário.
   */
  public int compare(Pair p1, Pair p2) {
    if(p1 == null || p2 == null
        || p1.schedule() == null
        || p2.schedule() == null)
      return 0;
    else if(p1.schedule().getCountdown() 
        < p2.schedule().getCountdown())
      return -1;
    else if(p1.schedule().getCountdown() 
        > p2.schedule().getCountdown())
      return 1;
    else return 0;
  }
  
  
  /**
   * Lista interna que suporta as funcionalidades de 
   * <code>JobList</code>.
   * @return <code>java.util.List&lt;Pair&gt;</code>.
   * @see us.pserver.scronV6.Pair
   */
  List<Pair> internal() { return list; }
  
  
  /**
   * Retorna o primeiro elemento da lista.
   * @return Primeiro elemento da lista.
   */
  public synchronized Pair peek() {
    return get(0);
  }
  
  
  /**
   * Remove e retorna o primeiro elemento da lista.
   * @return Primeiro elemento da lista.
   */
  public synchronized Pair pop() {
    return remove(0);
  }
  
  
  /**
   * Ordena a lista por ordem de ocorrência dos 
   * agendamentos <code>Schedule</code>.
   * @return Esta instância modificada de <code>JobList</code>.
   */
  public synchronized JobList sort() {
    if(this.isEmpty()) return this;
    try {
      lock.lock();
      list.sort(this::compare);
    } finally {
      lock.unlock();
    }
    return this;
  }
  
  
  /**
   * Remove todos os agendamentos inválidos.
   * @return Esta instância modificada de <code>JobList</code>.
   */
  public synchronized JobList removeInvalids() {
    if(isEmpty()) return this;
    try {
      lock.lock();
      List<Pair> l = new LinkedList();
      list.stream()
          .filter(p -> p.schedule().isValid())
          .forEach(l::add);
      list.clear();
      list.addAll(l);
      l.clear();
      l = null;
    } finally {
      lock.unlock();
    }
    return this;
  }
  
  
  /**
   * Insere o objeto <code>Pair</code> ao final da lista.
   * @param pair Objeto a ser inserido
   * @return Esta instância modificada de <code>JobList</code>.
   */
  public synchronized JobList put(Pair pair) {
    if(pair != null && pair.schedule() != null 
        && pair.job() != null) {
      try {
        lock.lock();
        list.add(pair);
      } finally {
        lock.unlock();
      }
    }
    return this;
  }
  
  
  /**
   * Insere todos os elementos de <code>Collection</code> 
   * ao final da lista.
   * @param col <code>Collections</code> cujos elementos
   * serão inseridos.
   * @return Esta instância modificada de <code>JobList</code>.
   */
  public synchronized JobList putAll(Collection<Pair> col) {
    if(col != null && !col.isEmpty()) {
      try {
        lock.lock();
        list.addAll(col);
      } finally {
        lock.unlock();
      }
    }
    return this;
  }
  
  
  /**
   * Retorna o elemento armazenado na posição 
   * <code>idx</code> da lista.
   * @param idx Índice do qual será retornado o elemento.
   * @return Objeto <code>Pair</code> armazenado na 
   * posição <code>idx</code>, ou <code>null</code>, 
   * caso não exista elemento armazenado no 
   * índice fornecido.
   */
  public synchronized Pair get(int idx) {
    Pair p = null;
    if(idx >= 0 && idx < size()) {
      try {
        lock.lock();
        p = list.get(idx);
      } finally {
        lock.unlock();
      }
    }
    return p;
  }
  
  
  /**
   * Retorna uma cópia da lista interna de <code>JobList</code>,
   * contendo referências à todos seus elementos.
   * @return <code>List&lt;Pair&gt;</code>.
   */
  public synchronized List<Pair> getAll() {
    if(list.isEmpty()) 
      return Collections.emptyList();
    try {
      LinkedList<Pair> copy = new LinkedList();
      lock.lock();
      Collections.copy(copy, list);
      return copy;
    } finally {
      lock.unlock();
    }
  }
  
  
  /**
   * Limpa a lista, removendo todos os elementos.
   * @return Esta instância modificada de <code>JobList</code>.
   */
  public synchronized JobList clear() {
    if(!isEmpty()) {
      try {
        lock.lock();
        list.clear();
      } finally {
        lock.unlock();
      }
    }
    return this;
  }
  
  
  /**
   * Remove e retorna o elemento armazenado na posição 
   * <code>idx</code> da lista.
   * @param idx Índice do qual será removido o elemento.
   * @return Objeto <code>Pair</code> removido, 
   * ou <code>null</code>, caso não exista elemento
   * armazenado no índice fornecido.
   */
  public synchronized Pair remove(int idx) {
    Pair p = null;
    if(idx >= 0 && idx < size()) {
      try {
        lock.lock();
        p = list.remove(idx);
      } finally {
        lock.unlock();
      }
    }
    return p;
  }
  
  
  /**
   * Remove da lista o elemento <code>Pair</code> 
   * fornecido como argumento.
   * @param pair Elemento a ser removido da lista.
   * @return <code>true</code> se o elemento for removido
   * com sucesso, <code>false</code> caso contrário.
   */
  public synchronized boolean remove(Pair pair) {
    boolean success = false;
    if(pair != null && !isEmpty()) {
      try {
        lock.lock();
        success = list.remove(pair);
      } finally {
        lock.unlock();
      }
    }
    return success;
  }
  
  
  /**
   * Verifica se a lista contém o elemento 
   * <code>Pair</code> fornecido como argumento.
   * @param pair Elemento a ser testado.
   * @return <code>true</code> se o objeto
   * está armazenado na lista, <code>false</code>
   * caso contrário.
   */
  public synchronized boolean contains(Pair pair) {
    boolean success = false;
    if(pair != null && !isEmpty()) {
      try {
        lock.lock();
        success = list.contains(pair);
      } finally {
        lock.unlock();
      }
    }
    return success;
  }
  
  
  /**
   * Retorna o número de elementos armazenados 
   * na lista.
   * @return Número de elementos na lista.
   */
  public synchronized int size() {
    return list.size();
  }
  
  
  /**
   * Verifica se a lista está vazia.
   * @return <code>true</code>
   */
  public synchronized boolean isEmpty() {
    return list.isEmpty();
  }
  
}
