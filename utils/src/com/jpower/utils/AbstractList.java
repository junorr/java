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
 * Classe abstrata que implementa uma lista
 * de nodos conectados entre si.
 * @author Juno Roesler
 */
public abstract class AbstractList {

  private Node head, tail, pointer;

  private int size;


  /**
   * Construtor protegido padrão.
   */
  protected AbstractList() {
    head = tail = pointer = null;
    size = 0;
  }//method()


  /**
   * Retorna o primeiro nodo da lista.
   * @return Primeiro nodo.
   */
  public Node getHead() {
    return head;
  }//method()


  /**
   * Seta o primeiro nodo da lista.
   * @param head Primeiro nodo.
   */
  public void setHead(Node head) {
    this.head = head;
  }//method()


  /**
   * Retorna o último nodo da lista.
   * @return Último nodo.
   */
  public Node getTail() {//{{{
    return tail;
  }//method()}}}



  /**
   * Seta o último nodo da lista.
   * @param tail Último nodo.
   */
  public void setTail(Node tail) {
//{{{
    this.tail = tail;
  }//method()
//}}}


  /**
   * Retorna o número de nodos existentes
   * na lista.
   * @return Número de nodos existentes.
   */
  public int size() {
    return size;
  }//method()


  /**
   * Retorna o próximo nodo da lista.
   * @return Próximo nodo da lista.
   */
  public Node next() {
    if(pointer == null) {
      pointer = head;
      return null;
    }//if

    Node n = pointer;
    pointer = pointer.next();
    return n;
  }//method()


  /**
   * Retorna o nodo anterior da lista.
   * @return Nodo anterior.
   */
  public Node prev() {
    if(pointer == null)
      return null;

    pointer = pointer.prev();

    if(pointer != null)
      return pointer;
    else {
      pointer = tail;
      return null;
    }//if-else
  }//method()


  /**
   * Retorna o objeto armazenado pelo
   * próximo nodo da lista.
   * @return O objeto armazenado.
   */
  public Object nextObject() {
    Node n = this.next();
    if(n != null)
      return n.getObject();
    else
      return null;
  }//method()


  /**
   * Indica se existem mais nodos na lista.
   * @return <code>boolean</code> indicando
   * se existem mais nodos na lista ou não.
   */
  public boolean hasMoreObjects() {
    if(pointer != null)
      return true;
    else {
      resetPointer();
      return false;
    }//else
  }//method()


  /**
   * Reseta o ponteiro do nodo atual,
   * indicando o primeiro elemento da
   * lista.
   */
  public void resetPointer() {
    pointer = head;
  }//method()


  /**
   * Adiciona um objeto à lista.
   * @param obj Objeto a ser adicionado.
   */
  public void add(Object obj) {
    if(head == null) {
      head = new Node(obj);
      tail = pointer = head;
    } else {
      tail = new Node(tail, obj, null);
      tail.prev().setNext(tail);
    }//if-else
    size++;
  }//method()


  /**
   * Verifica se o objeto indicado existe
   * na lista, através de <code>obj.equals(Object)</code>.
   * @param obj Objeto a ser verificado.
   * @return <code>boolean</code> indicando
   * se a lista contém o objeto indicado.
   */
  public boolean contains(Object obj) {
    if(this.containsObject(obj) != null)
      return true;
    return false;
  }//method()


  /**
   * Verifica se o objeto indicado existe
   * na lista, através de <code>obj.equals(Object)</code>,
   * instanciando um <code>Integer</code> com o índice
   * dele no segundo parâmetro.
   * @param obj Objeto a ser verificado.
   * @return Nodo que contém o objeto indicado, ou
   * <code>null</code> caso não seja encontrado na
   * lista.
   */
  public Node containsObject(Object obj) {
    if(size == 0)
      return null;

    this.resetPointer();
    while(this.hasMoreObjects()) {
      Node n = this.next();
      if(n.getObject() == null && obj == null)
        return n;
      else if(n.getObject().equals(obj))
        return n;
    }//while
    return null;
  }//method()


  /**
   * Pesquisa e retorna o índice do objeto
   * na lista.
   * @param obj Objeto a ser pesquisado.
   * @return O índice do objeto na lista, ou
   * <code>-1</code> se este for <code>null</code>
   * ou não existir.
   */
  public int indexOf(Object obj) {
    int value = -1;
    this.resetPointer();
    while(this.hasMoreObjects()) {
      value++;
      Object o = this.nextObject();
      if(o == null && obj == null)
        return value;
      else if(o.equals(obj))
        return value;
    }//while
    return value;
  }//method()


  /**
   * Retorna o objeto contido
   * no índice informado da lista.
   * @param index índice do objeto.
   * @return O objeto contido no índice.
   */
  public Object get(int index) {
    if(index < 0 || index > this.size())
      return null;

    this.resetPointer();
    Object o = null;
    for(int i = 0; i <= index; i++) {
      o = this.nextObject();
    }//for
    return o;
  }//method()


  /**
   * Remove o nodo da lista.
   * @param n Nodo a ser removido.
   * @return <code>boolean</code> indicando
   * o se o nodo foi removido com sucesso.
   */
  public boolean remove(Node n) {
    if(size == 0)
      return false;

    while(this.hasMoreObjects()) {
      Node node = this.next();
      if(node == n) {
        node.prev().setNext(node.next());
        size--;
        return true;
      }//if
    }//while

    return false;
  }//method()


  /**
   * Remove o nodo indicado da lista sem
   * verificar se este encontra-se na lista.
   */
  private boolean directRemove(Node n) {
    if(n == null)
      return false;
    n.prev().setNext(n.next());
    n = null;
    size--;
    return true;
  }//method()


  /**
   * Remove o objeto indicado da lista.
   * @param obj Objeto a ser removido.
   * @return <code>boolean</code> indicando
   * se o objeto foi removido com sucesso.
   */
  public boolean remove(Object obj) {
    return this.directRemove(this.containsObject(obj));
  }//method()


  /**
   * Elimina todos os elementos armazenados.
   */
  public void clear() {
    head = tail = pointer = null;
    size = 0;
  }//method()


}//class
