/*
 * Direitos Autorais Reservados (c) 2011 Juno Roesler
 * Contato: juno.rr@gmail.com
 * 
 * Esta biblioteca e um software livre; voce pode redistribui-la e/ou modifica-la sob os
 * termos da Licenca Publica Geral Menor do GNU conforme publicada pela Free
 * Software Foundation; tanto a versao 2.1 da Licenca, ou qualquer
 * versao posterior.
 * 
 * Esta biblioteca eh distribuida na expectativa de que seja util, porem, SEM
 * NENHUMA GARANTIA; nem mesmo a garantia implicita de COMERCIABILIDADE
 * OU ADEQUACAO A UMA FINALIDADE ESPECIFICA. Consulte a Licença Publica
 * Geral Menor do GNU para mais detalhes.
 * 
 * Voce deve ter recebido uma copia da Licença Publica Geral Menor do GNU junto
 * com esta biblioteca; se nao, acesse 
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html, 
 * ou escreva para a Free Software Foundation, Inc., no
 * endereco 59 Temple Street, Suite 330, Boston, MA 02111-1307 USA.
 */

package us.pserver.fpack;

import java.io.IOException;
import java.io.OutputStream;
import us.pserver.tools.UTF8String;
import us.pserver.valid.Valid;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 */
public class FPackFooter {

  
  private final long size;
  
  private static final int delimSize = 8;
  
  private static final UTF8String delimiter = 
      new UTF8String("#######\n");
  
  
  public FPackFooter(long size) {
    this.size = Valid.off(size)
        .forLesserEquals(0).getOrFail() 
        + delimSize;
  }
  
  
  public long getSize() {
    return size;
  }
  
  
  public int getBlockUnits() {
    int units = (int) (size / FPackUtils.BLOCK_SIZE);
    if(size % FPackUtils.BLOCK_SIZE > 0)
      units++;
    System.out.println("* getBlockUnits()="+ units);
    return units;
  }
  
  
  public byte[] alloc() {
    return new byte[
        (int)(getBlockUnits() * FPackUtils.BLOCK_SIZE - size + delimSize)
    ];
  }
  
  
  public void write(OutputStream out) throws IOException {
    Valid.off(out).forNull()
        .fail(OutputStream.class);
    byte[] bs = alloc();
    //System.out.println("* alloc().length="+ bs.length);
    System.arraycopy(delimiter.getBytes(), 0, bs, 0, delimSize);
    out.write(bs);
    out.flush();
  }
  
  
  public static void main(String[] args) {
    
  }
  
}
