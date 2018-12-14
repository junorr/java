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

import java.nio.ByteBuffer;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 11/12/2018
 */
public interface BitMap<K extends BitBox, V extends BitBox> extends BitArray<BitMap.BitEntry<K,V>> {
  
  public static final int ID = BitMap.class.getName().hashCode();

  public Optional<V> get(K key);
  
  public Map<K,V> getMap();
  
  public boolean containsKey(K key);
  
  
  
  
  
  static class BitEntry<K extends BitBox, V extends BitBox> extends AbstractBitBox implements Map.Entry<K,V> {
    
    public static final int ID = BitEntry.class.getName().hashCode();
    
    public BitEntry(ByteBuffer buf) {
      super(buf);
    }
    
    @Override
    public V get() {
      return getValue();
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
  
  
  
  
  

  static class BMap<K extends BitBox, V extends BitBox> extends BitArray.BArray<BitMap.BitEntry<K,V>> implements BitMap<K,V> {
    
    public BMap(ByteBuffer buf) {
      super(buf);
    }
    
    @Override
    public Map<K,V> getMap() {
      TreeMap<K,V> map = new TreeMap<>();
      stream(false).forEach(e -> map.putIfAbsent(e.getKey(), e.getValue()));
      return map;
    }
    
    @Override
    public Optional<V> get(K key) {
      return stream(false).filter(e -> e.getKey().equals(key)).map(e -> e.getValue()).findAny();
    }
    
    @Override
    public boolean containsKey(K key) {
      return stream(false).anyMatch(e -> e.getKey().equals(key));
    }
    
  }
  
}
