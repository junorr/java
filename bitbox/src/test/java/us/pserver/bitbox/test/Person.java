/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.bitbox.test;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import us.pserver.bitbox.BitCreate;
import us.pserver.bitbox.BitProperty;
import us.pserver.bitbox.BitType;


/**
 *
 * @author juno
 */
@BitType(IPerson.class)
public class Person implements IPerson {

  private final String name;

  private final String lastName;

  private final LocalDate birth;

  private final List<Address> address;
  

  public Person(String lastName, String name) {
    this.name = name;
    this.lastName = lastName;
    this.birth = LocalDate.now();
    this.address = Collections.EMPTY_LIST;
  }

  //@BitCreate({"address", "birth", "lastName", "name"})
  public Person(List<Address> address, LocalDate birth, String lastName, String name) {
    this.name = name;
    this.lastName = lastName;
    this.birth = birth;
    this.address = address;
  }

  public int getAge() {
    return (int) birth.until(LocalDate.now(), ChronoUnit.YEARS);
  }

  public String getFullName() {
    return String.format("%s %s", name, lastName);
  }

  public String getName() {
    return name;
  }

  public String getLastName() {
    return lastName;
  }

  //@BitIgnore
  public LocalDate getBirth() {
    return birth;
  }
  
  @BitProperty("strbirth")
  public String stringBirth() {
    return getBirth().toString();
  }

  public List<Address> getAddress() {
    return address;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 37 * hash + Objects.hashCode(this.name);
    hash = 37 * hash + Objects.hashCode(this.lastName);
    hash = 37 * hash + Objects.hashCode(this.birth);
    hash = 37 * hash + Objects.hashCode(this.address);
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
    final Person other = (Person) obj;
    if (!Objects.equals(this.name, other.name)) {
      return false;
    }
    if (!Objects.equals(this.lastName, other.lastName)) {
      return false;
    }
    if (!Objects.equals(this.birth, other.birth)) {
      return false;
    }
    return Objects.equals(this.address, other.address);
  }

  @Override
  public String toString() {
    return "Person{" + "name=" + getFullName() + ", birth=" + birth + ", address=" + address + '}';
  }

}
