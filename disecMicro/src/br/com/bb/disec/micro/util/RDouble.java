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
 * @version 0.0 - 26/07/2016
 */
public class RDouble extends Number {

  private final Double value;
  
  
  public RDouble(Double value) {
    this.value = value;
  }
  
  
  public Double value() {
    return value;
  }
  
  
  public RDouble value(Double value) {
    return RDouble.of(value);
  }
  
  
  public static RDouble of(Double d) {
    return new RDouble(d);
  }
  
  
  public RDouble round(int dec) {
    double pow = Math.pow(10, dec);
    long l = (long) (value * pow);
    return RDouble.of(l / pow);
  }
  
  
  public Double getRounded(int dec) {
    double pow = Math.pow(10, dec);
    long l = (long) (value * pow);
    return l / pow;
  }


  @Override
  public int intValue() {
    return value.intValue();
  }


  @Override
  public long longValue() {
    return value.longValue();
  }


  @Override
  public float floatValue() {
    return value.floatValue();
  }


  @Override
  public double doubleValue() {
    return value;
  }
  

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 89 * hash + Objects.hashCode(this.value);
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
    if(getClass() != obj.getClass()) {
      if(Double.class == obj.getClass()) {
        obj = of((Double) obj);
      } else {
        return false;
      }
    }
    final RDouble other = (RDouble) obj;
    return Objects.equals(this.value, other.value);
  }
  
  
  @Override
  public String toString() {
    return String.valueOf(this.getRounded(4));
  }

}
