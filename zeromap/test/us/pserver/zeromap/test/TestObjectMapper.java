/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.zeromap.test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import us.pserver.zeromap.Mapper;
import us.pserver.zeromap.Node;
import us.pserver.zeromap.mapper.ObjectMapper;



/**
 *
 * @author juno
 */
public class TestObjectMapper {
	
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
		public A() {
			list.add('d');
			list.add('e');
			list.add('f');
		}
		@Override
		public String toString() {
			return "A{" + "str=" + str + ", list=" + list + ", b=" + b + '}';
		}
	}
	
	
	public static void main(String[] args) {
		A a = new A();
		System.out.println("* a: "+ a);
		Mapper mapper = new ObjectMapper();
		Node na = mapper.map(a);
		System.out.println(na);
		a = (A) mapper.unmap(na);
		System.out.println("* a: "+ a);
	}
	
}
