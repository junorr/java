/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.zeromap.mapper;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;
import us.pserver.tools.rfl.Reflector;
import us.pserver.zeromap.Mapper;
import us.pserver.zeromap.MapperFactory;
import us.pserver.zeromap.Node;
import us.pserver.zeromap.impl.ClassFactory;
import us.pserver.zeromap.impl.ONode;



/**
 *
 * @author juno
 */
public class ObjectMapper implements Mapper {
	
	@Override
	public Node map(Object t) {
		return map(t, null);
	}


	@Override
	public Node map(Object t, Node parent) {
		Node n = null;
		if(t != null) {
			Mapper m = null;
			try {
				m = MapperFactory.factory().mapper(t.getClass());
			} catch(IllegalArgumentException e) {}
			if(m != null && ObjectMapper.class != m.getClass()) {
				n = m.map(t);
			}
			else {
				Reflector ref = new Reflector();
				Field[] fs = ref.on(t).fields();
				n = (parent != null ? parent 
						: new ONode(t.getClass().getName()));
				for(Field f : fs) {
					if(Modifier.isStatic(f.getModifiers())
							|| Modifier.isTransient(f.getModifiers())) {
						continue;
					}
					Mapper mp = MapperFactory.factory().mapper(f.getType());
					n.add(mp.map(
							ref.field(f.getName()).get(), 
							new ONode(f.getName()))
					);
				}
			}
		}
		return n;
	}


	@Override
	public Object unmap(Node n, Class cls) {
		Object o = null;
		if(n != null) {
			Mapper m = null;
			try {
				m = MapperFactory.factory().mapper(cls);
			} catch(IllegalArgumentException e) {}
			if(m != null && ObjectMapper.class != m.getClass()) {
				o = m.unmap(n, cls);
			}
			else {
				Reflector ref = new Reflector();
				o = ref.on(cls).create();
				Field[] fs = ref.fields();
				for(Field f : fs) {
					Node nc = n.findAny(f.getName());
					if(nc == null) continue;
					Class fclass = f.getType();
					Mapper mp = MapperFactory.factory().mapper(fclass);
					ref.on(o).field(f.getName()).set(mp.unmap(nc.firstChild(), fclass));
				}
			}
		}
		return o;
	}
	
	
	@Override
	public boolean canHandle(Class cls) {
		return cls != null 
				&& Object.class.isAssignableFrom(cls); 
	}

}
