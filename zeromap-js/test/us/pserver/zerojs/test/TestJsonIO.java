/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.zerojs.test;

import com.cedarsoftware.util.io.JsonReader;
import com.cedarsoftware.util.io.JsonWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import us.pserver.tools.timer.Timer;
import us.pserver.tools.timer.Timer.Nanos;



/**
 *
 * @author juno
 */
public class TestJsonIO {
	
	public static class B {
		String str = "abc";
		int[] iarray = {1, 2, 3};
		boolean bool = true;
		@Override
		public String toString() {
			return "B{" + "str=" + str + ", iarray=" + Arrays.toString(iarray) + ", bool=" + bool + '}';
		}
	}
	
	public static class A {
		String str = "def";
		List<Character> list = new LinkedList<>();
		B b = new B();
		Map chars = new LinkedHashMap();
		public A() {
			list.add('d');
			list.add('e');
			list.add('f');
			chars.put("a", 97);
			chars.put("b", 98);
			chars.put("c", 99);
			chars.put("d", 100);
			chars.put("e", 101);
			chars.put("f", 102);
		}
		@Override
		public String toString() {
			return "A{" + "str=" + str + ", list=" + list + ", b=" + b + ", chars=" + chars + '}';
		}
	}
  
  
  public static String obj2json() throws IOException {
    A a = new A();
    return JsonWriter.objectToJson(a);
  }
  
  
  public static A json2obj(String json) throws IOException {
    return (A) JsonReader.jsonToJava(json);
  }
  
  
  public static void process() throws IOException {
		A a = new A();
		System.out.println("* a: "+ a);
    System.out.println("* Mapping: Object -> Json...");
    Timer tm = new Nanos();
    System.out.println("* Warming up 5x...");
    tm.start();
    for(int i = 0; i < 5; i++) {
      obj2json();
      if(i == 4) tm.lapAndStop();
      else tm.lap();
    }
    System.out.println("* Warming up time: "+ tm);
    System.out.println("* Measuring 10x...");
    String json = null;
    tm.clear().start();
    for(int i = 0; i < 10; i++) {
      json = obj2json();
      if(i == 9) tm.lapAndStop();
      else tm.lap();
    }
    System.out.println("* Measuring time: "+ tm);
    System.out.println("* JSon: "+ json);
    System.out.println();
    
    System.out.println("* Mapping: Json -> Object...");
    System.out.println("* Warming up 5x...");
    tm.clear().start();
    for(int i = 0; i < 5; i++) {
      a = json2obj(json);
      if(i == 4) tm.lapAndStop();
      else tm.lap();
    }
    System.out.println("* Warming up time: "+ tm);
    System.out.println("* Measuring 10x...");
    tm.clear().start();
    for(int i = 0; i < 10; i++) {
      a = json2obj(json);
      if(i == 9) tm.lapAndStop();
      else tm.lap();
    }
    System.out.println("* Measuring time: "+ tm);
    System.out.println("* Object: "+ a);
  }
	
  
  public static void process2() throws IOException {
		A a = new A();
		System.out.println("* a: "+ a);
    System.out.println("* Mapping: Object -> Json...");
    String json = null;
    Timer tm = new Nanos();
    tm.start();
    json = obj2json();
    tm.lap();
    json = obj2json();
    tm.lapAndStop();
    System.out.println("* Time: "+ tm);
    System.out.println("* JSon: "+ json);
    System.out.println();
    
    System.out.println("* Mapping: Json -> Object...");
    tm.clear().start();
    a = json2obj(json);
    tm.lap();
    a = json2obj(json);
    tm.lapAndStop();
    System.out.println("* Time: "+ tm);
    System.out.println("* Object: "+ a);
  }
	
  
  public static void main(String[] args) throws IOException {
    process2();
  }
  
}
