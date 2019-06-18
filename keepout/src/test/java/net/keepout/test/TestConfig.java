/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.keepout.test;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import net.keepout.config.Config;
import net.powercoder.cdr.hex.HexStringCoder;
import org.junit.jupiter.api.Test;


/**
 *
 * @author juno
 */
public class TestConfig {
  
  @Test
  public void test_classpath_config() {
    try {
      System.out.println(Config.loadClasspath("config.yml"));
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  
  @Test
  public void str_to_hex() {
    String str = "localhost:2222";
    System.out.printf("%s >> %s%n", str, new HexStringCoder().encode(str));
  }
  
  @Test
  public void test_decode_bytebuffer() {
    String str = "Hello World ---";
    ByteBuffer buf = StandardCharsets.UTF_8.encode(str);
    System.out.printf("* %s >> %s%n", str, buf);
    str = StandardCharsets.UTF_8.decode(buf).toString();
    System.out.printf("* %s >> %s%n", buf, str);
  }
  
}
