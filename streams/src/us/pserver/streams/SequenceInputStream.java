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
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 16/06/2015
 */
public class SequenceInputStream extends InputStream {
  
  private static final Object SYNC = new Object();

  private int count;
  
  private int limit;
  
  
  public SequenceInputStream() {
    count = 0;
    limit = Integer.MAX_VALUE;
  }
  
  
  public SequenceInputStream(int limit) {
    this();
    if(limit > 0) 
      this.limit = limit;
  }
  
  
  public SequenceInputStream(int startValue, int limit) {
    count = startValue;
    this.limit = limit;
  }
  
  
  @Override
  public int available() throws IOException {
    return Math.max(limit - count, 0);
  }
  
  
  @Override
  public void close() throws IOException {
    synchronized(SYNC) {
      limit = 0;
    }
  }


  @Override
  public int read() throws IOException {
    synchronized(SYNC) {
      if(count >= limit) return -1;
      return count++;
    }
  }


  @Override
  public String toString() {
    return "SequenceInputStream{ " + count + " -> " + limit + " }";
  }
  
}
