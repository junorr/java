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

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.Iterator;
import java.util.stream.Stream;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 05/12/2018
 */
public class DefaultBinArray implements BinaryArray {
  
  private final ByteBuffer buffer;
  
  private final int size;
  
  
  public BinArray(ByteBuffer buf) {
    this.buffer = buf;
    size = buffer.getInt();
  }
  
  
  public int size() {
    return size;
  }
  
  
  public BinaryForm get(int idx) {
    return null;
  }
  
  
  public boolean isEmpty() {
    return size() < 1;
  }
  
  
  public boolean contains(BinaryForm bf) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
  
  
  public Iterator<BinaryForm> iterator() {
    return null;
  }
  
  
  public Stream<BinaryForm> stream() {
    return null;
  }
  
  
  public BinaryForm[] toArray() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
  

  @Override
  public String sha256sum() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }


  @Override
  public ByteBuffer toByteBuffer() {
    return buffer;
  }


  @Override
  public byte[] toByteArray() {
    if(size < 1) return new byte[0];
    byte[] bs = new byte[buffer.capacity()];
    buffer.position(0);
    buffer.get(bs);
    return bs;
  }


  @Override
  public int writeTo(ByteBuffer buf) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }


  @Override
  public int writeTo(DynamicByteBuffer buf) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }


  @Override
  public int writeTo(WritableByteChannel chl) throws IOException {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }


  @Override
  public int readFrom(ReadableByteChannel chl) throws IOException {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }


  @Override
  public int readFrom(ByteBuffer buf) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }


  @Override
  public int readFrom(DynamicByteBuffer buf) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

}
