/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.tools.test;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntSupplier;
import java.util.function.Supplier;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import us.pserver.tools.Reflect;
import us.pserver.tools.Timer;


/**
 *
 * @author juno
 */
public class TestReflect {
  
  @Test
  public void test_default_constructor() {
    System.out.println("==========================================");
    System.out.println("  public void test_default_constructor()");
    System.out.println("------------------------------------------");
    ReflectTarget tgt = Reflect.of(ReflectTarget.class).create();
    Assertions.assertNotNull(tgt);
  }
  
  @Test
  public void test_string_constructor() {
    System.out.println("=========================================");
    System.out.println("  public void test_string_constructor()");
    System.out.println("-----------------------------------------");
    ReflectTarget tgt = Reflect.of(ReflectTarget.class).create("Juno");
    Assertions.assertNotNull(tgt);
  }
  
  @Test
  public void test_greet_method() {
    System.out.println("===================================");
    System.out.println("  public void test_greet_method()");
    System.out.println("-----------------------------------");
    Reflect rfl = Reflect.of(ReflectTarget.class).reflectCreate("Juno");
    Assertions.assertEquals("Hello Juno!", rfl.selectMethod("greet").invoke());
  }
  
  @Test
  public void test_greet_string_method() {
    System.out.println("==========================================");
    System.out.println("  public void test_greet_string_method()");
    System.out.println("------------------------------------------");
    Reflect rfl = Reflect.of(ReflectTarget.class).createReflect();
    Assertions.assertEquals("Hello Juno!", rfl.selectMethod("greet", String.class).invoke("Juno"));
  }
  
  @Test
  public void test_withHello_method() {
    System.out.println("=======================================");
    System.out.println("  public void test_withHello_method()");
    System.out.println("---------------------------------------");
    Reflect rfl = Reflect.of(ReflectTarget.class).createReflect();
    Assertions.assertEquals("Hello Juno!", Reflect.of(rfl.selectMethod("withHello").invoke("Juno")).selectMethod("greet").invoke());
    Assertions.assertEquals("Hello Juno!", Reflect.of(rfl.invokeMethod("withHello", "Juno")).invokeMethod("greet"));
  }
  
  @Test
  public void test_magic_field() {
    System.out.println("==================================");
    System.out.println("  public void test_magic_field()");
    System.out.println("----------------------------------");
    Reflect rfl = Reflect.of(ReflectTarget.class).createReflect().selectField("magic").set(43);
    Assertions.assertEquals(43, rfl.get());
  }
  
  @Test
  public void test_method_supplier() {
    System.out.println("=====================================");
    System.out.println("  public void test_method_supplier()");
    System.out.println("-------------------------------------");
    try {
      Reflect r = Reflect.of(ReflectTarget.class).reflectCreate("Juno");
      Supplier<String> greet = r.selectMethod("greet").methodAsSupplier();
      Assertions.assertEquals("Hello Juno!", greet.get());
    }
    catch(Exception e) {
      e.printStackTrace();
      throw e;
    }
  }
  
  @Test
  public void test_method_consumer() {
    System.out.println("=====================================");
    System.out.println("  public void test_method_consumer()");
    System.out.println("-------------------------------------");
    try {
      Reflect r = Reflect.of(ReflectTarget.class).reflectCreate();
      Consumer<String> greet = r.selectMethod("printGreet", String.class).methodAsConsumer();
      greet.accept("Juno");
    }
    catch(Exception e) {
      e.printStackTrace();
      throw e;
    }
  }
  
  @Test
  public void test_method_function() {
    System.out.println("=====================================");
    System.out.println("  public void test_method_function()");
    System.out.println("-------------------------------------");
    try {
      Reflect r = Reflect.of(ReflectTarget.class).reflectCreate();
      Function<String,String> greet = r.selectMethod("greet", String.class).methodAsFunction();
      Assertions.assertEquals("Hello Juno!", greet.apply("Juno"));
    }
    catch(Exception e) {
      e.printStackTrace();
      throw e;
    }
  }
  
