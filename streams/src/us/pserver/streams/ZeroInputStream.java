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

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 30/07/2014
 */
public class ZeroInputStream extends InputStream {

  private long size;
  
  private long count;
  
  
  public ZeroInputStream() {
    this(-1);
  }
  
  
  public ZeroInputStream(int size) {
    this.size = size;
    count = 0;
  }
  
  
  @Override
  public int available() throws IOException {
    return (int) (size > 0 ? size : Integer.MAX_VALUE);
  }
  
  
  @Override
  public boolean markSupported() { return false; }
  
  
  @Override
  public long skip(long n) throws IOException {
    size -= n;
    return 0;
  }
  
  
  public long getCount() {
    return count;
  }


  @Override
  public int read() throws IOException {
    if(size > 0 && count >= size)
      return -1;
    count++;
    return 0;
  }
  
}
