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
import java.io.OutputStream;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 20/06/2014
 */
public class HexOutputStream extends OutputStream {

  private OutputStream out;
  
  private HexByteCoder hex;
  
  private byte[] bin;
  
  
  public HexOutputStream(OutputStream out) {
    if(out == null)
      throw new IllegalArgumentException(
          "Invalid OutputStream [out="+ out+ "]");
    this.out = out;
    hex = new HexByteCoder();
    bin = new byte[1];
  }


  @Override
  public void write(int b) throws IOException {
    bin[0] = (byte) b;
    out.write(hex.encode(bin));
  }
  
  
  @Override
  public void close() throws IOException {
    out.flush();
    out.close();
  }
  
}
