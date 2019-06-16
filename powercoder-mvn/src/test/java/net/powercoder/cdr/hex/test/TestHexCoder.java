/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.powercoder.cdr.hex.test;

import net.powercoder.cdr.hex.HexStringCoder;
import org.junit.jupiter.api.Test;


/**
 *
 * @author juno
 */
public class TestHexCoder {
  
  @Test
  public void encode_string_to_hex_and_back() {
    HexStringCoder hex = new HexStringCoder();
    String str = "localhost:6060";
    String enc = hex.encode(str);
    System.out.printf("%s >> %s%n", str, enc);
    str = hex.decode(enc);
    System.out.printf("%s >> %s%n", enc, str);
  }
  
}
