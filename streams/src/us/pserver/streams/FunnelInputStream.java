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

package us.pserver.streams;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 16/06/2015
 */
public class FunnelInputStream extends CounterInputStream {
  
  private final List<InputStream> streams;
  
  private int index;
  
  
  public FunnelInputStream() {
    super();
    streams = Collections.synchronizedList(new ArrayList<InputStream>());
    index = 0;
  }
  
  
  public FunnelInputStream append(InputStream is) {
    if(is != null) {
      streams.add(is);
    }
    return this;
  }
  
  
  public List<InputStream> listStream() {
    return streams;
  }
  
  
  @Override
  public int read(byte[] bs, int off, int len) throws IOException {
    if(bs == null || bs.length < 1)
      throw new IllegalArgumentException(
          "Invalid byte array: "+ (bs != null ? bs.length : bs));
    if(off < 0 || off+len > bs.length)
      throw new IllegalArgumentException(
          "Invalid off position: "+ off);
    if(len < 1 || off+len > bs.length)
      throw new IllegalArgumentException(
          "Invalid length: "+ len);
        
    if(streams.isEmpty() || index >= streams.size()) {
      return -1;
    }
    
    InputStream is = streams.get(index);
    int read = is.read(bs, off, len);
    if(read < 1) {
      index++;
      return read(bs, off, len);
    }
    off += read;
    len -= read;
    if(len > 0) {
      int nextRead = read(bs, off, len);
      read += (nextRead < 0 ? 0 : nextRead);
    }
    return increment(read);
  }
  
  
  @Override
  public int read(byte[] bs) throws IOException {
    return read(bs, 0, bs.length);
  }
  
  
  @Override
  public int read() throws IOException {
    if(streams.isEmpty() || index >= streams.size())
      return -1;
    byte[] bs = new byte[1];
    int read = read(bs);
    if(read < 1) return -1;
    return bs[0];
  }
  
  
  private void close(InputStream is) {
    if(is != null) {
      try { is.close(); }
      catch(IOException e) {}
    }
  }
  
  
  @Override
  public void close() throws IOException {
    streams.forEach(this::close);
    streams.clear();
  }

}
