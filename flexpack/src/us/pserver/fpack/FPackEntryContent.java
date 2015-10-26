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

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;
import us.pserver.cdr.crypt.CryptUtils;
import us.pserver.cdr.lzma.LzmaStreamFactory;
import us.pserver.streams.ProtectedOutputStream;
import us.pserver.valid.Valid;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 */
public class FPackEntryContent {
  
  
  private final FPackEntry entry;
  
  private final InputStream input;
  
  
  public FPackEntryContent(FPackEntry ent, InputStream in) {
    entry = Valid.off(ent).forNull()
        .getOrFail(FPackEntry.class);
    input = Valid.off(in).forNull()
        .getOrFail(InputStream.class);
  }


  public FPackEntry getEntry() {
    return entry;
  }


  public InputStream getInput() {
    return input;
  }
  
  
  protected void write(OutputStream out) throws IOException {
    Valid.off(out).forNull()
        .fail(OutputStream.class);
    int read = 0;
    byte[] buff = new byte[FPackUtils.BUFFER_SIZE];
    while(true) {
      read = input.read(buff);
      if(read == -1) break;
      out.write(buff, 0, read);
    }
    out.flush();
    input.close();
  }
  
  
}
