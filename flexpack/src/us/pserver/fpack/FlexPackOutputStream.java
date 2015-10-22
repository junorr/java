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

import com.cedarsoftware.util.io.JsonWriter;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import us.pserver.tools.UTF8String;
import us.pserver.valid.Valid;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 */
public class FlexPackOutputStream extends FilterOutputStream {
  
  
  private FPackEntry entry;
  
  private boolean writed;
  
  private OutputStream encout;
  
  private long size;
  

  public FlexPackOutputStream(OutputStream out) {
    super(out);
    entry = null;
    writed = false;
    encout = null;
    size = 0;
  }
  
  
  public FlexPackOutputStream putEntry(FPackEntry ent) throws IOException {
    if(ent != null) {
      writeFooter();
      entry = ent;
      writed = false;
      if(encout != null) {
        encout.flush();
        encout.close();
        encout = null;
      }
    }
    return this;
  }
  
  
  private void writeFooter() throws IOException {
    if(entry != null && size > 0) {
      int units = (int) (size / FPackUtils.BLOCK_SIZE);
      if(size % FPackUtils.)
      int remaining = (int) (units * FPackUtils.BLOCK_SIZE - size);
      if(remaining < 8) {
        remaining += FPackUtils.BLOCK_SIZE;
      }
      byte[] bs = new byte[remaining];
      System.arraycopy(
          FPackUtils.ENTRY_END_BYTES, 0, bs, 0, 
          FPackUtils.ENTRY_END_BYTES.length
      );
      out.write(bs);
      out.flush();
      size = 0;
    }
  }
  
  
  private void writeEntry() throws IOException {
    if(writed) return;
    if(entry == null) {
      throw new IllegalStateException(
          "Invalid write for no configured entry");
    }
    UTF8String str = new UTF8String(
        JsonWriter.objectToJson(entry)
    );
    byte[] sbs = str.getBytes();
    size = sbs.length;
    out.write(sbs);
    writeFooter();
    encout = FPackUtils.build(entry, out);
  }
  
  
  @Override
  public void write(byte[] bs, int off, int len) throws IOException {
    Valid.off(bs).forNull().fail();
    Valid.off(off).forLesserThan(0).fail();
    Valid.off(off+len).forGreaterThan(bs.length).fail();
    writeEntry();
    if(encout != null) {
      encout.write(bs, off, len);
    }
    else {
      out.write(bs, off, len);
    }
    size += (len - off);
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
