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

import java.util.concurrent.locks.ReentrantLock;

/**
 * Armazena um valor de acesso exclusive,
 * implementado via 
 * <code>java.util.concurrent.locks.ReentrantLock</code>.
 * 
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 22/04/2014
 * @param <T> Elemento a ser armazenado.
 */
public class Exclusive<T> {

  private T t;
  
  private final ReentrantLock lock;
  
  
  /**
   * Construtor padrão e sem argumentos.
   */
  public Exclusive() {
    lock = new ReentrantLock();
    t = null;
  }
  
  
  /**
   * Construtor que recebe o valor a ser armazenado.
   * @param o Valor a ser armazenado.
   */
  public Exclusive(T o) {
    this();
    t = o;
  }
  
  
  /**
   * Verifica se o valor armazenado foi definido.
   * @return <code>true</code> se o valor armazenado
   * for diferente de null <code>null (value != null)</code>,
   * <code>false</code> caso contrário.
   */
  public synchronized boolean isPresent() {
    return t != null;
  }
  
  
  /**
   * Retorna o valor armazenado.
   * @return O valor armazenado.
   */
  public synchronized T get() {
    try {
      lock.lock();
      return t;
    } finally {
      lock.unlock();
    }
  }
  
  
  /**
   * Compara o valor armazenado com objeto informado.
   * @param o Objeto a ser comparado com o valor armazenado.
   * @return <code>true</code> se os objetos forem 
   * iguais <code>( value.equals(o) )</code>,
   * <code>false</code> caso contrário.
   */
  public synchronized boolean compare(T o) {
    try {
      lock.lock();
      if((t == null && o == null)
          || t == o)
        return true;
      else
        return t.equals(o);
    } finally {
      lock.unlock();
    }
  }
  
  
  /**
   * Define o valor de <code>Exclusive</code>.
   * @param o O valor a ser armazenado.
   * @return Esta instância modificada de <code>Exclusive</code>.
   */
  public synchronized Exclusive<T> set(T o) {
    try {
      lock.lock();
      t = o;
    } finally {
      lock.unlock();
    }
    return this;
  }
  
  
  public synchronized boolean isTrue() {
    if(!isPresent()) return false;
    try {
      lock.lock();
      return (t instanceof Boolean)
          && Boolean.TRUE.equals(t);
    } finally {
      lock.unlock();
    }
  }
  
}
