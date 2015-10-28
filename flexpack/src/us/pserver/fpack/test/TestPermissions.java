/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.fpack.test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermission;


/**
 *
 * @author juno
 */
public class TestPermissions {
  
  
  public static void main(String[] args) throws IOException {
    Path p = Paths.get("/home/juno/netmap");
    PosixFileAttributes pat = Files.readAttributes(p, PosixFileAttributes.class);
    for(PosixFilePermission perm : pat.permissions()) {
      System.out.println("* "+ perm);
    }
  }
  
}
