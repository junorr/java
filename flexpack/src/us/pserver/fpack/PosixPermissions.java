/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.fpack;

import java.io.IOException;
import java.nio.file.attribute.PosixFilePermission;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import us.pserver.valid.Valid;


/**
 *
 * @author juno
 */
public class PosixPermissions {
  
  private List<PosixFilePermission> perms;
  
  
  public PosixPermissions() {
    perms = new LinkedList<>();
  }
  
  
  public PosixPermissions(String code) {
    this();
    this.setPermissions(code);
  }
  
  
  public PosixPermissions(Set<PosixFilePermission> set) {
    this();
    this.setPermissions(set);
  }
  
  
  public PosixPermissions add(PosixFilePermission p) {
    if(p != null) {
      perms.add(p);
    }
    return this;
  }
  
  
  public PosixFilePermission get(int i) {
    if(i < 0 || i >= perms.size())
      return null;
    return perms.get(i);
  }
  
  
  public PosixPermissions setPermissions(Set<PosixFilePermission> set) {
    Valid.off(set).forNull().fail(Set.class);
    if(!set.isEmpty()) {
      perms.clear();
      perms.addAll(set);
    }
    return this;
  }
  
  
  public PosixPermissions setPermissions(String code) {
    Valid.off(code).forEmpty().fail();
    try {
      Integer.parseInt(code);
      perms.clear();
      byte owner = Byte.parseByte(
          String.valueOf(code.charAt(0))
      );
      byte group = Byte.parseByte(
          String.valueOf(code.charAt(1))
      );
      byte others = Byte.parseByte(
          String.valueOf(code.charAt(2))
      );
      parseOwnerPermission(owner, perms);
      parseGroupPermission(group, perms);
      parseOthersPermission(others, perms);
    }
    catch(NumberFormatException e) {
      Valid.off(code).forNull().or()
          .forTest(code.length() != 9).fail();
      perms.clear();
      StringBuilder sb = new StringBuilder();
      sb.append(parsePermissionsString(code.substring(0, 3)))
          .append(parsePermissionsString(code.substring(3, 6)))
          .append(parsePermissionsString(code.substring(6)));
      setPermissions(sb.toString());
    } 
    return this;
  }
  
  
  public Set<PosixFilePermission> getPermissionsSet() {
    if(perms.isEmpty())
      return Collections.EMPTY_SET;
    return new HashSet<PosixFilePermission>(perms);
  }
  
  
  public List<PosixFilePermission> getPermissionsList() {
    return perms;
  }
  
  
  public String getPermissionsCode() {
    if(perms.isEmpty()) return null;
    Set<PosixFilePermission> set = getPermissionsSet();
    return new StringBuilder()
        .append(readOwnerPermissions(set))
        .append(readGroupPermissions(set))
        .append(readOthersPermissions(set))
        .toString();
  }
  

