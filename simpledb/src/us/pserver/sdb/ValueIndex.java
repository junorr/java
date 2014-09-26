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

package us.pserver.sdb;

import java.util.Objects;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 26/09/2014
 */
public class ValueIndex {

  private String value;
  
  private long index;


  public ValueIndex() {
    value = null;
    index = -1;
  }


  public ValueIndex(String value, long index) {
    this.value = value;
    this.index = index;
  }


  public ValueIndex(String value) {
    this.value = value;
    index = -1;
  }


  public String getValue() {
    return value;
  }


  public void setValue(String value) {
    this.value = value;
  }


  public long getIndex() {
    return index;
  }


  public void setIndex(long index) {
    this.index = index;
  }


  @Override
  public int hashCode() {
    int hash = 7;
    hash = 97 * hash + Objects.hashCode(this.value);
    return hash;
  }


  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final ValueIndex other = (ValueIndex) obj;
    if (!Objects.equals(this.value, other.value)) {
      return false;
    }
    return true;
  }


  @Override
  public String toString() {
    return "ValueIndex(" + "value=" + value + ", index=" + index + ')';
  }
  
}
