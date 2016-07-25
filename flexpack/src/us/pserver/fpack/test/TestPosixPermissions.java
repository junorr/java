/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.fpack.test;

import us.pserver.fpack.PosixPermissions;


/**
 *
 * @author juno
 */
public class TestPosixPermissions {
  
  
  public static void main(String[] args) {
    System.out.println("----{ '311' }----");
    PosixPermissions perm = new PosixPermissions("600");
    System.out.println("* perm.getPermissionsCode()..: "+ perm.getPermissionsCode());
    System.out.println("* perm.getPermissionsString(): "+ perm.getPermissionsString());
    System.out.println("* perm.getPermissionsSet()...: ");
    perm.getPermissionsSet().stream().sorted()
        .forEach(p->System.out.println("  - "+ p.name()));
    
    System.out.println("----{ Set<PosixFilePermission> }----");
    perm.setPermissions(perm.getPermissionsSet());
    System.out.println("* perm.getPermissionsCode()..: "+ perm.getPermissionsCode());
    System.out.println("* perm.getPermissionsString(): "+ perm.getPermissionsString());
    System.out.println("* perm.getPermissionsSet()...: ");
    perm.getPermissionsSet().stream().sorted()
        .forEach(p->System.out.println("  - "+ p.name()));
    
    System.out.println("----{ 'rw-r--r--' }----");
    perm.setPermissions("rw-r--r--");
    System.out.println("* perm.getPermissionsCode()..: "+ perm.getPermissionsCode());
    System.out.println("* perm.getPermissionsString(): "+ perm.getPermissionsString());
    System.out.println("* perm.getPermissionsSet()...: ");
    perm.getPermissionsSet().stream().sorted()
        .forEach(p->System.out.println("  - "+ p.name()));
  }
  
}
