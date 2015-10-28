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

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import us.pserver.valid.Valid;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 */
public class FlexPackOutputStream extends FilterOutputStream {
  
  
  private FPackEntry entry;
  
  private boolean writed;
  
  private long size, total;
  

  public FlexPackOutputStream(OutputStream out) {
    super(out);
    entry = null;
    writed = false;
    size = 0;
    total = 0;
  }
  
  
  public long getWritedSize() {
    return total;
  }
  
  
  public FlexPackOutputStream putEntry(FPackEntry ent) throws IOException {
    if(entry == null) {
      out.write(0);
    }
    else {
      writeFooter();
    }
    this.entry = ent;
    writed = false;
    return this;
  }
  
  
  @Override
  public void close() throws IOException {
    out.flush();
    out.close();
  }
  
  
  private void writeFooter() throws IOException {
    if(entry != null && size > 0) {
      out.write(FPackConstants.ENTRY_END.getBytes());
      out.flush();
      size = 0;
    }
  }
  
  
  private void writeEntry() throws IOException {
    if(writed) return;
    if(entry == null) {
      throw new IllegalStateException(
          "No configured entry. Write not allowed");
    }
    writed = true;
    int esize = entry.getWriteSize();
    entry.setPosition(total+esize)
        .write(out);
  }
  
  
  @Override
  public void write(byte[] bs, int off, int len) throws IOException {
    Valid.off(bs).forNull().fail();
    Valid.off(off).forLesserThan(0).fail();
    Valid.off(off+len).forGreaterThan(bs.length).fail();
    writeEntry();
    out.write(bs, off, len);
    size += (len - off);
    total += (len - off);
  }

  
  @Override
  public void write(byte[] bs) throws IOException {
    write(Valid.off(bs).forNull().getOrFail(), 0, bs.length);
  }
  
  
  @Override
  public void write(int b) throws IOException {
    write(new byte[]{(byte)b});
  }
  
}
