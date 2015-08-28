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

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import us.pserver.tools.Valid;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 14/04/2015
 */
public class CryptOutputStream extends OutputStream {
  
  public static final int DEFAULT_BUFFER_SIZE = 4096;
  
  private byte[] buffer;
  
  private int index;
  
  private CryptKey key;
  
  private Cipher encoder;
  
  private BufferedOutputStream output;
  
  private int count = 0;
  
  
  public CryptOutputStream(OutputStream out, CryptKey key, int bufferSize) {
    Valid.off(out).forNull().fail(OutputStream.class);
    Valid.off(key).forNull().fail(CryptKey.class);
    if(bufferSize < 128) 
      bufferSize = DEFAULT_BUFFER_SIZE;
    output = new BufferedOutputStream(out);
    buffer = new byte[bufferSize];
    index = 0;
    this.key = key;
    encoder = CryptUtils.createEncryptCipher(key);
  }
  
  
  public CryptOutputStream(OutputStream out, CryptKey key) {
    this(out, key, 0);
  }


  @Override
  public void write(int b) throws IOException {
    if(!hasRemaining()) {
      flush();
    }
    count++;
    buffer[index++] = (byte) b;
  }
  
  
  @Override
  public void flush() throws IOException {
    if(index == 0) return;
    byte[] enc = encoder.update(buffer, 0, index);
    output.write(enc);
    output.flush();
    index = 0;
  }
  
  
  public int remaining() {
    return buffer.length - index;
  }
  
  
  public boolean hasRemaining() {
    return remaining() > 0;
  }
  
  
  @Override
  public void close() throws IOException {
    finish();
    System.out.println("* CryptOutputStream.count = "+ count);
  }
  
  
  public void finish() throws IOException {
    try {
      byte[] enc;
      if(index == 0) 
        enc = encoder.doFinal();
      else
        enc = encoder.doFinal(buffer, 0, index);
      output.write(enc);
      output.flush();
      output.close();
    } catch(BadPaddingException | IllegalBlockSizeException e) {
      throw new IOException(e.toString(), e);
    } 
  }

}
