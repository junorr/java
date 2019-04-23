/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.bitbox.test;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import us.pserver.bitbox.BitCreate;


/**
 *
 * @author juno
 */
public interface IPerson {

  public int getAge();

  public String getFullName();

  public String getName();

  public String getLastName();

  public LocalDate getBirth();
  
  public String stringBirth();

  public List<Address> getAddress();
  
  
  @BitCreate
  public static IPerson of(String name, String lastName, LocalDate birth) {
    return new Person(name, lastName, birth, Collections.EMPTY_LIST);
  }

  @BitCreate
  public static IPerson of(String name, String lastName, LocalDate birth, List<Address> address) {
    return new Person(name, lastName, birth, address);
  }

}
