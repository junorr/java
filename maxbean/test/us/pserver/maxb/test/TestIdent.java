/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.maxb.test;

import us.pserver.maxb.util.Ident;



/**
 *
 * @author juno
 */
public class TestIdent {
	
	
	public static void main(String[] args) {
		Ident id = new Ident('|', 0);
		System.out.println("* ident(0): "+ id.getIdentation());
		System.out.println("* ident(1): "+ id.increment().getIdentation());
		System.out.println("* ident(2): "+ id.increment().getIdentation());
		System.out.println("* ident(3): "+ id.increment().getIdentation());
		System.out.println("* ident(4): "+ id.increment().getIdentation());
	}
	
}
