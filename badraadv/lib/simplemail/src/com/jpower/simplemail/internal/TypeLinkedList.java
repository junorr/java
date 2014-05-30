/*
 * Direitos Autorais Reservados (c) 2011 Juno Roesler
 * Contato: juno.rr@gmail.com
 * 
 * Esta biblioteca é software livre; você pode redistribuí-la e/ou modificá-la sob os
 * termos da Licença Pública Geral Menor do GNU conforme publicada pela Free
 * Software Foundation; tanto a versão 2.1 da Licença, ou (a seu critério) qualquer
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

package com.jpower.simplemail.internal;

import com.jpower.annotations.info.Contact;
import com.jpower.annotations.info.Copyright;
import com.jpower.annotations.info.License;
import com.jpower.annotations.info.Version;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


/**
 * <p style="font-size: medium;">
 * TypeLinkedList representa uma coleção 
 * genérica de objetos, suportado por uma
 * lista interna.
 * </p>
 * 
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 2011.10.13
 */
@Version (
		value			= "1.0",
    name      = "TypeLinkedList",
		date			= "2011.10.13",
		author		= "Juno Roesler",
		synopsis	= "Lista encadeada genérica"
)
@Copyright("2011 Juno Roesler")
@Contact("juno.rr@gmail.com")
@License("GNU/LGPL v2.1")
public abstract class TypeLinkedList<T> {
  
  protected List<T> list;
  
  
  /**
   * Construtor padrão sem argumentos, instancia
   * a lista interna.
   */
  protected TypeLinkedList() {
    list = new LinkedList<T>();
  }
  
  
  /**
   * Adiciona um objeto à lista.
   * @param t Objeto a ser adicionado.
   * @return Esta instância modificada.
   */
  public TypeLinkedList add(T t) {
    list.add(t);
    return this;
  }
  
  
  /**
   * Remove um objeto da lista.
   * @param t Objeto a ser removido.
   * @return <code>true</code> se o objeto foi
   * removido com sucesso, <code>false</code>
   * caso contrario.
   */
  public boolean remove(T t) {
    return list.remove(t);
  }
  
  
  /**
   * Retorna o primeiro objeto da lista.
   * @return O primeiro objeto da lista.
   */
  public T first() {
    if(list.isEmpty()) return null;
    return list.get(0);
  }
  
  
  /**
   * Retorna uma lista com todos objetos armazenados.
   * @return List com o tipo de dados definido por <code>T</code>.
   */
  public List<T> getList() {
    return list;
  }
  
  
  /**
   * Limpa a lista, removendo todos os 
   * objetos contidos.
   */
  public void clear() {
    list.clear();
  }
  
  
  /**
   * Retorna o tamanho atual da lista.
   * @return o número de objetos contidos 
   * na lista;
   */
  public int size() {
    return list.size();
  }
  
  
  /**
   * Retorna <code>boolean</code> indicando se
   * a lista está vazia ou não.
   * @return <code>true</code> se a lista não
   * contiver nenhum objeto, <code>false</code>
   * caso contrário.
   */
  public boolean isEmpty() {
    return list.isEmpty();
  }
  
  
  /**
   * Retorna um <code>Iterator</code> com
   * os objetos da lista.
   * @return <code>Iterator</code> com
   * os objetos da lista.
   */
  public Iterator<T> iterator() {
    return list.iterator();
  }
  
  
  /**
   * Retorna um array contendo todos os 
   * objetos da coleção.
   * @return array contendo os objetos da coleção.
   */
  public abstract T[] toArray();
  
}
