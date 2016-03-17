package us.pserver.zeromap.mapper;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.List;
import us.pserver.tools.rfl.Reflector;
import us.pserver.zeromap.Mapper;
import us.pserver.zeromap.MapperFactory;
import us.pserver.zeromap.Node;
import us.pserver.zeromap.ObjectBuilder;
import us.pserver.zeromap.impl.ONode;



/**
 *
 * @author juno
 */
public class ObjectMapper implements Mapper {
	
	private final List<String> ignored;
	
	
	public ObjectMapper() {
		this(null);
	}
	
	
	public ObjectMapper(List<String> ignored) {
		this.ignored = (ignored != null 
				? ignored : Collections.EMPTY_LIST
		);
	}
	
	
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
				Reflector ref = Reflector.of(t);
				Field[] fs = ref.fields();
				n = (parent != null ? parent 
						: new ONode(t.getClass().getName()));
				for(Field f : fs) {
					if(isIgnored(f)) continue;
					Mapper mp = MapperFactory.factory().mapper(f.getType());
					n.add(mp.map(
							ref.setField(f.getName()).get(), 
							new ONode(f.getName()))
					);
				}
			}
		}
		return n;
	}
	
	
	private boolean isIgnored(Field f) {
		return f == null
				|| Modifier.isStatic(f.getModifiers())
				|| Modifier.isTransient(f.getModifiers())
				|| ignored.contains(f.getName())
				|| ignored.contains(f.getType().getName());
	}
	
	
	private Object build(Class cls, Node node) {
		try {
			return ObjectBuilder.defaultBuilder().create(cls, node);
		} catch(ReflectiveOperationException e) {
			throw new RuntimeException(e);
		}
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
				o = build(cls, n);
				Reflector ref = Reflector.of(o);
				Field[] fs = ref.fields();
				for(Field f : fs) {
					if(isIgnored(f)) continue;
					Node nc = n.findAny(f.getName());
					if(nc == null) continue;
					Class fclass = f.getType();
					Mapper mp = MapperFactory.factory().mapper(fclass);
					ref.setField(f.getName()).set(mp.unmap(nc.firstChild(), fclass));
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
