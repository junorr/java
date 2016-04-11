package us.pserver.zeromap.test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.LinkedList;
import java.util.List;
import us.pserver.tools.timer.Timer;
import us.pserver.tools.timer.Timer.Nanos;
import us.pserver.zeromap.Mapper;
import us.pserver.zeromap.MapperFactory;
import us.pserver.zeromap.Node;
import us.pserver.zeromap.ObjectBuilder;
import us.pserver.zeromap.impl.DefaultObjectBuilder;
import us.pserver.zeromap.impl.ONode;



/**
 *
 * @author juno
 */
public class TestObjectBuilder {
	
	public static class A {
		final String str;
		int i;
		final List list;
		char c;
		public A(String str, int i) {
			this(str, i, new LinkedList());
		}
		public A(String s, int n, List l) {
			this.str = s;
			this.i = n;
			this.list = l;
		}
		public A(String s, int n, boolean m) {
			this(s, n, new LinkedList(), m);
		}
		public A(String s, int n, List l, boolean b) {
			this(s, n, l);
			c = (b ? 'v' : 'f');
		}
		@Override
		public String toString() {
			return "A{" + "str=" + str + ", i=" + i + ", list=" + list + ", c=" + c + '}';
		}
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
		if(sb.toString().endsWith(",")) {
			sb.deleteCharAt(sb.length()-1);
		}
		System.out.println(sb.append(" )").toString());
	}

	
	public static void main(String[] args) throws ReflectiveOperationException {
		ObjectBuilder<A> of = ObjectBuilder.defaultBuilder();
    DefaultObjectBuilder<A> dob = (DefaultObjectBuilder<A>) of;
		Node na = new ONode(A.class.getName())
				.add("str").add("i").add("list").add("map").add("c");
		Constructor c = dob.guessConstructor(A.class, na);
    printConst(c);
		A a = new A("hello", 1024);
		for(int i = 0; i < 5; i++) {
			a.list.add(i);
		}
		System.out.println("* "+ a);
		Mapper m = MapperFactory.factory().mapper(A.class);
		Node n = m.map(a);
		System.out.println("* "+ n);
		a = null;
    System.out.println("* Creating ObjectProxy...");
    Timer tm = new Nanos().start();
		a = (A) m.unmap(n, A.class);
    tm.lapAndStop();
    System.out.println("* Proxy load time: "+ tm);
    System.out.println("* Calling proxy method...");
    tm.clear().start();
		System.out.println("* "+ a);
    tm.lapAndStop();
    System.out.println("* Effective object creation time: "+ tm);
    System.out.println(a.getClass());
	}
	
}
