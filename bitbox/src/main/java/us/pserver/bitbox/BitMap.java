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

package us.pserver.bitbox;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Stream;
import us.pserver.tools.Hash;
import us.pserver.tools.io.DynamicByteBuffer;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 11/12/2018
 */
public interface BitMap<K extends BitBox, V extends BitBox> extends BitBox {
  
  public static final int ID = BitMap.class.getName().hashCode();

  public int length();
  
  public V get(K key);
  
  public boolean isEmpty();
  
  public boolean contains(K key);
  
  public Stream<BitEntry<K,V>> stream();
  
  public Stream<BitEntry<K,V>> parallelStream();
  
  public Iterator<BitEntry<K,V>> iterator();
  
  
  
  
  
  static class BitEntry<K extends BitBox, V extends BitBox> extends AbstractBitBox implements Map.Entry<K,V> {
    
    public static final int ID = BitEntry.class.getName().hashCode();
    
    public BitEntry(ByteBuffer buf) {
      super(buf);
    }
    
    @Override
    public Map.Entry<K,V> get() {
      return this;
    }
    
    @Override
    public String sha256sum() {
      return Hash.sha256().of(toByteArray());
    }
    
    @Override
    public ByteBuffer toByteBuffer() {
      buffer.position(0);
      return buffer.duplicate();
    }
    
    @Override
    public byte[] toByteArray() {
      if(buffer.hasArray() && buffer.array().length == boxSize()) {
        return buffer.array();
      }
      byte[] bs = new byte[boxSize()];
      buffer.position(0);
      buffer.get(bs);
      return bs;
    }
    
    @Override
    public int writeTo(ByteBuffer buf) {
      buffer.position(0);
      buf.put(buffer);
      return boxSize();
    }
    
    @Override
    public int writeTo(WritableByteChannel ch) throws IOException {
      buffer.position(0);
      return ch.write(buffer);
    }
    
    @Override
    public int writeTo(DynamicByteBuffer buf) {
      buffer.position(0);
      buf.put(buffer);
      return boxSize();
    }
    
    @Override
    public K getKey() {
      buffer.position(Integer.BYTES * 2);
      return (K) BitBox.factory().createFrom(buffer);
    }
    
    @Override
    public V getValue() {
      buffer.position(Integer.BYTES * 3);
      int len = buffer.getInt();
      buffer.position(Integer.BYTES * 2 + len);
      return (V) BitBox.factory().createFrom(buffer);
    }
    
    @Override
    public V setValue(V value) {
      throw new UnsupportedOperationException();
    }
    
  }
  
  
  
  
  
  static class BMap<K extends BitBox, V extends BitBox> extends AbstractBitBox implements BitMap<K,V> {
    
    private final int length;
    
    public BMap(ByteBuffer buf) {
      super(buf);
      int id = buffer.getInt();
      if(BitMap.ID != id) {
        throw new IllegalArgumentException("Not a BitMap content");
      }
      buffer.getInt();//boxSize()
      this.length = buffer.getInt();
    }
    
    @Override
    public Map<K,V> get() {
      TreeMap<K,V> map = new TreeMap<>();
      stream().forEach(e -> map.putIfAbsent(e.getKey(), e.getValue()));
      return map;
    }

    @Override
    public String sha256sum() {
      return Hash.sha256().of(toByteArray());
    }


    @Override
    public ByteBuffer toByteBuffer() {
      buffer.position(0);
      return buffer.duplicate();
    }


    @Override
    public byte[] toByteArray() {
      if(buffer.hasArray() && buffer.array().length == boxSize()) {
        return buffer.array();
      }
      byte[] bs = new byte[boxSize()];
      buffer.position(0);
      buffer.get(bs);
      return bs;
    }


    @Override
    public int writeTo(ByteBuffer buf) {
      buffer.position(0);
      buf.put(buffer);
      return boxSize();
    }


    @Override
    public int writeTo(WritableByteChannel ch) throws IOException {
      buffer.position(0);
      return ch.write(buffer);
    }


    @Override
    public int writeTo(DynamicByteBuffer buf) {
      
    }


    @Override
    public int length() {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


    @Override
    public V get(K key) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


    @Override
    public boolean isEmpty() {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


    @Override
    public boolean contains(K key) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


    @Override
    public Stream<BitEntry<K, V>> stream() {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


    @Override
    public Stream<BitEntry<K, V>> parallelStream() {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


    @Override
    public Iterator<BitEntry<K, V>> iterator() {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
  }
  
}
