/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.fpack.test;

import com.cedarsoftware.util.io.JsonWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import us.pserver.fpack.FPackFileEntry;


/**
 *
 * @author juno
 */
public class TestFPackFileEntry {
  
  
  public static void main(String[] args) throws IOException {
    //Path path = Paths.get("/etc/profile");
    Path path = Paths.get("/home/juno");
    FPackFileEntry fe = new FPackFileEntry(path);
    fe.readPathAttributes();
    fe.printInfo(null);
    System.out.println();
    System.out.println(JsonWriter.objectToJson(fe));
    System.out.println();
    fe.ls(null, false, null);
    System.out.println();
    fe.ls();
  }
  
}
