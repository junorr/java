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

package us.pserver.dbone.store;

import java.nio.ByteBuffer;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 17/05/2018
 */
public class BytePrinter {
  
  public static final int DEFAULT_BLOCK = 8;
  
  public static final char DEFAULT_SEPARATOR = '|';
  

  private final byte[] bytes;
  
  public BytePrinter(ByteBuffer buf) {
    if(buf.hasArray()) {
      bytes = buf.array();
    }
    else {
      int pos = buf.position();
      bytes = new byte[buf.remaining()];
      buf.get(bytes);
      buf.position(pos);
    }
  }
  
  public BytePrinter(byte[] bs) {
    bytes = bs;
  }
  
  public void print() {
    System.out.println(toString());
  }
  
  public void print(int block, char sep) {
    System.out.println(toString(block, sep));
  }
  
  @Override
  public String toString() {
    return toString(DEFAULT_BLOCK, DEFAULT_SEPARATOR);
  }
  
  public String toString(int block, char sep) {
    StringBuilder sb = new StringBuilder("[");
    for(int i = 0; i < bytes.length; i++) {
      sb.append(bytes[i]);
      if(i < bytes.length -1) {
        if(i > 0 && (i+1) % block == 0) {
          sb.append(" ").append(sep).append(" ");
        }
        else {
          sb.append(" ");
        }
      }
    }
    return sb.append("]").toString();
  }
  
}