  public String getPermissionsString() {
    String scode = getPermissionsCode();
    if(scode == null) {
      return null;
    }
    byte owner = Byte.parseByte(
        String.valueOf(scode.charAt(0))
    );
    byte group = Byte.parseByte(
        String.valueOf(scode.charAt(1))
    );
    byte others = Byte.parseByte(
        String.valueOf(scode.charAt(2))
    );
    return new StringBuilder()
        .append(permToRWX(owner))
        .append(permToRWX(group))
        .append(permToRWX(others))
        .toString();
  }
  
  
  private byte parsePermissionsString(String str) {
    Valid.off(str).forNull().or()
        .forTest(str.length() != 3).fail();
    switch(str) {
      case "r--":
        return 1;
      case "-w-":
        return 2;
      case "rw-":
        return 3;
      case "--x":
        return 4;
      case "r-x":
        return 5;
      case "-wx":
        return 6;
      case "rwx":
        return 7;
      default:
        return 0;
    }
  }
  
  
  private byte readOwnerPermissions(Set<PosixFilePermission> set) {
    if(set != null) {
      byte pmod = 0;
      byte[] pcodes = {1,2,4};
      for(PosixFilePermission p : set) {
        switch(p) {
          case OWNER_READ:
            pmod += pcodes[0];
            break;
          case OWNER_WRITE:
            pmod += pcodes[1];
            break;
          case OWNER_EXECUTE:
            pmod += pcodes[2];
            break;
        }
      }
      return pmod;
    }
    return 0;
  }
  
  
  private byte readGroupPermissions(Set<PosixFilePermission> set) {
    if(set != null) {
      byte pmod = 0;
      byte[] pcodes = {1,2,4};
      for(PosixFilePermission p : set) {
        switch(p) {
          case GROUP_READ:
            pmod += pcodes[0];
            break;
          case GROUP_WRITE:
            pmod += pcodes[1];
            break;
          case GROUP_EXECUTE:
            pmod += pcodes[2];
            break;
        }
      }
      return pmod;
    }
    return 0;
  }
  
  
  private byte readOthersPermissions(Set<PosixFilePermission> set) {
    if(set != null) {
      byte pmod = 0;
      byte[] pcodes = {1,2,4};
      for(PosixFilePermission p : set) {
        switch(p) {
          case OTHERS_READ:
            pmod += pcodes[0];
            break;
          case OTHERS_WRITE:
            pmod += pcodes[1];
            break;
          case OTHERS_EXECUTE:
            pmod += pcodes[2];
            break;
        }
      }
      return pmod;
    }
    return 0;
  }
  
  
  private String permToRWX(byte b) {
    char[] perm = {'-', '-', '-'};
    switch(b) {
      case 1:
        perm[0] = 'r';
        break;
      case 2:
        perm[1] = 'w';
        break;
      case 3:
        perm[0] = 'r';
        perm[1] = 'w';
        break;
      case 4:
        perm[2] = 'x';
        break;
      case 5:
        perm[0] = 'r';
        perm[2] = 'x';
        break;
      case 6:
        perm[1] = 'w';
        perm[2] = 'x';
        break;
      case 7:
        perm[0] = 'r';
        perm[1] = 'w';
        perm[2] = 'x';
        break;
    }
    return new String(perm);
  }
  
  
  private void parseOwnerPermission(byte perm, List<PosixFilePermission> lperm) {
    switch(perm) {
      case 1:
        lperm.add(PosixFilePermission.OWNER_READ);
        break;
      case 2:
        lperm.add(PosixFilePermission.OWNER_WRITE);
        break;
      case 3:
        lperm.add(PosixFilePermission.OWNER_READ);
        lperm.add(PosixFilePermission.OWNER_WRITE);
        break;
      case 4:
        lperm.add(PosixFilePermission.OWNER_EXECUTE);
        break;
      case 5:
        lperm.add(PosixFilePermission.OWNER_READ);
        lperm.add(PosixFilePermission.OWNER_EXECUTE);
        break;
      case 6:
        lperm.add(PosixFilePermission.OWNER_WRITE);
        lperm.add(PosixFilePermission.OWNER_EXECUTE);
        break;
      case 7:
        lperm.add(PosixFilePermission.OWNER_READ);
        lperm.add(PosixFilePermission.OWNER_WRITE);
        lperm.add(PosixFilePermission.OWNER_EXECUTE);
        break;
    }
  }
  
  
  private void parseGroupPermission(byte perm, List<PosixFilePermission> lperm) {
    switch(perm) {
      case 1:
        lperm.add(PosixFilePermission.GROUP_READ);
        break;
      case 2:
        lperm.add(PosixFilePermission.GROUP_WRITE);
        break;
      case 3:
        lperm.add(PosixFilePermission.GROUP_READ);
        lperm.add(PosixFilePermission.GROUP_WRITE);
        break;
      case 4:
        lperm.add(PosixFilePermission.GROUP_EXECUTE);
        break;
      case 5:
        lperm.add(PosixFilePermission.GROUP_READ);
        lperm.add(PosixFilePermission.GROUP_EXECUTE);
        break;
      case 6:
        lperm.add(PosixFilePermission.GROUP_WRITE);
        lperm.add(PosixFilePermission.OWNER_EXECUTE);
        break;
      case 7:
        lperm.add(PosixFilePermission.GROUP_READ);
        lperm.add(PosixFilePermission.GROUP_WRITE);
        lperm.add(PosixFilePermission.GROUP_EXECUTE);
        break;
    }
  }
  
  
  private void parseOthersPermission(byte perm, List<PosixFilePermission> lperm) {
    switch(perm) {
      case 1:
        lperm.add(PosixFilePermission.OTHERS_READ);
        break;
      case 2:
        lperm.add(PosixFilePermission.OTHERS_WRITE);
        break;
      case 3:
        lperm.add(PosixFilePermission.OTHERS_READ);
        lperm.add(PosixFilePermission.OTHERS_WRITE);
        break;
      case 4:
        lperm.add(PosixFilePermission.OTHERS_EXECUTE);
        break;
      case 5:
        lperm.add(PosixFilePermission.OTHERS_READ);
        lperm.add(PosixFilePermission.OTHERS_EXECUTE);
        break;
      case 6:
        lperm.add(PosixFilePermission.OTHERS_WRITE);
        lperm.add(PosixFilePermission.OTHERS_EXECUTE);
        break;
      case 7:
        lperm.add(PosixFilePermission.OTHERS_READ);
        lperm.add(PosixFilePermission.OTHERS_WRITE);
        lperm.add(PosixFilePermission.OTHERS_EXECUTE);
        break;
    }
  }
  
}
