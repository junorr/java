/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import static javafx.scene.input.KeyCode.L;
import us.pserver.tools.rfl.Reflector;
import us.pserver.zeromap.Mapper;
import us.pserver.zeromap.MapperFactory;
import us.pserver.zeromap.Node;



/**
 *
 * @author juno
 */
public class ConstructorObjectFactory<T> {
	
	private final Map<Field,Node> params;
	
	
	public ConstructorObjectFactory() {
		params = new LinkedHashMap<>();
	}
	
	
	private Constructor<T> guessConstructor(Class<T> cls, Node nod) {
		params.clear();
		Reflector ref = new Reflector().on(cls);
		Constructor[] cs = ref.getConstructors();
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
			Optional<Parameter> op = Arrays.asList(ps).stream().filter(p->p.getType().equals(f.getType())).findFirst();
			if(op.isPresent()) {
				count++;
				params.put(f, np);
			}
		}
		return count == constructor.getParameterCount();
	}
	
	
	private void setFields(T obj) {
		if(params.isEmpty()) return;
		Reflector ref = new Reflector().on(obj);
		Iterator<Field> it = params.keySet().iterator();
		while(it.hasNext()) {
			Field f = it.next();
			if(Modifier.isFinal(f.getModifiers())
					|| Modifier.isStatic(f.getModifiers())) {
				continue;
			}
			Node n = params.get(f);
			Mapper mp = MapperFactory.factory().mapper(f.getType());
			ref.field(f.getName()).set(mp.unmap(n.firstChild(), f.getType()));
		}
	}
	
	
	private Object[] initParams() {
		if(params.isEmpty()) return null;
		List list = new LinkedList();
		Iterator<Field> it = params.keySet().iterator();
		while(it.hasNext()) {
			Field f = it.next();
			if(Modifier.isFinal(f.getModifiers())
					|| Modifier.isStatic(f.getModifiers())) {
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
		Constructor cn = this.guessConstructor(cls, node);
		if(cn == null) {
			throw new InstantiationException(
					"No valid constructor found"
			);
		}
		try {
			return (T) cn.newInstance(initParams());
		} catch(Exception e) {
			throw new ReflectiveOperationException(e);
		}
	}
	
	
	public static class A {
		String str;
		int i;
		List list;
		Map map;
		char c;
		public A() {}
		public A(String str, int i) {}
		public A(String s, int n, List l) {}
		public A(String s, int n, boolean m) {}
		public A(String s, int n, List l, boolean b) {}
	}
	
	
	public static void printConst(Constructor c) {
		if(c == null) {
			System.out.println("#null constructor");
			return;
		}
		StringBuilder sb = new StringBuilder()
				.append(c.getName()).append("(");
		Parameter[] ps = c.getParameters();
		for(Parameter p : ps) {
			sb.append(" ")
					.append(p.getType().getSimpleName())
					.append(" ").append(p.getName()).append(",");
		}
		System.out.println(sb.append(" )").toString());
	}

	
	public static void main(String[] args) {
		ConstructorObjectFactory of = new ConstructorObjectFactory();
		Node na = new ONode(A.class.getName())
				.add("str").add("i").add("list").add("map").add("c");
		Constructor c = of.guessConstructor(A.class, na);
		printConst(c);
	}
	
}
