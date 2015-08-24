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

import java.io.InputStream;
import java.util.concurrent.atomic.AtomicLong;
import us.pserver.tools.FileSizeFormatter;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 23/08/2015
 */
public abstract class CounterInputStream extends InputStream {

  private final AtomicLong count;
  
  
  protected CounterInputStream() {
    count = new AtomicLong(0L);
  }
  
  
  public long getCount() {
    return count.get();
  }
  
  
  public String getCountFormatted() {
    return new FileSizeFormatter()
        .format(count.get());
  }
  
  
  protected int increment(int size) {
    count.addAndGet(size);
    return size;
  }
  
}
