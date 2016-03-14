/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.zeromap.test;

import us.pserver.zeromap.Mapper;
import us.pserver.zeromap.Node;
import us.pserver.zeromap.mapper.NumberMapper;



/**
 *
 * @author juno
 */
public class TestNumberMapper {
	
	
	public static void main(String[] args) {
		Mapper<Number> mp = new NumberMapper();
		double d = 16.51;
		int i = 200;
		Node nd = mp.map(d);
		System.out.println(nd);
		d = mp.unmap(nd).doubleValue();
		System.out.println(d);
		
		Node ni = mp.map(i);
		System.out.println(ni);
		i = mp.unmap(ni).intValue();
		System.out.println(i);
	}
	
}
