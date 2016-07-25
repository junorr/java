/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.zeromap.test;

import java.nio.file.Path;
import java.nio.file.Paths;
import us.pserver.zeromap.Mapper;
import us.pserver.zeromap.MapperFactory;
import us.pserver.zeromap.Node;



/**
 *
 * @author juno
 */
public class TestPathMapper {
	
	
	public static void main(String[] args) {
		Path fs = Paths.get("/storage");
		System.out.println("* path: "+ fs);
		Mapper map = MapperFactory.factory().mapper(fs.getClass());
		Node n = map.map(fs);
		System.out.println("* map :\n"+ n);
		fs = (Path) map.unmap(n, Path.class);
		System.out.println("* path: "+ fs);
	}
	
}
