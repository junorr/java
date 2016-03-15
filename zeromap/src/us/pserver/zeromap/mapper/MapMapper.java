/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.zeromap.mapper;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import us.pserver.zeromap.Mapper;
import us.pserver.zeromap.MapperFactory;
import us.pserver.zeromap.Node;
import us.pserver.zeromap.impl.ClassFactory;
import us.pserver.zeromap.impl.ONode;



/**
 *
 * @author juno
 */
public class MapMapper implements Mapper<Map> {
	
	@Override
	public Node map(Map t) {
		Node n = null;
		if(t != null && !t.isEmpty()) {
			Iterator it = t.keySet().iterator();
			Mapper map = null;
			while(it.hasNext()) {
				Object key = it.next();
				Object val = t.get(key);
				if(map == null) {
					map = MapperFactory.mapper(val.getClass());
				}
				if(n == null) {
					n = new ONode("map:"+ val.getClass().getName());
				}
				n.newChild(key.toString())
						.add(map.map(val));
			}
		}
		return n;
	}


	@Override
	public Map unmap(Node n) {
		Map m = null;
		if(n != null) {
			m = new LinkedHashMap();
			Iterator<Node> it = n.childs().iterator();
			Mapper mapper = null;
			while(it.hasNext()) {
				Node cur = it.next();
				if(mapper == null) {
					mapper = MapperFactory.mapper(ClassFactory.create(n.value()));
				}
				m.put(cur.value(), mapper.unmap(cur.firstChild()));
			}
		}
		return m;
	}
	
}
