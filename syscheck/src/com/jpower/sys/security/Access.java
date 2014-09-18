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


package com.jpower.sys.security;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import us.pserver.date.SerialDate;
import us.pserver.date.SimpleDate;

/**
 *
 * @version 0.0 - 05/02/2013
 * @author Juno Roesler - juno.rr@gmail.com
 */
public class Access implements Serializable {

  private AccessType type;
  
  private SerialDate time;
  
  
  public Access(AccessType type, Date time) {
    if(type == null)
      throw new IllegalArgumentException(
          "Invalid AccessType: "+ type);
    if(time == null)
      throw new IllegalArgumentException(
          "Invalid Date: "+ time);
    this.type = type;
    this.time = new SerialDate(time);
  }
  
  
  public Access(AccessType type) {
    this(type, new Date());
  }


  public AccessType getType() {
    return type;
  }


  public void setType(AccessType type) {
    this.type = type;
  }


  public SimpleDate getTime() {
    return time.getDate();
  }


  public void setTime(Date time) {
    if(time != null)
      this.time = new SerialDate(time);
  }


  @Override
  public int hashCode() {
    int hash = 7;
    hash = 97 * hash + (this.type != null ? this.type.hashCode() : 0);
    hash = 97 * hash + Objects.hashCode(this.time);
    return hash;
  }


  @Override
  public boolean equals(Object obj) {
    if(obj == null) {
      return false;
    }
    if(obj.getClass() == AccessType.class)
      return equals((AccessType) obj);
    if(getClass() != obj.getClass()) {
      return false;
    }
    final Access other = (Access) obj;
    if(this.type != other.type) {
      return false;
    }
    if(!Objects.equals(this.time, other.time)) {
      return false;
    }
    return true;
  }
  
  
  public boolean equals(AccessType act) {
    return type != null
        && act != null
        && type.name().equals(act.name());
  }


  @Override
  public String toString() {
    return "Access{" + "type=" + type 
        + ", time=" + 
        (time == null ? "null" : time.getDate())
        + '}';
  }
  
}
