/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.bitbox.test;

import java.util.Arrays;
import java.util.Objects;


/**
 *
 * @author juno
 */
public class Address {

  public static enum UF {
    DF, RS;
  }

  private final String street;

  private final int[] numbers;

  private final String neighborhood;

  private final String complement;

  private final String city;

  private final UF uf;

  private final long zip;
  

  public Address(String street, int[] numbers, String neighborhood, String complement, String city, UF uf, long zip) {
    this.street = street;
    this.numbers = numbers;
    this.neighborhood = neighborhood;
    this.complement = complement;
    this.city = city;
    this.uf = uf;
    this.zip = zip;
  }

  public String getStreet() {
    return street;
  }

  public int[] getNumbers() {
    return numbers;
  }

  public String getNeighborhood() {
    return neighborhood;
  }

  public String getComplement() {
    return complement;
  }

  public String getCity() {
    return city;
  }

  public UF getUf() {
    return uf;
  }

  public long getZip() {
    return zip;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 17 * hash + Objects.hashCode(this.street);
    hash = 17 * hash + Objects.hashCode(this.numbers);
    hash = 17 * hash + Objects.hashCode(this.neighborhood);
    hash = 17 * hash + Objects.hashCode(this.complement);
    hash = 17 * hash + Objects.hashCode(this.city);
    hash = 17 * hash + Objects.hashCode(this.uf);
    hash = 17 * hash + (int) (this.zip ^ (this.zip >>> 32));
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
    final Address other = (Address) obj;
    if (this.numbers != other.numbers) {
      return false;
    }
    if (this.zip != other.zip) {
      return false;
    }
    if (!Objects.equals(this.street, other.street)) {
      return false;
    }
    if (!Objects.equals(this.neighborhood, other.neighborhood)) {
      return false;
    }
    if (!Objects.equals(this.complement, other.complement)) {
      return false;
    }
    if (!Objects.equals(this.city, other.city)) {
      return false;
    }
    return this.uf == other.uf;
  }

  @Override
  public String toString() {
    return "Address{" + "street=" + street + ", numbers=" + Arrays.toString(numbers) + ", neighborhood=" + neighborhood + ", complement=" + complement + ", city=" + city + ", uf=" + uf + ", zip=" + zip + '}';
  }

}