  @Test
  public void test_method_lambda() {
    System.out.println("===================================");
    System.out.println("  public void test_method_lambda()");
    System.out.println("-----------------------------------");
    try {
      Reflect<ReflectTarget> r = Reflect.of(ReflectTarget.class).reflectCreate();
      Function<String,String> greet = r.selectMethod("greet", String.class).methodAsLambda(Function.class);
      Assertions.assertEquals("Hello Juno!", greet.apply("Juno"));
    }
    catch(Exception e) {
      e.printStackTrace();
      throw e;
    }
  }
  
  @Test
  public void test_method_bi_function() {
    System.out.println("=====================================");
    System.out.println("  public void test_method_bi_function()");
    System.out.println("-------------------------------------");
    try {
      Reflect<ReflectTarget> r = Reflect.of(ReflectTarget.class).reflectCreate();
      BiFunction<String,String,String> greet = r.selectMethod("greet", String.class, String.class).methodAsBiFunction();
      Assertions.assertEquals("Hello Juno Roesler!", greet.apply("Juno", "Roesler"));
    }
    catch(Exception e) {
      e.printStackTrace();
      throw e;
    }
  }
  
  @Test
  public void test_method_runnable() {
    System.out.println("=====================================");
    System.out.println("  public void test_method_runnable()");
    System.out.println("-------------------------------------");
    try {
      Reflect<ReflectTarget> r = Reflect.of(ReflectTarget.class).reflectCreate("Juno");
      Runnable print = r.selectMethod("printGreet").methodAsRunnable();
      print.run();
    }
    catch(Exception e) {
      e.printStackTrace();
      throw e;
    }
  }
  
  
  @RepeatedTest(3)
  public void test_method_function_performance() {
    System.out.println("=================================================");
    System.out.println("  public void test_method_function_performance()");
    System.out.println("-------------------------------------------------");
    try {
      Reflect<ReflectTarget> r = Reflect.of(ReflectTarget.class).reflectCreate().selectMethod("greet", String.class);
      ReflectTarget target = (ReflectTarget) r.getTarget();
      Function<String,String> greet = r.methodAsFunction();
      BiFunction<ReflectTarget,String,String> dynamicGreet = r.dynamicFunctionMethod();
      BiFunction<ReflectTarget,String,String> dynamicLambda = r.dynamicLambdaMethod(BiFunction.class);
      int TIMES = 100_000_000;
      Timer tm = new Timer.Nanos().start();
      int count = 0;
      for(int i = 0; i < TIMES; i++) {
        count++;
        Assertions.assertEquals("Hello Juno!", target.greet("Juno"));
      }
      Assertions.assertEquals(TIMES, count);
      tm.stop();
      System.out.println(" * direct invoke: " + tm);
      tm = new Timer.Nanos().start();
      count = 0;
      for(int i = 0; i < TIMES; i++) {
        count++;
        Assertions.assertEquals("Hello Juno!", greet.apply("Juno"));
      }
      Assertions.assertEquals(TIMES, count);
      tm.stop();
      System.out.println(" * lambda invoke: " + tm);
      tm = new Timer.Nanos().start();
      count = 0;
      for(int i = 0; i < TIMES; i++) {
        count++;
        Assertions.assertEquals("Hello Juno!", dynamicGreet.apply(target, "Juno"));
      }
      Assertions.assertEquals(TIMES, count);
      tm.stop();
      System.out.println(" * dynamic invoke: " + tm);
      tm = new Timer.Nanos().start();
      count = 0;
      for(int i = 0; i < TIMES; i++) {
        count++;
        Assertions.assertEquals("Hello Juno!", dynamicLambda.apply(target, "Juno"));
      }
      Assertions.assertEquals(TIMES, count);
      tm.stop();
      System.out.println(" * dynamic lambda: " + tm);
      tm = new Timer.Nanos().start();
      count = 0;
      for(int i = 0; i < TIMES; i++) {
        count++;
        Assertions.assertEquals("Hello Juno!", r.invoke("Juno"));
      }
      Assertions.assertEquals(TIMES, count);
      tm.stop();
      System.out.println(" * reflect invoke: " + tm);
    }
    catch(Exception e) {
      e.printStackTrace();
      throw e;
    }
  }
  
  
  @RepeatedTest(3)
  public void test_method_supplier_performance() {
    System.out.println("=================================================");
    System.out.println("  public void test_method_supplier_performance()");
    System.out.println("-------------------------------------------------");
    try {
      Reflect<ReflectTarget> r = Reflect.of(ReflectTarget.class).reflectCreate("Juno").selectMethod("greet");
      ReflectTarget target = (ReflectTarget) r.getTarget();
      Supplier<String> greet = r.methodAsSupplier();
      Function<ReflectTarget,String> dynamicGreet = r.dynamicSupplierMethod();
      int TIMES = 100_000_000;
      Timer tm = new Timer.Nanos().start();
      int count = 0;
      for(int i = 0; i < TIMES; i++) {
        count++;
        Assertions.assertEquals("Hello Juno!", target.greet());
      }
      Assertions.assertEquals(TIMES, count);
      tm.stop();
      System.out.println(" * direct invoke: " + tm);
      tm = new Timer.Nanos().start();
      count = 0;
      for(int i = 0; i < TIMES; i++) {
        count++;
        Assertions.assertEquals("Hello Juno!", greet.get());
      }
      Assertions.assertEquals(TIMES, count);
      tm.stop();
      System.out.println(" * lambda invoke: " + tm);
      tm = new Timer.Nanos().start();
      count = 0;
      for(int i = 0; i < TIMES; i++) {
        count++;
        Assertions.assertEquals("Hello Juno!", dynamicGreet.apply(target));
      }
      Assertions.assertEquals(TIMES, count);
      tm.stop();
      System.out.println(" * dynamic invoke: " + tm);
      tm = new Timer.Nanos().start();
      count = 0;
      for(int i = 0; i < TIMES; i++) {
        count++;
        Assertions.assertEquals("Hello Juno!", r.invoke());
      }
      Assertions.assertEquals(TIMES, count);
      tm.stop();
      System.out.println(" * reflect invoke: " + tm);
    }
    catch(Exception e) {
      e.printStackTrace();
      throw e;
    }
  }
  
  
  @RepeatedTest(3)
  public void test_constructor_supplier_performance() {
    System.out.println("=======================================================");
    System.out.println("  public void test_constructor_supplier_performance()");
    System.out.println("-------------------------------------------------------");
    try {
      Reflect<ReflectTarget> r = Reflect.of(ReflectTarget.class).selectConstructor();
      ReflectTarget rt = new ReflectTarget();
      Supplier<ReflectTarget> cct = r.constructorAsSupplier();
      int TIMES = 100_000_000;
      Timer tm = new Timer.Nanos().start();
      int count = 0;
      for(int i = 0; i < TIMES; i++) {
        count++;
        Assertions.assertEquals(rt, new ReflectTarget());
      }
      Assertions.assertEquals(TIMES, count);
      tm.stop();
      System.out.println(" * direct invoke: " + tm);
      tm = new Timer.Nanos().start();
      count = 0;
      for(int i = 0; i < TIMES; i++) {
        count++;
        Assertions.assertEquals(rt, cct.get());
      }
      Assertions.assertEquals(TIMES, count);
      tm.stop();
      System.out.println(" * lambda invoke: " + tm);
      tm = new Timer.Nanos().start();
      count = 0;
      for(int i = 0; i < TIMES; i++) {
        count++;
        Assertions.assertEquals(rt, r.create());
      }
      Assertions.assertEquals(TIMES, count);
      tm.stop();
      System.out.println(" * reflect invoke: " + tm);
    }
    catch(Exception e) {
      e.printStackTrace();
      throw e;
    }
  }
  
  
  private ReflectTarget createReflect(String str) {
    return new ReflectTarget(str);
  }
  
