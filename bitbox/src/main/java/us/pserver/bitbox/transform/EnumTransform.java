/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.bitbox.transform;

import java.util.Objects;
import java.util.Optional;
import us.pserver.bitbox.BitBoxConfiguration;
import us.pserver.bitbox.BitTransform;
import us.pserver.bitbox.impl.BitBuffer;


/**
 *
 * @author juno
 */
public class EnumTransform<T extends Enum> implements BitTransform<T>{
  
  private final BitBoxConfiguration cfg;
  
  public EnumTransform(BitBoxConfiguration cfg) {
    this.cfg = Objects.requireNonNull(cfg);
  }
  
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
    BitTransform<String> stran = cfg.getTransform(String.class);
    BitTransform<Class> ctran = cfg.getTransform(Class.class);
    Class<T> c = (Class<T>) e.getClass();
    return stran.box(e.name(), buf) + ctran.box(c, buf);
  }


  @Override
  public T unbox(BitBuffer buf) {
    BitTransform<String> stran = cfg.getTransform(String.class);
    BitTransform<Class> ctran = cfg.getTransform(Class.class);
    String name = stran.unbox(buf);
    Class<T> c = (Class<T>) ctran.unbox(buf);
    return (T) Enum.valueOf(c, name);
  }
  
}
