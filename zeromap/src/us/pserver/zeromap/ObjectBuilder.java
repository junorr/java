package us.pserver.zeromap;

import us.pserver.zeromap.impl.DefaultObjectBuilder;


/**
 *
 * @author juno
 */
public interface ObjectBuilder<T> {
	
	public T create(Class<T> cls, Node nod) throws ReflectiveOperationException;
	
	public static <T> ObjectBuilder<T> defaultBuilder() {
		return new DefaultObjectBuilder<>();
	}
	
}
