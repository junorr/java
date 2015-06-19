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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import us.pserver.cdr.crypt.CryptKey;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 19/06/2015
 */
public class EncoderInputStream extends FilterInputStream {
  
  public static int BUFFER_SIZE = 4096;

  private StreamCoderFactory sfact;
  
  private OutputStream out;
  
  private ByteArrayOutputStream bos;
  
  private ByteArrayInputStream bis;
  
  private boolean closed;
  
  
  public EncoderInputStream(InputStream src) {
    super(src);
    if(src == null)
      throw new IllegalArgumentException("Invalid null source InputStream");
    sfact = StreamCoderFactory.getNew();
    out = null;
    bos = new ByteArrayOutputStream();
    bis = null;
    closed = false;
  }
  
  
  public EncoderInputStream setBase64CoderEnabled(boolean enabled) {
    sfact.setBase64CoderEnabled(enabled);
    return this;
  }
  
  
  public EncoderInputStream setGZipCoderEnabled(boolean enabled) {
    sfact.setGZipCoderEnabled(enabled);
    return this;
  }
  
  
  public EncoderInputStream setCryptCoderEnabled(boolean enabled, CryptKey key) {
    sfact.setCryptCoderEnabled(enabled, key);
    return this;
  }
  
  
  public boolean isBase64CoderEnabled() {
    return sfact.isBase64CoderEnabled();
  }
  
  
  public boolean isGZipCoderEnabled() {
    return sfact.isGZipCoderEnabled();
  }
  
  
  public boolean isCryptCoderEnabled() {
    return sfact.isCryptCoderEnabled();
  }
  
  
  private void fillBuffer() throws IOException {
    if(closed) return;
    if(out == null) {
      if(sfact.isAnyCoderEnabled())
        out = sfact.create(bos);
      else 
        out = bos;
    }
    bos.reset();
    byte[] bs = new byte[BUFFER_SIZE];
    int read = in.read(bs);
    if(read < 1) {
      closed = true;
      out.close();
      in.close();
    }
    else {
      out.write(bs, 0, read);
      out.flush();
    }
    if(read != bs.length) fillBuffer();
    System.out.println("EncoderInputStream.bos.size()="+ bos.size());
  }
  
  
  @Override
  public int read() throws IOException {
    if(closed) return -1;
    if(bis == null && bos.size() < 1)
      fillBuffer();
    if(bis == null) {
      if(bos.size() > 0) {
        bis = new ByteArrayInputStream(bos.toByteArray());
        bos.reset();
      }
      else return -1;
    }
    int read = bis.read();
    if(read < 1) bis = null;
    return read;
  }
  
  
  @Override
  public int read(byte[] bs, int off, int len) throws IOException {
    if(closed) return -1;
    if(bs == null)
      throw new IllegalArgumentException("Invalid null byte array");
    if(bs.length == 0 || len < 1) return -1;
    if(off < 0 || off + len > bs.length)
      throw new IllegalArgumentException("Invalid arguments: off="+ off+ ", len="+ len);
    
    if(bis == null && bos.size() < 1)
      fillBuffer();
    if(bis == null) {
      if(bos.size() > 0) {
        bis = new ByteArrayInputStream(bos.toByteArray());
        bos.reset();
      }
      else return -1;
    }
    int read = bis.read(bs, off, len);
    if(read < 1) bis = null;
    return read;
  }
  
  
  @Override
  public int read(byte[] bs) throws IOException {
    return read(bs, 0, (bs != null ? bs.length : 0));
  }
  
}
