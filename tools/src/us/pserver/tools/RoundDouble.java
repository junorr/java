/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
