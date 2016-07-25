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

package gson.test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 08/07/2016
 */
public class Bean1 {

  private String str;
  
  private double dbl;
  
  private boolean bol;
  
  private char[] chs;
  
  public List lst;
  
  
  public Bean1() {
    str = "super bean";
    dbl = 137.59991;
    bol = true;
    chs = new char[]{'a', 'b', 'c', '0', 1, 2};
    lst = new LinkedList();
    //lst.add(new Bean1());
    lst.add(Boolean.FALSE);
    lst.add("kernel");
  }
  
  
  public Bean1 add(Object o) {
    if(o != null) {
      lst.add(o);
    }
    return this;
  }


  @Override
  public int hashCode() {
    int hash = 7;
    hash = 79 * hash + Objects.hashCode(this.str);
    hash = 79 * hash + (int) (Double.doubleToLongBits(this.dbl) ^ (Double.doubleToLongBits(this.dbl) >>> 32));
    hash = 79 * hash + (this.bol ? 1 : 0);
    hash = 79 * hash + Arrays.hashCode(this.chs);
    hash = 79 * hash + Objects.hashCode(this.lst);
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
    final Bean1 other = (Bean1) obj;
    if (Double.doubleToLongBits(this.dbl) != Double.doubleToLongBits(other.dbl)) {
      return false;
    }
    if (this.bol != other.bol) {
      return false;
    }
    if (!Objects.equals(this.str, other.str)) {
      return false;
    }
    if (!Arrays.equals(this.chs, other.chs)) {
      return false;
    }
    if (!Objects.equals(this.lst, other.lst)) {
      return false;
    }
    return true;
  }


  @Override
  public String toString() {
    return "Bean1{" + "str=" + str + ", dbl=" + dbl + ", bol=" + bol + ", chs=" + Arrays.toString(chs) + ", lst=" + lst + '}';
  }
  
}
