/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jpower.sys.security;

import com.thoughtworks.xstream.XStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;



/**
 *
 * @author juno
 */
public class UserXmlIO {
  
  private XStream xml;
  
  
  public UserXmlIO() {
    xml = new XStream();
  }
  
  
  public boolean save(User u, String file) {
    if(u == null || file == null)
      return false;
    try {
      xml.toXML(u, new FileOutputStream(file));
      return true;
    } catch(FileNotFoundException ex) {
      return false;
    }
  }
  
  
  public User load(String file) {
    if(file == null)
      return null;
    try {
      return (User) xml.fromXML(new FileInputStream(file));
    } catch(FileNotFoundException ex) {
      return null;
    }
  }
  
  
  public static void main(String[] args) {
    User u = new User();
    u.setAdmin(true);
    u.setEmail("juno@pserver.us");
    u.setName("Juno Roesler");
    u.setPass("0988");
    u.getAccess().add(new Access(AccessType.ALL));
    UserXmlIO uio = new UserXmlIO();
    uio.save(u, "./user.xml");
    u = uio.load("./user.xml");
    System.out.println("* validate: "+u.getPassword().validate(new Password("0988")));
  }
  
}
