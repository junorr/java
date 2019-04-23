/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.bitbox.transform;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;
import us.pserver.bitbox.BitTransform;
import us.pserver.bitbox.BoxRegistry;
import us.pserver.bitbox.impl.BitBuffer;


/**
 *
 * @author juno
 */
public class EnumTransform<T extends Enum> implements BitTransform<T>{
  
  private final CharSequenceTransform stran = new CharSequenceTransform();
  
  private final ClassTransform ctran = new ClassTransform();
  
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
    Class<T> c = (Class<T>) e.getClass();
    return stran.box(e.name(), buf) + ctran.box(c, buf);
  }


  @Override
  public T unbox(BitBuffer buf) {
    String name = stran.unbox(buf).toString();
    Class<T> c = (Class<T>) ctran.unbox(buf);
    return (T) Enum.valueOf(c, name);
  }
  
}
