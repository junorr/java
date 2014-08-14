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

package us.pserver.redfs;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import static us.pserver.chk.Checker.nullarg;
import static us.pserver.chk.Checker.range;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 14/08/2014
 */
public class ProgressInputStream extends FilterInputStream {

  private InputStream input;
  
  private IOData data;
  
  private long total;
  
  
  public ProgressInputStream(InputStream is, IOData dt) throws IOException {
    super(is);
    nullarg(InputStream.class, is);
    nullarg(IOData.class, dt);
    input = is;
    data = dt;
    total = 0;
    if(dt.getStartPos() > 0)
      input.skip(dt.getStartPos());
  }
  
  
  public long bytesReaded() {
    return total;
  }
  
  
  public InputStream input() {
    return input;
  }
  
  
  public IOData ioData() {
    return data;
  }
  
  
  @Override
  public int read() throws IOException {
    int r = super.read();
    if(r != -1) {
      total++;
      data.update(r);
    }
    return r;
  }
  
  
  @Override
  public int read(byte[] bs, int offset, int length) throws IOException {
    int r = super.read(bs, offset, length);
    if(r > 0) {
      total += r;
      data.update(r);
    }
    return r;
  }
  
  
  @Override
  public int read(byte[] bs) throws IOException {
    return this.read(bs, 0, bs.length);
  }
  
}
