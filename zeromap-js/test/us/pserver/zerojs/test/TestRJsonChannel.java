/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.zerojs.test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import us.pserver.tools.timer.Timer;
import us.pserver.tools.timer.Timer.Nanos;
import us.pserver.zerojs.io.ReadableBufferChannel;
import us.pserver.zerojs.io.ReadableJsonChannel;
import us.pserver.zerojs.io.WritableBufferChannel;
import us.pserver.zerojs.io.WritableJsonChannel;
import us.pserver.zeromap.Mapper;
import us.pserver.zeromap.MapperFactory;
import us.pserver.zeromap.Node;
import us.pserver.zeromap.impl.ONode;



/**
 *
 * @author juno
 */
public class TestRJsonChannel {
	
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
  
  
  public static Node obj2node() {
    A a = new A();
		Mapper mapper = MapperFactory.factory().mapper(A.class);
		return mapper.map(a);
  }
  
  
  public static WritableBufferChannel node2json(Node n) throws IOException {
    WritableBufferChannel wbuf = new WritableBufferChannel();
    WritableJsonChannel wjc = new WritableJsonChannel(wbuf);
    wjc.write(n);
    return wbuf;
  }
  
  
  public static WritableBufferChannel obj2json() throws IOException {
    return node2json(obj2node());
  }
  
  
  public static Node json2node(ReadableBufferChannel ch) throws IOException {
    ReadableJsonChannel rjc = new ReadableJsonChannel(ch);
    Node na = new ONode(A.class.getName());
    rjc.read(na);
    return na;
  }
  
  
  public static A node2obj(Node n) throws IOException {
    Mapper mapper = MapperFactory.factory().mapper(A.class);
    return (A) mapper.unmap(n, A.class);
  }
  
  
  public static A json2obj(ReadableBufferChannel ch) throws IOException {
    return node2obj(json2node(ch));
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
    WritableBufferChannel wbuf = null;
    tm.clear().start();
    for(int i = 0; i < 10; i++) {
      wbuf = obj2json();
      if(i == 9) tm.lapAndStop();
      else tm.lap();
    }
    System.out.println("* Measuring time: "+ tm);
    System.out.println("* JSon: "+ wbuf.toString());
    System.out.println();
    
    System.out.println("* Mapping: Json -> Object...");
    System.out.println("* Warming up 5x...");
    tm.clear().start();
    for(int i = 0; i < 5; i++) {
      a = json2obj(new ReadableBufferChannel(wbuf.getBuffer()));
      if(i == 4) tm.lapAndStop();
      else tm.lap();
    }
    System.out.println("* Warming up time: "+ tm);
    System.out.println("* Measuring 10x...");
    tm.clear().start();
    for(int i = 0; i < 10; i++) {
      a = json2obj(new ReadableBufferChannel(wbuf.getBuffer()));
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
    WritableBufferChannel wbuf = null;
    Timer tm = new Nanos();
    tm.start();
    wbuf = obj2json();
    tm.lap();
    wbuf = obj2json();
    tm.lapAndStop();
    System.out.println("* Time: "+ tm);
    System.out.println("* JSon: "+ wbuf.toString());
    System.out.println();
    
    System.out.println("* Mapping: Json -> Object...");
    tm.clear().start();
    a = json2obj(new ReadableBufferChannel(wbuf.getBuffer()));
    tm.lap();
    a = json2obj(new ReadableBufferChannel(wbuf.getBuffer()));
    tm.lapAndStop();
    System.out.println("* Time: "+ tm);
    System.out.println("* Object: "+ a);
  }
	
  
  public static void process3() throws IOException {
		A a = new A();
		System.out.println("* a: "+ a);
    System.out.println("* Mapping: Object -> Node...");
    WritableBufferChannel wbuf = null;
    Timer tm = new Nanos();
    tm.start();
    Node node = obj2node();
    tm.stop();
    System.out.println("* Time: "+ tm);
    System.out.println("* Mapping: Node -> Json...");
    tm.clear().start();
    wbuf = node2json(node);
    tm.stop();
    System.out.println("* Time: "+ tm);
    System.out.println("* JSon: "+ wbuf.toString());
    System.out.println();
    
    System.out.println("* Mapping: Json -> Node...");
    ReadableBufferChannel rch = new ReadableBufferChannel(wbuf.getBuffer());
    tm.clear().start();
    node = json2node(rch);
    tm.stop();
    System.out.println("* Time: "+ tm);
    System.out.println("* Mapping: Node -> Object...");
    tm.clear().start();
    a = node2obj(node);
    tm.stop();
    System.out.println("* Time: "+ tm);
    System.out.println("* Object: "+ a);
  }
	
  
  public static void main(String[] args) throws IOException {
    process3();
  }
  
}
