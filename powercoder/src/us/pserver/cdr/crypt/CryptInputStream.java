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

package us.pserver.cdr.crypt;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import us.pserver.valid.Valid;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 14/04/2015
 */
public class CryptInputStream extends InputStream {
  
  public static final int DEFAULT_BUFFER_SIZE = 4096;
  
  
  private final ByteBuffer buffer;
  
  private final CryptKey key;
  
  private final Cipher decoder;
  
  private boolean finished;
  
  private final BufferedInputStream input;
  
  private int count = 0;
  
  
  public CryptInputStream(InputStream in, CryptKey key) {
    Valid.off(in).forNull().fail(InputStream.class);
    Valid.off(key).forNull().fail(CryptKey.class);
    input = new BufferedInputStream(in);
    buffer = ByteBuffer.allocate(DEFAULT_BUFFER_SIZE);
    finished = false;
    this.key = key;
    decoder = CryptUtils.createDecryptCipher(key);
  }
  

  @Override
  public int read() throws IOException {
    if(!buffer.hasRemaining())
      encryptRaw();
    if(buffer.hasRemaining()) {
      count++;
      return buffer.get();
    }
    else
      return -1;
  }
  
  
  @Override
  public int read(byte[] bs, int off, int len) throws IOException {
    Valid.off(bs).forEmpty().fail();
    Valid.off(off).forNotBetween(0, bs.length-1);
    Valid.off(len).forNotBetween(1, bs.length-off);
    if(!buffer.hasRemaining()) {
      encryptRaw();
    }
    if(buffer.hasRemaining()) {
      int ilen = Math.min(len, buffer.remaining());
      count += ilen;
      buffer.get(bs, off, ilen);
      return ilen;
    }
    return -1;
  }
  
  
  @Override
  public int read(byte[] bs) throws IOException {
    Valid.off(bs).forEmpty().fail();
    return read(bs, 0, bs.length);
  }
  
  
  private void encryptRaw() throws IOException {
    if(finished) return;
    byte[] temp = new byte[DEFAULT_BUFFER_SIZE];
    int read = input.read(temp, 0, temp.length);
    finished = (read < 1);
    if(finished)
      System.out.println("* CryptInputStream.finished = "+ finished);
    buffer.clear();
    try {
      if(finished)
        buffer.put(decoder.doFinal());
      else
        buffer.put(decoder
            .update(temp, 0, read));
      buffer.flip();
    }
    catch(BadPaddingException | IllegalBlockSizeException e) {
      throw new IOException(e.toString(), e);
    }
  }

  
  @Override
  public void close() throws IOException {
    input.close();
    System.out.println("* CryptInputStream.finished = "+ finished);
    System.out.println("* CryptInputStream.count = "+ count);
  }
  
}
