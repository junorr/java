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

package br.com.bb.disec.micro.util;

import java.util.Objects;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 26/08/2016
 */
public class Identation {

  private final StringBuilder ident;
  
  private final int spaces;
  
  private int post;
  
  
  public Identation(int spaces, int init) {
    if(spaces < 1) {
      throw new IllegalArgumentException("Bad Spaces Value: "+ spaces);
    }
    this.spaces = spaces;
    this.ident = new StringBuilder();
    this.tab(init);
    post = 0;
  }
  
  
  private void tab(int size) {
    for(int i = 0; i < size; i++) {
      ident.append(" ");
    }
  }
  
  
  private void untab(int size) {
    if(size > 0) {
      ident.delete(
          ident.length()-size, 
          ident.length()
      );
    }
  }
  
  
  public Identation increment() {
    this.tab(spaces);
    return this;
  }
  
  
  public Identation decrement() {
    this.untab(spaces);
    return this;
  }
  
  
  public Identation postIncrement() {
    post = 1;
    return this;
  }


  public Identation postDecrement() {
    post = -1;
    return this;
  }


  @Override
  public int hashCode() {
    int hash = 7;
    hash = 23 * hash + Objects.hashCode(this.ident);
    hash = 23 * hash + this.spaces;
    return hash;
  }


  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final Identation other = (Identation) obj;
    if (this.spaces != other.spaces) {
      return false;
    }
    if (!Objects.equals(this.ident, other.ident)) {
      return false;
    }
    return true;
  }
  
  
  @Override
  public String toString() {
    try {
      return ident.toString();
    } finally {
      if(post > 0) this.increment();
      else if(post < 0) this.decrement();
      post = 0;
    }
  }
  
}
