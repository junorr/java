package us.pserver.zeromap;

import us.pserver.zeromap.impl.DefaultObjectBuilder;
import us.pserver.zeromap.impl.ProxyObjectBuilder;


/**
 *
 * @author juno
 */
public interface ObjectBuilder<T> {
	
	public T create(Class<T> cls, Node nod) throws ReflectiveOperationException;
	
	public static <T> ObjectBuilder<T> defaultBuilder() {
		return new DefaultObjectBuilder<>();
	}
	
	public static <T> ObjectBuilder<T> proxyBuilder() {
		return new ProxyObjectBuilder<>();
	}
	
}
