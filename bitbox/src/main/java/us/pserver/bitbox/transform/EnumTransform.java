/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.bitbox.transform;

import java.util.Optional;
import us.pserver.bitbox.BitTransform;
import us.pserver.bitbox.BitBoxRegistry;
import us.pserver.bitbox.impl.BitBuffer;


/**
 *
 * @author juno
 */
public class EnumTransform<T extends Enum> implements BitTransform<T>{
  
  @Override
  public boolean match(Class c) {
    return c.isEnum();
  }
  
  @Override
  public Optional<Class> serialType() {
    return Optional.empty();
  }
  
  @Override
  public int box(T e, BitBuffer buf) {
    BitTransform<String> stran = BitBoxRegistry.INSTANCE.getAnyTransform(String.class);
    BitTransform<Class> ctran = BitBoxRegistry.INSTANCE.getAnyTransform(Class.class);
    Class<T> c = (Class<T>) e.getClass();
    return stran.box(e.name(), buf) + ctran.box(c, buf);
  }


  @Override
  public T unbox(BitBuffer buf) {
    BitTransform<String> stran = BitBoxRegistry.INSTANCE.getAnyTransform(String.class);
    BitTransform<Class> ctran = BitBoxRegistry.INSTANCE.getAnyTransform(Class.class);
    String name = stran.unbox(buf);
    Class<T> c = (Class<T>) ctran.unbox(buf);
    return (T) Enum.valueOf(c, name);
  }
  
}
