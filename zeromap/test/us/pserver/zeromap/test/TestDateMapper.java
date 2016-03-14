/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.zeromap.test;

import java.util.Date;
import us.pserver.date.SimpleDate;
import us.pserver.zeromap.Mapper;
import us.pserver.zeromap.Node;
import us.pserver.zeromap.mapper.DateMapper;



/**
 *
 * @author juno
 */
public class TestDateMapper {
	
	
	public static void main(String[] args) {
		Mapper<Date> mp = new DateMapper();
		Date d = new SimpleDate().date(2016, 3, 14, 16, 30, 0);
		System.out.println(d);
		Node nd = mp.map(d);
		System.out.println(nd);
		d = mp.unmap(nd);
		System.out.println(d);
	}
	
}
