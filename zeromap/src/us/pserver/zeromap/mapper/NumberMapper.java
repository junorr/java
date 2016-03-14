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

  private final Class type;
  
  
  public NumberMapper() {
    this(null);
  }
  
  
  public NumberMapper(Class<? extends Number> cls) {
    type = (cls == null ? Number.class : cls);
  }
  
  
  public Class getType() {
    return type;
  }
  
  
	@Override
	public Node map(Number t) {
		Node n = null;
		if(t != null) {
			n = new ONode(t.toString());
		}
		return n;
	}


	@Override
	public Number unmap(Node node) {
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
	
}
