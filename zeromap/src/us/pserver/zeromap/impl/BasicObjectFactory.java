/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.zeromap.impl;

import java.lang.reflect.Field;
import us.pserver.tools.rfl.Reflector;
import us.pserver.zeromap.Mapper;
import us.pserver.zeromap.MapperFactory;
import us.pserver.zeromap.Node;



/**
 *
 * @author juno
 */
public class BasicObjectFactory extends AbstractObjectFactory {

	@Override
	public Object create(Class cls, Node nod) throws Exception {
		super.create(cls, nod);
		Object o = null;
		Mapper m = null;
		Reflector ref = new Reflector();
		o = ref.on(cls).create();
		Field[] fs = ref.fields();
		for(Field f : fs) {
			Node nc = nod.findAny(f.getName());
			if(nc == null) continue;
			Class fclass = f.getType();
			Mapper mp = MapperFactory.factory().mapper(fclass);
			ref.on(o).field(f.getName()).set(mp.unmap(nc.firstChild(), fclass));
		}
		return o;
	}
	
}