  private ReflectTarget passtrought(ReflectTarget rt) {
    return rt;
  }
  
  
  @RepeatedTest(3)
  public void test_constructor_function_performance() {
    System.out.println("=======================================================");
    System.out.println("  public void test_constructor_function_performance()");
    System.out.println("-------------------------------------------------------");
    try {
      Reflect<ReflectTarget> r = Reflect.of(ReflectTarget.class).selectConstructor(String.class);
      ReflectTarget rt = new ReflectTarget("Juno");
      Function<String,ReflectTarget> cct = r.constructorAsFunction();
      int TIMES = 100_000_000;
      Timer tm = new Timer.Nanos().start();
      int count = 0;
      for(int i = 0; i < TIMES; i++) {
        count++;
        Assertions.assertEquals(rt, passtrought(createReflect("Juno")));
      }
      Assertions.assertEquals(TIMES, count);
      tm.stop();
      System.out.println(" * direct invoke: " + tm);
      tm = new Timer.Nanos().start();
      count = 0;
      for(int i = 0; i < TIMES; i++) {
        count++;
        Assertions.assertEquals(rt, cct.apply("Juno"));
      }
      Assertions.assertEquals(TIMES, count);
      tm.stop();
      System.out.println(" * lambda invoke: " + tm);
      tm = new Timer.Nanos().start();
      count = 0;
      for(int i = 0; i < TIMES; i++) {
        count++;
        Assertions.assertEquals(rt, r.create("Juno"));
      }
      Assertions.assertEquals(TIMES, count);
      tm.stop();
      System.out.println(" * reflect invoke: " + tm);
    }
    catch(Exception e) {
      e.printStackTrace();
      throw e;
    }
  }
  
  
  @RepeatedTest(3)
  public void test_constructor_lambda_performance() {
    System.out.println("=======================================================");
    System.out.println("  public void test_constructor_lambda_performance()");
    System.out.println("-------------------------------------------------------");
    try {
      Reflect<ReflectTarget> r = Reflect.of(ReflectTarget.class).selectConstructor(String.class);
      ReflectTarget rt = new ReflectTarget("Juno");
      Function<String,ReflectTarget> cct = r.constructorAsLambda(Function.class);
      int TIMES = 100_000_000;
      Timer tm = new Timer.Nanos().start();
      int count = 0;
      for(int i = 0; i < TIMES; i++) {
        count++;
        Assertions.assertEquals(rt, passtrought(createReflect("Juno")));
      }
      Assertions.assertEquals(TIMES, count);
      tm.stop();
      System.out.println(" * direct invoke: " + tm);
      tm = new Timer.Nanos().start();
      count = 0;
      for(int i = 0; i < TIMES; i++) {
        count++;
        Assertions.assertEquals(rt, cct.apply("Juno"));
      }
      Assertions.assertEquals(TIMES, count);
      tm.stop();
      System.out.println(" * lambda invoke: " + tm);
      tm = new Timer.Nanos().start();
      count = 0;
      for(int i = 0; i < TIMES; i++) {
        count++;
        Assertions.assertEquals(rt, r.create("Juno"));
      }
      Assertions.assertEquals(TIMES, count);
      tm.stop();
      System.out.println(" * reflect invoke: " + tm);
    }
    catch(Exception e) {
      e.printStackTrace();
      throw e;
    }
  }
  
  
  private boolean assertInt(int a, Integer b) {
    return a == b;
  }
  
  
  @RepeatedTest(3)
  public void test_lambda_autobxing_performance() {
    System.out.println("=================================================");
    System.out.println("  public void test_lambda_autobxing_performance()");
    System.out.println("-------------------------------------------------");
    try {
      Reflect<ReflectTarget> r = Reflect.of(ReflectTarget.class).reflectCreate().selectMethod("hashCode");
      ReflectTarget target = (ReflectTarget) r.getTarget();
      Supplier<Integer> IHashCode = r.methodAsLambda(Supplier.class);
      IntSupplier iHashCode = r.methodAsLambda(IntSupplier.class);
      int hashCode = target.hashCode();
      int TIMES = 100_000_000;
      Timer tm = new Timer.Nanos().start();
      int count = 0;
      for(int i = 0; i < TIMES; i++) {
        count++;
        Assertions.assertEquals(hashCode, target.hashCode());
      }
      Assertions.assertEquals(TIMES, count);
      tm.stop();
      System.out.println(" * direct invoke: " + tm);
      tm = new Timer.Nanos().start();
      count = 0;
      for(int i = 0; i < TIMES; i++) {
        count++;
        assertInt(hashCode, IHashCode.get());
      }
      Assertions.assertEquals(TIMES, count);
      tm.stop();
      System.out.println(" * auto-boxing invoke: " + tm);
      tm = new Timer.Nanos().start();
      count = 0;
      for(int i = 0; i < TIMES; i++) {
        count++;
        Assertions.assertEquals(hashCode, iHashCode.getAsInt());
      }
      Assertions.assertEquals(TIMES, count);
      tm.stop();
      System.out.println(" * int invoke: " + tm);
    }
    catch(Exception e) {
      e.printStackTrace();
      throw e;
    }
  }
  
}
