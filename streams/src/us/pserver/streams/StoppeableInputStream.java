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

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.function.Consumer;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 19/06/2015
 */
public class StoppeableInputStream extends FilterInputStream {
  
  public static final String DEFAULT_CHARSET = "UTF-8";
  
  
  private String charset;
  
  private InputStream source;
  
  private Consumer<StoppeableInputStream> action;
  
  private byte[] stopFactor;
  
  private LimitedBuffer buffer;
  
  private boolean stopped;
  
  
  public StoppeableInputStream(InputStream src, byte[] stopOn, Consumer<StoppeableInputStream> cs) {
    super(src);
    if(src == null)
      throw new IllegalArgumentException("Invalid null InputStream");
    if(stopOn == null)
      throw new IllegalArgumentException("Invalid null stop string");
    source = src;
    charset = DEFAULT_CHARSET;
    action = cs;
    stopped = false;
    buffer = new LimitedBuffer(stopOn.length);
    stopFactor = stopOn;
  }
  
  
  public String getCharset() {
    return charset;
  }


  public void setCharset(String charset) {
    this.charset = charset;
  }


  public InputStream getSource() {
    return source;
  }


  public void setSource(InputStream source) {
    this.source = source;
  }


  public byte[] getStopFactor() {
    return stopFactor;
  }
  
  
  @Override
  public int read() throws IOException {
    if(stopped) return -1;
    byte[] bs = new byte[1];
    int read = in.read(bs);
    if(read < 1) return read;
    buffer.put(bs[0]);
    if(buffer.size() == stopFactor.length) {
      if(Arrays.equals(stopFactor, buffer.buffer())) {
        if(action != null) action.accept(this);
        stopped = true;
        return -1;
      }
    }
    return buffer.get(0);
  }
  
  
  @Override
  public int read(byte[] bs, int off, int len) throws IOException {
    if(bs == null)
      throw new IllegalArgumentException("Invalid null byte array");
    if(bs.length == 0 || len < 1) return -1;
    if(off < 0 || off + len > bs.length)
      throw new IllegalArgumentException("Invalid arguments: off="+ off+ ", len="+ len);
    
    int count = 0;
    for(int i = off; i < (off+len); i++) {
      int read = this.read();
      if(read == -1) return (count < 1 ? -1 : count);
      count++;
      bs[i] = (byte) read;
    }
    return count;
  }
  
  
  @Override
  public int read(byte[] bs) throws IOException {
    return read(bs, 0, (bs != null ? bs.length : 0));
  }
  
}
