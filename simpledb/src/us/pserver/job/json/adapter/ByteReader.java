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

package us.pserver.job.json.adapter;

import java.nio.ByteBuffer;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 07/03/2017
 */
public interface ByteReader extends Seekable {
  
  public int search(byte[] val);

  public byte[] read(int len);
  
  public byte[] readUntil(byte[] val);
  
  
  public static ByteReader of(ByteBuffer buf) {
    return null;
  }
  
  
  
  
  
  public static class DefByteReader implements ByteReader {
    
    private final ByteBuffer buffer;
    
    private int count;
    
    
    public DefByteReader(ByteBuffer buf) {
      if(buf == null || !buf.hasRemaining()) {
        throw new IllegalArgumentException("Bad Null/Empty ByteBuffer");
      }
      this.buffer = buf;
      this.count = 0;
    }
    

    @Override
    public byte[] read(int len) {
      if(len < 1) {
        throw new IllegalArgumentException("Bad Length to Read: "+ len);
      }
      byte[] bs = new byte[Math.min(len, buffer.remaining())];
      buffer.get(bs);
      return bs;
    }


    @Override
    public byte[] readUntil(byte[] val) {
      
    }


    @Override
    public int search(byte[] value) {
      if(buffer == null || buffer.remaining() < value.length) {
        return -1;
      }
      int idx = 0;
      while(idx < value.length && buffer.hasRemaining()) {
        count ++;
        byte b = buffer.get();
        if(b == value[idx]) {
          idx++;
        }
        else idx = 0;
      }
      return (idx != value.length ? -1 : count - idx);
    }


    @Override
    public void seek(int position) {
      if(position < 0 || position > buffer.capacity()) {
        throw new IllegalArgumentException("Bad Position: "+ position);
      }
      
    }

  }
  
}
