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

package us.pserver.jose.test;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;
import us.pserver.tools.UTF8String;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 10/03/2017
 */
public interface BufferCollection {

  public BufferCollection put(byte[] bts);
  
  public BufferCollection put(byte[] bts, int off, int len);
  
  public BufferCollection put(ByteBuffer buf);
  
  public BufferCollection putUTF8(String utf8);
  
  public int size();
  
  public long length();
  
  public long getUsedSpace();
  
  public boolean hasRemaining();
  
  public long remaining();
  
  public BufferCollection flip();
  
  public String getUTF8(int len);
  
  public String getUTF8();
  
  public Stream<ByteBuffer> stream();
  
  public Iterator<ByteBuffer> iterator();
  
  
  
  public static BufferCollection create() {
    return new DefMultiBuffer();
  }
  
  
  
  
  
  public static class DefMultiBuffer implements BufferCollection {
    
    private final List<ByteBuffer> buffers;
    
    
    public DefMultiBuffer() {
      this.buffers = new ArrayList<>();
    }
    
    
    @Override
    public BufferCollection put(byte[] bts) {
      return put(bts, 0, bts != null ? bts.length : 0);
    }


    @Override
    public BufferCollection put(byte[] bts, int off, int len) {
      if(bts != null && off >= 0 && len > 0 
          && (off+len) <= bts.length) {
        ByteBuffer bb = ByteBuffer.allocate(len);
        bb.put(bts, off, len);
        this.buffers.add(bb);
      }
      return this;
    }


    @Override
    public BufferCollection put(ByteBuffer buf) {
      //System.out.println("BufferCollection.put( ByteBuffer ): "+ buf+ " / "+ (buf != null ? buf.remaining() : 0));
      if(buf != null && buf.hasRemaining()) {
        byte[] bb = new byte[buf.remaining()];
        buf.get(bb);
        ByteBuffer nb = ByteBuffer.wrap(bb);
        nb.limit(nb.capacity());
        nb.position(nb.capacity());
        this.buffers.add(nb);
      }
      return this;
    }


    @Override
    public int size() {
      return this.buffers.size();
    }


    @Override
    public long length() {
      return this.buffers.stream().map(b->b.capacity()).reduce(0, (i,c)-> i+c);
    }
    
    
    @Override
    public long getUsedSpace() {
      return this.buffers.stream().map(b->b.position()).reduce(0, (i,p)->i+p);
    }


    @Override
    public BufferCollection putUTF8(String str) {
      return put(StandardCharsets.UTF_8.encode(str));
    }
    
    
    @Override
    public boolean hasRemaining() {
      return remaining() > 0;
    }


    @Override
    public long remaining() {
      return this.buffers.stream().map(b->b.remaining()).reduce(0, (i,r)->i+r);
    }


    @Override
    public Iterator<ByteBuffer> iterator() {
      return this.buffers.iterator();
    }
    
    
    @Override
    public Stream<ByteBuffer> stream() {
      return this.buffers.stream();
    }


    @Override
    public String getUTF8(int len) {
      if(len < 1) return "";
      byte[] bb = new byte[len];
      Iterator<ByteBuffer> it = iterator();
      int off = 0;
      int blen = len;
      while(it.hasNext() || blen > 0) {
        ByteBuffer buf = it.next();
        int rlen = Math.min(blen, buf.remaining());
        buf.get(bb, off, rlen);
        blen -= rlen;
        off += rlen;
      }
      return UTF8String.from(bb).toString();
    }


    @Override
    public String getUTF8() {
      return getUTF8((int) remaining());
    }


    @Override
    public BufferCollection flip() {
      this.buffers.forEach(b->b.flip());
      return this;
    }
    
  }
  
}
