/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.zeromap.test;

import java.util.Arrays;
import us.pserver.zeromap.Mapper;
import us.pserver.zeromap.MapperFactory;
import us.pserver.zeromap.Node;



/**
 *
 * @author juno
 */
public class TestPrimitiveArray {
	
	public static void main(String[] args) {
		int[] bs = new int[10];
		for(int i = 0; i < bs.length; i++) {
			bs[i] = i+1;
		}
		char a = 'a';
		int ia = (int) a;
		a = (char) ia;
		System.out.println("* a="+ia+ "="+ a);
		System.out.println("* array("+ bs.getClass().getName()+ "): "+ Arrays.toString(bs));
		Mapper map = MapperFactory.factory().mapper(bs.getClass());
		Node n = map.map(bs);
		System.out.println("* map  : ");
		System.out.println(n);
		bs = (int[]) map.unmap(n, int[].class);
		System.out.println("* unmap("+ bs.getClass().getName()+ "): "+ Arrays.toString(bs));
		System.out.println();

		char[] cs = new char[10];
		for(char i = 'a'; i < 'a'+cs.length; i++) {
			cs[i-97] = i;
		}
		System.out.println("* array("+ cs.getClass().getName()+ "): "+ Arrays.toString(cs));
		map = MapperFactory.factory().mapper(cs.getClass());
		n = map.map(cs);
		System.out.println("* map  : ");
		System.out.println(n);
		cs = (char[]) map.unmap(n, char[].class);
		System.out.println("* unmap("+ cs.getClass().getName()+ "): "+ Arrays.toString(cs));
		System.out.println();
	}
	
}
