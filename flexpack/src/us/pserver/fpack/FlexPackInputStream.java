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

import com.cedarsoftware.util.io.JsonReader;
import java.io.ByteArrayOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import us.pserver.streams.BulkStoppableInputStream;
import us.pserver.streams.PushbackInputStream;
import us.pserver.tools.UTF8String;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 */
public class FlexPackInputStream extends FilterInputStream {

  private PushbackInputStream pin;
  
  private FPackFileHeader filehd;
  
  private boolean headers;
  
  private FPackEntry entry;
  
  
  public FlexPackInputStream(InputStream in) {
    super(in);
    pin = new PushbackInputStream(in);
    try {
      headers = pin.read() == 1;
    } catch(IOException e) {
      throw new RuntimeException(e);
    }
  }
  
  
  private byte[] readNextEntry() throws IOException {
    BulkStoppableInputStream bin = 
        new BulkStoppableInputStream(
            pin, FPackUtils.ENTRY_END_BYTES, null
        );
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    byte[] block = new byte[FPackUtils.BLOCK_SIZE];
    int read = 0;
    while(true) {
      read = bin.read(block);
      if(read == -1) break;
      bos.write(block, 0, read);
    }
    return bos.toByteArray();
  }
  
  
  public FPackEntry getNextEntry() throws IOException {
    byte[] bes = readNextEntry();
    if(entry == null && headers) {
      filehd = (FPackFileHeader) JsonReader.jsonToJava(
          new UTF8String(bes).toString()
      );
    }
  }
  
}
