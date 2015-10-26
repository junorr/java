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
import java.io.InputStream;
import java.io.OutputStream;
import us.pserver.tools.UTF8String;
import us.pserver.valid.Valid;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 */
public abstract class FPackUtils {

  
  public static final int BUFFER_SIZE = 4096;
  
  
  public static final UTF8String ENTRY_END = new UTF8String("#######\n");
  
  
  public static long connect(InputStream in, OutputStream out, int bufsize) throws IOException {
    Valid.off(in).forNull().fail(InputStream.class);
    Valid.off(out).forNull().fail(OutputStream.class);
    if(bufsize <= 0) bufsize = BUFFER_SIZE;
    int read = 0;
    byte[] buf = new byte[bufsize];
    long count = 0;
    while(true) {
      read = in.read(buf);
      if(read == -1) break;
      count += read;
	  //System.out.println("\n* connect.readed("+ read+ "): '"+ new UTF8String(buf)+ "'");
      out.write(buf, 0, read);
    }
    out.flush();
    return count;
  }
  
  
}
