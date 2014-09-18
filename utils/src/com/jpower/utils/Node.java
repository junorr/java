/*
  Direitos Autorais Reservados (c) 2009 Juno Roesler
  Contato com o autor: powernet.de@gmail.com

  Esta biblioteca é software livre; você pode redistribuí-la e/ou modificá-la sob os
  termos da Licença Pública Geral Menor do GNU conforme publicada pela Free
  Software Foundation; tanto a versão 2.1 da Licença, ou (a seu critério) qualquer
  versão posterior.

  Esta biblioteca é distribuído na expectativa de que seja útil, porém, SEM
  NENHUMA GARANTIA; nem mesmo a garantia implícita de COMERCIABILIDADE
  OU ADEQUAÇÃO A UMA FINALIDADE ESPECÍFICA. Consulte a Licença Pública
  Geral Menor do GNU para mais detalhes.

  Você deve ter recebido uma cópia da Licença Pública Geral Menor do GNU junto
  com esta biblioteca; se não, escreva para a Free Software Foundation, Inc., no
  endereço 59 Temple Street, Suite 330, Boston, MA 02111-1307 USA.
*/

package com.jpower.utils;


/**
 * Node representa um nodo de uma lista
 * lincada e encapsula um objeto a ser
 * armazenado.
 * @author Juno Roesler
 */
public class Node {

  private Node prev, next;

  private Object object;


  /**
   * Construtor que recebe um objeto
   * @param obj Objeto a ser armazenado.
   */
  public Node(Object obj) {
    prev = next = null;
    this.object = obj;
  }//DescNode


  /**
   * Construtor que recebe o nodo anterior,
   * o objeto a ser armazenado e o próximo nodo
   * da lista.
   * @param prev Nodo anterior.
   * @param obj Objeto a ser armazenado.
   * @param next Próximo nodo.
   */
  public Node(Node prev, Object obj, Node next) {
    this.prev = prev;
    this.object = obj;
    this.next = next;
  }//DescNode


  /**
   * Retorna o objeto armazenado.
   * @return <code>Object</code> armazenado.
   */
  public Object getObject() {
    return object;
  }//getDescription


  /**
   * Retorna o nodo anterior.
   * @return Nodo anterior.
   */
  public Node prev() {
    return prev;
  }//prev


  /**
   * Rtorna o próximo nodo da lista.
   * @return Próximo nodo.
   */
  public Node next() {
    return next;
  }//next


  /**
   * Seta o objeto a ser encapsulada.
   * @param obj Objeto a ser armazenado.
   */
  public void setObject(Object obj) {
    this.object = obj;
  }//setDesc


  /**
   * Seta o nodo anterior.
   * @param prev Nodo anterior.
   */
  public void setPrev(Node prev) {
    this.prev = prev;
  }//setPrev


  /**
   * Seta o próximo nodo da lista.
   * @param next Próximo nodo.
   */
  public void setNext(Node next) {
    this.next = next;
  }//setNext


}//class
