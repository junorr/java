/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.tools.test;

import org.junit.jupiter.api.Test;
import us.pserver.tools.Indexed;

import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


/**
 *
 * @author juno
 */
public class TestIndexed {
  
  @Test
  public void test_indexed() {
    Function<Integer,Indexed<Integer>> bld = Indexed.builder();
    Function<Integer,Indexed<Integer>> ble = Indexed.builder(10);
    IntStream.range(0, 10)
        .mapToObj(bld::apply)
        .peek(System.out::println)
        .collect(Collectors.toList())
        .forEach(System.out::println);
    IntStream.range(10, 20)
        .mapToObj(ble::apply)
        .peek(System.out::println)
        .collect(Collectors.toList());
  }
  
}
