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
public abstract class FPackUtils {

  
  public static final int BLOCK_SIZE = 128;
  
  
  public static final int BUFFER_SIZE = 4096;
  
  
  public static final String ENTRY_END_STRING = "#######\n";
  
  
  public static final byte[] ENTRY_END_BYTES = {35, 35, 35, 35, 35, 35, 35, 10};
  
  
  protected static OutputStream build(FPackEntry entry, OutputStream out) throws IOException {
    Valid.off(out).forNull()
        .fail(OutputStream.class);
    Valid.off(entry).forNull().fail(FPackEntry.class);
    OutputStream eout = null;
    if(!entry.getEncodingList().isEmpty()) {
      eout = new ProtectedOutputStream(out);
    }
    for(int i = 0; i < entry.getEncodingList().size(); i++) {
      FPackEncoding enc = entry.getEncodingList().get(i);
      switch(enc) {
        case CRYPT:
          if(entry.getCryptKey() != null) {
            eout = CryptUtils.createCipherOutputStream(
                new BufferedOutputStream(eout), 
                entry.getCryptKey()
            );
          }
          break;
        case GZIP:
          eout = new GZIPOutputStream(
              new BufferedOutputStream(eout)
          );
          break;
        case LZMA:
          eout = LzmaStreamFactory.createLzmaOutput(
              new BufferedOutputStream(eout)
          );
          break;
      }
    }
    return eout;
  }
  
  
}
