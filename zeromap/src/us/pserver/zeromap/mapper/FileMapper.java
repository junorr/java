/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.zeromap.mapper;

import java.io.File;
import us.pserver.zeromap.Mapper;
import us.pserver.zeromap.Node;
import us.pserver.zeromap.impl.ONode;



/**
 *
 * @author juno
 */
public class FileMapper implements Mapper<File> {

	@Override
	public Node map(File t) {
		Node n = null;
		if(t != null) {
			n = new ONode(t.getAbsolutePath());
		}
		return n;
	}


	@Override
	public File unmap(Node n) {
		File f = null;
		if(n != null) {
			f = new File(n.value());
		}
		return f;
	}
	
}
