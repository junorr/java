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

package us.pserver.tools;


/**
 *
 * @author juno
 */
public class RoundDouble {
	
	private final int precision;
	
	private final double number;
	
	
	public RoundDouble(double number, int precision) {
		this.number = number;
		this.precision = precision;
	}
  
  
  public static double round(double number, int precision) {
    return new RoundDouble(number, precision).round();
  }
	
	
	public double round() {
    long i = (long) number;
    long d = Math.round((number - i) * Math.pow(10, precision));
    return i + d / Math.pow(10, precision);
	}
	
	
	public double getNumber() {
		return number;
	}
	
	
	public int getPrecision() {
		return precision;
	}


	@Override
	public int hashCode() {
		int hash = 3;
		hash = 59 * hash + this.precision;
		hash = 59 * hash + (int) (Double.doubleToLongBits(this.number) ^ (Double.doubleToLongBits(this.number) >>> 32));
		return hash;
	}


	@Override
	public boolean equals(Object obj) {
		if(this == obj) {
			return true;
		}
		if(obj == null) {
			return false;
		}
		if(getClass() != obj.getClass()) {
			return false;
		}
		final RoundDouble other = (RoundDouble) obj;
		if(this.precision != other.precision) {
			return false;
		}
		if(Double.doubleToLongBits(this.number) != Double.doubleToLongBits(other.number)) {
			return false;
		}
		return true;
	}


	@Override
	public String toString() {
		return String.valueOf(round());
	}
	
}
