/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.bitbox.transform;

import java.util.Optional;
import us.pserver.bitbox.BitBuffer;
import us.pserver.bitbox.BitTransform;


/**
 *
 * @author juno
 */
public class CharArrayTransform implements BitTransform<char[]> {
  
  private final CharSequenceTransform stran = new CharSequenceTransform();
  
  @Override
  public boolean match(Class c) {
    return c.isArray() && c.getComponentType() == char.class;
  }
  
  @Override
  public Optional<Class> serialType() {
    return Optional.empty();
  }
  
  @Override
  public int box(char[] cs, BitBuffer buf) {
    return stran.box(new String(cs), buf);
  }
  
  @Override
  public char[] unbox(BitBuffer buf) {
    return stran.unbox(buf).toString().toCharArray();
  }
  
}
