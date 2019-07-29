/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.bitbox.test;

import org.junit.jupiter.api.Test;
import org.tinylog.Logger;
import us.pserver.bitbox.BitBoxConfiguration;
import us.pserver.bitbox.transform.ClassTransform;


/**
 *
 * @author juno
 */
public class TestHashCode {
  
  @Test
  public void test_hash_code() {
    ClassTransform ct = new ClassTransform(new BitBoxConfiguration());
    Logger.debug("ClassTransform({}).hashCode() = {}", ct, ct.hashCode());
    ct = new ClassTransform(new BitBoxConfiguration());
    Logger.debug("ClassTransform({}).hashCode() = {}", ct, ct.hashCode());
  }
  
}
