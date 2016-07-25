/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.zeromap.test;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import us.pserver.zeromap.Mapper;
import us.pserver.zeromap.Node;
import us.pserver.zeromap.mapper.MapMapper;



/**
 *
 * @author juno
 */
public class TestMapMapper {
	
	public static String mapToString(Map m) {
		if(m == null || m.isEmpty()) return null;
		StringBuilder sb = new StringBuilder("Map{ ");
		Iterator it = m.keySet().iterator();
		while(it.hasNext()) {
			Object key = it.next();
			Object val = m.get(key);
			sb.append("'").append(key)
					.append("':'")
					.append(val).append("', ");
		}
		return sb.append(" }").toString();
	}
	
	
	public static void main(String[] args) {
		Map<String,Integer> map = new LinkedHashMap();
		for(int i = 0; i < 10; i++) {
			map.put(String.valueOf(i), i+1);
		}
		System.out.println("* map  : "+ mapToString(map));
		Mapper<Map> mapper = new MapMapper();
		Node n = mapper.map(map);
    System.out.println("map.size: "+ n.childs().size());
		System.out.println("* node :\n"+n);
    
		map = mapper.unmap(n, LinkedHashMap.class);
		System.out.println("* map  : "+ mapToString(map));
    
	}
	
}
