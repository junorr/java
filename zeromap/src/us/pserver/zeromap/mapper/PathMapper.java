/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.zeromap.mapper;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import us.pserver.zeromap.Mapper;
import us.pserver.zeromap.Node;
import us.pserver.zeromap.impl.ONode;



/**
 *
 * @author juno
 */
public class PathMapper implements Mapper<Path> {

	@Override
	public Node map(Path t) {
		Node n = null;
		if(t != null) {
			n = new ONode(t.toAbsolutePath().toString());
		}
		return n;
	}


	@Override
	public Path unmap(Node n) {
		Path p = null;
		if(n != null) {
			p = Paths.get(n.value());
		}
		return p;
	}
	
}
