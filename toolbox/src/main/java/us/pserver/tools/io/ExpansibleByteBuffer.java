/*
 * Direitos Autorais Reservados (c) 2011 Juno Roesler
 * Contato: juno.rr@gmail.com
 * 
 * Esta biblioteca é software livre; você pode redistribuí-la e/ou modificá-la sob os
 * termos da Licença Pública Geral Menor do GNU conforme publicada pela Free
 * Software Foundation; tanto a versão 2.1 da Licença, ou qualquer
 * versão posterior.
 * 
 * Esta biblioteca é distribuída na expectativa de que seja útil, porém, SEM
 * NENHUMA GARANTIA; nem mesmo a garantia implícita de COMERCIABILIDADE
 * OU ADEQUAÇÃO A UMA FINALIDADE ESPECÍFICA. Consulte a Licença Pública
 * Geral Menor do GNU para mais detalhes.
 * 
 * Você deve ter recebido uma cópia da Licença Pública Geral Menor do GNU junto
 * com esta biblioteca; se não, acesse 
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html, 
 * ou escreva para a Free Software Foundation, Inc., no
 * endereço 59 Temple Street, Suite 330, Boston, MA 02111-1307 USA.
 */

package us.pserver.tools.io;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.IntFunction;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 02/12/2018
 */
public class ExpansibleByteBuffer implements Comparable<ByteBuffer> {
  
  public static enum AllocPolicy {
    
    HEAP_ALLOC_POLICY(ByteBuffer::allocate),
    
    DIRECT_ALLOC_POLICY(ByteBuffer::allocateDirect);
    
    private AllocPolicy(IntFunction<ByteBuffer> allocPolicy) {
      this.allocPolicy = allocPolicy;
    }
    
    private final IntFunction<ByteBuffer> allocPolicy;
    
    public ByteBuffer allocate(int size) {
      return allocPolicy.apply(size);
    }
    
  }
  
  
  private final AllocPolicy allocPolicy;
  
  private final float expandFactor;
  
  private final ArrayList<ByteBuffer> buffers;
  
  private volatile int index, mark, position, limit, capacity;
  
  
  public ExpansibleByteBuffer(AllocPolicy allocPolicy, int initialCapacity, float expandFactor) {
    this.allocPolicy = Objects.requireNonNull(allocPolicy);
    if(initialCapacity <= 0 || expandFactor <= 0) {
      throw new IllegalArgumentException("initialCapacity/expandFactor must be greater than 0");
    }
    this.expandFactor = expandFactor;
    buffers = new ArrayList<>();
    index = 0;
    mark = 0;
    position = 0;
    capacity = initialCapacity;
    limit = capacity;
    buffers.add(allocPolicy.allocate(capacity));
  }
  

  public boolean hasArray() {
    return allocPolicy == AllocPolicy.HEAP_ALLOC_POLICY;
  }


  public Set<byte[]> array() {
    if(allocPolicy == AllocPolicy.DIRECT_ALLOC_POLICY) {
      throw new UnsupportedOperationException("Buffer not backed by array");
    }
    HashSet<byte[]> set = new HashSet<>();
    buffers.forEach(b -> set.add(b.array()));
    return Collections.unmodifiableSet(set);
  }


  public boolean isDirect() {
    return allocPolicy == AllocPolicy.DIRECT_ALLOC_POLICY;
  }


  public ByteBuffer slice() {
    retur
  }


  public ByteBuffer duplicate() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }


  public ByteBuffer asReadOnlyBuffer() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }


  public byte get() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }


  public ByteBuffer put(byte b) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }


  public byte get(int index) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }


  public ByteBuffer put(int index, byte b) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }


  public ByteBuffer compact() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }


  byte _get(int i) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }


  void _put(int i, byte b) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }


  public char getChar() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }


  public ByteBuffer putChar(char value) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }


  public char getChar(int index) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }


  public ByteBuffer putChar(int index, char value) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }


  public short getShort() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }


  public ByteBuffer putShort(short value) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }


  public short getShort(int index) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }


  public ByteBuffer putShort(int index, short value) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }


  public int getInt() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }


  public ByteBuffer putInt(int value) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }


  public int getInt(int index) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }


  public ByteBuffer putInt(int index, int value) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }


  public long getLong() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }


  public ByteBuffer putLong(long value) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }


  public long getLong(int index) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }


  public ByteBuffer putLong(int index, long value) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }


  public float getFloat() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }


  public ByteBuffer putFloat(float value) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }


  public float getFloat(int index) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }


  public ByteBuffer putFloat(int index, float value) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }


  public double getDouble() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }


  public ByteBuffer putDouble(double value) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }


  public double getDouble(int index) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }


  public ByteBuffer putDouble(int index, double value) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }


  @Override
  public int compareTo(ByteBuffer o) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

}
