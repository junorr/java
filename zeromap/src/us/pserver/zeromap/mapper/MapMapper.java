/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.zeromap.mapper;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import us.pserver.zeromap.Mapper;
import us.pserver.zeromap.MapperFactory;
import us.pserver.zeromap.Node;
import us.pserver.zeromap.ObjectMappingException;
import us.pserver.zeromap.impl.ClassFactory;
import us.pserver.zeromap.impl.ONode;



/**
 *
 * @author juno
 */
public class MapMapper implements Mapper<Map> {
  
  public static final String ENTRY_CLASS = "@entry-class";
  
  public static final String ENTRY_VALUE = "@entry-value";
  
	
	@Override
	public Node map(Map t) {
    Node nmap = null;
		if(t != null && !t.isEmpty()) {
			Iterator it = t.keySet().iterator();
			Mapper map = null;
      nmap = new ONode(t.getClass().getName());
			while(it.hasNext()) {
				Object key = it.next();
				Object val = t.get(key);
        Node entry = createEntry(key, val);
        nmap.add(entry);
			}
		}
		return nmap;
	}
  
  
  private Node createEntryOld(Object key, Object val) {
    if(key == null || val == null) {
      return null;
    }
    Mapper mk = MapperFactory.factory().mapper(key.getClass());
    Mapper mv = MapperFactory.factory().mapper(val.getClass());
    Node entry = mk.map(key);
    entry.newChild(ENTRY_CLASS).add(
        key.getClass().getName()+ "|"
            + val.getClass().getName()
    );
    entry.newChild(ENTRY_VALUE).add(mv.map(val));
    return entry;
  }


  private Node createEntry(Object key, Object val) {
    if(key == null || val == null) {
      return null;
    }
    Mapper mk = MapperFactory.factory().mapper(key.getClass());
    Mapper mv = MapperFactory.factory().mapper(val.getClass());
    Node entry = new ONode("entry#"+ String.valueOf(key.hashCode()));
    entry.newChild("class").add(
        key.getClass().getName()+ "|"
            + val.getClass().getName()
    );
    entry.newChild("key").add(mk.map(key));
    entry.newChild("value").add(mv.map(val));
    return entry;
  }


	@Override
	public Map unmap(Node n, Class<? extends Map> cls) {
		Map m = null;
		if(n != null) {
			try {
				Class<? extends Map> mc = ClassFactory.create(n.value());
        m = mc.newInstance();
			} catch(IllegalAccessException | InstantiationException e) {
				throw new ObjectMappingException("Can not create map from class "+ cls.getName(), e);
			}
			Iterator<Node> it = n.childs().iterator();
			while(it.hasNext()) {
				Object[] oentry = unmapEntry(it.next());
				m.put(oentry[0], oentry[1]);
			}
		}
		return m;
	}
  
  
  private Object[] unmapEntry(Node entry) {
    if(entry == null) {
      return null;
    }
    String[] scs = entry.findChild("class").firstChild().value().split("\\|");
    Class ck = ClassFactory.create(scs[0]);
    Class cv = ClassFactory.create(scs[1]);
    Mapper mk = MapperFactory.factory().mapper(ck);
    Mapper mv = MapperFactory.factory().mapper(cv);
    Object key = mk.unmap(entry
        .findChild("key").firstChild(), ck
    );
    Object val = mv.unmap(entry
        .findChild("value").firstChild(), cv
    );
    return new Object[]{key, val};
  }
  
  
	@Override
	public boolean canHandle(Class cls) {
		return cls != null 
				&& Map.class.isAssignableFrom(cls); 
	}

}
