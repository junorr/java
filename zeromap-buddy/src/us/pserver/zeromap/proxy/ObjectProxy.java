package us.pserver.zeromap.proxy;

import java.lang.reflect.Method;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import us.pserver.tools.rfl.Reflector;
import us.pserver.zeromap.MapperFactory;
import us.pserver.zeromap.Node;
import us.pserver.zeromap.mapper.ObjectMapper;



/**
 *
 * @author juno
 */
public class ObjectProxy<T> {
	
	private final Class<T> type;
	
	private final Node node;
	
	private final MapperFactory factory;
	
	private T object;
	
	
	public ObjectProxy(Class<T> cls, Node node) {
		this(cls, node, null);
	}
	
	
	public ObjectProxy(Class<T> cls, Node node, MapperFactory mfac) {
		if(cls == null) {
			throw new IllegalArgumentException("Class must be not null");
		}
		if(node == null) {
			throw new IllegalArgumentException("Node must be not null");
		}
		this.type = cls;
		this.node = node;
		this.object = null;
		this.factory = (mfac != null ? mfac : MapperFactory.factory());
	}
	
	
	@RuntimeType
	public Object intercept(@Origin Method meth, @AllArguments Object ... args) {
		if(object == null) {
      System.out.println("**** ObjectProxy creating real Object ****");
      object = (T) new ObjectMapper(factory).unmap(node, type);
      System.out.println("**** "+ object+ " ****");
		}
		return Reflector.of(object)
        .selectMethod(meth)
        .invoke(args);
	}
	
}