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

package us.pserver.remote;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Armazena os objetos no servidor, vinculando-os 
 * à uma chave de recuperação <code>String</code>.
 * <code>ObjectContainer</code> é seguro para ambientes
 * com múltiplos processos.
 * 
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 21/01/2014
 */
public class ObjectContainer {
  
  private final Map<String, Object> objs;
  
  
  /**
   * Construtor padrão sem argumentos.
   */
  public ObjectContainer() {
    objs = new ConcurrentHashMap<>();
  }
  
  
  /**
   * Insere um objeto a ser armazenado com
   * uma chave <code>String</code> de recuperação.
   * @param name Chave <code>String</code> de recuperação 
   * do objeto armazenado.
   * @param obj Objeto a ser armazenado.
   * @return Esta instância modificada de <code>ObjectContainer</code>.
   */
  public ObjectContainer put(String name, Object obj) {
    if(name != null && obj != null)
      objs.put(name, obj);
    return this;
  }
  
  
  /**
   * Remove um objeto do container através da 
   * chave de identificação.
   * @param name Chave <code>String</code>.
   * @return O objeto removido do container.
   */
  public Object remove(String name) {
    return objs.remove(name);
  }
  
  
  /**
   * Verifica se existe um objeto armazenado,
   * identificado pela chave fornecida.
   * @param name Chave <code>String</code>.
   * @return <code>true</code> se existir um
   * objeto armazenado identificado pela chave fornecida,
   * <code>false</code> caso contrário.
   */
  public boolean contains(String name) {
    return objs.containsKey(name);
  }
  
  
  /**
   * Retorna o objeto armazenado no container,
   * identificado pela chave fornecida.
   * @param name Chave <code>String</code>.
   * @return O Objeto armazenado ou <code>null</code>
   * caso a chave de identificação não exista no container.
   */
  public Object get(String name) {
    return objs.get(name);
  }
  
  
  /**
   * Retorna uma instância de 
   * <code>java.util.concurrent.ConcurrentHashMap</code> 
   * que suporta as funcionalidades de 
   * <code>ObjectContainer</code>.
   * @return <code>java.util.concurrent.ConcurrentHashMap</code>.
   */
  public Map<String, Object> objectMap() {
    return objs;
  }
  
  
  /**
   * Retorna a quantidade de objetos armazenados.
   * @return <code>int</code>.
   */
  public int size() {
    return objs.size();
  }
  
  
  /**
   * Retorna um <code>Iterator</code> com todas
   * as chaves de identificação armazenadas.
   * @return <code>Iterator</code>.
   */
  public Iterator<String> names() {
    return objs.keySet().iterator();
  }
  
  
  /**
   * Retorna um <code>Iterator</code> com todos
   * os objetos armazenadas.
   * @return <code>Iterator</code>.
   */
  public Iterator objects() {
    return objs.values().iterator();
  }
  
}
