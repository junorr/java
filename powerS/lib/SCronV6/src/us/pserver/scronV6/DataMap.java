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

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <b>DataMap</b> encapsula um <code>Map&lt;String, Object&gt;</code>
 * externalizando funções absolutamente seguras para ambientes
 * multithread, suportado internamente por 
 * <code>java.util.concurrent.locks.ReentrantLock</code>.
 * <b>DataMap</b> é utilizado em <code>SCronV6</code> para transferência de 
 * dados entre <code>Jobs</code> e <code>Threads</code>.
 * 
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 10/04/2014
 */
public class DataMap {

  private final ReentrantLock lock;
  
  private final Map<String, Object> map;
  
  
  /**
   * Construtor padrão sem argumentos.
   */
  public DataMap() {
    lock = new ReentrantLock(true);
    map = Collections.synchronizedMap(
        new TreeMap<String, Object>());
  }
  
  
  /**
   * Insere uma chave e valor em <code>DataMap</code>.
   * @param key Chave a ser inserida.
   * @param obj Valor a ser inserido.
   * @return Esta instância modificada de <code>DataMap</code>.
   */
  public synchronized DataMap put(String key, Object obj) {
    if(key != null && obj != null) {
      try {
        lock.lock();
        map.put(key, obj);
      } finally {
        lock.unlock();
      }
    }
    return this;
  }
  
  
  /**
   * Retorna o mapa interno de <code>DataMap</code>.
   * @return <code>java.util.Map&lt;String, Object&gt;</code>.
   */
  Map<String, Object> internal() {
    return map;
  }
  
  
  /**
   * Retorna o objeto armazenado sob a chave, 
   * ou <code>null</code> se a chave não existir.
   * @param key Chave cujo valor será recuperado.
   * @return O valor associado à chave, ou <code>null</code>
   * caso a chave não exista.
   */
  public synchronized Object get(String key) {
    Object obj = null;
    if(key != null && contains(key)) {
      try {
        lock.lock();
        obj = map.get(key);
      } finally {
        lock.unlock();
      }
    }
    return obj;
  }
  
  
  /**
   * Limpa todos os dados de <code>DataMap</code>.
   * @return Esta instância modificada de <code>DataMap</code>.
   */
  public synchronized DataMap clear() {
    if(!isEmpty()) {
      try {
        lock.lock();
        map.clear();
      } finally {
        lock.unlock();
      }
    }
    return this;
  }
  
  
  /**
   * Remove a chave e valor armazenados, 
   * retornando o objeto ou <code>null</code> 
   * se a chave não existir.
   * @param key Chave cujos dados serão removidos.
   * @return O valor associado à chave, ou <code>null</code>
   * caso a chave não exista.
   */
  public synchronized Object remove(String key) {
    Object obj = null;
    if(key != null && contains(key)) {
      try {
        lock.lock();
        obj = map.remove(key);
      } finally {
        lock.unlock();
      }
    }
    return obj;
  }
  
  
  /**
   * Verifica se <code>DataMap</code> possui a chave armazenada.
   * @param key Chave a ser testada.
   * @return <code>true</code> se a chave existir,
   * <code>false</code> caso contrário.
   */
  public synchronized boolean contains(String key) {
    boolean cont = false;
    if(key != null) {
      try {
        lock.lock();
        cont = map.containsKey(key);
      } finally {
        lock.unlock();
      }
    }
    return cont;
  }
  
  
  /**
   * Verifica se <code>DataMap</code> possui o valor armazenado.
   * @param obj Valor a ser testado.
   * @return <code>true</code> se o objeto existir,
   * <code>false</code> caso contrário.
   */
  public synchronized boolean contains(Object obj) {
    boolean cont = false;
    if(obj != null) {
      try {
        lock.lock();
        cont = map.containsValue(obj);
      } finally {
        lock.unlock();
      }
    }
    return cont;
  }
  
  
  /**
   * Retorna a quantidade de chaves/valores 
   * armazenados em <code>DataMap</code>.
   * @return Número de chaves/valores armazenados.
   */
  public synchronized int size() {
    return map.size();
  }
  
  
  /**
   * Verifica se <code>DataMap</code> está vazio.
   * @return <code>true</code> se nenhuma chave/valor
   * encontra-se armazenado, <code>false</code>
   * caso contrário.
   */
  public synchronized boolean isEmpty() {
    return map.isEmpty();
  }
  
}
