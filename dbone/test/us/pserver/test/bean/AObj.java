/*
 * Direitos Autorais Reservados (c) 2011 Juno Roesler
 * Contato: juno.rr@gmail.com
 * 
 * Esta biblioteca � software livre; voc� pode redistribu�-la e/ou modific�-la sob os
 * termos da Licen�a P�blica Geral Menor do GNU conforme publicada pela Free
 * Software Foundation; tanto a vers�o 2.1 da Licen�a, ou qualquer
 * vers�o posterior.
 * 
 * Esta biblioteca � distribu�da na expectativa de que seja �til, por�m, SEM
 * NENHUMA GARANTIA; nem mesmo a garantia impl�cita de COMERCIABILIDADE
 * OU ADEQUA��O A UMA FINALIDADE ESPEC�FICA. Consulte a Licen�a P�blica
 * Geral Menor do GNU para mais detalhes.
 * 
 * Voc� deve ter recebido uma c�pia da Licen�a P�blica Geral Menor do GNU junto
 * com esta biblioteca; se n�o, acesse 
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html, 
 * ou escreva para a Free Software Foundation, Inc., no
 * endere�o 59 Temple Street, Suite 330, Boston, MA 02111-1307 USA.
 */

package us.pserver.test.bean;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;
import us.pserver.dbone.index.Indexed;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 06/09/2017
 */
public class AObj implements Serializable {

  private final String name;
  
  private final int age;
  
  private final int[] magic;
  
  private final char[] chars;
  
  private final Date date;


  private AObj() {
    System.out.println("--- AObj using empty constructor");
    this.name = null;
    this.age = 0;
    this.magic = null;
    this.chars = null;
    this.date = null;
  }


  public AObj(String name, int age, int[] magic, char[] chars, Date date) {
    this.name = name;
    this.age = age;
    this.magic = magic;
    this.chars = chars;
    this.date = date;
  }


  public AObj(String name, Date dt) {
    this(name, 0, null, null, dt);
  }
  
  @Indexed
  public String name() {
    System.out.println("AObj.name():String method invoked!");
    return name;
  }

  public int age() {
    System.out.println("AObj.age():int method invoked!");
    return age;
  }

  public int[] getMagic() {
    return magic;
  }

  public char[] getChars() {
    return chars;
  }

  public Date getDate() {
    System.out.println("AObj.getDate():Date method invoked!");
    return date;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 53 * hash + Objects.hashCode(this.name);
    hash = 53 * hash + this.age;
    hash = 53 * hash + Arrays.hashCode(this.magic);
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
    final AObj other = (AObj) obj;
    if (this.age != other.age) {
      return false;
    }
    if (!Objects.equals(this.name, other.name)) {
      return false;
    }
    if (!Arrays.equals(this.magic, other.magic)) {
      return false;
    }
    return true;
  }


  @Override
  public String toString() {
    return "AObj{" + "name=" + name + ", age=" + age + ", magic=" + Arrays.toString(magic) + ", chars=" + Arrays.toString(chars) + ", date=" + date + '}';
  }

}
