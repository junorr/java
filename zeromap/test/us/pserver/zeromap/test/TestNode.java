/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.zeromap.test;

import us.pserver.zeromap.Node;
import us.pserver.zeromap.impl.ONode;





/**
 *
 * @author juno
 */
public class TestNode {
	
	
	public static void main(String[] args) {
		Node root = new ONode("root")
				.newChild("a").add("x").add("y")
				.parent().get()
				.newChild("b").add("z")
				.parent().get();
		System.out.println(root);
		System.out.println("* root.find('a'):");
		System.out.println(root.findAny("a"));
		
		System.out.println("* root.find('x'):");
		System.out.println(root.findAny("x"));
		
		System.out.println("* a.find('y'):");
		System.out.println(root.findAny("a").get().findAny("y"));
		
		System.out.println("* a.find('z'):");
		System.out.println(root.findAny("a").get().findAny("z"));
		
		System.out.println("* root.find('b'):");
		System.out.println(root.findAny("b"));
		
		System.out.println("* root.find('z'):");
		System.out.println(root.findAny("z"));
	}
	
}
