/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.bitbox.test;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import us.pserver.bitbox.annotation.BitCreate;
import us.pserver.bitbox.annotation.BitIgnore;


/**
 *
 * @author juno
 */
public interface IPerson {

  public int getAge();

  public String getFullName();

  public String getName();

  public String getLastName();

  @BitIgnore
  public LocalDate getBirth();
  
  public String stringBirth();

  public List<Address> getAddress();
  
  
  public static IPerson of(LocalDate birth, String lastName, String name) {
    return new Person(Collections.EMPTY_LIST, birth, lastName, name);
  }

  @BitCreate({"address", "birth", "lastName", "name"})
  public static IPerson of(List<Address> address, LocalDate birth, String lastName, String name) {
    return new Person(address, birth, lastName, name);
  }

}
