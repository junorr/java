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

package us.pserver.cdr.hex;

import java.io.IOException;
import java.io.InputStream;
import static us.pserver.chk.Checker.nullarray;
import static us.pserver.chk.Checker.range;


/**
 * InputStream para decodificação de dados
 * no formato hexadecimal.
 * 
 * @author Juno Roesler - juno@pserver.us
 * @version 1.0 - 20/06/2014
 */
public class HexInputStream extends InputStream {
  
  private InputStream in;
  
  private byte[] buf;
  
  private HexByteCoder hex;
  
  private long total;
  
  private long available;
  
  private boolean end;
  
  
  /**
   * Construtor padrão que recebe o <code>InputStream</code>
   * de onde serão lidos os dados a serem decodificados.
   * @param in <code>InputStream</code> de onde serão 
   * lidos os dados a serem decodificados.
   */
  public HexInputStream(InputStream in) {
    if(in == null)
      throw new IllegalArgumentException(
      "Invalid InputStream [in="+ in+ "]");
    this.in = in;
    buf = new byte[2];
    hex = new HexByteCoder();
    total = 0;
    end = false;
    try {
      available = in.available();
    } catch(IOException e) {
      available = -1;
    }
  }


  @Override
  public int read() throws IOException {
    for(int i = 0; i < 2; i++) {
      int read = in.read();
      if(read == -1 && available <= 0) {
        end = true;
        return -1;
      }
      buf[i] = (byte) read;
    }
    available -= buf.length;
    total += buf.length;
    return hex.decode(buf)[0];
  }
  
  
  @Override
  public int read(byte[] ba) throws IOException {
    return read(ba, 0, ba.length);
  }
  
  
  @Override
  public int read(byte[] ba, int off, int length) throws IOException {
    nullarray(ba);
    range(off, 0, ba.length-2);
    range(length, 1, ba.length-off);
    if(end) return -1;
    
    int len = 0;
    for(int i = off; i < length; i++) {
      int read = read();
      if(read == -1 && end) break;
      ba[i] = (byte) read;
      len++;
    }
    return len;
  }
  
  
  /**
   * Retorna a quantidade total de bytes lidos.
   * @return quantidade total de bytes lidos.
   */
  public long getTotalReaded() {
    return total;
  }
  
  
  @Override
  public void close() throws IOException {
    in.close();
  }
  
}
