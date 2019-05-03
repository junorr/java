/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.bitbox;

import java.util.Objects;
import us.pserver.bitbox.impl.BitBuffer;


/**
 *
 * @author juno
 */
public interface Reference {
  
  public static final Reference BAD_REFERENCE = of(Long.MIN_VALUE, Void.class, BitBuffer.of(0, false));
  
  
  public long getId();
  
  public BitBuffer getBuffer();
  
  public Class getType();
  
  
  
  public static Reference of(long id, Class c, BitBuffer buf) {
    return new Reference() {
      @Override
      public long getId() {
        return id;
      }
      @Override
      public BitBuffer getBuffer() {
        return buf;
      }
      @Override
      public Class getType() {
        return c;
      }
      @Override
      public boolean equals(Object o) {
        return o != null
            && Reference.class.isAssignableFrom(o.getClass())
            && getId() == ((Reference)o).getId()
            && getType() == ((Reference)o).getType();
      }
      @Override
      public int hashCode() {
        return Objects.hash(id, getType());
      }
      @Override
      public String toString() {
        return String.format("Reference{id=%d, type=%s}", getId(), getType());
      }
    };
  }
  
}
