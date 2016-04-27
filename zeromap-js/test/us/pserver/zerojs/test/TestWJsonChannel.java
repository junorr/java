/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.zerojs.test;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import us.pserver.tools.timer.Timer;
import us.pserver.tools.timer.Timer.Nanos;
import us.pserver.zerojs.io.WritableBufferChannel;
import us.pserver.zerojs.io.WritableJsonChannel;
import us.pserver.zeromap.Mapper;
import us.pserver.zeromap.MapperFactory;
import us.pserver.zeromap.Node;



/**
 *
 * @author juno
 */
public class TestWJsonChannel {
	
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
	
	
	public static void main(String[] args) throws IOException {
		A a = new A();
		System.out.println("* a: "+ a);
		Mapper mapper = MapperFactory.factory().mapper(A.class);
    System.out.println("* Mapping: Object -> Node...");
    Timer tm = new Nanos().start();
		Node na = mapper.map(a);
    System.out.println("* Mapping time: "+ tm.stop());
		System.out.println(na);
    
    WritableBufferChannel buf = new WritableBufferChannel();
    WritableJsonChannel wjc = new WritableJsonChannel(buf);
    System.out.println("* Mapping: Node -> Json...");
    tm.clear().start();
    wjc.write(na);
    System.out.println("* Mapping time: "+ tm.stop());
    System.out.println("* JSon: "+ buf.toString());
	}
	
}
