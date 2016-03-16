/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.zeromap.mapper;

import us.pserver.zeromap.Mapper;
import us.pserver.zeromap.Node;
import us.pserver.zeromap.impl.ONode;



/**
 *
 * @author juno
 */
public class NumberMapper implements Mapper<Number> {

	@Override
	public Node map(Number t) {
		Node n = null;
		if(t != null) {
			/*double d = t.doubleValue();
			String s = (d < 10 ? "000" 
					: (d < 100 ? "00" 
					: (d < 1000 ? "0" 
					: ""))) + t.toString();
			n = new ONode(s);*/
			n = new ONode(t.toString());
		}
		return n;
	}


	@Override
	public Number unmap(Node node, Class<? extends Number> type) {
		Number n = null;
		if(node != null) {
			try {
				if(node.value().contains(".")) {
					n = Double.parseDouble(node.value());
				} else {
					n = Long.parseLong(node.value());
				}
        if(byte.class == type
            || Byte.class == type) {
          n = n.byteValue();
        }
        else if(short.class == type
            || Short.class == type) {
          n = n.shortValue();
        }
        else if(int.class == type
            || Integer.class == type) {
          n = n.intValue();
        }
        else if(long.class == type
            || Long.class == type) {
          n = n.longValue();
        }
        else if(float.class == type
            || Float.class == type) {
          n = n.shortValue();
        }
        else if(double.class == type
            || Double.class == type) {
          n = n.shortValue();
        }
			} catch(NumberFormatException e) {}
		}
		return n;
	}
	
	
	@Override
	public boolean canHandle(Class cls) {
		return cls != null 
				&& (Number.class.isAssignableFrom(cls) 
				|| byte.class == cls
				|| short.class == cls
				|| int.class == cls
				|| long.class == cls
				|| float.class == cls
				|| double.class == cls); 
	}

}
