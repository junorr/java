/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.zeromap.test;

import java.io.File;
import java.util.Arrays;
import us.pserver.zeromap.Mapper;
import us.pserver.zeromap.MapperFactory;
import us.pserver.zeromap.Node;



/**
 *
 * @author juno
 */
public class TestFileMapper {
	
	
	public static void main(String[] args) {
		File[] fs = new File("/storage").listFiles();
		System.out.println("* files: "+ Arrays.toString(fs));
		Mapper map = MapperFactory.factory().mapper(fs.getClass());
		Node n = map.map(fs);
		System.out.println("* map  :\n"+ n);
		fs = (File[]) map.unmap(n, File.class);
		System.out.println("* files: "+ Arrays.toString(fs));
	}
	
}
