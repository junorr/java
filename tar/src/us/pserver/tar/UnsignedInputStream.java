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

package us.pserver.tar;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 18/06/2015
 */
public class UnsignedInputStream extends FilterInputStream {

  public UnsignedInputStream(InputStream is) {
    super(is);
  }
  
  
  private void toUnsigned(byte[] bs, int off, int len) {
    if(bs == null || bs.length == 0 || len < 1)
      return;
    for(int i = off; i < (off+len); i++) {
      byte b = bs[i];
      if(b < -1) bs[i] = (byte) (b + 256);
    }
  }
  
  
  @Override
  public int read() throws IOException {
    int b = super.read();
    if(b < -1) b += 256;
    return b;
  }
  
  
  @Override
  public int read(byte[] bs) throws IOException {
    if(bs == null) return 0;
    return read(bs, 0, bs.length);
  }
  
  
  @Override
  public int read(byte[] bs, int off, int len) throws IOException {
    int read = super.read(bs, off, len);
    if(read > 0) {
      toUnsigned(bs, off, read);
    }
    return read;
  }
  
}
