/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.maxb.test;

import us.pserver.maxb.util.CamelCaseName;



/**
 *
 * @author juno
 */
public class TestCamelCaseName {
	
	
	public static void main(String[] args) {
		String name = "cd_tp_ctu";
		System.out.println("* name..: "+ name);
		System.out.println("* ccName: "+ CamelCaseName.of(name));
	}
	
}
