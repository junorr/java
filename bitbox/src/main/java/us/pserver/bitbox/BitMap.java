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
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
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
  
  public Stream<BEntry<K,V>> stream();
  
  public Stream<BEntry<K,V>> parallelStream();
  
  public Iterator<BEntry<K,V>> iterator();
  
  
  
  
  
  static class BEntry<K extends BitBox, V extends BitBox> extends AbstractBitBox implements Map.Entry<K,V> {
    
    public static final int ID = BEntry.class.getName().hashCode();
    
    public BEntry(ByteBuffer buf) {
      super(buf);
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
    
    public BMap(ByteBuffer buf) {
      super(buf);
    }

    @Override
    public String sha256sum() {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


    @Override
    public ByteBuffer toByteBuffer() {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


    @Override
    public byte[] toByteArray() {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


    @Override
    public int writeTo(ByteBuffer buf) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


    @Override
    public int writeTo(WritableByteChannel ch) throws IOException {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


    @Override
    public int writeTo(DynamicByteBuffer buf) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
    public Stream<BEntry<K, V>> stream() {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


    @Override
    public Stream<BEntry<K, V>> parallelStream() {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


    @Override
    public Iterator<BEntry<K, V>> iterator() {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
  }
  
}
