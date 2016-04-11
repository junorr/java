package us.pserver.zeromap.impl;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import us.pserver.tools.rfl.Reflector;
import us.pserver.zeromap.Mapper;
import us.pserver.zeromap.MapperFactory;
import us.pserver.zeromap.Node;
import us.pserver.zeromap.ObjectBuilder;



/**
 *
 * @author juno
 */
public class DefaultObjectBuilder<T> implements ObjectBuilder<T> {
	
	private final Map<Field,Node> params;
	
	
	public DefaultObjectBuilder() {
		params = new LinkedHashMap<>();
	}
	
	
	public String toString(Constructor c) {
		if(c == null) {
			return "#null constructor";
		}
		StringBuilder sb = new StringBuilder()
				.append(c.getName()).append("(");
		Parameter[] ps = c.getParameters();
		for(Parameter p : ps) {
			sb.append(" ")
					.append(p.getType().getSimpleName())
					.append(" ").append(p.getName()).append(",");
		}
		if(sb.toString().endsWith(",")) {
			sb.deleteCharAt(sb.length()-1);
		}
		return sb.append(" )").toString();
	}

	
	public Constructor<T> guessConstructor(Class<T> cls, Node nod) {
		params.clear();
		Reflector ref = Reflector.of(cls);
		Constructor[] cs = ref.constructors();
		Constructor selected = null;
		final AtomicInteger nparams = new AtomicInteger(nod.childs().size());
		while(selected == null && nparams.get() >= 0) {
			Iterator<Constructor> it = Arrays.asList(cs).stream().filter(
					c->c.getParameterCount() == nparams.get()
			).iterator();
			while(it.hasNext()) {
				Constructor c = it.next();
				if(this.matchConstructor(c, nod, ref.fields())) {
					selected = c;
				}
			}
			nparams.decrementAndGet();
		}
		return selected;
	}
	
	
	private boolean matchConstructor(Constructor<T> constructor, Node node, Field[] parameters) {
		Parameter[] ps = constructor.getParameters();
		int count = 0;
		for(Field f : parameters) {
			Node np = node.findChild(f.getName());
			if(np == null) continue;
			Optional<Parameter> op = Arrays.asList(ps).stream().filter(
					p->p.getType().equals(f.getType())
			).findFirst();
			if(op.isPresent()) {
				count++;
				params.put(f, np);
			}
		}
		return count == constructor.getParameterCount();
	}
	
	
	private Object[] initParams() {
		if(params.isEmpty()) return null;
		List list = new LinkedList();
		Iterator<Field> it = params.keySet().iterator();
		while(it.hasNext()) {
			Field f = it.next();
			if(Modifier.isStatic(f.getModifiers())) {
				continue;
			}
			Node n = params.get(f);
			Mapper mp = MapperFactory.factory().mapper(f.getType());
			list.add(mp.unmap(n.firstChild(), f.getType()));
		}
		Object[] os = null;
		if(!list.isEmpty()) {
			os = list.toArray();
		}
		return os;
	}
	
	
  @Override
	public T create(Class<T> cls, Node node) throws ReflectiveOperationException {
		if(cls == null) {
			throw new IllegalArgumentException(
					"Class must be not null"
			);
		}
		if(node == null || !node.hasChilds()) {
			throw new IllegalArgumentException(
					"Node must be not null and not empty"
			);
		}
		Constructor ct = this.guessConstructor(cls, node);
		if(ct == null) {
			throw new ReflectiveOperationException(
					"No valid constructor found"
			);
		}
		try {
			return (T) ct.newInstance(initParams());
		} catch(Exception e) {
			throw new ReflectiveOperationException(e);
		}
	}
	
}
