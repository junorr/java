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


package com.jpower.pstat;

import java.util.Iterator;
import java.util.LinkedList;

/**
 *
 * @version 0.0 - 23/01/2013
 * @author Juno Roesler - juno.rr@gmail.com
 */
public class LimitedStack<T> {

  private LinkedList<T> stack;
  
  private int size;
  
  
  public LimitedStack(int size) {
    if(size <= 0) throw new IllegalArgumentException(
        "Invalid size: "+ size);
    this.size = size;
    stack = new LinkedList<>();
  }
  
  
  public LimitedStack push(T t) {
    if(t != null) {
      stack.addFirst(t);
      if(stack.size() > size)
        stack.pollLast();
    }
    return this;
  }
  
  
  public LimitedStack add(T t) {
    return push(t);
  }
  
  
  public int size() {
    return stack.size();
  }
  
  
  public boolean isEmpty() {
    return stack.isEmpty();
  }
  
  
  public T peek() {
    if(stack.isEmpty())
      return null;
    return stack.peek();
  }
  
  
  public T pop() {
    if(stack.isEmpty())
      return null;
    return stack.pop();
  }
  
  
  public LimitedStack clear() {
    stack.clear();
    return this;
  }
  
  
  public Iterator<T> descIterator() {
    return stack.descendingIterator();
  }
  
  
  public Iterator<T> ascIterator() {
    return stack.iterator();
  }
  
}
